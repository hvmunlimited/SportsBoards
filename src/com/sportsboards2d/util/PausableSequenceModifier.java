/**
 * 
 */
package com.sportsboards2d.util;

import org.anddev.andengine.entity.modifier.IEntityModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.shape.IShape;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class PausableSequenceModifier<T> extends SequenceEntityModifier{
	
	private boolean mPaused = false;
	
	public PausableSequenceModifier(final IEntityModifier... pShapeModifiers) throws IllegalArgumentException {

		super(pShapeModifiers);

	}



	public PausableSequenceModifier(final IEntityModifierListener pShapeModifierListener, final IEntityModifier... pShapeModifiers) throws IllegalArgumentException {

		super(pShapeModifierListener, pShapeModifiers);

	}



	public PausableSequenceModifier(final IEntityModifierListener pShapeModifierListener, final ISubSequenceShapeModifierListener pSubSequenceShapeModifierListener, final IEntityModifier... pShapeModifiers) throws IllegalArgumentException {

		super(pShapeModifierListener, pSubSequenceShapeModifierListener, pShapeModifiers);

	}



	protected PausableSequenceModifier(final PausableSequenceModifier pSequenceShapeModifier) {

		super(pSequenceShapeModifier);

	}



	public void pause(boolean pState) {

		this.mPaused = pState;

	}



	public void onUpdate(final float pSecondsElapsed, final IShape pItem) {

		if(!this.mPaused) {

			super.onUpdate(pSecondsElapsed, pItem);

		}

	}

}
