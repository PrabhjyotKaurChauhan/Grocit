package com.cargc0044.grocit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.util.ApiHandler;
import com.cargc0044.grocit.util.FloatLabel;
import com.cargc0044.grocit.util.utilMethods;
import com.cargc0044.grocit.util.utilMethods.InternetConnectionListener;

import static com.cargc0044.grocit.util.constants.JF_CONTACT_NUMBER;
import static com.cargc0044.grocit.util.constants.MSG_PASSWORD_CHANGE_FAILED;
import static com.cargc0044.grocit.util.constants.URL_CHANGE_PASSWORD;
import static com.cargc0044.grocit.util.constants.URL_NEW_PASSWORD;
import static com.cargc0044.grocit.util.constants.URL_PROFILE_UPDATE;
import static com.cargc0044.grocit.util.utilMethods.getPreferenceString;
import static com.cargc0044.grocit.util.utilMethods.hideSoftKeyboard;
import static com.cargc0044.grocit.util.utilMethods.isConnectedToInternet;
import static com.cargc0044.grocit.util.Validator.isInputted;
import static com.cargc0044.grocit.util.Validator.isPasswordMatched;
import static com.cargc0044.grocit.util.Validator.isPasswordValid;

/**
 * @author Nguza Yikona.
 * @class ChangePasswordActivity
 * @brief Activity for change user password
 */

public class NewPassword extends Activity implements View.OnClickListener, View.OnTouchListener,
        InternetConnectionListener, ApiHandler.ApiHandlerListener {

    private final int CHANGE_PASSWORD_ACTION = 1;
    private FloatLabel etPassword;
    private AlertDialog resetDialog = null;
    private FloatLabel etRetypePassword;
    private String oldPassword;
    private String Phone;
    private String password;
    private boolean isUserCanceled = false;
    private InternetConnectionListener internetConnectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        findViewById(R.id.btnUpdate).setOnClickListener(this);
        findViewById(R.id.showPasswordImg).setOnTouchListener(this);
        findViewById(R.id.showRetypePasswordImg).setOnTouchListener(this);
        etPassword = (FloatLabel) findViewById(R.id.etPassword);
        etRetypePassword = (FloatLabel) findViewById(R.id.etRetypePassword);
        etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        etRetypePassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
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
                if (isInputValid()) {
                    if (isConnectedToInternet(this)) {
                        doNewPasswordRequest();
                        showVerificationConfirmDialog(NewPassword.this,
                                getResources().getString(R.string.password_reset_heading),
                                getResources().getString(R.string.new_password_body),
                                getResources().getString(R.string.continue_string));
                    } else {
                        internetConnectionListener = NewPassword.this;
                        utilMethods.showNoInternetDialog(NewPassword.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), CHANGE_PASSWORD_ACTION);
                    }
                }
                break;
        }
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
                        startActivity(new Intent(NewPassword.this, LoginActivity.class));
                    }
                })
                .setView(view)
                .setCancelable(false);

        resetDialog = builder.create();
        resetDialog.show();
    }

    private void doNewPasswordRequest() {
        Phone = getPreferenceString(this, JF_CONTACT_NUMBER);
        ContentValues values = new ContentValues();
        password = etPassword.getEditText().getText().toString();
        values.put("phone", Phone);
        values.put("pass", password);
        ApiHandler apiHandler = new ApiHandler(this, URL_NEW_PASSWORD, values);
        apiHandler.doApiRequest(ApiHandler.REQUEST_POST);
    }


    private boolean isInputValid() {

        if (!isInputted(this, etPassword)) {
            return false;
        }

        if (!isPasswordValid(this, etPassword)) {
            return false;
        }

        if (!isInputted(this, etRetypePassword)) {
            return false;
        }

        if (!isPasswordMatched(this, etPassword, etRetypePassword)) {
            return false;
        }

        return true;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.showPasswordImg:
                if (!TextUtils.isEmpty(etPassword.getEditText().getText())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etPassword.getEditText().setTransformationMethod(null);
                        etPassword.getEditText().setSelection(etPassword.getEditText().getText().length());
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                        etPassword.getEditText().setSelection(etPassword.getEditText().getText().length());
                    }
                }
                break;

            case R.id.showRetypePasswordImg:

                if (!TextUtils.isEmpty(etRetypePassword.getEditText().getText())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etRetypePassword.getEditText().setTransformationMethod(null);
                        etRetypePassword.getEditText().setSelection(etRetypePassword.getEditText().getText().length());
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        etRetypePassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                        etRetypePassword.getEditText().setSelection(etRetypePassword.getEditText().getText().length());
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == CHANGE_PASSWORD_ACTION) {
            if (isConnectedToInternet(this)) {
                doNewPasswordRequest();
            }
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == CHANGE_PASSWORD_ACTION) {
            isUserCanceled = true;
            onPause();
        }
    }

    @Override
    public void onSuccessResponse(String tag, String jsonString) {

    }

    @Override
    public void onFailureResponse(String tag) {

    }
}
