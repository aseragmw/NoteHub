package com.example.notehub.presentation.screens;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notehub.R;

public class LoginOrContinueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_or_continue);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();

        Button loginButton = findViewById(R.id.btn_login_choiceScreen);
        Button registerButton = findViewById(R.id.btn_signup_choiceScreen);
        Button continueButton = findViewById(R.id.btn_continue_choiceScreen);

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));

        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));

        });

        continueButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.appBar_choiceScreen);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView textView = new TextView(this);
        textView.setText("NoteHub");
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextSize(30);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.START);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.START);
        textView.setLayoutParams(layoutParams);
        toolbar.addView(textView);

    }
}