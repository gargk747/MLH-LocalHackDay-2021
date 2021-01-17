package com.example.lutescensapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    private ArrayList<Task> tasks;
    private Context context;

    public TasksAdapter(ArrayList<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_action, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task task = tasks.get(position);

        holder.titleAction.setText(task.getTitle());
        holder.descriptionAction.setText(task.getHeadline());
        Glide.with(context)
                .load(task.getLogoUrl())
                .into(holder.logoAction);

        holder.vg_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TaskDetails.class)
                        .putExtra("task",task)
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logoAction;
        TextView titleAction, descriptionAction;
        LinearLayout vg_task;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionAction = itemView.findViewById(R.id.descriptionAction);
            titleAction = itemView.findViewById(R.id.titleAction);
            logoAction = itemView.findViewById(R.id.logoAction);
            vg_task = itemView.findViewById(R.id.vg_task);
        }
    }
}
