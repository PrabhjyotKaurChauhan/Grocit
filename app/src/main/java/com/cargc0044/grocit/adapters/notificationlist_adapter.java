package com.cargc0044.grocit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cargc0044.grocit.R;

import java.util.ArrayList;

/**
 * Created by Harpal Singh on 19-03-2016.
 */
public class notificationlist_adapter extends ArrayAdapter
{
    ArrayList<String> notificationid=new ArrayList<String>();

    ArrayList<String> notificationheading=new ArrayList<String>();
    ArrayList<String>notificationdetails=new ArrayList<String>();

    TextView Notificationheading,Notificationdetails;
    View row;
    Context mcontext;
    public notificationlist_adapter(Context context, int resource, ArrayList<String> notificationid, ArrayList<String> notificationheading,
                                    ArrayList<String> notificationdetails)    {
        super(context, resource);
        this.mcontext=context;
this.notificationheading=notificationheading;
        this.notificationdetails=notificationdetails;
    this.notificationid=notificationid;
    }


    @Override
    public int getCount() {
       return notificationdetails.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationdetails.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    row= layoutInflater.inflate(R.layout.notificationrowlayout,parent ,false);
        Notificationheading=(TextView) row.findViewById(R.id.notification_heading);
        Notificationdetails=(TextView) row.findViewById(R.id.notification_details);

        Notificationheading.setText(notificationheading.get(position));
        Notificationdetails.setText(notificationdetails.get(position));

        return row;
    }
}
