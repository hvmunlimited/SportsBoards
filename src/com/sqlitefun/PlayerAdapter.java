package com.sqlitefun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PlayerAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_TEAM_ID = "team";
	private static final String DATABASE_TABLE = "players";
	
	private Context context;
	private SQLiteDatabase database;
	private PlayerDb player;

	public PlayerAdapter(Context c){
		context = c;
	}
	
	public PlayerAdapter open(){
		player = new PlayerDb(context);
		database = player.getWritableDatabase();
		return this;
	}
	
	public void close(){
		database.close();
	}
	
	public long addPlayer(String name, int team){
		ContentValues cv = createContentValues(name, team);
		return database.insert(DATABASE_TABLE, null, cv);
	}
	
	public boolean updatePlayer(long rowID, String name, int team){
		ContentValues newvals = createContentValues(name, team);
		return database.update(DATABASE_TABLE, newvals, KEY_ROWID + "=" + rowID	, null) > 0;
	}
	
	public boolean deletePlayer(long rowID){
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor fetchPlayer(long rowID){
		Cursor mCursor = database.query(DATABASE_TABLE, 
				new String[]{KEY_ROWID, KEY_NAME, KEY_TEAM_ID}, 
				KEY_ROWID + "="+ rowID, null, null, null, null);
			if (mCursor != null){
				mCursor.moveToFirst();
			}
			return mCursor;
	}
	//returns all the players on a team.
	public Cursor fetchTeamPlayers(int teamID){
		Cursor mCursor = database.query(DATABASE_TABLE, 
				new String[]{KEY_ROWID, KEY_NAME, KEY_TEAM_ID}, 
				KEY_TEAM_ID + "=" + teamID, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor fetchAllPlayers(){
		return database.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_TEAM_ID}, null, null, null, null, null);
	}
	
	private ContentValues createContentValues(String name, int teamID){
		ContentValues tReturn = new ContentValues();
		tReturn.put(KEY_NAME, name);
		tReturn.put(KEY_TEAM_ID, teamID);
		return tReturn;
	}
}

