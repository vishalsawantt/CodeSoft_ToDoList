package com.example.todolist;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener {

    private TextView textViewWelcome;
    private TextView textViewTaskCount;
    private TextView textViewImportantCount;
    private RecyclerView rechomescreen;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;
    private FloatingActionButton fab;
    private RelativeLayout congratulationsLayout;
    private Button buttonRemoveAllTasks;
    private boolean isCongratulatoryMessageVisible = false;
    private TextView textViewDateTime;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check SharedPreferences for user name
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("USER_NAME", null);

        Window window = getWindow();
        window.setStatusBarColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        if (userName == null) {
            // If user name is not found, redirect to SplashScreen
            Intent intent = new Intent(MainActivity.this, SplachScreen.class); // Fixed class name to SplashScreen
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        rechomescreen = findViewById(R.id.rechomescreen);
        fab = findViewById(R.id.btnaddtask);
        textViewTaskCount = findViewById(R.id.textViewTaskCount);
        textViewImportantCount = findViewById(R.id.textViewImportantCount);
        buttonRemoveAllTasks = findViewById(R.id.buttonRemoveAllTasks);
        congratulationsLayout = findViewById(R.id.congratulationsLayout);

        // Initialize task list and adapter
        taskList = NoteManager.getNotes(this); // Load notes from SharedPreferences
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        taskAdapter = new TaskAdapter(taskList, this, rechomescreen);
        rechomescreen.setAdapter(taskAdapter);
        rechomescreen.setLayoutManager(new LinearLayoutManager(this));


        TaskItemDecoration itemDecoration = new TaskItemDecoration(
                5, // lineWidth
                Color.RED, // incompleteColor
                Color.GREEN, // completedColor
                Color.BLUE, // circleColor
                Color.BLACK, // circleBorderColor
                19, // circleRadius
                3, // circleBorderWidth
                -17 // lineOffset
        );
        rechomescreen.addItemDecoration(itemDecoration);


        fab.setOnClickListener(v -> showAddTaskDialog());
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewWelcome.setText("Welcome, " + userName + "!");

        // Initially hide congratulatory message and button
        congratulationsLayout.setVisibility(View.GONE);
        buttonRemoveAllTasks.setVisibility(View.GONE);

        updateTaskCount(); // Initial count update

        buttonRemoveAllTasks.setOnClickListener(v -> {
            removeAllTasks();
            // Hide the congratulatory message after removing tasks
            congratulationsLayout.setVisibility(View.GONE);
            buttonRemoveAllTasks.setVisibility(View.GONE);
        });

        textViewDateTime = findViewById(R.id.textViewDateTime);

        startUpdatingTime();


        ImageView imageViewImportant = findViewById(R.id.imageViewImportant);
        imageViewImportant.setOnClickListener(v -> {
            if (taskList.isEmpty()) {
                // Show toast message if there are no tasks
                Toast.makeText(MainActivity.this, "No tasks available.", Toast.LENGTH_SHORT).show();
            } else {
                boolean allTasksCompleted = taskList.stream().allMatch(Task::isCompleted);

                if (allTasksCompleted) {
                    if (!isCongratulatoryMessageVisible) {
                        // Show congratulatory message and remove all tasks
                        congratulationsLayout.setVisibility(View.VISIBLE);
                        buttonRemoveAllTasks.setVisibility(View.VISIBLE);
                        isCongratulatoryMessageVisible = true; // Update state
                    }
                } else {
                    if (isCongratulatoryMessageVisible) {
                        // Hide congratulatory message and delete button if tasks are not all completed
                        congratulationsLayout.setVisibility(View.GONE);
                        buttonRemoveAllTasks.setVisibility(View.GONE);
                        isCongratulatoryMessageVisible = false; // Update state
                    }

                    // Show toast with remaining tasks
                    int remainingTasks = (int) taskList.stream().filter(task -> !task.isCompleted()).count();
                    Toast.makeText(MainActivity.this, "Remaining tasks: " + remainingTasks, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        textViewDateTime.setText(currentDateTime);
    }

    private void startUpdatingTime() {
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 60000); // Update every minute
            }
        };
        handler.post(updateTimeRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && updateTimeRunnable != null) {
            handler.removeCallbacks(updateTimeRunnable);
        }
    }


    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Task");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        final EditText inputTitle = view.findViewById(R.id.edtaddtask);
        final EditText inputDescription = view.findViewById(R.id.edtdes);
        final EditText inputTime = view.findViewById(R.id.timeTextView);

        inputTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (TimePicker view1, int hourOfDay, int minuteOfHour) -> {
                        String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                        inputTime.setText(time);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        builder.setView(view);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = inputTitle.getText().toString();
            String description = inputDescription.getText().toString(); // Description can be empty
            String time = inputTime.getText().toString();

            if (!title.isEmpty() && !time.isEmpty()) {
                Task newTask = new Task(title, description.isEmpty() ? null : description, time); // Handle empty description
                NoteManager.saveNote(this, newTask); // Save note to SharedPreferences
                taskList.add(newTask);
                taskAdapter.notifyItemInserted(taskList.size() - 1);
                updateTaskCount(); // Update task count
            } else {
                // Handle case where title or time is empty if necessary
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onItemClick(int position) {
        // Handle task click if needed (or leave it empty if not used)
    }

    @Override
    public void onDeleteClick(int position) {
        // Remove task from the task list and notify adapter
        Task taskToRemove = taskList.get(position);
        taskList.remove(position);
        taskAdapter.notifyItemRemoved(position);

        // Remove task from SharedPreferences
        NoteManager.removeNote(this, taskToRemove);
        updateTaskCount(); // Update task count
    }

    @Override
    public void onEditClick(int position) {
        Task taskToEdit = taskList.get(position);
        showEditTaskDialog(taskToEdit, position);
    }

    private void showEditTaskDialog(Task task, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        final EditText inputTitle = view.findViewById(R.id.edtaddtask);
        final EditText inputDescription = view.findViewById(R.id.edtdes);
        final EditText inputTime = view.findViewById(R.id.timeTextView);

        // Set current task data in the dialog
        inputTitle.setText(task.getTitle());
        inputDescription.setText(task.getDescription() != null ? task.getDescription() : "");
        inputTime.setText(task.getTime());

        inputTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (TimePicker view1, int hourOfDay, int minuteOfHour) -> {
                        String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                        inputTime.setText(time);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        builder.setView(view);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = inputTitle.getText().toString();
            String description = inputDescription.getText().toString();
            String time = inputTime.getText().toString();

            if (!title.isEmpty() && !time.isEmpty()) {
                Task updatedTask = new Task(title, description.isEmpty() ? null : description, time);

                // Update the task in the list and notify adapter
                taskList.set(position, updatedTask);
                taskAdapter.notifyItemChanged(position);

                // Save the updated task list to SharedPreferences
                NoteManager.saveNotes(this, taskList); // Save the entire list
                updateTaskCount(); // Update task count
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void updateTaskCount() {
        int count = taskList.size();
        textViewTaskCount.setText("Tasks: " + count);

        // Calculate important tasks count
        int importantCount = 0;
        for (Task task : taskList) {
            if (task.isImportant()) {
                importantCount++;
            }
        }
        textViewImportantCount.setText("Important: " + importantCount);

        // Show congratulatory message and "Remove All Tasks" button if all tasks are completed
        boolean allTasksCompleted = taskList.stream().allMatch(Task::isCompleted);

        if (allTasksCompleted && count > 0) { // Ensure there is at least one task
            congratulationsLayout.setVisibility(View.VISIBLE);
            buttonRemoveAllTasks.setVisibility(View.VISIBLE);
        } else {
            congratulationsLayout.setVisibility(View.GONE);
            buttonRemoveAllTasks.setVisibility(View.GONE);
        }
    }

    private void removeAllTasks() {
        // Clear tasks from the list
        taskList.clear();
        taskAdapter.notifyDataSetChanged(); // Notify adapter of data change

        // Clear all notes from SharedPreferences
        NoteManager.clearNotes(this);

        // Update UI immediately after clearing tasks
        updateTaskCount(); // Update task count and visibility of UI elements
    }
}
