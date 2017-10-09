package srt.inz.cmsystem;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReqTransService extends Activity{
	EditText et0,et1,et2,et3; TextView tv1;
	Spinner spn; ArrayAdapter<String> myadapter; String styp;
	
	String spnam,sht,swt,saaddr,shvid,resp,sdate,sh_state;
	Button bpro;
	
	CheckBox chb;Boolean b_share;
	
	ApplicationPreference apPreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reqtransport);
		
		et0=(EditText)findViewById(R.id.editText_prdtnam);
		et1=(EditText)findViewById(R.id.editText_height);
		et2=(EditText)findViewById(R.id.editText_weight);
		et3=(EditText)findViewById(R.id.editText_addr);
		
		tv1=(TextView)findViewById(R.id.text_agencynam);
		bpro=(Button)findViewById(R.id.button_proceed);
		chb=(CheckBox)findViewById(R.id.checkBoxSharing);
		spn=(Spinner)findViewById(R.id.spinnerText_typ);
		
		apPreference=(ApplicationPreference)getApplication();	
		
		SharedPreferences sharv=getSharedPreferences("mvid_01", MODE_WORLD_READABLE);
		shvid=sharv.getString("vehid", "");
		
		tv1.setText(shvid);
		
		String[] stp= getResources().getStringArray(R.array.cargo_type);
        
        myadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stp);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spn.setAdapter(myadapter);
	    spn.setOnItemSelectedListener(new OnItemSelectedListener()
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
	    
	    bpro.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar calobj = Calendar.getInstance();
				sdate=df.format(calobj.getTime());
				
				spnam=et0.getText().toString();
				sht=et1.getText().toString();
				swt=et2.getText().toString();
				saaddr=et3.getText().toString();
				//system date should be fetch
				new ReqSendApi().execute();
				
			}
		});
	    
	    chb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuffer result=new StringBuffer();
				result.append("Location Service: ").append(chb.isChecked());
							
				if(chb.isChecked()==true)
				{
					sh_state="1";					
				}
				else
				{
					sh_state="0";
				}
					Toast.makeText(getApplicationContext(), 
							result.toString(),Toast.LENGTH_SHORT).show();
				
			}
		});
		
		
	}
	
	@SuppressLint("ShowToast") protected class ReqSendApi extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters=null;
			try {
				
			urlParameters="productname="+URLEncoder.encode(spnam,"UTF-8")
					+"&&"+ "username="+URLEncoder.encode(apPreference.getId(),"UTF-8")
					+"&&"+ "driver_id="+URLEncoder.encode(shvid,"UTF-8")
					+"&&"+ "height="+URLEncoder.encode(sht,"UTF-8")
					+"&&"+ "weight="+URLEncoder.encode(swt,"UTF-8")
					+"&&"+ "producttype="+URLEncoder.encode(styp,"UTF-8")
					+"&&"+ "destination="+URLEncoder.encode(saaddr,"UTF-8")
					+"&&"+ "transferdate="+URLEncoder.encode(sdate,"UTF-8")
					+"&&"+ "src="+URLEncoder.encode(apPreference.getMyLocation(),"UTF-8")
					+"&&"+ "share_state="+URLEncoder.encode(sh_state,"UTF-8");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			resp=Connectivity.excutePost(Constants.USERREQUEST_URL, urlParameters);
			Log.e("You are at ReqTransAsyncTask", resp);
			return resp;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(resp.contains("success"))
			{
				Toast.makeText(getApplicationContext(), 
						"Successfully requested",Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), ""
						+resp, Toast.LENGTH_SHORT).show();
		
			}
		}
		
	}
	

}
