package com.fss.saber.adapter.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Base64;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fss.saber.adapter.modelm24.BalanceRequest;
import com.fss.saber.adapter.modelm24.BalanceResponse;
import com.fss.saber.adapter.modelm24.FundTransferRequest;
import com.fss.saber.adapter.modelm24.FundTransferResponse;
import com.fss.saber.adapter.modelm24.MResponse;
import com.fss.saber.adapter.modelm24.MiniStatementRequest;
import com.fss.saber.adapter.modelm24.MiniStatementResponse;
import com.fss.saber.adapter.modelm24.ReversalRequest;
import com.fss.saber.adapter.modelm24.ReversalResponse;
import com.fss.saber.adapter.modelm24.TSPResponse;
import com.fss.saber.adapter.modelm24.WithdrawalRequest;
import com.fss.saber.adapter.modelm24.WithdrawalResponse;
import com.fss.saber.adapter.piddata.PidData;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public final class JaxbUtil {

	private static final Logger logger = LoggerFactory.getLogger(JaxbUtil.class);
	
	private static JAXBContext context = null;
	
	static {
		try {
			context = JAXBContext.newInstance(
					MResponse.class, 
					TSPResponse.class, 
					FundTransferRequest.class, 
					FundTransferResponse.class, 
					BalanceRequest.class, 
					BalanceResponse.class, 
					MiniStatementRequest.class, 
					MiniStatementResponse.class,
					WithdrawalRequest.class, 
					WithdrawalResponse.class, 
					ReversalRequest.class, 
					ReversalResponse.class, 
					PidData.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static final String marshal(final Object object) throws JAXBException {
		final StringWriter sw = new StringWriter();
		final Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(object, sw);
		final String request = sw.toString();
		logger.info("request : \r\n"+request);
		return Base64.getEncoder().encodeToString(request.getBytes());
	}
	
//	@SuppressWarnings("unchecked")
//	public static final <T> T unMarshal(final String string) throws JAXBException {
//		final Unmarshaller unmarshaller = context.createUnmarshaller();
//		return (T) unmarshaller.unmarshal(new ByteArrayInputStream(Base64.getDecoder().decode(string)));
//	}
	
	@SuppressWarnings("unchecked")
	public static final <T> T unMarshalPlain(final String string) throws JAXBException {
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(new StringReader(string));
	}
	
	public static final <T> T toResponse(String responseString) throws JAXBException {
		final MResponse mResponse = unMarshalPlain(responseString);
		final String xmlString = new String(Base64.getDecoder().decode(mResponse.data));
		logger.info("response : "+prettyFormat(xmlString, 4));
		return unMarshalPlain(xmlString);
	}
	
	public static final String prettyFormat(String input, int indent) {
	    try {
	        Source xmlInput = new StreamSource(new StringReader(input.replaceAll("\r\n\r\n", "\r\n")));
	        StringWriter stringWriter = new StringWriter();
	        StreamResult xmlOutput = new StreamResult(stringWriter);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", indent);
	        Transformer transformer = transformerFactory.newTransformer(); 
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.transform(xmlInput, xmlOutput);
	        return xmlOutput.getWriter().toString();
	    } catch (Exception e) {	       
	    }
		return input;
	}
}
