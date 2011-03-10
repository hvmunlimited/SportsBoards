package com.sportsboards2d.db.parsing;

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

	public static List<Formation> loadFormations(final Context context, int resID){
		
		List<Formation> forms = null;

		XMLReader parser = new XMLReader();
		InputStream inputStream = context.getResources().openRawResource(resID);
		forms = parser.parseFormation(inputStream);
	
		parser = null;
		inputStream = null;
		
		return forms;
	}
	
	public static Configuration loadConfig(final Context context){
		
		XMLReader parser = new XMLReader();
		InputStream inputStream = context.getResources().openRawResource(R.raw.config);
		return parser.parseConfig(inputStream);
	}
	
}