package com.cargc0044.grocit.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.adapters.CategoryAdapter;
import com.cargc0044.grocit.model.Category;
import com.cargc0044.grocit.model.ProductModel;
import com.cargc0044.grocit.model.User;
import com.cargc0044.grocit.util.ApiHandler;
import com.cargc0044.grocit.util.DBHelper;
import com.cargc0044.grocit.util.utilMethods;
import com.cargc0044.grocit.util.utilMethods.InternetConnectionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.cargc0044.grocit.util.constants.KEY_CATEGORY_ID;
import static com.cargc0044.grocit.util.constants.SUB_CATEGORY_ID;
import static com.cargc0044.grocit.util.constants.SUB_CATEGORY_NAME;
import static com.cargc0044.grocit.util.constants.URL_GET_PRODUCT;
import static com.cargc0044.grocit.util.constants.KEY_PRODUCT_ID;
import static com.cargc0044.grocit.util.constants.KEY_SHOP_ID;
import static com.cargc0044.grocit.util.constants.*;
import static com.cargc0044.grocit.util.utilMethods.hideSoftKeyboard;
import static com.cargc0044.grocit.util.utilMethods.showNoInternetDialog;
import static com.cargc0044.grocit.util.utilMethods.showSoftKeyboard;
import static com.cargc0044.grocit.util.utilMethods.isUserSignedIn;
import static com.cargc0044.grocit.util.utilMethods.getPreferenceString;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductDetail extends AppCompatActivity implements InternetConnectionListener, ApiHandler.ApiHandlerListener {

    public static String shopId;
    public static String prodId;
    //dbhelper object
    private static DBHelper dbHelper;
    private final int PRODUCT_DETAILS_ACTION = 1;
    ProgressBar prgLoading;
    TextView txtAlert;
    //Declare Price Format
    DecimalFormat formatData = new DecimalFormat("#.##");
    private ImageView imgPreview;
    private TextView ProductName, ProductName2, status_yes, status_no, Description, Highlight;
    private Context context = this;
    private TextView Price;
    private Button buy;
    private SearchView mySearchView;
    private CharSequence mTitle;
    private utilMethods.InternetConnectionListener internetConnectionListener;
    private ProductModel productModel;
    private String proID;
    private String prodName;
    private double priceTag;
    private String prodImage;
    private String prodShop;
    private String prodID;
    private String shopID;
    private Integer quantity = 1;
    private String userID;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getSupportActionBar().setElevation(0);

        prodID = getIntent().getStringExtra("prodID");
        shopID = getIntent().getStringExtra("shopID");
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());

        mTitle = getTitle();
        //mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        mySearchView = (SearchView) findViewById(R.id.searchView);

        dbHelper = new DBHelper(context);

        imgPreview = (ImageView) findViewById(R.id.detailHeadingImage);
        ProductName = (TextView) findViewById(R.id.txtText);
        ProductName2 = (TextView) findViewById(R.id.ProductName);
        status_yes = (TextView) findViewById(R.id.available);
        status_no = (TextView) findViewById(R.id.unavailable);
        Price = (TextView) findViewById(R.id.Price);
        Description = (TextView) findViewById(R.id.DescriptionTV);
        Highlight = (TextView) findViewById(R.id.infoTV);
        buy = (Button) findViewById(R.id.btnAdd);

        // create database
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        if (isUserSignedIn(this)) {
            getUserInfo();
            this.userID = user.getId();
        }

    }

    private void getUserInfo() {
        user = new User();
        user.setId(getPreferenceString(this, JF_ID));
    }


    @Override
    protected void onResume(){
        super.onResume();
        if (utilMethods.isConnectedToInternet(this))
            initProductDetails();
        else {
            internetConnectionListener = (InternetConnectionListener) ProductDetail.this;
            showNoInternetDialog(this, internetConnectionListener, getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), PRODUCT_DETAILS_ACTION);
    }
        // event listener to handle Buy button when clicked

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Options for options action bar item select
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mySearchView.isFocused()) {
            mySearchView.setFocusable(false);
        }
        hideSoftKeyboard(ProductDetail.this);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                showHideSearchView();
                break;
            case R.id.action_cart:
                hideSearchView();
                break;
            case R.id.action_bell:
                hideSearchView();
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
    }



    public void initProductDetails(){
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, prodID);
        ApiHandler apiHandler = new ApiHandler(this, URL_GET_PRODUCT, values);
        apiHandler.doApiRequest(ApiHandler.REQUEST_POST);

    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == PRODUCT_DETAILS_ACTION) {
            initProductDetails();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == PRODUCT_DETAILS_ACTION) {
            this.finish();
        }}

    @Override
    public void onSuccessResponse(String tag, String jsonString) {
        try {


            JSONArray jsonArray = new JSONArray(jsonString);


            for (int i = 0; i < jsonArray.length(); i++) {

                ProductModel productModel = new ProductModel();
                productModel.setId(jsonArray.getJSONObject(i).getString(PRODUCT_ID));
                productModel.setProductTitle(jsonArray.getJSONObject(i).getString(PRODUCT_NAME));
                productModel.setPrice(Double.valueOf(formatData.format(jsonArray.getJSONObject(i).getDouble(PRODUCT_PRICE))));
                productModel.setStatus(jsonArray.getJSONObject(i).getString(PRODUCT_STATUS));
                productModel.setDescription(jsonArray.getJSONObject(i).getString(PRODUCT_DESC));
                productModel.setHighlight(jsonArray.getJSONObject(i).getString(PRODUCT_BRIEF));
                productModel.setProductImage(jsonArray.getJSONObject(i).getString(PRODUCT_IMAGE));
                productModel.setCat_id(jsonArray.getJSONObject(i).getString(PRODUCT_CAT));
                productModel.setSub_id(jsonArray.getJSONObject(i).getString(PRODUCT_SUB));
                productModel.setShop_id(jsonArray.getJSONObject(i).getString(PRODUCT_SHOP));

                display(productModel);
                this.productModel = productModel;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }}

    public void buy(View view){
        // open database first

        proID = productModel.getId();
        priceTag = productModel.getPrice();
        prodName = productModel.getProductTitle();
        prodImage = productModel.getProductImage();
        prodShop = productModel.getShop_id();
        try{
            dbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
            if(dbHelper.isDataExist(proID)){
                dbHelper.updateData(userID, proID, priceTag);
            }else{
                dbHelper.addData(userID, proID, prodName, prodImage, quantity, priceTag);
            }

        Intent intent = new Intent(this, Cartz.class);
        intent.putExtra("userID", userID);
        startActivity(intent);

    }


    public void display(final ProductModel productModel){

                   this.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           String statusCheck = new String("available");
                           String status = new String(productModel.getStatus());
                    ProductName.setText(productModel.getProductTitle());
                    ProductName2.setText(productModel.getProductTitle());
                           if(statusCheck.equals(status)){
                               status_yes.setVisibility(View.VISIBLE);
                                status_yes.setText(productModel.getStatus());
                           }
                           else{
                               status_no.setVisibility(View.VISIBLE);
                               buy.setVisibility(View.GONE);
                               status_no.setText(productModel.getStatus());
                           }

                    Description.setText(Html.fromHtml("<b><u>Description : </u></b>")+"\n" + productModel.getDescription());
                    Double thePrice = productModel.getPrice();
                    Price.setText("Price : " + thePrice.toString() + " Rs.");
                    Highlight.setText(Html.fromHtml("<b><u>Features : </u></b>")+"\n" + productModel.getHighlight());
                    Picasso.with(context)
                            .load(productModel.getProductImage())
                            .placeholder(R.drawable.img_banner_placeholder)
                            .fit()
                            .into(imgPreview);    }
                   });


    }

    @Override
    public void onFailureResponse(String tag) {

    }


    }

