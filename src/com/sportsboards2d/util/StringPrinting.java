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
				
				System.out.println(p.getTeamColor());
				System.out.println(p.getType());
				System.out.println(p.getPlayerName());
				System.out.println(p.getX() + " " + p.getY());
			}
		}
	}

}
