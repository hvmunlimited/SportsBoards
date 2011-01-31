package com.sportsboards.db;
import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import com.sportsboards.db.CoordInfo;
import com.sportsboards.boards.BaseBoard;

public class DBTest extends BaseBoard{
	
	private ArrayList<CoordInfo> coords = new ArrayList<CoordInfo>();
	@Override
	public Engine onLoadEngine() {
		DBAccess db = new DBAccess(this);
		PlayerEntry p = new PlayerEntry("SOCCER", "GK", "Nate", "King", 24);
		db.createPlayer(p);
		
		CoordInfo pc = new CoordInfo("GK", 57, 234);
		coords.add(pc);
		pc = new CoordInfo("DF", 190, 60);
		coords.add(pc);
		pc = new CoordInfo("F", 383, 136);
		int xInterval;
		int yInterval;
		int startX = 0;
		int startY = 0;
		
		for(int i = 0; i < 11; i++){
			
			
			
			
		}
		
		
		
		// TODO Auto-generated method stub
		return super.onLoadEngine();
	}


	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
		super.onLoadResources();
	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		return super.onLoadScene();
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
	
	
}
