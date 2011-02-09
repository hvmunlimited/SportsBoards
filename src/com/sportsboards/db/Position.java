package com.sportsboards.db;

public class Position{
	
	private String type;
	public void setType(String type){ this.type = type;}
	public String getType(){ return type;}
	
	private String pName;
	public void setPlayerName(String p){ this.pName = p;}
	public String getPlayerName(){ return pName;}
	
	private Coordinates coords;
	
	public Position(){}
	
	public Position(String t, String p, Coordinates coords){
		this.type = t;
		this.pName = p;
		this.coords = coords;
	}
	
	private class Coordinates{

		private float x;
		private float y;

		public float getX(){
			return x;
		}
		public float getY(){
			return y;
		}
		public Coordinates(float x, float y){
			this.x = x;
			this.y = y;
		}
	}
}