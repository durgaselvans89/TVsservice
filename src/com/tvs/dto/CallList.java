package com.tvs.dto;

import java.io.Serializable;

public class CallList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String appointment_Date = null;
	
	private String numberOf_Calls = null;
	
	private CallListDTO[] callListDto = null;
	
	private boolean partStatus = false;
	
	private int partCount = 0;
	
	public int getPartCount() {
		return partCount;
	}
	
	public void setPartCount(String partCount) {
		if(partCount.length()>0)
			this.partCount = Integer.parseInt(partCount);
	}
	
	public boolean isPartStatus() {
		return partStatus;
	}
	
	public void setPartStatus(String partStatus) {
		if(partStatus.compareTo("ISSUED") == 0)
			this.partStatus = true;
	}
	
	public String getNumberOf_Calls() {
		return numberOf_Calls;
	}
	
	public String getAppointment_Date() {
		return appointment_Date;
	}
	
	public CallListDTO[] getCallListDto() {
		return callListDto;
	}
	
	public void setAppointment_Date(String appointmentDate) {
		appointment_Date = appointmentDate;
	}
	
	public void setCallListDto(CallListDTO[] callListDto) {
		this.callListDto = callListDto;
	}
	
	public void setNumberOf_Calls(String numberOfCalls) {
		numberOf_Calls = numberOfCalls;
	}
	
}
