package com.example.honeybee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterName extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    String email;
    private ProgressBar pb;
    private static String TAG = "RegisterName";
    private TextView mTextViewResult;
    private EditText myname;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        myname = (EditText) findViewById(R.id.et_name);
        pb = findViewById(R.id.progressBarName);

        firebaseAuth = FirebaseAuth.getInstance();
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());


        Button button = (Button) findViewById(R.id.btn_Countinue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                String name = myname.getText().toString().trim();


                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("phone", phone);
                autoLogin.putString("이름", name);
                autoLogin.putString("email", email);
                autoLogin.commit();

                Intent reg = new Intent(RegisterName.this, ImageDB.class);
                reg.putExtra("이름", name);
                reg.putExtra("phone", phone);
                reg.putExtra("email", email);
                RegisterName.this.startActivity(reg);

            }
        });


        updateProgress();
    }

    private void updateProgress() {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        int ProBar = pb.getProgress();
        //10%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }

//    private void savemyemail(String email, String phone) {
//
//
//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    if (success.equals("1") && success != null ) {
//                        Toast.makeText(RegisterName.this, "정보업데이트 성공", Toast.LENGTH_SHORT).show();
//
//
//                    }else {
//                        Toast.makeText(RegisterName.this, "정보업데이트 실패", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        };
//
//
//        registerEmail emailadd=new registerEmail(email, phone ,responseListener);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(emailadd);
//        Intent back = new Intent(RegisterName.this, profile.class);
//        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(back);
//        finish();
//    }


}