package com.example.honeybee;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Utills.IPadress;

public class location extends AppCompatActivity {
    private Button btn_skip, btn_locationgo;
    String phone;
    private ProgressBar pb;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;

    private String TAG = "내학교 등록";

    String token = " ";
    private String name;
    private String age;
    private String Hobbies;
    private String email;
    private String School;
    private String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }


        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        School = auto.getString("학교", "");
        Hobbies = auto.getString("취미", "");
        name = auto.getString("이름","");
        phone = auto.getString("phone","");
        age = auto.getString("age","");
        gender = auto.getString("gender","");
        email = auto.getString("email",null);
        token = auto.getString("토큰","");

       // Intent info = getIntent();
       // name = info.getStringExtra("이름");
       // phone = info.getStringExtra("phone");
       // age = info.getStringExtra("나이");
       // gender = info.getStringExtra("성별");
       // Hobbies = info.getStringExtra("취미");
       // email = info.getStringExtra("email");
        // School = info.getStringExtra("학교");
       // token = info.getStringExtra("토큰");

        setNoti();

        btn_locationgo = (Button) findViewById(R.id.btn_activateLocation);
        pb = findViewById(R.id.progressBarLo);

        updateProgress();

        btn_locationgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phpgetnum task = new phpgetnum();
                task.execute(IP_ADDRESS + "insert.php", phone, name, age, gender, Hobbies, email, School, token);
            }
        });
    }

    private void setNoti() {
        SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor saveSettings = setmessegelikes.edit();
        saveSettings.putBoolean("newMatch",true);
        saveSettings.putBoolean("CancelMatch",true);
        saveSettings.putBoolean("melikes",true);
        saveSettings.putBoolean("noMsg",true);
        saveSettings.apply();
    }

    private void updateProgress() {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor probarRemove = auto.edit();
        probarRemove.remove("ProBar");
        probarRemove.commit();

        int ProBar = pb.getProgress();
        //85%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }

    class phpgetnum extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Log.d(TAG, "POST response  - " + result);


            if (!TextUtils.isEmpty(result)) {
                Intent reg = new Intent(location.this, welcome.class);
                reg.putExtra("phone", phone);
                location.this.startActivity(reg);
                finish();
//                Toast.makeText(location.this, "저장", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "저장");
            } else {
//                Toast.makeText(location.this, "정보저장실패", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "저장 실패");

            }

        }


        @Override
        protected String doInBackground(String... params) {
            String token = (String) params[8];
            String school = (String) params[7];
            String email = (String) params[6];
            String hobby = (String) params[5];
            String gender = (String) params[4];
            String age = (String) params[3];
            String name = (String) params[2];
            String phone = (String) params[1];

            String serverURL = (String) params[0];
            String postParameters = "phone=" + phone + "&name=" + name + "&age=" + age + "&gender=" + gender + "&hobby=" + hobby + "&email=" + email + "&school=" + school + "&token=" + token;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {


                return new String("Error: " + e.getMessage());
            }

        }

    }

        //여기부터는 GPS 활성화를 위한 메소드들
        private void showDialogForLocationServiceSetting() {

            AlertDialog.Builder builder = new AlertDialog.Builder(location.this);
            builder.setTitle("위치 서비스 비활성화");
            builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                    + "위치 설정을 수정하실래요?");
            builder.setCancelable(true);
            builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent callGPSSettingIntent
                            = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {

                case GPS_ENABLE_REQUEST_CODE:

                    //사용자가 GPS 활성 시켰는지 검사
                    if (checkLocationServicesStatus()) {
                        if (checkLocationServicesStatus()) {

                            Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                            checkRunTimePermission();
                            return;
                        }
                    }

                    break;
            }
        }
        void checkRunTimePermission(){

            //런타임 퍼미션 처리
            // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(location.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(location.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                // 2. 이미 퍼미션을 가지고 있다면
                // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


                // 3.  위치 값을 가져올 수 있음



            } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

                // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
                if (ActivityCompat.shouldShowRequestPermissionRationale(location.this, REQUIRED_PERMISSIONS[0])) {

                    // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                    Toast.makeText(location.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                    // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions(location.this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);


                } else {
                    // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                    // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions(location.this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }

            }

        }

        public boolean checkLocationServicesStatus() {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
}