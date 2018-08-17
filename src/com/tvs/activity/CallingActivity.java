package com.tvs.activity;





import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;



public class CallingActivity extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.calling);
		call();	
	}
    
    private void call() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            //callIntent.setData(Uri.parse("0123456789"));
            startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            Log.d("helloandroid dialing example", "Call failed", e);
        }
    }
}
