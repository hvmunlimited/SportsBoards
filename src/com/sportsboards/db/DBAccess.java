package com.sportsboards.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBAccess{
	
	private static final String DATABASE_NAME = "database.db";
	private static final int DATABASE_VERSION = 1;
	private static final String [] TABLES = {"Soccer", "Basketball", "Football"};
	
	
}