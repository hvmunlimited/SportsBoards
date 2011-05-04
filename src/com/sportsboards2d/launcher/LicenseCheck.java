package com.sportsboards2d.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.licensing.AESObfuscator;
import com.android.vending.licensing.LicenseChecker;
import com.android.vending.licensing.LicenseCheckerCallback;
import com.android.vending.licensing.ServerManagedPolicy;

import com.sportsboards2d.R;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class LicenseCheck extends Activity{
	
	private Handler mHandler;
	protected TextView mStatusText;
	protected Button mCheckLicenseButton;
	
	private LicenseCheckerCallback mLicenseCheckerCallback;
	private LicenseChecker mChecker;
	
	private static final String BASE64_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZ5q7VZE+Y5ZiGx5sFkHPNlfzLbrwt0bPlgszY27C7cUcagkrhCx/LhxYD79PdRseIE5tvSuasg/YDggVj5mdIXGIQEHGJNMMC9dOQ8LYFeEWSGY1ZvkT8Gv0eqCy96F7mqoCCFpaxYFOxI7b8gRKLMqVDhvxZXgGmYKwG33xWMQIDAQAB";
	private String android_id;
	private byte[]SALT = new byte[]{15, -43, -69, 107, 104, -111, -105, -76, -18, -24, 
									62, 118, 45, 67, -89, 70, 79, 123, -3, 13};

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 this.setContentView(R.layout.license);
		 mStatusText = (TextView) findViewById(R.id.status_text);
		 
	     mHandler = new Handler();
		 mLicenseCheckerCallback = new MyLicenseCheckerCallback();
		 android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
	     mChecker = new LicenseChecker(this, 
	    		 new ServerManagedPolicy(this,
	    				 new AESObfuscator(SALT, getPackageName(), android_id)),
	            BASE64_PUBLIC_KEY 
	     );
	     
	     doCheck();
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(this)
		.setTitle("Application Not Licensed")
		.setCancelable(false)
		.setMessage(
		"This application is not licensed. Please purchase it from Android Market")
		.setPositiveButton("Buy App",
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent marketIntent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + getPackageName()));
				startActivity(marketIntent);
				finish();
			}
		})
		.setNegativeButton("Exit",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int which) {
					finish();
				}
			}).create();
	}
	public void toast(String string) {
	Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
	private void doCheck() {
        mStatusText.setText("checking license");
        mChecker.checkAccess(mLicenseCheckerCallback);
    }
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mChecker.onDestroy();
	}
	
	 private void displayResult(final String result) {
	        mHandler.post(new Runnable() {
	            @Override
				public void run() {
	                mStatusText.setText(result);
	                setProgressBarIndeterminateVisibility(false);
	            }
	        });
	 }
	
	private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        @Override
		public void allow() {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            displayResult(getString(R.string.allow));
            startActivity(new Intent(LicenseCheck.this, Launcher.class));
            finish();
        }

        @Override
		public void dontAllow() {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            displayResult(getString(R.string.dont_allow));
            showDialog(0);
        }

		@Override
		public void applicationError(ApplicationErrorCode errorCode) {
			if (isFinishing()) {
				// Don't update UI if Activity is finishing.
				return;
				}
			String result = String.format(getString(R.string.application_error), errorCode);
            displayResult(result);
		}

	}
}