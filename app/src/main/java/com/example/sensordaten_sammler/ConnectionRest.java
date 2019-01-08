package com.example.sensordaten_sammler;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectionRest extends AsyncTask<String, Void, Object> {

    Consumer<JSONArray> fun;
    public ConnectionRest (Consumer<JSONArray> c){
        this.fun = c;
    }
    public ConnectionRest (){
        super();
    }

    @Override
    protected Object doInBackground(String... params) {
        String urlstring = "http://sbcon.ddns.net:3000/api/"+params[0];
        OkHttpClient client = new OkHttpClient();
        Request request;
        //POST
        if(params.length>1){
            Log.d("XRESTPOST",urlstring+params[1]);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, params[1]);
            request = new Request.Builder()
                    .url(urlstring)
                    .post(body)
                    .build();
        }//GET
        else{
            Log.d("XRESTGET",urlstring);
            request = new Request.Builder()
                    .url(urlstring)
                    .build();
        }
        Response response = null;
        try{
            response = client.newCall(request).execute();
            //Log.d("Response", response.body().string());
            if(response.body()!=null)
                return new JSONArray(response.body().string());
            else return response;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
   }
    @Override
    protected void onPostExecute(Object jsonarrdata) {
        super.onPostExecute(jsonarrdata);
        if(this.fun!=null)
            this.fun.accept((JSONArray) jsonarrdata);
    }
}
