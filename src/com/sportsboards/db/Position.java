package com.sportsboards.db;

public class Position{
	
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
	
	public void setCoords(float x, float y){
		this.coords = new Coordinates(x, y);
	}
	public Coordinates getCoords(){ return coords;}
	
	public float getX(){ return this.coords.x;}
	public float getY(){ return this.coords.y;}
	
	public Position(){}
	
	public Position(String type, String pName, String team, Coordinates coords){
		this.type = type;
		this.pName = pName;
		this.team = team;
		this.coords = coords;
	}
	
	
}