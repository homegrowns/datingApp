package likesListAndActivity;

import static CouplesAndchat.Matched_Activity.getMsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.honeybee.R;
import com.example.honeybee.TopNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Matching.ItemModel;
import Matching.getlikelist;
import Matching.loadInfo;
import Profile.GpsTracker;
import Profile.profile;
import Utills.IPadress;


public class seelikes_Activity extends AppCompatActivity {
    mlikeslistAdapter mAdapter;
    RecyclerView recyclerView;
    Context context;
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    Uri im;
    private static final String TAG = "seelikes_Activity";
    private static final int ACTIVITY_NUM = 1;
    List<ItemModel> items = new ArrayList<>();
    private GpsTracker gpsTracker;
    private TextView numOflikes;
    private Context mContext = seelikes_Activity.this;
    private String likeslist;

    private ImageView imgbtn;

    private String[] mlike,mdislike, somelist;

    double latitude;
    double longitude;
    private CountDownTimer countDownTimer;

    String Llist;
    String name;
    String img;
    String img2;
    String img3;
    String img4;
    String img5;
    String img6;
    String hobbies;
    String gender;
    String school;
    String age;
    String phone;
    String mphone;
    String location;
    String token;
    String la;
    String Long;
    String intro;
    String job;
    String  workPlace;
    String distance;
    String hideage;
    String numoflikes;
    String likes;
    int dist;

    private String lati;
    private String longit;

    String mylikes, mydislikes, pm, dis, img1;
    String HobbiesReplaced;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholikes);

        imgbtn = findViewById(R.id.img_go_profile);
        recyclerView = findViewById(R.id.active_recycler_view);
        numOflikes = findViewById(R.id.tv_numberOflikes);
        setupTopNavigationView();

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        mphone = auto.getString("phone",null);
        mydislikes = auto.getString("dislikes", null);
        mydislikes = mydislikes.replace(" ", "");
        mdislike = mydislikes.split(",");

        getMsg = false;
        Log.e(TAG, "getMsg : "+getMsg );

        String img1 = auto.getString("img1",null);

        im =  Uri.parse(IP_ADDRESS+img1);
        Glide.with(this).load(im).into(imgbtn);

        getLikelist(mphone);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gopro = new Intent(seelikes_Activity.this, profile.class);
                startActivity(gopro);
                finish();
            }
        });


        gpsTracker = new GpsTracker(seelikes_Activity.this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

    }


    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public String findmylove(String n) {
        for (int z = 0; z < mlike.length; z++) {
            if (mlike[z].equals(n)) {
                pm = mlike[z];
                return pm;
            }
        }
        return null;
    }

    public String findDislike(String n) {
        for (int z = 0; z < mdislike.length; z++) {
            if (mdislike[z].equals(n)) {
                dis = mdislike[z];
                return dis;
            }
        }
        return null;
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

    private void loadmamberInfo() {
        String TAG_JSON = "webnautes";
        String TAG_NAME = "name";
        String TAG_p = "phone";
        String TAG_AGE = "age";
        String TAG_IMG = "img";
        String TAG_gender = "gender";
        String TAG_school = "school";
        String TAG_intro = "intro";
        String TAG_job = "job";
        String TAG_IMG2 = "img2";
        String TAG_IMG3 = "img3";
        String TAG_IMG4 = "img4";
        String TAG_IMG5 = "img5";
        String TAG_IMG6 = "img6";
        String TAG_hobby = "hobbies";
        String TAG_location = "location";
        String TAG_tk = "token";
        String TAG_likes = "likes";
        String TAG_lati = "lati";
        String TAG_longit = "longit";
        String TAG_hideage = "hideage";
        String TAG_msg = "msg";
        String TAG_work = "work";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "어레이갯수: d=" + jsonArray.length() + "개");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);

                        name = item.getString(TAG_NAME);
                        age = item.getString(TAG_AGE);
                        img = item.getString(TAG_IMG);
                        gender = item.getString(TAG_gender);
                        school = item.getString(TAG_school);
                        img2 = item.getString(TAG_IMG2);
                        img3 = item.getString(TAG_IMG3);
                        img4 = item.getString(TAG_IMG4);
                        img5 = item.getString(TAG_IMG5);
                        img6 = item.getString(TAG_IMG6);
                        hobbies = item.getString(TAG_hobby);
                        location = item.getString(TAG_location);
                        likes = item.getString(TAG_likes);
                        phone = item.getString(TAG_p);
                        token = item.getString(TAG_tk);
                        la = item.getString(TAG_lati);
                        Long = item.getString(TAG_longit);
                        intro = item.getString(TAG_intro);
                        job = item.getString(TAG_job);
                        hideage = item.getString(TAG_hideage);
                        workPlace = item.getString(TAG_work);

                        String hobbiesReplaced = hobbies.replace("[", " ").trim();
                        HobbiesReplaced = hobbiesReplaced.replace("]", " ").trim();

                        likes = likes.replace(" ", "");
                        somelist = likes.split(",");
                        Log.d(TAG, "somelist =" + somelist.length);
                        // 설정으로 나이 숨기기

                        if(hideage.equals("h")){
                            age="";
                        }

                        if (Long.equals("") && la.equals("")) {
                            dist = 0;
                        } else {
                            double gd = getDistance(latitude, longitude, Double.parseDouble(la), Double.parseDouble(Long));
                            distance = String.valueOf(Math.round(gd));
                            dist = Integer.parseInt(distance);
                        }

                        String dislikenum = findDislike(phone);
                        String somelikeme = findmylove(phone);


                        for (int z = 0; z < somelist.length; z++) {
                            if (somelist[z].equals(mphone)) {

                                if(somelikeme != null || dislikenum != null){

                                }else {

                                    if (Long.equals("") && la.equals("")) {
                                        items.add(new ItemModel(phone, img, gender, school, intro, job, name, age, location, HobbiesReplaced, token, "", img2, img3, img4, img5, img6, somelist, "", countDownTimer, "",workPlace));
                                        Log.d(TAG, "이름=" + name);
                                    }
                                    else
                                    {
                                        items.add(new ItemModel(phone, img, gender, school, intro, job, name, age, location, HobbiesReplaced, token, distance + " km", img2, img3, img4, img5, img6, somelist,"",countDownTimer,"",workPlace));
                                        Log.d(TAG, "이름=" + name);
                                    }
                                 }

                            }
                        }
