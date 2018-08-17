package com.tvs.dto;

import java.io.Serializable;

public class CallListDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customer_Name = null;
	
	private String Orgination_Name = null;
	
	private String serial_Number = null;
	
	private String constact_Number = null;
	
	private String engineer_Name = null;
	
	private String technician_Id = null;
	
	private String sla = null;
	
	private String sTag = null;
	
	private String alterNative_Contact_Number = null;
	
	private String customer_Address = null;
	
	private String dispatch_Time = null;
	
	private String part_ETA_Time = null;
	
	private String summery = null;
	
	private String dsp_Comments = null;
	
	private String long_Description = null;
	
	private String product_Code = null;
	
	private String tech_Support_Name = null;
	
	private String customer_Date_Time = null;
	
	private String parts_Number = null;
	
	private boolean flag_rc17 = false;
	
	private boolean isPartIssued = true;
	
	private int partCount = 0;
	
	private String[] partNumber = null;
	
	private String[] uniqueId = null;
	
	private boolean isNoPart = true;
	
	public boolean isNoPart() {
		return isNoPart;
	}
	
	public void setNoPart(String isNoPart) {
		if(null != isNoPart){
			isNoPart = isNoPart.trim().toLowerCase();
			if(isNoPart.compareTo("no") == 0)
				this.isNoPart = false;
		}
	}
	
	public int getPartCount() {
		return partCount;
	}
	
	public void setPartCount(String partCount) {
		partCount = partCount.trim();
		if(partCount.length()>0)
			this.partCount = Integer.parseInt(partCount);
	}
	
	public boolean isPartIssued() {
		return isPartIssued;
	}
	
	public void setPartIssued(String isPartIssued) {
		isPartIssued = isPartIssued.trim();
		if(isPartIssued.compareTo("ISSUED") == 0)
			this.isPartIssued = false;
	}
	
	public String[] getPartNumber() {
		return partNumber;
	}
	
	public String[] getUniqueId() {
		return uniqueId;
	}
	
	public void setPartNumber(String[] partNumber) {
		this.partNumber = partNumber;
	}
	
	public void setUniqueId(String[] uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public boolean getFlag_rc17() {
		return flag_rc17;
	}

	public void setFlag_rc17(String flagRc17) {
		if(flagRc17.toLowerCase().compareTo("yes") == 0)
			this.flag_rc17 = true;
	}

	public String getParts_Number() {
		return parts_Number;
	}
	
	public void setParts_Number(String partsNumber) {
		parts_Number = partsNumber;
	}
	
	public String getCustomer_Date_Time() {
		return customer_Date_Time;
	}
	
	public void setCustomer_Date_Time(String customerDateTime) {
		customer_Date_Time = customerDateTime;
	}
	
	public String getTech_Support_Name() {
		return tech_Support_Name;
	}
	
	public void setTech_Support_Name(String techSupportName) {
		tech_Support_Name = techSupportName;
	}
	
	public String getProduct_Code() {
		return product_Code;
	}
	
	public void setProduct_Code(String productCode) {
		product_Code = productCode;
	}
	
	public String getLong_Description() {
		return long_Description;
	}
	
	public void setLong_Description(String longDescription) {
		long_Description = longDescription;
	}
	
	public String getDsp_Comments() {
		return dsp_Comments;
	}
	
	public void setDsp_Comments(String dspComments) {
		dsp_Comments = dspComments;
	}
	
	public String getSummery() {
		return summery;
	}
	
	public void setSummery(String summery) {
		this.summery = summery;
	}
	
	public String getPart_ETA_Time() {
		return part_ETA_Time;
	}
	
	public void setPart_ETA_Time(String partETATime) {
		part_ETA_Time = partETATime;
	}
	
	public String getDispatch_Time() {
		return dispatch_Time;
	}
	
	public void setDispatch_Time(String dispatchTime) {
		dispatch_Time = dispatchTime;
	}
	
	
	
	public String getCustomer_Address() {
		return customer_Address;
	}
	
	public void setCustomer_Address(String customerAddress) {
		customer_Address = customerAddress;
	}
	
	public String getAlterNative_Contact_Number() {
		return alterNative_Contact_Number;
	}
	
	public void setAlterNative_Contact_Number(String alterNativeContactNumber) {
		alterNative_Contact_Number = alterNativeContactNumber;
	}
	
	public String getsTag() {
		return sTag;
	}
	
	public void setsTag(String sTag) {
		this.sTag = sTag;
	}
	
	public String getSla() {
		return sla;
	}
	
	public void setSla(String sla) {
		this.sla = sla;
	}
	
	public String getTechnician_Id() {
		return technician_Id;
	}
	
	public void setTechnician_Id(String technicianId) {
		technician_Id = technicianId;
	}
	
	public String getEngineer_Name() {
		return engineer_Name;
	}
	
	
	public void setEngineer_Name(String engineerName) {
		engineer_Name = engineerName;
	}
	
	
	
	public String getCustomer_Name() {
		return customer_Name;
	}
	
	public void setConstact_Number(String constactNumber) {
		constact_Number = constactNumber;
	}
	
	public String getConstact_Number() {
		return constact_Number;
	}
	
	public void setCustomer_Name(String customerName) {
		customer_Name = customerName;
	}
	
	public String getOrgination_Name() {
		return Orgination_Name;
	}
	
	public void setOrgination_Name(String orginationName) {
		Orgination_Name = orginationName;
	}
	
	public String getSerial_Number() {
		return serial_Number;
	}
	
	public void setSerial_Number(String serialNumber) {
		serial_Number = serialNumber;
	}
	
	
}
