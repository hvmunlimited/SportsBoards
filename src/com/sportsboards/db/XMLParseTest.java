package com.sportsboards.db;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XMLParseTest extends Activity{
	
	FormsList formsList = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String path = "C:/Users/nate/workspace/sportsboards/res/values/strings.xml";
		setContentView(R.layout.simple_list_item_1);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1);
		
		TextView form[];
		TextView name[];
		TextView sport[];
		
		try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			
			XMLParser myParser = new XMLParser();
			xr.setContentHandler(myParser);
			InputStream inputStream = this.getAssets().open(path);
			xr.parse(new InputSource(new BufferedInputStream(inputStream)));
		}
		catch (Exception e) {
			System.out.println("XML Passing Exception = " + e);
		}
		
		formsList = XMLParser.forms;
		
		form = new TextView[formsList.getForm().size()];
		
		for(int i = 0; i < formsList.getForm().size(); i++){
			
			form[i] = new TextView(this);
			form[i].setText("Name = " + formsList.getForm().get(i));
			layout.addView(form[i]);
		}
		setContentView(layout);
	}
	
}