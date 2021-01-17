package com.example.lutescensapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllTasks extends AppCompatActivity {
    RecyclerView rv_tasks;
    String TAG = "AllTasks";
    ArrayList<Task> tasks;
    ArrayList<Task> finalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        rv_tasks = findViewById(R.id.rv_tasks);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("The application is loading... If it takes a lot, close the app and check your connection.");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.show();

        tasks = new ArrayList<>();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("tasks");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tasks = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Task task = snapshot.getValue(Task.class);
                        Log.e(TAG, task.getTitle());
                        tasks.add(task);
                    }
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.hasChild("tasks")) {
                                    finalList = new ArrayList<>();
                                    finalList.addAll(tasks);
                                    ArrayList<Task> userTasks = new ArrayList<>();
                                    for (DataSnapshot taskSnapshot : dataSnapshot.child("tasks").getChildren()) {
                                        Task task = taskSnapshot.getValue(Task.class);
                                        userTasks.add(task);
                                    }
                                    // If a task is already in progress, the application removes it from the listView to avoid
                                    // the user to create multiple of the same type.
                                    for (Task userTask : userTasks) {
                                        for (Task task : tasks) {
                                            if (userTask.getTask_id().equals(task.getTask_id())) {
                                                if (userTask.getStatus().equals("in progress"))
                                                    finalList.remove(task);
                                            }
                                        }
                                    }
                                    progressDialog.dismiss();
                                    rv_tasks.setAdapter(new TasksAdapter(finalList, AllTasks.this));
                                    rv_tasks.setLayoutManager(new LinearLayoutManager(AllTasks.this));
                                } else {
                                    progressDialog.dismiss();
                                    rv_tasks.setAdapter(new TasksAdapter(tasks, AllTasks.this));
                                    rv_tasks.setLayoutManager(new LinearLayoutManager(AllTasks.this));
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Toast.makeText(AllTasks.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(AllTasks.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
