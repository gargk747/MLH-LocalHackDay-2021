package com.example.lutescensapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.ViewHolder> {

    Context context;
    ArrayList<Task> tasks;

    public CompletedTaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_completed_task, parent
                , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Task task = tasks.get(position);
        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            Date startDate = sdf3.parse(task.getStartTime());
            Date currentDate = new Date();
            Date endDate = sdf3.parse(task.getEndTime());

            long differenceBetweenStartEnd = endDate.getTime() - startDate.getTime();
            long differenceDays = differenceBetweenStartEnd / (24 * 60 * 60 * 1000);
            Log.e("ADAPTER", " differenceDays " + differenceDays);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String str = simpleDateFormat.format(endDate);

            holder.tv_dateOfCompletion.setText(str);
            if (differenceDays <= 0) {
                holder.tv_days.setText("Days: " + 1);
            } else
                holder.tv_days.setText("Days: " + differenceDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int pos = position + 1;
        String str = pos + "." + " " + task.getTitle();
        holder.tv_taskName.setText(str);

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("shared_tasks").push();
                SharedTask sharedTask = new SharedTask();

                sharedTask.setSharedBy(user.getUid());

                sharedTask.setDescription(task.getDescription());
                sharedTask.setLog_url(task.getLogoUrl());
                sharedTask.setHeadline(task.getHeadline());
                sharedTask.setTitle(task.getTitle());
                sharedTask.setStartTime(task.getStartTime());
                sharedTask.setEndTime(task.getEndTime());
                sharedTask.setTaskId(task.getTask_id());
                Calendar calendar = Calendar.getInstance();
                Date sharedAt = calendar.getTime();
                sharedTask.setSharedAt(sharedAt.toString());
                databaseReference.setValue(sharedTask);

                final String shareId = databaseReference.getKey();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("shared_tasks").child(shareId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("id").setValue(shareId);
                        Toast.makeText(context, "Task shared", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(context, "Shared on Home", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_taskName, tv_days, tv_dateOfCompletion;
        ImageView img_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_taskName = itemView.findViewById(R.id.tv_taskName);
            tv_days = itemView.findViewById(R.id.tv_days);
            tv_dateOfCompletion = itemView.findViewById(R.id.tv_dateOfCompletion);
            img_share = itemView.findViewById(R.id.img_share);
        }
    }
}
