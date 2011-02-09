package com.sportsboards.db;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
	
	List<Formation> forms = null;
	Formation fn = null;
	
	ArrayList<Position> positions = null;
	Position pos = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.simple_list_item_1);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1);
		
		TextView name;
		TextView sport;
		
		try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			
			XMLParser myParser = new XMLParser();
			xr.setContentHandler(myParser);
			InputStream inputStream = this.getAssets().open("formations.xml");
			xr.parse(new InputSource(new BufferedInputStream(inputStream)));
		}
		catch (Exception e) {
			System.out.println("XML Passing Exception = " + e);
		}
		
		forms = XMLParser.forms;

		for(int i = 0; i < forms.size(); i++){
			fn = forms.get(i);
			System.out.println(fn.getName());
			System.out.println(fn.getSport());
			
			positions = (ArrayList<Position>)fn.getPositions();
			
			if(positions != null){
			
				for(int j = 0; j < positions.size(); j++){
					pos = positions.get(j);
					System.out.println(pos.getType());
					System.out.println(pos.getPlayerName());
					
				}
			}
			
			name = new TextView(this);
			name.setText("Name = " + fn.getName());
			
			sport = new TextView(this);
			sport.setText("Sport = " + fn.getSport());
			
			layout.addView(name);
			layout.addView(sport);

		}
		setContentView(layout);
		
	}
	
}