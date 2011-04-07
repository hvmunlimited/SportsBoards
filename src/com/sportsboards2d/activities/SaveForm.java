/**
 * 
 */
package com.sportsboards2d.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.sportsboards2d.R;

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

			alert.setTitle(R.string.save_form);
			alert.setMessage(R.string.enter_form_name);
			alert.setCancelable(false);
			final EditText input = new EditText(this);
			alert.setView(input);
			
			alert.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton){
					
					Intent value = new Intent();
					value.setType((input.getText().toString()));
			
					setResult(1, value);
					SaveForm.this.finish();
				}
			});
			
			alert.setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
					setResult(-1, null);
					SaveForm.this.finish();
				}
			});
			alert.show();
	}
}
