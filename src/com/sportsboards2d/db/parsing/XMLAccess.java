package com.sportsboards2d.db.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.sportsboards2d.db.objects.Configuration;
import com.sportsboards2d.db.objects.Formation;
import com.sportsboards2d.db.objects.FormationEntry;
import com.sportsboards2d.db.objects.Player;
import com.sportsboards2d.db.objects.PlayerEntry;
import com.sportsboards2d.db.objects.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class XMLAccess{
	
	public static OutputStream op;

	public static void writeFormations(final Context context, List<Formation>forms, String path){
		
		XMLWriter writer = new XMLWriter();
		String output;
		FileOutputStream fOut;
		
		try {
			fOut = context.openFileOutput(path + "forms", Context.MODE_PRIVATE);
			output = writer.convertFormations(context, forms);
			System.out.println(output);
			fOut.write(output.getBytes());
			fOut.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static void writePlayers(final Context context, List<PlayerInfo>players, String path){
		
		XMLWriter writer = new XMLWriter();
		String output;
		FileOutputStream fOut;
		
		try {
			fOut = context.openFileOutput(path + "players", Context.MODE_PRIVATE);
			output = writer.convertPlayers(context, players);
			System.out.println(output);
			fOut.write(output.getBytes());
			fOut.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static List<Formation> loadFormations(final Context context, String path){
		
		List<Formation> forms = null;
		List<FormationEntry> formEntries = null;
		List<PlayerInfo> players = null;
		InputStream input = null;	
		XMLReader parser = new XMLReader();
		
		try{
			input = new FileInputStream(context.getFilesDir() + "/" + path + "forms");
			System.out.println(context.getFilesDir() + "/" + path + "forms");
			formEntries = parser.parseFormation(input);
			input.close();
			input = new FileInputStream(context.getFilesDir() + "/" + path + "players");
			players = parser.parsePlayers(input);
			input.close();
			matchPlayers(formEntries, players);
		}
		catch(IOException oshit){
			oshit.printStackTrace();
			System.out.println("internal formation not found");
			input = null;
		}
		if(input == null){
			
			try {
				
				input = context.getAssets().open("database/" + path + "/formations.xml");
				formEntries = parser.parseFormation(input);
				input.close();
				input = context.getAssets().open("database/" + path + "/players.xml");
				players = parser.parsePlayers(input);
				input.close();
				forms = matchPlayers(formEntries, players);
								
				writeFormations(context, forms, path);
				writePlayers(context, players, path);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return forms;
	}
	
	public static void writeConfig(final Context context, Configuration config, String path){
		
		XMLWriter writer = new XMLWriter();
		String output;
		FileOutputStream fOut;
		
		try {
			fOut = context.openFileOutput(path, Context.MODE_PRIVATE);
			output = writer.writeConfig(context, config);
			fOut.write(output.getBytes());
			fOut.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Configuration readConfig(final Context context, String path){
		
		XMLReader parser = new XMLReader();
		InputStream input = null;
		Configuration result = null;
		
		try{
			
			input = new FileInputStream(context.getFilesDir() + "/" + path);
			result = parser.parseConfig(input);
			input.close();		
		}
		catch(IOException oshit){
			input = null;
			System.out.println("internal config not found");
		}
		if(input == null){
			
			try{
				
				input = context.getAssets().open("database/config.xml");
				result = parser.parseConfig(input);
				input.close();
				
				writeConfig(context, result, path);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static List<PlayerInfo> readPlayers(final Context context, String path){
		
		return null;
	}
	
	private static List<Formation> matchPlayers(List<FormationEntry> formEntries, List<PlayerInfo> players){
		
		List<Formation> forms = new ArrayList<Formation>();
		Player newPlayer = null;
		List<Player> matchList = new ArrayList<Player>();
		Formation fn = null;
		int pID;
		
		for(FormationEntry fEntry:formEntries){
			
			for(PlayerEntry pEntry:fEntry.getPlayers()){
				
				pID = pEntry.getpID();
				
				for(PlayerInfo playerInfo:players){
					if(pID == playerInfo.getpID()){
						newPlayer = new Player(pEntry.getCoords().getX(), pEntry.getCoords().getY(), pEntry.getpTeam(), 
								playerInfo.getpID(), playerInfo.getjNum(), playerInfo.getType(), playerInfo.getPName());
						matchList.add(newPlayer);
						break;
					}
				}
				
			}
			fn = new Formation(fEntry.getfName(), fEntry.getBall(), matchList);
			forms.add(fn);
		}
		
		
		
		
		return forms;
	}
	
}