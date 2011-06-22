package com.sqlitefun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TeamDb extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "appdata";
	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_CREATE = "create table teams " +
			"(_id integer primary key autoincrement, name text unique not null);";
	public TeamDb(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TimeDb.class.getName(),"Upgrading database from version " + oldVersion +" to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS teams");
		onCreate(db);
		
	}
}
