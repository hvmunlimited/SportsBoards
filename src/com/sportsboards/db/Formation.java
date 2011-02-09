package com.sportsboards.db;

import java.util.ArrayList;
import java.util.List;

public class Formation{
	
	private String name;
	public String getName(){return name;}
	public void setName(String name){ this.name = name;}
	
	private String sport;
	public String getSport(){return sport;}
	public void setSport(String sport){ this.sport = sport;}
	
	private List<Position> positions;
	public List<Position> getPositions(){ return positions;}
	public void setPositions(List<Position> positions){ this.positions = positions;}
	
	public Formation(){}
	
	public Formation(String name, String sport, ArrayList<Position> positions){
		this.name = name;
		this.sport = sport;
		this.positions = positions;
	}
	
	
}