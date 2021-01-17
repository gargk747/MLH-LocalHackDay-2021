package com.example.lutescensapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class HomeFragment extends Fragment {

    private FloatingActionButton fab;
    private DatabaseReference databaseReference;
    private ArrayList<SharedTask> tasks;
    private RecyclerView rv_sharedTasks;
    private ArrayList<String> following;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fab = view.findViewById(R.id.startActionFab);
        rv_sharedTasks = view.findViewById(R.id.rv_sharedTasks);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait. If it takes too long, check your connection or restart the app.");
        progressDialog.setTitle("Loading");
        progressDialog.show();
        progressDialog.setCancelable(false);

        // To handle the click on the floating action button (fab), that brings the user to the TaskMenu.java activity.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllTasks.class);
                startActivity(intent);
            }
        });
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        LogUtils.e(user.getUid());
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid()).child("following");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    following = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LogUtils.e(snapshot.getKey());
                        following.add(snapshot.getKey());
                    }
                } else {
                    following = new ArrayList<>();
                }
                if (following.size() > 0) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("shared_tasks");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                ArrayList<SharedTask> sharedTasks = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    SharedTask t = snapshot.getValue(SharedTask.class);
                                    sharedTasks.add(t);
                                }
                                LogUtils.e(sharedTasks.size());
                                LogUtils.e(following.size());
                                tasks = new ArrayList<>();
                                for (SharedTask t : sharedTasks) {
                                    for (String userId : following) {
                                        LogUtils.e(t.getSharedBy());
                                        if (t.getSharedBy().equals(userId)) {
                                            tasks.add(t);
                                        }
                                    }
                                    if (t.getSharedBy().equals(user.getUid())) {
                                        tasks.add(t);
                                    }
                                }
                                Collections.reverse(tasks);
                                progressDialog.dismiss();
                                rv_sharedTasks.setAdapter(new SharedTasksAdapter(tasks, getContext()));
                                rv_sharedTasks.setLayoutManager(new LinearLayoutManager(getContext()));
                            } else {
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
        return view;
    }
}
