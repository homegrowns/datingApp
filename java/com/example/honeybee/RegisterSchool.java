package com.example.honeybee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Utills.IPadress;

public class RegisterSchool extends AppCompatActivity {
    private EditText mschool;
    private TextView schoolcheck;
    private String School = "";
    private ProgressBar pb;
    private Button btn_continue;
    TextView btn_next;
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    private  String TAG = "내학교 등록";
    String token = "";
    private String name;
    private String phone;
    private String age;
    private String Gender;
    private String Hobbies;
    private String email;
    int gender;
   

    ProgressDialog progressDialog;
//    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//    Pattern emailPatttern = Patterns.EMAIL_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "FCM토큰 받기 실패", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        token = task.getResult();
//
//                        // Log and toast
//
//                        Log.e(TAG,"토큰 : " + token.toString());
//
//                    }
//                });

        Intent info = getIntent();
        name = info.getStringExtra("이름");
        phone = info.getStringExtra("phone");
        age = info.getStringExtra("나이");
        gender = info.getIntExtra("성별",0);
        Hobbies = info.getStringExtra("취미");
        email = info.getStringExtra("email");
        Gender = String.valueOf(gender);


        mschool = (EditText)findViewById(R.id.et_mschool);


        schoolcheck = (TextView)findViewById(R.id.tv_schoolcheck);
        btn_continue = (Button)findViewById(R.id.btn_Countinuewith학교);
        btn_next = findViewById(R.id.tv_nextbtn);
        pb = findViewById(R.id.progressBarSchool);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("학교", School);
                autoLogin.commit();

                Intent intent = new Intent(RegisterSchool.this, location.class);
                intent.putExtra("성별", Gender);
                intent.putExtra("phone", phone);
                intent.putExtra("이름", name);
                intent.putExtra("나이", age);
                intent.putExtra("email",email);
                intent.putExtra("취미", Hobbies);
                intent.putExtra("학교", School);
                intent.putExtra("토큰", token);
                startActivity(intent);
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                School = mschool.getText().toString().trim();
                if(!School.isEmpty()) {

                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("학교", School);
                    autoLogin.commit();

                    Intent intent = new Intent(RegisterSchool.this, location.class);
                    intent.putExtra("성별", Gender);
                    intent.putExtra("phone", phone);
                    intent.putExtra("이름", name);
                    intent.putExtra("나이", age);
                    intent.putExtra("email",email);
                    intent.putExtra("취미", Hobbies);
                    intent.putExtra("학교", School);
                    intent.putExtra("토큰", token);
                    startActivity(intent);
                    finish();
                    

                    } else {
                        schoolcheck.setText("출신학교를 등록하세요");
                  
                    }
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
        //70%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }

}