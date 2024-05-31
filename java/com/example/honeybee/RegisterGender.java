package com.example.honeybee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterGender extends AppCompatActivity {

    int gender=1;
    private Button genderContinueButton;
    private Button maleSelectionButton;
    private Button femaleSelectionButton;
    private String name;
    private String phone;
    private String age;
    private String email;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gender);

        Intent info = getIntent();
        name = info.getStringExtra("이름");
        phone = info.getStringExtra("phone");
        age = info.getStringExtra("나이");
        email = info.getStringExtra("email");

        maleSelectionButton = findViewById(R.id.maleSelectionButton);
        femaleSelectionButton = findViewById(R.id.femaleSelectionButton);
        genderContinueButton = findViewById(R.id.genderContinueButton);
        pb = findViewById(R.id.progressBarGender);
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

        genderContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferenceEntryPage();
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
        //40%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }

    public void maleButtonSelected() {
        gender = 1;
        maleSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        maleSelectionButton.setAlpha(1.0f);
        femaleSelectionButton.setAlpha(.5f);
        femaleSelectionButton.setBackgroundColor(Color.GRAY);
    }

    public void femaleButtonSelected() {
        gender = 0;
        femaleSelectionButton.setBackgroundColor(Color.parseColor("#FF4081"));
        femaleSelectionButton.setAlpha(1.0f);
        maleSelectionButton.setAlpha(.5f);
        maleSelectionButton.setBackgroundColor(Color.GRAY);
    }

    public void openPreferenceEntryPage() {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putString("gender", String.valueOf(gender));
        autoLogin.commit();

        Intent intent = new Intent(this, RegisterGenderPrefection.class);
        intent.putExtra("성별", gender);
        intent.putExtra("phone", phone);
        intent.putExtra("이름", name);
        intent.putExtra("나이", age);
        intent.putExtra("email", email);

        startActivity(intent);
        finish();
    }
}