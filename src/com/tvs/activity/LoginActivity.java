package com.tvs.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tvs.dto.LoginDTO;
import com.tvs.dto.MasterDto;
import com.tvs.module.Constants;
import com.tvs.module.CustomException;
import com.tvs.module.NetworkHelper;
import com.tvs.module.TVSServiceManager;
import com.tvs.utility.sendOldData;

public class LoginActivity extends Activity {
	private static final int LOGIN = 1;
	//private ProgressDialog myProgressDialog = null;
	private String username = null;
	private String password = null;
	private Timer timers = null;
//	boolean validNetwork = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	
		setContentView(R.layout.login);
		
		final EditText passwordText = (EditText) findViewById(R.id.edtxtPassword_01);
		passwordText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
					if (passwordText.getText().toString().length() > 0) {
						loadServiceData();
						return true;
					} else {
						Toast.makeText(LoginActivity.this, "Please Enter UserName and Password.", Toast.LENGTH_SHORT).show();
					}
				}
				return false;
			}
		});
		
		Button login = (Button) findViewById(R.id.buttonLogin_01);
		login.setOnClickListener(new CustomClickHandler());
	}

	private boolean isValidLoginInput(String userName, String password) {
		if (userName.length() == 0) {
			Toast.makeText(LoginActivity.this, Constants.ENTER_USER_NAME, Toast.LENGTH_SHORT).show();
			return false;
		} else if (password.length() == 0) {
			Toast.makeText(LoginActivity.this, Constants.ENTET_PASSWORD, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public void onStop() {
		super.onStop();
		EditText userName = ((EditText) findViewById(R.id.edtxtUserName_01));
		userName.setText("");
		EditText passWord = ((EditText) findViewById(R.id.edtxtPassword_01));
		passWord.setText("");
	}

	private void loadServiceData() {
	
		username = ((EditText) findViewById(R.id.edtxtUserName_01)).getText().toString();
		password = ((EditText) findViewById(R.id.edtxtPassword_01)).getText().toString();
		boolean isValid = isValidLoginInput(username, password);
		if (isValid) {
			boolean validNetwork = false;
			try {
				validNetwork = NetworkHelper.isOnlines(LoginActivity.this);
			} catch (CustomException e1) { }
			
			if(validNetwork){
				sendOldData.showPrograssBar(LoginActivity.this);
				timers = new Timer();
				timers.schedule(new TimerTask() {
					public void run() {
						Message msg = Message.obtain();
						msg.what = LOGIN;
						
						try {
							TVSServiceManager service = new TVSServiceManager(LoginActivity.this);
							TVSEService.loginDto = service.login(LoginActivity.this,username, password);
							if(null != TVSEService.loginDto){
								sendOldData.sendOldDatas(LoginActivity.this);
							}
							TVSEService.callList = service.callList(TVSEService.loginDto.getEngineerId());		
							TVSEService.masterDto = service.getMasterValue();
							storeUserNameAndPassword(TVSEService.loginDto);
							storeMasterTable(TVSEService.masterDto);
							sendOldData.updateCallList(LoginActivity.this);
					    } catch (CustomException custom) {
							msg.what = Constants.ERROR;
							Constants.errorMsg = custom.getMessage();
						} catch (Exception e) {
							msg.what = Constants.LOCALERROR;
						}
						progressHandler.sendMessage(msg);
					}
				},0);
			} else {
				SharedPreferences prefsPrivate = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
				if(prefsPrivate.getString(Constants.G_USERNAME, "").equals("") && prefsPrivate.getString(Constants.G_PASSWORD, "").equals("") ){
					Toast.makeText(LoginActivity.this, "Atleast login one time with proper network connection", Toast.LENGTH_LONG).show();
					return;
				} else {
					if(!prefsPrivate.getString(Constants.G_USERNAME, "").equals(username) || !prefsPrivate.getString(Constants.G_PASSWORD, "").equals(password)){
						Toast.makeText(LoginActivity.this, "UserName or Password is Wrong", Toast.LENGTH_LONG).show();
						return;
					} 
				}
				TVSEService.loginDto = getLoginInformationFromShared();
				TVSEService.masterDto = getMasterDtoFromShared();
				sendOldData.setCallList(LoginActivity.this);
				Message msg = Message.obtain();
				msg.what = LOGIN;
				progressHandler.sendMessage(msg);
			}
			
		}
	}

	private void storeUserNameAndPassword(LoginDTO login) {
		SharedPreferences prefsPrivate = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
		Editor prefsPrivateEditor = prefsPrivate.edit();
		prefsPrivateEditor.putString(Constants.G_USERNAME, login.getUsername());
		prefsPrivateEditor.putString(Constants.G_PASSWORD, login.getPassword());
		prefsPrivateEditor.putString(Constants.ENG_ID, login.getEngineerId());
		prefsPrivateEditor.putString(Constants.ENG_NAME, login.getEngineerName());
		prefsPrivateEditor.putString(Constants.SERVERDATE, login.getServerDateTime());
		prefsPrivateEditor.commit();
	}
	
	private void storeMasterTable(MasterDto masterdto) {
		SharedPreferences prefsPrivate = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
		Editor prefsPrivateEditor = prefsPrivate.edit();

		String master = null;
		StringBuffer buffer = null;
		
		if(null != masterdto.getParts()){
			buffer = new StringBuffer();
			for(int i = 0;i < masterdto.getParts().length;i++){
				buffer.append(masterdto.getParts()[i]);
				buffer.append("|");
			}
			master = buffer.toString().substring(0, buffer.toString().length() - 1);
			prefsPrivateEditor.putString(Constants.PART_LIST,master);
		}
		
		master = null;
		buffer = null;
		if(null != masterdto.getResults()){
			buffer = new StringBuffer();
			for(int i = 0;i < masterdto.getResults().length;i++){
				buffer.append(masterdto.getResults()[i]);
				buffer.append("|");
			}
			master = buffer.toString().substring(0, buffer.toString().length() - 1);
			prefsPrivateEditor.putString(Constants.CALL_RESULT,master);
		}
		
		master = null;
		buffer = null;
		if(null != masterdto.getReason()){
			buffer = new StringBuffer();
			for(int i = 0;i < masterdto.getReason().length;i++){
				buffer.append(masterdto.getReason()[i]);
				buffer.append("|");
			}
			master = buffer.toString().substring(0, buffer.toString().length() - 1);
			prefsPrivateEditor.putString(Constants.REASON,master);
		}
		
		master = null;
		buffer = null;
		if(null != masterdto.getActivity_done()){
			buffer = new StringBuffer();
			for(int i = 0;i < masterdto.getActivity_done().length;i++){
				buffer.append(masterdto.getActivity_done()[i]);
				buffer.append("|");
			}
			master = buffer.toString().substring(0, buffer.toString().length() - 1);
			prefsPrivateEditor.putString(Constants.ACTIVITY_DONE,master);
		}

		master = null;
		buffer = null;
		if(null != masterdto.getAdditional_Activity()){
			buffer = new StringBuffer();
			for(int i = 0;i < masterdto.getAdditional_Activity().length;i++){
				buffer.append(masterdto.getAdditional_Activity()[i]);
				buffer.append("|");
			}
			master = buffer.toString().substring(0, buffer.toString().length() - 1);
			prefsPrivateEditor.putString(Constants.ADDITIONAL_ACTIVITY,master);
		}
		
		master = null;
		buffer = null;
		if(null != masterdto.getPart_Usage()){
			buffer = new StringBuffer();
			for(int i = 0;i < masterdto.getPart_Usage().length;i++){
				buffer.append(masterdto.getPart_Usage()[i]);
				buffer.append("|");
			}
			master = buffer.toString().substring(0, buffer.toString().length() - 1);
			prefsPrivateEditor.putString(Constants.PART_USAGE,master);
		}
		
		master = null;
		buffer = null;
		if(null != masterdto.getPart_Usage_Id()){
			buffer = new StringBuffer();
			for(int i = 0;i < masterdto.getPart_Usage_Id().length;i++){
				buffer.append(masterdto.getPart_Usage_Id()[i]);
				buffer.append("|");
			}
			master = buffer.toString().substring(0, buffer.toString().length() - 1);
			prefsPrivateEditor.putString(Constants.PART_USAGE_ID,master);
		}
		
		prefsPrivateEditor.commit();
	}

	private MasterDto getMasterDtoFromShared(){
		MasterDto master = new MasterDto();
		SharedPreferences prefsPrivate = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
		String[] master_detail = null;
		if(prefsPrivate.getString(Constants.PART_LIST,"") != ""){		
			master_detail = prefsPrivate.getString(Constants.PART_LIST,"").split("\\|");
			master.setParts(master_detail);
		}
		
		master_detail = prefsPrivate.getString(Constants.CALL_RESULT, "").split("\\|");
		master.setResults(master_detail);
		master_detail = prefsPrivate.getString(Constants.REASON, "").split("\\|");
		master.setReason(master_detail);
		master_detail = prefsPrivate.getString(Constants.ACTIVITY_DONE, "").split("\\|");
		master.setActivity_done(master_detail);	
		master_detail = prefsPrivate.getString(Constants.ADDITIONAL_ACTIVITY, "").split("\\|");
		master.setAdditional_Activity(master_detail);
		master_detail = prefsPrivate.getString(Constants.PART_USAGE, "").split("\\|");
		master.setPart_Usage(master_detail);
		master_detail = prefsPrivate.getString(Constants.PART_USAGE_ID, "").split("\\|");
		master.setPart_Usage_Id(master_detail);
		return master;
		
	}
	
	private LoginDTO getLoginInformationFromShared(){
		
		SharedPreferences prefsPrivate = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
		LoginDTO logindto = new LoginDTO();
		logindto.setUsername(prefsPrivate.getString(Constants.G_USERNAME, ""));
		logindto.setPassword(prefsPrivate.getString(Constants.G_PASSWORD, ""));
		logindto.setEngineerId(prefsPrivate.getString(Constants.ENG_ID, ""));
		logindto.setEngineerName(prefsPrivate.getString(Constants.ENG_NAME, ""));
		logindto.setServerDateTime(prefsPrivate.getString(Constants.SERVERDATE, ""));
		
		return logindto;
	}
	
	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String errorMessage = null;
			switch (msg.what) {
			case LOGIN:
				Intent intent = new Intent(LoginActivity.this, CallListActivity.class);
				startActivity(intent);
				break;
			case Constants.LOCALERROR:
				errorMessage = getString(R.string.login_error);
				Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
				break;
			case Constants.ERROR:
				Toast.makeText(LoginActivity.this, Constants.errorMsg, Toast.LENGTH_LONG).show();
				break;
			}
			if(null != timers){
				timers.cancel();
				timers = null;
			}
			sendOldData.dismissPrograssBar();
		}
	};

	private class CustomClickHandler implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.buttonLogin_01:
				loadServiceData();
				break;
			}
		}

	}
}