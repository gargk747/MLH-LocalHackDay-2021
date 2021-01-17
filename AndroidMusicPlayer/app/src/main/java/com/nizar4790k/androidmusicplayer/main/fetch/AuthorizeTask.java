package com.nizar4790k.androidmusicplayer.main.fetch;

import android.os.AsyncTask;

public class AuthorizeTask extends AsyncTask<Void,Void,Void> {




    @Override
    protected Void doInBackground(Void... voids) {

        SpotifyFetch  spotifyFetch = new SpotifyFetch();
        spotifyFetch.authorizeApp();

        return null;
    }
}
