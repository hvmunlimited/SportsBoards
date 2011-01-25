package com.sportsboards.db;

public class PlayerEntry{
	
	public String pos;
	public String fName;
	public String lName;
	public String sport;
	public int jNum;
	
	public PlayerEntry(){
		
		sport = "";
		pos = "";
		fName = "";
		lName = "";
		jNum = 0;
	}

	public PlayerEntry(String sport, String pos, String f, String l, int n){
		this.sport = sport;
		this.pos = pos;
		this.fName = f;
		this.lName = l;
		this.jNum = n;
	}
}

