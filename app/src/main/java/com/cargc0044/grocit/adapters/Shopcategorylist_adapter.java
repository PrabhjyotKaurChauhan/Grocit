package com.cargc0044.grocit.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.fragment.SubCategoryFragment;
import com.cargc0044.grocit.fragment.shopcategoryfragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Harpal Singh on 19-03-2016.
 */
public class Shopcategorylist_adapter extends ArrayAdapter
{
    ArrayList<String>categoryimage=new ArrayList<String>();
    ArrayList<String> categoryid=new ArrayList<String>();
    ArrayList<String>categoryname=new ArrayList<String>();
    ImageView Categoryimage;
    TextView Categoryname;
    String Categoryid;
FragmentManager fragmentmanager;
    shopcategoryfragment shopcategoryfragment;
    View row;
    Context mcontext;
    public Shopcategorylist_adapter(Context context, int resource, ArrayList<String> categoryname,
                                    ArrayList<String> categoryimage, ArrayList<String> categoryid,shopcategoryfragment shopcategoryfragment)
    {
        super(context, resource);
        this.mcontext=context;
this.categoryimage=categoryimage;
        this.categoryname=categoryname;
        this.categoryid=categoryid;
        this.shopcategoryfragment=shopcategoryfragment;
    }



    @Override
    public int getCount() {
       return categoryid.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryid.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    row = layoutInflater.inflate(R.layout.shopcategoryrowlayout,parent ,false);
    Categoryname=(TextView) row.findViewById(R.id.category_name);
       Categoryimage=(ImageView) row.findViewById(R.id.category_image);
 Categoryname.setText(categoryname.get(position));

        Picasso.with(mcontext).load(categoryimage.get(position)).fit().placeholder(R.drawable.img_placeholder)
                .into(Categoryimage);
   row.setOnClickListener(new View.OnClickListener() {
       public void onClick(View v) {
           Categoryid = categoryid.get(position);

           Toast.makeText(mcontext,Categoryid,Toast.LENGTH_LONG).show();

           SubCategoryFragment s =new SubCategoryFragment();
          fragmentmanager=shopcategoryfragment.getFragmentManager();
           fragmentmanager.beginTransaction().replace(R.id.con,s.newInstance(Categoryid)).addToBackStack(null).commit();

       }
   });
        {}
        return row;
    }
}
