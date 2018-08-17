package com.tvs.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ExpandableListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tvs.dto.CallList;
import com.tvs.dto.CallListDTO;
import com.tvs.module.Constants;
import com.tvs.module.CustomException;
import com.tvs.module.NetworkHelper;
import com.tvs.module.TVSServiceManager;
import com.tvs.utility.sendOldData;

public class CallListActivity extends ExpandableListActivity {
    /** Called when the activity is first created. */
	
	MyExpandableListAdapter myAdapter = null;
	private static final int LOGIN = 1;
	private Timer timers = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.call_list);
		loadScreen();
    }
    
	private void loadScreen(){
		ExpandableListView expenderListView = (ExpandableListView)findViewById(android.R.id.list);
		Display newDisplay = getWindowManager().getDefaultDisplay(); 
		int width = newDisplay.getWidth();
		expenderListView.setIndicatorBounds(width-25, width);
		if(null != TVSEService.callList){
			myAdapter = new MyExpandableListAdapter(TVSEService.callList, this.getApplicationContext());
		} else myAdapter = null;
		expenderListView.setAdapter(myAdapter);
	}

    public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    	CallList[] callList = null;
    	CallListDTO[] callListDto = null;
    	Context context = null;
    	
    	public MyExpandableListAdapter(CallList[] callList, Context context){
    		this.callList = callList;
    		this.context = context;
    	}
    	
		@Override
		public Object getChild(int arg0, int arg1) {
			callListDto = callList[arg0].getCallListDto();
			return callListDto[arg1];
			
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			callListDto = callList[groupPosition].getCallListDto();
			if(null == convertView){
				LayoutInflater inflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
				convertView = (LinearLayout)inflater.inflate(R.layout.expandablelistchild_row, null); 
				//convertView.setOnClickListener(new CustomClickHandler(callListDto[childPosition]));
			}
			convertView.setOnClickListener(new CustomClickHandler(callListDto[childPosition],groupPosition,childPosition));
			
			TextView textview = (TextView)convertView.findViewById(R.id.txtSER_02);
			textview.setText(callListDto[childPosition].getSerial_Number());
			
			TextView textview1 = (TextView)convertView.findViewById(R.id.txtOrgName_02);
			textview1.setText(callListDto[childPosition].getOrgination_Name());
			
			TextView textview2 = (TextView)convertView.findViewById(R.id.txtContactName_02);
			textview2.setText(callListDto[childPosition].getCustomer_Name());
			
			TextView textview3 = (TextView)convertView.findViewById(R.id.txtAppDateTime_02);
			textview3.setText(callListDto[childPosition].getCustomer_Date_Time());
			
			
			
			TextView textview4 = (TextView)convertView.findViewById(R.id.txtPhoneNo_02);
			textview4.setText(callListDto[childPosition].getConstact_Number());
			textview4.setOnClickListener(new CustomClickHandler(callListDto[childPosition],groupPosition,childPosition));
			
			ImageView image =(ImageView)convertView.findViewById(R.id.imgViewCall_02);
			String strphno = callListDto[childPosition].getConstact_Number();
			if(null != strphno){
				image.setVisibility(ImageView.VISIBLE);
				image.setOnClickListener(new CustomClickHandler(callListDto[childPosition],groupPosition,childPosition));
			}else{
				image.setVisibility(ImageView.GONE);
			}
			
			convertView.setTag(callListDto);

			return convertView;
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			callListDto = callList[groupPosition].getCallListDto();
			return callListDto.length;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return callList[groupPosition];
		}
		
		 

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return callList.length;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if(null == convertView){
				LayoutInflater inflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
				convertView = (LinearLayout)inflater.inflate(R.layout.expandgrouplead, null); 
			}
			TextView textview =(TextView)convertView.findViewById(R.id.txtdate_02);
			textview.setText(callList[groupPosition].getAppointment_Date());
			TextView textview1 = (TextView)convertView.findViewById(R.id.txtNOCHeader_02);
			textview1.setText("NOC - "+callList[groupPosition].getNumberOf_Calls());
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
    }
    
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        View emptyView = findViewById(android.R.id.empty);
    	ExpandableListView expenderListView = (ExpandableListView)findViewById(android.R.id.list);
      
        if (emptyView != null) {
        	expenderListView.setEmptyView(emptyView);
        }
    
    }
    
    
    class CustomClickHandler implements OnClickListener{
    	
    	CallListDTO callListDto = null;
    	int parentIndex = -1;
    	int childIndex = -1;
    	
    	public CustomClickHandler(CallListDTO callListDto, int parent, int child){
    		this.callListDto = callListDto;
    		parentIndex = parent;
    		childIndex = child;
    	}
    	
    	public void onClick(View v) {
    		TVSEService.ParentIndex = parentIndex;
    		TVSEService.childIndex = childIndex;
    		
    		switch (v.getId()) {
    			case R.id.imgViewCall_02:
    				
    				break;
				case R.id.txtPhoneNo_02:
					call(callListDto.getConstact_Number());
					break;	
				default:
					Intent intent = new Intent(CallListActivity.this,CallDetailsActivity.class);
					startActivityForResult(intent, 0);
					break;
			}
    	}
    }
    
    private void call(String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNumber));
            startActivityForResult(callIntent,1);
        } catch (ActivityNotFoundException e) {
            Log.d("helloandroid dialing example", "Call failed", e);
        }
    }
    
    /**
	 * Method to handle the when there is a activity result
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 0){
			boolean validNetwork = false;
			try {
				validNetwork = NetworkHelper.isOnlines(CallListActivity.this);
			} catch (CustomException e1) { }
			if(validNetwork){
				loadServiceData();
			} else {
				loadScreen();
			}
		}
	}
	
	private void loadServiceData() {
		sendOldData.showPrograssBar(CallListActivity.this);

		timers = new Timer();
		timers.schedule(new TimerTask() {
			public void run() {
				Message msg = Message.obtain();
				msg.what = LOGIN;
				try{
					sendOldData.sendOldDatas(CallListActivity.this);
					TVSServiceManager loginDetail = new TVSServiceManager(CallListActivity.this);
					TVSEService.callList = loginDetail.callList(TVSEService.loginDto.getEngineerId());
					sendOldData.updateCallList(CallListActivity.this);
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

	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String errorMessage = null;
			loadScreen();
			switch (msg.what) {
				case Constants.LOCALERROR:
					errorMessage = getString(R.string.login_error);
					Toast.makeText(CallListActivity.this, errorMessage, Toast.LENGTH_LONG).show();
					break;
				case Constants.ERROR:
					Toast.makeText(CallListActivity.this, Constants.errorMsg, Toast.LENGTH_LONG).show();
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
