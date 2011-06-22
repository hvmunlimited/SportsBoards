package com.sqlitefun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//Handles interaction with the database.
public class DbAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TEAMNAME = "teamname";
	public static final String KEY_NAME = "name";
	public static final String KEY_TIME = "time";
	public static final String KEY_TEAM_ID = "team";
	public static final String KEY_PLAYER_ID = "player";
	
	private static final String TEAM_TABLE = "teams";
	private static final String PLAYER_TABLE = "players";
	private static final String TIME_TABLE = "times";
	
	private Context context;
	private SQLiteDatabase database;
	private DbHelper mDbhelper;

	public DbAdapter(Context c){
		context = c;
	}
	
	public DbAdapter open(){
		mDbhelper = new DbHelper(context);
		database = mDbhelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		database.close();
	}
	
	public long addTeam(String name){
		ContentValues cv = createContentValues(name);
		System.out.println("ADDED TEAM " + name + cv);
		return database.insert(TEAM_TABLE, null, cv);
	}
	
	public boolean updateTeam(long rowID, String name){
		ContentValues newvals = createContentValues(name);
		return database.update(TEAM_TABLE, newvals, KEY_ROWID + "=" + rowID	, null) > 0;
	}
	
	public boolean deleteTeam(long rowID){
		return database.delete(TEAM_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor fetchTeam(long rowID){
		Cursor mCursor = database.query(TEAM_TABLE, 
				new String[]{KEY_ROWID, KEY_TEAMNAME}, 
				KEY_ROWID + "="+ rowID, null, null, null, null);
			if (mCursor != null){
				mCursor.moveToFirst();
			}
			return mCursor;
	}
	
	public Cursor fetchAllTeams(){
		return database.query(TEAM_TABLE, new String[]{KEY_ROWID, KEY_TEAMNAME}, null, null, null, null, null);
	}
	
	public long addPlayer(String name, long mRowId){
		ContentValues cv = createContentValues(name, mRowId);
		return database.insert(PLAYER_TABLE, null, cv);
	}
	
	public boolean updatePlayer(long rowID, String name, int team){
		ContentValues newvals = createContentValues(name, team);
		return database.update(PLAYER_TABLE, newvals, KEY_ROWID + "=" + rowID	, null) > 0;
	}
	
	public boolean deletePlayer(long rowID){
		return database.delete(PLAYER_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor fetchPlayer(long rowID){
		Cursor mCursor = database.query(PLAYER_TABLE, 
				new String[]{KEY_ROWID, KEY_NAME, KEY_TEAM_ID}, 
				KEY_ROWID + "="+ rowID, null, null, null, null);
			if (mCursor != null){
				mCursor.moveToFirst();
			}
			return mCursor;
	}
	//returns all the players on a team.
	public Cursor fetchTeamPlayers(long currTeamID2){
		Cursor mCursor = database.query(PLAYER_TABLE, 
				new String[]{KEY_ROWID, KEY_NAME, KEY_TEAM_ID}, 
				KEY_TEAM_ID + "=" + currTeamID2, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor fetchAllPlayers(){
		return database.query(PLAYER_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_TEAM_ID}, null, null, null, null, null);
	}
	public long addTime(long val, long playerID){
		ContentValues cv = createContentValues(val,playerID);
		return database.insert(TIME_TABLE, null, cv);
	}
	
	public boolean updateTime(long rowID, long val, int playerID){
		ContentValues newvals = createContentValues(val, playerID);
		return database.update(TIME_TABLE, newvals, KEY_ROWID + "=" + rowID	, null) > 0;
	}
	
	public boolean deleteTime(long rowID){
		return database.delete(TIME_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor fetchTime(long rowID){
		Cursor mCursor = database.query(TIME_TABLE, 
				new String[]{KEY_ROWID, KEY_TIME, KEY_PLAYER_ID}, 
				KEY_ROWID + "="+ rowID, null, null, null, null);
			if (mCursor != null){
				mCursor.moveToFirst();
			}
			return mCursor;
	}
	//returns all the times associated with a player.
	public Cursor fetchPlayerTimes(int playerID){
		Cursor mCursor = database.query(TIME_TABLE, 
				new String[]{KEY_ROWID, KEY_TIME, KEY_PLAYER_ID}, 
				KEY_PLAYER_ID + "=" + playerID, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor fetchAllTimes(){
		return database.query(TIME_TABLE, new String[]{KEY_ROWID, KEY_TIME, KEY_PLAYER_ID}, null, null, null, null, null);
	}
	//time
	private ContentValues createContentValues(long val, long mRowId){
		ContentValues tReturn = new ContentValues();
		tReturn.put(KEY_TIME, val);
		tReturn.put(KEY_PLAYER_ID, mRowId);
		return tReturn;
	}
	//player
	private ContentValues createContentValues(String name, long teamID){
		ContentValues tReturn = new ContentValues();
		tReturn.put(KEY_NAME, name);
		tReturn.put(KEY_TEAM_ID, teamID);
		return tReturn;
	}
	//team
	private ContentValues createContentValues(String name){
		ContentValues tReturn = new ContentValues();
		tReturn.put(KEY_TEAMNAME, name);
		return tReturn;
	}
}
