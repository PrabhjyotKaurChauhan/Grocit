package com.cargc0044.grocit.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.activity.MainActivity;
import com.cargc0044.grocit.fragment.CategoryFragment;
import com.cargc0044.grocit.fragment.SubCategoryFragment;
import com.cargc0044.grocit.interfaces.CatGridSelectionCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Harpal Singh on 03-04-2016.
 */
public class category_grid_adapter extends BaseAdapter  {
    private FragmentManager fragmentManager;
    CatGridSelectionCallback mCallback;
    ArrayList<String> categoryid=new ArrayList<String>();
    ArrayList<String>categoryname=new ArrayList<String>();
    ArrayList<String>categoryicon=new ArrayList<String>();
    ImageView Categoryicon;
    TextView Categoryname;

    View row;
    Context mcontext;

    public category_grid_adapter(Context context,CatGridSelectionCallback mCallback, ArrayList<String> categoryname,
                                  ArrayList<String> categoryid,ArrayList<String> categoryicon) {
        this.mcontext = context;
this.categoryicon=categoryicon;
        this.categoryid=categoryid;
        this.categoryname=categoryname;
        this.mCallback = mCallback;
    }





    @Override
    public int getCount()
    {
        return categoryid.size();
    }


    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater=(LayoutInflater) this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     row=  layoutInflater.inflate(R.layout.categorygrid,parent ,false);
       Categoryicon=(ImageView) row.findViewById(R.id.categories_image);
       Categoryname=(TextView) row.findViewById(R.id.categories_text);

        Picasso.with(mcontext).load(categoryicon.get(position)).fit().placeholder(R.drawable.img_placeholder)
        .into(Categoryicon);
Categoryname.setText(categoryname.get(position));
        row.setBackgroundResource(R.drawable.gridborder);
        row.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       Log.d("SUNGGA", categoryid.get(position));
                                       mCallback.onCatGridSelected(categoryid.get(position), categoryname.get(position));

                                   }
                               }
        );

            return row;
     }



}


