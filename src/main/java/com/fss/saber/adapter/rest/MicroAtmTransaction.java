package com.fss.saber.adapter.rest;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.util.datautil.collections.BERTLV;
import org.util.iso8583.ISO8583DateFormat;
import org.util.iso8583.util.ByteHexUtil;
import org.util.iso8583.util.Track2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fss.saber.adapter.jpa.UserAdditionalDetails;
import com.fss.saber.adapter.jpa.UserAthnMstr;
import com.fss.saber.adapter.jws.CSBService;
import com.fss.saber.adapter.jws.CSBServiceSoap;
import com.fss.saber.adapter.jws.LoggingHandler;
import com.fss.saber.adapter.model.RequestParams;
import com.fss.saber.adapter.model.ResponseParams;
import com.fss.saber.adapter.modelm24.BalanceRequest;
import com.fss.saber.adapter.modelm24.BalanceResponse;
import com.fss.saber.adapter.modelm24.Request;
import com.fss.saber.adapter.modelm24.ReversalRequest;
import com.fss.saber.adapter.modelm24.ReversalResponse;
import com.fss.saber.adapter.modelm24.WithdrawalRequest;
import com.fss.saber.adapter.modelm24.WithdrawalResponse;
import com.fss.saber.adapter.repository.AgentRepository;
import com.fss.saber.adapter.util.Generator;
import com.fss.saber.adapter.util.JaxbUtil;
import com.fss.saber.adapter.util.MiscService;
import com.fss.saber.adapter.util.SessionIdGenerator;
import com.sun.xml.ws.client.BindingProviderProperties;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.ws.Binding;
import jakarta.xml.ws.BindingProvider;

@RestController
@RequestMapping(value = "/kiosk/csb")
public class MicroAtmTransaction {

