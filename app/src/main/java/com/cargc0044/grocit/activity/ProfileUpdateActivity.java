package com.cargc0044.grocit.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.model.User;
import com.cargc0044.grocit.util.ApiHandler;
import com.cargc0044.grocit.util.FloatLabel;
import com.cargc0044.grocit.util.utilMethods;
import com.cargc0044.grocit.util.utilMethods.InternetConnectionListener;

import static com.cargc0044.grocit.util.constants.JF_CONTACT_NUMBER;
import static com.cargc0044.grocit.util.constants.JF_EMAIL;
import static com.cargc0044.grocit.util.constants.JF_ID;
import static com.cargc0044.grocit.util.constants.JF_NAME;
import static com.cargc0044.grocit.util.constants.JL_NAME;
import static com.cargc0044.grocit.util.constants.URL_PROFILE_UPDATE;
import static com.cargc0044.grocit.util.utilMethods.getPreferenceString;
import static com.cargc0044.grocit.util.utilMethods.hideSoftKeyboard;
import static com.cargc0044.grocit.util.utilMethods.isConnectedToInternet;
import static com.cargc0044.grocit.util.utilMethods.isUserSignedIn;
import static com.cargc0044.grocit.util.utilMethods.savePreference;
import static com.cargc0044.grocit.util.Validator.isInputted;
import static com.cargc0044.grocit.util.Validator.isMobileNumberValid;
import static com.cargc0044.grocit.util.Validator.isValidEmail;
import static com.cargc0044.grocit.util.Validator.setPhoneCodeListener;

/**
 * @author Nguza Yikona.
 * @class ProfileUpdateActivity
 * @brief Activity of updating user information
 */
public class ProfileUpdateActivity extends Activity implements View.OnClickListener, InternetConnectionListener,
        ApiHandler.ApiHandlerListener {

    private final int PROFILE_UPDATE_ACTION = 1;
    private FloatLabel etMobileNumber;
    private FloatLabel etFirstName;
    private FloatLabel etLastName;
    private FloatLabel etEmail;
    private boolean isUserCanceled = false;
    private InternetConnectionListener internetConnectionListener;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        etMobileNumber = (FloatLabel) findViewById(R.id.etMobileNumber);
        etFirstName = (FloatLabel) findViewById(R.id.etFirstName);
        etLastName = (FloatLabel) findViewById(R.id.etLastName);
        etEmail = (FloatLabel) findViewById(R.id.etEmail);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        findViewById(R.id.btnUpdate).setOnClickListener(this);
        etMobileNumber.getEditText().setOnFocusChangeListener(setPhoneCodeListener(this));

        if (isUserSignedIn(this)) {
            getUserInfo();
            etFirstName.getEditText().setText(user.getFname());
            etLastName.getEditText().setText(user.getLname());
            etMobileNumber.getEditText().setText(user.getPhoneNumber());
            etMobileNumber.getEditText().setClickable(false);
            etMobileNumber.getEditText().setFocusable(false);
            etMobileNumber.getEditText().setFocusableInTouchMode(false);
            etMobileNumber.getEditText().setCursorVisible(false);
            etEmail.getEditText().setText(user.getEmail());
        }
    }

    private void getUserInfo() {
        user = new User();
        user.setId(getPreferenceString(this, JF_ID));
        user.setPhoneNumber(getPreferenceString(this, JF_CONTACT_NUMBER));
        user.setFname(getPreferenceString(this, JF_NAME));
        user.setLname(getPreferenceString(this, JL_NAME));
        user.setEmail(getPreferenceString(this, JF_EMAIL));


    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isUserCanceled) {
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            finish();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.crossImgView:
                hideSoftKeyboard(this);
                isUserCanceled = true;
                onPause();
                break;

            case R.id.btnUpdate:
                if (inputValid()) {
                    if (isConnectedToInternet(ProfileUpdateActivity.this)) {
                        updateProfile(etFirstName.getEditText().getText().toString(),
                                etLastName.getEditText().getText().toString(),
                                etMobileNumber.getEditText().getText().toString(),
                                etEmail.getEditText().getText().toString());
                    } else {
                        internetConnectionListener = ProfileUpdateActivity.this;
                        utilMethods.showNoInternetDialog(ProfileUpdateActivity.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), PROFILE_UPDATE_ACTION);
                    }

                }
                break;
        }
    }

    private void updateProfile(String name,String lname, String mobileNumber, String email) {
        User user = new User();
        user.setPhoneNumber(mobileNumber);
        user.setLname(name);
        user.setLname(lname);
        user.setEmail(email);
        savePreference(ProfileUpdateActivity.this, JF_ID, user.getId());
        savePreference(ProfileUpdateActivity.this, JF_CONTACT_NUMBER, user.getPhoneNumber());
        savePreference(ProfileUpdateActivity.this, JF_NAME, user.getFname());
        savePreference(ProfileUpdateActivity.this, JL_NAME, user.getLname());
        savePreference(ProfileUpdateActivity.this, JF_EMAIL, user.getEmail());
        ContentValues values = new ContentValues();
        values.put("user_id", user.getId());
        values.put("fname", user.getFname());
        values.put("lname", user.getLname());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhoneNumber());
        ApiHandler apiHandler = new ApiHandler(this, URL_PROFILE_UPDATE, values);
        apiHandler.doApiRequest(ApiHandler.REQUEST_POST);
        Toast.makeText(ProfileUpdateActivity.this, getResources().getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
        isUserCanceled = true;
        onPause();

    }

    private boolean inputValid() {

        if (!isInputted(this, etFirstName)) {
            return false;
        }

        if (!isInputted(this, etLastName)) {
            return false;
        }

        if (!isInputted(this, etMobileNumber)) {
            return false;
        }

        if (!isMobileNumberValid(this, etMobileNumber)) {
            return false;
        }

        if (!isInputted(this, etEmail)) {
            return false;
        }

        if (!isValidEmail(this, etEmail)) {
            return false;
        }

        return true;
    }


    @Override
    public void onConnectionEstablished(int code) {
        if (code == PROFILE_UPDATE_ACTION) {
            updateProfile(etFirstName.getEditText().getText().toString(),
                    etLastName.getEditText().getText().toString(),
                    etMobileNumber.getEditText().getText().toString(),
                    etEmail.getEditText().getText().toString());
        }
    }

    @Override
    public void onUserCanceled(int code) {

    }

    @Override
    public void onSuccessResponse(String tag, String jsonString) {

    }

    @Override
    public void onFailureResponse(String tag) {

    }
}
