package com.tvs.dto;


public class MasterDto {

	private String[] parts = null;
	
	private int[] partsCount = null;
	
	private String[] reason = null;
	
	private String[] activity_done = null;
	
	private String[] results = null;
	
	private String[] selectedPartUsage = null;
	
	private String[] ppid = null;
	
	private String[] part_Usage = null;
	
	private String[] additional_Activity = null;
	
	private CallResultsDto[] callResults = null;
	
	private boolean[] additional_Activity_Enabled = null;
	
	private String[] part_Usage_Id = null;
	
	private String[] selectedPartUsage_ID = null;
	
	
	public String[] getSelectedPartUsage_ID() {
		return selectedPartUsage_ID;
	}
	
	public void setSelectedPartUsage_ID(String[] selectedPartUsageID) {
		selectedPartUsage_ID = selectedPartUsageID;
	}
	
	public String[] getPart_Usage_Id() {
		return part_Usage_Id;
	}
	
	public void setPart_Usage_Id(String[] partUsageId) {
		part_Usage_Id = partUsageId;
	}
	
	public void setAdditional_Activity_Enabled(
			boolean[] additionalActivityEnabled) {
		additional_Activity_Enabled = additionalActivityEnabled;
	}
	
	public boolean[] getAdditional_Activity_Enabled() {
		if(null == additional_Activity_Enabled && null != additional_Activity)
			additional_Activity_Enabled = new boolean[additional_Activity.length];
		return additional_Activity_Enabled;
	}
	
	public String[] getAdditional_Activity() {
		return additional_Activity;
	}
	
	public void setAdditional_Activity(String[] additionalActivity) {
		additional_Activity = additionalActivity;
		if(null != additionalActivity)
			additional_Activity_Enabled = new boolean[additionalActivity.length];
	}
	
	public String[] getPart_Usage() {
		return part_Usage;
	}
	
	public void setPart_Usage(String[] partUsage) {
		part_Usage = partUsage;
	}
	
	public String[] getActivity_done() {
		return activity_done;
	}
	
	public void setActivity_done(String[] activityDone) {
		activity_done = activityDone;
	}
	
	public String[] getSelectedPartUsage() {
		return selectedPartUsage;
	}
	
	public String[] getPPID() {
		return ppid;
	}
	
	public void setSelectedPartUsage(String[] partUsage) {
		this.selectedPartUsage = partUsage;
	}
	
	public void setPPID(String[] ppid) {
		this.ppid = ppid;
	}
	
	public String[] getResults() {
		return results;
	}
	
	public void setResults(String[] results) {
		this.results = results;
	}
	
	public int[] getPartsCount() {
		return partsCount;
	}
	
	public void setPartsCount(int[] partsCount) {
		this.partsCount = partsCount;
	}
	
	public String[] getParts() {
		return parts;
	}
	
	public void setParts(String[] parts) {
		this.parts = parts;
	}
	
	public String[] getReason() {
		return reason;
	}
	
	public void setReason(String[] reason) {
		this.reason = reason;
	}
		
	public CallResultsDto[] getCallResults() {
		return callResults;
	}
	
	public void setCallResults(CallResultsDto[] callResults) {
		this.callResults = callResults;
	}
}
