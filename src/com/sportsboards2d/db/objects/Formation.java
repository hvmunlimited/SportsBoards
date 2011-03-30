package com.sportsboards2d.db.objects;

import java.util.ArrayList;
import java.util.List;


/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class Formation{
	
	/*
	 * Variables + Getters/Setters
	 */
	
	private String name;
	public String getName(){return name;}
	public void setName(String name){ this.name = name;}
	
	private Coordinates ballCoords;
	public Coordinates getBall(){ return ballCoords;}
	public void setBall(Coordinates coords){ this.ballCoords = coords;}
	
	private List<Player> players;
	public List<Player> getPlayers(){ return players;}
	public void setPlayers(List<Player> players){ this.players = players;}
	
	/*
	 * Constructors
	 */
	
	public Formation(){}
	
	public Formation(String name, Coordinates ballCoords, List<Player> players){
		this.setName(name);
		this.setBall(ballCoords);
		this.setPlayers(players);
	}
}
