package com.sportsboards2d.boards;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.HoldDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.SurfaceScrollDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sportsboards2d.R;
import com.sportsboards2d.sprites.LineFactory;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public abstract class Interface extends BaseGameActivity implements IOnAreaTouchListener, IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener{
	
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final int CAMERA_WIDTH = 1024;
	protected static final int CAMERA_HEIGHT = 600;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected Camera mCamera;
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	protected HoldDetector mHoldDetector;
	
	protected boolean LARGE_PLAYERS = true;
	protected boolean LINE_ENABLED = false;
	
	@Override
	public void onScroll(final ScrollDetector pScollDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY) {
		//final float zoomFactor = this.mZoomCamera.getZoomFactor();
		//this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		//this.mPinchZoomStartedCameraZoomFactor = this.mZoomCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		//if(this.mZoomCamera.getZoomFactor() < 1.0f){
		//	this.mZoomCamera.setZoomFactor(1.0f);
		//}
		//else{
		//	this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
		//}
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		//this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
		//if(this.mZoomCamera.getZoomFactor() > 3.0f){
		//	this.mZoomCamera.setZoomFactor(1.0f);
		//}
		//else if(this.mZoomCamera.getZoomFactor() < 1.0f){
		//	this.mZoomCamera.setZoomFactor(1.0f);
		//}
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		
		if(this.mPinchZoomDetector != null) {
			this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

			if(this.mPinchZoomDetector.isZooming()) {
				this.mScrollDetector.setEnabled(false);
			} else {
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					//this.mScrollDetector.setEnabled(true);
				}
				this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}//
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.anddev.andengine.ui.IGameInterface#onLoadComplete()
	 */
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.anddev.andengine.ui.IGameInterface#onLoadEngine()
	 */
	@Override
	public Engine onLoadEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.anddev.andengine.ui.IGameInterface#onLoadResources()
	 */
	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.anddev.andengine.ui.IGameInterface#onLoadScene()
	 */
	@Override
	public Scene onLoadScene() {
		
		Scene scene = new Scene(3);
		this.mScrollDetector = new SurfaceScrollDetector(this);
		if(MultiTouch.isSupportedByAndroidVersion()) {
			try {
				this.mPinchZoomDetector = new PinchZoomDetector(this);
			} catch (final MultiTouchException e) {
				this.mPinchZoomDetector = null;
			}
		} else {
			this.mPinchZoomDetector = null;
		}
		this.mPinchZoomDetector.setEnabled(false);
		this.mScrollDetector.setEnabled(false);
		return scene;
	}
	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.layout.settings_menu, pMenu);
		 return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()) {
		
			case R.id.line_enable:
				
				if(LINE_ENABLED){
					LINE_ENABLED = false;

				}
				else{
					LINE_ENABLED = true;
					item.setChecked(true);
				}
				
				return true;
				
			case R.id.line_black:
				item.setChecked(true);
				LineFactory.setColor(item.getItemId());
				return true;
			case R.id.line_white:
				item.setChecked(true);
				LineFactory.setColor(item.getItemId());
				return true;
			case R.id.line_red:
				item.setChecked(true);
				LineFactory.setColor(item.getItemId());
				return true;
			case R.id.line_green:
				item.setChecked(true);
				LineFactory.setColor(item.getItemId());
				return true;
			case R.id.line_blue:
				item.setChecked(true);
				LineFactory.setColor(item.getItemId());
				return true;
		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int key, KeyEvent event){
		
		if(key == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                Interface.this.finish();
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}
		return super.onKeyDown(key, event);
	}
	
}