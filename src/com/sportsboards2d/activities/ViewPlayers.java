/**
 * 
 */
package com.sportsboards2d.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.sportsboards2d.R;
import com.sportsboards2d.boards.BaseBoard;
import com.sportsboards2d.db.objects.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class ViewPlayers extends ListActivity{

	private TextView textFirstName;
	private TextView textLastName;
	private TextView textJersey;
	private TextView textPosition;
	@Override
	public void onCreate(Bundle bundle){
		
		 super.onCreate(bundle);
		 bundle = getIntent().getExtras();
		// PlayerInfo player = bundle.getParcelable("player");
		 setContentView(R.layout.viewplayers);
		
		 
		 setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, BaseBoard.playerNames));
		  
			ListView lv = getListView();
			
			lv.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			    	
			    	setResult(position, null);
			    	ViewPlayers.this.finish();
			    }
			});
	}
	
	public void saveClicked(View v){
		
	}
	public void editClicked(View v){
		
	}
	public void exitClicked(View v){
		this.finish();
	}
}
