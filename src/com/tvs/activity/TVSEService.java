package com.tvs.activity;

import android.app.Application;
import com.tvs.dao.DBHelperDAO;
import com.tvs.dto.CallList;
import com.tvs.dto.LoginDTO;
import com.tvs.dto.MasterDto;



public class TVSEService extends Application {
	public static final String APP_NAME = "TVSEService";
	
	
	public static LoginDTO loginDto = null;
	public static MasterDto masterDto = null;
	public static CallList[] callList = null;
	public static int ParentIndex = -1;
	public static int childIndex = -1;
	public static String barcode = "";
	public static boolean Sign_Saved = false; 
	public static boolean Parts_Entered = false; 
	public static String actValue = null;
	public static String filesign = null;
	public static String oldFileSign = null;
	
	private DBHelperDAO dbhelpher;
	

	public DBHelperDAO getDbhelpher() {
		return dbhelpher;
	}

	public void setDbhelpher(DBHelperDAO dbhelpher) {
		this.dbhelpher = dbhelpher;
	}
	
	public TVSEService() {
		super();
	}
	
	@Override
	public void onCreate() {
		super.onCreate(); 
		 this.dbhelpher = new DBHelperDAO(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
