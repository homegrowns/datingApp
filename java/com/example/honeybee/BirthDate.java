package com.example.honeybee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Utills.IPadress;

public class BirthDate extends AppCompatActivity {
    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;
    Context context;
    private ProgressBar pb;
    private static String TAG = "birthdate";
    EditText y;
    EditText m ,d;
    TextView tv_result;
    String year,month,day;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    private DatePicker ageSelectionPicker;
    Button ageContinueButton;
    // age limit attribute
    private int ageLimit = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_date);
        firebaseAuth = FirebaseAuth.getInstance();


        ageSelectionPicker = findViewById(R.id.ageSelectionPicker);

        tv_result = findViewById(R.id.tv_ageResult);

        pb = findViewById(R.id.progressBarAge);

        ageContinueButton = findViewById(R.id.ageContinueButton);

        updateProgress();




        ageContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHobbiesEntryPage();

            }
        });


    }

    private void updateProgress() {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor probarRemove = auto.edit();
        probarRemove.remove("ProBar");
        probarRemove.commit();

        int ProBar = pb.getProgress();
        //30%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void openHobbiesEntryPage() {

        FirebaseUser fireUser = firebaseAuth.getCurrentUser();
        String phone = fireUser.getPhoneNumber();

        int age = getAge(ageSelectionPicker.getYear(), ageSelectionPicker.getMonth(), ageSelectionPicker.getDayOfMonth());


        // if user is above 13 years old then only he/she will be allowed to register to the system.
        if (age > ageLimit && age < 100) {
            // code for converting date to string
            Calendar cal = Calendar.getInstance();
//
//
            cal.set(Calendar.DAY_OF_YEAR, ageSelectionPicker.getYear());
            cal.set(Calendar.MONTH, ageSelectionPicker.getMonth());
            cal.set(Calendar.DAY_OF_MONTH, ageSelectionPicker.getDayOfMonth());
            Date dateOfBirth = cal.getTime();
            String strDateOfBirth = dateFormatter.format(dateOfBirth);

            Intent info = getIntent();
            String ageold = String.valueOf(age);
            String name = info.getStringExtra("이름");
            String email = info.getStringExtra("email");

            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putString("age", ageold);
            autoLogin.commit();

            Intent gender = new Intent(BirthDate.this, RegisterGender.class);
            gender.putExtra("이름", name);
            gender.putExtra("phone", phone);
            gender.putExtra("나이", ageold);
            gender.putExtra("email", email);

            BirthDate.this.startActivity(gender);

            finish();

        }

        else if(age < ageLimit){
            tv_result.setText(String.format("만 %d세 이상 가입가능합니다.", ageLimit));
          //  Toast.makeText(getApplicationContext(), "만 "+ageLimit + "세 이상 가입가능합니다.", Toast.LENGTH_SHORT).show();
        }

    }

    // method to get the current age of the user.
    private int getAge(int year, int month, int day) {
        Calendar dateOfBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dateOfBirth.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }


}