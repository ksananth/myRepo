package com.college.collegeAttendancemanagement;

import com.college.virtualattendancesystem.utilities.Constants;

public interface Config {

	
	// CONSTANTS
	static final String YOUR_SERVER_URL =  Constants.base+"api/gcm/register.php";
	//static final String YOUR_SERVER_URL =  "http://10.128.67.127/AttendanceSystem_preethi/api/gcm/register.php";
	// YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id
    static final String GOOGLE_SENDER_ID = "12451642097";  // Place here your Google project id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "You got a notification";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.ticketSystem.webview.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";
		
	
}
