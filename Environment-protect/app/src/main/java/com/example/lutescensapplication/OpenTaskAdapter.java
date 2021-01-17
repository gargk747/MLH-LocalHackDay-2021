package com.example.lutescensapplication;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.androidcountdown.Counter;
import com.white.progressview.HorizontalProgressView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OpenTaskAdapter extends RecyclerView.Adapter<OpenTaskAdapter.ViewHolder> {

    Context context;
    ArrayList<Task> tasks;

    public OpenTaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_open_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Task task = tasks.get(position);
        int pos = position + 1;
        String str = pos + "." + " " + task.getTitle();

        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            final Date startDate = sdf3.parse(task.getStartTime());
            final Date currentDate = new Date();
            Date endDate = sdf3.parse(task.getEndTime());

            LogUtils.e(endDate);
            printDifference(currentDate, startDate);
            long startMinutes = currentDate.getTime() / 60000;
            long endMinutes = endDate.getTime() / 60000;

            long p = startMinutes / endMinutes;
            long finalProgress = p * 100;

            long differenceBetweenStartEnd = endDate.getTime() - startDate.getTime();
            long differenceDays = differenceBetweenStartEnd / (24 * 60 * 60 * 1000);

            final long totalMinutes = differenceDays * 1440;

            long differenceBetweenCurrentAndStart = currentDate.getTime() - startDate.getTime();
            LogUtils.e(differenceBetweenCurrentAndStart);
            long differenceOfDaysBetweenCurrentAndStart = differenceBetweenCurrentAndStart / (24 * 60 * 60 * 1000);

            LogUtils.e(differenceOfDaysBetweenCurrentAndStart);
            long differenceOfHoursBetweenCurrentAndStart = differenceBetweenCurrentAndStart / (60 * 60 * 1000) % 24;
            LogUtils.e(differenceOfHoursBetweenCurrentAndStart);
            long minutesBetweenCurrentAndStart = differenceOfHoursBetweenCurrentAndStart * 60;

            LogUtils.e(differenceBetweenCurrentAndStart);

            long progress = (long) ((float) minutesBetweenCurrentAndStart / totalMinutes * 100);

            holder.progress_task.setProgress((int) progress);

            final CountDownTimer countDownTimer = new CountDownTimer(currentDate.getTime() - startDate.getTime(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long differenceBetweenCurrentAndStart = currentDate.getTime() - startDate.getTime();
                    long differenceOfDaysBetweenCurrentAndStart = differenceBetweenCurrentAndStart / (24 * 60 * 60 * 1000);
                    long differenceOfHoursBetweenCurrentAndStart = differenceBetweenCurrentAndStart / (60 * 60 * 1000) % 24;
                    long minutesBetweenCurrentAndStart = differenceOfHoursBetweenCurrentAndStart * 60;

                    long progress = (long) ((float) minutesBetweenCurrentAndStart / totalMinutes * 100);
                    holder.progress_task.setProgress((int) progress);
                }

                @Override
                public void onFinish() {

                }
            };

            countDownTimer.start();

            long difference = endDate.getTime() - currentDate.getTime();
            long diffSeconds = difference / 1000 % 60;
            long diffMinutes = difference / (60 * 1000) % 60;
            long diffHours = difference / (60 * 60 * 1000) % 24;
            long diffDays = difference / (24 * 60 * 60 * 1000);
            LogUtils.e(diffHours);
            LogUtils.e(diffDays);

            holder.tv_remainingTime.setDate(endDate);

            if (diffHours <= 0 && diffMinutes <= 0) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("tasks").child(task.getRecord_id());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("status").setValue("completed");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            holder.open_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countDownTimer.cancel();

                    Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("tasks").orderByChild("task_id").equalTo(task.getTask_id());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot taskShot : dataSnapshot.getChildren()) {
                                Task t = taskShot.getValue(Task.class);
                                if (t.getStatus().equals("in progress")) {
                                    taskShot.getRef().removeValue();
                                    Toast.makeText(context, "Task cancelled", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tv_taskName.setText(str);


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_taskName;
        ImageView open_cancel;
        Counter tv_remainingTime;
        HorizontalProgressView progress_task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_taskName = itemView.findViewById(R.id.tv_taskName);
            open_cancel = itemView.findViewById(R.id.open_cancel);
            tv_remainingTime = itemView.findViewById(R.id.tv_remainingTime);
            progress_task = itemView.findViewById(R.id.progress_task);
        }
    }

    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }
}
