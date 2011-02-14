package com.sportsboards.db.parsing;

import java.io.InputStream;
import java.util.List;

import android.content.Context;

import com.sportsboards.db.Formation;

public class XMLAccess{
		
	public XMLAccess(){}
	
	public List<Formation> loadFormations(final Context context, int resID){
		
		List<Formation> forms = null;
		
		
			XMLPullFeedParser parser = new XMLPullFeedParser();
			InputStream inputStream = context.getResources().openRawResource(resID);
			forms = parser.parse(inputStream);
			//
		System.out.println(forms.get(0).getName());
		
		return forms;
	}
	
}