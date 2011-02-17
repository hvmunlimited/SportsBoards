package com.sportsboards2d.db.parsing;

import java.io.InputStream;
import java.util.List;
import android.content.Context;

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
		

		XMLPullFeedParser parser = new XMLPullFeedParser();
		InputStream inputStream = context.getResources().openRawResource(resID);
		forms = parser.parse(inputStream);
	
		
		return forms;
	}
	
}