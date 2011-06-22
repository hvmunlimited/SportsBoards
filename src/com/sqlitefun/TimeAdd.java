package com.sqlitefun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TimeAdd extends Activity{
	private EditText timeInput;
	private Button btnAdd;
	private Button btnCancel;
	private DbAdapter mDb;
	private int playerID;
	//open the database, put something in it when Add is clicked.
	public void onCreate(Bundle bundle){
		mDb = new DbAdapter(this);
		mDb.open();
		Bundle extras = getIntent().getExtras();
		playerID = extras.getInt("player");
		super.onCreate(bundle);
		setContentView(R.layout.time);
		timeInput = (EditText)findViewById(R.id.name_time);
		btnAdd = (Button)findViewById(R.id.add_time);
		btnCancel = (Button)findViewById(R.id.close_time);
		
		btnAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO add the record and return to the main screen
				mDb.addTime(Integer.parseInt(timeInput.getText().toString()), playerID);
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