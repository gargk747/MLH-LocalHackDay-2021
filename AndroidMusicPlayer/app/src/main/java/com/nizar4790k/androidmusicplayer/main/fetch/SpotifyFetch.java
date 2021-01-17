package com.nizar4790k.androidmusicplayer.main.fetch;

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpotifyFetch {


    private static final String TAG="SpotifyApp";

    private static final String CLIENT_ID="fb38a5ab81c947c7ac053ce21a289bbe";



    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }

    }



    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public void fetchItems(){

        try {
            String url = Uri.parse("https://api.spotify.com/v1")
                    .buildUpon()
                    .appendQueryParameter(null,null)
                    .build()
                    .toString();

            String jsonString = getUrlString(url);


        }catch (IOException ioe){

        }

    }



    public void authorizeApp(){

        try{
            String url = Uri.parse("https://accounts.spotify.com/authorize")
                    .buildUpon()
                    .appendQueryParameter("client_id",CLIENT_ID)
                    .appendQueryParameter("response_type","code")
                    .appendQueryParameter("redirect_uri","https://api.spotify.com/v1")
                    .build()
                    .toString();


            String jsonString = getUrlString(url);
            Log.i(TAG,"Recieved json" + jsonString);

        }catch (IOException ioe){

            Log.i(TAG,"Failed to fetch items",ioe);

        }




    }



}
