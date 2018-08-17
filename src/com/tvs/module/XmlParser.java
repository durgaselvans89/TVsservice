package com.tvs.module;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Class to handle response data
 * 
 * @author Hakuna Matata
 * @version 1.0
 */
public class XmlParser {

	Document doc = null;
	
	/**
	 * Constructor method
	 * 
	 * @param responseString
	 * @throws Exception when the given string is not a valid xml
	 */
	public XmlParser(String responseString) throws Exception {
		initialize(responseString);
	}
	
	/**
	 * Method to get the xml document for the given string
	 * 
	 * @param xmlRecords
	 * @return
	 * @throws Exception
	 */
	private Document initialize(String xmlRecords) throws Exception {
		try {
//			xmlRecords = xmlRecords.replaceAll("&lt;","<");
//			xmlRecords = xmlRecords.replaceAll("&gt;",">");
//			xmlRecords = xmlRecords.replaceAll("&quot;","\"");
//			xmlRecords = xmlRecords.replaceAll("&#039;", "\'");
//			xmlRecords = xmlRecords.replaceAll("&amp;","&");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlRecords));
			doc = db.parse(is);
		} catch (Exception exception) {
			throw exception;
		}
		return doc;
	}

	/**
	 * 
	 * @param tagname
	 * @return
	 */
	public String getNodeValue(String tagname) {
		String value = null;
		
		NodeList nodes = doc.getElementsByTagName(tagname);
		int len = nodes.getLength();
		
		if(null != nodes && len > 0) {
			value = nodes.item(0).getFirstChild().getNodeValue();
		}
		return value;
	}
	
	/**
	 * 
	 * @param tagname
	 * @return
	 */
	public String getNodeValue(Element element, String tagname) {
		String value = null;
		
		NodeList nodes = element.getElementsByTagName(tagname);
		int len = nodes.getLength();
		NodeList childNode = null;
		int count = 0;
		if(null != nodes && len > 0) {
			childNode = nodes.item(0).getChildNodes();
			count = childNode.getLength();
			for(int j=0;j<count;j++){
				if(null == value)
					value = "";
				value += childNode.item(j).getNodeValue();
			}
			//value = nodes.item(0).getFirstChild().getNodeValue();
		}
		return value;
	}
	
	/**
	 * 
	 * @param tagname
	 * @return
	 */
	public int getIntegerValue(Element element, String tagname) {
		int value = 0;
		
		NodeList nodes = element.getElementsByTagName(tagname);
		int len = nodes.getLength();
		
		if(null != nodes && len > 0) {
			value = Integer.parseInt(nodes.item(0).getFirstChild().getNodeValue());
		}
		return value;
	}
	
	/**
	 * 
	 * @param tagname
	 * @return
	 */
	public char getCharValue(Element element, String tagname) {
		//char value = 0;
		
		NodeList nodes = element.getElementsByTagName(tagname);
		int len = nodes.getLength();
		
		if(null != nodes && len > 0) {
			String temp = nodes.item(0).getFirstChild().getNodeValue();
			return temp.charAt(0);
		}
		return 0;
	}
	
	/**
	 * Method to get the node list
	 * 
	 * @param tagname
	 * @return
	 */
	public NodeList getNodeList(String tagname) {
		NodeList nodes = doc.getElementsByTagName(tagname);
		return nodes;
	}
	
	/**
	 * 
	 * @param tagname
	 * @return
	 */
	public int getIntegerValue(String tagname) throws NumberFormatException{
		int value = 0;
		
		NodeList nodes = doc.getElementsByTagName(tagname);
		int len = nodes.getLength();
		
		if(null != nodes && len > 0) {
			value = Integer.parseInt(nodes.item(0).getFirstChild().getNodeValue());
		}
		
		return value;
	}
	
	/**
	 * 
	 * @param tagname
	 * @return
	 */
	public char getCharValue(String tagname) throws NumberFormatException{
		String value = null;
		
		NodeList nodes = doc.getElementsByTagName(tagname);
		int len = nodes.getLength();
		
		if(null != nodes && len > 0) {
			value = nodes.item(0).getFirstChild().getNodeValue();
			return value.charAt(0);
		}
		
		return 0;
	}
	
	/**
	 * 
	 * @param tagname
	 * @return
	 */
	public boolean isNodePresent(String tagname){
		
		NodeList nodes = doc.getElementsByTagName(tagname);
		int len = nodes.getLength();
		
		if(null != nodes && len > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method to deinitialize
	 */
	public void deInitialize()
	{
		doc = null;
	}
	/**
	 * Method to get the character data from element
	 * 
	 * @param e
	 * @return
	 */
	/*private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}*/
}
