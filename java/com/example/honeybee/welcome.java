package com.example.honeybee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import Utills.SplashActivity;

public class welcome extends AppCompatActivity {
    private Button btn_go;
    String phone;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");

        btn_go = (Button)findViewById(R.id.btn_skip2);

        pb = findViewById(R.id.progressBarWelcome);

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);

        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.remove("학교");
        autoLogin.remove("취미");
        autoLogin.remove("이름");
        autoLogin.remove("phone");
        autoLogin.remove("age");
        autoLogin.remove("gender");
        autoLogin.remove("email");
        autoLogin.remove("토큰");


        updateProgress();

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor save = getPhone.edit();
                save.putString("phone",phone);
                save.apply();
                Intent gomatching = new Intent(welcome.this, SplashActivity.class);
                gomatching.putExtra("phone",phone);
                startActivity(gomatching);
                finish();
            }
        });
    }



    private void updateProgress() {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor probarRemove = auto.edit();
        probarRemove.remove("ProBar");
        probarRemove.commit();

        int ProBar = pb.getProgress();
        //100%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }
}