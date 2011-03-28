package com.sportsboards2d.db.parsing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.sportsboards2d.db.objects.Configuration;
import com.sportsboards2d.db.objects.Coordinates;
import com.sportsboards2d.db.objects.FormationEntry;
import com.sportsboards2d.db.objects.PlayerEntry;
import com.sportsboards2d.db.objects.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class XMLReader extends BaseFeedParser{
	
	public XMLReader(){}
	
	public List<FormationEntry> parseFormation(InputStream input){

		List<FormationEntry> entries = null;
		List<PlayerEntry> players = null;
		
		PlayerEntry pEntry;
		FormationEntry fEntry;
		
		String formName = "", pTeam = "";
		int pID = 0;
		float xBall = 0.0f, yBall = 0.0f, xPlayer = 0.0f, yPlayer = 0.0f;
		
		XmlPullParser parser = Xml.newPullParser();
		
		try {
            // auto-detect the encoding from the stream
            parser.setInput(input, null);
            
            int eventType = parser.getEventType();

            boolean done = false;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	
                        name = parser.getName();
                        
                        if (name.equalsIgnoreCase(FORMS)){
                            entries = new ArrayList<FormationEntry>();
                        } 
                        else if (name.equalsIgnoreCase(FORM)){
                        	players = new ArrayList<PlayerEntry>();
                        }
                        else if (name.equalsIgnoreCase(FNAME)){
                        	formName = parser.getText();
                        }
                        else if (name.equalsIgnoreCase(BALL)){                           	
                        	xBall = Float.parseFloat(parser.getAttributeValue(0));
                        	yBall = Float.parseFloat(parser.getAttributeValue(1));
                        }
                        
                        else if (name.equalsIgnoreCase(PID)){
                        	pID = Integer.parseInt(parser.getText());
                        }
                        else if (name.equalsIgnoreCase(PTEAM)){
                        	pTeam = parser.getText();
                        }
                        else if (name.equalsIgnoreCase(PCOORDS)){
                        	xPlayer = Float.parseFloat(parser.getAttributeValue(0));
                        	yPlayer = Float.parseFloat(parser.getAttributeValue(1));
                        }
                        break;
                        	
                        
                    case XmlPullParser.END_TAG:
                    	
                        name = parser.getName();
                        
                        if (name.equalsIgnoreCase(FORM)){
                        	
                        	fEntry = new FormationEntry(formName, new Coordinates(xBall, yBall), players);
                        	entries.add(fEntry);

                        }
                        else if (name.equalsIgnoreCase(PENTRY)){
                        	pEntry = new PlayerEntry(pID, pTeam, new Coordinates(xPlayer, yPlayer));
                        	players.add(pEntry);
                        }
                        
                        break;
                        
                }
                
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }	
		return entries;
	}
	
	@Override
	public Configuration parseConfig(InputStream input) {
		
		Configuration config = null;
		String temp = null;
		XmlPullParser parser = Xml.newPullParser();
		
		try {
            // auto-detect the encoding from the stream
            parser.setInput(input, null);
            int eventType = parser.getEventType();
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	
                        name = parser.getName();
                        
                        if(name.equalsIgnoreCase(CONFIG)){
                        	config = new Configuration();
                        }
                        else if(name.equalsIgnoreCase(PLAYER_SIZE)){
                        	temp = parser.nextText();
                        	if(temp.equalsIgnoreCase("true")){
                        		config.setLargePlayers(true);
                        	}
                        	else{
                        		config.setLargePlayers(false);
                        	}
                        }
                        else if(name.equalsIgnoreCase(DEFAULT)){
                        	config.setDefault_sport(parser.nextText());
                        }
                        else if(name.equalsIgnoreCase(LINE_ENABLED)){

                        	temp = parser.nextText();
                        	if(temp.equalsIgnoreCase("true")){
                        		config.setLineEnabled(true);

                        	}
                        	else{
                        		config.setLineEnabled(false);
                        	}
                        }
                        else if(name.equalsIgnoreCase(LOAD_LAST)){
                        	temp = parser.nextText();
                        	if(temp.equalsIgnoreCase("true")){
                        		config.setLastLoaded(true);
                        	}
                        	else{
                        		config.setLastLoaded(false);    
                        	}
                        }
                        
                        break;
                        
                    case XmlPullParser.END_TAG:
                    
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            System.out.println("exception");
        }
        return config;
	}
	
	public ArrayList<PlayerInfo> parsePlayers(InputStream input){
		
		ArrayList<PlayerInfo> pList = new ArrayList<PlayerInfo>();
		
		XmlPullParser parser = Xml.newPullParser();
		PlayerInfo newPlayer = null;
		
		int id = 0, num = 0;
		String type = null, pName = null;
		
		try {
            // auto-detect the encoding from the stream
            parser.setInput(input, null);
            
            int eventType = parser.getEventType();
            
            boolean done = false;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                
                    case XmlPullParser.START_DOCUMENT:
                    	
                        break;
                    case XmlPullParser.START_TAG:
                    	
                        name = parser.getName();
                        
                        if (name.equalsIgnoreCase(PLAYERS)){
                        	pList = new ArrayList<PlayerInfo>();
                        }
                        else if(name.equalsIgnoreCase(PID)){
                        	
                        	id = Integer.parseInt(parser.getText());
                        }
                        else if(name.equalsIgnoreCase(JNUM)){

                        	num = Integer.parseInt(parser.getText());
                        }
                        else if(name.equalsIgnoreCase(TYPE)){

                        	type = parser.getText();
                        }
                        else if(name.equalsIgnoreCase(PNAME)){
                        	
                        	pName = parser.getText();
                        }
                        
                        break;
                        
                    case XmlPullParser.END_TAG:
                    	
                        name = parser.getName();
                        
                        if(name.equalsIgnoreCase(PLAYER)){

                            newPlayer = new PlayerInfo(id, num, type, pName);
                            pList.add(newPlayer);
                        }
                    
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            System.out.println("exception");
        }
		
		
		return pList;
	}
}