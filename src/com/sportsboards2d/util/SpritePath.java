/**
 * 
 */
package com.sportsboards2d.util;

import org.anddev.andengine.entity.sprite.AnimatedSprite;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class SpritePath {
	
	private AnimatedSprite sprite;
	private PausablePathModifier.Path path;
	
	public SpritePath(AnimatedSprite sprite, PausablePathModifier.Path path1){
		this.setSprite(sprite);
		this.setPath(path1);
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	public AnimatedSprite getSprite() {
		return sprite;
	}

	public void setPath(PausablePathModifier.Path path) {
		this.path = path;
	}

	public PausablePathModifier.Path getPath() {
		return path;
	}

}
