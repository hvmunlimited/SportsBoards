package com.sportsboards2d.db.parsing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;

import com.sportsboards2d.R;
import com.sportsboards2d.db.Configuration;
import com.sportsboards2d.db.Formation;

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
			fOut = context.openFileOutput(path, Context.MODE_PRIVATE);
			output = writer.convertFormations(context, forms);
			fOut.write(output.getBytes());
			fOut.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static List<Formation> loadFormations(final Context context, String path, int resID){
		
		List<Formation> forms = null;
		InputStream input;		
		XMLReader parser = new XMLReader();
		
		try{
			input = new FileInputStream(context.getFilesDir() + "/" + path);
			forms = parser.parseFormation(input);
			input.close();
		}
		catch(IOException oshit){
			System.out.println("internal formation not found");
			input = null;
		}
		if(input == null){
			
			try {
				
				input = context.getResources().openRawResource(resID);
				forms = parser.parseFormation(input);
				input.close();
				
				writeFormations(context, forms, path);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return forms;
	}
	
	public static List<String> loadFormNames(final Context context, String path){
	
		XMLReader parser = new XMLReader();
		InputStream input = null;
		List<String> result = null;
		
		try{
			input = new FileInputStream(context.getFilesDir() + "/" + path);
			result = parser.parseFormationNames(input);
			input.close();
		}
		catch(IOException oshit){
			oshit.printStackTrace();
		}
		return result;
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
	
	public static Configuration loadConfig(final Context context, String path){
		
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
				input = context.getResources().openRawResource(R.raw.config);
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
	
}