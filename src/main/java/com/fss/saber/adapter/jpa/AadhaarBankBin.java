package com.fss.saber.adapter.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AADHAAR_BANK_BIN")
public class AadhaarBankBin {

	@Column(name = "BANK_ID")
	private String bankId;

	@Id
	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "BANK_BIN_ID")
	private String bankBinId;

	@Column(name = "ACTIVATION")
	private char activation;

	@Column(name = "TRAN_CODE")
	private String trancode;

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBinId() {
		return bankBinId;
	}

	public void setBankBinId(String bankBinId) {
		this.bankBinId = bankBinId;
	}

	public char getActivation() {
		return activation;
	}

	public void setActivation(char activation) {
		this.activation = activation;
	}

	public String getTrancode() {
		return trancode;
	}

	public void setTrancode(String trancode) {
		this.trancode = trancode;
	}

	@Override
	public String toString() {
		return "AadhaarBankBin [bankId=" + bankId + ", bankName=" + bankName + ", bankBinId=" + bankBinId
				+ ", activation=" + activation + ", trancode=" + trancode + "]";
	}
}
