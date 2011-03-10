package com.sportsboards2d.db.parsing;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

import com.sportsboards2d.db.Configuration;
import com.sportsboards2d.db.Formation;
import com.sportsboards2d.db.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class XMLReader extends BaseFeedParser{
	
	public XMLReader(){}
	
	public ArrayList<Formation> parseFormation(InputStream input){
		
		ArrayList<Formation> forms = null;
		Formation newForm = null;
		
		ArrayList<PlayerInfo> players = null;
		PlayerInfo newPlayer = null;
		
		float x = 0;
		float y = 0;
		
		XmlPullParser parser = Xml.newPullParser();
		
		try {
            // auto-detect the encoding from the stream
            parser.setInput(input, null);
            int eventType = parser.getEventType();
            newForm = null;
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        forms = new ArrayList<Formation>();
                        break;
                    case XmlPullParser.START_TAG:
                    	
                        name = parser.getName();
                        
                        if (name.equalsIgnoreCase(FORM)){
                            newForm = new Formation();
                            players = null;
                        } 
                        else if (newForm != null){
                        	
                            if (name.equalsIgnoreCase(NAME)){

                            	newForm.setName(parser.nextText());
                            	
                            } else if (name.equalsIgnoreCase(BALL)){                           	
                            	x = Float.parseFloat(parser.getAttributeValue(0));
                            	y = Float.parseFloat(parser.getAttributeValue(1));
                            	newForm.setBall(x, y);
                            	
                            } else if (name.equalsIgnoreCase(PLAYER)){
                            	
                            	if(players == null){
                            		players = new ArrayList<PlayerInfo>();
                            	}
                            	newPlayer = new PlayerInfo();
                            	
                            } else if (name.equalsIgnoreCase(TEAM)){
                            	
                            	newPlayer.setTeamColor(parser.nextText());
                            
                        	} else if (name.equalsIgnoreCase(TYPE)){
                        		
                        		newPlayer.setType(parser.nextText());
                        	
                        	} else if (name.equalsIgnoreCase(PNAME)){
                    		
                        		newPlayer.setPlayerName(parser.nextText());
                        		
               				} else if (name.equalsIgnoreCase(COORDS)){
                            	x = Float.parseFloat(parser.getAttributeValue(0));
                            	y = Float.parseFloat(parser.getAttributeValue(1));
                            	newPlayer.setCoords(x,y);
               				}
                        }
                        break;
                        
                    case XmlPullParser.END_TAG:
                    	
                        name = parser.getName();
                        
                        if (name.equalsIgnoreCase(FORM) && newForm != null){
                        	newForm.setPlayers(players);
                            forms.add(newForm);
                        }
                        else if (name.equalsIgnoreCase(PLAYER)){

                        	players.add(newPlayer); 	
                        }
                        
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            System.out.println("exception");
        }	
		return forms;
	}

	/* (non-Javadoc)
	 * @see com.sportsboards2d.db.parsing.FeedParser#parseConfig(java.io.InputStream)
	 */
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
}