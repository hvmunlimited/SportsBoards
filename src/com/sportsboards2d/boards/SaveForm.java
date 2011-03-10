/**
 * 
 */
package com.sportsboards2d.boards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class SaveForm extends Activity{
 
	@Override
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Save Formation");
			alert.setMessage("Enter a name for the formation");
			
			final EditText input = new EditText(this);
			alert.setView(input);
			
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton){
					
					Intent value = new Intent();
					value.setType((input.getText().toString()));
			
					setResult(1, value);
					SaveForm.this.finish();
				}
			});
			
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
					setResult(-1, null);
					SaveForm.this.finish();
				}
			});
			alert.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        setResult(-1, null);
	        this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	
}
