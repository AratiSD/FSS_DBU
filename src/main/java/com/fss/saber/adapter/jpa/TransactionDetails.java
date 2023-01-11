package com.fss.saber.adapter.jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TRANSACTION_DETAILS")
public class TransactionDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "TRANSACTION_DETAILS_SEQ")
	@SequenceGenerator(name = "TRANSACTION_DETAILS_SEQ", sequenceName = "TRANSACTION_DETAILS_SEQ", allocationSize = 1)
	private int id;
	
	@Column(name="REQUEST_CODE")
	private String requestcode;
	
	@Column(name="USER_NAME")
	private String username;
	
	@Column(name="IMEI")
	private String imei;
	
	@Column(name="IMSI")
	private String imsi;
	
	@Column(name="SESSION_ID")
	private String sessionId;
	
	@Column(name="TXN_TYPE")
	private String txnType;
	
	@Column(name="AADHAR_NO")
	private String aadharNo;
	
	@Column(name="AMOUNT")
	private double amount;
	
	@Column(name="MOBILE_NO")
	private String mobileno;
	
	@Column(name="LAST_TXN_STATUS")
	private String lastTxnStatus;
	
	@Column(name="BANK_CODE")
	private String bankCode;
	
	@Column(name="IFSC_CODE")
	private String ifscCode;
	
	@Column(name="LOCATION")
	private String location;
	
	@Column(name="STAN")
	private int stan;
	
	@Column(name="POS_ID")
	private String posId;
	
	@Column(name="RRN")
	private String rrn;
	
	@Column(name="MID")
	private String mid;
	
	@Column(name="TID")
	private String tid;
	
	@Column(name="TXN_DATE")
	private String txnDate;
	
	@Column(name="OP")
	private String op;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="TXN_ID")
	private String txnid;
	
	@Column(name="TYPEDENOM_1")
	private String typedenom1;
	
	@Column(name="TYPEDENOM_2")
	private String typedenom2;
	
	@Column(name="TYPEDENOM_3")
	private String typedenom3;
	
	@Column(name="TYPEDENOM_4")
	private String typedenom4;
	
	@Column(name="TYPEDENOM1COUNT")
	private String typedenom1count;
	
	@Column(name="TYPEDENOM2COUNT")
	private String typedenom2count;
	
	@Column(name="TYPEDENOM3COUNT")
	private String typedenom3count;
	
	@Column(name="TYPEDENOM4COUNT")
	private String typedenom4count;
	
	@Column(name="CREATED_ON")
	private Timestamp createdOn;
	
	@Column(name="MODIFIED_ON")
	private Timestamp modifiedOn;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="EXCEPTION")
	private String exception;
	
	@Column(name="RESPONSE_CODE")
	private String responseCode;
	
	@Column(name="RESPONSE_MESSAGE")
	private String responseMessage;
	
	@Column(name="TRANSACTION_STATUS")
	private String transactionStatus;
	
	@Column(name="BALANCE")
	private String balance;
	
	@Column(name="CURRENCY_CODE")
	private int currencycode;
	
	@Column(name="NOTE")
	private String note;
	
	@Column(name="INVOICE_NO")
	private String invoiceNo;
	
	@Column(name="BANKREF_NO")
	private String bankRefNo;
	
	@Column(name="DEV_STATUS")
	private String devStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequestcode() {
		return requestcode;
	}

	public void setRequestcode(String requestcode) {
		this.requestcode = requestcode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getLastTxnStatus() {
		return lastTxnStatus;
	}

	public void setLastTxnStatus(String lastTxnStatus) {
		this.lastTxnStatus = lastTxnStatus;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStan() {
		return stan;
	}

	public void setStan(int stan) {
		this.stan = stan;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTxnid() {
		return txnid;
	}

	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}

	public String getTypedenom1() {
		return typedenom1;
	}

	public void setTypedenom1(String typedenom1) {
		this.typedenom1 = typedenom1;
	}

	public String getTypedenom2() {
		return typedenom2;
	}

	public void setTypedenom2(String typedenom2) {
		this.typedenom2 = typedenom2;
	}

	public String getTypedenom3() {
		return typedenom3;
	}

	public void setTypedenom3(String typedenom3) {
		this.typedenom3 = typedenom3;
	}

	public String getTypedenom4() {
		return typedenom4;
	}

	public void setTypedenom4(String typedenom4) {
		this.typedenom4 = typedenom4;
	}

	public String getTypedenom1count() {
		return typedenom1count;
	}

	public void setTypedenom1count(String typedenom1count) {
		this.typedenom1count = typedenom1count;
	}

	public String getTypedenom2count() {
		return typedenom2count;
	}

	public void setTypedenom2count(String typedenom2count) {
		this.typedenom2count = typedenom2count;
	}

	public String getTypedenom3count() {
		return typedenom3count;
	}

	public void setTypedenom3count(String typedenom3count) {
		this.typedenom3count = typedenom3count;
	}

	public String getTypedenom4count() {
		return typedenom4count;
	}

	public void setTypedenom4count(String typedenom4count) {
		this.typedenom4count = typedenom4count;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public int getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(int currencycode) {
		this.currencycode = currencycode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getBankRefNo() {
		return bankRefNo;
	}

	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}

	public String getDevStatus() {
		return devStatus;
	}

	public void setDevStatus(String devStatus) {
		this.devStatus = devStatus;
	}
}
