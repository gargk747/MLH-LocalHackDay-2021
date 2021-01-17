package com.nizar4790k.androidmusicplayer.main.fragment;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nizar4790k.androidmusicplayer.R;
import com.nizar4790k.androidmusicplayer.main.fetch.AuthorizeTask;
import com.nizar4790k.androidmusicplayer.main.fetch.SpotifyFetch;
import com.nizar4790k.androidmusicplayer.main.model.Music;

import java.util.ArrayList;
import java.util.List;

public class LikedSongsListFragment extends Fragment {


    private RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_activity_list,null,false);

        mRecyclerView = view.findViewById(R.id.music_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MusicAdapter adapter = new MusicAdapter(new ArrayList<Music>());
        mRecyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthorizeTask authorizeTask = new AuthorizeTask();
        authorizeTask.execute();

    }


    private class MusicAdapter extends RecyclerView.Adapter<MusicHolder>{

        private List<Music> mMusicList;

        private MusicAdapter(List<Music> musicList){
            mMusicList = musicList;
        }


        @NonNull
        @Override
        public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           LayoutInflater inflater = LayoutInflater.from(getActivity());


            return new MusicHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
            holder.bind(mMusicList.get(position));
        }

        @Override
        public int getItemCount() {
            return mMusicList.size();
        }
    }


    private class MusicHolder extends RecyclerView.ViewHolder{


        private TextView mTextViewArtist;
        private TextView mTextViewTitle;

        public MusicHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.music_activity_list,parent,false));
            mTextViewArtist = itemView.findViewById(R.id.textViewArtist);
            mTextViewTitle = itemView.findViewById(R.id.textViewTitle);

        }

        public void bind (Music music){

            mTextViewTitle.setText(music.getTitle());
            mTextViewArtist.setText(music.getArtist());

        }

    }



}
