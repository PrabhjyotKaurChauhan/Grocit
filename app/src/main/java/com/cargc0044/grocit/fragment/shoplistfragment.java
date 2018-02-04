package com.cargc0044.grocit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.adapters.Shoplist_adapter;
import com.cargc0044.grocit.interfaces.shopinterface;
import com.cargc0044.grocit.util.ShopListAsyntask;
import com.cargc0044.grocit.util.utilMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cargc0044.grocit.util.utilMethods.showNoInternetDialog;

public class shoplistfragment extends Fragment implements utilMethods.InternetConnectionListener {
    ListView shoplist;
    Shoplist_adapter shoplistadapter;
    private final int SHOP_ACTION = 1;
    private utilMethods.InternetConnectionListener internetConnectionListener;
    static shoplistfragment fragment;
static FragmentManager fm;
    View rootView;
ProgressBar shoplistprogressbar;
    String shop_string;
    ArrayList<String> shopimage,shopname,shopid;
    JSONObject shopjobj,shopjsonobj ;
    JSONArray shopjsonarray;


public shoplistfragment(){}
    public static shoplistfragment newInstance() {

        shoplistfragment.fragment = new shoplistfragment();
        Bundle args = new Bundle();
        shoplistfragment.fragment.setArguments(args);
        return shoplistfragment.fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
rootView= inflater.inflate(R.layout.fragment_shoplist, container, false);
        shoplist = (ListView) rootView.findViewById(R.id.shoplist);
shoplistprogressbar=(ProgressBar)rootView.findViewById(R.id.shoplistprogressBar);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (utilMethods.isConnectedToInternet(getActivity())) {
            initShopList();
        } else {
            internetConnectionListener = shoplistfragment.this;
            showNoInternetDialog(getActivity(), internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), SHOP_ACTION);
        }

    }

    private void initShopList(){
        new ShopListAsyntask(new shopinterface() {
            @Override
            public void shoppostresult(String result) {
                shop_string=result;
                try {
                    shopjsonobj= new JSONObject(shop_string);
                    shopjsonarray= shopjsonobj.getJSONArray("shops");
                    int shopcount = 0;
                    shopimage=new ArrayList<String>();
                    shopid=new ArrayList<String>();
                    shopname=new ArrayList<String>();

                    while (shopcount <7 ) {
                        shopjobj= shopjsonarray.getJSONObject(shopcount);
                        shopimage.add(shopjobj.getString("shop_image"));
                        shopname.add(shopjobj.getString("shop_name"));
                        shopid.add(shopjobj.getString("shopid"));
                        shopcount++;
                    }
                    shoplistadapter = new Shoplist_adapter(getActivity(), R.layout.shoplistrowlayout,shopimage,shopname,shopid,
                            shoplistfragment.this);
                    shoplist.setAdapter(shoplistadapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void shopprogress() {
                shoplistprogressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void shopprogressfinished() {
                shoplistprogressbar.setVisibility(View.GONE);
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
            initShopList();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == SHOP_ACTION) {
            getActivity().finish();
        }
    }
}
