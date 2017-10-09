package srt.inz.cmsystem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DriverHome extends Activity implements LocationListener{
	
	String resp;
	String stplace; double latitude,longitude; Location location;
	
	ListView mlist; ListAdapter adapter;
	
	ArrayList<HashMap<String, String>> oslist=new ArrayList<HashMap<String,String>>();	
	String valueurid,valuepdrt,valueprdtyp,valuestatus,valuedest,valuetdate,
	valuedlg,valueshstate,valuesrc,response;
	
	ApplicationPreference apprPreference;
	
	CheckBox chbls,chbshare;Boolean b_lserv,b_share; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_home);
		mlist=(ListView)findViewById(R.id.listreqsts);
		chbls=(CheckBox)findViewById(R.id.checkBoxlcserv);
		chbshare=(CheckBox)findViewById(R.id.CheckBoxlcshare);
		apprPreference=(ApplicationPreference)getApplication();
		getmyloc();
		
		if(apprPreference.getServiceStatus())
		{
			chbls.setChecked(true);
			startService(new Intent(getApplicationContext(), srt.inz.cmsystem.SrtMyService.class));
		}
		else
		{
			chbls.setChecked(false);
			stopService(new Intent(getApplicationContext(), srt.inz.cmsystem.SrtMyService.class));

		}
		
		if(apprPreference.getShareServiceStatus())
		{
			chbshare.setChecked(true);
		}
		else
		{
			chbshare.setChecked(false);
		}
		
		 chbls.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					StringBuffer result=new StringBuffer();
					result.append("Location Service: ").append(chbls.isChecked());
								
					if(chbls.isChecked()==true)
					{
						apprPreference.setServiceStatus(true);	
						
					}
					else
					{
						apprPreference.setServiceStatus(false);	
					}
						Toast.makeText(getApplicationContext(), 
								result.toString(),Toast.LENGTH_SHORT).show();
					
				}
			});
		 chbshare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					StringBuffer result=new StringBuffer();
					result.append("Sharing Service: ").append(chbshare.isChecked());
								
					if(chbshare.isChecked()==true)
					{
						apprPreference.setShareServiceStatus(true);					
					}
					else
					{
						apprPreference.setShareServiceStatus(false);
					}
						Toast.makeText(getApplicationContext(), 
								result.toString(),Toast.LENGTH_SHORT).show();
					
				}
			});
		
		
		new MyDrRequestDataFromdb().execute();
		
	}

	protected class MyDrRequestDataFromdb extends AsyncTask<String, String, String>
	
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlParameters = null;
	        try {
	            urlParameters =  "driver_id=" + URLEncoder.encode(apprPreference.getId(), "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        resp = Connectivity.excutePost(Constants.MYREQLISTD_URL,
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
			oslist.clear();
			mlist.setAdapter(null);
			
			JSONObject  jObject = new JSONObject(resp);
			JSONObject  jObject1 = jObject.getJSONObject("Event");
			JSONArray ja = jObject1.getJSONArray("Details"); 
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1= ja.getJSONObject(i);
				String username=data1.getString("username");
				String productname=data1.getString("productname");
				String producttype=data1.getString("producttype");
				String destination=data1.getString("destination");
				String transferdate=data1.getString("transferdate");
				String status=data1.getString("status");
				String share_state=data1.getString("share_state");
				String src=data1.getString("src");
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("username", username);
	            map.put("productname", productname);
	            map.put("producttype", producttype);
	            map.put("destination", destination);
	            map.put("transferdate", transferdate);
	            map.put("status", status);
	            map.put("share_state", share_state);
	            map.put("src", src);
	            
	            map.put("notification", "Request from "+username+" on date : "+transferdate+"."+
	            "\n  To :"+destination+".");
	            
	            map.put("dalogmsg", "From : "+src+"\n Destination :"+destination+"\n Driver id :"+username+""
	            		+"\t Status :"+status+"\n Sharing State : "+share_state );
	            
	            //Toast.makeText(getApplicationContext(), ""+sname,Toast.LENGTH_SHORT).show();
	            oslist.add(map);
	            
	            adapter = new SimpleAdapter(getApplicationContext(), oslist,
	                R.layout.layout_single,
	                new String[] {"notification"}, new int[] {R.id.mtext_single});
	            mlist.setAdapter(adapter);
	            
	            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	               @SuppressWarnings("unused")
				@Override
	               public void onItemClick(AdapterView<?> parent, View view,
	                                            int position, long id) {               
	               Toast.makeText(getApplicationContext(), " "+oslist.get(+position).get("transferdate"), Toast.LENGTH_SHORT).show();
	               
	               valueurid=oslist.get(+position).get("username");
	                valuepdrt=oslist.get(+position).get("productname"); 
	               valueprdtyp=oslist.get(+position).get("producttype");
	               valuedest=oslist.get(+position).get("destination");
	               valuetdate=oslist.get(+position).get("transferdate");
	               valuestatus=oslist.get(+position).get("status");
	               valuedlg=oslist.get(+position).get("dalogmsg");
	               valueshstate=oslist.get(+position).get("share_state");
	               valuesrc=oslist.get(+position).get("src");
	               
	               if(valueshstate.equals("0"))
	               {
	            	   apprPreference.setShareServiceStatus(false);
	            	   
	               }
	               {
	            	   apprPreference.setShareServiceStatus(true);
	            	   
	               }
	               
	               
	               openDialog();
	               }
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	
	public void openDialog(){
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    
   if(valuestatus.equals("0")) 
   
  	 {
  	 alertDialogBuilder.setTitle("Please choose an action!");
	      alertDialogBuilder.setMessage(""+valuedlg);
  	 alertDialogBuilder.setPositiveButton("Navigate", new DialogInterface.OnClickListener() {
       @Override
       public void onClick(DialogInterface arg0, int arg1) {
          
          Toast.makeText(getApplicationContext(),"Navigating...",Toast.LENGTH_SHORT).show();
        //  new StatuschangeApiTask().execute();
          Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
      		    Uri.parse("google.navigation:q="+valuesrc));
          startActivity(intent);
      
       }
    });
    
    alertDialogBuilder.setNegativeButton("Accept",new DialogInterface.OnClickListener() {
       @Override
       public void onClick(DialogInterface dialog, int which) {
      	
      	 Toast.makeText(getApplicationContext(),"Ok.",Toast.LENGTH_SHORT).show();
      	 new ServiceAcceptApi().execute();
      	 //finish();
       }
    });
    
   
  	 }
   else
   {
  	 alertDialogBuilder.setTitle("Drive!!!");
	      alertDialogBuilder.setMessage("This service is already accepted.");
  	 alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {

	         }
	      });
   }
    
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
 }
	
	public void m_call(String number)
	  {
		  Intent phoneIntent = new Intent(Intent.ACTION_CALL); 
			phoneIntent.setData(Uri.parse("tel:"+number));
		      try{
		         startActivity(phoneIntent);
		      }
		      
		      catch (android.content.ActivityNotFoundException ex){
		         Toast.makeText(getApplicationContext(),"Error in call facility",Toast.LENGTH_SHORT).show();
		      }
	  }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Geocoder gc= new Geocoder(this, Locale.ENGLISH);
        // Getting latitude of the current location
        latitude =  location.getLatitude();

        // Getting longitude of the current location
        longitude =  location.getLongitude();

