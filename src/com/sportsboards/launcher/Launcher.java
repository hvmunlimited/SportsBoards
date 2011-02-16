package com.sportsboards.launcher;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.sportsboards.R;

/**
 * Coded by Nathan King
 */

public class Launcher extends ListActivity{
	
	private MenuAdapter mListAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 this.setContentView(R.layout.launcher_menu);
		 mListAdapter = new MenuAdapter(this); 
		 this.setListAdapter(mListAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id){
		super.onListItemClick(lv, v, position, id);
		Activity act = (Activity) this.getListAdapter().getItem(position);
		this.startActivity(new Intent(this, act.Class));
	}
	
}