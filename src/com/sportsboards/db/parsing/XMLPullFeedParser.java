package com.sportsboards.db.parsing;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import com.sportsboards.db.Formation;
import com.sportsboards.db.PlayerInfo;

/**
 * Coded by Nathan King
 */

public class XMLPullFeedParser extends BaseFeedParser{
	
	public XMLPullFeedParser(){}
	
	public ArrayList<Formation> parse(InputStream input){
		
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
                        
                        break;
                    case XmlPullParser.START_TAG:
                    	
                        name = parser.getName();
                        
                        if(name.equalsIgnoreCase(TAG_ROOT)){
                        	forms = new ArrayList<Formation>();
                        }
                        
                        else if (name.equalsIgnoreCase(TAG_FORM)){
                            newForm = new Formation();
                            players = null;
                        } 
                        else if (newForm != null){
                        	
                            if (name.equalsIgnoreCase(TAG_FORM_NAME)){

                            	newForm.setName(parser.nextText());
                            	
                            } else if (name.equalsIgnoreCase(TAG_FORM_BALL)){   
                            
                            	x = Float.parseFloat(parser.getAttributeValue(null, TAG_FORM_BALL_ATTRIBUTE_X));
                            	y = Float.parseFloat(parser.getAttributeValue(null, TAG_FORM_BALL_ATTRIBUTE_Y));
                            	newForm.setBall(x, y);
                            	
                            } else if (name.equalsIgnoreCase(TAG_PLAYER)){
                            	
                            	if(players == null){
                            		players = new ArrayList<PlayerInfo>();
                            	}
                            	newPlayer = new PlayerInfo();
                            	
                            } else if (name.equalsIgnoreCase(TAG_PLAYER_TEAM)){
                            	
                            	newPlayer.setTeamColor(parser.nextText());
                            
                        	} else if (name.equalsIgnoreCase(TAG_PLAYER_TYPE)){
                        		
                        		newPlayer.setType(parser.nextText());
                        	
                        	} else if (name.equalsIgnoreCase(TAG_PLAYER_PNAME)){
                    		
                        		newPlayer.setPlayerName(parser.nextText());
                        		
               				} else if (name.equalsIgnoreCase(TAG_PLAYER_COORDS)){
                            	x = Float.parseFloat(parser.getAttributeValue(null, TAG_PLAYER_ATTRIBUTE_X));
                            	y = Float.parseFloat(parser.getAttributeValue(null, TAG_PLAYER_ATTRIBUTE_Y));
                            	newPlayer.setCoords(x,y);
                       
               				}
                        }
                        break;
                        
                    case XmlPullParser.END_TAG:
                    	
                        name = parser.getName();
                        
                        if(name.equalsIgnoreCase(TAG_ROOT)){
                        	done = true;
                        }
                        
                        else if (name.equalsIgnoreCase(TAG_FORM) && newForm != null){
                        	newForm.setPlayers(players);
                        	System.out.println("end form");
                            forms.add(newForm);
                        }
                        else if (name.equalsIgnoreCase(TAG_PLAYER)){

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
}