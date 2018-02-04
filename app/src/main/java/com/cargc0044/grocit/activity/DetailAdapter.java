package com.cargc0044.grocit.activity;

/**
 * Created by Harpal Singh on 15-03-2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.model.details;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public DetailAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(details object) {
        super.add(object);
        list.add(object);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        ContactHolder contactHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);
            contactHolder = new ContactHolder();
            contactHolder.tx_name = (TextView) row.findViewById(R.id.tx_name);
            contactHolder.tx_password = (TextView) row.findViewById(R.id.tx_password);
            row.setTag(contactHolder);
        } else {
            contactHolder = (ContactHolder) row.getTag();
        }

        details dtl = (details) this.getItem(position);
        contactHolder.tx_name.setText(dtl.getName());
        contactHolder.tx_password.setText(dtl.getPassword());

        return row;
    }

    static class ContactHolder {
        TextView tx_name, tx_password;
    }
}
