package com.tvs.sign;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tvs.activity.R;
import com.tvs.activity.TVSEService;

public class MyPainter extends GraphicsActivity {

	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Path mPath;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private boolean FIRST_RUN = false;
	private boolean IsSuccess = false;
	MyView mView;
	String filename = TVSEService.filesign;
	Context context;
	private static final String TAG = "TVS";
	private static final int ERASE_MENU_ID = Menu.FIRST;
	private static final int SAVE_MENU_ID = Menu.FIRST + 1;
	private static final int PREVIEW_ID = Menu.FIRST + 2;
	private static boolean isNotAlreadyExit = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//filename = TVSEService.callList[TVSEService.ParentIndex]
	      //                 				.getCallListDto()[TVSEService.childIndex].getSerial_Number();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		context = getApplicationContext();
		

		mView = new MyView(this);
		setContentView(mView);
		mView.requestFocus();
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(3);
		FIRST_RUN = true;
		isNotAlreadyExit = true;
		if(null != TVSEService.oldFileSign && TVSEService.oldFileSign == TVSEService.filesign) {
			readSDCard();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if(null != TVSEService.mBitmapSign){
		// mBitmap = TVSEService.mBitmapSign;
		// mCanvas = TVSEService.mCanvas;
		// mPath= TVSEService.mPath;
		// mBitmapPaint= TVSEService.mBitmapPaint;
		// mPaint= TVSEService.mPaint;
		// mView= TVSEService.mView;
		// setContentView(mView);
		// }else{
		// mView = new MyView(this);
		// setContentView(mView);
		// }
	}

	public void colorChanged(int color) {
		mPaint.setColor(color);
	}

	public class MyView extends View {

		public MyView(Context c) {
			super(c);
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//			super.onSizeChanged(w, h, oldw, oldh);
//			
//			if(null == mBitmap)
//				mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//			
//			try  {
//				mCanvas = new Canvas(mBitmap);
//			}catch(Exception exception) {
//				System.out.println(exception.getMessage());
//			}
			int curW = mBitmap != null ? mBitmap.getWidth() : 0;
            int curH = mBitmap != null ? mBitmap.getHeight() : 0;
            if (curW >= w && curH >= h) {
                return;
            }

            if (curW < w) curW = w;
            if (curH < h) curH = h;

            Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
                                                   Bitmap.Config.RGB_565);
            Canvas newCanvas = new Canvas();
            newCanvas.setBitmap(newBitmap);
            if (mBitmap != null) {
                newCanvas.drawBitmap(mBitmap, 0, 0, null);
            }
            mBitmap = newBitmap;
            mCanvas = newCanvas;
            mCanvas.drawColor(0xFFFFFFFF);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if(null != mBitmap){// && null == TVSEService.oldFileSign || TVSEService.oldFileSign != TVSEService.filesign){
				canvas.drawColor(0xFFFFFFFF);
				canvas.drawBitmap(mBitmap, 0, 0, null);
				canvas.drawPath(mPath, mPaint);
				invalidate();
			}
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 0;

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			if(null != mBitmap && null == TVSEService.oldFileSign || TVSEService.oldFileSign != TVSEService.filesign){
				mPath.lineTo(mX, mY);
				// commit the path to our offscreen
				mCanvas.drawPath(mPath, mPaint);
				// kill this so we don't double draw
				mPath.reset();
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();
			/*
			 * very very important to call mView.onDraw(mCanvas) in order
			 * initialize all painting related objects before even drawing a
			 * point. else the consequence will be that drawing will be stored
			 * and saved in a null bitmap object, so there will be a drawing in
			 * black bg if this bitmap file is stored.
			 */
			if (FIRST_RUN == true) {
				mView.onDraw(mCanvas);
				FIRST_RUN = false;
			}
			if(isNotAlreadyExit){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					touch_start(x, y);
					invalidate();
					break;
				case MotionEvent.ACTION_MOVE:
					touch_move(x, y);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					touch_up();
					invalidate();
					break;
				}
			}
			return true;
		}
	}

