package srt.inz.cmsystem;

import java.net.URLEncoder;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends Activity {

	EditText etu,etp; 
	String su,sp,styp,response;
	Button bl; TextView tr; Spinner sp_typ; ArrayAdapter<String> sadapter;
	ApplicationPreference appPreference;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        
        etu=(EditText)findViewById(R.id.edit_phone);
        etp=(EditText)findViewById(R.id.edit_pass);
        bl=(Button)findViewById(R.id.button_login);
        tr=(TextView)findViewById(R.id.txt_signup);
        sp_typ=(Spinner)findViewById(R.id.spinner_typ);
        appPreference=(ApplicationPreference)getApplication();
        
        String[] typ = getResources().getStringArray(R.array.type);
        
        sadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typ);
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_typ.setAdapter(sadapter);
	    sp_typ.setOnItemSelectedListener(new OnItemSelectedListener()
        {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				styp=arg0.getItemAtPosition(arg2).toString();
				((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
        	
        });
        
        tr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if(styp.equals("Driver"))
				{
				Intent i=new Intent(getApplicationContext(),DriverRegistration.class);
				startActivity(i);
				}
				else if(styp.equals("User"))
				{
					Intent i=new Intent(getApplicationContext(),UserRegistration.class);
					startActivity(i);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Select Type", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        
        bl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				su=etu.getText().toString();
				sp=etp.getText().toString();
				
				if(su.length()==0 ||sp.length()==0)
			    {
					Toast.makeText(getApplicationContext(), "Field vacant", Toast.LENGTH_SHORT).show();
			    }
			    else
			    {
			    	new LoginApi().execute();
			    }
				
			}
		});
        
    }
    
    protected class LoginApi extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters="username="+URLEncoder.encode(su, "UTF-8")+
						"&&"+"type="+URLEncoder.encode(styp, "UTF-8")+
						"&&"+"password="+URLEncoder.encode(sp, "UTF-8");
				
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}
			response=Connectivity.excutePost(Constants.LOGIN_URL,urlParameters);
			Log.e("You are at", "" + response);
			return response;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(MainPage.this, response, Toast.LENGTH_SHORT).show();
			if(response.contains("success"))
			{
				appPreference.setLoginStatus(true);
				appPreference.setId(su);
				Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
	        	if(styp.equals("Driver"))
	        	{
				Intent i=new Intent(getApplicationContext(),DriverHome.class);
	    		startActivity(i);
	        	}
	        	else
	        	{
	        		Intent i=new Intent(getApplicationContext(),UserHome.class);
		    		startActivity(i);
	        	}
	        	
			}
			else
			{
				Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
			}
		}
		
	}
    
    
}
