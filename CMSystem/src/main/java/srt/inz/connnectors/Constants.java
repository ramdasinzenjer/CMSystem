package srt.inz.connnectors;


public interface Constants {

    //Progress Message
    String LOGIN_MESSAGE="Logging in...";
    String REGISTER_MESSAGE="Register in...";

    //Urls
    String BASE_URL="https://ammonvasili.000webhostapp.com/";
    String DRIVER_REGISTER_URL=BASE_URL+"cargo_dregister.php?";
    String USER_REGISTER_URL=BASE_URL+"cargo_uregister.php?";
    String LOGIN_URL=BASE_URL+"cago_login.php?";
    String LOCATIONFETCH_URL=BASE_URL+"mLocServices.php?";
    String DRIVERDATA_URL=BASE_URL+"cargo_driverdata.php?";    
    String USERREQUEST_URL=BASE_URL+"cargo_req.php?";
    String MYREQLIST_URL=BASE_URL+"cargo_mrqlist.php?";
    String MYREQLISTD_URL=BASE_URL+"cargo_mrqlistd.php?";
    
    String MYPROFILE_URL=BASE_URL+"cargo_prodetails.php?";
    String MYPROFILEUP_URL=BASE_URL+"cargo_uproup.php?";
    String ACCEPTFRIEND_URL = BASE_URL+"cargo_accpetreq.php?";
    String SAVELOCATION_URL = BASE_URL+"mUpdateLoc.php?";
    
    String BUSNOFETCH2_URL=BASE_URL+"mBusinofetch2.php?";
    String CARDDETAILS=BASE_URL+"mfullCarddetails.php?";
    String CARDPAYMENTDETAILS_URL=BASE_URL+"mPaymentdet.php?";
    String USERAPPROVECARD=BASE_URL+"mApprovecard.php?";
    
    //Details
    String PASSWORD="Password";
    String USERNAME="Username";
    String LOGINSTATUS="LoginStatus";
    String USERID="UserId";
    String USERTYPE="UserType";
    
    String MYLOCATION = "MyLocation";
    String MYLOCATIONLAT = "MyLocationLat";
    String MYLOCATIONLON = "MyLocationLon";
    String SERVICESTATUS="ServiceStatus";
    String SHARESERVICESTATUS="ShareService";
    
    //SharedPreference
    String PREFERENCE_PARENT="Parent_Pref";
	
	
	
	
   
}
