package com.tvs.utility;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;

import com.tvs.activity.TVSEService;
import com.tvs.dao.DBHelperDAO;
import com.tvs.module.Constants;
import com.tvs.module.CustomException;
import com.tvs.module.TVSServiceManager;

public class sendOldData {

	private static ProgressDialog myProgressDialog = null;
	
	public static void sendOldDatas(Context context){
		DBHelperDAO helper = new DBHelperDAO(context);
		helper.openDB();
		TVSServiceManager service = new TVSServiceManager(context);
		try {
			ArrayList<Object[]> temp = helper.getCallListDatas();
			if(null != temp){
				int count = temp.size();
				Object[] object = null;
				boolean isSent = false;
				for(int i=0;i<count;i++){
					isSent = false;
					object = (Object[])temp.get(i);
					try{
						isSent = service.sendRCValues(object);
					}catch (CustomException e) {
						if(e.getCode() == Constants.ERR_SEND)
							isSent = true;
					}
					if(isSent)
						helper.deleteCallListDatasUrl(object);
				}
			}
		} catch (Exception e) {}
		if(null != helper){
			helper.closeDB();
			helper = null;
		}
	}
	
	public static void updateUrl(Context context, String url, byte[] image_byte, String serialNumber, String updateflag){
		DBHelperDAO helper = new DBHelperDAO(context);
		helper.openDB();
		try{
			helper.updateCallListData(url, image_byte);
			helper.updateCallListData(serialNumber, updateflag);
		}catch (Exception e) {
			
		}
		helper.closeDB();
		helper = null;
	}
	
	public static void showPrograssBar(Context context){
		if(null != myProgressDialog){
			myProgressDialog.cancel();
			myProgressDialog = null;
		}
		myProgressDialog = new ProgressDialog(context);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("Loading...");
		myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		myProgressDialog.show();
	}
	
	public static void dismissPrograssBar(){
		if(null != myProgressDialog){
			myProgressDialog.cancel();
			myProgressDialog = null;
		}
	}
	
	public static void setCallList(Context context){
		DBHelperDAO helper = new DBHelperDAO(context);
		helper.openDB();
		TVSEService.callList = helper.getCallList();
		helper.closeDB();
		helper = null;
	}
	
	public static void updateCallList(Context context){
		
		DBHelperDAO helper = new DBHelperDAO(context);
		helper.openDB();
		try{
			helper.updateNewCallList(TVSEService.callList);
		}catch (Exception e) {
			
		}
		helper.closeDB();
		helper = null;
	}
}
