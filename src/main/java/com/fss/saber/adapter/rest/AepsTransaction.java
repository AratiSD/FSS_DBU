package com.fss.saber.adapter.rest;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.util.iso8583.ISO8583DateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fss.saber.adapter.jpa.AadhaarBankBin;
import com.fss.saber.adapter.jpa.TransactionDetails;
import com.fss.saber.adapter.jpa.UserAthnMstr;
import com.fss.saber.adapter.jws.CSBService;
import com.fss.saber.adapter.jws.CSBServiceSoap;
import com.fss.saber.adapter.jws.LoggingHandler;
import com.fss.saber.adapter.model.AepsTransactionSbm;
import com.fss.saber.adapter.model.AepsTransactionSbmId;
import com.fss.saber.adapter.model.CodeValue;
import com.fss.saber.adapter.model.RequestParamAeps;
import com.fss.saber.adapter.model.ResponseParamAeps;
import com.fss.saber.adapter.modelm24.BalanceRequest;
import com.fss.saber.adapter.modelm24.BalanceResponse;
import com.fss.saber.adapter.modelm24.FundTransferRequest;
import com.fss.saber.adapter.modelm24.FundTransferResponse;
import com.fss.saber.adapter.modelm24.MiniStatementRequest;
import com.fss.saber.adapter.modelm24.MiniStatementResponse;
import com.fss.saber.adapter.modelm24.Request;
import com.fss.saber.adapter.modelm24.ReversalRequest;
import com.fss.saber.adapter.modelm24.ReversalResponse;
import com.fss.saber.adapter.modelm24.WithdrawalRequest;
import com.fss.saber.adapter.modelm24.WithdrawalResponse;
import com.fss.saber.adapter.piddata.PidData;
import com.fss.saber.adapter.repository.AadharBankBinRepoitory;
import com.fss.saber.adapter.repository.AgentRepository;
import com.fss.saber.adapter.repository.TransactionRepository;
import com.fss.saber.adapter.util.Generator;
import com.fss.saber.adapter.util.JaxbUtil;
import com.fss.saber.adapter.util.SessionIdGenerator;
import com.fss.saber.adapter.util.TLV;
import com.sun.xml.ws.client.BindingProviderProperties;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.ws.Binding;
import jakarta.xml.ws.BindingProvider;

@RestController
@RequestMapping(value = "/kiosk/csb")
public class AepsTransaction {

