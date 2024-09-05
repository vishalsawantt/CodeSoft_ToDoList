package com.example.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NoteManager {

    private static final String TASK_LIST_KEY = "task_list";
    private static final String NOTES_KEY = "notes";

    // Save a single task to SharedPreferences
    public static void saveNote(Context context, Task task) {
        ArrayList<Task> taskList = getNotes(context);
        // Remove the old task if it exists to update its status
        taskList.remove(task);
        taskList.add(task);
        saveTaskList(context, taskList);
    }

    // Remove a task from SharedPreferences
    public static void removeNote(Context context, Task task) {
        ArrayList<Task> taskList = getNotes(context);
        taskList.remove(task);
        saveTaskList(context, taskList);
    }

    // Get the list of tasks from SharedPreferences
    public static ArrayList<Task> getNotes(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(TASK_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type) != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    // Save the list of tasks to SharedPreferences
    public static void saveNotes(Context context, ArrayList<Task> taskList) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString(TASK_LIST_KEY, json);
        editor.apply();
    }

    private static void saveTaskList(Context context, ArrayList<Task> taskList) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString(TASK_LIST_KEY, json);
        editor.apply();
    }

    // Clear all tasks from SharedPreferences
    public static void clearNotes(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TASK_LIST_KEY); // Use TASK_LIST_KEY for clearing
        editor.apply();
    }
}
