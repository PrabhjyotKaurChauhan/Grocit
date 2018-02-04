package com.cargc0044.grocit.activity;

import java.text.DecimalFormat;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;;
import android.widget.TextView;
import com.cargc0044.grocit.R;
import com.cargc0044.grocit.fragment.CartListFragment;
import com.cargc0044.grocit.util.TotalPriceCallback;

public class Cartz extends AppCompatActivity implements TotalPriceCallback {

    //Declare Price Format
    DecimalFormat formatData = new DecimalFormat("#.##");
    private boolean isCartOpen = false, isProductDetailsOpen = false;

    // used to store app title
    private CharSequence mTitle = "Cart";
    private String USER_ID;
    private FragmentManager fragmentManager;
    private String products_category;
    private double Total = 0;
    private TextView Total_Price;
    private Double GrandPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartz);
        Total_Price = (TextView) findViewById(R.id.TotalPrice);
        USER_ID = getIntent().getStringExtra("userID");
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, CartListFragment.newInstance(USER_ID))
                .commit();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    /**
     * Slide menu item click listener
     * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public void onTotal(Double TotalPrice){
       // Toast.makeText(this, Double.toString(TotalPrice), Toast.LENGTH_LONG).show();
        TotalPrice = Double.parseDouble(formatData.format(TotalPrice));
        this.GrandPrice = TotalPrice;
        Total_Price.setText("Grand Total : Rs. " + Double.toString(TotalPrice));
    }


    public void buyNow(View view){
        // open database first

        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("GrandPrice", Double.toString(GrandPrice));
        startActivity(intent);
    }

    public String getProducts_category() {
        return products_category;
    }

    public void setProducts_category(String products_category) {
        this.products_category = products_category;
    }

}