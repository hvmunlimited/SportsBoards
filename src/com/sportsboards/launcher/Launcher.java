package com.sportsboards.launcher;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.vending.licensing.LicenseCheckerCallback;
import com.android.vending.licensing.LicenseChecker;
import com.android.vending.licensing.ServerManagedPolicy;
import com.android.vending.licensing.AESObfuscator;
import com.sportsboards.R;


public class Launcher extends ListActivity{
	
	private MenuAdapter mListAdapter;
	private View mHandler;
	protected TextView mStatusText;
	protected View mCheckLicenseButton;
	
	private LicenseCheckerCallback mLicenseCheckerCallback;
	private LicenseChecker mChecker;
	
	private static final String BASE64_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZ5q7VZE+Y5ZiGx5sFkHPNlfzLbrwt0bPlgszY27C7cUcagkrhCx/LhxYD79PdRseIE5tvSuasg/YDggVj5mdIXGIQEHGJNMMC9dOQ8LYFeEWSGY1ZvkT8Gv0eqCy96F7mqoCCFpaxYFOxI7b8gRKLMqVDhvxZXgGmYKwG33xWMQIDAQAB";
	private String android_id;
	private byte[]SALT = new byte[]{15, -43, -69, 107, 104, -111, -105, -76, -18, -24, 
									62, 118, 45, 67, -89, 70, 79, 123, -3, 13};

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 
		 mLicenseCheckerCallback = new MyLicenseCheckerCallback();
		 android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
	        // Construct the LicenseChecker with a Policy.
	     mChecker = new LicenseChecker(this, 
	    		 new ServerManagedPolicy(this,
	    				 new AESObfuscator(SALT, getPackageName(), android_id)),
	            BASE64_PUBLIC_KEY  // Your public licensing key.
	     );
	       

		 
		 
		 this.setContentView(R.layout.launcher_menu);
		 mListAdapter = new MenuAdapter(this); 
		 this.setListAdapter(mListAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id){
		super.onListItemClick(lv, v, position, id);
		Activity act = (Activity) this.getListAdapter().getItem(position);
		this.startActivity(new Intent(this, act.Class));
	}
	
	 private void displayResult(final String result) {
	        mHandler.post(new Runnable() {
	            public void run() {
	                mStatusText.setText(result);
	                setProgressBarIndeterminateVisibility(false);
	                mCheckLicenseButton.setEnabled(true);
	            }
	        });
	 }
	
	private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        public void allow() {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            displayResult(getString(R.string.allow));
        }

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
				//toast("Error: " + errorCode.name());
		}

	}
}