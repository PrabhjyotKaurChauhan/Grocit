package com.cargc0044.grocit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.model.User;
import com.cargc0044.grocit.util.FloatLabel;
import com.cargc0044.grocit.util.GetUserCallBack;
import com.cargc0044.grocit.util.ServerRequest;
import com.cargc0044.grocit.util.utilMethods;


import static com.cargc0044.grocit.util.Validator.isInputted;
import static com.cargc0044.grocit.util.constants.JF_CONTACT_NUMBER;
import static com.cargc0044.grocit.util.constants.JF_EMAIL;
import static com.cargc0044.grocit.util.constants.JF_FNAME;
import static com.cargc0044.grocit.util.constants.JF_LNAME;
import static com.cargc0044.grocit.util.constants.JF_ID;
import static com.cargc0044.grocit.util.constants.JF_NAME;
import static com.cargc0044.grocit.util.constants.JF_PASSWORD;
import static com.cargc0044.grocit.util.constants.JL_NAME;
import static com.cargc0044.grocit.util.utilMethods.InternetConnectionListener;
import static com.cargc0044.grocit.util.utilMethods.getPreferenceString;
import static com.cargc0044.grocit.util.utilMethods.hideSoftKeyboard;
import static com.cargc0044.grocit.util.utilMethods.isConnectedToInternet;
import static com.cargc0044.grocit.util.utilMethods.savePreference;
import static com.cargc0044.grocit.util.utilMethods.setUserLoggedIn;



//Todo: Make database check connection and create Session
/**
 * @author Nguza Yikona
 *  LoginActivity
 *  Responsible for making user logged in
 */

public class LoginActivity extends Activity implements View.OnClickListener, View.OnTouchListener, InternetConnectionListener {

    private final int SIGNED_IN_ACTION = 1;
    String phone,password;
    private FloatLabel etMobileNumber;
    private FloatLabel etPassword;
    private boolean isUserCanceled = false;
    private InternetConnectionListener internetConnectionListener;
    private Button btnNewuserTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        findViewById(R.id.btnNewUserTV).setOnClickListener(this);
        findViewById(R.id.showPasswordImg).setOnTouchListener(this);
        findViewById(R.id.btnForgotPasswordTV).setOnClickListener(this);

        etMobileNumber = (FloatLabel) findViewById(R.id.etMobileNumber);
        etPassword = (FloatLabel) findViewById(R.id.etPassword);
        etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        etMobileNumber.getEditText();

        //btnNewuserTV.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.crossImgView:
                hideSoftKeyboard(this);
                isUserCanceled = true;
                Intent intent = new Intent(this, LandingActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                onPause();
                break;

            case R.id.btnSignIn:
                if (isInputValid()) {
                    if (isConnectedToInternet(LoginActivity.this)) {
                        doLoginRequest(etMobileNumber.getEditText().getText().toString(),
                                etPassword.getEditText().getText().toString());

                    } else {

                        internetConnectionListener = LoginActivity.this;
                        utilMethods.showNoInternetDialog(LoginActivity.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), SIGNED_IN_ACTION);
                    }

                }
                break;

            case R.id.btnNewUserTV:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                isUserCanceled = true;
                onPause();
                break;



            case R.id.btnForgotPasswordTV:
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
                break;

            //String fname, String lname, String phoneNumber
        }
    }

    private void doLoginRequest(String phone, String password) {
        User user = new User();
        user.setEmail(phone);
        user.setPassword(password);
        authenticate(user);

    }

    private void authenticate(User user){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {

                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogbuilder.setMessage("Incorrect user details");
        dialogbuilder.setPositiveButton("ok", null);
        dialogbuilder.show();
    }

    private void logUserIn(User returnedUser){
        savePreference(LoginActivity.this, JF_ID, returnedUser.getId());
        savePreference(LoginActivity.this, JF_CONTACT_NUMBER, returnedUser.getPhoneNumber());
        savePreference(LoginActivity.this, JF_FNAME, returnedUser.getFname());
        savePreference(LoginActivity.this, JF_LNAME, returnedUser.getLname());
        savePreference(LoginActivity.this, JF_EMAIL, returnedUser.getEmail());


        setUserLoggedIn(LoginActivity.this, true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!TextUtils.isEmpty(etPassword.getEditText().getText())) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    etPassword.getEditText().setTransformationMethod(null);
                    etPassword.getEditText().setSelection(etPassword.getEditText().getText().length());
                    break;

                case MotionEvent.ACTION_UP:
                    etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                    etPassword.getEditText().setSelection(etPassword.getEditText().getText().length());
                    break;

            }
        }

        return false;
    }

    private boolean isInputValid() {

        if (!isInputted(this, etMobileNumber)) {
            return false;
        }

        if (!isInputted(this, etPassword)) {
            return false;
        }

        return true;
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == SIGNED_IN_ACTION) {
            doLoginRequest(etMobileNumber.getEditText().getText().toString(),
                    etPassword.getEditText().getText().toString());
        }
    }

    @Override
    public void onUserCanceled(int code) {

    }


}