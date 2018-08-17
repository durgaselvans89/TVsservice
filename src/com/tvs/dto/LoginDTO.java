package com.tvs.dto;

public class LoginDTO {
	
	private String username = null;
	private String password = null;
	private String engineerId = null;
	private String engineerName = null;
	private String responseCode = null;
	private String serverDateTime = null;
	private String error_code = null;
	
	public String getServerDateTime() {
		return serverDateTime;
	}
	
	public void setServerDateTime(String serverDateTime) {
		this.serverDateTime = serverDateTime;
	}
	
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String errorCode) {
		error_code = errorCode;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getEngineerId() {
		return engineerId;
	}
	public void setEngineerId(String engineerId) {
		this.engineerId = engineerId;
	}
	public String getEngineerName() {
		return engineerName;
	}
	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
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

}