	private static final Logger logger = LoggerFactory.getLogger(AepsTransaction.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private SessionIdGenerator sessionIdGenerator;

	@Autowired
	AadharBankBinRepoitory bankRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Value("${merchant.id}")
	public String merchantId;

	@Value("${acq.id}")
	public String acqId;

	@Value("${merchant.pass}")
	public String merchantPass;

	@PostMapping(path = "/aeps/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseParamAeps transaction(@RequestBody RequestParamAeps message) {
		AepsTransactionSbmId aepsTransactionSbmId = new AepsTransactionSbmId();
		final ResponseParamAeps responseParams = new ResponseParamAeps();
		logger.info("Transaction type : {}", message.txType);
		final Date date = new Date();
		logger.info("AepsTransaction.transaction() message.txType :: {}", message);
		message.date = date;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String sessionID = sessionIdGenerator.generateHash(message, responseParams);
		try {
			final String rrn = message.stan;
			if ("TS".equalsIgnoreCase(message.txType))
				return null;
			logger.info("KIOSK_request :: {}", mapper.writeValueAsString(message));
			/*
			 * responseParams.respcode = "96"; responseParams.msg = "No Response"; responseParams.status = false; responseParams.aepsTransactionSbm = new AepsTransactionSbm();
			 * responseParams.balance="0#0"; responseParams.aepsTransactionSbm.setBankRefNo(rrn);
			 */

			if ("BE".equalsIgnoreCase(message.txType)) {
				try {
					logger.info("Inside BE transaction");
					final Request request = populateRequest(message, new BalanceRequest());
					request.txnCode = "310000";
					request.txnAmount = "000000000000";
					logger.info("BalanceRequest", mapper.writeValueAsString(request));
					final CSBServiceSoap serviceSoap = getClient();
					final String responseString = serviceSoap.transactionRequest(JaxbUtil.marshal(request));
					//final String responseString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><MRESPONSE xmlns:ns2=\"http://aeps.Csbemvonline.in/\" xmlns:ns3=\"http://www.finacle.com/fixml\"><SID xsi:type=\"xs:string\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">FSS111</SID><TYPE>1</TYPE><DATA>PEJBTEFOQ0VSRVNQT05TRSB4bWxuczpuczI9Imh0dHA6Ly9hZXBzLkNzYmVtdm9ubGluZS5pbi8iIHhtbG5zOm5zMz0iaHR0cDovL3d3dy5maW5hY2xlLmNvbS9maXhtbCI+ICAgICA8UkVTUE9OU0VfQ09ERT4wMDwvUkVTUE9OU0VfQ09ERT4gICAgIDxBQ0NfTEVEX0JBTEFOQ0U+MC4wPC9BQ0NfTEVEX0JBTEFOQ0U+ICAgICA8QUNDX0FWQUxJQl9CQUxBTkNFPjI0NjguMDwvQUNDX0FWQUxJQl9CQUxBTkNFPiAgICAgPE1TR19UWVBFPjAyMTA8L01TR19UWVBFPiAgICAgPFJFU1BPTlNFX0RFU0M+VHJhbnNhY3Rpb24gQ29tcGxldGVkIFN1Y2Nlc3NmdWxseTwvUkVTUE9OU0VfREVTQz4gPC9CQUxBTkNFUkVTUE9OU0U+</DATA></MRESPONSE>";
					if (responseString != null) {
						final BalanceResponse response = JaxbUtil.toResponse(responseString);
						logger.info("BalanceResponse", mapper.writeValueAsString(response));

						if (response.responseCode.equalsIgnoreCase("00")) {
							responseParams.status = true;
						} else {
							responseParams.status = false;
						}
						responseParams.respcode = response.responseCode;
						responseParams.msg = response.responseDesc;
						responseParams.balance = response.accAvalibBalance / 100.0 + "#" + response.accLEDBalance / 100.0;
						responseParams.aepsTransactionSbm = new AepsTransactionSbm();
						aepsTransactionSbmId.setPosId(message.tid);
						aepsTransactionSbmId.setRrn(rrn);
						aepsTransactionSbmId.setTransactionType(message.txType);
						responseParams.aepsTransactionSbm.setId(aepsTransactionSbmId);
						responseParams.aepsTransactionSbm.setUsername(message.username);
						responseParams.aepsTransactionSbm.setMerchantId(String.valueOf(message.hmData.get("mid")));
						responseParams.aepsTransactionSbm.setStan(Integer.parseInt(message.stan.substring(6)));
						responseParams.aepsTransactionSbm.setTransactionDate(timestamp.getTime());
						responseParams.aepsTransactionSbm.setMsidn(message.username);
						responseParams.aepsTransactionSbm.setAadharno(message.aadharNo);
						responseParams.aepsTransactionSbm.setBankcode(message.bankCode);
						responseParams.aepsTransactionSbm.setAmount(Double.valueOf(message.amount));
						responseParams.aepsTransactionSbm.setPoslocation(message.location);
						responseParams.aepsTransactionSbm.setCurrencycode(356);
						responseParams.aepsTransactionSbm.setNote("NOTE TEST"); // dummy
						responseParams.aepsTransactionSbm.setInvoiceNo(message.stan.substring(6)); // dummy
						responseParams.aepsTransactionSbm.setResponsecode(response.responseCode);
						responseParams.aepsTransactionSbm.setResponsemsg(response.responseDesc);
						responseParams.aepsTransactionSbm.setAuthid("");
						responseParams.aepsTransactionSbm.setBankRefNo(rrn);
						responseParams.aepsTransactionSbm.setStatus(response.responseCode);
						responseParams.aepsTransactionSbm.setDevStatus("S");
						responseParams.aepsTransactionSbm.setMatmTid(message.tid);
						responseParams.aepsTransactionSbm.setMdMdr(0.0);
						responseParams.aepsTransactionSbm.setAdMdr(0.0);
						responseParams.aepsTransactionSbm.setLastTxnStatus("");
						responseParams.aepsTransactionSbm.setInstCommSettleStatus("P");
						responseParams.aepsTransactionSbm.setInstSettleStatus("P");
						responseParams.aepsTransactionSbm.setIsLtsUpdate("N");
						responseParams.aepsTransactionSbm.setCharges(0.0);
						responseParams.aepsTransactionSbm.setTypedemon1(0);
						responseParams.aepsTransactionSbm.setTypedemon1count(0);
						responseParams.aepsTransactionSbm.setTypedemon2(0);
						responseParams.aepsTransactionSbm.setTypedemon2count(0);
						responseParams.aepsTransactionSbm.setTypedemon3(0);
						responseParams.aepsTransactionSbm.setTypedemon3count(0);
						responseParams.aepsTransactionSbm.setTypedemon4(0);
						responseParams.aepsTransactionSbm.setTypedemon4count(0);
						responseParams.sessionId = sessionID;

						try {
							TransactionDetails transaction = addTransactionDetails(message, responseParams);
							transactionRepository.save(transaction);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("Exception inside BE transaction details :: {}", e.getMessage());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("Exception inside BE :: {}", e.getMessage());
				}
			} else if ("CW".equalsIgnoreCase(message.txType)) {
				logger.info("AepsTransaction.transaction() inside CW");
				try {
					if (message.requestcode.equalsIgnoreCase("aepsSbmTimeoutRev")) {
						TransactionDetails transactionDetails = transactionRepository.findById(rrn);
						transactionDetails.setTransactionStatus("RE");
						transactionDetails.setLastTxnStatus("CW FAILED");
						responseParams.respcode = "00";
						responseParams.msg = "Reversal Success";
						if (responseParams.respcode.equalsIgnoreCase("00")) {
							responseParams.status = true;
						} else {
							responseParams.status = false;
						}
						transactionRepository.save(transactionDetails);
						logger.info("RE data is updated");
					} else {
						final Request request = populateRequest(message, new WithdrawalRequest());
						logger.info("WithdrawalRequest :: {}", mapper.writeValueAsString(request));
						request.txnCode = "010000";
						final CSBServiceSoap serviceSoap = getClient();
						final String responseString = serviceSoap.transactionRequest(JaxbUtil.marshal(request));
						//final String responseString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><MRESPONSE xmlns:ns2=\"http://aeps.Csbemvonline.in/\" xmlns:ns3=\"http://www.finacle.com/fixml\"><SID xsi:type=\"xs:string\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">FSS111</SID><TYPE>1</TYPE><DATA>PFdJVEhEUkFXQUxSRVNQT05TRSB4bWxuczpuczI9Imh0dHA6Ly9hZXBzLkNzYmVtdm9ubGluZS5pbi8iIHhtbG5zOm5zMz0iaHR0cDovL3d3dy5maW5hY2xlLmNvbS9maXhtbCI+PE1TR19UWVBFPjAyMTA8L01TR19UWVBFPjxDQVJEX05PPjYwNzA4MjA1OTgyMTU1NTQ4NzI8L0NBUkRfTk8+PFRYTl9DT0RFPjAxMDAwMDwvVFhOX0NPREU+PFRYTl9EQVRFX1RJTUU+MTIxOTAzMjIzMTwvVFhOX0RBVEVfVElNRT48U1RBTj41MTQ0Njk8L1NUQU4+PFRJTUU+MDg1MjMxPC9USU1FPjxEQVRFPjEyMTk8L0RBVEU+PEJBVENIX0lEPio8L0JBVENIX0lEPjxBR0VOVF9JRD4qPC9BR0VOVF9JRD48QkNfTkFNRT4qPC9CQ19OQU1FPiDCoMKgwqAgPENVU1RPTUVSX05BTUU+KjwvQ1VTVE9NRVJfTkFNRT48UkVTUE9OU0VfREVTQz5UcmFuc2FjdGlvbiBDb21wbGV0ZWQgU3VjY2Vzc2Z1bGx5PC9SRVNQT05TRV9ERVNDPjxSRVNQT05TRV9DT0RFPjAwPC9SRVNQT05TRV9DT0RFPjxDQVJEX0VYUF9EQVRFPio8L0NBUkRfRVhQX0RBVEU+PFZJRD4qPC9WSUQ+PFVJRF9WSURfTk8+NTk4MjE1NTU0ODcyPC9VSURfVklEX05PPjxNRVJDSEFOVF9UWVBFPjYwMTI8L01FUkNIQU5UX1RZUEU+PEVOVFJZX01PREU+MDE5PC9FTlRSWV9NT0RFPiDCoMKgwqAgPFNFUlZJQ0VfQ09ORElUSU9OPjA1PC9TRVJWSUNFX0NPTkRJVElPTj48QUNRVVJJRVJfSU5TVF9JRD42MDcwODI8L0FDUVVSSUVSX0lOU1RfSUQ+PFJSTj4yMzUzMDg1MTQ0Njk8L1JSTj48VEVSTUlOQUxfSUQ+RlNTMDAwMzE8L1RFUk1JTkFMX0lEPiDCoMKgwqAgPENBUkRfQUNQVF9JRD5DU0JUU1BTQUJFUjAwMDE8L0NBUkRfQUNQVF9JRD48Q0FSRF9BQ1BUX05BTUVfTE9DPkRJR0kgS0lPS8KgwqDCoMKgwqDCoMKgwqDCoMKgwqDCoMKgIENISVRUT09SwqDCoMKgwqAgQVBJTjwvQ0FSRF9BQ1BUX05BTUVfTE9DPjxUWE5fQU1PVU5UPjAwMDAwMDMwMDAwMDwvVFhOX0FNT1VOVD48RkVFPio8L0ZFRT48U0VSVklDRV9DSEFSR0U+KjwvU0VSVklDRV9DSEFSR0U+PEFDQ19BVkFMSUJfQkFMQU5DRT4qPC9BQ0NfQVZBTElCX0JBTEFOQ0U+PENVUlJFTkNZX0NPREU+MzU2PC9DVVJSRU5DWV9DT0RFPiDCoMKgwqAgPFRYTl9BVVRIX0NPREU+KjwvVFhOX0FVVEhfQ09ERT48VUlEQUlfQVVUSF9DT0RFPmY5YTg0NjBlNzEyMTQ1NmY4OTAwNjdiYjY4OGQ3ZGU3PC9VSURBSV9BVVRIX0NPREU+PEZST01fQUNDT1VOVF9OTz4qPC9GUk9NX0FDQ09VTlRfTk8+IMKgwqDCoCA8UkVNSVRURVJfTkFNRT4qPC9SRU1JVFRFUl9OQU1FPjxDT1VOVFJZX0NPREU+KjwvQ09VTlRSWV9DT0RFPjxQT1NUQUxfQ09ERT40MDA3MDM8L1BPU1RBTF9DT0RFPjwvV0lUSERSQVdBTFJFU1BPTlNFPg</DATA></MRESPONSE>";
						if (responseString != null) {
							final WithdrawalResponse response = JaxbUtil.toResponse(responseString);
							logger.info("WithdrawalResponse", mapper.writeValueAsString(response));
							responseParams.respcode = response.responseCode;
							responseParams.msg = response.responseDesc;
							responseParams.balance = response.accAvalibBalance / 100.0 + "#" + response.accLEDBalance / 100.0;
							if (response.responseCode.equalsIgnoreCase("00")) {
								responseParams.status = true;
							} else {
								responseParams.status = false;
							}
							responseParams.aepsTransactionSbm = new AepsTransactionSbm();
							aepsTransactionSbmId.setPosId(message.tid);
							aepsTransactionSbmId.setRrn(rrn);
							aepsTransactionSbmId.setTransactionType(message.txType);
							responseParams.aepsTransactionSbm.setId(aepsTransactionSbmId);
							responseParams.aepsTransactionSbm.setUsername(message.username);
							responseParams.aepsTransactionSbm.setMerchantId(String.valueOf(message.hmData.get("mid")));
							responseParams.aepsTransactionSbm.setStan(Integer.parseInt(message.stan.substring(6)));
							responseParams.aepsTransactionSbm.setTransactionDate(timestamp.getTime());
							responseParams.aepsTransactionSbm.setMsidn(message.username);
							responseParams.aepsTransactionSbm.setAadharno(message.aadharNo);
							responseParams.aepsTransactionSbm.setBankcode(message.bankCode);
							responseParams.aepsTransactionSbm.setAmount(Double.valueOf(message.amount));
							responseParams.aepsTransactionSbm.setPoslocation(message.location);
							responseParams.aepsTransactionSbm.setCurrencycode(356);
							responseParams.aepsTransactionSbm.setNote("NOTE TEST");
							responseParams.aepsTransactionSbm.setInvoiceNo(message.stan.substring(6));
							responseParams.aepsTransactionSbm.setResponsecode(response.responseCode);
							responseParams.aepsTransactionSbm.setResponsemsg(response.responseDesc);
							responseParams.aepsTransactionSbm.setAuthid("");
							responseParams.aepsTransactionSbm.setBankRefNo(rrn);
							responseParams.aepsTransactionSbm.setStatus(response.responseCode);
							responseParams.aepsTransactionSbm.setDevStatus("S");
							responseParams.aepsTransactionSbm.setMatmTid(message.tid);
							responseParams.aepsTransactionSbm.setMdMdr(0.0);
							responseParams.aepsTransactionSbm.setAdMdr(0.0);
							responseParams.aepsTransactionSbm.setLastTxnStatus("");
							responseParams.aepsTransactionSbm.setInstCommSettleStatus("P");
							responseParams.aepsTransactionSbm.setInstSettleStatus("P");
							responseParams.aepsTransactionSbm.setIsLtsUpdate("N");
							responseParams.aepsTransactionSbm.setCharges(0.0);
							responseParams.aepsTransactionSbm.setTypedemon1(0);
							responseParams.aepsTransactionSbm.setTypedemon1count(0);
							responseParams.aepsTransactionSbm.setTypedemon2(0);
							responseParams.aepsTransactionSbm.setTypedemon2count(0);
							responseParams.aepsTransactionSbm.setTypedemon3(0);
							responseParams.aepsTransactionSbm.setTypedemon3count(0);
							responseParams.aepsTransactionSbm.setTypedemon4(0);
							responseParams.aepsTransactionSbm.setTypedemon4count(0);
							responseParams.sessionId = sessionID;

							try {
								TransactionDetails transaction = addTransactionDetails(message, responseParams);
								transactionRepository.save(transaction);
							} catch (Exception e) {
								e.printStackTrace();
								logger.info("Exception inside CW transaction details :: {}", e.getMessage());
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("Exception inside CW :: {}", e.getMessage());
				}
			} else if ("FT".equalsIgnoreCase(message.txType)) {
				final Request request = populateRequest(message, new FundTransferRequest());
				request.beneficiaryData = new TLV().put("060", "6521500891607617819").build();
				request.txnCode = "450000";
				logger.info("WithdrawalRequest", mapper.writeValueAsString(request));
				final CSBServiceSoap serviceSoap = getClient();
				final String responseString = serviceSoap.transactionRequest(JaxbUtil.marshal(request));
				if (responseString != null) {
					final FundTransferResponse response = JaxbUtil.toResponse(responseString);
					logger.info("FundTransferRequest", mapper.writeValueAsString(response));
					responseParams.respcode = response.responseCode;
					responseParams.msg = response.responseDesc;
					responseParams.status = "00".equals(response.responseCode);
					responseParams.aepsTransactionSbm = new AepsTransactionSbm();
					responseParams.balance = response.accAvalibBalance / 100.0 + "#" + response.accLEDBalance / 100.0;
					responseParams.aepsTransactionSbm.setBankRefNo(rrn);
					responseParams.aepsTransactionSbm.setAuthid(response.txnAuthCode);
				}
			} else if ("MS".equalsIgnoreCase(message.txType)) {
				try {
					final Request request = populateRequest(message, new MiniStatementRequest());
					logger.info("@@@@ hmData : {}", message.hmData.toString());
					request.txnCode = "070000";
					request.txnAmount = "000000000000";
					logger.info("MiniStatementRequest", mapper.writeValueAsString(request));
					final CSBServiceSoap serviceSoap = getClient();
					final String responseString = serviceSoap.transactionRequest(JaxbUtil.marshal(request));
					//final String responseString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><MRESPONSE xmlns:ns2=\"http://aeps.Csbemvonline.in/\" xmlns:ns3=\"http://www.finacle.com/fixml\"><SID xsi:type=\"xs:string\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">FSS111</SID><TYPE>1</TYPE><DATA>PE1JTklTVE1UUkVTUE9OU0UgeG1sbnM6bnMyPSJodHRwOi8vYWVwcy5Dc2JlbXZvbmxpbmUuaW4vIiB4bWxuczpuczM9Imh0dHA6Ly93d3cuZmluYWNsZS5jb20vZml4bWwiPiAgPE1TR19UWVBFPjAyMTA8L01TR19UWVBFPiA8Q0FSRF9OTz42MDcwOTQwMzY0NjE5MzAwNDAxPC9DQVJEX05PPiA8VFhOX0NPREU+MDcwMDAwPC9UWE5fQ09ERT4gPFRYTl9EQVRFX1RJTUU+MTIxOTA1MjIzNzwvVFhOX0RBVEVfVElNRT4gPFNUQU4+NDU1MzExPC9TVEFOPiA8VElNRT4xMDUyMzc8L1RJTUU+IDxEQVRFPjEyMTk8L0RBVEU+IDxCQVRDSF9JRD4qPC9CQVRDSF9JRD4gPEFHRU5UX0lEPio8L0FHRU5UX0lEPiA8QkNfTkFNRT4qPC9CQ19OQU1FPiA8Q1VTVE9NRVJfTkFNRT4qPC9DVVNUT01FUl9OQU1FPiA8UkVTUE9OU0VfREVTQz5UcmFuc2FjdGlvbiBDb21wbGV0ZWQgU3VjY2Vzc2Z1bGx5PC9SRVNQT05TRV9ERVNDPiA8UkVTUE9OU0VfQ09ERT4wMDwvUkVTUE9OU0VfQ09ERT4gPENBUkRfRVhQX0RBVEU+KjwvQ0FSRF9FWFBfREFURT4gPFZJRD4qPC9WSUQ+IDxVSURfVklEX05PPjM2NDYxOTMwMDQwMTwvVUlEX1ZJRF9OTz4gPE1FUkNIQU5UX1RZUEU+NjAxMjwvTUVSQ0hBTlRfVFlQRT4gPEVOVFJZX01PREU+MDE5PC9FTlRSWV9NT0RFPiA8U0VSVklDRV9DT05ESVRJT04+MDU8L1NFUlZJQ0VfQ09ORElUSU9OPiA8QUNRVVJJRVJfSU5TVF9JRD42MDcwODI8L0FDUVVSSUVSX0lOU1RfSUQ+IDxSUk4+MjM1MzEwNDU1MzExPC9SUk4+IDxURVJNSU5BTF9JRD5GU1MwMDA2NDwvVEVSTUlOQUxfSUQ+IDxDQVJEX0FDUFRfSUQ+Q1NCVFNQU0FCRVIwMDAxPC9DQVJEX0FDUFRfSUQ+IDxDQVJEX0FDUFRfTkFNRV9MT0M+RElHSSBLSU9LICAgICAgICAgICAgICBTQVJBTiAgICAgICAgQlJJTjwvQ0FSRF9BQ1BUX05BTUVfTE9DPiA8VFhOX0FNT1VOVD4wMDAwMDAwMDAwMDA8L1RYTl9BTU9VTlQ+IDxGRUU+KjwvRkVFPiA8U0VSVklDRV9DSEFSR0U+KjwvU0VSVklDRV9DSEFSR0U+IDxBQ0NfTEVEX0JBTEFOQ0U+KjwvQUNDX0xFRF9CQUxBTkNFPiAgPEFDQ19BVkFMSUJfQkFMQU5DRT4qPC9BQ0NfQVZBTElCX0JBTEFOQ0U+ICA8Q1VSUkVOQ1lfQ09ERT4zNTY8L0NVUlJFTkNZX0NPREU+IDxUWE5fQVVUSF9DT0RFPjAwMDAwMTwvVFhOX0FVVEhfQ09ERT4gPFVJREFJX0FVVEhfQ09ERT4zZTZlODMwOWE0NmU0YjlkOTExY2IyMTM2MzMxY2VmNDwvVUlEQUlfQVVUSF9DT0RFPiA8RlJPTV9BQ0NPVU5UX05PPio8L0ZST01fQUNDT1VOVF9OTz4gPE1JTklTVE1UPjA4MDYyMkNSIEJVTEsgUE9TVElORyAgMDAwMDAwMDQwMDAwMjUwNjIyQ1IgQ1JFRElUIElOVEVSRVMwMDAwMDAwMTYwMDAxNDA3MjJDUiBCVUxLIFBPU1RJTkcgIDAwMDAwMDA0MDAwMDE2MDcyMkNSIEJZIFRSQU5TRkVSICAgMDAwMDAxMjAwMDAwMTEwODIyQ1IgQlVMSyBQT1NUSU5HICAwMDAwMDAwNDAwMDAxNDA4MjJEUiBGSSBUeG4gQCBDU1AgbzAwMDAwMDA0NjAwMDI0MDgyMkNSIEJVTEsgUE9TVElORyAgMDAwMDAwMDQwMDAwMjUwODIyQ1IgQlVMSyBQT1NUSU5HICAwMDAwMDAwNDAwMDBCYWxhbmNlICAgICAgMDQwODAyLjYyIENSICAgICAgICAgIDwvTUlOSVNUTVQ+IDxSRU1JVFRFUl9OQU1FPio8L1JFTUlUVEVSX05BTUU+ICA8L01JTklTVE1UUkVTUE9OU0U+</DATA></MRESPONSE>";
					if (responseString != null) {
						final MiniStatementResponse response = JaxbUtil.toResponse(responseString);
						logger.info("MiniStatementResponse", mapper.writeValueAsString(response));
						responseParams.respcode = response.responseCode;
						responseParams.msg = response.responseDesc;
						if (response.responseCode.equalsIgnoreCase("00")) {
							responseParams.status = true;
						} else {
							responseParams.status = false;
						}
						responseParams.aepsTransactionSbm = new AepsTransactionSbm();
						aepsTransactionSbmId.setPosId(message.tid);
						aepsTransactionSbmId.setRrn(rrn);
						aepsTransactionSbmId.setTransactionType(message.txType);
						responseParams.balance = response.accAvalibBalance / 100.0 + "#" + response.accLEDBalance / 100.0;
						responseParams.aepsTransactionSbm.setId(aepsTransactionSbmId);
						responseParams.aepsTransactionSbm.setUsername(message.username);
						responseParams.aepsTransactionSbm.setMerchantId(String.valueOf(message.hmData.get("mid")));
						responseParams.aepsTransactionSbm.setStan(Integer.parseInt(message.stan.substring(6)));
						responseParams.aepsTransactionSbm.setTransactionDate(timestamp.getTime());
						responseParams.aepsTransactionSbm.setMsidn(message.username);
						responseParams.aepsTransactionSbm.setAadharno(message.aadharNo);
						responseParams.aepsTransactionSbm.setBankcode(message.bankCode);
						responseParams.aepsTransactionSbm.setAmount(Double.valueOf(message.amount));
						responseParams.aepsTransactionSbm.setPoslocation(message.location);
						responseParams.aepsTransactionSbm.setCurrencycode(356);
						responseParams.aepsTransactionSbm.setNote("NOTE TEST"); // dummy
						responseParams.aepsTransactionSbm.setInvoiceNo(message.stan.substring(6)); // dummy
						responseParams.aepsTransactionSbm.setResponsecode(response.responseCode);
						responseParams.aepsTransactionSbm.setResponsemsg(response.responseDesc);
						responseParams.aepsTransactionSbm.setAuthid(response.txnAuthCode);
						responseParams.aepsTransactionSbm.setBankRefNo(rrn);
						responseParams.aepsTransactionSbm.setStatus(response.responseCode);
						responseParams.aepsTransactionSbm.setAuthid("");
						responseParams.aepsTransactionSbm.setBankRefNo(rrn);
						responseParams.aepsTransactionSbm.setDevStatus("S");
						responseParams.aepsTransactionSbm.setMatmTid(message.tid);
						responseParams.aepsTransactionSbm.setMdMdr(0.0);
						responseParams.aepsTransactionSbm.setAdMdr(0.0);
						responseParams.aepsTransactionSbm.setLastTxnStatus("");
						responseParams.aepsTransactionSbm.setInstCommSettleStatus("P");
						responseParams.aepsTransactionSbm.setInstSettleStatus("P");
						responseParams.aepsTransactionSbm.setIsLtsUpdate("N");
						responseParams.aepsTransactionSbm.setCharges(0.0);
						responseParams.aepsTransactionSbm.setTypedemon1(0);
						responseParams.aepsTransactionSbm.setTypedemon1count(0);
						responseParams.aepsTransactionSbm.setTypedemon2(0);
						responseParams.aepsTransactionSbm.setTypedemon2count(0);
						responseParams.aepsTransactionSbm.setTypedemon3(0);
						responseParams.aepsTransactionSbm.setTypedemon3count(0);
						responseParams.aepsTransactionSbm.setTypedemon4(0);
						responseParams.aepsTransactionSbm.setTypedemon4count(0);
						responseParams.msData = response.miniStatement;
						responseParams.sessionId = sessionID;

						try {
							TransactionDetails transaction = addTransactionDetails(message, responseParams);
							transactionRepository.save(transaction);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("Exception inside MS transaction details :: {}", e.getMessage());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("Exception inside MS :: {}", e.getMessage());
				}
			} else if ("RE".equalsIgnoreCase(message.txType)) {
				final Request request = populateRev(message, new ReversalRequest());
				request.msgType = "0420";
				request.txnCode = "010000";
				// request.originalData = "0200"+ rrn + message.get("F090").split("#")[2];
				logger.info("ReversalRequest", mapper.writeValueAsString(request));
				final CSBServiceSoap serviceSoap = getClient();
				final String responseString = serviceSoap.transactionRequest(JaxbUtil.marshal(request));
				if (responseString != null) {
					final ReversalResponse response = JaxbUtil.toResponse(responseString);
					logger.info("ReversalResponse", mapper.writeValueAsString(response));
					responseParams.respcode = response.responseCode;
					responseParams.msg = response.responseDesc;
					responseParams.status = "00".equals(response.responseCode);
					responseParams.aepsTransactionSbm = new AepsTransactionSbm();
					responseParams.balance = response.accAvalibBalance / 100.0 + "#" + response.accLEDBalance / 100.0;
					responseParams.aepsTransactionSbm.setBankRefNo(rrn);
					responseParams.aepsTransactionSbm.setAuthid(response.txnAuthCode);
				}
			} else {
				logger.error("invalid processing code.");
			}
			logger.info("KIOSK_response :: {}", mapper.writeValueAsString(responseParams));
			return responseParams;
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("exiting end");
		return responseParams;
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

	private final Request populateRequest(final RequestParamAeps message, final Request request) throws JAXBException {
		final UserAthnMstr agent = agentRepository.findById(message.tid).get();
		final boolean isVid = message.aadharNo.charAt(0) == '1';
		final String uid_vid = message.aadharNo.substring(1);
		agent.getUserAdditionalDetailses().stream().findFirst().ifPresent(det -> {
			request.postalCode = det.getId().getPincode();
			request.cardAcptNameLOC = det.getId().getAddrLneOne();
		});
		request.msgType = "0200";
		request.txnAmount = Generator.amountToFormattedString12(new BigDecimal(message.amount).multiply(new BigDecimal(100.0)).toBigInteger());
		request.cardNo = message.bankCode + (isVid ? ("2" + "999999999999") : ("0" + uid_vid.substring(uid_vid.length() - 12)));
		request.uidVidNo = uid_vid;
		request.txnDateTime = ISO8583DateFormat.getTransmissionTime(message.date);
		request.stan = message.stan.substring(6);
		request.time = ISO8583DateFormat.getLocalTime(message.date);
		request.date = ISO8583DateFormat.getLocalDate(message.date);
		request.srcType = "T";
		request.merchantType = "6012";
		request.entryMode = "019";
		request.serviceCondition = "05";
		request.rrn = message.stan;
		request.agentID = message.mid;
		request.terminalID = message.tid;
		request.acqurierInstId = acqId;
		request.cardAcptID = merchantId;
		request.merchantPassCode = merchantPass;
		request.fromAccountNo = "*";
		request.toAccountNo = "*";
		request.currencyCode = "356";
		request.authIndicator = "2";
		if (!"RE".equalsIgnoreCase(message.txType)) {
			final PidData bioData = JaxbUtil.unMarshalPlain(message.pidData);
			final TLV authFactorTlv = new TLV();
			authFactorTlv.put("001", "nnnyFMRnn");
			authFactorTlv.put("005", "G");
			authFactorTlv.put("006", request.postalCode);
			authFactorTlv.put("008", bioData.data.type);
			authFactorTlv.put("009", merchantId);
			authFactorTlv.put("010", bioData.deviceInfo.dpId);
			authFactorTlv.put("011", bioData.deviceInfo.rdsId);
			authFactorTlv.put("012", bioData.deviceInfo.rdsVer);
			authFactorTlv.put("013", bioData.deviceInfo.dc);
			authFactorTlv.put("014", bioData.deviceInfo.mi);
			authFactorTlv.put("015", "FPD");

			request.mcData = "001" + String.format("%04d", bioData.deviceInfo.mc.length()) + bioData.deviceInfo.mc;
			request.authFactor = authFactorTlv.build();
			request.keyData = "001" + String.format("%03d", bioData.skey.value.length()) + bioData.skey.value + "002" + String.format("%03d", bioData.skey.ci.length())
					+ bioData.skey.ci + "003" + String.format("%03d", bioData.hmac.length()) + bioData.hmac;
			request.fingerData = "001" + String.format("%04d", bioData.data.value.length()) + bioData.data.value;
		}
		return request;
	}

	private final Request populateRev(final RequestParamAeps message, final Request request) throws JAXBException {
		final UserAthnMstr agent = agentRepository.findById(message.tid).get();
		final boolean isVid = message.aadharNo.charAt(0) == '1';
		final String uid_vid = message.aadharNo.substring(1);
		agent.getUserAdditionalDetailses().stream().findFirst().ifPresent(det -> {
			request.postalCode = det.getId().getPincode();
			request.cardAcptNameLOC = det.getId().getAddrLneOne();
		});
		request.msgType = "0200";
		request.txnAmount = Generator.amountToFormattedString12(new BigDecimal(message.amount).multiply(new BigDecimal(100.0)).toBigInteger());
		// request.cardNo = message.bankCode + (isVid ? ("2" + "999999999999") : ("0" +
		// uid_vid));
		logger.info("@@@@@@@ request.cardNo :: {}", request.cardNo);
		request.uidVidNo = uid_vid;
		request.txnDateTime = ISO8583DateFormat.getTransmissionTime(message.date);
		request.stan = message.stan.substring(6);
		request.time = ISO8583DateFormat.getLocalTime(message.date);
		request.date = ISO8583DateFormat.getLocalDate(message.date);
		request.srcType = "T";
		request.merchantType = "6012";
		request.entryMode = "019";
		request.serviceCondition = "05";
		request.rrn = message.stan;
		request.agentID = message.mid;
		request.terminalID = message.tid;
		request.acqurierInstId = acqId;
		request.cardAcptID = merchantId;
		request.merchantPassCode = merchantPass;
		request.fromAccountNo = "*";
		request.toAccountNo = "*";
		request.currencyCode = "356";
		request.authIndicator = "2";
		if (!"RE".equalsIgnoreCase(message.txType)) {
			final PidData bioData = JaxbUtil.unMarshalPlain(message.pidData);
			final TLV authFactorTlv = new TLV();
			authFactorTlv.put("001", "nnnyFMRnn");
			authFactorTlv.put("005", "G");
			authFactorTlv.put("006", request.postalCode);
			authFactorTlv.put("008", bioData.data.type);
			authFactorTlv.put("009", merchantId);
			authFactorTlv.put("010", bioData.deviceInfo.dpId);
			authFactorTlv.put("011", bioData.deviceInfo.rdsId);
			authFactorTlv.put("012", bioData.deviceInfo.rdsVer);
			authFactorTlv.put("013", bioData.deviceInfo.dc);
			authFactorTlv.put("014", bioData.deviceInfo.mi);
			authFactorTlv.put("015", "FPD");

			request.mcData = "001" + String.format("%04d", bioData.deviceInfo.mc.length()) + bioData.deviceInfo.mc;
			request.authFactor = authFactorTlv.build();
			request.keyData = "001" + String.format("%03d", bioData.skey.value.length()) + bioData.skey.value + "002" + String.format("%03d", bioData.skey.ci.length())
					+ bioData.skey.ci + "003" + String.format("%03d", bioData.hmac.length()) + bioData.hmac;
			request.fingerData = "001" + String.format("%04d", bioData.data.value.length()) + bioData.data.value;
		}
		return request;
	}

	@PostMapping(path = "/aeps/aepsBanks", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseParamAeps getAepsBanks(@RequestBody RequestParamAeps message) {
		final ResponseParamAeps responseParams = new ResponseParamAeps();
		responseParams.status = true;
		responseParams.msg = "SUCCESS";
		responseParams.respcode = "00";

		List<AadhaarBankBin> bankData = bankRepository.findAll();
		ArrayList<CodeValue> list = new ArrayList<CodeValue>();
		for (AadhaarBankBin obj : bankData) {
			list.add(new CodeValue(obj.getBankBinId(), obj.getBankName()));
		}
		responseParams.codeValues = list;
		return responseParams;
	}

	TransactionDetails addTransactionDetails(RequestParamAeps message, ResponseParamAeps responseParams) {
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());

		TransactionDetails transaction = new TransactionDetails();
		transaction.setTxnType(responseParams.aepsTransactionSbm.getId().getTransactionType() != null ? responseParams.aepsTransactionSbm.getId().getTransactionType() : null);
		if (responseParams.status == true) {
			transaction.setStatus("Y");
			transaction.setTransactionStatus("SUCCESS");
		} else {
			transaction.setStatus("N");
			transaction.setTransactionStatus("FAILED");
		}
		transaction.setRrn(message.stan != null ? message.stan : null);
		transaction.setBalance(responseParams.balance != null ? responseParams.balance : null);
		transaction.setPosId(responseParams.aepsTransactionSbm.getId().getPosId() != null ? responseParams.aepsTransactionSbm.getId().getPosId() : null);
		transaction.setStan(responseParams.aepsTransactionSbm.getStan() != 0 ? responseParams.aepsTransactionSbm.getStan() : null);
		transaction.setUsername(responseParams.aepsTransactionSbm.getUsername() != null ? responseParams.aepsTransactionSbm.getUsername() : null);
		transaction.setMid(responseParams.aepsTransactionSbm.getMerchantId() != null ? responseParams.aepsTransactionSbm.getMerchantId() : null);
		transaction.setTxnDate(String.valueOf(responseParams.aepsTransactionSbm.getTransactionDate()));
		transaction.setMobileno(responseParams.aepsTransactionSbm.getUsername() != null ? responseParams.aepsTransactionSbm.getUsername() : null);
		transaction.setAadharNo(responseParams.aepsTransactionSbm.getAadharno() != null ? responseParams.aepsTransactionSbm.getAadharno() : null);
		transaction.setBankCode(responseParams.aepsTransactionSbm.getBankcode() != null ? responseParams.aepsTransactionSbm.getBankcode() : null);
		transaction.setAmount(responseParams.aepsTransactionSbm.getAmount() != null ? responseParams.aepsTransactionSbm.getAmount() : null);
		transaction.setLocation(responseParams.aepsTransactionSbm.getPoslocation() != null ? responseParams.aepsTransactionSbm.getPoslocation() : null);
		transaction.setCurrencycode(responseParams.aepsTransactionSbm.getCurrencycode() != 0 ? responseParams.aepsTransactionSbm.getCurrencycode() : null);
		transaction.setNote(responseParams.aepsTransactionSbm.getNote() != null ? responseParams.aepsTransactionSbm.getNote() : null);
		transaction.setInvoiceNo(responseParams.aepsTransactionSbm.getInvoiceNo() != null ? responseParams.aepsTransactionSbm.getInvoiceNo() : null);
		transaction.setResponseCode(responseParams.aepsTransactionSbm.getResponsecode() != null ? responseParams.aepsTransactionSbm.getResponsecode() : null);
		transaction.setResponseMessage(responseParams.aepsTransactionSbm.getResponsemsg() != null ? responseParams.aepsTransactionSbm.getResponsemsg() : null);
		transaction.setBankRefNo(responseParams.aepsTransactionSbm.getBankRefNo() != null ? responseParams.aepsTransactionSbm.getBankRefNo() : null);
		transaction.setDevStatus(responseParams.aepsTransactionSbm.getDevStatus() != null ? responseParams.aepsTransactionSbm.getDevStatus() : null);
		transaction.setTid(responseParams.aepsTransactionSbm.getMatmTid() != null ? responseParams.aepsTransactionSbm.getMatmTid() : null);
		transaction.setSessionId(responseParams.sessionId != null ? responseParams.sessionId : null);
		transaction.setRequestcode(message.requestcode != null ? message.requestcode : null);
		transaction.setCreatedOn(ts);
		transaction.setModifiedOn(ts);
		transaction.setImei(message.imei != null ? message.imei : null);
		Map<String, Object> txnDispenseObj = (Map<String, Object>) message.hmData.get("txnDispense");
		String txnId = (String) txnDispenseObj.get("txnid");
		transaction.setTxnid(txnId != null ? txnId : null);

		return transaction;
	}

	@GetMapping(path = "/aeps/test")
	public String test() {
		return "Welcome to DBU switch...!";
	}
}