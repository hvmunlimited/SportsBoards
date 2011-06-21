package com.sqlitefun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TimeAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TIME = "millis";
	public static final String KEY_PLAYER_ID = "player";
	private static final String DATABASE_TABLE = "times";
	
	private Context context;
	private SQLiteDatabase database;
	private TimeDb time;
	
	public TimeAdapter(Context c){
		context = c;
	}
	
	public TimeAdapter open() throws SQLException{
		time = new TimeDb(context);
		database = time.getWritableDatabase();
		return this;
	}
	
	public void close(){
		database.close();
	}
	
	public long addTime(long val, int playerID){
		ContentValues cv = createContentValues(val,playerID);
		return database.insert(DATABASE_TABLE, null, cv);
	}
	
	public boolean updateTime(long rowID, long val, int playerID){
		ContentValues newvals = createContentValues(val, playerID);
		return database.update(DATABASE_TABLE, newvals, KEY_ROWID + "=" + rowID	, null) > 0;
	}
	
	public boolean deleteTime(long rowID){
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	
	public Cursor fetchTime(long rowID){
		Cursor mCursor = database.query(DATABASE_TABLE, 
				new String[]{KEY_ROWID, KEY_TIME, KEY_PLAYER_ID}, 
				KEY_ROWID + "="+ rowID, null, null, null, null);
			if (mCursor != null){
				mCursor.moveToFirst();
			}
			return mCursor;
	}
	//returns all the times associated with a player.
	public Cursor fetchPlayerTimes(int playerID){
		Cursor mCursor = database.query(DATABASE_TABLE, 
				new String[]{KEY_ROWID, KEY_TIME, KEY_PLAYER_ID}, 
				KEY_PLAYER_ID + "=" + playerID, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor fetchAllTimes(){
		return database.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TIME, KEY_PLAYER_ID}, null, null, null, null, null);
	}
	
	private ContentValues createContentValues(long value, int player){
		ContentValues tReturn = new ContentValues();
		tReturn.put(KEY_TIME, value);
		tReturn.put(KEY_PLAYER_ID, player);
		return tReturn;
	}
}
