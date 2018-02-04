package com.cargc0044.grocit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.adapters.Shopcategorylist_adapter;
import com.cargc0044.grocit.interfaces.shopcategorylistinterface;
import com.cargc0044.grocit.util.ShopCategoryAsyntask;
import com.cargc0044.grocit.util.utilMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cargc0044.grocit.util.utilMethods.showNoInternetDialog;


public class shopcategoryfragment extends Fragment implements utilMethods.InternetConnectionListener {
    ListView shopcategorylist;
    Shopcategorylist_adapter shopcategorylist_adapter;
    static shopcategoryfragment fragment;
    private final int SHOP_ACTION = 1;
    utilMethods.InternetConnectionListener internetConnectionListener;
    View rootView;
    JSONObject shopcategorylistjsonobj ,shopcategorylistjobj;
    JSONArray shopcategorylistjsonarray ;
    ArrayList<String> categoryname,categoryimage,categoryid;
    int count = 0;
    static String shopcategoryjson_string = null;
ProgressBar shopcategoryprogressBar;
    String shopid;
    public static shopcategoryfragment newInstance(String shopid) {

         fragment = new shopcategoryfragment();
        Bundle args = new Bundle();
       args.putString("shopid",shopid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopcategory, container, false);
        shopcategorylist = (ListView) rootView.findViewById(R.id.list);
shopcategoryprogressBar=(ProgressBar) rootView.findViewById(R.id.shopcategoryprogressBar);
        shopid=getArguments().getString("shopid");



      return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (utilMethods.isConnectedToInternet(getActivity())) {
            initShopCat();
        } else {
            internetConnectionListener = shopcategoryfragment.this;
            showNoInternetDialog(getActivity(), internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), SHOP_ACTION);
        }

    }


    private void initShopCat(){

            new ShopCategoryAsyntask(shopid,new shopcategorylistinterface() {
                @Override
                public void shopcategorypostresult(String result) {
                    shopcategoryjson_string=result;
                    try {
                        shopcategorylistjsonobj = new JSONObject(shopcategoryjson_string);
                        shopcategorylistjsonarray = shopcategorylistjsonobj.getJSONArray("categories");

                        categoryimage=new ArrayList<String>();
                        categoryid=new ArrayList<String>();
                        categoryname=new ArrayList<String>();

                        while (count < shopcategorylistjsonarray.length()) {
                            shopcategorylistjobj = shopcategorylistjsonarray.getJSONObject(count);
                            categoryname.add(shopcategorylistjobj.getString("category_name"));
                            categoryimage.add(shopcategorylistjobj.getString("image"));
                            categoryid.add(shopcategorylistjobj.getString("categoryid"));
                            count++;
                        }
                        shopcategorylist_adapter = new Shopcategorylist_adapter(getActivity(),R.layout.shopcategoryrowlayout,
                                categoryname,categoryimage,categoryid,shopcategoryfragment.this);
                        shopcategorylist.setAdapter(shopcategorylist_adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void shopcategoryprogress() {
                    shopcategoryprogressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void shopcategoryrogressfinished() {
                    shopcategoryprogressBar.setVisibility(View.GONE);
                }
            }).execute();
    }

        @Override
        public void onAttach(Context context) {
        super.onAttach(context);


        }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onConnectionEstablished(int code) {
        if (code == SHOP_ACTION) {
            initShopCat();
        }

    }

    @Override
    public void onUserCanceled(int code) {
        if (code == SHOP_ACTION) {
            getActivity().finish();
        }
    }
}

