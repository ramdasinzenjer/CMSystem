package srt.inz.cmsystem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class UserHome extends Activity implements LocationListener{
	
	String stplace; double latitude,longitude; Location location;
	Button btmap,btpro,btlgt,btstat;
	
	ApplicationPreference apprPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_home);
		
		btmap=(Button)findViewById(R.id.btf_nearbydrivers);
		btpro=(Button)findViewById(R.id.btf_profdetails);
		btlgt=(Button)findViewById(R.id.btf_logout);
		btstat=(Button)findViewById(R.id.btf_status);
		apprPreference=(ApplicationPreference)getApplication();
		
		getmyloc();
		
		btmap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),NearbyDrivers.class);
				startActivity(i);
				
			}
		});
		
		btpro.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i=new Intent(getApplicationContext(),Profileup.class);
						startActivity(i);
						
					}
				});
		
		btlgt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		btstat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),Mystatus.class);
				startActivity(i);
				
			}
		});
		
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

}
