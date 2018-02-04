package com.cargc0044.grocit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.adapters.notificationlist_adapter;
import com.cargc0044.grocit.interfaces.notificationinterface;
import com.cargc0044.grocit.util.notificationAsyntask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class notifications extends Activity implements notificationinterface {
ListView notificationlist;
    JSONObject notificationjsonobj, notificationjobj;
    JSONArray   notificationjsonarray;
   int notificationcount =0;
    notificationlist_adapter notification_adapter;
    ArrayList<String>  notificationid,notificationheading,notificationdetails;
String json_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        notificationheading=new ArrayList<String>();
        notificationdetails=new ArrayList<String>();
                notificationlist =(ListView) findViewById(R.id.notificationlist);
new notificationAsyntask(notifications.this).execute();


    }
@Override
    public void notificationpostresult(String result) {
        this.json_string=result;

    try {
            notificationjsonobj= new JSONObject(json_string);
            notificationjsonarray=  notificationjsonobj.getJSONArray("notifications");

            notificationid=new ArrayList<String>();
            notificationheading =new ArrayList<String>();
            notificationdetails=new ArrayList<String>();



            while ( notificationcount <notificationjsonarray.length()) {
                notificationjobj=  notificationjsonarray.getJSONObject(notificationcount);
                notificationid.add( notificationjobj.getString("notification_id"));
                notificationheading.add( notificationjobj.getString("notfication_name"));
                notificationdetails.add( notificationjobj.getString("notification_details"));
                notificationcount++;
            }
            notification_adapter=new notificationlist_adapter(notifications.this,
                    R.layout.activity_notifications,notificationid,
                    notificationheading,notificationdetails );
            notificationlist.setAdapter(notification_adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
