package com.tvs.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;


public class AdditionalActivity extends Activity {
	private TableLayout table = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.additional_activity);

		table = (TableLayout) findViewById(R.id.ActivityTable);
		LayoutParams layoutParams = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT);

		Button button = (Button)findViewById(R.id.buttonSubmit_activity_01);
		button.setOnClickListener(new CustomClickHandler());
		Display newDisplay = getWindowManager().getDefaultDisplay(); 
		int width = newDisplay.getWidth();
		TableRow row = null;
		String[] activity = TVSEService.masterDto.getAdditional_Activity();
		boolean[] activity_Enabled = TVSEService.masterDto.getAdditional_Activity_Enabled();
		for (int i = 0; i <activity.length; i++) {
			if(null != activity_Enabled)
				row = createPartsRows(activity[i],activity_Enabled[i], width);
			else row = createPartsRows(activity[i],false, width);
			table.addView(row, layoutParams);
		}
    }
    
    
    public TableRow createPartsRows(String value, boolean isSelected, int width) {
		TableRow row = new TableRow(this);
		LayoutInflater inflater = (LayoutInflater) this.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout itemView = (LinearLayout)inflater.inflate(R.layout.additional_activity_inflator, null);	
		TextView txt_pName = (TextView)itemView.findViewById(R.id.txtactivity_01);
		txt_pName.setText(value);
		txt_pName.setWidth(width-51);
		CheckBox checkbox = (CheckBox)itemView.findViewById(R.id.cboxactivity_01);
		checkbox.setChecked(isSelected);
		row.addView(itemView);
		return row;
	}
    
    private class CustomClickHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int count = table.getChildCount();
			TableRow row = null;
			CheckBox checkbox = null;
			boolean[] selected = TVSEService.masterDto.getAdditional_Activity_Enabled();
			for(int i=0;i<count;i++){
				 row = (TableRow)table.getChildAt(i);
				 checkbox =  (CheckBox)row.findViewById(R.id.cboxactivity_01);
				 selected[i] = checkbox.isChecked();
			}
			TVSEService.masterDto.setAdditional_Activity_Enabled(selected);
			Intent intent = new Intent();
			setResult(0, intent);
			finish();
		}
    }
}