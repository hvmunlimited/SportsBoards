package com.sportsboards2d.sprites;

import org.anddev.andengine.entity.primitive.Line;
	
/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class LineFactory {
	
	private static final float LINEWIDTH_DEFAULT = 5.0f;

	private static float pRed = 100.0f;
	private static float pGreen = 100.0f;
	private static float pBlue = 100.0f;
	
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final int RED = 2;
	public static final int GREEN = 3;
	public static final int BLUE = 4;
	
	
	public static Line createLine(float pStartX, float pStartY, float pEndX, float pEndY){
		Line line = new Line(pStartX, pStartY, pEndX, pEndY, LINEWIDTH_DEFAULT);
		line.setColor(pRed, pGreen, pBlue);
		return line;
	}
	
	public static Line createLine(float pStartX, float pStartY, float pEndX, float pEndY, float line_width){
		Line line = new Line(pStartX, pStartY, pEndX, pEndY, line_width);
		line.setColor(pRed, pGreen, pBlue);
		return line;
	}
	
	public static void setColor(int id){
		switch(id){
		
			case WHITE:
				pRed = 100.0f;
				pGreen = 100.0f;
				pBlue = 100.0f;
				
				break;
			case BLACK:
				pRed = 0.0f;
				pGreen = 0.0f;
				pBlue = 0.0f;
				break;
				
			case RED:
				pRed = 255.0f;
				pGreen = 0.0f;
				pBlue = 0.0f;
				break;
				
			case GREEN:
				pRed = 0.0f;
				pGreen = 255.0f;
				pBlue = 0.0f;
				break;
				
			case BLUE:
				pRed = 0.0f;
				pGreen = 0.0f;
				pBlue = 255.0f;
				break;
		}	
	}
}
