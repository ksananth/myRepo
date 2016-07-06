package com.college.collegeAttendancemanagement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.college.virtualattendancesystem.utilities.*;

public class LoginScreenFragment extends Fragment  implements  ResponseCallback{
	final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
	ImageView imageView1;
    Button next;
    EditText txtRolNo;
    onNextSelected mCallback;
    Bitmap bitmap;
    ProgressDialog pDialog;
    Context ctx;
    RoundImage roundedImage;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface onNextSelected {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onNextSelected(String empNo);
    }

    private static void post(String endpoint, Map<String, String> params)
            throws IOException {   	
        
        URL url;
        try {
        	
            url = new URL(endpoint);
            
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        
        String body = bodyBuilder.toString();
        
        Log.v(Config.TAG, "Posting '" + body + "' to " + url);
        
        byte[] bytes = body.getBytes();
        
        HttpURLConnection conn = null;
        try {
        	
        	Log.e("URL", "> " + url);
        	
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            
            // handle the response
            int status = conn.getResponseCode();
            
            // If response is not success
            if (status != 200) {
            	
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
    
    public LoginScreenFragment(Context ctx) {
    	this.ctx=ctx;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateArticleView(mCurrentPosition);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (onNextSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void updateArticleView(int position) {
    	next = (Button) getActivity().findViewById(R.id.btnNext);
    	txtRolNo = (EditText) getActivity().findViewById(R.id.txtRolNo);
    	 imageView1 = (ImageView) getActivity().findViewById(R.id.imageView1);
         Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.photo);
         roundedImage = new RoundImage(bm);
         imageView1.setImageDrawable(roundedImage);
         
         
         next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Notify the parent activity of selected item
				if(txtRolNo.getText().toString().length()>0){
					
			        JSONObject parentData = new JSONObject();
			        JSONObject jsonobj = new JSONObject();
			        
			        try {
						jsonobj.put("email", txtRolNo.getText().toString());
						parentData.put("customer",jsonobj);
						AsyncT task = new AsyncT(getActivity(),LoginScreenFragment.this, Constants.baseUrl+"getProfileImage", parentData,"getProfileImage");   
					    task.execute();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					Toast.makeText(ctx, "Please Enter Username", Toast.LENGTH_SHORT).show();
				}
			}
		});
         
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

	@Override
	public void callbackCall(String response,String flag) {
		String user_profile_pic="";
		String status="";
		String message="";
		 try {
			 if(response!=null){
				    JSONObject jsonObj = new JSONObject(response);
					user_profile_pic=jsonObj.getString("user_profile_pic");
					status=jsonObj.getString("status");
					message=jsonObj.getString("message"); 
			 }else{
				 Toast.makeText(getActivity().getApplicationContext(), "Connection error", 1000).show();
			 }
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 System.out.println("response"+response);
		 System.out.println("status"+status);
		 System.out.println("user_profile_pic"+user_profile_pic);
		 System.out.println("message"+message);
		if(status.equalsIgnoreCase("success")){
			message="Login successful!";
			Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
			mCallback.onNextSelected(txtRolNo.getText().toString());
			//new LoadImage().execute(Constants.imageUrl+user_profile_pic);
		}else{
			message="Login Unsuccessful!";
			 Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
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

