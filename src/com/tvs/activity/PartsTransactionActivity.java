package com.tvs.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class PartsTransactionActivity extends Activity {
	
	private TableLayout table = null;
	AlertDialog dialog = null;
	private boolean isProtected = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.partsdetails);
        
        Display newDisplay = getWindowManager().getDefaultDisplay(); 
		int width = newDisplay.getWidth()-20;
		
		TextView textview = (TextView)findViewById(R.id.txt_view_partNo_10);
		textview.setWidth(width/3);
		
		textview = (TextView)findViewById(R.id.txt_view_sNo_10);
		textview.setWidth(width/3);
		
		textview = (TextView)findViewById(R.id.txt_view_UniqueId_10);
		textview.setWidth(width/3);
        
        table = (TableLayout) findViewById(R.id.ppidTable_10);
		
		Button button = (Button)findViewById(R.id.btnDone_10);
		button.setOnClickListener(new CustomClickHandler());
		
		
		
		String[] ppid = TVSEService.masterDto.getPPID();
		TableRow row = null;
		
		String[] partNumber = TVSEService.callList[TVSEService.ParentIndex].getCallListDto()[TVSEService.childIndex].getPartNumber();
		
		if(null != partNumber){
			//width = newDisplay.getWidth()-20;
			int count = TVSEService.masterDto.getPart_Usage().length;
			count = partNumber.length;
			String[] uniqueId = TVSEService.callList[TVSEService.ParentIndex].getCallListDto()[TVSEService.childIndex].getUniqueId();
			String[] selectedPartUsage = TVSEService.masterDto.getSelectedPartUsage();
			String[] selectedPartUsageId = TVSEService.masterDto.getSelectedPartUsage_ID();
			for(int i=0;i<count;i++){
				if(null != selectedPartUsage){
					row = createPartsRows(selectedPartUsage[i],ppid[i],i,partNumber[i],uniqueId[i],width,selectedPartUsageId[i]);
				} else {
					row = createPartsRows(null,null,i,partNumber[i],uniqueId[i],width,null);
				}
				table.addView(row);
				//table.addView(row, layoutParams);
			}
		}
    }
    
    
    public TableRow createPartsRows(String selectedPartUsage, String ppid,int serialNumber,String partNumber,String uniqueId, 
    		int width,String id) {
		TableRow row = new TableRow(this);
		LayoutInflater inflater = (LayoutInflater) this.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout itemView = (LinearLayout)inflater.inflate(R.layout.partsdetails_inflater, null);
		
		TextView textView = (TextView)itemView.findViewById(R.id.txtserialNo_10);
		textView.setText((serialNumber+1)+"");
		textView.setWidth(width/3);
		
		textView = (TextView)itemView.findViewById(R.id.txtuniqueIdHeader_10);
		textView.setText(uniqueId);
		textView.setWidth(width/3);
		
		textView = (TextView)itemView.findViewById(R.id.txtPartNumber_10);
		textView.setText(partNumber);
		textView.setWidth(width/3);
		
		
		
		
		final Button button = (Button)itemView.findViewById(R.id.btnPartUsage_10);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
            public void onClick(View v) 
            {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						new ContextThemeWrapper(PartsTransactionActivity.this,R.style.alert_list_style));
				// Set its title
				builder.setTitle("Select Any");
				// Set the list items and assign with the click listener
				builder.setItems(TVSEService.masterDto.getPart_Usage(), new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dia, int position) 
		            {
		            	String value = TVSEService.masterDto.getPart_Usage_Id()[position];
		            	button.setTag(value);
		            	value = TVSEService.masterDto.getPart_Usage()[position];
		            	button.setText(value);
		            	
		            	if(dialog.isShowing())
		            		dialog.dismiss();
		            	EditText editText = (EditText)itemView.findViewById(R.id.edtxtppid_10);
						editText.setText("");
						if(null != value && (value.trim().compareTo("D-Returned DOA - RNU1") == 0 || value.trim().compareTo("D-Returned DOA - RNU2") == 0)){
							editText.setEnabled(true);
						} else editText.setEnabled(false);
		            }
		        }).setTitle("Select Any");
		        dialog = builder.create();
		        dialog.show();
            }
            });

		if(null != selectedPartUsage)
		{
			button.setText(selectedPartUsage);
			button.setTag(id);
		}
		//itemView.
		final EditText txt_pNo = (EditText)itemView.findViewById(R.id.edtxtppid_10);
		if(null != ppid && ppid.length()>0){
			txt_pNo.setText(formatPPID(ppid));
			txt_pNo.setEnabled(true);
		} else txt_pNo.setEnabled(false);
		
		
		InputFilter[] FilterArray = new InputFilter[2];
		FilterArray[0] = new InputFilter.LengthFilter(28);
		FilterArray[1] = new InputFilter() {
			
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				for (int i = start; i < end; i++) { 
                    if (!Character.isLetterOrDigit(source.charAt(i)) && source.charAt(i) != '-') { 
                            return ""; 
                    } 
				} 
            return null;
			}
		};
		
		txt_pNo.setFilters(FilterArray);
		
		txt_pNo.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				boolean isChanged = false;
				StringBuffer text = new StringBuffer(s);
				if(!isProtected && text.length() > 2) {
					if(text.charAt(2) != '-'){ 
						text = text.insert(2, '-');
						isChanged = true;
					}
					if(text.length() > 9)
						if(text.charAt(9) != '-') {
							text = text.insert(9, '-');
							isChanged = true;
						}
					if(text.length() > 15)
						if(text.charAt(15) != '-'){
							text = text.insert(15, '-');
							isChanged = true;
						}
					if(text.length() > 19)
						if(text.charAt(19) != '-') {
							text = text.insert(19, '-');
							isChanged = true;
						}
					if(text.length() > 24)
						if(text.charAt(24) != '-') {
							text = text.insert(24, '-');
							isChanged = true;
						}
					if(isChanged){
						isProtected = true;
						txt_pNo.setText(text);
						txt_pNo.setSelection(text.length());
						isProtected = false;
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
		});
		row.addView(itemView);
		return row;
	}
    
    
    private void loadDialog(final View v, final View itemView){
    	AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(this,R.style.alert_list_style));
		// Set its title
		builder.setTitle("Select Any");
		// Set the list items and assign with the click listener
		builder.setItems(TVSEService.masterDto.getPart_Usage(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dia, int position) 
            {
            	String value = TVSEService.masterDto.getPart_Usage_Id()[position];
            	v.setTag(value);
            	value = TVSEService.masterDto.getPart_Usage()[position];
            	((Button)v).setText(value);
            	
            	if(dialog.isShowing())
            		dialog.dismiss();
            	EditText editText = (EditText)itemView.findViewById(R.id.edtxtppid_10);
				editText.setText("");
				if(null != value && (value.trim().compareTo("D-Returned DOA - RNU1") == 0 || value.trim().compareTo("D-Returned DOA - RNU2") == 0)){
					editText.setEnabled(true);
				} else editText.setEnabled(false);
            }
        }).setTitle("Select Any");
        dialog = builder.create();
        dialog.show();
    }
    
    private String formatPPID(String PPID) {
		if (PPID.length() == 24) {
			String ppid1 = PPID.substring(0, 2);
			String ppid2 = PPID.substring(2, 8);
			String ppid3 = PPID.substring(8, 13);
			String ppid4 = PPID.substring(13, 16);
			String ppid5 = PPID.substring(16, 20);

			String dash1 = PPID.substring(2, 3);
			String dash2 = PPID.substring(9, 10);
			String dash3 = PPID.substring(15, 16);
			String dash4 = PPID.substring(19, 20);
			if (dash1.equals("-"))
				if (dash2.equals("-"))
					if (dash3.equals("-"))
						if (dash4.equals("-"))
							return PPID;
			PPID = ppid1 + "-" + ppid2 + "-" + ppid3 + "-" + ppid4 + "-" + ppid5;
		} 
		return PPID;
	}

    
    private class CustomClickHandler implements OnClickListener {
    	private void goBack(){
    		int count = table.getChildCount();
    		String[] partUsage = new String[count];
			String[] ppid = new String[count];
			String[] partUsageID = new String[count];
			if(count>0){
				TableRow row = null;
				Button button = null;
				EditText editText = null;
				String value = null;
				for(int i=0;i<count;i++){
					 row = (TableRow)table.getChildAt(i);
					 button =  (Button)row.findViewById(R.id.btnPartUsage_10);
					 value = button.getText().toString();
					 if("Part Usage".compareTo(value) == 0){
						 Toast.makeText(PartsTransactionActivity.this, "Part usage type not filled for the S.no " + (i+1), Toast.LENGTH_LONG).show();
						 return;
					 } 
					 partUsage[i] = value;
					 
					 value = (String)button.getTag();
					 partUsageID[i] = value;
					 
					 editText =  (EditText)row.findViewById(R.id.edtxtppid_10);
					 if(editText.isEnabled()){
						 value = editText.getText().toString();
						 if (value.length() == 28) {
							 ppid[i] = value;
						 } else {
							 Toast.makeText(PartsTransactionActivity.this, "Please enter the correct PPID, PPID should be 23 Characters", Toast.LENGTH_SHORT).show();
							 return;
						  }
					 }
				}
			}
			TVSEService.masterDto.setSelectedPartUsage(partUsage);
			TVSEService.masterDto.setPPID(ppid);
			TVSEService.masterDto.setSelectedPartUsage_ID(partUsageID);
			Intent intent = new Intent();
			setResult(3, intent);
			finish();
    	}
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.btnDone_10:
					goBack();
					break;
			}
		}
    }
    
}