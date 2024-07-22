package com.example.notehub.presentation.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notehub.R;
import com.example.notehub.presentation.viewmodels.AuthViewModel;
import com.example.notehub.utils.CacheHelper;


public class SplashActivity extends AppCompatActivity {

    AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CacheHelper sharedPreferences = new CacheHelper(this);
        boolean flagExists = sharedPreferences.contains("firstTime");
        boolean firstTime =true;
        if(flagExists){
            firstTime = (boolean) sharedPreferences.get("firstTime",true);
        }
        else{
            sharedPreferences.save("firstTime",true);
        }

        boolean finalFirstTime = firstTime;
        new Handler().postDelayed(new Runnable () {
            @Override
            public void run() {
                if(!finalFirstTime){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                else{
                    sharedPreferences.save("firstTime",false);
                    startActivity(new Intent(getApplicationContext(),LoginOrContinueActivity.class));
                    finish();
                }

            }
        },3000);

    }
}