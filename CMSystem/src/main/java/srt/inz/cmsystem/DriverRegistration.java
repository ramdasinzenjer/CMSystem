package srt.inz.cmsystem;

import java.net.URLEncoder;

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

public class DriverRegistration extends Activity{
	
	EditText enam,eemail,eph,eaddr,elice,eps,evehicle,enat;
	String snm,smail,sph,sadd,slice,sps,sveh,snat,response;
	Button breg;
	
	RadioGroup rggender;
	RadioButton rbgen;
	int selgen; String sgen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_reg);
		
		enam=(EditText)findViewById(R.id.et_nam);
		eemail=(EditText)findViewById(R.id.et_email);
		eph=(EditText)findViewById(R.id.et_phone);
		eaddr=(EditText)findViewById(R.id.et_addr);
		elice=(EditText)findViewById(R.id.et_lid);
		eps=(EditText)findViewById(R.id.et_ps);
		evehicle=(EditText)findViewById(R.id.et_vnum);		
		enat=(EditText)findViewById(R.id.et_nat);
		breg=(Button)findViewById(R.id.button_register);
		
		
	breg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					snm=enam.getText().toString();
					smail=eemail.getText().toString();
					sph=eph.getText().toString();
					sadd=eaddr.getText().toString();
					slice=elice.getText().toString();
					sps=eps.getText().toString();
					sveh=evehicle.getText().toString();
					snat=enat.getText().toString();
					
					rggender=(RadioGroup)findViewById(R.id.radioGroup1Usr);
					selgen=rggender.getCheckedRadioButtonId();
					rbgen=(RadioButton)findViewById(selgen);
					sgen=rbgen.getText().toString();
					
					if(smail.indexOf("@")!=-1)
				    {
				       if(sph.length()==10){
				    	   
				       
						 new DriverRegApi().execute();
				    	   
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
			
	}
	
	protected class DriverRegApi extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				urlParameters="name="+URLEncoder.encode(snm, "UTF-8")+
						"&&"+"email="+URLEncoder.encode(smail, "UTF-8")+
						"&&"+"phone="+URLEncoder.encode(sph, "UTF-8")+
						"&&"+"address="+URLEncoder.encode(sadd, "UTF-8")+
						"&&"+"license="+URLEncoder.encode(slice, "UTF-8")+
						"&&"+"vehiclenum="+URLEncoder.encode(sveh, "UTF-8")+
						"&&"+"nat="+URLEncoder.encode(snat, "UTF-8")+
						"&&"+"gender="+URLEncoder.encode(sgen, "UTF-8")+
						"&&"+"password="+URLEncoder.encode(sps, "UTF-8");
				
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}
			response=Connectivity.excutePost(Constants.DRIVER_REGISTER_URL,urlParameters);
			Log.e("You are at", "" + response);
			return response;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(response.contains("success"))
			{
				Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
	        	Intent i=new Intent(getApplicationContext(),DriverHome.class);
	    		startActivity(i);
	    		
			}
			else
			{
				Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}