try {
	List<Address> addresses = gc.getFromLocation(latitude,longitude, 1);
	
	if(addresses != null) {
		Address returnedAddress = addresses.get(0);
		StringBuilder strReturnedAddress = new StringBuilder("");
		for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) 
		{
			strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
			
		}
	
		stplace=strReturnedAddress.toString().trim();
		Toast.makeText( getBaseContext(),stplace,Toast.LENGTH_SHORT).show();
		apprPreference.setMyLocation(stplace);
		apprPreference.setMyLocationLat(String.valueOf(latitude));
		apprPreference.setMyLocationLon(String.valueOf(longitude));
		
	}

	else{
		Toast.makeText(getBaseContext(),"GPS Disabled",Toast.LENGTH_SHORT).show();
	
	}
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void getmyloc()
	{
		try {
						
			 LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			    // Creating a criteria object to retrieve provider
			    Criteria criteria = new Criteria();

			    // Getting the name of the best provider
			    String provider = locationManager.getBestProvider(criteria, true);

			    // Getting Current Location
			    location = locationManager.getLastKnownLocation(provider);
			    

			    if(location!=null){
			            onLocationChanged(location);
			            
			    }

			    locationManager.requestLocationUpdates(provider, 120000, 0, this);
		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
		}
	}	
	
	
	 public class ServiceAcceptApi extends AsyncTask<String, String, String>
	  {
		
		  @Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
		
			  String urlParameters = null;
	            try {
	                urlParameters ="username=" + URLEncoder.encode(valueurid, "UTF-8")+"&&"
	                		+"driver_id=" + URLEncoder.encode(apprPreference.getId(), "UTF-8")+"&&"
	                		+"transferdate=" + URLEncoder.encode(valuetdate, "UTF-8");
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            response = Connectivity.excutePost(Constants.ACCEPTFRIEND_URL,
	                    urlParameters);
	            Log.e("You are at", "" + response);
	       return response;
			 
		}
		  @Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(response.contains("success"))
			{
				Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_SHORT).show();
				new MyDrRequestDataFromdb().execute();
			}
			else {
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
			}
		}
	  }
	
}
