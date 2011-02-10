package com.sportsboards.db;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;

public class XMLAccess{
	
	public XMLAccess(){}

	public List<Formation> loadFormations(final Context context, String pFileName){
		
		List<Formation> forms = null;
		
		try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			
			XMLParser myParser = new XMLParser();
			xr.setContentHandler(myParser);
			InputStream inputStream = context.getAssets().open("formations/" + pFileName + ".xml");
			xr.parse(new InputSource(new BufferedInputStream(inputStream)));
		}
		catch (Exception e) {
			System.out.println("XML Passing Exception = " + e);
		}
		
		forms = XMLParser.forms;
		
		return forms;
	}
	
}