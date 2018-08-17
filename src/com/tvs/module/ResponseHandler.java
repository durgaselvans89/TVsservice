package com.tvs.module;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.tvs.dto.CallList;
import com.tvs.dto.CallListDTO;
import com.tvs.dto.LoginDTO;
import com.tvs.dto.MasterDto;

public class ResponseHandler {
	
	public LoginDTO handleLoginResponse(String responseString) throws CustomException {
		LoginDTO dto = new LoginDTO();

		XmlParser parser = null;
		String erroCode = null;
		try {
			parser = new XmlParser(responseString);
			
			try{
				erroCode = parser.getNodeValue(Constants.ERROR_CODE);
			}catch (Exception e) {
				// TODO: handle exception
			}
			if(null != erroCode && erroCode.length()>0){
				throw new CustomException(parser.getNodeValue(Constants.RESPONSE_CODE),Constants.ERR_SEND);
			} else {
				NodeList nodeList = parser.getNodeList("USER");
				Element element = (Element)nodeList.item(0);
				dto.setUsername(parser.getNodeValue(element, Constants.USER_NAME));
				dto.setEngineerId(parser.getNodeValue(element,Constants.ENG_ID));
				dto.setEngineerName(parser.getNodeValue(element,Constants.ENG_NAME));
				dto.setServerDateTime(parser.getNodeValue(element,Constants.SERVERDATE));
			}
		} catch (Exception e) {
			if(null == erroCode){
				throw new CustomException(Constants.ERR_PARSER,Constants.ERR_COMMON);
			} else throw new CustomException(erroCode,Constants.ERR_SEND);
		}finally {
			if(null != parser)
				parser.deInitialize();
		}
		return dto;
	}
	
	public CallList[] handleCallListResponse(String responseString) throws CustomException {
		CallList[] callList = null;

		XmlParser parser = null;
		String erroCode = null;
		try {
			parser = new XmlParser(responseString);
			
			try{
				erroCode = parser.getNodeValue(Constants.ERROR_CODE);
			}catch (Exception e) {
				// TODO: handle exception
			}
			if(null != erroCode && erroCode.length()>0){
				throw new CustomException(erroCode,Constants.ERR_SEND);
			} else {
				NodeList nodeList = parser.getNodeList("DATEWITHNUMBEROFCALLS");
				int count = nodeList.getLength();
				CallListDTO[] dto = null;
				NodeList childNode = null;
				NodeList patList = null;
				int partCount = 0;
				int childCount = 0;
				Element element = null;
				Element parentElement = null;
				if(count>0){
					callList = new CallList[count];
					for(int i=0;i<count;i++){
						parentElement = (Element)nodeList.item(i);
						callList[i] = new CallList();
						callList[i].setAppointment_Date(parser.getNodeValue(parentElement, Constants.APPOINTMENTDATETIME));
						callList[i].setNumberOf_Calls(parser.getNodeValue(parentElement, Constants.NOOFCALLS));
						childNode = parentElement.getElementsByTagName("CALLLIST");
						dto = null;
						if(null != childNode && (childCount=childNode.getLength())>0){
							dto = new CallListDTO[childCount];
							for(int j=0;j<childCount;j++){
								element = (Element)childNode.item(j);
								dto[j] = new CallListDTO();
								dto[j].setConstact_Number(getvalues(parser,element, Constants.CONTACTNO));
								dto[j].setOrgination_Name(getvalues(parser,element,Constants.ORGANIZATIONNAME));
								dto[j].setCustomer_Name(getvalues(parser,element,Constants.CUSTNAME));
								dto[j].setAlterNative_Contact_Number(getvalues(parser,element,Constants.ALTCONTACTNO));
								dto[j].setTechnician_Id(getvalues(parser,element,Constants.TECHID));
								dto[j].setLong_Description(getvalues(parser,element,Constants.LONGDESCRIPTION));
								dto[j].setProduct_Code(getvalues(parser,element,Constants.PRODUCTCODE));
								dto[j].setSummery(getvalues(parser,element,Constants.SUMMARY));
								dto[j].setDsp_Comments(getvalues(parser,element,Constants.DSPCOMMENTS));
								dto[j].setSla(getvalues(parser,element,Constants.SLA));
								dto[j].setsTag(getvalues(parser,element,Constants.ASSET));
								dto[j].setTech_Support_Name(getvalues(parser,element,Constants.TECHSUPPORTNAME));
								dto[j].setDispatch_Time(getvalues(parser,element,Constants.DISPATCHDATETIME));
								dto[j].setPart_ETA_Time(getvalues(parser,element,Constants.PARTETADATETIME));
								dto[j].setCustomer_Address(getvalues(parser,element,Constants.CUSTADD));
								dto[j].setSerial_Number(getvalues(parser,element,Constants.SERIALNUMBER));
								dto[j].setEngineer_Name(getvalues(parser,element,Constants.ENG_NAME));
								dto[j].setCustomer_Date_Time(getvalues(parser,element, Constants.CUSTOMERETADATETIME));
								dto[j].setParts_Number(getvalues(parser,element, Constants.PARTNO));
								dto[j].setFlag_rc17(getvalues(parser,element, Constants.RC17_FlAG));
							}
						}
						
						childNode = parentElement.getElementsByTagName("PartTransaction");
						
						if(null != childNode && (childCount=childNode.getLength())>0){
							for(int j=0;j<childCount;j++){
								element = (Element)childNode.item(j);
								dto[j].setPartCount(parser.getNodeValue(element, Constants.PARTCOUNT));
								dto[j].setPartIssued(parser.getNodeValue(element, Constants.PARTSTATUS));
								dto[j].setNoPart(getvalues(parser,element, Constants.ISPARTCALL));
								patList = element.getElementsByTagName("Parts");
								if(null != patList && (partCount=patList.getLength())>0){
									String[] partNumber = new String[partCount];
									String[] uniqueID = new String[partCount];
									for(int k=0;k<partCount;k++){
										element = (Element)patList.item(k);
										partNumber[k] = getvalues(parser,element, Constants.PARTSNO);
										uniqueID[k] = getvalues(parser,element,Constants.UNIQUE_ID);
									}
									dto[j].setPartNumber(partNumber);
									dto[j].setUniqueId(uniqueID);
								}
							}
						}
						
						callList[i].setCallListDto(dto);
					}
				} /*else {
					callList = getTempValue();
				}*/
			}
		} catch (Exception e) {
			callList = null;
			if(null == erroCode){
				throw new CustomException(Constants.ERR_PARSER,Constants.ERR_COMMON);
			} else 	throw new CustomException(erroCode,Constants.ERR_SEND);
		}finally {
			if(null != parser)
				parser.deInitialize();
		}
		return callList;
	}
	
