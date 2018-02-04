package com.cargc0044.grocit.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cargc0044.grocit.model.User;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static org.apache.http.params.HttpConnectionParams.setConnectionTimeout;
import static org.apache.http.params.HttpConnectionParams.setSoTimeout;

/**
 * Created by Nguza on 15/02/16.
 */
public class ServerRequest {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "";
    ProgressDialog progressDialog;
    String json_string;
    JSONObject jsonObject;

    public ServerRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallBack usercallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, usercallBack).execute();

    }

    public void fetchUserDataInBackground(User user, GetUserCallBack usercallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, usercallBack).execute();

    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, User> {

        User user;
        GetUserCallBack userCallBack;
        String JSON_STRING;
        String register_url;

        public StoreUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;


        }

        @Override
        protected void onPreExecute() {
            register_url = "http://quiz8tnow.com/grocit/registeruser.php";
        }

        @Override
        protected User doInBackground(Void... params) {
            try {
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(user.getFname(), "UTF-8") + "&" +
                        URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode(user.getLname(), "UTF-8")+ "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8")+ "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(user.getPhoneNumber(), "UTF-8")+ "&" +
                        URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(user.getPassword(), "UTF-8");
                Log.d("String Data", data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {

                    stringBuilder.append(JSON_STRING + "\n");
                }
                json_string = stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("JSON REGISTER", json_string);
                jsonObject = new JSONObject(json_string);

                String id = jsonObject.optString("user_id");
                String fname = jsonObject.optString("fname");
                String lname = jsonObject.optString("lname");
                String email = jsonObject.optString("email");
                String phone = jsonObject.optString("phone");



                user.setId(id);
                user.setFname(fname);
                user.setLname(lname);
                user.setEmail(email);
                user.setPhoneNumber(phone);


                return user;

            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data [" + e.getMessage() + "] " + json_string);
            }
            return null;
        }
        @Override
        protected void onPostExecute(User returnedUser) {

            progressDialog.dismiss();


            userCallBack.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        String login_url;
        String JSON_STRING;
        User user;
        GetUserCallBack userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected void onPreExecute() {
            login_url = "http://quiz8tnow.com/grocit/fetchUserData.php";
        }

        @Override
        protected User doInBackground(Void... params) {



            // Login Part

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&" +
                        URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(user.getPassword(), "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d(data,"data");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {

                    stringBuilder.append(JSON_STRING + "\n");
                }
                json_string = stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("JSON", json_string);

                jsonObject = new JSONObject(json_string);

                String id = jsonObject.optString("user_id");
                String fname = jsonObject.optString("fname");
                String lname = jsonObject.optString("lname");
                String email = jsonObject.optString("email");
                String phone = jsonObject.optString("phone");



                user.setId(id);
                user.setFname(fname);
                user.setLname(lname);
                user.setEmail(email);
                user.setPhoneNumber(phone);


                return user;

            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data [" + e.getMessage() + "] " + json_string);
            }
            return null;
        }

        @Override
        protected void onPostExecute(User returnedUser) {

            progressDialog.dismiss();


            userCallBack.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

}
