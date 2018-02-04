package com.cargc0044.grocit.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.model.items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class displayList extends AppCompatActivity {
String json_string;
    JSONObject jsonobj;
    JSONArray  jsonarray;
    item_adapter itemadp;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        list=(ListView)findViewById(R.id.list1);

        itemadp=new item_adapter(this,R.layout.rowlayout);
        list.setAdapter(itemadp);
        json_string=getIntent().getExtras().getString("json_data");
        try {
            jsonobj=new JSONObject(json_string);
            jsonarray=jsonobj.getJSONArray("server_response");
            int count=0;
            String name,password;
            while(count<jsonarray.length())
            {
JSONObject jobj=jsonarray.getJSONObject(count);
name=jobj.getString("name");
                password=jobj.getString("password");
                items item =new items(name,password);
            itemadp.add(item);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setid(Context ctx)
    {
        Context c=ctx;
        Intent a=new Intent(c,displayList.class);
        c.startActivity(a);
    }
}
