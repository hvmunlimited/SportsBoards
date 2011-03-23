package com.sportsboards2d.db;


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
	
	private String type;
	public void setType(String type){ this.type = type;}
	public String getType(){ return type;}
	
	private String pName;
	public void setPlayerName(String p){ this.pName = p;}
	public String getPlayerName(){ return pName;}
	
	private String team;
	public void setTeamColor(String team){ this.team = team;}
	public String getTeamColor(){ return this.team;}
	
	private Coordinates coords;
	public void setCoords(float x, float y){ this.coords = new Coordinates(x, y);}
	
	public float getX(){ return this.coords.getX();}
	public float getY(){ return this.coords.getY();}
	
	/*
	 * Constructors
	 */
	
	public PlayerInfo(){}
	
	public PlayerInfo(String type, String pName, String team, Coordinates coords){
		this.type = type;
		this.pName = pName;
		this.team = team;
		this.coords = coords;
	}
	
	public String getInitials(){
		
		String result = "";
		int index;
		
		if(pName==""){
			return "";
		}
		result += pName.charAt(0);
		index = pName.indexOf(" ");
		result += pName.charAt(index+1);
		return result;
	}
	
}