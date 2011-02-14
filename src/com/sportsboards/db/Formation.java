package com.sportsboards.db;

import java.util.ArrayList;
import java.util.List;

public class Formation{
	
	private String name;
	public String getName(){return name;}
	public void setName(String name){ this.name = name;}
	
	private Coordinates ballCoords;
	public Coordinates getBall(){ return ballCoords;}
	public void setBall(float x, float y){ this.ballCoords = new Coordinates(x, y);}
	
	private List<PlayerInfo> players;
	public List<PlayerInfo> getPlayers(){ return players;}
	public void setPlayers(List<PlayerInfo> players){ this.players = players;}
	
	public Formation(){}
	
	public Formation(String name, ArrayList<PlayerInfo> players){
		this.name = name;
		this.players = players;
	}
}
