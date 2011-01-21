package com.sportsboards.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.sportsboards.db.PlayerEntry;

public class DBAccess{
	
	private static final String DATABASE_NAME = "database.db";
	private static final int DATABASE_VERSION = 1;
	
	//private static final String TABLE_FORMATIONS = "Formations";
	//private static final String TABLE_LINEUPS = "Line-ups";
 	
	private final Context context;
	private final SQLiteDatabase db;
	
	private static final String TABLE_PLAYERS = "Players";
	private static final String PLAYER_TABLE_CREATE = "CREATE TABLE " + TABLE_PLAYERS + 
															"(pID INTEGER PRIMARY KEY, "
														   +"FIRST TEXT NOT NULL, LAST TEXT NOT NULL," 
														   +"JNUM INTEGER NOT NULL)";
	
	private static String TABLE_NAME = TABLE_PLAYERS;
	//rows and columns indices
	
	public static final String KEY_PID = "pID";
	public static final String KEY_PFNAME = "FIRST";
	public static final String KEY_PLNAME = "LAST";
	public static final String KEY_JNUM = "JNUM";
	
	public static final int FNAME_COL = 1;
	public static final int LNAME_COL = 2;
	public static final int JNUM_COL = 3;
	
	private final DBAccessHelper dh;
	
	public DBAccess(Context context){
		this.context = context;
		//this.context.deleteDatabase(TABLE_NAME);
		dh = new DBAccessHelper(context);
		db = dh.getWritableDatabase();
		dh.onCreate(db);
		
	}
	
	public void close(){
		if(db!=null){
			db.close();
		}
	}
	
	public void createRow(PlayerEntry player){
		ContentValues initialValue = new ContentValues();
		initialValue.put("pID", player.pID);
		initialValue.put("FIRST", player.fName);
		initialValue.put("LAST", player.lName);
		initialValue.put("JNUM", player.jNum);

		db.insertOrThrow("Players", null, initialValue);
	}
	
	public void deleteRow(long rowID){
		db.delete(TABLE_NAME, "pID=" + rowID, null);
	}
	
	public Cursor GetAllRows(){
		try {
			return db.query(TABLE_NAME, new String[] {"_id", "name"}, null, null, null, null, "");
		}catch (SQLException e){
			Log.i("error on query", e.toString());
			return null;
		}
	}
	
	public void updateRow(long id, String name){
	
		ContentValues args = new ContentValues();
		args.put("name", name);
		db.update(TABLE_NAME, args, "id=" + id, null);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private class DBAccessHelper extends SQLiteOpenHelper{
		
		public DBAccessHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			try {
				db.execSQL(PLAYER_TABLE_CREATE);
				Log.i("create", "created player table");
			}catch(SQLException e){
				Log.i("error", "error making database");
				e.printStackTrace();
			}
			
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
}