package com.tvs.dao;


import java.util.ArrayList;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tvs.dto.CallList;
import com.tvs.dto.CallListDTO;
import com.tvs.module.Constants;
import com.tvs.module.CustomException;


public class DBHelperDAO {

	private SQLiteDatabase db;
	private OpenHelper openHelper;

	public DBHelperDAO(Context context) {
		openHelper = new OpenHelper(context);
	}

	public SQLiteDatabase getDb() throws SQLException{
		try{
			this.db = openHelper.getWritableDatabase();
		}catch(SQLException exception){
			exception.printStackTrace();
		}
		return this.db;
	}

	public DBHelperDAO openDB() throws SQLException {
		try{
			this.db = openHelper.getWritableDatabase();
		}catch(SQLException exception){
			exception.printStackTrace();
		}
		return this;
	}
	
	public DBHelperDAO openReadableDB() throws SQLException {
		try{
			this.db = openHelper.getReadableDatabase();
		}catch(SQLException exception){
			exception.printStackTrace();
		}
		return this;
	}

	public void closeDB() {
		openHelper.close();
	}

	public void updateNewCallList(CallList[] serviceCallList) throws CustomException,SQLException{
		try{
			if(null != serviceCallList){
				db.delete(DAOConstants.SERVICE_TABLE_NAME, null, null);
				int count = serviceCallList.length;
				String appointDate = null;
				CallListDTO[] serviceCall = null;
				int dtoCount = 0;
				ContentValues contentValues = null;
				for(int i=0;i<count;i++){
					appointDate = serviceCallList[i].getAppointment_Date();
					serviceCall = serviceCallList[i].getCallListDto();
					dtoCount = serviceCall.length;
					for(int j=0;j<dtoCount;j++){
						contentValues = new ContentValues();
						contentValues.put(Constants.SERIALNUMBER, serviceCall[j].getSerial_Number());
						contentValues.put(Constants.SERVICEDATE, appointDate);
						contentValues.put(Constants.ENG_NAME, serviceCall[j].getEngineer_Name());
						contentValues.put(Constants.TECHID, serviceCall[j].getTechnician_Id());
						contentValues.put(Constants.SLA, serviceCall[j].getSla());
						contentValues.put(Constants.PRODUCTCODE, serviceCall[j].getProduct_Code());
						contentValues.put(Constants.PARTNO, serviceCall[j].getParts_Number());
						contentValues.put(Constants.TECHSUPPORTNAME, serviceCall[j].getTech_Support_Name());
						contentValues.put(Constants.DISPATCHDATETIME, serviceCall[j].getDispatch_Time());
						contentValues.put(Constants.ORGANIZATIONNAME, serviceCall[j].getOrgination_Name());
						contentValues.put(Constants.CONTACTNAME, serviceCall[j].getCustomer_Name());
						contentValues.put(Constants.CONTACTNO, serviceCall[j].getConstact_Number());
						contentValues.put(Constants.ALTCONTACTNO, serviceCall[j].getAlterNative_Contact_Number());
						contentValues.put(Constants.ADDRESS, serviceCall[j].getCustomer_Address());
						contentValues.put(Constants.SUMMARY,serviceCall[j].getSummery());
						contentValues.put(Constants.LONGDESCRIPTION, serviceCall[j].getLong_Description());
						if(serviceCall[j].getFlag_rc17())
							contentValues.put(Constants.STATUS,"yes");
						else
							contentValues.put(Constants.STATUS,"no");
						contentValues.put(Constants.CUSTOMERETADATETIME, serviceCall[j].getCustomer_Date_Time());
						
						contentValues.put(Constants.PART_NUMBER,getPartTransactionAppendValue(serviceCall[j].getPartNumber()));
						contentValues.put(Constants.UNIQUE_ID,getPartTransactionAppendValue(serviceCall[j].getUniqueId()));
						contentValues.put(Constants.PARTCOUNT,serviceCall[j].getPartCount());
						if(serviceCall[j].isPartIssued())
							contentValues.put(Constants.PARTSTATUS,"NOTISSUED");
						else contentValues.put(Constants.PARTSTATUS,"ISSUED");
						if(serviceCall[j].isNoPart()){
							contentValues.put(Constants.NOPART,"YES");
						} else {
							contentValues.put(Constants.NOPART,"NO");
						}
						
						
						db.insert(DAOConstants.SERVICE_TABLE_NAME,null, contentValues);
					}
				}
				db.setTransactionSuccessful();
			}
		}catch(SQLException exception){
			throw new CustomException(exception.getMessage(),Constants.ERR_COMMON);
		}
	}
	
	
	private String getPartTransactionAppendValue(String[] values){
		String master = null;
		StringBuffer buffer = null;
		if(null != values){
			buffer = new StringBuffer();
			for(int i = 0;i < values.length;i++){
				buffer.append(values[i]);
				buffer.append("|");
			}
			master = buffer.toString().substring(0, buffer.toString().length() - 1);
		}
		return master;
	}
	
	
	public boolean updateCallListData(String url,byte[] image_bytes){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Constants.URL,url);
		contentValues.put(Constants.IMAGE_BYTE,image_bytes);
		long rowInserted = db.insert(DAOConstants.FOREIGN_SERVICE_TABLE_NAME,null, contentValues);
		if(rowInserted != -1){
		 return true;
		}
		return false;
	}
	
	public boolean deleteCallListDatasUrl(Object[] object){
		if(null != object){
			String url = (String)object[0];
			int count = db.delete(DAOConstants.FOREIGN_SERVICE_TABLE_NAME, Constants.URL+"=?", new String[]{url});
			if(count>-1)
				return true;
		}
		return false;
	}
	
	public boolean updateCallListData(String serialnumber, String updateFlag){
		int count = -1;
		if(Constants.RC17.compareTo(updateFlag) == 0){
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constants.STATUS,"yes");
			count = db.update(DAOConstants.SERVICE_TABLE_NAME, contentValues, Constants.SERIALNUMBER+"=?", new String[]{serialnumber});
		} else {
			count = db.delete(DAOConstants.SERVICE_TABLE_NAME, Constants.SERIALNUMBER+"=?", new String[]{serialnumber});
		}
		if(count>-1)
			return true;
		return false;
	}
	
	
	public ArrayList<Object[]> getCallListDatas(){
		Cursor cursor = null;
		ArrayList<Object[]> urlValues = null;
		try{
			cursor = db.rawQuery(DAOConstants.GET_SEND_DATA, new String[]{});
			int count = cursor.getCount();
			Object[] object = null;
			urlValues = new ArrayList<Object[]>();
			for(int i=0;i<count;i++){
				cursor.moveToPosition(i);
				object = new Object[2];
				object[0] = (cursor.getString(cursor.getColumnIndex(Constants.URL)));
				object[1] = (cursor.getBlob(cursor.getColumnIndex(Constants.IMAGE_BYTE)));
				urlValues.add(object);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
		return urlValues;
	}
	
	public CallList[] getCallList() throws SQLException{		
		CallList[] call = null;
		Cursor cursor = null;
		Hashtable<String, ArrayList<CallListDTO>> result = null;
		ArrayList<CallListDTO> tempCallList = null;
		ArrayList<String> dateString = null;
		String date = null;
		String previousDate = null;
		String tempValue = null;
		try{		
			cursor = db.rawQuery(DAOConstants.GET_CALL_LIST, new String[]{});
			if(null != cursor){
				result = new Hashtable<String, ArrayList<CallListDTO>>();
				dateString = new ArrayList<String>();
				int count = cursor.getCount();
				CallListDTO serviceCall = null;
				
				for(int i=0;i<count;i++){
					cursor.moveToPosition(i);
					serviceCall = new CallListDTO();
					serviceCall.setSerial_Number(cursor.getString(cursor.getColumnIndex(Constants.SERIALNUMBER)));
					serviceCall.setEngineer_Name(cursor.getString(cursor.getColumnIndex(Constants.ENG_NAME)));
					serviceCall.setTechnician_Id(cursor.getString(cursor.getColumnIndex(Constants.TECHID)));
					serviceCall.setSla(cursor.getString(cursor.getColumnIndex(Constants.SLA)));
					serviceCall.setProduct_Code(cursor.getString(cursor.getColumnIndex(Constants.PRODUCTCODE)));
					serviceCall.setParts_Number(cursor.getString(cursor.getColumnIndex(Constants.PARTNO)));
					serviceCall.setTech_Support_Name(cursor.getString(cursor.getColumnIndex(Constants.TECHSUPPORTNAME)));
					serviceCall.setDispatch_Time(cursor.getString(cursor.getColumnIndex(Constants.DISPATCHDATETIME)));
					serviceCall.setOrgination_Name(cursor.getString(cursor.getColumnIndex(Constants.ORGANIZATIONNAME)));
					serviceCall.setCustomer_Name(cursor.getString(cursor.getColumnIndex(Constants.CONTACTNAME))); //contactname
					serviceCall.setConstact_Number(cursor.getString(cursor.getColumnIndex(Constants.CONTACTNO)));
					serviceCall.setAlterNative_Contact_Number(cursor.getString(cursor.getColumnIndex(Constants.ALTCONTACTNO)));
					serviceCall.setCustomer_Address(cursor.getString(cursor.getColumnIndex(Constants.ADDRESS))); //Address
					serviceCall.setSummery(cursor.getString(cursor.getColumnIndex(Constants.SUMMARY)));
					serviceCall.setLong_Description(cursor.getString(cursor.getColumnIndex(Constants.LONGDESCRIPTION)));
					serviceCall.setFlag_rc17(cursor.getString(cursor.getColumnIndex(Constants.STATUS)));
					
					tempValue = cursor.getString(cursor.getColumnIndex(Constants.PART_NUMBER));
					if(null != tempValue)
						serviceCall.setPartNumber(tempValue.split("\\|"));
					
					tempValue = cursor.getString(cursor.getColumnIndex(Constants.UNIQUE_ID));
					if(null != tempValue)
						serviceCall.setUniqueId(tempValue.split("\\|"));
					
					serviceCall.setPartIssued(cursor.getString(cursor.getColumnIndex(Constants.PARTSTATUS)));
					serviceCall.setPartCount(cursor.getString(cursor.getColumnIndex(Constants.PARTCOUNT)));
					serviceCall.setNoPart(cursor.getString(cursor.getColumnIndex(Constants.NOPART)));
					
					date = cursor.getString(cursor.getColumnIndex(Constants.SERVICEDATE));
					serviceCall.setCustomer_Date_Time(cursor.getString(cursor.getColumnIndex(Constants.CUSTOMERETADATETIME)));
					if(result.containsKey(date)){
						tempCallList = (ArrayList<CallListDTO>)result.get(date);
						result.remove(date);
					} else {
						tempCallList = new ArrayList<CallListDTO>();
					}
					tempCallList.add(serviceCall);
					result.put(date, tempCallList);
					if(null == previousDate || date.compareTo(previousDate) != 0){
						dateString.add(date);
						previousDate = date;
					}
				}
			}
		}catch(SQLException exception){
			exception.printStackTrace();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}
	
		if(null != result){
			call = new CallList[result.size()];
			String[] dates = (String[])dateString.toArray(new String[dateString.size()]);
			int count = dates.length;;
			CallListDTO[] callListdto = null;
			for(int i=0;i<count;i++){
				tempCallList = (ArrayList<CallListDTO>)result.get(dates[i]);
				callListdto = (CallListDTO[]) tempCallList.toArray(new CallListDTO[tempCallList.size()]);
				call[i] = new CallList();
				call[i].setNumberOf_Calls(tempCallList.size()+"");
				call[i].setCallListDto(callListdto);
				call[i].setAppointment_Date(dates[i]);
			}
		}
		
		return call;
	}
	
	
	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DAOConstants.DATABASE_NAME, null,DAOConstants.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DAOConstants.SERVICETABLE);
			db.execSQL(DAOConstants.FOREIGN_SERVICETABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d("Example","Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS "+ DAOConstants.SERVICE_TABLE_NAME);		
			db.execSQL("DROP TABLE IF EXISTS "+ DAOConstants.FOREIGN_SERVICE_TABLE_NAME);	
			onCreate(db);
		}
	}
}