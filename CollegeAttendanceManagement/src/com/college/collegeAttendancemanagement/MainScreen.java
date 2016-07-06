package com.college.collegeAttendancemanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.college.collegeAttendancemanagement.PasswordScreenFragment.onLoginClicked;
import com.college.collegeAttendancemanagement.R;
import com.google.android.gcm.GCMRegistrar;


public class MainScreen extends FragmentActivity implements com.college.collegeAttendancemanagement.LoginScreenFragment.onNextSelected,onLoginClicked{

	Controller aController;
	AsyncTask<Void, Void, Void> mRegisterTask;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
      		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      		//Remove notification bar
      		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
       
        

        //registerGCM("dd","ff","ff","ee");
     // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            LoginScreenFragment firstFragment = new LoginScreenFragment(this);

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }
    
    
    public void registerGCM(final String email,final String name,final String id,final String msg){
    	
    	//name="dd";
    	//email="dd";
    	//id="d";
    	//msg="ff";
    	Log.e("JavaScriptInterface", email+"----------"+name);
    	//progress = ProgressDialog.show(activity, "Registration",
    	//	    "In progress", true);
    	//progress.setCancelable(true);
    	
    	//Get Global Controller Class object (see application tag in AndroidManifest.xml)
    			aController = (Controller) getApplicationContext();
    			
    			
    			// Check if Internet present
    			if (!aController.isConnectingToInternet()) {
    				
    				// Internet Connection is not present
    				aController.showAlertDialog(this,
    						"Internet Connection Error",
    						"Please connect to Internet connection", false);
    				// stop executing code by return
    				return;
    			}
    			
    					
    			
    			// Make sure the device has the proper dependencies.
    			GCMRegistrar.checkDevice(this.getApplicationContext());

    			// Make sure the manifest permissions was properly set 
    			GCMRegistrar.checkManifest(this.getApplicationContext());

    			//lblMessage = (TextView) findViewById(R.id.lblMessage);
    			
    			// Register custom Broadcast receiver to show messages on activity
    			this.registerReceiver(mHandleMessageReceiver, new IntentFilter(
    					Config.DISPLAY_MESSAGE_ACTION));
    			
    			
    			// Get GCM registration id
    			final String regId = GCMRegistrar.getRegistrationId(this.getApplicationContext());
    			Log.e("regId", "regId--"+regId);
    			
    			
    			// Check if regid already presents
    			if (regId.equals("")) {
    				
    				// Register with GCM			
    				GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
    				}
    			//} else {
    				
    				// Device is already registered on GCM Server
    				//if (GCMRegistrar.isRegisteredOnServer(activity)) {
    					
    					// Skips registration.				
    					//Toast.makeText(activity.getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();
    				
    				//} else {
    					
    					// Try to register again, but not in the UI thread.
    					// It's also necessary to cancel the thread onDestroy(),
    					// hence the use of AsyncTask instead of a raw thread.
    					
    					final Context context = this;
    					mRegisterTask = new AsyncTask<Void, Void, Void>() {

    						@Override
    						protected Void doInBackground(Void... params) {
    							
    		    				
    							// Register on our server
    							// On server creates a new user
    							aController.register(getApplicationContext(), name, email,id, regId,msg);
    							
    							return null;
    						}

    						@Override
    						protected void onPostExecute(Void result) {
    							mRegisterTask = null;
    							//progress.dismiss();
    						}

    					};
    					
    					// execute AsyncTask
    					mRegisterTask.execute(null, null, null);
    				//}
    			//}
    			
    			
    	//CallBackWebview ie=new ShowWebView(webview);
    	//ie.sendRegId("jj");
    }
    
    // Create a broadcast receiver to get message and show on screen 
 	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
 		
 		@Override
 		public void onReceive(Context context, Intent intent) {
 			
 			String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
 			
 			// Waking up mobile if it is sleeping
 			aController.acquireWakeLock(getApplicationContext());
 			
 			// Display message on the screen
 			//lblMessage.append(newMessage + "\n");			
 			
 			Toast.makeText(getApplicationContext(), "" + newMessage, Toast.LENGTH_LONG).show();
 			
 			// Releasing wake lock
 			aController.releaseWakeLock();
 		}
 	};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onNextSelected(String empNo) {
		PasswordScreenFragment newFragment = new PasswordScreenFragment(this);
          Bundle args = new Bundle();
          args.putString(PasswordScreenFragment.EMP_NO, empNo);
          
          newFragment.setArguments(args);
          FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
          transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,R.anim.enter_from_right, R.anim.exit_to_left);
          // Replace whatever is in the fragment_container view with this fragment,
          // and add the transaction to the back stack so the user can navigate back
          transaction.replace(R.id.fragment_container, newFragment);
         
          transaction.addToBackStack(null);

          // Commit the transaction
          transaction.commit();
		
	}

	@Override
	public void onLoginClicked(String password,String email,String name,String id,String courseName,String classRoom,String beaconId,String classId,String courseId) {
		registerGCM(email,name,id,"Login SuccessFul");
		Intent homeIntent = new Intent(MainScreen.this,ScannerBeaconActivity.class);
		homeIntent.putExtra("id",id);
		homeIntent.putExtra("name",name);
		homeIntent.putExtra("email",email);
		homeIntent.putExtra("courseName",courseName);
		homeIntent.putExtra("classRoom",classRoom);
		homeIntent.putExtra("beaconId",beaconId);
		homeIntent.putExtra("classId",classId);
		homeIntent.putExtra("courseId",courseId);
		startActivity(homeIntent);
	}
}
