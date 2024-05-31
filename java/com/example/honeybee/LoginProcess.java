package com.example.honeybee;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Utills.IPadress;

public class LoginProcess extends AppCompatActivity {
    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;

    private static String TAG = "profile";


    FirebaseAuth firebaseAuth;
    phpdo task;
    private String mJsonString;
    private TextView phpname;
    private TextView phonnum;
    private TextView ResultT;

    List<String> uriList = new ArrayList<String>();

    RecyclerView recyclerView;   // 이미지를 보여줄 리사이클러뷰
    profileAdapter profileAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_process);
        ResultT = (TextView) findViewById(R.id.textView_main_resultpro);
        phpname = (TextView) findViewById(R.id.tv_name);
        phonnum = (TextView) findViewById(R.id.tv_phone);

        recyclerView = findViewById(R.id.recyclerView2);

//        firebaseAuth = FirebaseAuth.getInstance();



//        FirebaseUser fireUser = firebaseAuth.getCurrentUser();
//        if(fireUser != null) {
//            String phone = fireUser.getPhoneNumber();
            String phone = "+821033334444";

            task = new phpdo();
            task.execute("http://" + IP_ADDRESS + "/Getname.php", phone);

//          }else{
//
//
//         //유저가 로그인 되지 않았다.
//           firebaseAuth.signOut();
//           finish();
//       }

    }


    class phpdo extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginProcess.this,
                    "Please Wait", "wait", true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            ResultT.setText(result);
            Log.d(TAG, "POST response  - " + result);

            if (TextUtils.isEmpty(result)){

                ResultT.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String phone = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "phone=" + phone;


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
        String TAG_IMG = "imguri";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String name = item.getString(TAG_NAME);
                String img = item.getString(TAG_IMG);
                String phone = item.getString(TAG_phone);

                phpname.setText(name);
                phonnum.setText(phone);

                uriList = Collections.singletonList(img);

                profileAdapter = new profileAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(profileAdapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                recyclerView.setLayoutManager(gridLayoutManager);
//

            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}