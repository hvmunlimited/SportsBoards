package com.sportsboards.db;

import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler{
	
	private boolean currentElement = false;
	private String currentValue = null;
	
	public static FormsList forms = null;
	
	public static FormsList getFormsList(){
		return forms;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException{
		
		currentElement = true;
		
		if(localName.equals("forms")){
			
		}
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length){
	
	}
	
}