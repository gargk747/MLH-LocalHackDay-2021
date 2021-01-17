package com.example.lutescensapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SharedTasksAdapter extends RecyclerView.Adapter<SharedTasksAdapter.ViewHolder> {
    ArrayList<SharedTask> tasks;
    Context context;

    public SharedTasksAdapter(ArrayList<SharedTask> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shared_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final SharedTask task = tasks.get(position);
        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
        try {
            Date startDate = sdf3.parse(task.getStartTime());
            Date currentDate = new Date();
            Date endDate = sdf3.parse(task.getEndTime());
            Date dateOfSharing = sdf3.parse(task.getSharedAt());

            long differenceBetweenStartEnd = endDate.getTime() - startDate.getTime();
            long differenceDays = differenceBetweenStartEnd / (24 * 60 * 60 * 1000);
            Log.e("ADAPTER", " differenceDays " + differenceDays);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String str = simpleDateFormat.format(dateOfSharing);

            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
            String time = localDateFormat.format(dateOfSharing);

            holder.tv_dateOfCompletion.setText(str);
            holder.tv_timeShared.setText(time);
            if (differenceDays <= 0) {
                holder.tv_days.setText("Task days: " + 1);
            } else
                holder.tv_days.setText("Task days: " + differenceDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("uid").equalTo(task.getSharedBy());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        String image = "" + snapshot.child("image").getValue();
                        holder.tv_sharedBy.setText("Shared by: " + user.getNickname());
                        try{
                            Picasso.get().load(image).into(holder.iv_homePostImage);
                        } catch (Exception e){
                            Picasso.get().load(R.drawable.ic_profile_image).into(holder.iv_homePostImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.tv_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Comments.class);
                intent.putExtra("id", task.getId());
                intent.putExtra("sharedBy", task.getSharedBy());
                context.startActivity(intent);
            }
        });

        int pos = position + 1;
        String str = "" + task.getTitle();
        holder.tv_taskName.setText(str);
        getComments(task.getId(), holder.tv_comments);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_taskName, tv_days, tv_dateOfCompletion, tv_timeShared, tv_sharedBy, tv_comments;
        ImageView iv_homePostImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_taskName = itemView.findViewById(R.id.tv_taskName);
            tv_days = itemView.findViewById(R.id.tv_days);
            tv_dateOfCompletion = itemView.findViewById(R.id.tv_dateOfCompletion);
            tv_timeShared = itemView.findViewById(R.id.tv_timeShared);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            tv_sharedBy = itemView.findViewById(R.id.tv_sharedBy);
            iv_homePostImage = itemView.findViewById(R.id.iv_homePostImage);
        }
    }

    public void getComments(String sharedTaskId, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(sharedTaskId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("View all " +dataSnapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
