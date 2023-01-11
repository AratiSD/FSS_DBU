package com.fss.saber.adapter.model;

public class AepsTransactionSbmId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String posId;
	private String rrn;
	private String transactionType;

	public AepsTransactionSbmId() {
	}

	public AepsTransactionSbmId(String posId, String rrn) {
		this.posId = posId;
		this.rrn = rrn;
	}

	public String getPosId() {
		return this.posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getRrn() {
		return this.rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((posId == null) ? 0 : posId.hashCode());
		result = prime * result + ((rrn == null) ? 0 : rrn.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AepsTransactionSbmId other = (AepsTransactionSbmId) obj;
		if (posId == null) {
			if (other.posId != null)
				return false;
		} else if (!posId.equals(other.posId))
			return false;
		if (rrn == null) {
			if (other.rrn != null)
				return false;
		} else if (!rrn.equals(other.rrn))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		return true;
	}

}
