package com.sportsboards2d.db.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;

import com.sportsboards2d.db.objects.Configuration;
import com.sportsboards2d.db.objects.Formation;
import com.sportsboards2d.db.objects.Player;
import com.sportsboards2d.db.objects.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class XMLWriter{
	
	public XMLWriter(){}
		
	public String convertFormations(final Context context, List<Formation>forms){
		
		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    
	    try {
			
	    	serializer.setOutput(writer);
	    	serializer.startDocument("UTF-8", true);
	    	serializer.startTag("", "forms");
	    	for(Formation fn: forms){
	
		    	serializer.startTag("", "form");
		    	serializer.startTag("", "name");
		    	serializer.text(fn.getName());
		    	serializer.endTag("", "name");
		    	serializer.startTag("", "ball");
		    	serializer.attribute("", "x", String.valueOf(fn.getBall().getX()));
		    	serializer.attribute("", "y", String.valueOf(fn.getBall().getY()));
		    	serializer.endTag("", "ball");
	
		    	for(Player pInfo:fn.getPlayers()){
			    	serializer.startTag("", "pEntry");

		    		serializer.startTag("", "pID");
		    		serializer.attribute("", "id", String.valueOf(pInfo.getpID()));
		    		serializer.endTag("", "pID");
		    		
		    		serializer.startTag("", "team");
		    		serializer.text(pInfo.getTeamColor());
		    		serializer.endTag("", "team");
		    		
		    		serializer.startTag("", "coords");
		    		serializer.attribute("", "x", String.valueOf(pInfo.getX()));
		    		serializer.attribute("", "y", String.valueOf(pInfo.getY()));
		    		serializer.endTag("", "coords");
		    		
		    		serializer.endTag("", "pEntry");
		    	}
		    	serializer.endTag("", "form");
	    	}
	    	serializer.endTag("", "forms");
	    	serializer.endDocument();
	    }
	    catch(IOException oshit){
	    	oshit.printStackTrace();
	    }
	    return writer.toString();   
	}
	
	public String convertPlayers(final Context context, List<PlayerInfo>players){
		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    
	    try {
			
	    	serializer.setOutput(writer);
	    	serializer.startDocument("UTF-8", true);
	    	serializer.startTag("", "players");
	    	for(PlayerInfo p: players){
	
		    	serializer.startTag("", "player");
		    	
		    	serializer.startTag("", "pID");
		    	serializer.attribute("", "id", String.valueOf(p.getpID()));
		    	serializer.endTag("", "pID");
		    	
		    	serializer.startTag("", "jNum");
		    	serializer.attribute("", "num", String.valueOf(p.getjNum()));
		    	serializer.endTag("", "jNum");
		    	
		    	serializer.startTag("", "type");
		    	serializer.text(p.getType());
		    	serializer.endTag("", "type");
		    	
		    	serializer.startTag("", "pName");
		    	serializer.text(p.getPName());
		    	serializer.endTag("", "pName");
		    	
		    	serializer.endTag("", "player");
	    	}
	    	serializer.endTag("", "players");
	    	serializer.endDocument();
	    }
	    catch(IOException oshit){
	    	oshit.printStackTrace();
	    }
	    return writer.toString();   
	}
	
	public String writeConfig(final Context context, Configuration config){
		
		XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    
	    String playerSize = "false";
	    String load = "false";
	    String line = "false";
	    
	    if(config.isLargePlayers()){
	    	playerSize = "true";
	    }
	    if(config.isLastLoaded()){
	    	load = "true";
	    }
	    if(config.isLineEnabled()){
	    	line = "true";
	    }
	    
	    try {			
	    	serializer.setOutput(writer);
	    	serializer.startDocument("UTF-8", true);
	    	
	    	serializer.startTag("", "config");
	    	
	    	serializer.startTag("", "player_size");
	    	serializer.text(playerSize);
	    	serializer.endTag("", "player_size");
	    	
	    	serializer.startTag("", "line_enabled");
	    	serializer.text(line);
	    	serializer.endTag("", "line_enabled");
	    	
	    	serializer.startTag("", "last_loaded");
	    	serializer.text(load);
	    	serializer.endTag("", "last_loaded");
	    	
	    	serializer.startTag("", "default_sport");
	    	serializer.text(config.getDefault_sport());
	    	serializer.endTag("", "default_sport");
	    	
	    	serializer.endTag("", "config");
	    	
	    	serializer.endDocument();
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
        return writer.toString();
	}
	
	public String convertStreamToString(InputStream input) throws IOException{
		
		if(input != null){
			Writer writer = new StringWriter();
			
			char[]buffer = new char[1024];
			
			try{
				Reader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
				int n;
				while((n = reader.read(buffer)) != -1){
					writer.write(buffer, 0, n);
				}
			}finally{
				input.close();
			}
			return writer.toString();
		}
		else{
			return "";
		}
	}
	
}