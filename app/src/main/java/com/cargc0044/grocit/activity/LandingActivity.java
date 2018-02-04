package com.cargc0044.grocit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cargc0044.grocit.R;

/**
 * @author Nguza Yikona.
 * @class LandingActivity
 * @brief Activity for showing Sign up, Sign In and See first option
 */
public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        findViewById(R.id.btnSignUps).setOnClickListener(this);
        findViewById(R.id.btnSeeFirst).setOnClickListener(this);
        findViewById(R.id.btnSignIns).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSeeFirst:
                startActivity(new Intent(LandingActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.btnSignUps:
                startActivity(new Intent(LandingActivity.this, RegistrationActivity.class));
                break;
            case R.id.btnSignIns:
                startActivity(new Intent(LandingActivity.this, LoginActivity.class));
                break;
        }
    }
}