//                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                        String minAge = auto.getString("min", "0");
//                        String maxAge = auto.getString("max", "50");
//                        mydislikes = auto.getString("dislikes", null);
//                        mydislikes = mydislikes.replace(" ", "");
//                        mdislike = mydislikes.split(",");
//                        String dislikenum = findDislike(phone);
//
//                        int prog = auto.getInt("distance", 1);
//                        int mw = auto.getInt("mw", 2);


                    }
                    Log.d(TAG, "items.size() =" + items.size());
                    numoflikes = items.size() + " likes";
                    numOflikes.setText(numoflikes);
                    mAdapter = new mlikeslistAdapter(items,context);
                    recyclerView.setLayoutManager(new GridLayoutManager(seelikes_Activity.this,2));
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setHasFixedSize(true);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                }




            }

        };
        loadInfo add = new loadInfo(responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }

    private void getLikelist(String phone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {
//                        Toast.makeText(seelikes_Activity.this, "겟라이크 리스트: "+success, Toast.LENGTH_SHORT).show();
                        Llist = success;
                        Llist = Llist.replace(" ", "");
                        mlike = Llist.split(",");
                        loadmamberInfo();

                    }else{
//                        Toast.makeText(seelikes_Activity.this, "라이크받아오기 실패: "+success, Toast.LENGTH_SHORT).show();
                        Llist = " , ";
                        mlike = Llist.split(",");
                        loadmamberInfo();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        getlikelist add= new getlikelist(phone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }

}
