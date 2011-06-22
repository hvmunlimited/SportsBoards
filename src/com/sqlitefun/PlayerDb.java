package com.sqlitefun;
/*
 * DEPRECATED, This class has been replaced by DbHelper
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlayerDb extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "appdata";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table players " +
			"(_id integer primary key autoincrement, name text not null, team integer not null);"; 
	public PlayerDb(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TimeDb.class.getName(),"Upgrading database from version " + oldVersion +" to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS players");
		onCreate(db);
		
	}
	
}
