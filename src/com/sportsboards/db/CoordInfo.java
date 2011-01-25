package com.sportsboards.db;

public class CoordInfo{
		
	private String tag;
	private long X, Y;
	
	public String getTag(){return tag;}
	public long getX(){return X;}
	public long getY(){return Y;}

	public CoordInfo(String tag, long x, long y){
		this.tag = tag;
		this.X = x;
		this.Y = y;
	}
	
}