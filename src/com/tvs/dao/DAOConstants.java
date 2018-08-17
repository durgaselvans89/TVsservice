package com.tvs.dao;


public class DAOConstants {

	public static final String DATABASE_NAME = "tvs.db";
	public static final int DATABASE_VERSION = 3;
	
	public static final String SERVICE_TABLE_NAME = "service_table";
	public static final String FOREIGN_SERVICE_TABLE_NAME = "foreign_service_tables";
	public static final String PARTS_TABLE_NAME = "parts_table";
	public static final String RESULT_TABLE_NAME = "result_table";	

		
	public static final String SERVICETABLE =  "CREATE TABLE IF NOT EXISTS service_table (SER varchar(30) not null,SERVICEDATE varchar(30),ENGINEERNAME varchar(40),TECHID varchar(30),SLA varchar(100),PRODUCTCODE varchar(30),PARTNO varchar(30),TECHSUPPORTNAME varchar(30),DISPATCHDATETIME varchar(30),ORGANIZATIONNAME varchar(70),CONTACTNAME varchar(30),CONTACTNO varchar(30),ALTCONTACTNO varchar(30),ADDRESS varchar(200),SUMMARY varchar(1000),LONGDESCRIPTION varchar(3000),STATUS varchar(10), CUSTOMERETADATETIME varchar(30), PARTNUMBER varchar(2000), UniqueId varchar(2000), PartCount varchar(10), PartStatus varchar(15), NOPART varchar(5))";	
	public static final String GET_CALL_LIST = "select * from service_table";
	public static final String GET_SEND_DATA = "select * from foreign_service_tables";
	public static final String FOREIGN_SERVICETABLE = "CREATE TABLE IF NOT EXISTS foreign_service_tables(IMAGE_BYTE blob,URL varchar(6000) not null)";	
}
