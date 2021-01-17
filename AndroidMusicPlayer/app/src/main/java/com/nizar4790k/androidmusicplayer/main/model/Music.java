package com.nizar4790k.androidmusicplayer.main.model;

public class Music {

    private String mId;
    private String mTitle;
    private String mArtist;


    public Music(String id, String title, String artist) {
        mId = id;
        mTitle = title;
        mArtist = artist;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }
}
