/**
 * 
 */
package com.sportsboards2d.util;

import java.util.List;

import com.sportsboards2d.db.objects.Formation;
import com.sportsboards2d.db.objects.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class StringPrinting {
	
	
	public static void printAllFormation(List<Formation> forms){
		
		for(Formation fn:forms){
			System.out.println(fn.getName());
			System.out.println(fn.getBall().getX() + " " + fn.getBall().getY());
			
			for(PlayerInfo p:fn.getPlayers()){
				
				System.out.println(p.getType());
			}
		}
	}
	
	public static void printPlayerInfo(PlayerInfo player){
		
		System.out.println("Player name: " + player.getPName());
		System.out.println("Player ID: " + player.getpID());
		System.out.println("Player jersey#: " + player.getjNum());
		System.out.println("Player position: " + player.getType());
	}

}
