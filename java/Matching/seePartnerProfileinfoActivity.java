package Matching;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.honeybee.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utills.NotiModel;
import likesListAndActivity.Matchedcouple_Activity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class seePartnerProfileinfoActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    List<String> reallist = new ArrayList<>();
    private static final String TAG = "seeProfileinfoActivity";
    private Context mContext = seePartnerProfileinfoActivity.this;
    private static final String CHANNEL_ID = "matchingCH";
    static int notificationId = 0;
    protected ImageButton btn_like, btn_dislike, btn_report, btn_back;
    String Llist,dlist,partnerLikeList = null;
    String name;
    String img, myimg;
    String img2;
    String img3;
    String img4;
    String img5;
    String img6;
    String hobbies;
    String gender;
    String school= "";
    String age;
    String phonewithlikes;
    String intro;
    String job = "";
    String city = "";
    String work = "";
    String distance;
    String token;
    Boolean likeischecked = false;
    String mynum,myname;
    TextView Name,Age,City,Hobiies,Gender,School,Intro,Job,Distance,WorkPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profileinfo);

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
        distance = intent.getStringExtra("distance");
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
        work = intent.getStringExtra("work");

        loadmamber(phonewithlikes);

        btn_report = findViewById(R.id.btn_reportInSeeinfo);
        viewPager2 = findViewById(R.id.item_imageforSee);
        Name = findViewById(R.id.tv_item_nameinfo);
        Age = findViewById(R.id.tv_item_ageinfo);
        City = findViewById(R.id.tv_item_locationinfo);
        WorkPlace = findViewById(R.id.tv_item_workinfo);
        Job = findViewById(R.id.tv_item_jobinfo);
        School = findViewById(R.id.tv_item_schoolinfo);
        Gender = findViewById(R.id.tv_item_genderinfo);
        Distance = findViewById(R.id.tv_item_distanceinfo);
        Intro = findViewById(R.id.tv_item_introinfo);
        Hobiies = findViewById(R.id.tv_item_hobbiesinfo);
        btn_like = findViewById(R.id.btn_likeinfo);
        btn_dislike = findViewById(R.id.btn_dislikeinfo);
        btn_back = findViewById(R.id.btn_PartnerViewFormToMain);

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
      viewPager2.setAdapter(new ViewPagerAdapter(reallist));
        // 페이져뷰로넘어갈 자료 구성후 페이져뷰로 리스트데이터 넘긴다,

        SetValues();


        btn_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikes(phonewithlikes,mynum);
                Intent dislike = new Intent(seePartnerProfileinfoActivity.this, MatchingActivity.class);
                dislike.putExtra("removedisphone", phonewithlikes);
                startActivity(dislike);
                finish();

            }
        });


        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likes(phonewithlikes,mynum);
                getLikelist(phonewithlikes);
//                getlistformatching(phonewithlikes,token);

                }

        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ////////////////////////////////////신고/////////////////////////////////////////////////////////
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bt=new BottomSheetDialog(seePartnerProfileinfoActivity.this);
                View view= LayoutInflater.from(seePartnerProfileinfoActivity.this).inflate(R.layout.report_layout,null);

