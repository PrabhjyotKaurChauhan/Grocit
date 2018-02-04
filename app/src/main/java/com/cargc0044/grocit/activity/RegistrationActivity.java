package com.cargc0044.grocit.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.model.User;
import com.cargc0044.grocit.util.FloatLabel;
import com.cargc0044.grocit.util.GetUserCallBack;
import com.cargc0044.grocit.util.ServerRequest;
import com.cargc0044.grocit.util.utilMethods;

import static com.cargc0044.grocit.util.Validator.isInputted;
import static com.cargc0044.grocit.util.Validator.isMobileNumberValid;
import static com.cargc0044.grocit.util.Validator.isPasswordValid;
import static com.cargc0044.grocit.util.Validator.isValidEmail;
import static com.cargc0044.grocit.util.utilMethods.isConnectedToInternet;


/**
 * Created by Hahn on 24/01/16.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private final int SIGNED_UP_ACTION = 1;
    private FloatLabel etFirstName;
    private FloatLabel etLastName;
    private FloatLabel etUsermail;
    private FloatLabel etMobileNumber;
    private FloatLabel etPassword;

    protected TextView signup_button;
    protected TextView GoToSignin;
    private utilMethods.InternetConnectionListener internetConnectionListener;

    protected void onCreate(Bundle SavedInstancesState) {

        super.onCreate(SavedInstancesState);
        setContentView(R.layout.register);
        GoToSignin=(TextView)findViewById(R.id.GoToSignin);
        signup_button = (TextView) findViewById(R.id.btnSignUpz);


        etFirstName = (FloatLabel) findViewById(R.id.etFirstName);
        etLastName = (FloatLabel) findViewById(R.id.etLastName);
        etUsermail = (FloatLabel) findViewById(R.id.etUsermail);
        etMobileNumber = (FloatLabel) findViewById(R.id.etMobileNumber);
        etPassword = (FloatLabel) findViewById(R.id.etPassword);

        etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());


       // GoToSignin.setOnClickListener(this);

        signup_button.setOnClickListener(this);

    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUpz:
                if (isInputValid()) {
                    if (isConnectedToInternet(RegistrationActivity.this)) {

                        String fname = etFirstName.getEditText().getText().toString();
                        String lname= etLastName.getEditText().getText().toString().trim();
                        String email = etUsermail.getEditText().getText().toString().trim();
                        String phoneNumber = etMobileNumber.getEditText().getText().toString().trim();
                        String password = etPassword.getEditText().getText().toString().trim();

                        User user= new User();
                        user.setFname(fname);
                        user.setLname(lname);
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setPhoneNumber(phoneNumber);
                        registerUser(user);

                        Toast.makeText(
                                getApplicationContext(), "Your Account has been Created! Sign up To Continue",Toast.LENGTH_LONG).show();

                    } else {

                        internetConnectionListener = (utilMethods.InternetConnectionListener) RegistrationActivity.this;
                        utilMethods.showNoInternetDialog(RegistrationActivity.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), SIGNED_UP_ACTION);
                    }
                }

                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

                break;

            case R.id.GoToSignin:
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                boolean isUserCanceled = true;
                onPause();
                break;

        }
    }

    private void registerUser(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser != null) {
                    showErrorMessage();
                } else {

                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }


            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(RegistrationActivity.this);
        dialogbuilder.setMessage("Phone or Email entered already exists");
        dialogbuilder.setPositiveButton("ok", null);
        dialogbuilder.show();
    }

    private boolean isInputValid() {

        if (!isInputted(this, etFirstName)) {
            return false;
        }

        if (!isInputted(this, etLastName)) {
            return false;
        }
        if (!isMobileNumberValid(this, etMobileNumber)){
            return false;
        }
        if (!isValidEmail(this, etUsermail)){
            return false;
        }
        if (!isPasswordValid(this, etPassword)){
            return false;
        }


        return true;
    }

}
