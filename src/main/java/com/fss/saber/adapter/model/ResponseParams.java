package com.fss.saber.adapter.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseParams {

	private boolean status;
	private String msg;
	private String token;
	private String rrn;
	private String cardno;
	private String arpcdata;
	private String printmsg;
	private String aid;
	private String tvr;
	private String tsi;
	private String authid;
	private String date;
	private String respCode;
	private String hitachiResCode;
	private String ed;
	private String invoiceNumber;
	private String batchNo;
	private String applName;
	private String serviceBin;
	private ArrayList<ResponseParams> transhistory = new ArrayList<>();
	private String sessionId;
	private String mkskKeys;

	public ResponseParams() {
	}

	public ResponseParams(RequestParams request) {
		this.token = request.getRrn();
		this.rrn = request.getRrn();
		this.serviceBin = "Offus";
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getArpcdata() {
		return arpcdata;
	}

	public void setArpcdata(String arpcdata) {
		this.arpcdata = arpcdata;
	}

	public String getPrintmsg() {
		return printmsg;
	}

	public void setPrintmsg(String printmsg) {
		this.printmsg = printmsg;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getTvr() {
		return tvr;
	}

	public void setTvr(String tvr) {
		this.tvr = tvr;
	}

	public String getTsi() {
		return tsi;
	}

	public void setTsi(String tsi) {
		this.tsi = tsi;
	}

	public String getAuthid() {
		return authid;
	}

	public void setAuthid(String authid) {
		this.authid = authid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getHitachiResCode() {
		return hitachiResCode;
	}

	public void setHitachiResCode(String hitachiResCode) {
		this.hitachiResCode = hitachiResCode;
	}

	public String getEd() {
		return ed;
	}

	public void setEd(String ed) {
		this.ed = ed;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getApplName() {
		return applName;
	}

	public void setApplName(String applName) {
		this.applName = applName;
	}

	public String getServiceBin() {
		return serviceBin;
	}

	public void setServiceBin(String serviceBin) {
		this.serviceBin = serviceBin;
	}

	public ArrayList<ResponseParams> getTranshistory() {
		return transhistory;
	}

	public void setTranshistory(ArrayList<ResponseParams> transhistory) {
		this.transhistory = transhistory;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getMkskKeys() {
		return mkskKeys;
	}

	public void setMkskKeys(String mkskKeys) {
		this.mkskKeys = mkskKeys;
	}

}
