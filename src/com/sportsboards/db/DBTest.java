package com.sportsboards.db;
import android.app.Activity;
import android.os.Bundle;

public class DBTest extends Activity{
	
	@Override
	public void onCreate(Bundle bd){
		super.onCreate(bd);
		DBAccess db = new DBAccess(this);
		PlayerEntry p = new PlayerEntry(0, "Nate", "King", 24);
		db.createRow(p);
		
		
	}
	
	
}
