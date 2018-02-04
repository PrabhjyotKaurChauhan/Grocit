package com.cargc0044.grocit.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.util.utilMethods;

public class NoInternetConnection extends Activity {
ConnectivityManager cm;
    NetworkInfo info;
  Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nointernetconnection);}

    public void retry(View v) {
                ConnectivityManager   cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();

                if (info != null && info.isConnected()) {
                    if(utilMethods.isUserSignedIn(NoInternetConnection.this) ==true)
                    {
                        Intent i = new Intent(NoInternetConnection.this, MainActivity.class);
                        startActivity(i);
                       finish();
                    }
                       else
                    {
                        Intent i = new Intent(NoInternetConnection.this, LandingActivity.class);
                        startActivity(i);
                        finish();
                    }
                    }
             }

}

