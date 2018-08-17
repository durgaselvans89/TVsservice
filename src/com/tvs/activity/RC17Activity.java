package com.tvs.activity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tvs.module.Constants;
import com.tvs.module.CustomException;
import com.tvs.module.NetworkHelper;
import com.tvs.module.TVSServiceManager;
import com.tvs.utility.sendOldData;

public class RC17Activity extends Activity {
	
	private TextView mTimeDisplay;
	private TextView mDateDisplay;
	private static final int SUCCESS = 1;
    private int mHour;
    private int mMinute;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID = 0;
    
	//private ProgressDialog myProgressDialog = null;
//	boolean isOnline = false;
	private boolean isSuccess = false;
	private String serialNumber =null;
	private String remarks = null;
	Timer timer = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.rcseventeen);
        
        mTimeDisplay = (TextView) findViewById(R.id.txtTime_08);
        mTimeDisplay.setOnClickListener(new CustomClickHandler());
        
        mDateDisplay = (TextView) findViewById(R.id.txtDate_08);
        mDateDisplay.setOnClickListener(new CustomClickHandler());
        
        serialNumber = TVSEService.callList[TVSEService.ParentIndex].getCallListDto()[TVSEService.childIndex].getSerial_Number();
        
       Button submit = (Button) findViewById(R.id.buttonSubmit_08);
       submit.setOnClickListener(new CustomClickHandler());
       
       TextView textView = (TextView)findViewById(R.id.txtSERNUMBER_08);
       textView.setText(serialNumber);
        
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH); 
        updateDisplayTime();
        updateDisplayDate();
    }
    
    private void updateDisplayTime() {
        mTimeDisplay.setText(
            new StringBuilder()
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute)));
    }
    
    private void updateDisplayDate() {
        mDateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mDay).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mYear).append(" "));
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                updateDisplayTime();
            }
        };
        
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, 
                                  int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                updateDisplayDate();
            }
        };     
        @Override
        protected Dialog onCreateDialog(int id) {
            switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, mHour, mMinute, false);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);
    
            }
            return null;
        }
    
    private class CustomClickHandler implements OnClickListener {
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.txtTime_08:
					// showDialog(TIME_DIALOG_ID);
					 break;
				case R.id.txtDate_08:
					 //showDialog(DATE_DIALOG_ID);
					 break;
				case R.id.buttonSubmit_08:
					loadServiceData();
					break;
			}
		}

    }
    
    private void loadServiceData(){
    	boolean isValid = true;
    	remarks = ((EditText)findViewById(R.id.edtxtReason_08)).getText().toString();
		if(isValid) {
			boolean isOnline = false;
			try {				
				isOnline = NetworkHelper.isOnlines(RC17Activity.this);			
			} catch (CustomException e1) { }
			TVSServiceManager loginDetail = new TVSServiceManager(RC17Activity.this);
			String urlString = loginDetail.getRC17UrlString(serialNumber, remarks);
			loginDetail = null;
			if(isOnline){
				sendResult(urlString);
			} else {
				TVSEService.callList[TVSEService.ParentIndex].getCallListDto()[TVSEService.childIndex].setFlag_rc17("yes");
				sendOldData.updateUrl(RC17Activity.this, urlString, null,serialNumber,Constants.RC17);
				isSuccess = true;
				Message msg = Message.obtain();
				msg.what = SUCCESS;
				progressHandler.sendMessage(msg);
			}
		}
    }
    
    private void sendResult(final String urlString){
    	sendOldData.showPrograssBar(RC17Activity.this);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = SUCCESS;
				sendOldData.sendOldDatas(RC17Activity.this);
				try {
					TVSServiceManager service = new TVSServiceManager(RC17Activity.this);
					isSuccess = service.getRC17Value(urlString);
					if(isSuccess){
						TVSEService.callList = service.callList(TVSEService.loginDto.getEngineerId());
						sendOldData.updateCallList(RC17Activity.this);
					}
				} catch (CustomException custom) {
					msg.what=Constants.ERROR;
					Constants.errorMsg = custom.getMessage();
				} catch (Exception e) {
					msg.what=Constants.LOCALERROR;
				}
				progressHandler.sendMessage(msg);
			}
		}, 0);
    }
    
    Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SUCCESS:
					if(isSuccess){
						String errorMessage ="Submitted in offline Mode – System will get updated once connectivity restored";
						if(null != Constants.statusString)
							errorMessage = Constants.statusString;
						//String errorMessage = "RC 17 Updated successfully,";
//						int count = TVSEService.callList[TVSEService.ParentIndex].getCallListDto()[TVSEService.childIndex].getPartCount();
//						boolean isPartNotIssued = TVSEService.callList[TVSEService.ParentIndex].getCallListDto()[TVSEService.childIndex].isPartIssued();
//						int usageCount = TVSEService.masterDto.getPart_Usage().length;
//						if(count != usageCount || isPartNotIssued){
//							errorMessage += " Part(s) not issued in system hence completion not allowed , pls contact SLC.";
//						}
						Intent intent = new Intent();
						setResult(4,intent);
						finish();
						Toast.makeText(RC17Activity.this, errorMessage, Toast.LENGTH_LONG).show();
					} else Toast.makeText(RC17Activity.this, "Not Submitted", Toast.LENGTH_LONG).show();
					break;
				case Constants.LOCALERROR:
					Toast.makeText(RC17Activity.this, "Not Submitted", Toast.LENGTH_LONG).show();
					break;
				case Constants.ERROR:
					Toast.makeText(RC17Activity.this, Constants.errorMsg, Toast.LENGTH_LONG).show();
					break;
			}
			if(null != timer){
				timer.cancel();
				timer = null;
			}
			sendOldData.dismissPrograssBar();
		}
	};
}