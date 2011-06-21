package com.sqlitefun;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimeDb extends SQLiteOpenHelper{
		private static final String DATABASE_NAME = "appdata";
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_CREATE = "create table times (_id integer primary key autoincrement, " +
				"millis numeric not null, player integer not null);";
		
	public TimeDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TimeDb.class.getName(),"Upgrading database from version " + oldVersion +" to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS times");
		onCreate(db);
	}

}
