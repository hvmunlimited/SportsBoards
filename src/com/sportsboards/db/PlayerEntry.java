package com.sportsboards.db;

public class PlayerEntry{
	
	public int pID;
	public String fName;
	public String lName;
	public int jNum;
	
	public PlayerEntry(){
		pID = -1;
		fName = "";
		lName = "";
		jNum = 0;
	}
	
	public PlayerEntry(int pID, String f, String l, int n){
		this.pID = pID;
		this.fName = f;
		this.lName = l;
		this.jNum = n;
	}
}

