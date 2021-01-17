package com.example.lutescensapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

import java.util.Calendar;
import java.util.Date;

public class TaskDetails extends AppCompatActivity {
    Task task;

    TextView tv_description;
    MultiLineRadioGroup rg_multi;
    Button btn_save;
    Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent i = getIntent();
        task = i.getParcelableExtra("task");

        tv_description = findViewById(R.id.tv_description);
        rg_multi = findViewById(R.id.rg_multi);
        btn_save = findViewById(R.id.btn_save);

        tv_description.setText(task.getDescription());

        rg_multi.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup viewGroup, RadioButton button) {
                int period = Integer.parseInt(button.getText().toString());
                Date date = new Date();
                task.setStartTime(date.toString());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, period);
                endDate = calendar;
                date = calendar.getTime();
                task.setEndTime(date.toString());
                btn_save.setVisibility(View.VISIBLE);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("tasks").push();
                task.setStatus("in progress");
                databaseReference.setValue(task);
                final String key = databaseReference.getKey();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("tasks").child(key);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("record_id").setValue(key);
                        Toast.makeText(TaskDetails.this, "Task assigned", Toast.LENGTH_SHORT).show();


                        // When the time of the task will finish, the app will send a notification to the user
                        // and mark the app as completed, moving it to the completed task screen.
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        NotifyMe.Builder notifyMe = new NotifyMe.Builder(getApplicationContext());
                        notifyMe.title("'"+task.getTitle()+"'"+" task completed");
                        notifyMe.color(51, 204, 51, 255);
                        notifyMe.led_color(51, 204, 51, 255);
                        notifyMe.time(endDate);
                        notifyMe.key("Task completed");
                        notifyMe.large_icon(R.mipmap.ic_launcher_round);
                        notifyMe.rrule("FREQ=MINUTELY;INTERVAL=5;COUNT=2");
                        notifyMe.addAction(new Intent(), "Dismiss", true, false);
                        notifyMe.build();
                        startActivity(new Intent(TaskDetails.this, UserProfile.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
