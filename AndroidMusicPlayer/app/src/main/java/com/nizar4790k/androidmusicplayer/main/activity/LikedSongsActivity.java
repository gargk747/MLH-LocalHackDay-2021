package com.nizar4790k.androidmusicplayer.main.activity;

import androidx.fragment.app.Fragment;

import com.nizar4790k.androidmusicplayer.main.fragment.LikedSongsListFragment;

public class LikedSongsActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new LikedSongsListFragment();
    }
}
