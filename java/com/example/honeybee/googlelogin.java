package com.example.honeybee;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Utills.IPadress;
import Utills.SplashActivity;

public class googlelogin extends AppCompatActivity {
    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;

    FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    googlelogin.phpdo task;
    private String mJsonString;
    String email;
    String phonnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlelogin);
        firebaseAuth = FirebaseAuth.getInstance();
        String myuid = firebaseAuth.getUid();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        email = user.getEmail();
        Log.d("googleLogin", "email : " + email);


        if (email != null) {
            task = new phpdo();
            task.execute(IP_ADDRESS + "GetnameEmail.php", email);

            if(phonnum == null){
                Intent login = new Intent(googlelogin.this, PhonAuth.class);
                login.putExtra("email",email);
                startActivity(login);
            }
        } else {
            Intent login = new Intent(googlelogin.this, PhonAuth.class);
            login.putExtra("email",email);
            startActivity(login);
        }
    }

    private void logandregi(String phonnum) {

        if(phonnum != null){
            Intent regis = new Intent(googlelogin.this, SplashActivity.class);
            regis.putExtra("email",email);
            regis.putExtra("phone",phonnum);
            startActivity(regis);

        }
        else {
            Intent login = new Intent(googlelogin.this, PhonAuth.class);
            startActivity(login);
        }
    }


    class phpdo extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(googlelogin.this,
                    "Please Wait", "wait", true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();


            Log.d(TAG, "POST response  - " + result);

            if (!TextUtils.isEmpty(result)){
                mJsonString = result;
                showResult();
            }
            else {

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "email=" + email;


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

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String phone = item.getString(TAG_phone);

                phonnum = phone;
                logandregi(phonnum);
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}