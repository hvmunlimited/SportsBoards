package com.sqlitefun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PlayerAdd extends Activity{
	private EditText playerName;
	private Button btnAdd;
	private Button btnCancel;
	private DbAdapter mDb;
	long mRowId;
	//open the database, put something in it when Add is clicked.
	public void onCreate(Bundle bundle){
		mDb = new DbAdapter(this);
		mDb.open();
		mRowId = 0;
		Bundle extras = getIntent().getExtras();
		mRowId = extras.getLong(DbAdapter.KEY_TEAM_ID);
		super.onCreate(bundle);
		setContentView(R.layout.player);
		playerName = (EditText)findViewById(R.id.name_player);
		btnAdd = (Button)findViewById(R.id.add_player);
		btnCancel = (Button)findViewById(R.id.close_player);
		
		btnAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO add the record and return to the main screen
				mDb.addPlayer(playerName.getText().toString(), mRowId);
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