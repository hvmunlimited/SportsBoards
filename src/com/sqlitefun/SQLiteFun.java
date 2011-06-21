package com.sqlitefun;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SQLiteFun extends Activity {
	
	private DbAdapter mDb;	
	private long currTeamID;
	private long currPlayerID;
	private Button addTeam;
	private Button addPlayer;
	private Button addTime;
	private Spinner spinTeam;
	private Spinner spinPlayer;
	private ListView listTime;
	private TextView txtTeamID;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //get the database
        mDb = new DbAdapter(this);
        mDb.open();
        //start up all the widgets
        txtTeamID= (TextView)findViewById(R.id.team_id);
        addTeam = (Button)findViewById(R.id.add_team);
        addPlayer = (Button)findViewById(R.id.add_player);
        addTime = (Button)findViewById(R.id.add_time);
        spinTeam = (Spinner)findViewById(R.id.spin_team);
        spinPlayer = (Spinner)findViewById(R.id.spin_player);
        listTime = (ListView)findViewById(R.id.list_time);
        //load the teams into the spinner

        updateTeams();
        //set listeners for spinners;
        addTeam.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//make a splash screen that prompts for a name,
				//add the name to the database, and refresh the Team Spinner
				openTeamMenu();
			}
        	
        });
        addPlayer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//open a menu to add a player
				openPlayerMenu();
			}
        	
        });
        addTime.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		//open a menu to add a time
        		openTimeMenu();
        	}
        });

    } 
    public class MyTeamSpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			//get the ID from the db row at (position)
			System.out.println("TEAM SELECTED: " + parent.getItemAtPosition(position));
			currTeamID = id;
			loadPlayers(currTeamID);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// do nothing
		}
    	
    }
    public class MyPlayerSpinnerListener implements OnItemSelectedListener{

		
		public void onItemSelected(AdapterView<?> spin, View v, int position,
				long id) {
			System.out.println(spin.getItemAtPosition(position));
			loadTimes((int) id);
			currPlayerID = (int)id;
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// do nothing
		}
    	
    }
    //loads all the teams into the spinner
    private void updateTeams(){
    	Cursor teamsCursor;
    	teamsCursor = mDb.fetchAllTeams();
    	startManagingCursor(teamsCursor);
    	//fields that you want in the spinner...
    	String[] from = new String[]{mDb.KEY_TEAMNAME, mDb.KEY_ROWID};
    	//we should load one record at a time, id into a team ID array, name into a different array.
    	int[] to = new int[]{R.id.label, R.id.label_id};
    	SimpleCursorAdapter teamsAdapter = new SimpleCursorAdapter(this, R.layout.db_row, teamsCursor, from, to);
    	spinTeam.setAdapter(teamsAdapter);
    }
    //loads all the players on a given team
    private void loadPlayers(long teamID){
    	Cursor playersCursor;
    	playersCursor = mDb.fetchTeamPlayers(teamID);
    	startManagingCursor(playersCursor);
    	String[] from = new String[]{mDb.KEY_NAME, mDb.KEY_TEAM_ID};
    	int[] to = new int[]{R.id.label, R.id.label_id};
    	SimpleCursorAdapter playersAdapter = new SimpleCursorAdapter(this, R.layout.db_row, playersCursor, from, to);
    	spinPlayer.setAdapter(playersAdapter);
    }
    //loads all the player's times into the list.
    private void loadTimes(int player){
    	Cursor timesCursor;
    	timesCursor = mDb.fetchPlayerTimes(player);
    	startManagingCursor(timesCursor);
    	String[] from = new String[]{mDb.KEY_TIME, mDb.KEY_PLAYER_ID};
    	int[] to = new int[]{R.id.label, R.id.label_id};
    	SimpleCursorAdapter timesAdapter = new SimpleCursorAdapter(this,R.layout.db_row, timesCursor, from, to);
    	listTime.setAdapter(timesAdapter);
    }
	public void onResume(){
		updateTeams();
		super.onResume();
	}
    
    private void openTeamMenu(){
    	Intent i = new Intent(this, TeamAdd.class);
    	startActivity(i);
    }
    private void openPlayerMenu(){
    	Intent i = new Intent(this, PlayerAdd.class);
    	i.putExtra(DbAdapter.KEY_TEAM_ID, currTeamID);
    	startActivity(i);
    }
    private void openTimeMenu(){
    	Intent i = new Intent(this, TimeAdd.class);
    	i.putExtra(DbAdapter.KEY_PLAYER_ID, currPlayerID);
    	startActivity(i);
    }
    
    public void finish(){
    	super.finish();
    	mDb.close();
    }
}
