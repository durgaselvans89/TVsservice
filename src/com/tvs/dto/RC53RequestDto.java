package com.tvs.dto;

public class RC53RequestDto {

	private String serailNumber = null;
	
	private String result = null;
	
	private String complete_Date = null;
	
	private String cold_boot = "No";
	
	private String poh = "No";
	
	private String dts_name = null;
	
	private String caseid= null;
	
	private String esurvey_Education = "No";
	
	private String activity_Done = null;
	
	private String customer_satisfied = "No";
	
	private String signature_filename = null;
	
	private String part_transaction = null;
	
	private String additional_activity = null;
	
	private String customerCommants = null;
	
	private String customer_Mail_Id = "";
	
	private String customer_name = null;
	
	private String customer_job_title = "";
	
	private String additional_information = null;
	
	private String ACCEPT = "No";
	
	public String getACCEPT() {
		return ACCEPT;
	}
	
	public void setACCEPT(String aCCEPT) {
		ACCEPT = aCCEPT;
	}
	
	public String getAdditional_information() {
		return additional_information;
	}
	
	public void setAdditional_information(String additionalInformation) {
		additional_information = additionalInformation;
	}
	
	public String getCustomer_job_title() {
		return customer_job_title;
	}
	
	public void setCustomer_job_title(String customerJobTitle) {
		customer_job_title = customerJobTitle;
	}
	
	public String getCustomer_Mail_Id() {
		return customer_Mail_Id;
	}
	
	public void setCustomer_Mail_Id(String customerMailId) {
		customer_Mail_Id = customerMailId;
	}
	
	public String getCustomer_name() {
		return customer_name;
	}
	
	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}
	
	public String getCustomerCommants() {
		return customerCommants;
	}
	
	public void setCustomerCommants(String userCommants) {
		this.customerCommants = userCommants;
	}
	
	public String getPart_transaction() {
		return part_transaction;
	}
	
	public void setPart_transaction(String partTransaction) {
		part_transaction = partTransaction;
	}
	
	public String getAdditional_activity() {
		return additional_activity;
	}
	
	public void setAdditional_activity(String additionalActivity) {
		additional_activity = additionalActivity;
	}

	public String getSignature_filename() {
		return signature_filename;
	}

	public void setSignature_filename(String signature_filename) {
		this.signature_filename = signature_filename;
	}

//	public String getActivity_msg() {
//		return activity_msg;
//	}
//
//	public void setActivity_msg(String activityMsg) {
//		activity_msg = activityMsg;
//	}

	public String getCustomer_satisfied() {
		return customer_satisfied;
	}

	public void setCustomer_satisfied(String customerSatisfied) {
		customer_satisfied = customerSatisfied;
	}

//	public String getTentative_Pickup_Date() {
//		return tentative_Pickup_Date;
//	}
//	
//	public void setTentative_Pickup_Date(String tentativePickupDate) {
//		tentative_Pickup_Date = tentativePickupDate;
//	}
	
//	public String getParts_Collected() {
//		return parts_Collected;
//	}
//	
//	public void setParts_Collected(String Parts_Collected) {
//		parts_Collected = Parts_Collected;
//	}
	
//	public String getReason() {
//		return reason;
//	}
	
	public String getActivity_Done() {
		return activity_Done;
	}
	
	public String getCaseid() {
		return caseid;
	}
	
	public String getCold_boot() {
		return cold_boot;
	}
	
	public String getComplete_Date() {
		return complete_Date;
	}
	
	public String getDts_name() {
		return dts_name;
	}
	
	public String getEsurvey_Education() {
		return esurvey_Education;
	}
	
//	public String getPart_Involved() {
//		return part_Involved;
//	}
	
//	PUBLIC STRING GETPART_NUMBER() {
//		RETURN PART_NUMBER;
//	}
	
//	public String getPart_Replaced() {
//		return part_Replaced;
//	}
	
	public String getPoh() {
		return poh;
	}
	
//	public String getPpid() {
//		return ppid;
//	}
	
//	public String getQuantity() {
//		return quantity;
//	}
	
	public String getResult() {
		return result;
	}
	
	public String getSerailNumber() {
		return serailNumber;
	}
	
	public void setActivity_Done(String activityDone) {
		activity_Done = activityDone;
	}
	
	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}
	
	public void setCold_boot(String coldBoot) {
		cold_boot = coldBoot;
	}
	
	public void setComplete_Date(String completeDate) {
		complete_Date = completeDate;
	}
	
	public void setDts_name(String dtsName) {
		dts_name = dtsName;
	}
	
	public void setEsurvey_Education(String esurveyEducation) {
		esurvey_Education = esurveyEducation;
	}
	
//	public void setPart_Involved(String partInvolved) {
//		part_Involved = partInvolved;
//	}
	
//	public void setPart_Number(String partNumber) {
//		part_Number = partNumber;
//	}
	
//	public void setPart_Replaced(String partReplaced) {
//		part_Replaced = partReplaced;
//	}
	
	public void setPoh(String poh) {
		this.poh = poh;
	}
	
//	public void setPpid(String ppid) {
//		this.ppid = ppid;
//	}
	
//	public void setQuantity(String quantity) {
//		this.quantity = quantity;
//	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public void setSerailNumber(String serailNumber) {
		this.serailNumber = serailNumber;
	}
	
//	public void setReason(String reason) {
//		this.reason = reason;
//	}
}
