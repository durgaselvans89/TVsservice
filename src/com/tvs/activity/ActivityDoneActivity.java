package com.tvs.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tvs.module.Constants;
import com.tvs.utility.LayoutUtility;

/**
 * 
 * @author XRG
 * @version 1.0
 */
public class ActivityDoneActivity extends Activity {

	//private Context context;
	CheckBox previousCheck = null;
	String selectedPlanId = null;
	private TextView item[];
	private CheckBox checkView[];
	String[] values = null;
	String[] items = null;

	/**
	 * OnCreate method for plan activity.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.plan);
		String value = this.getIntent().getStringExtra("OldData");
		loadActivityDone(value);
		Button button = (Button) findViewById(R.id.DoneButton);
		button.setOnClickListener(new CustomClickHandler());
		button = (Button) findViewById(R.id.NoneButton);
		button.setOnClickListener(new CustomClickHandler());
	}

	private void loadActivityDone(String oldValues) {
		String[] items_temp = TVSEService.masterDto.getActivity_done();
		boolean checkstate = false;
		items = eleminateLastValue(items_temp);
		TableLayout table = (TableLayout) findViewById(R.id.table_plan);
		LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
		int length = items.length;
		TableRow[] row = new TableRow[length];
		View[] rowspacer = new View[length];
		item = new TextView[length];
		checkView = new CheckBox[length];
		Display d = getWindowManager().getDefaultDisplay();
		int scr_width = d.getWidth();

		for (int i = 0; i < length; i++) {
			if(oldValues.indexOf(items[i]+"-")>-1)
				checkstate = true;
			else checkstate = false;
			row[i] = createRows(i, items[i], checkstate, getApplicationContext(), scr_width);
			rowspacer[i] = createRowSpacers(getApplicationContext());
			table.addView(row[i], layoutParams);
			table.addView(rowspacer[i], layoutParams);
		}

	}

	public View createRowSpacers(Context context) {

		View spacer = new View(context);
		spacer.setMinimumHeight(1);
		spacer.setBackgroundColor(Color.GRAY);
		return spacer;
	}

	public String[] eleminateLastValue(String[] temp) {
		ArrayList<String> temp_list = null;
		String vals = null;
		int len;
		if (null != temp) {
			len = temp.length - 1;
			temp_list = new ArrayList<String>();

			for (int i = 0; i < len; i++) {
				vals = temp[i];
				temp_list.add(vals);
			}
		}
		if (null != temp_list && temp_list.size() > 0) {
			temp = temp_list.toArray(new String[temp_list.size()]);
		}
		return temp;
	}

	public TableRow createRows(int id, String planName, boolean defBool,Context context, int width) {

		TableRow row = null;

		LinearLayout itemLayout = new LinearLayout(context);
		itemLayout.setOrientation(LinearLayout.HORIZONTAL);

		item[id] = new TextView(context);
		item[id].setText(planName);
		item[id].setTextColor(Color.WHITE);
		item[id].setWidth(width-50);
		item[id].setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams pageTextLayout = new LinearLayout.LayoutParams(width-50, LayoutParams.WRAP_CONTENT);
		item[id].setLayoutParams(pageTextLayout);
		itemLayout.addView(item[id]);
		LayoutUtility.Layout.WidthWrap_HeightWrap.applyTableRowParams(itemLayout);

		LinearLayout checkLayout = new LinearLayout(context);
		checkView[id] = new CheckBox(context);
		checkView[id].setTag(id);
		checkView[id].setClickable(true);
		checkView[id].setChecked(defBool);
		checkView[id].setWidth(50);

		LinearLayout.LayoutParams pageCheckLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);;
		checkView[id].setLayoutParams(pageCheckLayout);
		checkView[id].setGravity(Gravity.CENTER_VERTICAL);
		checkLayout.addView(checkView[id]);
		LayoutUtility.Layout.WidthFill_HeightWrap
				.applyTableRowParams(checkLayout);

		row = new TableRow(context);
		LayoutUtility.Layout.WidthWrap_HeightWrap.applyTableLayoutParams(row);
		row.addView(itemLayout);
		row.addView(checkLayout);
		return row;
	}

	private class CustomClickHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			String msg = "";
			Intent intent;
			switch (v.getId()) {
			case R.id.NoneButton:
				if (null != checkView) {
					for (int i = 0; i < checkView.length; i++) {
						checkView[i].setChecked(false);
					}
				}

				Toast.makeText(ActivityDoneActivity.this, "None Selected",Toast.LENGTH_LONG).show();
				break;
			case R.id.DoneButton:
				boolean isNone = false;
				for (int i = 0; i < checkView.length; i++) {
					if (checkView[i].isChecked()) {
						isNone = true;
						break;
					}
				}
				if (isNone) {
					ArrayList<String> shop = new ArrayList<String>();

					for (int i = 0; i < checkView.length; i++) {
						if (checkView[i].isChecked()) {
							if (!item[i].getText().toString().equals(""))
								shop.add(item[i].getText().toString());
						}
					}

					if (null != shop && shop.size() > 0) {
						values = shop.toArray(new String[shop.size()]);
					}
					if (null != values) {
						for (int i = 0; i < values.length; i++) {
							msg += values[i] + "-";			
						}
						//msg = msg.substring(0,msg.length() - 1);
					} else {
						Toast.makeText(ActivityDoneActivity.this,
								"Please select activity", Toast.LENGTH_LONG)
								.show();
					}
					intent = new Intent();
					intent.putExtra(Constants.ACTIVITY_DONE, msg);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					msg = "NONE";
					intent = new Intent();
					intent.putExtra(Constants.ACTIVITY_DONE, msg);
					setResult(RESULT_OK, intent);
					finish();
				}
				break;
			}

		}
	}

}
