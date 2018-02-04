package com.cargc0044.grocit.activity;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.adapters.Viewpager_Adapter;
import com.cargc0044.grocit.adapters.category_grid_adapter;
import com.cargc0044.grocit.adapters.product_grid_adapter;
import com.cargc0044.grocit.fragment.CategoryFragment;
import com.cargc0044.grocit.fragment.NavigationDrawerFragment;
import com.cargc0044.grocit.fragment.ProductListFragment;
import com.cargc0044.grocit.fragment.SubCategoryFragment;
import com.cargc0044.grocit.fragment.shoplistfragment;
import com.cargc0044.grocit.interfaces.CatGridSelectionCallback;
import com.cargc0044.grocit.interfaces.categoryinterface;
import com.cargc0044.grocit.interfaces.productinterface;
import com.cargc0044.grocit.interfaces.shopinterface;
import com.cargc0044.grocit.util.CategoryAsyntask;
import com.cargc0044.grocit.util.ProductsAsyntask;
import com.cargc0044.grocit.util.ShopListAsyntask;
import com.cargc0044.grocit.util.utilMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.cargc0044.grocit.util.constants.JF_ID;
import static com.cargc0044.grocit.util.constants.isResultListFragmentOpened;
import static com.cargc0044.grocit.util.utilMethods.hideSoftKeyboard;
import static com.cargc0044.grocit.util.utilMethods.savePreference;
import static com.cargc0044.grocit.util.utilMethods.showNoInternetDialog;
import static com.cargc0044.grocit.util.utilMethods.showSoftKeyboard;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        categoryinterface,shopinterface,productinterface, NavigationDrawerFragment.NavigationDrawerCallbacks,
        CategoryFragment.CategorySelectionCallbacks, SubCategoryFragment.SubCategorySelectionCallbacks,CatGridSelectionCallback,
        utilMethods.InternetConnectionListener{

    CatGridSelectionCallback mCallback;
    private final int HOME_ACTION = 1;
    utilMethods.InternetConnectionListener internetConnectionListener;
    public static int selectedDistrictId = 1;
    public static String selectedArea = "All Area";
    private int navigationDepth = 0;
    private SearchView mySearchView;
    private List<String> suggestions;
    private String mCurrentSearchText;
    private List<String> areaList;
    private FragmentManager fragmentManager;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private String subCategoryTitle = null;
    private String resultListTitle = null;
    private String detailViewTitle = null;
    private String searchQueryTitle = null;
    private boolean isPopupWindowNeedToUpdate = false;
    private boolean isResultListNeedToUpdate = false;
    private RelativeLayout filterLayout;
    private Spinner spinnerDistrict;
    private Spinner spinnerArea;
    private TextView buttonApply;

    Geocoder geocoder;
    TextView Address;
    List<android.location.Address> addresses;

    GPSTracker gps;
/* --- Variables by harpals singh---- */

    ArrayList<String> shopimage,shopname,shopid,productname,productimage,productid,categoryname,categoryid,categoryicon;
    boolean doubleclick=false;
    JSONObject categoryjsonobj ,categoryjobj, shopjsonobj ,shopjobj,productjsonobj ,productjobj;
    NetworkInfo info;
    ConnectivityManager cm ;
    JSONArray categoryjsonarray,shopjsonarray,productjsonarray;
    int categorycount = 0,productcount=0,shopcount=0;
    shoplistfragment shoplistfragment;
    String category_string=null,product_string =null,shop_string =null;
    Viewpager_Adapter adapter;
    ViewPager viewPager;
    category_grid_adapter categoryadapter;
    product_grid_adapter productadapter;
    GridView categorygridview,productgridview;
    private BroadcastReceiver mBroadcastReciver;
    ProgressBar shopprogress,categoryprogress,productprogress;
    private ListView suggestionListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());

        mTitle = getTitle();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        mySearchView = (SearchView) findViewById(R.id.searchView);
        mySearchView.setSearchableInfo(searchableInfo);
        mySearchView.setOnQueryTextListener(this);
        mySearchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mySearchView.setQueryHint(Html.fromHtml("<small><small>" + getString(R.string.search_hint) + "</small></small>"));
        int searchPlateId = mySearchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = mySearchView.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        int submitAreaId = mySearchView.getContext().getResources().getIdentifier("android:id/submit_area", null, null);
        View submitArea = mySearchView.findViewById(submitAreaId);
        submitArea.setBackgroundColor(Color.TRANSPARENT);
        int searchImgIcon = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView searchImgView = (ImageView) mySearchView.findViewById(searchImgIcon);
        searchImgView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        searchImgView.setVisibility(View.GONE);
        int closeButtonId = mySearchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) mySearchView.findViewById(closeButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySearchView.setQuery(null, false);
                utilMethods.hideSoftKeyboard(MainActivity.this);
            }
        });

        //Current Location
        Address = (TextView) findViewById(R.id.location);

        //Harpal code below
        viewPager= (ViewPager) findViewById(R.id.pager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(15);
        final Context c=getBaseContext();
        categorygridview= (GridView) findViewById(R.id.categorygridview);
        productgridview= (GridView) findViewById(R.id.productgridview);
        shopprogress =(ProgressBar) findViewById(R.id.shopviewpager);
        productprogress =(ProgressBar)findViewById(R.id.pgrid);
        categoryprogress =(ProgressBar)findViewById(R.id.grid);

        cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        info= cm.getActiveNetworkInfo();





       // this.registerReceiver(mBroadcastReciver, newIntentFilter("start.fragment.action"));

        mCallback = (CatGridSelectionCallback) this;

        launchHome();



        }

    private void launchHome(){
        if (utilMethods.isConnectedToInternet(this)) {
            initHome();
        } else {
            internetConnectionListener = this;
            showNoInternetDialog(this, internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), HOME_ACTION);
        }}


    private void initHome(){
        new CategoryAsyntask(MainActivity.this).execute();
        new ShopListAsyntask(MainActivity.this).execute();
        new ProductsAsyntask(MainActivity.this).execute();
        initCurrentLocation();
    }



    private void initCurrentLocation(){
        geocoder = new Geocoder(this, Locale.getDefault());

        gps = new GPSTracker(MainActivity.this);

        if(gps.CanGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();



            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL*/
                Log.d("OBSERVE", address);
                String location = getResources().getString(R.string.geopin);
                String geo = getResources().getString(R.string.locationz);
                savePreference(MainActivity.this, "locality", address);
                savePreference(MainActivity.this, "city", city);
                if(address.equals(location)){
                    Address.setText(geo);
                }
                else {
                    Address.setText(address);
                }
                Toast.makeText(getApplicationContext(), "Your location is -\nLat: " + latitude + "-\nLong: " + longitude, Toast.LENGTH_LONG);
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
                Log.d("Cordinates",Double.toString(latitude));
            }
        }
        else{
            gps.showSettingAlert();


        }
        this.doubleclick = false;
    }

    public void refreshGPS(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void viewallshops(View v)
    {
        if (info != null && info.isConnected()) {
            shoplistfragment=new shoplistfragment();
            fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.con,shoplistfragment.newInstance()).addToBackStack(null).commit();


        }
        else
        {
            Toast.makeText(MainActivity.this,"Please make sure you have internet connection!!",Toast.LENGTH_LONG).show();
        }
    }

    public void viewallcategories(View v)
    {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.con, CategoryFragment.newInstance(0))
                    .addToBackStack(null).commit();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mySearchView.setQuery(query, false);
            mySearchView.clearFocus();
        }
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (mySearchView.getVisibility() == View.VISIBLE) {
            hideSearchView();
        }
        startActivity(new Intent(this, MainActivity.class));
        navigationDepth = 0;
        isResultListFragmentOpened = false;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        if (navigationDepth == 0)
            actionBar.setTitle(getTitle());
        else if (navigationDepth == 1)
            actionBar.setTitle(subCategoryTitle);
        else if (navigationDepth == 2)
            actionBar.setTitle(resultListTitle);
        else if (navigationDepth == 3)
            actionBar.setTitle(detailViewTitle);
        else if (navigationDepth == 4)
            actionBar.setTitle(searchQueryTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.menu, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    //Options for options action bar item select
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mySearchView.isFocused()) {
            mySearchView.setFocusable(false);
        }
        hideSoftKeyboard(MainActivity.this);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                if (!mNavigationDrawerFragment.isDrawerOpen())
                    showHideSearchView();
                break;
            case R.id.action_cart:
                if (!mNavigationDrawerFragment.isDrawerOpen())
                    hideSearchView();
                    Intent intent = new Intent(this, Cartz.class);
                    startActivity(intent);
                break;
            case R.id.action_bell:
                if (!mNavigationDrawerFragment.isDrawerOpen())
                    hideSearchView();
                Intent intents = new Intent(this, notifications.class);
                startActivity(intents);
                break;
            case R.id.home:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHideSearchView() {
        if (mySearchView.getVisibility() == View.VISIBLE) {
            mySearchView.setFocusable(false);
            mySearchView.setVisibility(View.GONE);
            hideSoftKeyboard(this);
        } else {
            mySearchView.setVisibility(View.VISIBLE);
            mySearchView.setQuery("", false);
            mySearchView.setFocusable(true);
            mySearchView.requestFocus();
            showSoftKeyboard(this);
        }
    }

    private void hideSearchView() {
        if (mySearchView.getVisibility() == View.VISIBLE) {
            mySearchView.setVisibility(View.GONE);
        }
        hideSuggestionList();
    }
    private void hideSuggestionList() {
        if (suggestionListView != null && suggestionListView.getVisibility() == View.VISIBLE) {
            suggestionListView.setAdapter(null);
            suggestionListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCategorySelected(String catID, String title) {
        hideSearchView();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, SubCategoryFragment.newInstance(catID)).addToBackStack(null)
                .commit();
        navigationDepth++;
        getSupportActionBar().setTitle(title);
        subCategoryTitle = title;
    }

    @Override
    public void onSubCategorySelected(String subCatID, String title) {
        hideSearchView();
        Log.d("ProductList",subCatID);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, ProductListFragment.newInstance(subCatID)).addToBackStack(null)
                .commit();
        navigationDepth++;
        getSupportActionBar().setTitle(title);
        resultListTitle = title;
    }

    //Check this file
    @Override
    public boolean onQueryTextSubmit(String query) {
        mySearchView.clearFocus();
        hideSearchView();
        utilMethods.hideSoftKeyboard(this);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, ProductListFragment.newInstance("", query)).addToBackStack(null)
                .commit();
        navigationDepth = 4;
        getSupportActionBar().setTitle(query);
        searchQueryTitle = query;
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    @Override
    public void shoppostresult(String result) {
        shop_string=result;

        try {
            shopjsonobj= new JSONObject(shop_string);
            shopjsonarray= shopjsonobj.getJSONArray("shops");
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
            fragmentManager = getSupportFragmentManager();
            adapter= new Viewpager_Adapter(MainActivity.this,shopimage,shopname,shopid,fragmentManager);
            viewPager.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
   }

    @Override
    public void shopprogress() {
        shopprogress.setVisibility(View.VISIBLE);
    }

    @Override
    public void shopprogressfinished() {
        shopprogress.setVisibility(View.GONE);
    }

    @Override
    public void categorypostresult(String result) {
     category_string=result;
        try {
            categoryjsonobj= new JSONObject(category_string);
            categoryjsonarray= categoryjsonobj.getJSONArray("category");
            categoryid=new ArrayList<String>();
            categoryname=new ArrayList<String>();
            categoryicon=new ArrayList<String>();


            while (categorycount<12) {
                categoryjobj = categoryjsonarray.getJSONObject(categorycount);
                categoryname.add(categoryjobj.getString("category_name"));
                categoryid.add(categoryjobj.getString("category_id"));
                categoryicon.add(categoryjobj.getString("category_icon"));
                categorycount++;
            }
            categoryadapter = new category_grid_adapter(MainActivity.this, mCallback, categoryname,categoryid,categoryicon);
            categorygridview.setAdapter(categoryadapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void categoryprogress() {
        categoryprogress.setVisibility(View.VISIBLE);
    }
    @Override
    public void categoryrogressfinished() {
categoryprogress.setVisibility(View.GONE);
    }
    @Override
    public void productpostresult(String result) {

        product_string=result;
        try {
            productjsonobj= new JSONObject(product_string);
            productjsonarray= productjsonobj.getJSONArray("products");
            productimage=new ArrayList<String>();
            productid=new ArrayList<String>();
            productname=new ArrayList<String>();
            while (productcount<productjsonarray.length()) {
                productjobj = productjsonarray.getJSONObject(productcount);
                productname.add(productjobj.getString("product_name"));
                productimage.add(productjobj.getString("product_image"));
                productid.add(productjobj.getString("product_id"));
                productcount++;
            }
            productadapter = new product_grid_adapter(MainActivity.this,productname,productimage,productid);
            productgridview.setAdapter(productadapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void productprogress() {
   productprogress.setVisibility(View.VISIBLE);
    }

    @Override
    public void progressprogressfinished() {
productprogress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

                super.onBackPressed();
        getFragmentManager().popBackStack();

        return;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onCatGridSelected(String subID, String title) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, SubCategoryFragment.newInstance(subID)).addToBackStack(null)
                .commit();
        navigationDepth++;
        getSupportActionBar().setTitle(title);
        subCategoryTitle = title;

    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == HOME_ACTION) {
            initHome();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == HOME_ACTION) {
            this.finish();
        }
    }
}

