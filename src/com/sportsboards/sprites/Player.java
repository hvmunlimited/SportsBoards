package com.sportsboards.sprites;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Player extends Sprite{
	
	private int number;
	private String name;
	private String initials;
	
	public Player(int num, String name, int startx, int starty, TextureRegion tex){
		super(startx, starty, tex);
	}
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		System.out.println(this.getX() + " " + this.getY());
		this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
		return true;
	}
	
}