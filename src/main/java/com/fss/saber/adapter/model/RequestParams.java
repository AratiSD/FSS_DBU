package com.fss.saber.adapter.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RequestParams {

	private String date;
	private String requestcode;
	private String username;
	private String password;
	private String srno;
	private String carddata;
	private String pindata;
	private String amt;
	private String imei;
	private String imsi;
	private String companyid;
	private String remark;
	private String reader;
	private String tag55;
	private String mid;
	private String sessionId;
	private String rrn;
	private HashMap<String, Object> hmData = new HashMap<>();
	
	private String op;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSrno() {
		return srno;
	}

	public void setSrno(String srno) {
		this.srno = srno;
	}

	public String getCarddata() {
		return carddata;
	}

	public void setCarddata(String carddata) {
		this.carddata = carddata;
	}

	public String getPindata() {
		return pindata;
	}

	public void setPindata(String pindata) {
		this.pindata = pindata;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
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

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getTag55() {
		return tag55;
	}

	public void setTag55(String tag55) {
		this.tag55 = tag55;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public HashMap<String, Object> getHmData() {
		return hmData;
	}

	public void setHmData(HashMap<String, Object> hmData) {
		this.hmData = hmData;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public static void main(String[] args) {
		String s = """
					<XL:
				""";
		System.out.println(s);
	}
}
