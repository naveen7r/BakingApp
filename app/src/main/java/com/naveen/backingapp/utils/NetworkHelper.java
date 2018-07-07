package com.naveen.backingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Naveen on 4/21/2018.
 */

public class NetworkHelper {

//    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String DOMAIN = "d17h27t6h515a5.cloudfront.net";

    private static final String BASE_URL = "https://"+DOMAIN;
    public static final String BAKING_URL = BASE_URL + "/topher/2017/May/59121517_baking/baking.json";



    private static final String TAG = NetworkHelper.class.getCanonicalName();




    public static URL constructURL(String urlString){
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            if(urlConnection.getResponseCode() == 200) {

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } else {
                String location = urlConnection.getHeaderField("Location");
                return getResponseFromHttpUrl(new URL(location));
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String getResponseFromHttpUrl(URL url, HashMap<String, String> params) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream out = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();

            if(urlConnection.getResponseCode() == 200) {

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } else {
                String location = urlConnection.getHeaderField("Location");
                return getResponseFromHttpUrl(new URL(location));
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        if(params != null) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.get(key), "UTF-8"));
            }
        }

        return result.toString();
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
        return false;
    }
}
