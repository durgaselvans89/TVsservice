package com.tvs.dto;

import java.io.Serializable;

public class RC17OR52DTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String serialNumber = null;
	private String rcDate = null;
	private String rcType = null;
	private String remarks = null;
	private String username = null;
	private String status;
	
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getRcDate() {
		return rcDate;
	}
	public void setRcDate(String rcDate) {
		this.rcDate = rcDate;
	}
	public String getRcType() {
		return rcType;
	}
	public void setRcType(String rcType) {
		this.rcType = rcType;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
