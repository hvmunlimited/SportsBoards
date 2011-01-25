package com.sportsboards.db;

import java.util.ArrayList;


public class LineupEntry{
	
	private ArrayList<PlayerEntry> list;
	public ArrayList<PlayerEntry> getPlayers(){return list;}
	
	public LineupEntry(ArrayList<PlayerEntry> list){
		
		this.list = list;
	}
	
}