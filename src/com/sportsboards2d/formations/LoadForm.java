/**
 * 
 */
package com.sportsboards2d.formations;

import android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sportsboards2d.db.parsing.XMLAccess;
import com.sportsboards2d.db.parsing.XMLReader;
import com.sportsboards2d.boards.BaseBoard;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class LoadForm extends ListActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setResult(-1, null);
	  
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_1, BaseBoard.formNames));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	     
	    	setResult(position, null);
	    	LoadForm.this.finish();
	    }
	  });
	}
	
	
}
