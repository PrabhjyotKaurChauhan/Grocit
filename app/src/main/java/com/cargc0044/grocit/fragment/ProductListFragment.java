package com.cargc0044.grocit.fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cargc0044.grocit.activity.Cartz;
import com.cargc0044.grocit.R;
import com.cargc0044.grocit.activity.MainActivity;
import com.cargc0044.grocit.adapters.SubCategoryAdapter;
import com.cargc0044.grocit.model.CartModel;
import com.cargc0044.grocit.adapters.ProductListAdapter;

import com.cargc0044.grocit.model.Category;
import com.cargc0044.grocit.model.ProductModel;
import com.cargc0044.grocit.util.DBHelper;
import com.cargc0044.grocit.util.ApiHandler;
import com.cargc0044.grocit.util.TotalPriceCallback;
import com.cargc0044.grocit.util.utilMethods;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.cargc0044.grocit.util.constants.KEY_CATEGORY_ID;
import static com.cargc0044.grocit.util.constants.PRODUCT_BRIEF;
import static com.cargc0044.grocit.util.constants.PRODUCT_CAT;
import static com.cargc0044.grocit.util.constants.PRODUCT_DESC;
import static com.cargc0044.grocit.util.constants.PRODUCT_ID;
import static com.cargc0044.grocit.util.constants.PRODUCT_IMAGE;
import static com.cargc0044.grocit.util.constants.PRODUCT_NAME;
import static com.cargc0044.grocit.util.constants.PRODUCT_PRICE;
import static com.cargc0044.grocit.util.constants.PRODUCT_SHOP;
import static com.cargc0044.grocit.util.constants.PRODUCT_STATUS;
import static com.cargc0044.grocit.util.constants.PRODUCT_SUB;
import static com.cargc0044.grocit.util.constants.SUB_CATEGORY_ID;
import static com.cargc0044.grocit.util.constants.SUB_CATEGORY_NAME;
import static com.cargc0044.grocit.util.constants.URL_GET_CATEGORY;
import static com.cargc0044.grocit.util.constants.URL_GET_PRODUCTLIST;
import static com.cargc0044.grocit.util.constants.URL_GET_SEARCHRESULT;
import static com.cargc0044.grocit.util.constants.URL_GET_SUB_CATEGORY;
import static com.cargc0044.grocit.util.utilMethods.showNoInternetDialog;

public class ProductListFragment extends Fragment implements utilMethods.InternetConnectionListener, ApiHandler.ApiHandlerListener{

    MainActivity prods;
    private String productCart;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final int CART_ACTION = 1;
    private ArrayList<CartModel> cartList;
    private ListView prodListView;
    private static String subCatid;
    private static String searchText;
    private ProductSelectionCallbacks mCallbacks;
    private TextView subTotal, orderName;
    private ImageView delete;
    private TextView Price;
    private ImageView ImgThumb;
    private ImageView minus;
    private ImageView add;
    private TextView displayQty;
    public TextView TxtTotal;

    private utilMethods.InternetConnectionListener internetConnectionListener;
    ProgressDialog progessDialog;

    // declate dbhelper and adapter objects
    DBHelper dbhelper;
    ProductListAdapter mola;


    // declare static variables to store tax and currency data
    static double Tax;
    static String Currency;

    // declare arraylist variable to store data
    ArrayList<ArrayList<Object>> data;
    static ArrayList<Integer> Product_ID = new ArrayList<Integer>();
    static ArrayList<String> Order_name = new ArrayList<String>();
    static ArrayList<Integer> Shop_ID= new ArrayList<Integer>();
    static ArrayList<Double> Sub_total_price = new ArrayList<Double>();
    static ArrayList<Double> order_price = new ArrayList<Double>();
    static ArrayList<String> Image = new ArrayList<String>();
    static ArrayList<Double> Total = new ArrayList<Double>();
    ProgressDialog progressDialog;


    double Total_price = 0;
    double NewTotalPrice;
    final int CLEAR_ALL_ORDER = 0;
    final int CLEAR_ONE_ORDER = 1;
    // create price format
    DecimalFormat formatData = new DecimalFormat("#.##");

    public ProductListFragment() {


    }

    public static ProductListFragment newInstance(String id) {
        ProductListFragment fragment = new ProductListFragment();
        subCatid = id;
        return fragment;
    }

    //Search
    public static ProductListFragment newInstance(String tag, String query) {
        ProductListFragment fragment = new ProductListFragment();
        searchText = query;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prods = (MainActivity) getActivity();


    }

    //public void onTotal(Double TotalPrice){
      //      Toast.makeText(getActivity(),Double.toString(TotalPrice),Toast.LENGTH_LONG).show();
    //}

    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.listproduct, container, false);
        prodListView = (ListView) rootView.findViewById(R.id.products_listviews);

        dbhelper = new DBHelper(getActivity());

        // open database
       /* try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }*/

        prods.getSupportActionBar().setTitle("Products");
        // selecting single ListView item

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (utilMethods.isConnectedToInternet(getActivity())) {
            if (!TextUtils.isEmpty(subCatid))
                initProductList();
            else if (!TextUtils.isEmpty(searchText))
                getSearchResults(searchText);
        } else {
            internetConnectionListener = ProductListFragment.this;
            showNoInternetDialog(getActivity(), internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), CART_ACTION);
        }

    }

    private void getSearchResults(String query){

        query = query.toLowerCase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, query);
        ApiHandler apiHandler = new ApiHandler(this, URL_GET_SEARCHRESULT, values);
        apiHandler.doApiRequest(ApiHandler.REQUEST_POST);


    }

    private void initProductList() {

        ContentValues values = new ContentValues();
        values.put(PRODUCT_SUB, subCatid);
        ApiHandler apiHandler = new ApiHandler(this, URL_GET_PRODUCTLIST, values);
        apiHandler.doApiRequest(ApiHandler.REQUEST_POST);


    }

    // clear arraylist variables before used
    void clearData(){
        Order_name.clear();
        Product_ID.clear();
        Sub_total_price.clear();
        Image.clear();

    }

    @Override
    public void onSuccessResponse(String tag, String jsonString) {
        Total_price = 0;
        clearData();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {

                Product_ID.add(Integer.parseInt(jsonArray.getJSONObject(i).getString(PRODUCT_ID)));
                Order_name.add(jsonArray.getJSONObject(i).getString(PRODUCT_NAME));
                order_price.add(Double.valueOf(formatData.format(jsonArray.getJSONObject(i).getDouble(PRODUCT_PRICE))));
                Image.add(jsonArray.getJSONObject(i).getString(PRODUCT_IMAGE));
                Shop_ID.add(Integer.parseInt(jsonArray.getJSONObject(i).getString(PRODUCT_SHOP)));

                }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    prodListView.setAdapter(new ProductListAdapter(getActivity(), Product_ID,Order_name,order_price,Image, Shop_ID));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureResponse(String tag) {
        Toast.makeText(getContext(),"No result",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == CART_ACTION) {
            initProductList();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == CART_ACTION) {
            getActivity().finish();
        }
    }

    //! callback interface listen by CategoryActivity to detect user click on category
    public static interface ProductSelectionCallbacks {
        public abstract void onProductSelected(String prodID, String title);
    }

}
