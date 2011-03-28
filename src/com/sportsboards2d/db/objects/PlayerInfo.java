package com.sportsboards2d.db.objects;



/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class PlayerInfo{
	
	/*
	 * Variables + Getters/Setters
	 */
	
	private int pID;
	private int jNum;
	private String type;
	private String pName;
	
	/*
	 * Constructors
	 */
	
	public PlayerInfo(int id, int num, String type, String name){
		
		this.setpID(id);
		this.setjNum(num);
		this.setType(type);
		this.pName = name;
	}
	
	public String getInitials(){
		
		String result = "";
		int index;
		
		if(this.pName==""){
			return "";
		}
		result += this.pName.charAt(0);
		index = this.pName.indexOf(" ");
		result += this.pName.charAt(index+1);
		return result;
	}
	public void setpID(int pID) {
		this.pID = pID;
	}
	public int getpID() {
		return pID;
	}
	public void setjNum(int jNum) {
		this.jNum = jNum;
	}
	public int getjNum() {
		return jNum;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}

	public void setPName(String pName) {
		this.pName = pName;
	}

	public String getPName() {
		return pName;
	}
	
}