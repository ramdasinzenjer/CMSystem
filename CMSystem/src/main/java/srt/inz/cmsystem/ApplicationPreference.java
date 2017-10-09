package srt.inz.cmsystem;

import srt.inz.connnectors.Constants;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits") public class ApplicationPreference extends Application {
    private static SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    String Name,Email,Password,id,Type,MyLocation,MyLocationlat,MyLocationlon; 
    Boolean LoginStatus,ServiceStatus,Shareloc;

    public String getId() {
         String userid= appSharedPrefs.getString(Constants.USERID,"");
        return userid;
    }

    public void setId(String type) {
        prefsEditor.putString(Constants.USERID, type);
        prefsEditor.commit();
    }

    public String getType() {
        Type= appSharedPrefs.getString(Constants.USERTYPE,"");
       return Type;
   }

   public void setType(String id) {
       prefsEditor.putString(Constants.USERTYPE, Type);
       prefsEditor.commit();
   }
    
    
    public Boolean getLoginStatus() {
        LoginStatus=   appSharedPrefs.getBoolean(Constants.LOGINSTATUS,false);
        return LoginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        prefsEditor.putBoolean(Constants.LOGINSTATUS,loginStatus);
    }

    public String getMyLocation() {
        MyLocation=appSharedPrefs.getString(Constants.MYLOCATION,"");
        return MyLocation;
    }

    public void setMyLocation(String loc) {
        prefsEditor.putString(Constants.MYLOCATION, loc);
        prefsEditor.commit();
    }

    public String getMyLocationLat() {
        MyLocationlat=appSharedPrefs.getString(Constants.MYLOCATIONLAT,"");
        return MyLocation;
    }

    public void setMyLocationLat(String lat) {
        prefsEditor.putString(Constants.MYLOCATIONLAT, lat);
        prefsEditor.commit();
    }
    
    public String getMyLocationLon() {
        MyLocationlat=appSharedPrefs.getString(Constants.MYLOCATIONLON,"");
        return MyLocation;
    }

    public void setMyLocationLon(String lon) {
        prefsEditor.putString(Constants.MYLOCATIONLON, lon);
        prefsEditor.commit();
    }
    
 /*   public String getEmail() {
        Email=appSharedPrefs.getString(Constants.EMAIL,"");
        return Email;
    }

    public void setEmail(String email) {
        prefsEditor.putString(Constants.EMAIL, email);
        prefsEditor.commit();
    }*/
    
    public Boolean getServiceStatus() {
    	ServiceStatus= appSharedPrefs.getBoolean(Constants.SERVICESTATUS, false);     
        return ServiceStatus;
    }

    public void setServiceStatus(Boolean serviceStatus) {
        prefsEditor.putBoolean(Constants.SERVICESTATUS,serviceStatus);
        prefsEditor.commit();
    }
    
    public Boolean getShareServiceStatus() {
    	Shareloc= appSharedPrefs.getBoolean(Constants.SHARESERVICESTATUS, false);     
        return Shareloc;
    }

    public void setShareServiceStatus(Boolean shareserviceStatus) {
        prefsEditor.putBoolean(Constants.SHARESERVICESTATUS,shareserviceStatus);
        prefsEditor.commit();
    }

    public String getPassword() {

        Password=appSharedPrefs.getString(Constants.PASSWORD,"");
        return Password;
    }

    public void setPassword(String password) {
        prefsEditor.putString(Constants.PASSWORD, password);
        prefsEditor.commit();
    }

	@SuppressWarnings("static-access")
	@Override
    public void onCreate() {
        super.onCreate();
        this.appSharedPrefs = getApplicationContext().getSharedPreferences(
                Constants.PREFERENCE_PARENT, MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

}
