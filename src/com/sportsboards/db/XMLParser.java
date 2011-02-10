package com.sportsboards.db;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler{
	
	private boolean currentElement = false;
	private String currentValue = null;
	private Formation newForm = null;
	private Position newPosition = null;
	
	public static List<Formation> forms = null;
	public List<Position> positions = null;
	
	public static List<Formation> getFormsList(){
		return forms;
	}
	public static void setFormsList(List<Formation> list){
		XMLParser.forms = list;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException{
		
		currentElement = true;
		int x = 0;
		int y = 0;
		
		if(localName.equals("forms")){
			forms = new ArrayList<Formation>();
		}
		else if(localName.equals("form")){
			newForm = new Formation();
		}
		else if(localName.equals("positions")){
			positions = new ArrayList<Position>();
		}
		else if(localName.equals("pos")){
			newPosition = new Position();
		}
		else if(localName.equals("ball")){
			x = SAXUtils.getIntAttributeOrThrow(attrs, "x");
			y = SAXUtils.getIntAttributeOrThrow(attrs, "y");
			newForm.setBall(x, y);
		}
		else if(localName.equals("coords")){
			x = SAXUtils.getIntAttributeOrThrow(attrs, "x");
			y = SAXUtils.getIntAttributeOrThrow(attrs, "y");
			newPosition.setCoords(x, y);
		}
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
		currentElement = false;
		
		if(localName.equalsIgnoreCase("form")){
			forms.add(newForm);
		}
		else if(localName.equals("name")){
			newForm.setName(currentValue);
		}
		else if(localName.equals("team")){
			newPosition.setTeamColor(currentValue);
		}
		else if(localName.equals("type")){
			newPosition.setType(currentValue);
		}
		else if(localName.equals("pName")){
			newPosition.setPlayerName(currentValue);
		}
		else if(localName.equals("pos")){
			positions.add(newPosition);
		}
		else if(localName.equals("positions")){
			newForm.setPositions(positions);	
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length){
	
		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}
	}
	
}