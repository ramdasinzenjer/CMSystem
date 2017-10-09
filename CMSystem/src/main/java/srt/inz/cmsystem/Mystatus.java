package srt.inz.cmsystem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import srt.inz.connnectors.Connectivity;
import srt.inz.connnectors.Constants;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Mystatus extends Activity{

	TextView tv; String resp;
	
	ListView mlist; ListAdapter adapter;
	
	ArrayList<HashMap<String, String>> oslist=new ArrayList<HashMap<String,String>>();	
	String valuedrid,valuepdrt,valueprdtyp,valuestatus,valuedest,valuetdate,valuedlg;
	
	ApplicationPreference apprPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mystatus);
		mlist=(ListView)findViewById(R.id.listmyreqsts);
		tv=(TextView)findViewById(R.id.mtxt_state);
		apprPreference=(ApplicationPreference)getApplication();
			
		new MyRequestDataFromdb().execute();
		
	}
	
	protected class MyRequestDataFromdb extends AsyncTask<String, String, String>
	
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

	        resp = Connectivity.excutePost(Constants.MYREQLIST_URL,
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
				String driver_id=data1.getString("driver_id");
				String productname=data1.getString("productname");
				String producttype=data1.getString("producttype");
				String destination=data1.getString("destination");
				String transferdate=data1.getString("transferdate");
				String status=data1.getString("status");
				
				// Adding value HashMap key => value
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("driver_id", driver_id);
	            map.put("productname", productname);
	            map.put("producttype", producttype);
	            map.put("destination", destination);
	            map.put("transferdate", transferdate);
	            map.put("status", status);
	            
	            map.put("notification", "Request to "+driver_id+" on date : "+transferdate+"."+
	            "\n  To :"+destination+".");
	            
	            map.put("dalogmsg", "\n Destination :"+destination+"\n Driver id :"+driver_id+""
	            		+"\t Status :"+status);
	            
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
	               
	               valuedrid=oslist.get(+position).get("driver_id");
	                valuepdrt=oslist.get(+position).get("productname"); 
	               valueprdtyp=oslist.get(+position).get("producttype");
	               valuedest=oslist.get(+position).get("destination");
	               valuetdate=oslist.get(+position).get("transferdate");
	               valuestatus=oslist.get(+position).get("status");
	               valuedlg=oslist.get(+position).get("dalogmsg");
	               
	         tv.setText("Prduct : "+valuepdrt+"\n"+valuedlg);
	               
	               }
	                });
			}
		}
			catch (Exception e)         
		{
				System.out.println("Error:"+e);
		}
	}
	 /*public void openDialog(){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      
	     if(valuestatus.equals("pending")) 
	     
	    	 {
	    	 alertDialogBuilder.setTitle("Please choose an action!");
		      alertDialogBuilder.setMessage(""+valuedlg);
	    	 alertDialogBuilder.setPositiveButton("Accept & Navigate", new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	            
	            Toast.makeText(getApplicationContext(),"Navigating...",Toast.LENGTH_SHORT).show();
	            new StatuschangeApiTask().execute();
	            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            		    Uri.parse("google.navigation:q="+valuesrc));
	            startActivity(intent);
	        
	         }
	      });
	      
	      alertDialogBuilder.setNegativeButton("Reject",new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	
	        	 Toast.makeText(getApplicationContext(),"Request rejected.",Toast.LENGTH_SHORT).show();
	        
	        	 //finish();
	         }
	      });
	      
	      alertDialogBuilder.setNeutralButton("Chat", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),"Forwading to Chatroom.",Toast.LENGTH_SHORT).show();
				
			}
		});
	      
	    	 }
	     else
	     {
	    	 alertDialogBuilder.setTitle("Drive!!!");
		      alertDialogBuilder.setMessage("This drive is already accepted.");
	    	 alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
     
		         }
		      });
	     }
	      
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
	   }*/
}
