package com.sportsboards2d.boards;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.input.touch.detector.HoldDetector;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public abstract class Interface extends BaseGameActivity implements IOnMenuItemClickListener, IOnAreaTouchListener{
	
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final int CAMERA_WIDTH = 1024;
	protected static final int CAMERA_HEIGHT = 600;
	
	protected static final int BACKGROUND_LAYER = 0;
	protected static final int LINE_LAYER = 1;
	protected static final int PLAYER_LAYER = 2;
	protected static final int MENU_LAYER = 3;
	
	protected static final int MAIN_MENU_RESET = 0;
	protected static final int MAIN_MENU_CLEARLINES = 1;
	protected static final int MAIN_MENU_SETTINGS = 2;
	
	private static final int SETTINGS_MENU_GENERAL = 3;
	private static final int SETTINGS_MENU_LINE = 4;
	private static final int SETTINGS_MENU_PLAYER = 5;
	
	private static final int SETTINGS_LINE_ENABLE = 6;
	private static final int SETTINGS_LINE_COLOR = 7;
	private static final int SETTINGS_LINE_TYPE = 8;
	
	protected static final int SETTINGS_PLAYER_SIZE = 9;
	
	protected static final int PMENU_DELETE = 0;
	protected static final int PMENU_HIDE = 1;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected Camera mCamera;
	protected HoldDetector mHoldDetector;
	
	protected boolean LARGE_PLAYERS = true;
	protected boolean LINE_ENABLED = false;
	
	private Texture mMenuFontTexture;
	private Font mMenuFont;
	
	protected Scene mMainScene;
	protected MenuScene mMainMenu;
	private MenuScene mSettingsMenu;
	private MenuScene mLineSettingsMenu;
	private MenuScene mGeneralSettingsMenu;
	private MenuScene mPlayerSettingsMenu;
	
	@Override 
	public void onLoadResources(){
		
		FontFactory.setAssetBasePath("font/");

		this.mMenuFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuFont = FontFactory.createFromAsset(this.mMenuFontTexture, this, "Plok.ttf", 36, true, Color.RED);

		this.mEngine.getTextureManager().loadTexture(this.mMenuFontTexture);
		this.mEngine.getFontManager().loadFont(this.mMenuFont);
		
	}

	@Override
	public Scene onLoadScene() {
		
		mMainScene = new Scene(4);
		createMainMenu();
		createSettingsMenu();
		
		return mMainScene;
	}
	
	public void createMainMenu(){
	
		this.mMainMenu = new MenuScene(this.mCamera);
		
		final IMenuItem reset = new ColorMenuItemDecorator(new TextMenuItem(MAIN_MENU_RESET, this.mMenuFont, "Reset"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		reset.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(reset);
		final IMenuItem clearLines = new ColorMenuItemDecorator(new TextMenuItem(MAIN_MENU_CLEARLINES, this.mMenuFont, "Clear Lines"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		clearLines.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(clearLines);
		final IMenuItem settings = new ColorMenuItemDecorator(new TextMenuItem(MAIN_MENU_SETTINGS, this.mMenuFont, "Settings"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		settings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(settings);
		
		this.mMainMenu.buildAnimations();
		this.mMainMenu.setBackgroundEnabled(false);
		this.mMainMenu.setOnMenuItemClickListener(this);
	}
	
	public void createSettingsMenu(){
		
		this.mSettingsMenu = new MenuScene(this.mCamera);
		
		final IMenuItem genSettings = new ColorMenuItemDecorator(new TextMenuItem(SETTINGS_MENU_GENERAL, this.mMenuFont, "System Settings"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		genSettings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mSettingsMenu.addMenuItem(genSettings);
		
		final IMenuItem lineSettings = new ColorMenuItemDecorator(new TextMenuItem(SETTINGS_MENU_LINE, this.mMenuFont, "Line Settings"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		lineSettings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mSettingsMenu.addMenuItem(lineSettings);
		
		final IMenuItem playerSettings = new ColorMenuItemDecorator(new TextMenuItem(SETTINGS_MENU_PLAYER, this.mMenuFont, "Player Settings"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		playerSettings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mSettingsMenu.addMenuItem(playerSettings);
		
		this.mSettingsMenu.setMenuAnimator(new SlideMenuAnimator());
		this.mSettingsMenu.buildAnimations();
		this.mSettingsMenu.setBackgroundEnabled(false);
		this.mSettingsMenu.setOnMenuItemClickListener(this);
	}
	
	public void createLineSettingsMenu(){
		
	}
	
	public void createPlayerSettingsMenu(){
		
	}
	
	public void createGeneralSettingsMenu(){
		
	}
	
	@Override
	public boolean onKeyDown(int key, KeyEvent event){
		
		if(key == KeyEvent.KEYCODE_MENU){
			
			if(this.mMainScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mMainMenu.back();
			} else {
				/* Attach the menu. */
				this.mMainScene.setChildScene(this.mMainMenu, false, true, true);
			}
			return true;
		}
		
		else if(key == KeyEvent.KEYCODE_BACK){
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
		else {
			return super.onKeyDown(key, event);
		}
	}
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
		
			case MAIN_MENU_SETTINGS:
				
				//attach settings menu
				this.mMainMenu.setChildSceneModal(mSettingsMenu);
				
				return true;
				
			case SETTINGS_MENU_GENERAL:
				
				this.mSettingsMenu.back();
				//attach general settings menu
				return true;
				
			case SETTINGS_MENU_LINE:
				
				this.mSettingsMenu.back();
				//attach line settings menu
				return true;
				
				case SETTINGS_LINE_ENABLE:
					
					return true;
					
				case SETTINGS_LINE_COLOR:
					
					return true;
			
				case SETTINGS_LINE_TYPE:
					
					return true;
					
			case SETTINGS_MENU_PLAYER:
	
				this.mSettingsMenu.back();
	
				return true;
				
			default:
				return false;
		}
	}
}