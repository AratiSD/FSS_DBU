package com.fss.saber.adapter.model;

import java.util.ArrayList;

public class ResponseParamAeps {
	public boolean status;
	public String msg;
	public String respcode;
	public String balance;
	public String data;
	public String msData;
	public String sessionId;
	public AepsTransactionSbm aepsTransactionSbm;
	public ArrayList<CodeValue> codeValues = new ArrayList<>();
}
