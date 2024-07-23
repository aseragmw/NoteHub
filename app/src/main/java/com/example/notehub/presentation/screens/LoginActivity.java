package com.example.notehub.presentation.screens;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.notehub.R;
import com.example.notehub.presentation.listeners.AuthCallBack;
import com.example.notehub.presentation.viewmodels.AuthViewModel;
import com.example.notehub.utils.CacheHelper;
import com.example.notehub.utils.Constants;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    AuthViewModel viewModel;
    EditText email,password;
    Button btnRegister;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupToolbar();

        setupView();

        viewModel=new ViewModelProvider(this).get(AuthViewModel.class);
        CacheHelper cacheHelper=new CacheHelper(this);


        btnRegister.setOnClickListener(v -> {
            btnRegister.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            if( email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            else{
                viewModel.login(email.getText().toString(), password.getText().toString(), new AuthCallBack() {
                    @Override
                    public void onSuccess(FirebaseUser user) {

                        Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        boolean isLoggedIn = viewModel.isLoggedIn();
                        Log.d("TAG", "onCreate: "+isLoggedIn);
                        Log.d("TAG", "onCreate: "+user.getEmail());
                        cacheHelper.save(Constants.USER_FULLNAME_CACHE,user.getDisplayName());
                        cacheHelper.save(Constants.USER_ID_CACHE,user.getUid());
                        cacheHelper.save(Constants.USER_EMAIL_CACHE,user.getEmail());
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            btnRegister.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        });

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        email = findViewById(R.id.et_emailLoginScreen);
        password = findViewById(R.id.et_passwordLoginScreen);
        btnRegister = findViewById(R.id.btn_loginScreen);
        progressBar=findViewById(R.id.progressBar2);
    }

    void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.appBar_loginScreen);
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

        Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.back_icon);
        getSupportActionBar().setHomeAsUpIndicator(backIcon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}