/**
 * 
 */
package com.sportsboards2d.boards;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import com.sportsboards2d.util.Constants;

import android.graphics.Color;
import android.view.KeyEvent;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class HockeyBoard extends BaseBoard {

	@Override
	public Engine onLoadEngine() {
		NUM_PLAYERS = 11;
		SPORT_NAME = "hockey";
		BALL_PATH_SMALL = "hockey_puck.png";
		return super.onLoadEngine();
	}
	@Override
	public void onLoadResources() {
		super.onLoadResources();	
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "hockey_rink.jpg", 0, 0);
		this.mMenuFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuFont = FontFactory.createFromAsset(this.mMenuFontTexture, this, "VeraBd.ttf", 36, true, Color.WHITE);
		
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, this.mMenuFontTexture);
		this.mEngine.getFontManager().loadFont(this.mMenuFont);
	}
	
	@Override
	protected void createMainMenu(){
		
		this.mMainMenu = new MenuScene(this.mCamera);
		
		final IMenuItem reset = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_RESET, this.mMenuFont, "Reset"), 1.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f);
		reset.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(reset);
		
		final IMenuItem lineEnable = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_LINE_ENABLE, this.mMenuFont, "Toggle Line Draw"), 1.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f);
		lineEnable.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(lineEnable);
		
		final IMenuItem clearLines = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_CLEARLINES, this.mMenuFont, "Clear Lines"), 1.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f);
		clearLines.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(clearLines);
		
		final IMenuItem save = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_SAVE, this.mMenuFont, "Save Formation"), 1.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f);
		save.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(save);
		
		final IMenuItem delete = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_DELETE, this.mMenuFont, "Delete Formation"), 1.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f);
		delete.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(delete);
		
		final IMenuItem load = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_LOAD, this.mMenuFont, "Load Formation"), 1.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f);
		load.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(load);

		final IMenuItem playback = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_PLAYBACK, this.mMenuFont, "Playback Mode"), 1.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f);
		playback.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(playback);
		
		this.mMainMenu.buildAnimations();
		this.mMainMenu.setBackgroundEnabled(false);
		this.mMainMenu.setOnMenuItemClickListener(this);
	}
	@Override
	public boolean onKeyDown(int key, KeyEvent event){
		
		if(key == KeyEvent.KEYCODE_MENU){
			
			if(this.mMainScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mMainMenu.back();
				this.mMainScene.getChild(BALL_LAYER).getChild(0).setVisible(true);

			} else {
				
				this.mMainScene.setChildScene(this.mMainMenu, false, true, true);
				this.mMainScene.getChild(BALL_LAYER).getChild(0).setVisible(false);
			}
			return true;
		}
		
		else{
			return super.onKeyDown(key, event);
		}
	}
	@Override
	public void onLoadComplete() {

	}
}
