package Utills;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.honeybee.BirthDate;
import com.example.honeybee.ImageDB;
import com.example.honeybee.MainActivity;
import com.example.honeybee.R;
import com.example.honeybee.RegisterGender;
import com.example.honeybee.RegisterGenderPrefection;
import com.example.honeybee.RegisterHobby;
import com.example.honeybee.RegisterName;
import com.example.honeybee.RegisterSchool;
import com.example.honeybee.location;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Matching.MatchingActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    private String myJsonString;
    private static final String TAG = "SplashA";
    FirebaseAuth firebaseAuth;
    Uri im;
    String email,phone;

    phpgetinfo task;
    String name;
    String imge;
    String img;
    String img2;
    String img3;
    String img4;
    String img5;
    String img6;
    String gender;
    String age;
    String likes;
    String token;
    String lati;
    String longit;
    String dislikes;

    String TK;

    LottieAnimationView animationView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");


        textView = findViewById(R.id.textView);
        animationView = findViewById(R.id.lottie);
        animationView.setAnimation("6233-honey-bee.json");
        animationView.playAnimation();
        animationView.setRepeatCount(2);

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        Intent info = getIntent();

        img = info.getStringExtra("이미지");
// 회원가입후 데이터 받는게 없어서 이부분이 터지는것같다



        if(phone == null){

            SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
            String Phone = getPhone.getString("phone",null);
//            task = new phpgetinfo();
//            task.execute(IP_ADDRESS + "Getname.php",Phone);
            Log.d(TAG, " 1.번호= " + Phone+ "개");

            ManageuserData(Phone);

        } else {
//            task = new phpgetinfo();
//            task.execute(IP_ADDRESS + "Getname.php", phone);
            Log.d(TAG, " 2.번호= " + phone+ "개");

            ManageuserData(phone);

        }

      //  goBackRergi();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000);
    }



    private class splashhandler implements Runnable {
        public void run() {
         //   Toast.makeText(SplashActivity.this, "메인이동", Toast.LENGTH_SHORT).show();

         //   startActivity(new Intent(getApplication(), MatchingActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
          //  SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거

            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
            int probar = auto.getInt("ProBar",0);
            if (probar == 101)
            {
                startActivity(new Intent(getApplication(), MatchingActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }
            if (probar == 100)
            {
                startActivity(new Intent(getApplication(), MatchingActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
                SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
            }

            if (probar == 85)
            {
                Intent intent = new Intent(getApplication(), location.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            if (probar == 70)
            {
                Intent intent = new Intent(getApplication(), RegisterSchool.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            if (probar == 60)
            {
                Intent intent = new Intent(getApplication(), RegisterHobby.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            if (probar == 50)
            {
                Intent intent = new Intent(getApplication(), RegisterGenderPrefection.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            if (probar == 40)
            {
                Intent intent = new Intent(getApplication(), RegisterGender.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            if (probar == 30)
            {
                Intent intent = new Intent(getApplication(), BirthDate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            if (probar == 20)
            {
                Intent intent = new Intent(getApplication(), ImageDB.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            if (probar == 10)
            {
                Intent intent = new Intent(getApplication(), RegisterName.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            if (probar == 0)
            {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }

    }



    private void ManageuserData(String num) {

        // **if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
        //  php에서 위에 코드 제거해야 데이터 전송 가능
        
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("phone", num)
                .build();

        Request request = new Request.Builder()

                .url(IP_ADDRESS + "/Getname.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(CallbackUser);
    }

    private final Callback CallbackUser = new Callback() {

        String TAG_JSON="webnautes";
        String TAG_phone="phone";
        String TAG_NAME = "name";
        String TAG_AGE = "age";
        String TAG_IMG = "img";
        String TAG_gender = "gender";
        String TAG_IMG2 = "img2";
        String TAG_IMG3 = "img3";
        String TAG_IMG4 = "img4";
        String TAG_IMG5 = "img5";
        String TAG_IMG6 = "img6";
        String TAG_likes = "likes";
        String TAG_tk = "token";
        String TAG_la = "lati";
        String TAG_long = "longit";
        String TAG_dislikes = "dislikes";


        @Override
        public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
            final String responseData = response.body().string();
            Log.d(TAG, "responseData : " + responseData);
            // 서브 스레드 Ui 변경 할 경우 에러
            // 메인스레드 Ui 설정
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                        Log.d(TAG, "MatchedA 어레이갯수: d=" + jsonArray.length() + "개");

                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject item = jsonArray.getJSONObject(i);

                            phone = item.getString(TAG_phone);
                            name = item.getString(TAG_NAME);
                            age = item.getString(TAG_AGE);
                            img = item.getString(TAG_IMG);
                            gender = item.getString(TAG_gender);
                            img2 = item.getString(TAG_IMG2);
                            img3 = item.getString(TAG_IMG3);
                            img4 = item.getString(TAG_IMG4);
                            img5 = item.getString(TAG_IMG5);
                            img6 = item.getString(TAG_IMG6);
                            likes = item.getString(TAG_likes);
                            token = item.getString(TAG_tk);
                            lati = item.getString(TAG_la);
                            longit = item.getString(TAG_long);
                            dislikes = item.getString(TAG_dislikes);


                            Toast.makeText(SplashActivity.this, "불러오기", Toast.LENGTH_SHORT).show();

                            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor probarRemove = auto.edit();
                            probarRemove.remove("ProBar");
                            probarRemove.commit();

                            SharedPreferences.Editor autoLogin = auto.edit();
                            autoLogin.putString("email", email);
                            autoLogin.putString("phone", phone);
                            autoLogin.putString("img1", img);
                            autoLogin.putString("name", name);
                            autoLogin.putString("img2", img2);
                            autoLogin.putString("img3", img3);
                            autoLogin.putString("img4", img4);
                            autoLogin.putString("img5", img5);
                            autoLogin.putString("img6", img6);
                            autoLogin.putString("gender", gender);
                            autoLogin.putString("age", age);
                            autoLogin.putString("likes", likes);
                            autoLogin.putString("dislikes", dislikes);
                            autoLogin.putString("lati", lati);
                            autoLogin.putString("longit", longit);
                            autoLogin.commit();

                            //100%
                            autoLogin.putInt("ProBar", 101);
                            autoLogin.commit();
                            Log.d(TAG, "데이터저장 : "+ phone);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("TEST", "ERROR Message : " + e.getMessage());
        }

    };


    class phpgetinfo extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);

            if(TextUtils.isEmpty(result)){
            //    Toast.makeText(SplashActivity.this, "불러오기실패", Toast.LENGTH_SHORT).show();
            }else {
                myJsonString = result;
                showResult();
            }

        }



        @Override
        protected String doInBackground(String... params) {

            String phone1 = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "phone="+phone1;


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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    private void showResult() {

        String TAG_JSON="webnautes";
        String TAG_phone="phone";
        String TAG_NAME = "name";
        String TAG_AGE = "age";
        String TAG_IMG = "img";
        String TAG_gender = "gender";
        String TAG_IMG2 = "img2";
        String TAG_IMG3 = "img3";
        String TAG_IMG4 = "img4";
        String TAG_IMG5 = "img5";
        String TAG_IMG6 = "img6";
        String TAG_likes = "likes";
        String TAG_tk = "token";
        String TAG_la = "lati";
        String TAG_long = "longit";
        String TAG_dislikes = "dislikes";

        try {
            JSONObject jsonObject = new JSONObject(myJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject item = jsonArray.getJSONObject(i);


                phone = item.getString(TAG_phone);
                name = item.getString(TAG_NAME);
                age = item.getString(TAG_AGE);
                img = item.getString(TAG_IMG);
                gender = item.getString(TAG_gender);
                img2 = item.getString(TAG_IMG2);
                img3 = item.getString(TAG_IMG3);
                img4 = item.getString(TAG_IMG4);
                img5 = item.getString(TAG_IMG5);
                img6 = item.getString(TAG_IMG6);
                likes = item.getString(TAG_likes);
                token = item.getString(TAG_tk);
                lati = item.getString(TAG_la);
                longit = item.getString(TAG_long);
                dislikes = item.getString(TAG_dislikes);


                Toast.makeText(SplashActivity.this, "불러오기", Toast.LENGTH_SHORT).show();

                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor probarRemove = auto.edit();
                probarRemove.remove("ProBar");
                probarRemove.commit();

                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("email", email);
                autoLogin.putString("phone", phone);
                autoLogin.putString("img1", img);
                autoLogin.putString("name", name);
                autoLogin.putString("img2", img2);
                autoLogin.putString("img3", img3);
                autoLogin.putString("img4", img4);
                autoLogin.putString("img5", img5);
                autoLogin.putString("img6", img6);
                autoLogin.putString("gender", gender);
                autoLogin.putString("age", age);
                autoLogin.putString("likes", likes);
                autoLogin.putString("dislikes", dislikes);
                autoLogin.putString("lati", lati);
                autoLogin.putString("longit", longit);
                autoLogin.commit();

                //100%
                autoLogin.putInt("ProBar", 101);
                autoLogin.commit();
                Log.d(TAG, "데이터저장 : "+ phone);

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}