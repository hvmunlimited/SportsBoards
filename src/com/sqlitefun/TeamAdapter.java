package com.sqlitefun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TeamAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	private static final String DATABASE_TABLE = "teams";
	
	private Context context;
	private SQLiteDatabase database;
	private TeamDb team;

	public TeamAdapter(Context c){
		context = c;
	}
	
	public TeamAdapter open(){
		team = new TeamDb(context);
		database = team.getWritableDatabase();
		return this;
	}
	
	public void close(){
		database.close();
	}
	
	public long addTeam(String name){
		ContentValues cv = createContentValues(name);
		return database.insert(DATABASE_TABLE, null, cv);
	}
	
	public boolean updateTeam(long rowID, String name){
		ContentValues newvals = createContentValues(name);
		return database.update(DATABASE_TABLE, newvals, KEY_ROWID + "=" + rowID	, null) > 0;
	}
	
	public boolean deleteTeam(long rowID){
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor fetchTeam(long rowID){
		Cursor mCursor = database.query(DATABASE_TABLE, 
				new String[]{KEY_ROWID, KEY_NAME}, 
				KEY_ROWID + "="+ rowID, null, null, null, null);
			if (mCursor != null){
				mCursor.moveToFirst();
			}
			return mCursor;
	}
	
	public Cursor fetchAllTeams(){
		return database.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME}, null, null, null, null, null);
	}
	
	private ContentValues createContentValues(String name){
		ContentValues tReturn = new ContentValues();
		tReturn.put(KEY_NAME, name);
		return tReturn;
	}
}