	private CallList[] getTempValue(){
		int count = 1;
		CallList[] callList = new CallList[count];
			for(int i=0;i<count;i++){
				callList[i] = new CallList();
				callList[i].setAppointment_Date("sasi");
				callList[i].setNumberOf_Calls("sasi");
				int childCount = 1;
				CallListDTO[] dto = null;
				if((childCount)>0){
					dto = new CallListDTO[childCount];
					for(int j=0;j<childCount;j++){
						dto[j] = new CallListDTO();
						dto[j].setConstact_Number("sasi");
						dto[j].setOrgination_Name("sasi");
						dto[j].setCustomer_Name("sasi");
						dto[j].setAlterNative_Contact_Number("sasi");
						dto[j].setTechnician_Id("sasi");
						dto[j].setLong_Description("sasi");
						dto[j].setProduct_Code("sasi");
						dto[j].setSummery("sasi");
						dto[j].setDsp_Comments("sasi");
						dto[j].setSla("sasi");
						dto[j].setsTag("sasi");
						dto[j].setTech_Support_Name("sasi");
						dto[j].setDispatch_Time("sasi");
						dto[j].setPart_ETA_Time("sasi");
						dto[j].setCustomer_Address("sasi");
						dto[j].setSerial_Number("sasi");
						dto[j].setEngineer_Name("sasi");
						dto[j].setCustomer_Date_Time("26/06/2011 11:30:20");
						//dto[j].setParts_Number("sasi");
						dto[j].setFlag_rc17("Yes");
					}
				}
				callList[i].setCallListDto(dto);
			}
			return callList;
	}
	
