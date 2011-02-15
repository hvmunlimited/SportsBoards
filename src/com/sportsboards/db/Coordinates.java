package com.sportsboards.db;

/**
 * Coded by Nathan King
 */

public class Coordinates{

	/*
	 * Variables + Getters/Setters
	 */
	
	private float x;
	public float getX(){ return this.x;}
	public void setX(float x){ this.x = x;}
	
	public float y;
	public float getY(){ return this.y;}
	public void setY(float y){ this.y = y;}
	
	/*
	 * Constructors
	 */
	
	public Coordinates(float x, float y){
		this.x = x;
		this.y = y;
	}
}