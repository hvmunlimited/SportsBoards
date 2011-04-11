/**
 * 
 */
package com.sportsboards2d.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.sportsboards2d.boards.BaseBoard;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class SelectPlayer extends ListActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		
		//int size = bundle.getParcelable("size");
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, BaseBoard.playerNames));
	  
	}

}
