package com.tvs.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tvs.dto.CallListDTO;
import com.tvs.module.Constants;


public class CallDetailsActivity extends Activity {
    /** Called when the activity is first created. */
	String phoneNumber = null;
	String altPhoneNumber = null;
	boolean isRC17Finished = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.call_details);
        CallListDTO callListDto = TVSEService.callList[TVSEService.ParentIndex].getCallListDto()[TVSEService.childIndex];
        isRC17Finished = callListDto.getFlag_rc17();
       
        TextView textview =(TextView)findViewById(R.id.txtEngineerName_03);
        textview.setText(callListDto.getEngineer_Name());
        
        TextView techId = (TextView)findViewById(R.id.txtTechnicianId_03);
        techId.setText(callListDto.getTechnician_Id());
        
        TextView sla= (TextView)findViewById(R.id.txtSLA_03);
        sla.setText(callListDto.getSla());
        
        TextView asset = (TextView)findViewById(R.id.txtAsset_03);
        asset.setText(callListDto.getsTag());
        
        TextView product_code= (TextView)findViewById(R.id.txtProductCode_03);
        product_code.setText(callListDto.getProduct_Code());
        
        TextView parts_No = (TextView)findViewById(R.id.txtPartNo_03);
        parts_No.setText(callListDto.getParts_Number());
        
        TextView tech_Add = (TextView)findViewById(R.id.txtCustomerAddress_03);
        tech_Add.setText(callListDto.getCustomer_Address());
        
        TextView displatch_DT = (TextView)findViewById(R.id.txtDipatchDT_03);
        displatch_DT.setText(callListDto.getDispatch_Time());
        
        TextView part_eat_date = (TextView)findViewById(R.id.txtPartETADT_03);
        part_eat_date.setText(callListDto.getPart_ETA_Time());
        
        TextView customer_EAt_date= (TextView)findViewById(R.id.txtCustomerETADT_03);
        customer_EAt_date.setText(callListDto.getCustomer_Date_Time());
        
        TextView org_name = (TextView)findViewById(R.id.txtOrgName_03);
        org_name.setText(callListDto.getOrgination_Name());
        
        TextView contact_name = (TextView)findViewById(R.id.txtContactName_03);
        contact_name.setText(callListDto.getCustomer_Name());
        
        Button callimage =(Button)findViewById(R.id.imgViewCall_03);
        callimage.setOnClickListener(new CustomClickHandler());
        
        Button altCall =(Button)findViewById(R.id.imgViewCallAlt_03);
        altCall.setOnClickListener(new CustomClickHandler());
        
        View viewPhone = (View) findViewById(R.id.viewphone_03);
        viewPhone.setOnClickListener(new CustomClickHandler());
        
        View callAltview = (View) findViewById(R.id.viewphoneAlt_03);
        callAltview.setOnClickListener(new CustomClickHandler());
        
        TextView Phone_no = (TextView)findViewById(R.id.txtPhoneNo_03);
        phoneNumber = callListDto.getConstact_Number();
        Phone_no.setText(callListDto.getConstact_Number());
        Phone_no.setOnClickListener(new CustomClickHandler());
        String strphno = callListDto.getConstact_Number();
        if(null != strphno){
        	callimage.setVisibility(Button.VISIBLE);
        	viewPhone.setVisibility(View.VISIBLE);
        }else{
        	callimage.setVisibility(Button.GONE);
        	viewPhone.setVisibility(View.GONE);
        }

        TextView alt_no = (TextView)findViewById(R.id.txtAltNo_03);
        altPhoneNumber = callListDto.getAlterNative_Contact_Number();
        alt_no.setText(callListDto.getAlterNative_Contact_Number());
        alt_no.setOnClickListener(new CustomClickHandler());
        String stralt = callListDto.getAlterNative_Contact_Number();
        if(null != stralt){
        	altCall.setVisibility(Button.VISIBLE);
        	callAltview.setVisibility(View.VISIBLE);
        }else{
        	altCall.setVisibility(Button.GONE);
        	callAltview.setVisibility(View.GONE);
        }
        
        TextView cust_add = (TextView)findViewById(R.id.txtCustomerAddress_03);
        cust_add.setText(callListDto.getCustomer_Address());
        
        TextView summary = (TextView)findViewById(R.id.txtSummary_03);
       summary.setText(callListDto.getSummery());
        
       
       TextView long_desc = (TextView)findViewById(R.id.txtLongDes_03);
       String subtext = "DSP:";
       long_desc.setText(callListDto.getLong_Description(), TextView.BufferType.SPANNABLE);
       Spannable str = (Spannable)  long_desc.getText();
       if(null != callListDto.getLong_Description()){
	       int i = callListDto.getLong_Description().indexOf(subtext);
	       if (i>0)str.setSpan(new ForegroundColorSpan(Color.RED), i, i+subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       }
       long_desc.setText(str);

       
       TextView ser_no = (TextView)findViewById(R.id.txtSERNUMBER_03);
       ser_no.setText(callListDto.getSerial_Number());
       
       TextView tech_supportNamer = (TextView)findViewById(R.id.txtTechSupportName_03);
       tech_supportNamer.setText(callListDto.getTech_Support_Name());
       
       TextView phoneHeader = (TextView)findViewById(R.id.txtPhoneNoHeader_03);
       phoneHeader.setOnClickListener(new CustomClickHandler());

       TextView AltPhoneHeader = (TextView)findViewById(R.id.txtAltNoHeader_03);
       AltPhoneHeader.setOnClickListener(new CustomClickHandler());
       
     
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.commonmenu, menu);
        menu.getItem(0).setVisible(isRC17Finished);
    	menu.getItem(1).setVisible(!isRC17Finished);
    	menu.getItem(2).setVisible(!isRC17Finished);
        return true;
    }
    
    public boolean onPrepareOptionsMenu (Menu menu) {
    	menu.getItem(0).setVisible(!isRC17Finished);
    	menu.getItem(1).setVisible(isRC17Finished);
    	menu.getItem(2).setVisible(isRC17Finished);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by onOptionsItemSelected()
    	Intent callCompleted = null;
    	Constants.statusString = null;
    	switch (item.getItemId()) {
			case R.id.menuCallCompleted:
				CallListDTO callList = TVSEService.callList[TVSEService.ParentIndex].getCallListDto()[TVSEService.childIndex];
				if(callList.isNoPart()){
					if(callList.isPartIssued()){
						Toast.makeText(CallDetailsActivity.this, "Call Completion is not allowed , Part is not issued in system", Toast.LENGTH_LONG).show();
						return true;
					}
					if(null != callList.getPartNumber()){
						if(callList.getPartCount() != callList.getPartNumber().length){
							Toast.makeText(CallDetailsActivity.this, "Call completion is not allowed , Part count mismatches between physical receipt and system.", Toast.LENGTH_LONG).show();
							return true;
						}
					} else if(callList.getPartCount()>0) {
						Toast.makeText(CallDetailsActivity.this, "Call completion is not allowed , Part count mismatches between physical receipt and system.", Toast.LENGTH_LONG).show();
						return true;
					}
				}
				
				if(isRC17Finished){
					TVSEService.masterDto.setPartsCount(null);
					TVSEService.masterDto.setAdditional_Activity_Enabled(null);
					TVSEService.masterDto.setSelectedPartUsage(null);
					TVSEService.masterDto.setSelectedPartUsage_ID(null);
					TVSEService.masterDto.setPPID(null);
					callCompleted = new Intent(CallDetailsActivity.this,CallCompletedActivity.class);
					startActivityForResult(callCompleted, 4);
				} else Toast.makeText(CallDetailsActivity.this, "Please Complete RC17 First", Toast.LENGTH_LONG).show();
				return true;
			case R.id.menuRC52:
				if(isRC17Finished ){
					callCompleted = new Intent(CallDetailsActivity.this,RC52Activity.class);
					startActivityForResult(callCompleted, 1);
				} else Toast.makeText(CallDetailsActivity.this, "Please Complete RC17 First", Toast.LENGTH_LONG).show();
				return true;	
			case R.id.menuRC17:
				if(!isRC17Finished ){
					callCompleted = new Intent(CallDetailsActivity.this,RC17Activity.class);
					startActivityForResult(callCompleted, 2);
				} else Toast.makeText(CallDetailsActivity.this, "Already RC17 Completed", Toast.LENGTH_LONG).show();
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
    }
    
    private class CustomClickHandler implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.txtAltNoHeader_03:
				case R.id.viewphoneAlt_03:	
				case R.id.imgViewCallAlt_03:	
				case R.id.txtAltNo_03:
					navigate(altPhoneNumber);
				break;
				case R.id.txtPhoneNoHeader_03:
				case R.id.viewphone_03:	
				case R.id.txtPhoneNo_03:	
				case R.id.imgViewCall_03:
			        try {
			            navigate(phoneNumber);
			        } catch (ActivityNotFoundException e) {
			            Log.d("helloandroid dialing example", "Call failed", e);
			        }
			    
				break;
			}
		}

    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 2 ||resultCode == 1 || resultCode == 4){
			Intent intent = new Intent();
			setResult(0,intent);
			finish();
		} 
	}
    
 public void navigate(String Number){
	 if(null != Number){
		 Intent callIntent = new Intent(Intent.ACTION_CALL);
	     callIntent.setData(Uri.parse("tel:"+Number));
	     startActivityForResult(callIntent, 4);
	 }
    }
}