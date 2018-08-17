package com.tvs.activity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.tvs.dto.CallListDTO;
import com.tvs.dto.RC53RequestDto;
import com.tvs.module.Constants;
import com.tvs.module.CustomException;
import com.tvs.module.NetworkHelper;
import com.tvs.module.TVSServiceManager;
import com.tvs.sign.MyPainter;
import com.tvs.utility.sendOldData;

public class CallCompletedActivity extends Activity {
	/** Called when the activity is first created. */
	private Button btnActivityDone;
	private TextView mTimeDisplay;
	private TextView mDateDisplay;
//	private EditText mEditText;
	private boolean customer_sign = false;
	static final int DATE_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID = 0;
	static final int TO_DATE_DIALOG_ID = 1;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private CallListDTO callListDto = null;
	private static final int SUCCESS = 1;
	private Timer timers = null;
	private boolean isSuccess = false;
	private int cYear = 0;
	private int cMonth = 0;
	private int cDate = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.call_comp);
		
		Display newDisplay = getWindowManager().getDefaultDisplay(); 
		int width = newDisplay.getWidth();
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.cold_Layout);
		layout.setLayoutParams(new LinearLayout.LayoutParams(width/2, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		layout = (LinearLayout)findViewById(R.id.Poh_Layout);
		layout.setLayoutParams(new LinearLayout.LayoutParams(width/2, LinearLayout.LayoutParams.WRAP_CONTENT));

		// Display the current date
		callListDto = TVSEService.callList[TVSEService.ParentIndex]
				.getCallListDto()[TVSEService.childIndex];

		String serverDate = TVSEService.loginDto.getServerDateTime();
		int index = serverDate.indexOf("/");
		cDate = Integer.parseInt(serverDate.substring(0, index));
		serverDate = serverDate.substring(index + 1);
		cMonth = serverDate.indexOf("/");
		cMonth = Integer.parseInt(serverDate.substring(0, index));
		serverDate = serverDate.substring(index + 1);
		index = serverDate.indexOf(" ");
		if (index > -1) {
			cYear = Integer.parseInt(serverDate.substring(0, index));
		} else
			cYear = Integer.parseInt(serverDate);

		Calendar calender = Calendar.getInstance();
		mHour = calender.get(Calendar.HOUR_OF_DAY);
		mMinute = calender.get(Calendar.MINUTE);

		mDateDisplay = (TextView) this.findViewById(R.id.txtDate_05);
		mDateDisplay.setOnClickListener(new CustomClickHandler());
		mDateDisplay.setText(cDate + "/" + cMonth + "/" + cYear);

		
		mTimeDisplay = (TextView) this.findViewById(R.id.txtTime_05);
		mTimeDisplay.setOnClickListener(new CustomClickHandler());

		Button button = (Button) findViewById(R.id.btnPartsTransaction_05);
		button.setOnClickListener(new CustomClickHandler());

		button = (Button)findViewById(R.id.btnAdditionalActtivityDone_05);
		button.setOnClickListener(new CustomClickHandler());
		
		button = (Button) findViewById(R.id.buttonSign_05);
		button.setOnClickListener(new CustomClickHandler());

		TextView txtSERIAL = (TextView) this.findViewById(R.id.txtSERNUMBER_05);
		txtSERIAL.setText("SER NO:"+callListDto.getSerial_Number());
		TVSEService.filesign = callListDto.getSerial_Number().toString();

		TextView txtEngName = (TextView) this.findViewById(R.id.edtxtEngineerName_05);
		txtEngName.setText(callListDto.getEngineer_Name());

		TextView txtTechId = (TextView) this.findViewById(R.id.edtxtTechID_05);
		txtTechId.setText(callListDto.getTechnician_Id());
		
		EditText editText = (EditText)findViewById(R.id.edtCustomer_name_05);
		editText.setText(callListDto.getCustomer_Name());
		
		CheckBox checkbox = (CheckBox)findViewById(R.id.chkAcceptance_05);
		checkbox.setOnCheckedChangeListener(new CustomClickHandler());

		final Spinner results = (Spinner) findViewById(R.id.spinResult_05);
		ArrayAdapter<String> resultsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, TVSEService.masterDto.getResults());
		resultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		results.setAdapter(resultsAdapter);
		results.setOnTouchListener(new CustomClickHandler());
		results.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
						results.setFocusable(true);
						results.setFocusableInTouchMode(true);
						results.requestFocus();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

		Button submitButton = (Button) findViewById(R.id.buttonSubmit_05);
		submitButton.setOnClickListener(new CustomClickHandler());

		/*
		 * mYear = cYear; mMonth = cMonth; mDay = cDate;
		 */

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		updateDisplayTime();

		btnActivityDone = (Button) findViewById(R.id.btnActDone_05);
		btnActivityDone.setPadding(10, 0, 10, 0);
		btnActivityDone.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		btnActivityDone.setText("Activity Done");
		btnActivityDone.setOnClickListener(new CustomClickHandler());
		
		if(null != TVSEService.oldFileSign && TVSEService.oldFileSign == TVSEService.filesign) {
			customer_sign = true;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public class CustomClickHandler implements OnClickListener, OnTouchListener, OnCheckedChangeListener {
		Intent intent = null;

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.btnActDone_05:
				intent = new Intent(CallCompletedActivity.this,ActivityDoneActivity.class);
				Button button = (Button) findViewById(R.id.btnActDone_05);
				intent.putExtra("OldData", button.getText());
				startActivityForResult(intent, 4);
				break;
			case R.id.buttonSubmit_05:
				if (customer_sign)
					loadServiceData();
				else {
					Toast.makeText(CallCompletedActivity.this,"Customer not signed.", Toast.LENGTH_LONG).show();
					return;
				}
				break;
			case R.id.buttonSign_05:
				intent = new Intent(CallCompletedActivity.this, MyPainter.class);
				startActivityForResult(intent, 3);
				break;
			case R.id.btnPartsTransaction_05:
				intent = new Intent(CallCompletedActivity.this, PartsTransactionActivity.class);
				startActivityForResult(intent, 1);
				break;
			case R.id.btnAdditionalActtivityDone_05:
				intent = new Intent(CallCompletedActivity.this, AdditionalActivity.class);
				startActivityForResult(intent, 2);
				break;
			}
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Spinner spinner = null;
			switch (v.getId()) {
			case R.id.spinResult_05:
				spinner = (Spinner) findViewById(R.id.spinResult_05);
				spinner.showContextMenu();
				break;
			default:
				break;
			}
			return false;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout_Acceptance_05);
			if(isChecked){
				layout.setVisibility(LinearLayout.VISIBLE);
			} else layout.setVisibility(LinearLayout.GONE);
		}
	}

	private void updateDisplayDate() {
		boolean isSet = false;
		// mMonth +=1;
		if (cYear == mYear && cMonth == mMonth && cDate <= mDay) {
			isSet = true;
		} else if (cYear == mYear && cMonth < mMonth) {
			isSet = true;
		} else if (cYear < mYear) {
			isSet = true;
		}
		if (isSet) {
			mDateDisplay.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mDay).append("/").append(mMonth).append("/").append(mYear));
		} else
			Toast.makeText(CallCompletedActivity.this,"Please select valid Date", Toast.LENGTH_LONG).show();

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			updateDisplayDate();
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplayTime();
		}
	};

	// updates the time we display in the TextView
	private void updateDisplayTime() {
		mTimeDisplay.setText(new StringBuilder().append(pad(mHour)).append(":").append(pad(mMinute)));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(CallCompletedActivity.this, mDateSetListener, mYear, mMonth, mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
		}

		return null;
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	protected void showSelectActDoneDialog() {

	}

	private RC53RequestDto getRequestDto() {
		RC53RequestDto rC53RequestDto = new RC53RequestDto();
		
		//Result
		Spinner spinner = (Spinner) findViewById(R.id.spinResult_05);
		String tempValue = spinner.getSelectedItem().toString();
		int index = tempValue.indexOf("-");
		tempValue = tempValue.substring(0, index).trim();
		rC53RequestDto.setResult(tempValue);
		
		//Comp.DT/Time
		tempValue = "";
		TextView textView = (TextView) findViewById(R.id.txtDate_05);
		tempValue = textView.getText().toString().trim();

		textView = (TextView) findViewById(R.id.txtTime_05);
		tempValue += " " + textView.getText().toString().trim();

		rC53RequestDto.setComplete_Date(tempValue);
		
		tempValue = "";
		String partTrans = "";
		int count = 0;
		//Part Transaction
		if(callListDto.isNoPart()){
			String[] selectedPartUsage_Id = TVSEService.masterDto.getSelectedPartUsage_ID();
			if(null != selectedPartUsage_Id && (count=selectedPartUsage_Id.length)>0){
				String[] ppid = TVSEService.masterDto.getPPID();
				String[] partNumber = callListDto.getPartNumber();
				String[] uniqueID = callListDto.getUniqueId();
				String[] seltedPartTransaction = TVSEService.masterDto.getSelectedPartUsage();
				for(int i=0;i<count;i++){
					if(null != ppid[i]){
						tempValue += "," +uniqueID[i]+":"+partNumber[i]+":"+selectedPartUsage_Id[i]+":"+ppid[i];
					} else tempValue += "," + uniqueID[i]+":"+partNumber[i]+":"+selectedPartUsage_Id[i];
					partTrans += seltedPartTransaction[i]+",";
				}
				tempValue = tempValue.substring(1);
			} else {
				Toast.makeText(CallCompletedActivity.this, "Please complete Part Transaction", Toast.LENGTH_LONG).show();
				return null;
			}
		}
		rC53RequestDto.setPart_transaction(tempValue);

		boolean isCheckedDone = false;
		//Cold Boot Done
		RadioButton radiobutton = (RadioButton) findViewById(R.id.radiocooldDone);
		if(radiobutton.isChecked()){
			isCheckedDone = true;
			rC53RequestDto.setCold_boot("Yes");
		} else rC53RequestDto.setCold_boot("No");
		
		radiobutton = (RadioButton) findViewById(R.id.radiocooldNo);
		if(!radiobutton.isChecked() && !isCheckedDone){
			Toast.makeText(CallCompletedActivity.this, "Please select Cold Boot YES/No", Toast.LENGTH_LONG).show();
			return null;
		}
		
		//POH
		isCheckedDone = false;
		radiobutton = (RadioButton) findViewById(R.id.radioPOHDone);
		if (radiobutton.isChecked()) {
			isCheckedDone = true;
			rC53RequestDto.setPoh("Yes");
		} else rC53RequestDto.setPoh("No");
		
		
		if(callListDto.isNoPart()){
			radiobutton = (RadioButton) findViewById(R.id.radioPOHNo);
			if(!radiobutton.isChecked() && !isCheckedDone){
				Toast.makeText(CallCompletedActivity.this, "Please select POH YES/No", Toast.LENGTH_LONG).show();
				return null;
			}
		}
		
		//DTS Name
		EditText editText = (EditText) findViewById(R.id.edtxtDtsName_05);
		rC53RequestDto.setDts_name(editText.getText().toString().trim());
		
		//Case Id
		editText = (EditText) findViewById(R.id.edTxtCaseId_05);
		tempValue = editText.getText().toString().trim();
		if(callListDto.isNoPart()){
			if(partTrans.indexOf("H-Returned Document - POH")>-1 && tempValue.length() == 0)
			{
				Toast.makeText(CallCompletedActivity.this, "Please fill the case Id", Toast.LENGTH_LONG).show();
				return null;
			}
		}
		rC53RequestDto.setCaseid(tempValue);
		
		isCheckedDone = false;
		//Call Wrap-up / System Check
		radiobutton = (RadioButton) findViewById(R.id.radioEsurvey_05Done);
		if (radiobutton.isChecked()) {
			isCheckedDone = true;
			rC53RequestDto.setEsurvey_Education("Yes");
		} else rC53RequestDto.setEsurvey_Education("No");
		
		radiobutton = (RadioButton) findViewById(R.id.radiocEsurvey_05No);
		if(!radiobutton.isChecked() && !isCheckedDone){
			Toast.makeText(CallCompletedActivity.this, "Please select call wrap-up Yes/No", Toast.LENGTH_LONG).show();
			return null;
		}
		
		isCheckedDone = false;
		//Customer Satisfied
		radiobutton = (RadioButton) findViewById(R.id.radiocustomer_satisfied_05Done);
		if (radiobutton.isChecked()) {
			isCheckedDone = true;
			rC53RequestDto.setCustomer_satisfied("Yes");
		} else rC53RequestDto.setCustomer_satisfied("No");

		
		radiobutton = (RadioButton) findViewById(R.id.radioccustomer_satisfied_05No);
		if(!radiobutton.isChecked() && !isCheckedDone){
			Toast.makeText(CallCompletedActivity.this, "Please select customer satisfied Yes/No", Toast.LENGTH_LONG).show();
			return null;
		}
		
		//Activity Done
		Button button = (Button) findViewById(R.id.btnActDone_05);
		String actStr = (String)button.getText();
		String actDone = null;
		if(actStr.toLowerCase().compareTo("activity done")== 0 || actStr.toLowerCase().compareTo("none") == 0){
			Toast.makeText(CallCompletedActivity.this, "Please complete Activity Done", Toast.LENGTH_LONG).show();
			return null;
		} else {
			actDone = "";
			actStr = actStr.replaceAll("/-",",");
			actStr = actStr.substring(0,actStr.length()-1);
			int len = actStr.length();
			String comma = actStr.substring(len - 1, len);
			if (comma.equals(","))
				actStr = actStr.substring(0, len - 1);
			actDone = actStr + ",";
		}
		
		rC53RequestDto.setActivity_Done(actDone);
		
		//Additional Activity
		tempValue ="";
		boolean[] enabledActivity = TVSEService.masterDto.getAdditional_Activity_Enabled();
		if(null != enabledActivity && (count= enabledActivity.length)>0){
			String[] additionalActivity = TVSEService.masterDto.getAdditional_Activity();
			for(int i=0;i<count;i++){
				if(enabledActivity[i])
					tempValue += "," + additionalActivity[i];
			}
			if(tempValue.length()>0)
				tempValue = tempValue.substring(1);
		}
		rC53RequestDto.setAdditional_activity(tempValue);
		
		//Provide Additional inputs, if any
		editText = (EditText) findViewById(R.id.edTxtActivityDone_05);
		tempValue = editText.getText().toString().trim();
		tempValue = tempValue.replace("\n", "%20");
		rC53RequestDto.setAdditional_information(tempValue);
		
		tempValue = "";
		//Customer Name
		editText = (EditText)findViewById(R.id.edtCustomer_name_05);
		tempValue = editText.getText().toString();
		rC53RequestDto.setCustomer_name(tempValue);
		
		//Accept
		CheckBox checkBox = (CheckBox)findViewById(R.id.chkAcceptance_05);
		if(checkBox.isChecked()){
			rC53RequestDto.setACCEPT("YES");
			
			//Customer Name Validation
			if(tempValue.length() ==0){
				Toast.makeText(CallCompletedActivity.this, "Customer name should not empty", Toast.LENGTH_LONG).show();
				return null;
			}
			
			tempValue = "";
			//Customer Mail Id
			editText = (EditText)findViewById(R.id.edtCustomer_mailId_05);
			tempValue = editText.getText().toString();
			final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
			          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
			          "\\@" +
			          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
			          "(" +
			          "\\." +
			          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
			          ")+"
			      );

			
			if(tempValue.length()>0){
				try {
					if(!EMAIL_ADDRESS_PATTERN.matcher(tempValue).matches())
			        	tempValue ="";
			    }
			    catch(Exception exception ) {
			    	tempValue="";
			    }
			} 
			
			if(tempValue.length() ==0){
				Toast.makeText(CallCompletedActivity.this, "Please enter valid email id", Toast.LENGTH_LONG).show();
				return null;
			}
			rC53RequestDto.setCustomer_Mail_Id(tempValue);
			
			tempValue = "";
			//Customer Job Title
			editText = (EditText)findViewById(R.id.edtCustomer_jobTitle_05);
			tempValue = editText.getText().toString();
			if(tempValue.length() ==0){
				Toast.makeText(CallCompletedActivity.this, "Customer job titile should not empty", Toast.LENGTH_LONG).show();
				return null;
			}
			rC53RequestDto.setCustomer_job_title(tempValue);
		}
		
		//Customer Comments
		tempValue ="";
		editText = (EditText)findViewById(R.id.edtCustomer_commants_05);
		tempValue = editText.getText().toString();
		tempValue = tempValue.replace("\n", "%20");
		rC53RequestDto.setCustomerCommants(tempValue);
		
		//Serial Number
		rC53RequestDto.setSerailNumber(callListDto.getSerial_Number());
		
		if(null != TVSEService.filesign){
			rC53RequestDto.setSignature_filename(TVSEService.filesign+".png");
		}
		return rC53RequestDto;
	}

	/**
	 * 
	 */
	private void loadServiceData() {
		
		final RC53RequestDto rc53RequestDto = getRequestDto();
		if (null == rc53RequestDto)
			return;
		
		boolean validNetwork = false;
		try {
			validNetwork = NetworkHelper.isOnlines(CallCompletedActivity.this);
		} catch (CustomException e1) {
			validNetwork = false;
		}
		
		TVSServiceManager loginDetail = new TVSServiceManager(CallCompletedActivity.this);
		String urlString = loginDetail.getRc53UrlString(rc53RequestDto);
		byte[] imageByte = loginDetail.getImageByte(getResources().getString(R.string.directory));
		
		if(validNetwork){
			sendResult(urlString, imageByte);
		} else {
			sendOldData.updateUrl(CallCompletedActivity.this, urlString, imageByte, callListDto.getSerial_Number(), Constants.CALLCOMPLETE);
			sendOldData.setCallList(CallCompletedActivity.this);
			isSuccess = true;
			Message msg = Message.obtain();
			msg.what = SUCCESS;
			progressHandler.sendMessage(msg);
			
		}
	}
	
	private void sendResult(final String urlString,final byte[] imageByte){
		sendOldData.showPrograssBar(CallCompletedActivity.this);
		timers = new Timer();
		timers.schedule(new TimerTask() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = SUCCESS;
				sendOldData.sendOldDatas(CallCompletedActivity.this);
				try {
					TVSServiceManager loginDetail = new TVSServiceManager(CallCompletedActivity.this);
					isSuccess = loginDetail.getRC53Value(imageByte,urlString);
					if (isSuccess){
						TVSEService.callList = loginDetail.callList(TVSEService.loginDto.getEngineerId());
						sendOldData.updateCallList(CallCompletedActivity.this);
					}
				} catch (CustomException custom) {
					msg.what = Constants.ERROR;
					Constants.errorMsg = custom.getMessage();
				} catch (Exception e) {
					msg.what = Constants.LOCALERROR;
				}
				progressHandler.sendMessage(msg);
			}
		}, 0);
	}

	/**
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Button button = null;
		if (requestCode == 3 && resultCode == RESULT_OK) {
			customer_sign = data.getExtras().getBoolean("CUST_SIGNED");
			EditText edtxtActDone = (EditText) findViewById(R.id.edTxtActivityDone_05);
			edtxtActDone.setFocusable(true);
			edtxtActDone.setFocusableInTouchMode(true);
			edtxtActDone.requestFocus();
		} else if (requestCode == 4) {
			if (resultCode == RESULT_OK) {
				button = (Button) findViewById(R.id.btnActDone_05);
				Bundle bundle = data.getExtras();
				button.setText(bundle.getString(Constants.ACTIVITY_DONE));
			}
			button = (Button) findViewById(R.id.buttonSign_05);
			button.setFocusable(true);
			button.setFocusableInTouchMode(true);
			button.requestFocus();
		} else if(requestCode == 1){
			button = (Button) findViewById(R.id.btnPartsTransaction_05);
			button.setFocusable(true);
			button.setFocusableInTouchMode(true);
			button.requestFocus();
			
			String[] selectedPartUsage = TVSEService.masterDto.getSelectedPartUsage();
			int count = 0;
			boolean isEnable = true;
			if(null != selectedPartUsage && (count= selectedPartUsage.length)>0){
				for(int i=0;i<count;i++){
					if(selectedPartUsage[i].compareTo("H-Returned Document - POH") == 0){
						isEnable = false;
						break;
					}
				}
			} 
			
			RadioButton radioButton = (RadioButton)findViewById(R.id.radioPOHNo);
			radioButton.setChecked(isEnable);
			radioButton.setEnabled(isEnable);
			
			radioButton = (RadioButton)findViewById(R.id.radioPOHDone);
			radioButton.setChecked(!isEnable);
			radioButton.setEnabled(!isEnable);
		} else if(requestCode == 2){
			button = (Button) findViewById(R.id.btnAdditionalActtivityDone_05);
			button.setFocusable(true);
			button.setFocusableInTouchMode(true);
			button.requestFocus();
		}
	}

	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				if (isSuccess) {
					Intent intent = new Intent();
					setResult(2, intent);
					finish();
					String errorMessage = "Submitted in offline Mode – System will get updated once connectivity restored";
					if(null != Constants.statusString)
						errorMessage = Constants.statusString;
					Toast.makeText(CallCompletedActivity.this, errorMessage, Toast.LENGTH_LONG).show();
				}
				break;
			case Constants.LOCALERROR:
				if (Constants.errorMsg.length() > 0) {
					Toast.makeText(CallCompletedActivity.this, Constants.errorMsg, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(CallCompletedActivity.this, "Not Submitted", Toast.LENGTH_LONG).show();
				break;
			case Constants.ERROR:
				Toast.makeText(CallCompletedActivity.this, Constants.errorMsg, Toast.LENGTH_LONG).show();
				break;
			}
			if(null != timers){
				timers.cancel();
				timers = null;
			}
			sendOldData.dismissPrograssBar();
		}
	};
}