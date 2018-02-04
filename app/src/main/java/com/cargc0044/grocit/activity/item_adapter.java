package com.cargc0044.grocit.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.model.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harpal Singh on 19-03-2016.
 */
public class item_adapter extends ArrayAdapter

{
    List list=new ArrayList();
    public item_adapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(items object) {
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
        row=convertView;
        itemholder itemh ;
    if(row==null)
    {
        itemh=new itemholder();
        LayoutInflater layoutInflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row =layoutInflater.inflate(R.layout.rowlayout,parent ,false);
    itemh.name=(TextView) row.findViewById(R.id.name);
        itemh.password=(TextView) row.findViewById(R.id.password);
row.setTag(itemh);
    }
    else
    {
itemh=(itemholder )row.getTag();
    }
        items sitems=(items) this.getItem(position);
     itemh.name.setText(sitems.getName());
        itemh.password.setText(sitems.getPassword());

        return row;
    }
static class itemholder
{
    TextView name,password;
}
}