	private String getvalues(XmlParser parser, Element element,String tagName){
		String value ="";
		try{
			value = parser.getNodeValue(element,tagName);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return value;
	}

	public MasterDto handleMasterResponse(String responseString) throws CustomException {
		MasterDto masterDto = null;
		XmlParser parser = null;
		String erroCode = null;
		try {
			parser = new XmlParser(responseString);
			
			try{
				erroCode = parser.getNodeValue(Constants.ERROR_CODE);
			}catch (Exception e) {
				// TODO: handle exception
			}
			if(null != erroCode && erroCode.length()>0){
				throw new CustomException(erroCode,Constants.ERR_SEND);
			} else {
				masterDto = new MasterDto();
				NodeList nodeList = parser.getNodeList("PARTLIST");
				int count = 0;
				Element element = (Element)nodeList.item(0);
				count = (element.getChildNodes()).getLength();
				
				if(count>0){
					String[] parts = new String[count];
					for(int i=0;i<count;i++){
						parts[i] = parser.getNodeValue(element, Constants.PART+(i+1));	
					}
					masterDto.setParts(parts);
				}
				//
				
				//
				nodeList = parser.getNodeList("REASON");
				element = (Element)nodeList.item(0);
				count = (element.getChildNodes()).getLength();
				if(count>0){
					String[] reason = new String[count];
					for(int i=0;i<count;i++){
						reason[i] = parser.getNodeValue(element, Constants.REASON+(i+1));
					}
					masterDto.setReason(reason);
				}
				
				nodeList = parser.getNodeList("ACTIVITY_DONE");
				element = (Element)nodeList.item(0);
				count = (element.getChildNodes()).getLength();
				if(count>0){
					String[] activity = new String[count];
					for(int i=0;i<count;i++){
						activity[i] = parser.getNodeValue(element, Constants.ACTIVITY_DONE+(i+1));
					}
					masterDto.setActivity_done(activity);
				}
				
				
				nodeList = parser.getNodeList("CALLRESULT");
				element = (Element)nodeList.item(0);
				count = (element.getChildNodes()).getLength();
				if(count>0){
					String[] result = new String[count];
					for(int i=0;i<count;i++){
						result[i]= parser.getNodeValue(element, Constants.RESULT+(i+1));
					}
					masterDto.setResults(result);
				}
				
				nodeList = parser.getNodeList("ADDITIONALACTIVITY");
				element = (Element)nodeList.item(0);
				count = (element.getChildNodes()).getLength();
				if(count>0){
					String[] additionalActivity = new String[count];
					for(int i=0;i<count;i++){
						additionalActivity[i]= parser.getNodeValue(element, Constants.ADDITIONAL_ACTIVITY+(i+1));
					}
					masterDto.setAdditional_Activity(additionalActivity);
				}

				nodeList = parser.getNodeList("PARTUSAGE");
				element = (Element)nodeList.item(0);
				nodeList = element.getChildNodes();
				count = nodeList.getLength();
				if(count>0){
					String[] partUsage = new String[count];
					String[] partUsage_Id = new String[count];
					for(int i=0;i<count;i++){
						element = (Element)nodeList.item(i);
						partUsage[i] = getvalues(parser,element, "USAGE").trim();
						partUsage_Id[i] = getvalues(parser,element, "ID").trim();
					}
					masterDto.setPart_Usage(partUsage);
					masterDto.setPart_Usage_Id(partUsage_Id);
				}
			}
		} catch (Exception e) {
			if(null == erroCode){
				throw new CustomException(Constants.ERR_PARSER,Constants.ERR_COMMON);
			}else throw new CustomException(erroCode,Constants.ERR_SEND);
		}finally {
			if(null != parser)
				parser.deInitialize();
		}
		return masterDto;
	}

	public boolean handleRCResponse(String responseString) throws CustomException {
		boolean isSuccess = false;

		XmlParser parser = null;
		String erroCode = null;
		try {
			parser = new XmlParser(responseString);
			try{
				erroCode = parser.getNodeValue(Constants.ERROR_CODE);
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			if(null != erroCode && erroCode.length()>0){
				throw new CustomException(erroCode,Constants.ERR_SEND);
			} else {
				erroCode = parser.getNodeValue(Constants.RESPONSE_CODE);
				Constants.statusString = erroCode;
				if(erroCode.indexOf(Constants.NOTSUBMITTED)>-1 || erroCode.indexOf(Constants.NOT_SUBMITTED)>-1){
					isSuccess = false;
				} else isSuccess = true;
			}
		} catch (Exception e) {
			if(null == erroCode){
				throw new CustomException(Constants.ERR_PARSER,Constants.ERR_COMMON);
			}  else throw new CustomException(erroCode,Constants.ERR_SEND);
			
		}finally {
			if(null != parser)
				parser.deInitialize();
		}
		return isSuccess;
	}
	
	
}