	private static final Logger logger = LoggerFactory.getLogger(MicroAtmTransaction.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private SessionIdGenerator sessionIdGenerator;

	@Value("${merchant.id}")
	public String merchantId;

	@Value("${acq.id}")
	public String acqId;

	@Value("${merchant.pass}")
	public String merchantPass;

	@Value("${session.key}")
	public String sessionKey;

	@Value("${target.zpk}")
	public String targetZPK;

	@PostMapping(path = "/matm/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseParams transaction(@RequestBody RequestParams message) {

		if ("getSdkMainKey".equals(message.getRequestcode())) {
			logger.info("getSdkMainKey request received");
			return MiscService.getMainKeys(message);
		} else if ("validateSdk".equals(message.getRequestcode())) {
			logger.info("validateSdk request received");
			return MiscService.getSessionKeys(message);
		}

		final ResponseParams responseParams = new ResponseParams();
		try {
			try {
				logger.info("message from middleware\r\n" + mapper.writeValueAsString(message));
				// if (!"0420".equals(message.get("MTI")))
				responseParams.setRespCode("91");

				if ("microCashWithdrawSbm".equals(message.getRequestcode())) {
					logger.info("creating withdrawal request");
					final Request request = populateRequest(message, new WithdrawalRequest());
					request.txnAmount = Generator.amountToFormattedString12(new BigDecimal(message.getAmt()).multiply(new BigDecimal(100.0)).toBigInteger());
					;
					request.txnCode = "010000";
					logger.info("sending request to fss");
					final CSBServiceSoap serviceSoap = getClient();
					final String responseString = serviceSoap.transactionRequest(JaxbUtil.marshal(request));
					//final String responseString = "<?xml version='1.0' encoding='UTF-8'?><MRESPONSE><SID></SID><TYPE>0</TYPE><DATA>PD94bWwgdmVyc2lvbj0nMS4wJyBlbmNvZGluZz0nVVRGLTgnID8+CjxXSVRIRFJBV0FMUkVTUE9OU0U+PE1TR19UWVBFPjAyMTA8L01TR19UWVBFPjxDQVJEX05PPjYwNzA1MzIxOTcwMTg1MjA8L0NBUkRfTk8+PFRYTl9DT0RFPjAxMDAwMDwvVFhOX0NPREU+PFRYTl9EQVRFX1RJTUU+MTIyMDAzMTYzOTwvVFhOX0RBVEVfVElNRT48U1RBTj4xODU5NjE8L1NUQU4+PFRJTUU+MDg0NjM5PC9USU1FPjxEQVRFPjEyMjA8L0RBVEU+PEJBVENIX0lEPio8L0JBVENIX0lEPjxBR0VOVF9JRD5GU1MwMDAwMDAwMzY8L0FHRU5UX0lEPjxCQ19OQU1FPio8L0JDX05BTUU+PENVU1RPTUVSX05BTUU+KjwvQ1VTVE9NRVJfTkFNRT48UkVTUE9OU0VfREVTQz5UcmFuc2FjdGlvbiBDb21wbGV0ZWQgU3VjY2Vzc2Z1bGx5PC9SRVNQT05TRV9ERVNDPjxSRVNQT05TRV9DT0RFPjAwPC9SRVNQT05TRV9DT0RFPjxDQVJEX0VYUF9EQVRFPjA3Mjg8L0NBUkRfRVhQX0RBVEU+PFZJRD4qPC9WSUQ+PFVJRF9WSURfTk8+KjwvVUlEX1ZJRF9OTz48TUVSQ0hBTlRfVFlQRT42MDEyPC9NRVJDSEFOVF9UWVBFPjxFTlRSWV9NT0RFPjA1MTwvRU5UUllfTU9ERT48U0VSVklDRV9DT05ESVRJT04+MDA8L1NFUlZJQ0VfQ09ORElUSU9OPjxBQ1FVUklFUl9JTlNUX0lEPjYwNzA4MjwvQUNRVVJJRVJfSU5TVF9JRD48UlJOPjIzNTQwODE4NTk2MTwvUlJOPjxURVJNSU5BTF9JRD5GU1MwMDAzNjwvVEVSTUlOQUxfSUQ+PENBUkRfQUNQVF9JRD5DU0JUU1BTQUJFUjAwMDE8L0NBUkRfQUNQVF9JRD48Q0FSRF9BQ1BUX05BTUVfTE9DPkRJR0kgS0lPSyAgICAgICAgICAgICAgQ0hJVFRPT1IgICAgIEFQSU48L0NBUkRfQUNQVF9OQU1FX0xPQz48VFhOX0FNT1VOVD4wMDAwMDAwNTAwMDA8L1RYTl9BTU9VTlQ+PEZFRT4qPC9GRUU+PFNFUlZJQ0VfQ0hBUkdFPio8L1NFUlZJQ0VfQ0hBUkdFPjxBQ0NfQVZBTElCX0JBTEFOQ0U+MDAwMDAwMDAwMDAxOTM0MDA8L0FDQ19BVkFMSUJfQkFMQU5DRT4gPENVUlJFTkNZX0NPREU+MzU2PC9DVVJSRU5DWV9DT0RFPjxUWE5fQVVUSF9DT0RFPjg0MzAwMDwvVFhOX0FVVEhfQ09ERT48VUlEQUlfQVVUSF9DT0RFPio8L1VJREFJX0FVVEhfQ09ERT48RlJPTV9BQ0NPVU5UX05PPio8L0ZST01fQUNDT1VOVF9OTz4gPFJFTUlUVEVSX05BTUU+KjwvUkVNSVRURVJfTkFNRT4gPEVNVl9DSElQX0RBVEE+OTEwYTY1MmVkZjdiMTJhNTI1N2YwMDE0PC9FTVZfQ0hJUF9EQVRBPjxDT1VOVFJZX0NPREU+KjwvQ09VTlRSWV9DT0RFPiA8UE9TVEFMX0NPREU+NTE3MjgwPC9QT1NUQUxfQ09ERT4gPC9XSVRIRFJBV0FMUkVTUE9OU0U+</DATA></MRESPONSE>";
					logger.info("response from fss : " + responseString);
					if (responseString != null) {
						final WithdrawalResponse response = JaxbUtil.toResponse(responseString);
						logger.info("WithdrawaResponse", mapper.writeValueAsString(response));
						responseParams.setRespCode(response.responseCode);
						responseParams.setHitachiResCode(response.responseCode);
						if ("00".equalsIgnoreCase(response.responseCode)) {
							responseParams.setStatus(true);
							responseParams.setMsg("Successful");
						} else {
							responseParams.setStatus(false);
							responseParams.setMsg("Failed");
						}

						responseParams.setArpcdata(response.emvChipData);
						responseParams.setAuthid(response.txnAuthCode);
						responseParams.setMsg(response.responseDesc);
						responseParams.setEd(response.accAvalibBalance / 100.0 + "#" + response.accLEDBalance / 100.0);

						responseParams.setToken(message.getRrn());
						responseParams.setRrn(message.getRrn());
						Track2 track2 = Track2.parse(message.getCarddata());
						responseParams.setCardno(track2.pan);
						BERTLV tlv = BERTLV.parse(message.getTag55());
						responseParams.setAid(tlv.getFirst("9F06"));
						responseParams.setTvr(tlv.getFirst("95"));
						responseParams.setTsi(tlv.getFirst("9B"));
						responseParams.setDate(message.getDate());
						responseParams.setInvoiceNumber(response.txnAuthCode);
						responseParams.setBatchNo(response.txnAuthCode);
						responseParams.setApplName("Card");
						responseParams.setServiceBin("Offus");
						sessionIdGenerator.generateHash(message, responseParams);

					}
				} else if ("microBalEnqSbm".equals(message.getRequestcode())) {
					logger.info("creating balance request");
					final Request request = populateRequest(message, new BalanceRequest());
					request.txnAmount = "000000000000";
					request.txnCode = "310000";
					final CSBServiceSoap serviceSoap = getClient();
					final String responseString = serviceSoap.transactionRequest(JaxbUtil.marshal(request));
					//final String responseString = "<?xml version='1.0' encoding='UTF-8'?><MRESPONSE><SID></SID><TYPE>0</TYPE><DATA>PD94bWwgdmVyc2lvbj0nMS4wJyBlbmNvZGluZz0nVVRGLTgnID8+CjxCQUxBTkNFUkVTUE9OU0U+PE1TR19UWVBFPjAyMTA8L01TR19UWVBFPjxDQVJEX05PPjUwODg3NzA3NTAwMDAxMjk8L0NBUkRfTk8+PFRYTl9DT0RFPjMxMDAwMDwvVFhOX0NPREU+PFRYTl9EQVRFX1RJTUU+MTIxOTAzMDQ1OTwvVFhOX0RBVEVfVElNRT48U1RBTj4xODU2NjA8L1NUQU4+PFRJTUU+MDgzNDU5PC9USU1FPjxEQVRFPjEyMTk8L0RBVEU+PEJBVENIX0lEPio8L0JBVENIX0lEPjxBR0VOVF9JRD5GU1MwMDAwMDAwMzE8L0FHRU5UX0lEPjxCQ19OQU1FPio8L0JDX05BTUU+PENVU1RPTUVSX05BTUU+KjwvQ1VTVE9NRVJfTkFNRT48UkVTUE9OU0VfREVTQz5UcmFuc2FjdGlvbiBDb21wbGV0ZWQgU3VjY2Vzc2Z1bGx5PC9SRVNQT05TRV9ERVNDPjxSRVNQT05TRV9DT0RFPjAwPC9SRVNQT05TRV9DT0RFPjxDQVJEX0VYUF9EQVRFPjA1Mjc8L0NBUkRfRVhQX0RBVEU+PFZJRD4qPC9WSUQ+PFVJRF9WSURfTk8+KjwvVUlEX1ZJRF9OTz48TUVSQ0hBTlRfVFlQRT42MDEyPC9NRVJDSEFOVF9UWVBFPjxFTlRSWV9NT0RFPjA1MTwvRU5UUllfTU9ERT48U0VSVklDRV9DT05ESVRJT04+MDA8L1NFUlZJQ0VfQ09ORElUSU9OPjxBQ1FVUklFUl9JTlNUX0lEPjYwNzA4MjwvQUNRVVJJRVJfSU5TVF9JRD48UlJOPjIzNTMwODE4NTY2MDwvUlJOPjxURVJNSU5BTF9JRD5GU1MwMDAzMTwvVEVSTUlOQUxfSUQ+PENBUkRfQUNQVF9JRD5DU0JUU1BTQUJFUjAwMDE8L0NBUkRfQUNQVF9JRD48Q0FSRF9BQ1BUX05BTUVfTE9DPkRJR0kgS0lPSyAgICAgICAgICAgICAgQ0hJVFRPT1IgICAgIEFQSU48L0NBUkRfQUNQVF9OQU1FX0xPQz48VFhOX0FNT1VOVD4wMDAwMDAwMDAwMDA8L1RYTl9BTU9VTlQ+PEZFRT4qPC9GRUU+PFNFUlZJQ0VfQ0hBUkdFPio8L1NFUlZJQ0VfQ0hBUkdFPjxBQ0NfTEVEX0JBTEFOQ0U+MDAwMDAwMDAwMDAwMDI5MzI8L0FDQ19MRURfQkFMQU5DRT4gPEFDQ19BVkFMSUJfQkFMQU5DRT4wMDAwMDAwMDAwMDAwMjkzMjwvQUNDX0FWQUxJQl9CQUxBTkNFPiA8Q1VSUkVOQ1lfQ09ERT4zNTY8L0NVUlJFTkNZX0NPREU+PFRYTl9BVVRIX0NPREU+NjYwMDAwPC9UWE5fQVVUSF9DT0RFPjxVSURBSV9BVVRIX0NPREU+KjwvVUlEQUlfQVVUSF9DT0RFPjxFTVZfQ0hJUF9EQVRBPjkxMGE0MTZlMjBhZWIwMzk1ZGUyMDAxNDwvRU1WX0NISVBfREFUQT48RlJPTV9BQ0NPVU5UX05PPio8L0ZST01fQUNDT1VOVF9OTz4gPFJFTUlUVEVSX05BTUU+KjwvUkVNSVRURVJfTkFNRT4gPC9CQUxBTkNFUkVTUE9OU0U+</DATA></MRESPONSE>";
					if (responseString != null) {
						final BalanceResponse response = JaxbUtil.toResponse(responseString);
						logger.info("BalanceResponse", mapper.writeValueAsString(response));
						responseParams.setRespCode(response.responseCode);
						responseParams.setHitachiResCode(response.responseCode);
						if ("00".equalsIgnoreCase(response.responseCode)) {
							responseParams.setStatus(true);
							responseParams.setMsg("Successful");
						} else {
							responseParams.setStatus(false);
							responseParams.setMsg("Failed");
						}

						responseParams.setArpcdata(response.emvChipData);
						responseParams.setAuthid(response.txnAuthCode);
						responseParams.setMsg(response.responseDesc);
						responseParams.setEd(response.accAvalibBalance / 100.0 + "#" + response.accLEDBalance / 100.0);

						responseParams.setToken(message.getRrn());
						responseParams.setRrn(message.getRrn());
						Track2 track2 = Track2.parse(message.getCarddata());
						responseParams.setCardno(track2.pan);
						BERTLV tlv = BERTLV.parse(message.getTag55());
						responseParams.setAid(tlv.getFirst("9F06"));
						responseParams.setTvr(tlv.getFirst("95"));
						responseParams.setTsi(tlv.getFirst("9B"));
						responseParams.setDate(message.getDate());
						responseParams.setInvoiceNumber(response.txnAuthCode);
						responseParams.setBatchNo(response.txnAuthCode);
						responseParams.setApplName("Card");
						responseParams.setServiceBin("Offus");
						sessionIdGenerator.generateHash(message, responseParams);
					}
				} else if ("wsReversalMatmSbm".equals(message.getRequestcode())) {
					final Request request = populateRequest(message, new ReversalRequest());
					request.txnAmount = Generator.amountToFormattedString12(new BigDecimal(message.getAmt()).multiply(new BigDecimal(100.0)).toBigInteger());
					;
					request.txnCode = "010000";
					request.msgType = "0420";
					logger.info("F039", message.getOp());
					request.reversalRespCode = message.getOp();
					// request.originalData = "0200"+ message.get("F037") + message.get("F063");
					request.track2Data = "*";
					final CSBServiceSoap serviceSoap = getClient();
					final String responseString = serviceSoap.transactionRequest(JaxbUtil.marshal(request));
					if (responseString != null) {
						final ReversalResponse response = JaxbUtil.toResponse(responseString);
						logger.info("ReversalResponse", mapper.writeValueAsString(response));
						responseParams.setArpcdata(response.emvChipData);
						if ("00".equalsIgnoreCase(response.responseCode))
							responseParams.setStatus(true);
						else
							responseParams.setStatus(false);
						responseParams.setRespCode(response.responseCode);
						responseParams.setAuthid(response.txnAuthCode);
						responseParams.setMsg(response.responseDesc);
						// message.put("F102", response.fromAccount);
						responseParams.setEd(response.accAvalibBalance / 100.0 + "#" + response.accLEDBalance / 100.0);
					}
				}
				logger.info("message to middleware\r\n" + mapper.writeValueAsString(message));
			} catch (Exception e) {
				logger.error("", e);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		sessionIdGenerator.generateHash(message, responseParams);
		return responseParams;
	}

	private final Request populateRequest(final RequestParams message, final Request request) throws JAXBException {
		final UserAthnMstr agent = agentRepository.findById(message.getSrno()).get();
		message.setCarddata(decryptTrack2(message.getCarddata()));
		Track2 track2 = Track2.parse(message.getCarddata());
		request.msgType = "0200";
		request.txnAmount = Generator.amountToFormattedString12(new BigDecimal(message.getAmt()).multiply(new BigDecimal(100.0)).toBigInteger());
		request.cardNo = track2.pan;
		final Date date = new Date();
		request.txnDateTime = ISO8583DateFormat.getTransmissionTime(date);
		request.stan = message.getRrn().substring(6);
		request.time = ISO8583DateFormat.getLocalTime(date);
		request.date = ISO8583DateFormat.getLocalDate(date);
		request.srcType = "T";
		request.merchantType = "6012";
		request.postalCode = "400703";
		request.entryMode = "051";
		request.serviceCondition = "00";
		request.rrn = message.getRrn();
		request.terminalID = message.getSrno();
		request.agentID = message.getMid();
		request.cardAcptNameLOC = "DIGI KIOK              CHITTOOR     APIN";
		agent.getUserAdditionalDetailses().stream().findFirst().ifPresent(det -> {
			request.postalCode = det.getId().getPincode();
			//request.cardAcptNameLOC = det.getId().getAddrLneOne();
		});

		UserAdditionalDetails add = agent.getUserAdditionalDetailses().stream().findFirst().get();
		request.acqurierInstId = acqId;
		request.cardAcptID = merchantId;
		request.merchantPassCode = merchantPass;
		request.fromAccountNo = "*";
		request.toAccountNo = "*";
		request.currencyCode = "356";
		request.authIndicator = "3";
		request.authFactor = "*";
		request.track2Data = message.getCarddata();
		request.track1Data = "*";
		request.pinData = "*";

		if (message.getTag55() != null) {
			logger.info("Inside message.getTag55() != null method ::");
			request.emvChipData = message.getTag55();
			logger.info("message.getTag55() :: {}",message.getTag55());
			final BERTLV tlv = BERTLV.parse(message.getTag55());
			logger.info("tlv :: {}", tlv);
			logger.info("tlv.getFirst(\"5F34\") :: {}", tlv.getFirst("5F34"));
			request.cardSeqNo = (tlv.getFirst("5F34") == null || tlv.getFirst("5F34").length() == 0) ? "00" : tlv.getFirst("5F34");
			if (tlv != null) {
				// if("E1".equals(message.get("F039"))) tlv.put("9F5B", "2030303030");
				// if(tlv.getFirst("9F07") == null) tlv.put("9F07", "FF80");
				//if (tlv.getFirst("5F34") == null)
					//tlv.put("5F34", request.cardSeqNo);
				tlv.removeAll(List.of("9B")).put("9F53", "52");
				logger.info("tlv :: {}", tlv);
				request.emvChipData = tlv.pack().toLowerCase();
			}
			request.postalCode = request.postalCode;
			String address20 = trimToLengthStrict(add.getId().getAddrLneOne(), 20);
			request.posDataCode = "422112321033000" + request.postalCode + address20;
		} else {
			request.emvChipData = "*";
			request.cardSeqNo = "*";
			request.entryMode = "801";
			request.postalCode = request.postalCode;
			String address20 = trimToLengthStrict(add.getId().getAddrLneOne(), 20);
			request.posDataCode = "422112321033000" + request.postalCode + address20;
		}

		if (track2 != null) {
			request.cardNo = track2.pan;
			request.serviceCode = track2.servicecode;
			request.cardExpiryYYMM = track2.expiry.substring(2, 4) + track2.expiry.substring(0, 2);

		}
		if (message.getPindata() != null) {
			request.pinData = translatePinblock(message.getPindata());
		}
		return request;
	}

	private final String translatePinblock(String pinblock) {
		pinblock = pinblock.replaceAll(" ", "");
		byte[] clearPin = MiscService.decryptTDES(ByteHexUtil.hexToByte(pinblock), ByteHexUtil.hexToByte(sessionKey));
		logger.info("clearPin : " + clearPin);
		byte[] encPin = MiscService.encryptTDES(clearPin, ByteHexUtil.hexToByte(targetZPK));
		return ByteHexUtil.byteToHex(encPin).toUpperCase();
	}

	private String decryptTrack2(String cardData) {
		byte[] clearPan = MiscService.decryptTDES(ByteHexUtil.hexToByte(cardData), ByteHexUtil.hexToByte(sessionKey));
		String track2Data = ByteHexUtil.byteToHex(clearPan);
		logger.info("track2Data :: {}", track2Data);
		int length = Integer.parseInt(track2Data.substring(0, 2));
		logger.info("length :: {}", length);
		return track2Data.substring(2, 2+length);
	}

	private String trimToLengthStrict(String string, int len) {
		return String.format("%" + len + "s", string).substring(0, len);
	}

	private String trimToLength(String string, int len) {
		if (string == null || string.length() <= len)
			return string;
		return string.substring(0, len);
	}

	private static final CSBServiceSoap getClient() throws MalformedURLException {
		final CSBService service = new CSBService(new File("fss.wsdl").toURI().toURL());
		final CSBServiceSoap serviceSoap = service.getCSBServiceSoap12();
		final BindingProvider bindingProvider = (BindingProvider) serviceSoap;
		final Binding binding = bindingProvider.getBinding();
		final Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, 100000); // Timeout in millis
		requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, 100000); // Timeout in millis
		binding.setHandlerChain(List.of(new LoggingHandler()));
		return serviceSoap;
	}

}