	public void detectSDcard(Bitmap bitmap) {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			Log.d(TAG, "SD Card is available for read and write " + mExternalStorageAvailable
					+ mExternalStorageWriteable);
			saveTOSDCard(bitmap);
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			Log.d(TAG, "SD Card is available for read " + mExternalStorageAvailable);
			Toast.makeText(MyPainter.this, "SD Card is available for read-only", Toast.LENGTH_LONG).show();
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			Log.v(TAG, "Please insert a SD Card to save your Video " + mExternalStorageAvailable
					+ mExternalStorageWriteable);
			Toast.makeText(MyPainter.this, "Please insert a SD Card ", Toast.LENGTH_LONG).show();
		}
	}

	private void saveTOSDCard(Bitmap bitmap) {
		ContentValues values = new ContentValues();
		File sdImageMainDirPath = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.directory));// set dir path
		sdImageMainDirPath.mkdirs();// make dir if not present
		File outputFile = new File(sdImageMainDirPath, filename + ".png");
		values.put(MediaStore.MediaColumns.DATA, outputFile.toString());
		values.put(MediaStore.MediaColumns.TITLE, filename);
		values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
		Uri uri = this.getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				values);
		try {
			OutputStream outStream = this.getContentResolver().openOutputStream(uri);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			IsSuccess = true;
			Toast.makeText(MyPainter.this, "Sign Saved Successfully.", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Toast.makeText(MyPainter.this, "File not found.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(MyPainter.this, "Sign Could not be Saved.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	public void readSDCard() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			Log.d(TAG, "SD Card is available for read and write " + mExternalStorageAvailable
					+ mExternalStorageWriteable);
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			Log.d(TAG, "SD Card is available for read " + mExternalStorageAvailable);
			Toast.makeText(MyPainter.this, "SD Card is available for read-only", Toast.LENGTH_LONG).show();
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			Log.v(TAG, "Please insert a SD Card to save your Video " + mExternalStorageAvailable
					+ mExternalStorageWriteable);
			Toast.makeText(MyPainter.this, "Please insert a SD Card ", Toast.LENGTH_LONG).show();
		}

		File sdImageMainDirPath = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.directory));// set dir path
		if (sdImageMainDirPath.isDirectory()) {// make dir if not present
			// File outputFile = new File(sdImageMainDirPath, filename+".png");
			// try {
			// FileInputStream fileIS = new FileInputStream(outputFile);
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			Context mContext = getApplicationContext();
			//Dialog dialog = new Dialog(mContext);

			String testImg = sdImageMainDirPath.toString() + "/" + filename + ".png";
			// File f = new File(testImg);
			//         
			// ImageView im = (ImageView) dialog.findViewById(R.id.image);
			// Bitmap bm = BitmapFactory.decodeFile(testImg);
			// im.setImageBitmap(bm);
			// ImageView picturesView = (ImageView)
			// dialog.findViewById(R.id.image);
			// String imageInSD = c.getString(5); //"/sdcard/Hanud/image1.jpg"or
			// image2;

			// LinearLayout.LayoutParams pageTextLayout = new
			// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			Bitmap bitmap = BitmapFactory.decodeFile(testImg);
			mBitmap = bitmap;
			FIRST_RUN = false;
			isNotAlreadyExit = false;
			IsSuccess = true;
//			Canvas newCanvas = new Canvas();
//			newCanvas.drawBitmap(mBitmap, 0, 0,null);
//			newCanvas.drawColor(0xFFFFFFFF);
			//if(null == mCanvas) mCanvas = new Canvas();
			//mCanvas.setBitmap(mBitmap);
//			mCanvas = newCanvas;
//			mCanvas.drawColor(0xFFFFFFFF);
			//ImageView myImageView = new ImageView(this.context);
			// myImageView.setImageURI(null);
			//myImageView.setImageBitmap(bitmap);
			// dialog.addContentView(myImageView, pageTextLayout);

			// if (f == null || !f.canRead() || f.length()<1 ) {
			// Log.e("ocr", testImg+ " is not readable");
			// } else {
			// Log.i("ocr", "Trying to read file");
			// ImageView picturesView = (ImageView)
			// dialog.findViewById(R.id.image);
			// 		
			// picturesView.setImageURI(Uri.withAppendedPath(
			// MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" +
			// outputFile));
			// picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);

			//dialog.setContentView(R.layout.dialog);
			//dialog.setTitle("Custom Dialog");

			//dialog.show();

		} else {
			Toast.makeText(MyPainter.this, "Directory not found", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if(null == TVSEService.oldFileSign || TVSEService.oldFileSign != TVSEService.filesign) {
			menu.add(0, ERASE_MENU_ID, 0, "Clear").setShortcut('5', 'z');
			menu.add(0, SAVE_MENU_ID, 0, "Save");
		}
		// menu.add(0, PREVIEW_ID, 0, "Preview");
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case ERASE_MENU_ID:
//			if(null != TVSEService.oldFileSign){
//				Canvas newCanvas = new Canvas();
//				if (mBitmap != null) {
//					newCanvas.drawBitmap(mBitmap, 0, 0, null);
//				}
//				mCanvas = newCanvas;
//				mCanvas.drawColor(0xFFFFFFFF);
//			}
			mView = new MyView(this);
			mView.onDraw(mCanvas);
			//TVSEService.oldFileSign = null;
			return true;
		case SAVE_MENU_ID:
			if (null != TVSEService.oldFileSign) {
				File sdImageMainDirPath = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.directory));
				File outputFile = new File(sdImageMainDirPath, filename + ".png");
				boolean found = outputFile.exists();
				if (found) {
					boolean deleted = outputFile.delete();
				}
			}
			detectSDcard(mBitmap);
			TVSEService.oldFileSign = filename;

			if (IsSuccess) {
				TVSEService.Sign_Saved = true;
				Intent returnIntent = new Intent();
				returnIntent.putExtra("CUST_SIGNED", true);
				setResult(Activity.RESULT_OK, returnIntent);
				finish();
			}

			return true;
		case PREVIEW_ID:
			// readSDCard();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}