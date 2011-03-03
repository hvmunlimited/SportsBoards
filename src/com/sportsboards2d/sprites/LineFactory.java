package com.sportsboards2d.sprites;

import org.anddev.andengine.entity.primitive.Line;
//import com.sportsboards2d.R;
/**
 * Coded by Nathan King
 */

import com.sportsboards2d.R;
	
/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class LineFactory {
	
	private static final float LINEWIDTH_DEFAULT = 5.0f;

	private static float RED = 100.0f;
	private static float GREEN = 100.0f;
	private static float BLUE = 100.0f;
	
	
	public static Line createLine(float pStartX, float pStartY, float pEndX, float pEndY){
		Line line = new Line(pStartX, pStartY, pEndX, pEndY, LINEWIDTH_DEFAULT);
		line.setColor(RED, GREEN, BLUE);
		return line;
	}
	
	public static Line createLine(float pStartX, float pStartY, float pEndX, float pEndY, float line_width){
		Line line = new Line(pStartX, pStartY, pEndX, pEndY, line_width);
		line.setColor(RED, GREEN, BLUE);
		return line;
	}
	
	public static void setColor(int id){
		switch(id){
		
			case R.id.line_white:
				RED = 100.0f;
				GREEN = 100.0f;
				BLUE = 100.0f;
				
				break;
			case R.id.line_black:
				RED = 0.0f;
				GREEN = 0.0f;
				BLUE = 0.0f;
				break;
				
			case R.id.line_red:
				RED = 255.0f;
				GREEN = 0.0f;
				BLUE = 0.0f;
				break;
				
			case R.id.line_green:
				RED = 0.0f;
				GREEN = 255.0f;
				BLUE = 0.0f;
				break;
				
			case R.id.line_blue:
				RED = 0.0f;
				GREEN = 0.0f;
				BLUE = 255.0f;
				break;
		}	
	}
}
