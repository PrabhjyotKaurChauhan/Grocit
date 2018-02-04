package com.cargc0044.grocit.util;

import android.os.AsyncTask;

import com.cargc0044.grocit.interfaces.productinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Harpal Singh on 27-03-2016.
 */
public class ProductsAsyntask extends AsyncTask<Void, Void, String> {
    URL url;
    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
  productinterface productinterface;
    String login_url,JSON_STRING,json_string;

    public ProductsAsyntask(productinterface productinterface) {
    this.productinterface=productinterface;
    }



    @Override
    protected void onPreExecute() {
        productinterface.productprogress();
        login_url = "http://quiz8tnow.com/grocit/getproducts.php";
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
          url = new URL(login_url);
             httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);


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
       productinterface.productpostresult(result);
    productinterface.progressprogressfinished();
    }
}





