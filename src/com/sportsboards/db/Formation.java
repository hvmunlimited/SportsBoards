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
	
	private List<Position> positions;
	public List<Position> getPositions(){ return positions;}
	public void setPositions(List<Position> positions){ this.positions = positions;}
	
	public Formation(){}
	
	public Formation(String name, ArrayList<Position> positions){
		this.name = name;
		this.positions = positions;
	}
	
	
}