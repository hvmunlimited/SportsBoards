package com.sportsboards.db;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

import android.content.Context;
import android.util.Xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class XMLWriter{
	
	
	
	public XMLWriter(){}
	
	public void writeFormation(final Context context, Formation newFN, String pFileName){
		
		try{
			//XMLSerializer writer;
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			
			XMLParser myParser = new XMLParser();
			xr.setContentHandler(myParser);
			context.getAssets().open("formations/" + pFileName + ".xml");
		}
		catch (Exception e) {
			System.out.println("XML Passing Exception = " + e);
		}
		
		
	}
}