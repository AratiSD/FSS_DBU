package com.fss.saber.adapter.model;

public class AepsTransactionSbm implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private AepsTransactionSbmId id;
	private String username;
	private String merchantId;
	private int stan;
	private long transactionDate;
	private String msidn;
	private String aadharno;
	private String bankcode;
	private Double amount;
	private Double charges;
	private String poslocation;
	private Integer currencycode;
	private String note;
	private String invoiceNo;
	private String responsecode;
	private String responsemsg;
	private String authid;
	private String bankRefNo;
	private String status;
	private String mdrStatus;
	private String sourceAccount;
	private String targetAccount;
	private String matmTid;
	private boolean isOnus;
	private String instSettleStatus;
	private String instCommSettleStatus;
	private Double mdMdr;
	private Double adMdr;
	private String custMono;
	private String lastTxnStatus;
	private Integer typedemon1;
	private Integer typedemon2;
	private Integer typedemon3;
	private Integer typedemon4;
	private Integer typedemon1count;
	private Integer typedemon2count;
	private Integer typedemon3count;
	private Integer typedemon4count;
	private String devStatus;
	private String isLtsUpdate;

	public AepsTransactionSbmId getId() {
		return id;
	}

	public void setId(AepsTransactionSbmId id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public int getStan() {
		return stan;
	}

	public void setStan(int stan) {
		this.stan = stan;
	}

	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getMsidn() {
		return msidn;
	}

	public void setMsidn(String msidn) {
		this.msidn = msidn;
	}

	public String getAadharno() {
		return aadharno;
	}

	public void setAadharno(String aadharno) {
		this.aadharno = aadharno;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getCharges() {
		return charges;
	}

	public void setCharges(Double charges) {
		this.charges = charges;
	}

	public String getPoslocation() {
		return poslocation;
	}

	public void setPoslocation(String poslocation) {
		this.poslocation = poslocation;
	}

	public Integer getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(Integer currencycode) {
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

	public String getResponsecode() {
		return responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	public String getResponsemsg() {
		return responsemsg;
	}

	public void setResponsemsg(String responsemsg) {
		this.responsemsg = responsemsg;
	}

	public String getAuthid() {
		return authid;
	}

	public void setAuthid(String authid) {
		this.authid = authid;
	}

	public String getBankRefNo() {
		return bankRefNo;
	}

	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMdrStatus() {
		return mdrStatus;
	}

	public void setMdrStatus(String mdrStatus) {
		this.mdrStatus = mdrStatus;
	}

	public String getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public String getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(String targetAccount) {
		this.targetAccount = targetAccount;
	}

	public String getMatmTid() {
		return matmTid;
	}

	public void setMatmTid(String matmTid) {
		this.matmTid = matmTid;
	}

	public boolean isOnus() {
		return isOnus;
	}

	public void setOnus(boolean isOnus) {
		this.isOnus = isOnus;
	}

	public String getInstSettleStatus() {
		return instSettleStatus;
	}

	public void setInstSettleStatus(String instSettleStatus) {
		this.instSettleStatus = instSettleStatus;
	}

	public String getInstCommSettleStatus() {
		return instCommSettleStatus;
	}

	public void setInstCommSettleStatus(String instCommSettleStatus) {
		this.instCommSettleStatus = instCommSettleStatus;
	}

	public Double getMdMdr() {
		return mdMdr;
	}

	public void setMdMdr(Double mdMdr) {
		this.mdMdr = mdMdr;
	}

	public Double getAdMdr() {
		return adMdr;
	}

	public void setAdMdr(Double adMdr) {
		this.adMdr = adMdr;
	}

	public String getCustMono() {
		return custMono;
	}

	public void setCustMono(String custMono) {
		this.custMono = custMono;
	}

	public String getLastTxnStatus() {
		return lastTxnStatus;
	}

	public void setLastTxnStatus(String lastTxnStatus) {
		this.lastTxnStatus = lastTxnStatus;
	}

	public Integer getTypedemon1() {
		return typedemon1;
	}

	public void setTypedemon1(Integer typedemon1) {
		this.typedemon1 = typedemon1;
	}

	public Integer getTypedemon2() {
		return typedemon2;
	}

	public void setTypedemon2(Integer typedemon2) {
		this.typedemon2 = typedemon2;
	}

	public Integer getTypedemon3() {
		return typedemon3;
	}

	public void setTypedemon3(Integer typedemon3) {
		this.typedemon3 = typedemon3;
	}

	public Integer getTypedemon4() {
		return typedemon4;
	}

	public void setTypedemon4(Integer typedemon4) {
		this.typedemon4 = typedemon4;
	}

	public Integer getTypedemon1count() {
		return typedemon1count;
	}

	public void setTypedemon1count(Integer typedemon1count) {
		this.typedemon1count = typedemon1count;
	}

	public Integer getTypedemon2count() {
		return typedemon2count;
	}

	public void setTypedemon2count(Integer typedemon2count) {
		this.typedemon2count = typedemon2count;
	}

	public Integer getTypedemon3count() {
		return typedemon3count;
	}

	public void setTypedemon3count(Integer typedemon3count) {
		this.typedemon3count = typedemon3count;
	}

	public Integer getTypedemon4count() {
		return typedemon4count;
	}

	public void setTypedemon4count(Integer typedemon4count) {
		this.typedemon4count = typedemon4count;
	}

	public String getDevStatus() {
		return devStatus;
	}

	public void setDevStatus(String devStatus) {
		this.devStatus = devStatus;
	}

	public String getIsLtsUpdate() {
		return isLtsUpdate;
	}

	public void setIsLtsUpdate(String isLtsUpdate) {
		this.isLtsUpdate = isLtsUpdate;
	}

}
