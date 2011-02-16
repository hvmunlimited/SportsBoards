package com.sportsboards.launcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sportsboards.R;

/**
 * Coded by Nathan King
 */

class MenuAdapter extends BaseAdapter{

	private static final Activity[] Activities = {
		Activity.SOCCER,
		//Activity.FOOTBALL,
		Activity.BBALL,
		//Activity.XMLPARSETEST,
	};
	
	private final Context mContext;
	
	public MenuAdapter(final Context context){
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		return Activities.length;
	}

	@Override
	public Activity getItem(int arg0) {
		return Activities[arg0];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		
		if(convertView!=null){
			view = convertView;
		}
		else{
			view = LayoutInflater.from(this.mContext).inflate(R.layout.row, null);
		}
		
		((TextView)view.findViewById(R.id.tv_listrow_sport_name)).setText(this.getItem(position).id);
		return view;
	}
	
}