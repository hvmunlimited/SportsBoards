package com.sportsboards.db;

import java.util.ArrayList;

public class FormationEntry{
	
	private ArrayList<CoordInfo> list;
	public ArrayList<CoordInfo> getCoords(){return list;}
	
	public FormationEntry(ArrayList<CoordInfo> list){
		
		this.list = list;
	
	}
}
