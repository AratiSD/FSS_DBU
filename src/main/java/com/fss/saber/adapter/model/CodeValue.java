package com.fss.saber.adapter.model;

public class CodeValue {
	public String code, value;
	
	
	public CodeValue() {
	}
	
	public CodeValue(String code, String value) {
		this.code = code;
		this.value = value;
	}
	@Override
	public String toString() {
		return "CodeValue [code=" + code + ", value=" + value + "]";
	}
}