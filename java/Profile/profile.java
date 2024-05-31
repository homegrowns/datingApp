package Profile;


import static CouplesAndchat.Matched_Activity.getMsg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.honeybee.R;
import com.example.honeybee.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CouplesAndchat.SeePartnerInfo;
import Matching.MatchingActivity;
import Utills.IPadress;
import Utills.PulsatorLayout;

public class profile extends AppCompatActivity {

    static Context mContext;

    static ProgressDialog pd;

    Uri im;
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    private static String TAG = "profile";
    private TextView TextName;

    private ImageView myimg;
    private ActivityProfileBinding binding;
    private String myJsonString;

    ImageButton back;

    String phone;
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
    String lati;
    String longti;
    String dislikes;
    String school,intro,job,work,hobby,city;




    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pd = new ProgressDialog(this);
        pd.setTitle("기다려주세요..");
        pd.setCancelable(true);

        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();

        getMsg = false;
        Log.e(TAG, "getMsg : "+getMsg );

        mContext=this;


        TextName = (TextView) findViewById(R.id.tv_profile_name);
        myimg = (ImageView) findViewById(R.id.img_circle_profile_image);
        back = (ImageButton) findViewById(R.id.btn_backtoMain);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(profile.this, MatchingActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
                finish();
            }
        });


        myimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent love = new Intent(profile.this, SeePartnerInfo.class);
                love.putExtra("name",name);
                love.putExtra("phone",phone);
                love.putExtra("age",age);
                love.putExtra("city",city);
                love.putExtra("hobbies",hobby);
                love.putExtra("img1",img);
                love.putExtra("img2",img2);
                love.putExtra("img3",img3);
                love.putExtra("img4",img4);
                love.putExtra("img5",img5);
                love.putExtra("img6",img6);
                love.putExtra("intro",intro);
                love.putExtra("job",job);
                love.putExtra("school",school);
                love.putExtra("gender",gender);
                love.putExtra("work",work);
                love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(love);
            }
        });

        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(profile.this, EditProfileActivity.class);
                intent.putExtra("이름", name);
                intent.putExtra("이미지2", img2);
                intent.putExtra("이미지3", img3);
                intent.putExtra("이미지4", img4);
                intent.putExtra("이미지5", img5);
                intent.putExtra("이미지6", img6);
                intent.putExtra("이미지", img);
                intent.putExtra("나이", age);
                intent.putExtra("성별", gender);

                startActivity(intent);
            }
        });


        binding.imgbtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(profile.this, setting_profile.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        phone = auto.getString("phone",null);
        Log.d(TAG, "phone : "+ phone);

        loadmamberInfo(phone);
    }


    


    ////////////////////////////My loadmamberInfo/////////////////////////////////////////////////
    public void loadmamberInfo(String myphone) {


        String TAG_JSON="webnautes";
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
        String TAG_lati = "lati";
        String TAG_longti = "longit";
        String TAG_dislikes = "dislikes";
        String TAG_school = "school";
        String TAG_intro = "intro";
        String TAG_job = "job";
        String TAG_work = "workplace";
        String TAG_hobby = "hobbies";
        String TAG_location = "location";



        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "MatchedA 어레이갯수: d=" + jsonArray.length() + "개");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);
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
                        lati = item.getString(TAG_lati);
                        longti = item.getString(TAG_longti);
                        school = item.getString(TAG_school);
                        job = item.getString(TAG_job);
                        work = item.getString(TAG_work);
                        hobby = item.getString(TAG_hobby);
                        city = item.getString(TAG_location);
                        intro = item.getString(TAG_intro);
                        dislikes = item.getString(TAG_dislikes);


                        TextName.setText(name);
                        binding.tvProfileAge.setText(age);
                        im =  Uri.parse(IP_ADDRESS+img);


                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.remove("likes");
                        autoLogin.putString("gender", gender);
                        autoLogin.putString("img1", img);
                        autoLogin.putString("likes", likes);
                        autoLogin.putString("age", age);
                        autoLogin.putString("lati", lati);
                        autoLogin.putString("longit", longti);
                        autoLogin.putString("name", name);
                        autoLogin.putString("dislikes", dislikes);

                        autoLogin.commit();

                        Glide.with(profile.this).load(im).into(myimg);
                    } // for.....

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e1) {
                    e1.printStackTrace();
                }

            }//..onResponse

        };
        GetMyinfo add = new GetMyinfo(myphone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(profile.this);
        queue.add(add);

    }

}
