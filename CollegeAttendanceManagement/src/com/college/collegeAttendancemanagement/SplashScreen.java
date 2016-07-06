package com.college.collegeAttendancemanagement;


import com.college.virtualattendancesystem.utilities.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.os.Handler;

public class SplashScreen extends Activity{
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		 setContentView(R.layout.splash);
		 
		 new Handler().postDelayed(new Runnable() {
			 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	            	
	            	SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
	                String restoredText = myPrefs.getString("host", null);
	                if (restoredText != null) {
				        Constants.baseUrl=restoredText+"api/v1/";
				        Constants.imageUrl=restoredText;
	                	   // This method will be executed once the timer is over
		                // Start your app main activity
		                Intent i = new Intent(SplashScreen.this, MainScreen.class);
		                startActivity(i);
		                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		 
		                // close this activity
		                finish();
	            	}else{
	            		// get prompts.xml view
	    				LayoutInflater li = LayoutInflater.from(SplashScreen.this);
	    				View promptsView = li.inflate(R.layout.prompts, null);

	    				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	    						SplashScreen.this);

	    				// set prompts.xml to alertdialog builder
	    				alertDialogBuilder.setView(promptsView);

	    				final EditText userInput = (EditText) promptsView
	    						.findViewById(R.id.editTextDialogUserInput);

	    				// set dialog message
	    				alertDialogBuilder
	    					.setCancelable(false)
	    					.setPositiveButton("OK",
	    					  new DialogInterface.OnClickListener() {
	    					    public void onClick(DialogInterface dialog,int id) {
	    						// get user input and set it to result
	    						// edit text
	    						//result.setText(userInput.getText());
	    					    	SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
	    					    	SharedPreferences.Editor editor = myPrefs.edit();
	    					    	editor.putString("host", userInput.getText().toString());
	    					    	editor.commit();
	    					    	
	    					        Constants.baseUrl=userInput.getText().toString()+"api/v1/";
	    					        Constants.base=userInput.getText().toString();
	    					        Constants.imageUrl=userInput.getText().toString();
	    					        
	    					        // This method will be executed once the timer is over
	    			                // Start your app main activity
	    			                Intent i = new Intent(SplashScreen.this, MainScreen.class);
	    			                startActivity(i);
	    			                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
	    			 
	    			                // close this activity
	    			                finish();
	    					    }
	    					  });
	    					

	    				// create alert dialog
	    				AlertDialog alertDialog = alertDialogBuilder.create();

	    				// show it
	    				alertDialog.show();
	            	}
	             
	            }
	        }, SPLASH_TIME_OUT);
	}

}
