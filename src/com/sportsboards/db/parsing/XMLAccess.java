package com.sportsboards.db.parsing;

import java.io.InputStream;
import java.util.List;
import android.content.Context;
import com.sportsboards.db.Formation;

/**
 * Coded by Nathan King
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