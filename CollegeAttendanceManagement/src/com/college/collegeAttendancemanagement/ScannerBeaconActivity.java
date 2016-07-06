package com.college.collegeAttendancemanagement;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.college.virtualattendancesystem.utilities.AsyncT;
import com.college.virtualattendancesystem.utilities.BeaconScannerView;
import com.college.virtualattendancesystem.utilities.Constants;
import com.college.virtualattendancesystem.utilities.ResponseCallback;
import com.tcs.beaconmanager.Beacon;
import com.tcs.beaconmanager.BeaconListener;
import com.tcs.beaconmanager.BeaconManager;
import com.tcs.beaconmanager.ConnectionRequestModel;
import com.tcs.beaconmanager.IBeaconService;
import com.tcs.beaconmanager.Preferences;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScannerBeaconActivity extends FragmentActivity implements BeaconListener ,  ResponseCallback{
	private static final String TAG = "BeaconActivity";
	TextView text;
	ImageView pressAttendance;
	String id="";
	String name="";
	String email="";
	String courseName="";
	String classRoom="";
	String classId="";
	String courseId="";
	private static HashMap<String, String> beaconAddressNameMap =new HashMap<String, String>();
	
	/*static{
		beaconAddressNameMap.put("CB:D2:F5:5E:E1:ED", "B.E CSE B-Batch");
		beaconAddressNameMap.put("D4:24:F7:AB:F7:41", "B.E CSE IT");
		beaconAddressNameMap.put("F0:EA:1E:FD:54:99", "B.E CSE A-Batch");
	}*/
	
	private HashMap<String, Beacon> mBeacons;
	BeaconManager bm = null;
	BeaconScannerView beaconScanner = null;

	private boolean isConnected = false;
	// The service
	private IBeaconService service = null;

	// The service connection to talk to the service
	private ServiceConnection svcConn = new ServiceConnection() {

		// We register ourselves to the
		// service so that we can receive
		// updates
		public void onServiceConnected(ComponentName className, IBinder binder) {
			isConnected = true;
			service = (IBeaconService) binder;
			bm = service.getBeaconManager();
			bm.setBeaconsFilterList(beaconAddressNameMap);
			Map<String, Beacon> beacons = service.getCurrentBeacons();
			Log.i("ServiceConnection", "onServiceConnected - "+beacons);
			for(String beaconAddress : beacons.keySet()){
				Beacon beacon = beacons.get(beaconAddress);
				mBeacons.put(beacon.getAddress(), beacon);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
					}
				});
				int id = beaconScanner.drawNewBeacon(beaconAddressNameMap.get(beaconAddress));
				beacon.setTag(id);
				System.out.println("beacon"+mBeacons.size());
			}
			service.registerActivity(ScannerBeaconActivity.this, ScannerBeaconActivity.this);
			
		}

		public void onServiceDisconnected(ComponentName className) {
			isConnected = false;
			service = null;
			service.unregisterActivity(ScannerBeaconActivity.this);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar
  		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
  		//Remove notification bar
  		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setProgressBarIndeterminate(true);
		Log.i("MainActivity", "onCreate");
		
		 id = getIntent().getExtras().getString("id");
		 name = getIntent().getExtras().getString("name");
		 email = getIntent().getExtras().getString("email");
		
		 courseName = getIntent().getExtras().getString("courseName");
		 classRoom = getIntent().getExtras().getString("classRoom");
		 
		 classId = getIntent().getExtras().getString("classId");
		 courseId = getIntent().getExtras().getString("courseId");
		String beaconId = getIntent().getExtras().getString("beaconId");
		
		beaconAddressNameMap.put(beaconId, courseName+" - "+classRoom);
		setContentView(R.layout.activity_beacon);
		
		text=(TextView)findViewById(R.id.textView1);
		pressAttendance=(ImageView)findViewById(R.id.imageView1);
		beaconScanner = (BeaconScannerView) findViewById(R.id.scanner);
		
		
		pressAttendance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JSONObject parentData = new JSONObject();
		        JSONObject jsonobj = new JSONObject();
		        
		        try {
					jsonobj.put("courseId", courseId);
					jsonobj.put("courseName", courseName);
					jsonobj.put("classId", classId);
					jsonobj.put("ClassName", classRoom);
					jsonobj.put("userId", id);
					parentData.put("classroom",jsonobj);
					AsyncT task = new AsyncT(ScannerBeaconActivity.this,ScannerBeaconActivity.this, Constants.baseUrl+"enterAttedance", parentData,"enterAttedance");   
				    task.execute();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		} else {
		    if (!mBluetoothAdapter.isEnabled()) {
		        // Bluetooth is not enable :)
		    	Toast.makeText(this, "please enable Bluetooth", 1000).show();
		    }else{
		    	new Timer().schedule(new TimerTask() {
					
					@Override
					public void run() {
						startService(new Intent(ScannerBeaconActivity.this, BeaconManager.class));
						bindService(new Intent(ScannerBeaconActivity.this, BeaconManager.class), svcConn,
								BIND_AUTO_CREATE);
						
					}
				}, 1000);
		    }
		}
		
		
		
		
		mBeacons = new HashMap<String, Beacon>();

	}

	



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// bm.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Deactivate updates to us so that we dont get callbacks no more.
		//service.unregisterActivity(this);

		// Finally stop the service
		if(isConnected)
		unbindService(svcConn);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
 
	@Override
	public void onBeaconEntered(Beacon beacon) {
		Log.e(TAG, "onBeaconEntered - " + beacon);
		mBeacons.put(beacon.getAddress(), beacon);
		int id = beaconScanner.drawNewBeacon(beaconAddressNameMap.get(beacon.getAddress()));
		System.out.println("Beacon entered+++++++");
		text.setText("You are in class room! please put your attendance!");
		beacon.setTag(id);

	}
 
	@Override
	public void onBeaconExited(Beacon beacon) {
		Log.e(TAG,
				"onBeaconExited - " + beacon + "; Time Spent: "
						+ Math.round(beacon.getTimeSpent() / 1000) + "s");
		
		beaconScanner.removeBeacon((Integer) beacon.getTag());
		System.out.println("Beacon exit+++++++");
		text.setText("You are not in class room!");
		mBeacons.remove(beacon.getAddress());

	}

	@Override
	public void onUpdate(String beaconAddress, Beacon beacon) {

		if (mBeacons.get(beaconAddress) != null) {
			mBeacons.get(beaconAddress).updateBeaconWithNewInfo(beacon);
			int timeSpent = (int) Math.round((mBeacons.get(beaconAddress)
					.getLiveTimeSpent() / 1000));
			int lastServerHit = mBeacons.get(beaconAddress).getExtras()
					.getInt("last_update", 0);
			Log.i(TAG, "onUpdate - " + mBeacons.get(beaconAddress) + ";Time - "
					+ timeSpent);
			if (timeSpent >= 10 && timeSpent / 10 > lastServerHit) {
				ConnectionRequestModel model = new ConnectionRequestModel();
				Log.d(TAG, "Updating data for beacon " + beaconAddress);
				model.setUrl(Preferences.getUrl(this)+"/rpc/initiateUserState/"
						+ beaconAddress + "," + bm.getDeviceBTAddress());
			}

		}
	}





	@Override
	public void callbackCall(String response, String flag) {
		 try {
			 if(response!=null){
				    JSONObject jsonObj = new JSONObject(response);
				    String status=jsonObj.getString("status");
				    String message=jsonObj.getString("message");
				    String course=jsonObj.getString("course");
				    String classroom=jsonObj.getString("classroom");
				    String time=jsonObj.getString("time");
				    String subject=jsonObj.getString("subject");
				    
				    Intent doneAttendance=new Intent(ScannerBeaconActivity.this,Attendance.class);
				    doneAttendance.putExtra("status", status);
				    doneAttendance.putExtra("message", message);
				    doneAttendance.putExtra("course", course);
				    doneAttendance.putExtra("classroom", classroom);
				    doneAttendance.putExtra("time", time);
				    doneAttendance.putExtra("subject", subject);
				    startActivity(doneAttendance);
				    //finish();
			 }else{
				 Toast.makeText(ScannerBeaconActivity.this, "Connection error", 1000).show();
			 }
			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

	
}
