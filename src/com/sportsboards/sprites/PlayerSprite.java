package com.sportsboards.sprites;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.sportsboards.db.PlayerInfo;

public class PlayerSprite extends Sprite{
	
	private PlayerInfo pInfo;
	public PlayerInfo getPlayerInfo(){ return pInfo;}
	protected boolean mGrabbed;
	
	public PlayerSprite(PlayerInfo pInfo, float startx, float starty, TextureRegion tex){
		super(startx, starty, tex);
		this.pInfo = pInfo;
	}
	
	public void setTexture(TextureRegion tex){
		
		this.setTexture(tex);
		
	}
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.setScale(2.0f);
				//System.out.println(this.getX() + " , " + this.getY());

				this.mGrabbed = true;
				break;
			case TouchEvent.ACTION_MOVE:
				if(this.mGrabbed) {
					this.setPosition(pSceneTouchEvent.getX() - 48 / 2, pSceneTouchEvent.getY() - 48 / 2);
					//System.out.println(this.getX() + " , " + this.getY());
				}
				break;
			case TouchEvent.ACTION_UP:
				if(this.mGrabbed) {
					this.mGrabbed = false;
					this.setScale(1.0f);
					System.out.println(this.getX() + " , " + this.getY());
				}
				break;
		}
		return true;
	}
	
}