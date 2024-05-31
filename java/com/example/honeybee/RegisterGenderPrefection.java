package com.example.honeybee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import Utills.IPadress;

public class RegisterGenderPrefection extends AppCompatActivity {

    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    private  String TAG = "genderprefer";

    boolean preferMale = true;
    private Button preferenceContinueButton;
    private Button hobbiesContinueButton;
    private Button maleSelectionButton;
    private Button femaleSelectionButton;
    private ProgressBar pb;
    private String name;
    private String phone;
    private String age;
    private String email;
    int gender ;

    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gender_prefection);


        Intent info = getIntent();
        name = info.getStringExtra("이름");
        phone = info.getStringExtra("phone");
        age = info.getStringExtra("나이");
        gender = info.getIntExtra("성별",0);
        email = info.getStringExtra("email");

        pb = findViewById(R.id.progressBarGenderPre);
        maleSelectionButton = findViewById(R.id.maleSelectionButton);
        femaleSelectionButton = findViewById(R.id.femaleSelectionButton);
        preferenceContinueButton = findViewById(R.id.preferenceContinueButton);
        //By default male has to be selected so below code is added

        femaleSelectionButton.setAlpha(.5f);
        femaleSelectionButton.setBackgroundColor(Color.GRAY);


        maleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButtonSelected();
            }
        });

        femaleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButtonSelected();
            }
        });

        preferenceContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHobbyEntryPage();
            }
        });

        updateProgress();
    }

    private void updateProgress() {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor probarRemove = auto.edit();
        probarRemove.remove("ProBar");
        probarRemove.commit();

        int ProBar = pb.getProgress();
        //50%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }

    public void maleButtonSelected() {
        preferMale = true;
        maleSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        maleSelectionButton.setAlpha(1.0f);
        femaleSelectionButton.setAlpha(.5f);
        femaleSelectionButton.setBackgroundColor(Color.GRAY);

        SharedPreferences setgender = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor saveSettings = setgender.edit();
        saveSettings.remove("mw");
        saveSettings.putInt("mw",0);
        saveSettings.apply();
    }

    public void femaleButtonSelected() {
        preferMale = false;
        femaleSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        femaleSelectionButton.setAlpha(1.0f);
        maleSelectionButton.setAlpha(.5f);
        maleSelectionButton.setBackgroundColor(Color.GRAY);

        SharedPreferences setgender = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor saveSettings = setgender.edit();
        saveSettings.remove("mw");
        saveSettings.putInt("mw",1);
        saveSettings.apply();
    }

    public void openHobbyEntryPage() {

        Intent reg = new Intent(RegisterGenderPrefection.this, RegisterHobby.class);
        reg.putExtra("이름", name);
        reg.putExtra("성별", gender);
        reg.putExtra("phone", phone);
        reg.putExtra("나이", age);
        reg.putExtra("email", email);
        RegisterGenderPrefection.this.startActivity(reg);
        finish();


    }



}