package com.cargc0044.grocit.adapters;

/**
 * Created by Harpal Singh on 22-02-2016.
 */

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.fragment.shopcategoryfragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Viewpager_Adapter extends PagerAdapter {
   FragmentManager fragment;
    private Context mcontext;
    LayoutInflater layoutInflater;
    ArrayList<String>images=new ArrayList<String>();
    ArrayList<String> shopid=new ArrayList<String>();
    ArrayList<String>shopname=new ArrayList<String>();
    shopcategoryfragment shopcategoryfragment;
    String Shopid;
    ImageView  Shopimage;

    View row;

    public Viewpager_Adapter(Context context, ArrayList<String> images, ArrayList<String> shopname,
                             ArrayList<String> shopid, FragmentManager mainActivity) {
    mcontext = context;
        this.images=images;
        this.shopid=shopid;
        this.shopname=shopname;
        this.fragment=mainActivity;
    }



    @Override
    public float getPageWidth (int position) {
        return 1.0f;
    }


    @Override
    public int getCount() {
        return shopid.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.shops, container, false);
           Shopimage = (ImageView) row.findViewById(R.id.image);
            Picasso.with(mcontext).load(images.get(position)).fit().placeholder(R.drawable.img_placeholder)
                    .into(Shopimage);

        row.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {
           Shopid=shopid.get(position);

                                 fragment.beginTransaction()
                                               .replace(R.id.con, shopcategoryfragment.newInstance(Shopid)).addToBackStack(null).commit();

                                   }

                               }
        );

        container.addView(row);
        return row;

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


}