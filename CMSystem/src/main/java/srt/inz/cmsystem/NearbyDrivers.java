package srt.inz.cmsystem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi") public class NearbyDrivers extends FragmentActivity implements OnMapReadyCallback{
	
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	LatLng[] latLng= new LatLng[10]; String[] straray=new String[10];
	LatLng llg,ltt;
	ProgressDialog pDialog;
	String respo,proresp;
	double lat; double lon;	List<Address> addr; String mloc,sl;
	String slat,slng,response2;
	
	String svh_id;	String ssvid,ssph,ssh_typ;
	
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_driver);	

		SupportMapFragment supportMapFragment = (SupportMapFragment) 
				getSupportFragmentManager().findFragmentById(R.id.map);
		// Getting a reference to the map
		supportMapFragment.getMapAsync(this);
		
		// Getting reference to btn_find of the layout activity_main
        Button btn_find = (Button) findViewById(R.id.btn_find);
        Button btn_current= (Button) findViewById(R.id.btcurrent);
        
        btn_current.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lat  = googleMap.getMyLocation().getLatitude();
		        lon   = googleMap.getMyLocation().getLongitude();
		         sl=String.valueOf(googleMap.getMyLocation().getLatitude())+"-"+
		     		   String.valueOf(googleMap.getMyLocation().getLongitude());
		         Toast.makeText(getApplicationContext(), ""+sl, Toast.LENGTH_SHORT).show();
			}
		});
        googleMap.setMyLocationEnabled(true);
        
        
       
        // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {			
			@Override
			public void onClick(View v) {
							
				ltt=new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
			       	           
		           llg=new LatLng(lat, lon);
				
				
			//	if(location!=null && !location.equals("")){
					new GeocoderTask().execute("");
				//}
			}
		};
		
		// Setting button click event listener for the find button
		btn_find.setOnClickListener(findClickListener);		
				  
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

	}


	private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;
			
			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace();
			}			
			return addresses;
		}
				
		@Override
		protected void onPostExecute(List<Address> addresses) {			
	        
	        // Clears all the existing markers on the map
	        googleMap.clear();
			
	        // Adding Markers on Google Map for each matching address
			for(int i=0;i<addresses.size();i++){				
				
				Address address = (Address) addresses.get(i);
		        String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());		        
			}			
			new Mapfrom_db().execute();	
		}		
	}
	private void drawMarker(LatLng point) {
	    // Creating an instance of MarkerOptions
		
		markerOptions=new MarkerOptions();
	    MarkerOptions markerOptionsc=new MarkerOptions();
	    // Setting latitude and longitude for the marker
	    
	    markerOptions.position(point);	    
	    markerOptions.title(""+svh_id);

	    markerOptionsc.position(llg);
	    markerOptionsc.title("center");

	    double radiusInMeters = 100.0;
	     //red outline
	    int strokeColor = 0xffff0000;
	    //opaque red fill
	    int shadeColor = 0x44ff0000; 

	    CircleOptions circleOptions = new CircleOptions().center(llg).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
	    googleMap.addCircle(circleOptions);
	    // Adding marker on the Google Map
	    googleMap.addMarker(markerOptions);
	    	    
	  //  googleMap.addMarker(markerOptionsc);
	    CameraUpdate center=
	            CameraUpdateFactory.newLatLng(llg);
	        CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);

	        googleMap.moveCamera(center);
	        googleMap.animateCamera(zoom);
	        
	        
	        final Circle circle = googleMap.addCircle(new CircleOptions().center(llg)
	                .strokeColor(Color.CYAN).radius(1000));
	        
	        ValueAnimator vAnimator = new ValueAnimator();
	        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
	        vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
	        vAnimator.setIntValues(0, 100);
	        vAnimator.setDuration(1000);
	        vAnimator.setEvaluator(new IntEvaluator());
	        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
	        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	            @Override
	            public void onAnimationUpdate(ValueAnimator valueAnimator) {
	                float animatedFraction = valueAnimator.getAnimatedFraction();
	                // Log.e("", "" + animatedFraction);
	                circle.setRadius(animatedFraction * 100);
	            }
	        });
	        vAnimator.start();
	        
	}
	public class Mapfrom_db extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(NearbyDrivers.this);
            pDialog.setMessage("Loaing coordinates..");
            pDialog.setCancelable(false);
            pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String urlParameters = null;
			
			//Toast.makeText(getApplicationContext(), ""+sl, Toast.LENGTH_SHORT).show();
			
            try {
                urlParameters =  "coordinates=" + URLEncoder.encode(""+sl, "UTF-8") + "&&"
                        + "distance=" + URLEncoder.encode("5", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

           String result = Connectivity.excutePost(Constants.LOCATIONFETCH_URL,
                    urlParameters);
            respo=result;
            Log.e("You are at", "" + result);

       return result;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog.isShowing())
                pDialog.dismiss();
			if(respo.contains("success"))
			{
				parsingmethod();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Nearby Services Found", Toast.LENGTH_LONG).show();
				
			}
		}
		
	}
	public void parsingmethod()
	{
		try
		{
			JSONObject jobject=new JSONObject(respo);
			JSONObject jobject1=jobject.getJSONObject("Event");
			JSONArray ja=jobject1.getJSONArray("Details");
			int length=ja.length();
			for(int i=0;i<length;i++)
			{
				JSONObject data1=ja.getJSONObject(i);
				slat=data1.getString("lat");
				slng=data1.getString("lon");
				svh_id=data1.getString("vhid");

				
				
				HashMap<String, String> map = new HashMap<String, String>();
	            map.put("vhid", svh_id);
	            
	            //Toast.makeText(getApplicationContext(), ""+sname,Toast.LENGTH_SHORT).show();
	            oslist.add(map);
	            				
				double dlt,dlng;
				dlt=Double.valueOf(slat).doubleValue();
				dlng=Double.valueOf(slng).doubleValue();
				latLng[i]= new LatLng(dlt, dlng);
				drawMarker(latLng[i]);
				
				
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					
					@Override
					public void onInfoWindowClick(Marker arg0) {
						// TODO Auto-generated method stub
						
					String sid = arg0.getTitle();
					svh_id=sid;
						
					Toast.makeText(getApplicationContext(), ""+sid, Toast.LENGTH_LONG).show();
					
					SharedPreferences sharv=getSharedPreferences("mvid_01", MODE_WORLD_READABLE);
					SharedPreferences.Editor de=sharv.edit();
					de.putString("vehid", sid);
					de.commit();
					
					new Driverdet().execute();
					
					}
				});	
				
			//	Toast.makeText(getApplicationContext(), ""+slat+""+slng, Toast.LENGTH_SHORT).show();
							
			}
		}
		catch(Exception e)
		{
			System.out.println("error:"+e);
		}
	}
	
	
	public void openDialog(String svid){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      
	  final String sv=svid;

			   
	    	 alertDialogBuilder.setTitle("Please choose an action!");
		      alertDialogBuilder.setMessage("Driver Id: "+svid+"\n Sharing Type : "+ssh_typ
		    		  +"\n Vehicle Number : "+ssvid);
	    	 alertDialogBuilder.setPositiveButton("Request Ride", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	        	 
	        	 SharedPreferences sh=getSharedPreferences("mKey2", MODE_WORLD_READABLE);
	        	 SharedPreferences.Editor ed =sh.edit();
	        	 ed.putString("vid", svh_id);	        	 
	        	 ed.commit();
	        	 if(ssh_typ.equals("0"))
	        	 {
	        	 
	        	 Toast.makeText(getApplicationContext(), "" +
	        	 		"Selected service disabled sharing ", Toast.LENGTH_SHORT).show();
	        	 }
	        	 else
	        	 {
	        		 Intent i=new Intent(getApplicationContext(),ReqTransService.class);
		        	 startActivity(i);
	        	 }
	        	 
	         }
	      });
	      
	      alertDialogBuilder.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	
	        //	 Toast.makeText(getApplicationContext(),"Back",Toast.LENGTH_SHORT).show();
	        
	         }
	      });
	      
	     
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
    	 
	}

	 public class Driverdet extends AsyncTask<String, String, String>
	  {
		
		  @Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
		
			  String urlParameters = null;
	            try {
	                urlParameters ="driver_id=" + URLEncoder.encode(svh_id, "UTF-8");
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	            response2 = Connectivity.excutePost(Constants.DRIVERDATA_URL,
	                    urlParameters);
	            Log.e("You are at", "" + response2);
	       return response2;
			 
		}
		  @Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(response2.contains("success"))
			{
				mparsingmethod();
			}
			else {
				Toast.makeText(getApplicationContext(), "Service Unavailable", Toast.LENGTH_SHORT).show();
			}
		}
	  }
	  
	  
	  public void mparsingmethod() {
			try
			{
				JSONObject jobject=new JSONObject(response2);
				JSONObject jobject1=jobject.getJSONObject("Event");
				JSONArray ja=jobject1.getJSONArray("Details");
				int length=ja.length();
				for(int i=0;i<length;i++)
				{
		          
					JSONObject c = ja.getJSONObject(i);
		            // Storing  JSON item in a Variable
					 ssh_typ= c.getString("sh_state");
					 ssph= c.getString("phone");
					 ssvid= c.getString("vehiclenum");
					 
					 openDialog(svh_id);
		               
		            }
			}
		        catch (JSONException e) {
		          e.printStackTrace();
		        }
		       }
	
}
