package srt.inz.cmsystem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.cmsystem.UserRegistration.UserRegApi;
import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Profileup extends Activity{
	
	String resp,upresp;
		
	EditText enam,eemail,eph,eaddr,eun,eps,enat;
	String snm,smail,sph,sadd,sps,snat;
	
	Button bupdate;
	
	String valuedrid,valuepdrt,valueprdtyp,valuestatus,valuedest,valuetdate,valuedlg;
	
	ApplicationPreference apprPreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prof_up);
		enam=(EditText)findViewById(R.id.et01_nam);
		eemail=(EditText)findViewById(R.id.et01_email);
		eph=(EditText)findViewById(R.id.et01_phone);
		eaddr=(EditText)findViewById(R.id.et01_addr);
		eun=(EditText)findViewById(R.id.et01_un);
		eps=(EditText)findViewById(R.id.et01_ps);	
		enat=(EditText)findViewById(R.id.et01_nat);
		bupdate=(Button)findViewById(R.id.button01_updtepro);
		
		bupdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				snm=enam.getText().toString();
				smail=eemail.getText().toString();
				sph=eph.getText().toString();
				sadd=eaddr.getText().toString();
				
				sps=eps.getText().toString();
				snat=enat.getText().toString();
				
				
				if(smail.indexOf("@")!=-1)
			    {
			       if(sph.length()==10){
			    	   
			       
				new ProfileupApiTask().execute();
			    	   
			       }
			       else{
			    	   eph.setError("Please enter valid phone number");
			       }
			    }
			    else
			    {
			    	eemail.setError("Please enter valid email id");
			    }
			}
		});
		
		eemail.setEnabled(false);
		eun.setEnabled(false);
		
		apprPreference=(ApplicationPreference)getApplication();
		new MyProfDataFromdb().execute();
		
	}

	protected class MyProfDataFromdb extends AsyncTask<String, String, String>
	
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters = null;
	        try {
	            urlParameters =  "username=" + URLEncoder.encode(apprPreference.getId(), "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        resp = Connectivity.excutePost(Constants.MYPROFILE_URL,
	                urlParameters);
	        Log.e("You are at", "" + resp);

	        return resp;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
	
		keyparser();
		
		}
		
	}
	
	public void keyparser()
	{
		try
		{
			JSONObject  jObject = new JSONObject(resp);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1= ja.getJSONObject(i);
				String name=data1.getString("name");
				String address=data1.getString("address");
				String nationality=data1.getString("nationality");
				String email=data1.getString("email");
				String phone=data1.getString("phone");
				String 	password=data1.getString("password");
				
				eun.setText(apprPreference.getId());
				enam.setText(name); eemail.setText(email); enat.setText(nationality);
				eaddr.setText(address); eph.setText(phone); eps.setText(password);
				
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
	
	public class ProfileupApiTask extends AsyncTask<String,String,String> {
	    
	    @Override
	    protected String doInBackground(String... params) {


	            String urlParameters = null;
	            try {
	                urlParameters = "name="+URLEncoder.encode(snm, "UTF-8")+
							"&&"+"email="+URLEncoder.encode(smail, "UTF-8")+
							"&&"+"phone="+URLEncoder.encode(sph, "UTF-8")+
							"&&"+"address="+URLEncoder.encode(sadd, "UTF-8")+
							"&&"+"username="+URLEncoder.encode(apprPreference.getId(), "UTF-8")+						
							"&&"+"nat="+URLEncoder.encode(snat, "UTF-8")+
							"&&"+"password="+URLEncoder.encode(sps, "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }		

	            upresp = Connectivity.excutePost(Constants.MYPROFILEUP_URL,
	                    urlParameters);
	            Log.e("You are at", "" + upresp);

	       return upresp;
	    }

	    @Override
	    protected void onPostExecute(String s) {
	        super.onPostExecute(s);
	        
	        if(upresp.contains("success"))
	        {
	        	
	        	Intent i=new Intent(getApplicationContext(),UserHome.class);
	    		startActivity(i);
	    	    
	        Toast.makeText(getApplicationContext(), ""+upresp, Toast.LENGTH_SHORT).show();
	        
	        
	        }
	        else
	        {
	        	Toast.makeText(getApplicationContext(), ""+upresp, Toast.LENGTH_SHORT).show();
	        }
	        
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	    }
	}
	
	
}