//                view.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int videos_idx = videos_ArrayList.get(position).getVideos_idx();
//                        String videos_content = videos_ArrayList.get(position).getVideos_content();
//                        String videos_video = videos_ArrayList.get(position).getVideos_video();
//                        Intent intent = new Intent(Board_VideoActivity.this, Edit_VideoActivity.class);
//                        // 데이터 보내기
//                        intent.putExtra("videos_idx", videos_idx);
//                        intent.putExtra("videos_content", videos_content);
//                        intent.putExtra("videos_video", videos_video);
//                        intent.putExtra("user_email", user_email);
//                        intent.putExtra("user_nick", user_nick);
//                        intent.putExtra("user_password", user_password);
//                        startActivity(intent);
//                        finish();
//                        overridePendingTransition(0, 0);
//                        player.setPlayWhenReady(false);
//                        bt.dismiss();
//                    }
//                });

                view.findViewById(R.id.btn_ReporT).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(seePartnerProfileinfoActivity.this);
                        View mView2 = getLayoutInflater().inflate(R.layout.report_dialog, null);
                        Button btn_no = mView2.findViewById(R.id.btn_no);
                        Button btn_yes = mView2.findViewById(R.id.btn_yes);

                        // 삭제 눌렸을 떄
                        mBuilder2.setView(mView2);
                        final AlertDialog dialog2 = mBuilder2.create();
                        dialog2.show();
                        btn_yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog2.dismiss();
                                bt.dismiss();
                            }
                        });

                        // 삭제 취소 눌렸을 떄
                        btn_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                                bt.dismiss();
                            }
                        });
                    }
                });

                view.findViewById(R.id.btn_end).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bt.dismiss();
                    }
                });

                bt.setContentView(view);
                bt.show();

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");


    }

    private void dislikes(String likeswithnum, String phone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {
                        Log.d(TAG, "Dislikes리스트 저장 : "+ likeswithnum);


//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent dislike = new Intent(seeProfileinfoActivity.this, MatchingActivity.class);
//                                dislike.putExtra("removedisphone", phonewithlikes);
//                                startActivity(dislike);
//                                finish();
//                            }
//                        }, 1000); //딜레이 타임 조절


                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        dislikes add = new dislikes(likeswithnum, phone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }


    private void likes(String likeswithnum, String MYphone) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {

//                        Toast.makeText(seeProfileinfoActivity.this, "라이크 리스트: "+Llist, Toast.LENGTH_SHORT).show();

//                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                        SharedPreferences.Editor autoLogin = auto.edit();
//                        autoLogin.remove("likes");
//                        autoLogin.putString("likes",Llist);
//                        autoLogin.commit();
//                        Log.d(TAG, "리스트: " + Llist);



                    }else{
//                        Toast.makeText(seeProfileinfoActivity.this, "라이크받아오기 실패: "+success, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        likes add= new likes(likeswithnum, MYphone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }


    private void getLikelist(String partnerphone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {

                        partnerLikeList = success;
                        Log.d(TAG, "partnerLikeList리스트: " + partnerLikeList);

//                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                        SharedPreferences.Editor autoLogin = auto.edit();
//                        autoLogin.remove("likes");
//                        autoLogin.putString("likes",Llist);
//                        autoLogin.commit();
//                        Log.d(TAG, "리스트: " + success);

                        Goformatching(token);
                    }else{
                        Log.d(TAG, "partnerLikeList리스트:  비어있다");

//                        Toast.makeText(seeProfileinfoActivity.this, "라이크받아오기 실패: "+success, Toast.LENGTH_SHORT).show();
                        Goformatching(token);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        getlikelist add= new getlikelist(partnerphone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }



    private void loadmamber(String Pnum) {
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
                            distance = "";
                        }


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
        Log.d(TAG, "자료확인=" + city + job+" 인트로 "+intro+"나이 "+age+"  이름= "+name);

        if(distance.equals("0 km")){
               distance = "1 km";
        }

        if(city.equals("")){
            City.setVisibility(View.GONE);
        }
        if(job.equals("")){
            Job.setVisibility(View.GONE);
        }
        if(school.equals("")){
            School.setVisibility(View.GONE);
        }
        if(work.equals("")){
            WorkPlace.setVisibility(View.GONE);
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
        WorkPlace.setText(work);
        if(gender.equals("0")){
            Gender.setText("여성");
        } else if(gender.equals("1")){
            Gender.setText("남성");

        }
    }



    private void Goformatching( String tk)
    {
        sendFCM(token);
        if(partnerLikeList != null) {

            String[] likelist = partnerLikeList.split(",");
            for (int i = 0; i < likelist.length; i++) {

                if (mynum.equals(likelist[i])) {

                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);

                    Boolean Matchsischecked = auto.getBoolean("newMatch", false);

                    if (Matchsischecked) {
                        createNotificationChannel();
                        sendNotification();

                    }

                    Intent love = new Intent(seePartnerProfileinfoActivity.this, Matchedcouple_Activity.class);
                    love.putExtra("이성이미지", img);
                    love.putExtra("내이미지", myimg);
                    love.putExtra("token", tk);
                    love.putExtra("이름", myname);
                    love.putExtra("내번호", mynum);
                    love.putExtra("상대번호", phonewithlikes);
                    startActivity(love);
                    finish();
                } else
                    {
                            Intent like = new Intent(seePartnerProfileinfoActivity.this, MatchingActivity.class);
                            like.putExtra("removephone", phonewithlikes);
                            startActivity(like);
                            finish();
                    }
            }
        }
        else
        {
            Intent like = new Intent(seePartnerProfileinfoActivity.this, MatchingActivity.class);
            like.putExtra("removephone", phonewithlikes);
            startActivity(like);
            finish();
        }
    }



    void sendFCM(String token){
        Gson gson = new Gson();

        NotiModel notiModel = new NotiModel();
        notiModel.to = token;
//        notiModel.notification.title = " (nofi)누군가 좋아요를 해주셨습니다.";
//        notiModel.notification.text = " (nofi)누군가 좋아요를 해주셨습니다.";
        notiModel.data.title = " 좋아요 확인!";
        notiModel.data.text = " 누군가좋아요를 해주셨습니다.";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notiModel));

        Request request = new Request.Builder().header("Content-Type","application/json")
                .addHeader("Authorization", "key=AAAAZGdBsr4:APA91bEFvMXbmYJfPKEmFyPu-vUkjfoxf1rusZwYiDWG1gGDox2qtpmPtQ2iUi-F5WGbHCjl2Fr8P_ELpE8PX0BPDwaaSDYf8ijriPpEWD0rRach955loHmmMmGgjCEvkC8gLEZ2QqFt")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Toast.makeText(MatchingActivity.this, "푸시메세지 실패", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {


            }
        });
    }

    // 아래는 노티피케이션
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_app_icon)
                .setContentTitle("알림")
                .setContentText(name+"님과 매칭되었습니다!")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true); //notification을 탭 했을경우 notification을 없앤다
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(notificationId, builder.build());
    }
}