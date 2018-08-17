package com.tvs.module;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.tvs.activity.TVSEService;
import com.tvs.dto.CallList;
import com.tvs.dto.LoginDTO;
import com.tvs.dto.MasterDto;
import com.tvs.dto.RC53RequestDto;



public class TVSServiceManager {
	
	
	NetworkHelper helper = null;
	

	public TVSServiceManager(Context context) {
		helper = NetworkHelper.getInstance(context);
	}

	public LoginDTO login(Context context,String loginId, String password) throws CustomException {
		final String LOGINID_PARAM = "username";
		final String PASSWD_PARAM = "password";
		LoginDTO loginDTO = null;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(LOGINID_PARAM, loginId);
		map.put(PASSWD_PARAM, password);
		String url = Constants.LOGIN_URL + getRequestString(map);
		String responseString = helper.doGet(url);
		ResponseHandler handler = new ResponseHandler();
		loginDTO = handler.handleLoginResponse(responseString);
		loginDTO.setPassword(password);
		return loginDTO;
	}
	
	public CallList[] callList(String eng_Id) throws CustomException{
		final String ENGINEER_ID="engid";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ENGINEER_ID, eng_Id);
		CallList[] callList = null;
		String url = Constants.CALL_LIST_URL + getRequestString(map);

		try {
			String responseString = helper.doGet(url);
			ResponseHandler handler = new ResponseHandler();
			callList = handler.handleCallListResponse(responseString);
			handler = null;
		} catch (CustomException custom) {
			throw custom;
		}
		
		return callList;
	}
	
	public MasterDto getMasterValue() throws CustomException{
		String url = Constants.MASTER_URL;
		MasterDto masterDto = null;
		try{
			String responseString = helper.doGet(url);
			ResponseHandler handler = new ResponseHandler();
			masterDto = handler.handleMasterResponse(responseString);
			handler = null;
		}catch (CustomException custom) {
			throw custom;
		}
		return masterDto;
	}
	
	public String getRC17UrlString(String serialNumber, String remarks){
		final String SERIALNUMBER ="ser";
		final String REMARKS ="remarks";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SERIALNUMBER, serialNumber);
		map.put(REMARKS, remarks);
		return Constants.RC17_URL + getRequestString(map);
	}
	
	public boolean getRC17Value(String urlString) throws CustomException{
		boolean isSuccess = false;
		String responseString = helper.doGet(urlString);
		ResponseHandler handler = new ResponseHandler();
		isSuccess = handler.handleRCResponse(responseString);
		return isSuccess;
	}
	
	public boolean uploadImage(boolean netStatus,String resources){
		try{
			return helper.uploadImage(resources);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	public String getRc52UrlString(String serialNumber, String remarks){
		final String SERIALNUMBER ="ser";
		final String REMARKS ="remarks";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SERIALNUMBER, serialNumber);
		map.put(REMARKS, remarks);
		return Constants.RC52_URL + getRequestString(map);
	}
	
	public boolean getRC52Value(String urlString) throws CustomException{
		String responseString = helper.doGet(urlString);
		ResponseHandler handler = new ResponseHandler();
		return handler.handleRCResponse(responseString);
	}
	
	public String getRc53UrlString(RC53RequestDto rc53RequestDto){
		final String SERIALNUMBER ="ser";
		final String REAULTS ="RC";
		final String COMPLETE_DATE ="CmplDate";
		final String PART_TRANSACTION = "partusgtype";
		final String COLD_BOOT="ColtBoot";
		final String POH ="poh";
		final String DTSNAME="DTSName";
		final String CASEID="CaseId";
		final String CALL_WRRP_UP="CallWrapUp";
		final String CUSTOMER_SATISFIED = "CustSat";
		final String ACTIVITY_DONE="ActDone";
		final String ADDITIONAL_ACTIVITY= "AdditionalAct";
		final String ADDITIONAL_INFORMATION ="AdditionalInp";
		final String ACCEPT ="E-ROF";
		final String CUSTOMER_NAME ="CustName";
		final String CUSTOMER_MAILID="CustMail";
		final String CUSTOMER_JOB="CustJob";
		final String CUSTOMER_COMMENTS ="CustCmnts";
		
		final String SIGNATURE_FILENAME = "filename";
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SERIALNUMBER, rc53RequestDto.getSerailNumber());
		map.put(REAULTS, rc53RequestDto.getResult());
		map.put(COMPLETE_DATE, rc53RequestDto.getComplete_Date());
		map.put(PART_TRANSACTION,rc53RequestDto.getPart_transaction());
		map.put(COLD_BOOT, rc53RequestDto.getCold_boot());
		map.put(POH, rc53RequestDto.getPoh());
		map.put(DTSNAME, rc53RequestDto.getDts_name());
		map.put(CASEID, rc53RequestDto.getCaseid());
		map.put(CALL_WRRP_UP, rc53RequestDto.getEsurvey_Education());
		map.put(CUSTOMER_SATISFIED, rc53RequestDto.getCustomer_satisfied());
		map.put(ACTIVITY_DONE, rc53RequestDto.getActivity_Done());
		map.put(ADDITIONAL_ACTIVITY,rc53RequestDto.getAdditional_activity());
		map.put(ADDITIONAL_INFORMATION,rc53RequestDto.getAdditional_information());
		map.put(ACCEPT,rc53RequestDto.getACCEPT());
		map.put(CUSTOMER_NAME,rc53RequestDto.getCustomer_name());
		map.put(CUSTOMER_MAILID, rc53RequestDto.getCustomer_Mail_Id());
		map.put(CUSTOMER_JOB,rc53RequestDto.getCustomer_job_title());
		map.put(CUSTOMER_COMMENTS,rc53RequestDto.getCustomerCommants());
		map.put(SIGNATURE_FILENAME, rc53RequestDto.getSerailNumber()+".png");
		
		return Constants.CALL_COMPLETION_URL + getRequestString(map);
	}
	
	public byte[] getImageByte(String path){
		File sdImageMainDirPath = new File(Environment.getExternalStorageDirectory(), path);
		String fileNameWithPath = null;
		byte[] imageBytes = null;
		 if (sdImageMainDirPath.isDirectory()) {// make dir if not present
			 fileNameWithPath = sdImageMainDirPath.toString() + "/" + TVSEService.filesign + ".png";
			 Bitmap bitmapOrg = BitmapFactory.decodeFile(fileNameWithPath);
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 bitmapOrg.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
			 imageBytes = stream.toByteArray();
		 }
		 return imageBytes;
	}
	
	public boolean getRC53Value(byte[] imageBytes, String url) throws CustomException{
		String responseString = helper.doPostImage(url, imageBytes);
		ResponseHandler handler = new ResponseHandler();
		return handler.handleRCResponse(responseString);
	}
	
	public boolean sendRCValues(Object[] object) throws CustomException{
		if(null != object && object.length>1){
			String url = (String) object[0];
			byte[] imageBytes = null;
			if(null != object[1]){
				imageBytes = (byte[])object[1];
			}
			String responseString = null;
			if(null != imageBytes)
				responseString = helper.doPostImage(url, imageBytes);
			else responseString = helper.doGet(url);
			ResponseHandler handler = new ResponseHandler();
			return handler.handleRCResponse(responseString);
		}
		return false;
	}
	
	private String getRequestString(HashMap<String, String> requestMap) {
		String request = "";
		
		for(String key:requestMap.keySet()) {
			try {
				request += key + "=" + URLEncoder.encode(requestMap.get(key),"utf-8") + "&";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(request.length() > 0) request = request.substring(0, request.length() - 1);
		
		return request;
	}
}
