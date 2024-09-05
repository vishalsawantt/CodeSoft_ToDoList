package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SplachScreen extends AppCompatActivity {

    private EditText editTextFullName;
    private ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setStatusBarColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        // Check SharedPreferences for user name
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("USER_NAME", null);

        if (userName != null) {
            // If user name is found, redirect to MainActivity
            Intent intent = new Intent(SplachScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.splach_screen);

        editTextFullName = findViewById(R.id.editTextFullName);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String userNameInput = editTextFullName.getText().toString();
            if (userNameInput.isEmpty()) {
                // Show a Toast message if the input is empty
                Toast.makeText(SplachScreen.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            }

            else if(!userNameInput.isEmpty()) {
                // Save the user name to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USER_NAME", userNameInput);
                editor.apply();

                Intent intent = new Intent(SplachScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
