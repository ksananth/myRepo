package com.college.collegeAttendancemanagement;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Attendance extends Activity{
TextView  editText2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.activity_attendance);
		
		String status = getIntent().getExtras().getString("status");
		String message = getIntent().getExtras().getString("message");
		String course = getIntent().getExtras().getString("course");
		String classroom = getIntent().getExtras().getString("classroom");
		String time = getIntent().getExtras().getString("time");
		String subject = getIntent().getExtras().getString("subject");
		
		editText2 = (TextView)findViewById(R.id.editText2);
		editText2.setText("Attendance done  "+"@ "+time +" in classroom-"+course+ " "+classroom);
	}
	
	@Override
	public void onBackPressed() {
	    // do nothing.
	}
}
