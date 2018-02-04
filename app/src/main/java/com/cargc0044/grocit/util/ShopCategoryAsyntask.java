package com.cargc0044.grocit.util;

import android.os.AsyncTask;

import com.cargc0044.grocit.interfaces.shopcategorylistinterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Harpal Singh on 27-03-2016.
 */
public class ShopCategoryAsyntask extends AsyncTask<Void, Void, String> {
    URL url;
    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    shopcategorylistinterface Shopcategoryinterface;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String shopid;
    String login_url,JSON_STRING,json_string;



    public ShopCategoryAsyntask(String shopid, shopcategorylistinterface shopcategorylistinterface) {
    this.Shopcategoryinterface=shopcategorylistinterface;
    this.shopid=shopid;
    }



    @Override
    protected void onPreExecute() {
        Shopcategoryinterface.shopcategoryprogress();
        login_url = "http://quiz8tnow.com/grocit/shopcategories.php";
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            url = new URL(login_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

String s=shopid;
            String data = URLEncoder.encode("shop_id", "UTF-8") + "=" + URLEncoder.encode(s);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            inputStream = httpURLConnection.getInputStream();

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {

                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            json_string = stringBuilder.toString().trim();

            return json_string;
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
    Shopcategoryinterface.shopcategorypostresult(result);
Shopcategoryinterface.shopcategoryrogressfinished();
    }

}





