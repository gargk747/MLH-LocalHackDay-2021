package com.example.lutescensapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OpenTasks extends AppCompatActivity {

    RecyclerView rv_openTasks;
    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_tasks);

        rv_openTasks = findViewById(R.id.rv_openTasks);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching tasks");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("tasks")) {
                        tasks = new ArrayList<>();
                        for (DataSnapshot taskSnapshot : dataSnapshot.child("tasks").getChildren()) {
                            Task task = taskSnapshot.getValue(Task.class);
                            if (task.getStatus().equals("in progress")) {
                                tasks.add(task);
                            }
                        }
                        progressDialog.dismiss();
                        rv_openTasks.setAdapter(new OpenTaskAdapter(OpenTasks.this, tasks));
                        rv_openTasks.setLayoutManager(new LinearLayoutManager(OpenTasks.this));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(OpenTasks.this, "You have no active tasks. Start one!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(OpenTasks.this, "You have no active tasks. Start one!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(OpenTasks.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
