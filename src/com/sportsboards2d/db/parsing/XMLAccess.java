package com.sportsboards2d.db.parsing;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

	public static void writeFormation(final Context context, Formation fn, String path){
		
		XMLWriter writer = new XMLWriter();
		writer.writeFormation(context, fn, path + ".xml");
		
	}
	
	public static void writeConfig(final Context context, Configuration config, String path){
		
		
		XMLWriter writer = new XMLWriter();
		writer.writeConfig(context, config, path + ".xml");
		
	}
	
	
	public static List<Formation> loadFormations(final Context context, int resID){
		
		List<Formation> forms = null;
		String[] fileList = context.fileList();
		
		
		for(String s:fileList){
			System.out.println(s);
		}
		
		XMLReader parser = new XMLReader();
		try{
			InputStream input = new FileInputStream(context.getFilesDir() + "basketball.xml");
			forms = parser.parseFormation(input);
		}
		catch(FileNotFoundException oshit){
			System.out.println("internal formation not found");
			InputStream inputStream = context.getResources().openRawResource(resID);
			forms = parser.parseFormation(inputStream);
		}
		
		
		InputStream inputStream = context.getResources().openRawResource(resID);
		forms = parser.parseFormation(inputStream);
	
		parser = null;
		inputStream = null;
		
		return forms;
	}
	
	public static Configuration loadConfig(final Context context){
		
		XMLReader parser = new XMLReader();
		InputStream input;
		
		try{
			input = new FileInputStream(context.getFilesDir()+"config.xml");
			return parser.parseConfig(input);
		}
		catch(FileNotFoundException oshit){
			System.out.println("internal config not found");
			input = context.getResources().openRawResource(R.raw.config);
			return parser.parseConfig(input);
		}
		
	}
	
}