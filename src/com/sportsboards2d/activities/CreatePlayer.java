/**
 * 
 */
package com.sportsboards2d.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sportsboards2d.R;
import com.sportsboards2d.boards.BaseBoard;
import com.sportsboards2d.db.objects.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class CreatePlayer extends Activity{
	
	private EditText textFirstName;
	private EditText textLastName;
	private EditText jerseyNumber;
	private EditText position;

	private boolean validFirstName = false;
	private boolean validLastName = false;
	private boolean validJerseyNumber = false;
	private boolean validPosition = false;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.createplayer);
		
        // Builder dialog = new Builder(this);
         
         textFirstName = (EditText)findViewById(R.id.playername_first_edit);
         textLastName = (EditText)findViewById(R.id.playername_last_edit);
         jerseyNumber = (EditText)findViewById(R.id.playernum_edit);
         position = (EditText)findViewById(R.id.playerposition_edit);
         
         textFirstName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
        	 @Override
        	 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        		 if (actionId == EditorInfo.IME_ACTION_DONE) {
        			 
        			 boolean result = v.getText().toString().matches(getString(R.string.regex_first_name));
        			 System.out.println(v.getText());
        			 System.out.println(result);
        			 
        			 if(result){
        				 validFirstName = true;
        			 }
        			 
        		 }
        		 return false;
        	 }
         }
         );
         textLastName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
        	 @Override
        	 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        		 if (actionId == EditorInfo.IME_ACTION_DONE) {
        			 boolean result = v.getText().toString().matches(getString(R.string.regex_last_name));
        			 System.out.println(v.getText());
        			 System.out.println(result);
        			 if(result){
        				 validLastName = true;
        			 }
        		 }
        		 return false;
        	 }
         }
         );
         jerseyNumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
        	 @Override
        	 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        		 if (actionId == EditorInfo.IME_ACTION_DONE) {
        			 boolean result = v.getText().toString().matches(getString(R.string.regex_jerseynum));
        			 System.out.println(v.getText());
        			 System.out.println(result);
        			 if(result){
            			 validJerseyNumber = true;
            		 }
        		 }
        		 
        		 return false;
        	 }
         }
         );
         position.setOnEditorActionListener(new EditText.OnEditorActionListener() {
        	 @Override
        	 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        		 if (actionId == EditorInfo.IME_ACTION_DONE) {
        			 boolean result = v.getText().toString().matches(getString(R.string.regex_position));
        			 System.out.println(v.getText());
        			 System.out.println(result);
        			 if(result){
            			 validPosition = true;
            		 }
        		 }
        		 return false;
        	 }
         }
         );
        
	}
	
	public void okClicked(View v){
		PlayerInfo newPlayer;
		
		if(validFirstName && validLastName && validJerseyNumber && validPosition){
			int num = Integer.parseInt(jerseyNumber.getText().toString());
			String type = position.getText().toString();
			String name = textFirstName.getText().toString() + " " + textLastName.getText().toString();
			newPlayer = new PlayerInfo(BaseBoard.playerIDCounter, num, type, name);
			
			Intent result = new Intent();
			result.putExtra(getString(R.string.players_create), newPlayer);
			setResult(5, result);
			
			this.finish();
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}
	
	public void cancelClicked(View v){
		this.setResult(-1, null);
		this.finish();
	}
	
}
