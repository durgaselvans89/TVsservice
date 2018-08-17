package com.tvs.module;

/**
 * 
 * @author XRG
 * @version 1.0
 */
public class CustomException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String exception = null;
	
	int code = 0;
	
	
	/**
	 * Constructor method
	 * @param exception
	 */
	public CustomException(String exception, int code) {
		this.exception = exception;
		this.code = code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	/**
	 * Method to get the exception string
	 * 
	 * @return String
	 */
	@Override
	public String getMessage()
	{
		return exception;
	}
}
