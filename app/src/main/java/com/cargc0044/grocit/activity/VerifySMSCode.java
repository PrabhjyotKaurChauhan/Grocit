package com.cargc0044.grocit.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import android.net.Uri;

import android.support.v7.app.ActionBarActivity;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cargc0044.grocit.R;
import com.cargc0044.grocit.model.User;
import com.cargc0044.grocit.util.FloatLabel;
import com.cargc0044.grocit.util.GetUserCallBack;
import com.cargc0044.grocit.util.ServerRequest;
import com.cargc0044.grocit.util.ServerRequest2;
import com.cargc0044.grocit.util.utilMethods;
import com.cargc0044.grocit.util.utilMethods.InternetConnectionListener;

import java.util.Random;

import static com.cargc0044.grocit.util.Validator.isValidEmail;
import static com.cargc0044.grocit.util.constants.JF_CONTACT_NUMBER;
import static com.cargc0044.grocit.util.constants.MSG_PASSWORD_CHANGE_SUCCESS_2;
import static com.cargc0044.grocit.util.utilMethods.isConnectedToInternet;
import static com.cargc0044.grocit.util.Validator.isInputted;
import static com.cargc0044.grocit.util.Validator.isMobileNumberValid;
import static com.cargc0044.grocit.util.Validator.setPhoneCodeListener;
import static com.cargc0044.grocit.util.utilMethods.savePreference;

/**
 * @author Nguza Yikona
 * @class ForgetPassword
 * @brief Activity for sending the password to the user in case of forget
 */

public class VerifySMSCode extends Activity implements View.OnClickListener, InternetConnectionListener {

    private final int FORGET_PASSWORD_ACTION = 1;
    private FloatLabel etMobileNumber;
    private AlertDialog resetDialog = null;
    private boolean isUserCanceled = false;
    private int code;
    private String verify;
    private String msgBody;
    private BroadcastReceiver receiver;
    private InternetConnectionListener internetConnectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_verify_sms);
        findViewById(R.id.btnReset).setOnClickListener(this);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        verify = getIntent().getStringExtra("verify");

        etMobileNumber = (FloatLabel) findViewById(R.id.etMobileNumber);
        etMobileNumber.getEditText().setTextColor(getResources().getColor(R.color.post_business_edit_text_color));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReset:
                //Toast.makeText(this, etMobileNumber.getEditText().getText().toString()+" and "+verify.toString(), Toast.LENGTH_SHORT).show();
                if(etMobileNumber.getEditText().getText().toString().equals(verify)) {
                            showVerificationConfirmDialog(VerifySMSCode.this,
                            getResources().getString(R.string.password_reset_heading),
                            getResources().getString(R.string.password_reset_body),
                            getResources().getString(R.string.continue_string));
                }
                else{
                    showErrorMessage();
                }
                    break;

            case R.id.crossImgView:
                isUserCanceled = true;
                onPause();
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
                        startActivity(new Intent(VerifySMSCode.this, NewPassword.class));
                        dialog.dismiss();
                        isUserCanceled = true;
                        onPause();
                    }
                })
                .setView(view)
                .setCancelable(false);

        resetDialog = builder.create();
        resetDialog.show();
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(VerifySMSCode.this);
        dialogbuilder.setMessage("This number is not registered");
        dialogbuilder.setPositiveButton("ok", null);
        dialogbuilder.show();
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
    public void onConnectionEstablished(int code) {
        if (code == FORGET_PASSWORD_ACTION) {
            showVerificationConfirmDialog(VerifySMSCode.this,
                    getResources().getString(R.string.password_reset_heading),
                    getResources().getString(R.string.password_reset_body),
                    getResources().getString(R.string.continue_string));
        }
    }

    @Override
    public void onUserCanceled(int code) {
        isUserCanceled = true;
        onPause();
    }
}
