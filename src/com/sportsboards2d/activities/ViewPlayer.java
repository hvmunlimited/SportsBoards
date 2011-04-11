/**
 * 
 */
package com.sportsboards2d.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sportsboards2d.R;
import com.sportsboards2d.db.objects.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class ViewPlayer extends Activity{

	private TextView textFirstName;
	private TextView textLastName;
	private TextView textJersey;
	private TextView textPosition;
	@Override
	public void onCreate(Bundle bundle){
		
		 super.onCreate(bundle);
		 bundle = getIntent().getExtras();
		 PlayerInfo player = bundle.getParcelable("player");
		 setContentView(R.layout.viewplayer);
		 
		 textFirstName = (TextView)findViewById(R.id.playername_first_text);
         textLastName = (TextView)findViewById(R.id.playername_last_text);
         textJersey = (TextView)findViewById(R.id.playernum_text);
         textPosition = (TextView)findViewById(R.id.playerposition_text);
         
         textFirstName.setText(player.getFirstName());
         textLastName.setText(player.getLastName());
         textJersey.setText(String.valueOf(player.getjNum()));
         textPosition.setText(player.getType());
	}
	
	public void okClicked(View v){
		this.finish();
	}
}
