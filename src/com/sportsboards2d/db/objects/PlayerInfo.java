package com.sportsboards2d.db.objects;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class PlayerInfo implements Parcelable{
	
	/*
	 * Variables + Getters/Setters
	 */
	
	private int pID;
	private int jNum;
	private String type;
	private String pName;
	
	/*
	 * Constructors
	 */
	
	public PlayerInfo(int id, int num, String type, String name){
		
		this.setpID(id);
		this.setjNum(num);
		this.setType(type);
		this.pName = name;
	}
	
	public String getInitials(){
		
		String result = "";
		int index;
		
		if(this.pName==""){
			return "";
		}
		result += this.pName.charAt(0);
		index = this.pName.indexOf(" ");
		result += this.pName.charAt(index+1);
		return result;
	}
	public void setpID(int pID) {
		this.pID = pID;
	}
	public int getpID() {
		return pID;
	}
	public void setjNum(int jNum) {
		this.jNum = jNum;
	}
	public int getjNum() {
		return jNum;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}

	public void setPName(String pName) {
		this.pName = pName;
	}

	public String getPName() {
		return pName;
	}

	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeInt(pID);
		out.writeInt(jNum);
		out.writeString(type);
		out.writeString(pName);
		
	}
	public static final Parcelable.Creator<PlayerInfo> CREATOR = new Parcelable.Creator<PlayerInfo>() {
        public PlayerInfo createFromParcel(Parcel in) {
            return new PlayerInfo(in);
        }

        public PlayerInfo[] newArray(int size) {
            return new PlayerInfo[size];
        }
    };
    private PlayerInfo(Parcel in) {
        this.pID = in.readInt();
        this.jNum = in.readInt();
        this.type = in.readString();
        this.pName = in.readString();
    }

	
}