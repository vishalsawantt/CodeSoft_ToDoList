package com.example.todolist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<Task> tasks;
    private OnItemClickListener onItemClickListener;
    private RecyclerView recyclerView;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public TaskAdapter(ArrayList<Task> tasks, OnItemClickListener listener, RecyclerView recyclerView) {
        this.tasks = tasks;
        this.onItemClickListener = listener;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Task currentTask = tasks.get(position);
        holder.titleTextView.setText(currentTask.getTitle());
        holder.descriptionTextView.setText(currentTask.getDescription() != null ? currentTask.getDescription() : "");
        holder.timeTextView.setText(currentTask.getTime());

        holder.checkBoxComplete.setChecked(currentTask.isCompleted());

        // Set the background color based on isImportant status
        int backgroundColor = currentTask.isImportant() ?
                ContextCompat.getColor(holder.itemView.getContext(), R.color.important_task_color) :
                ContextCompat.getColor(holder.itemView.getContext(), R.color.default_task_color);
        holder.itemView.setBackgroundColor(backgroundColor);

        holder.checkBoxComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentTask.setCompleted(isChecked);
            NoteManager.saveNotes(buttonView.getContext(), tasks);
            // Notify item change and invalidate item decorations
            holder.itemView.post(() -> {
                notifyItemChanged(position);
                if (recyclerView != null) {
                    recyclerView.invalidateItemDecorations();
                }
            });
        });

        // Handle long click to toggle importance
        holder.itemView.setOnLongClickListener(v -> {
            boolean isImportant = !currentTask.isImportant();
            currentTask.setImportant(isImportant);
            NoteManager.saveNotes(holder.itemView.getContext(), tasks);
            notifyItemChanged(position);
            ((MainActivity) holder.itemView.getContext()).updateTaskCount(); // Update the important task count
            return true;
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDeleteClick(position);
            }
        });

        holder.editButton.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onEditClick(position);
            }
        });
    }




    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView timeTextView;
        public ImageButton deleteButton;
        public ImageButton editButton;
        public CheckBox checkBoxComplete;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
            checkBoxComplete = itemView.findViewById(R.id.checkBoxComplete);
        }
    }
}
