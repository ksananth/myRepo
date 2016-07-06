package com.college.collegeAttendancemanagement;

import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.college.collegeAttendancemanagement.R;
import com.college.collegeAttendancemanagement.LoginScreenFragment.LoadImage;
import com.college.virtualattendancesystem.utilities.AsyncT;
import com.college.virtualattendancesystem.utilities.Constants;
import com.college.virtualattendancesystem.utilities.ResizeBitmap;
import com.college.virtualattendancesystem.utilities.ResponseCallback;
import com.college.virtualattendancesystem.utilities.RoundImage;

public class PasswordScreenFragment extends Fragment   implements  ResponseCallback{
	
	final static String EMP_NO = "position";
	ImageView imageView1;
	Button login;
	Bitmap bitmap;
	EditText password;
	RoundImage roundedImage;
	
	onLoginClicked mCallback;
	ProgressDialog pDialog;
    Context ctx;
    String roleNum;


    // The container Activity must implement this interface so the frag can deliver messages
    public interface onLoginClicked {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onLoginClicked(String password,String email,String name,String id,String courseName,String classRoom,String beaconId,String classId,String courseId);
    }
    
    public PasswordScreenFragment(Context ctx) {
		this.ctx=ctx;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.password_fragment_view, container, false);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (onLoginClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        updateArticleView(1);
    }

    public void updateArticleView(int position) {
    	login = (Button) getActivity().findViewById(R.id.btnNext);
    	password = (EditText) getActivity().findViewById(R.id.txtRolNo);
    	 imageView1 = (ImageView) getActivity().findViewById(R.id.imageView1);
         Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.photo);
         roundedImage = new RoundImage(bm);
         imageView1.setImageDrawable(roundedImage);
         
         Bundle bundle = getArguments();
         roleNum = bundle.getString(PasswordScreenFragment.EMP_NO);
         System.out.println("str---"+roleNum);
         
	        JSONObject parentData = new JSONObject();
	        JSONObject jsonobj = new JSONObject();
	        
	        try {
				jsonobj.put("email", roleNum);
				parentData.put("customer",jsonobj);
				AsyncT task = new AsyncT(getActivity(),PasswordScreenFragment.this, Constants.baseUrl+"/getProfileImage", parentData,"getProfileImage");   
			    task.execute();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
         login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
		        
		        String pass= password.getText().toString();
		        if(pass.length()>0){
					
			        JSONObject parentData = new JSONObject();
			        JSONObject jsonobj = new JSONObject();
			        
			        try {
						jsonobj.put("email", pass);
						jsonobj.put("password", pass);
						parentData.put("customer",jsonobj);
						AsyncT task = new AsyncT(getActivity(),PasswordScreenFragment.this, Constants.baseUrl+"/login", parentData,"login");   
					    task.execute();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					Toast.makeText(ctx, "Please enter password", Toast.LENGTH_SHORT).show();
				}
			}
		});
         
    }

	@Override
	public void callbackCall(String response,String flag) {
		if(flag.equalsIgnoreCase("login")){
			String status="",email="",user_login="",uid="",courseName="",classRoom="",beaconId="",courseId="",classId="";
			
			 try {
				JSONObject jsonObj = new JSONObject(response);
				status=jsonObj.getString("status");
				System.out.println("status--"+status);
				if(status.equalsIgnoreCase("success")){
					user_login=jsonObj.getString("user_login");
					uid=jsonObj.getString("uid");
					email=jsonObj.getString("email");
					
					courseName=jsonObj.getString("courseName");
					classRoom=jsonObj.getString("classRoom");
					beaconId=jsonObj.getString("beaconId");
					
					classId=jsonObj.getString("classId");
					courseId=jsonObj.getString("courseId");
					
					Toast.makeText(ctx, "Login Successfull!", Toast.LENGTH_SHORT).show();
					// Notify the parent activity of selected item
			        mCallback.onLoginClicked("489182",email,user_login,uid,courseName,classRoom,beaconId,classId,courseId);
				}else{
					Toast.makeText(ctx, "Login UnSuccessfull!", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else if(flag.equalsIgnoreCase("getProfileImage")){
			String user_profile_pic="";
			 try {
				JSONObject jsonObj = new JSONObject(response);
				user_profile_pic=jsonObj.getString("user_profile_pic");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			new LoadImage().execute(Constants.imageUrl+user_profile_pic);
		}

		
	}
	
	public class LoadImage extends AsyncTask<String, String, Bitmap> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(ctx);
	        pDialog.setMessage("Loading Image ....");
	        pDialog.show();

	    }
	     protected Bitmap doInBackground(String... args) {
	         try {
	        	 System.out.println("sdsfsf"+args[0]);
	               bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

	        } catch (Exception e) {
	              e.printStackTrace();
	        }
	        return bitmap;
	     }

	     protected void onPostExecute(Bitmap image) {

	         if(image != null){
	        	 
	        	 Bitmap rezizedImage=new ResizeBitmap().getResizedBitmap(image, 350, 350);
	        	 roundedImage = new RoundImage(rezizedImage);
	        	 imageView1.setImageDrawable(roundedImage);
	         pDialog.dismiss();

	         }else{

	         pDialog.dismiss();
	         Toast.makeText(ctx, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

	         }
	     }
	 }

}
