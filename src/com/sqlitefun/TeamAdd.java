package com.sqlitefun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TeamAdd extends Activity{
	private EditText teamName;
	private Button btnAdd;
	private Button btnCancel;
	private DbAdapter mDb;
	//open the database, put something in it when Add is clicked.
	public void onCreate(Bundle bundle){
		mDb = new DbAdapter(this);
		mDb.open();
		super.onCreate(bundle);
		setContentView(R.layout.team);
		teamName = (EditText)findViewById(R.id.name_team);
		btnAdd = (Button)findViewById(R.id.add_team);
		btnCancel = (Button)findViewById(R.id.close_team);
		
		btnAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO add the record and return to the main screen
				mDb.addTeam(teamName.getText().toString());
				setResult(RESULT_OK);
				finish();
			}
			
		});
		btnCancel.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
			finish();	
			}
		});
	}
	public void finish(){
		mDb.close();
		super.finish();
	}
}
