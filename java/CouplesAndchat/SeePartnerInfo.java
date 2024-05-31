package CouplesAndchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.honeybee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Matching.LoadMember;
import Matching.ViewPagerInChat;
import Matching.imageData;
import Profile.GpsTracker;

public class SeePartnerInfo extends AppCompatActivity {

    ViewPager2 viewPager2;
    List<String> reallist = new ArrayList<>();
    private static final String TAG = "SeePartnerInfo";
    private Context mContext = SeePartnerInfo.this;


    ImageButton img_btn_back;
    String name;
    String img ="", myimg;
    String img2 = "";
    String img3 = "";
    String img4 = "";
    String img5 = "";
    String img6 = "";
    String hobbies;
    String gender= "";
    String school = "";
    String age;
    String phonewithlikes;
    String intro;
    String job= "";
    String workplace= "";
    String city= "";
    String distance;
    String token;
    int dist;
    private GpsTracker gpsTracker;

    double latitude;
    double longitude;

    String mynum,myname;
    TextView Name,Age,City,Hobiies,Gender,School,Intro,Job,Distance,work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_partner_info);

        gpsTracker = new GpsTracker(SeePartnerInfo.this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        mynum = auto.getString("phone",null);
        myname = auto.getString("name",null);
        myimg = auto.getString("img1",null);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        city = intent.getStringExtra("city");
        hobbies = intent.getStringExtra("hobbies");
        intro = intent.getStringExtra("intro");
        job = intent.getStringExtra("job");
        school = intent.getStringExtra("school");
        gender = intent.getStringExtra("gender");
        phonewithlikes = intent.getStringExtra("phone");
        token = intent.getStringExtra("token");
        img = intent.getStringExtra("img1");
        img2 = intent.getStringExtra("img2");
        img3 = intent.getStringExtra("img3");
        img4 = intent.getStringExtra("img4");
        img5 = intent.getStringExtra("img5");
        img6 = intent.getStringExtra("img6");
        workplace = intent.getStringExtra("work");
        loadmamberInfo(phonewithlikes);

        viewPager2 = findViewById(R.id.item_imagesForViewpager);
        Name = findViewById(R.id.tv_item_name);
        Age = findViewById(R.id.tv_item_age);
        City = findViewById(R.id.tv_item_location);
        Job = findViewById(R.id.tv_item_job);
        School = findViewById(R.id.tv_item_school);
        Gender = findViewById(R.id.tv_item_gender);
        Distance = findViewById(R.id.tv_item_distance);
        Intro = findViewById(R.id.tv_item_intro);
        Hobiies = findViewById(R.id.tv_item_hobbies);
        work = findViewById(R.id.tv_item_work);

        img_btn_back = findViewById(R.id.btn_backToChatRoom);
        // 페이져뷰로넘어갈 자료 구성후 페이져뷰로 리스트데이터 넘긴다,
        List<imageData> list = new ArrayList<>();
        list.add(new imageData(img));
        list.add(new imageData(img2));
        list.add(new imageData(img3));
        list.add(new imageData(img4));
        list.add(new imageData(img5));
        list.add(new imageData(img6));
        for (int i = 0; i < list.size(); i++)
        {
            Log.e(TAG, "이미지자료 확인 = "+ list.get(i).getImg1());

        }
        for (int i = 0; i < list.size(); i++) {

            if(!list.get(i).getImg1().equals("")) {

                if (!list.get(i).getImg1().equals("null")) {
                    reallist.add(list.get(i).getImg1());

                }
            }
        }
        viewPager2.setAdapter(new ViewPagerInChat(reallist));
        // 페이져뷰로넘어갈 자료 구성후 페이져뷰로 리스트데이터 넘긴다,


        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
                finish();
            }
        });
    }

    private void loadmamberInfo(String Pnum) {
        String TAG_JSON = "webnautes";
        String TAG_lati = "lati";
        String TAG_longit = "longit";
        String TAG_hideage = "hideage";


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "어레이갯수: d=" + jsonArray.length() + "개");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);


                       String la = item.getString(TAG_lati);
                       String Long = item.getString(TAG_longit);
                       String hideage = item.getString(TAG_hideage);

                        // 설정으로 나이 숨기기

                        if(hideage.equals("h")){
                            age="";
                        }

                        if (Long.equals("") && la.equals("")) {
                            distance = " ";
                        }
                        else
                        {
                            double gd = getDistance(latitude, longitude, Double.parseDouble(la), Double.parseDouble(Long));
                            String dista = String.valueOf(Math.round(gd));
                            Log.d(TAG, "gd거리 =" +gd+" km");

                            if(gd == 0 || gd < 1){
                                distance = 1+" km";
                            }
                            else
                            {
                                distance = dista+" km";

                            }

                        }

                        SetValues();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                }


            }

        };
        LoadMember add = new LoadMember(Pnum,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }

    private void SetValues()
    {
        if(distance.equals("")){
            Distance.setVisibility(View.GONE);
        }
        if(school.equals("")){
            School.setVisibility(View.GONE);
        }
        if(workplace.equals("")){
            work.setVisibility(View.GONE);
        }
        if(job.equals("")){
            Job.setVisibility(View.GONE);
        }
        Name.setText(name);
        Age.setText(age);
        City.setText(city);
        Gender.setText(gender);
        Job.setText(job);
        School.setText(school);
        Distance.setText(distance);
        Intro.setText(intro);
        Hobiies.setText(hobbies);
        work.setText(workplace);
        if(gender.equals("0")){
            Gender.setText("여성");
        } else if(gender.equals("1")){
            Gender.setText("남성");

        }
    }

    private double getDistance(double lati1, double longit1, double lati2, double longit2) {
        Location mlocation = new Location("");
        mlocation.setLatitude(lati1);
        mlocation.setLongitude(longit1);

        Location location2 = new Location("");
        location2.setLatitude(lati2);
        location2.setLongitude(longit2);

        double distance = mlocation.distanceTo(location2);
        double disresult = Math.round(distance);
        double finaldis = disresult / 1000;

        return finaldis;
    }
}