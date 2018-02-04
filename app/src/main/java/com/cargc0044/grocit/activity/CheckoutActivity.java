package com.cargc0044.grocit.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cargc0044.grocit.util.DBHelper;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.util.utilMethods;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import static com.cargc0044.grocit.util.constants.JF_ID;
import static com.cargc0044.grocit.util.utilMethods.getPreferenceString;
import static com.cargc0044.grocit.util.utilMethods.showNoInternetDialog;
//import android.widget.CheckBox;

public class CheckoutActivity extends AppCompatActivity  implements utilMethods.InternetConnectionListener{

    TextView btnSend,btnPayPal;
    private AlertDialog resetDialog = null;
    utilMethods.InternetConnectionListener internetConnectionListener;
    private final int CART_ACTION = 1;
    static Button btnDate;
    static Button btnTime;
    EditText edtName, edtName2, edtPhone, edtOrderList, edtComment, edtAlamat, edtEmail, edtKota, edtProvinsi;
    ScrollView sclDetail;

    TextView txtAlert;

    private String TotalPrice;

    // declare dbhelper object
    static DBHelper dbhelper;
    ArrayList<ArrayList<Object>> data;

    // declare string variables to store data
    String Name, Name2, Date, Time, Phone, Date_n_Time, Alamat, Email, Kota, Provinsi;
    String OrderList = "";
    String Comment = "";


    // declare static int variables to store date and time
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;

    // declare static variables to store tax and currency data
    static double Tax;


    static final String TIME_DIALOG_ID = "timePicker";
    static final String DATE_DIALOG_ID = "datePicker";

    // create price format
    DecimalFormat formatData = new DecimalFormat("#.##");

    String Result;
    String TaxCurrencyAPI;
    int IOConnect = 0;
    private String locality,city,user;
    int code;
    String orderNo;

    SimpleDateFormat inputDF = new SimpleDateFormat("MM/dd/yy");
    java.util.Date date = new Date();
    Calendar cal = Calendar.getInstance();



    //Payment Gateway
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId("Abe6mrG-mCO3puFiJOVW2vaagkb9sb1APG2tPV1Nc3MF0-xsNXBm-bwXbXWrooCoOrut5xFTUav5kCTr");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setElevation(0);
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);

        TotalPrice = getIntent().getStringExtra("GrandPrice");
        edtName = (EditText) findViewById(R.id.edtName);
        edtName2 = (EditText) findViewById(R.id.edtName2);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnTime = (Button) findViewById(R.id.btnTime);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtOrderList = (EditText) findViewById(R.id.edtOrderList);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnSend = (TextView) findViewById(R.id.btnSend);
        sclDetail = (ScrollView) findViewById(R.id.sclDetail);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        edtAlamat = (EditText) findViewById(R.id.edtAlamat);
        edtKota = (EditText) findViewById(R.id.edtKota);
        edtProvinsi = (EditText) findViewById(R.id.edtProvinsi);

        locality = getPreferenceString(this, "locality");
        city = getPreferenceString(this, "city");
        user = getPreferenceString(this, JF_ID);

        edtKota.setText(locality);
        edtProvinsi.setText(city);

        if (utilMethods.isConnectedToInternet(this)) {
            initCheckout();
            Log.d("Inside Out", "All DAY");
        } else {
            internetConnectionListener = this;
            showNoInternetDialog(this, internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), CART_ACTION);
        }




        // tax and currency API url


        // event listener to handle date button when pressed
        btnDate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show date picker dialog
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });

        // event listener to handle time button when pressed
        btnTime.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                // show time picker dialog
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == CART_ACTION) {
            initCheckout();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == CART_ACTION) {
            this.finish();
        }

    }

    public void onCOD(View v){

        cal.setTime(date);
        String month = Integer.toString(cal.get(Calendar.MONTH));
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String year = Integer.toString(cal.get(Calendar.YEAR));

        String date = month+day+year;
        Random rnd = new Random();
        code = 100000 + rnd.nextInt(900000);
        orderNo = user+Integer.toString(code)+date;
        showVerificationConfirmDialog(CheckoutActivity.this,"Order Successful",
                "You order number is #"+orderNo,
                getResources().getString(R.string.continue_string));
        Toast.makeText(getApplicationContext(),"Order Successful", Toast.LENGTH_LONG).show();
    }

    public void showVerificationConfirmDialog(final Context context, String headline, String body,
                                              String positiveString) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_dialog, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(headline);
        ((TextView) view.findViewById(R.id.bodyTV)).setText(body);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setView(view)
                .setCancelable(false);

        resetDialog = builder.create();
        resetDialog.show();
    }



    public void onPayPal(View v){

        Double GrandPix = Double.parseDouble(TotalPrice) / 66;
        String NewTotal = GrandPix.toString();
        PayPalPayment payment = new PayPalPayment(new BigDecimal(NewTotal), "USD", "Total Price",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
            cal.setTime(date);
            String month = Integer.toString(cal.get(Calendar.MONTH));
            String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            String year = Integer.toString(cal.get(Calendar.YEAR));

            String date = month+day+year;
            Random rnd = new Random();
            code = 100000 + rnd.nextInt(900000);
            orderNo = user+Integer.toString(code)+date;
            showVerificationConfirmDialog(CheckoutActivity.this, "Order Successful",
                    "You order number is #" + orderNo,
                    getResources().getString(R.string.continue_string));
            Toast.makeText(getApplicationContext(),"Order Successful", Toast.LENGTH_LONG).show();
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    // method to create date picker dialog
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear = year;
            mMonth = month;
            mDay = day;

            // show selected date to date button
            btnDate.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
        }
    }

    // method to create time picker dialog
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default time
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of DatePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // get selected time
            mHour = hourOfDay;
            mMinute = minute;

            // show selected time to time button
            btnTime.setText(new StringBuilder()
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute)).append(":")
                    .append("00"));
        }
    }

    private void initCheckout() {

        /**
         * json is populating from text file. To make api call use ApiHandler class
         */

        OrderList += "\nTotal: "+TotalPrice+" Rs";
        edtOrderList.setText(OrderList);

         /* You will get the response in onSuccessResponse(String tag, String jsonString) method
         * if successful api call has done. Do the parsing as the following.
         */

    }


    // method to parse json data from server




    // method to format date
    private static String pad(int c) {
        if (c >= 10){
            return String.valueOf(c);
        }else{
            return "0" + String.valueOf(c);
        }
    }

    // when back button pressed close database and back to previous page
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig)
    {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }
}
