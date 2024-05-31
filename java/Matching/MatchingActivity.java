package Matching;

import static CouplesAndchat.Matched_Activity.getMsg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.honeybee.R;
import com.example.honeybee.TopNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import Profile.GetMyinfo;
import Profile.GpsTracker;
import Profile.profile;
import Utills.IPadress;
import Utills.NotiModel;
import Utills.PulsatorLayout;
import Utills.gmailsender;
import likesListAndActivity.Matchedcouple_Activity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MatchingActivity extends AppCompatActivity {
    // Channel에 대한 id 생성 : Channel을 구분하기 위한 ID 이다.
    private static final String CHANNEL_ID = "matchingCH";
    private static final String CHANNEL_ID2 = "likes";

    static int notificationId = 0;
    private GpsTracker gpsTracker;
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    Uri im;
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = MatchingActivity.this;
    final private int MY_PERMISSIONS_REQUEST_LOCATION = 123;


    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    ViewPager2 viewPager2;

    TextView tv;
    protected ImageButton btn_like, btn_dislike, btn_report;
    CardStackAdapter adapter;
    CardStackLayoutManager manager;
    CardStackView cardview;
    ImageView myimg, goprofile;
    FrameLayout cardFrame, moreFrame;
    LinearLayout imgNavi;
    String name;
    String img;
    String img2;
    String img3;
    String img4;
    String img5;
    String img6;
    String hobbies;
    String gender;
    String workPlace;
    String school;
    String age;
    String phone;
    String location;
    String getMytoken,token;
    String la;
    String Long;
    String intro;
    String job;
    String distance;
    String hideage;
    String unmatched;
    String removephone,removedisphone;
    int dist;
    double gd;

    private String lati;
    private String longit;
    private CountDownTimer countDownTimer;

    String likes,Dislikes, mylikes, pm, dis, img1;
    String dislikenum = null;
    String somelikesme;
    String HobbiesReplaced;
    String G, num, myname;
    String likeWithphone, disLikeWithphone;
    String setG;

    String[] pnum;
    String[] mlike, mdislike;

    String mlikes, mdislikes;
    String[] Llist;

    double latitude;
    double longitude;

    List<ItemModel> users;
    String[] likeslist, Dislikelist;
    private ArrayList<ItemModel> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

//        Toast.makeText(MatchingActivity.this, "onCreate", Toast.LENGTH_SHORT).show();
        items = new ArrayList<>();
        users = new ArrayList<>();
        getMsg = false;
        Log.e(TAG, "getMsg : "+getMsg );
        Log.d(TAG, "onCreate");

        setupTopNavigationView();
        btn_dislike = findViewById(R.id.dislikebtn);
        btn_like = findViewById(R.id.likebtn);
        btn_report = findViewById(R.id.btn_report);
        cardview = findViewById(R.id.img_cardview);
        imgNavi = findViewById(R.id.imgbtn_navi);
        cardFrame = findViewById(R.id.card_frame);
        moreFrame = findViewById(R.id.more_frame);
        myimg = findViewById(R.id.img_main);
        goprofile = findViewById(R.id.img_go_profile);

        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();



        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        } else {

            checkRunTimePermission();
        }

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        num = auto.getString("phone", null);
        img1 = auto.getString("img1", null);
        myname = auto.getString("name", " ");
        G = auto.getString("gender", null);
//        Boolean hideDisisChecked = auto.getBoolean("거리숨기기",false);

        im = Uri.parse(IP_ADDRESS + img1);
        Glide.with(this).load(im).into(myimg);
        Glide.with(this).load(im).into(goprofile);

/////////////////////FCM토큰 and 위도경도 저장하기  ////////////////////////////////////////////////
//        if(!hideDisisChecked){
            SaveCurrentLo();
//        }

/////////////////////////////////////////////////////////////////////////////////////////////
        btn_report.setOnClickListener(new View.OnClickListener() {
                        @Override
            public void onClick(View v) {
        final BottomSheetDialog bt=new BottomSheetDialog(MatchingActivity.this);
        View view= LayoutInflater.from(MatchingActivity.this).inflate(R.layout.report_layout,null);

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
                final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(MatchingActivity.this);
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
                        if(!items.isEmpty()) {

                    try {
                        gmailsender gMailSender = new gmailsender("ggambuk@gmail.com", "ojfuyyrucajyyqow");
                        //GMailSender.sendMail(제목, 본문내용, 받는사람);
//                    gMailSender.sendMail("제목입니다", message.getText().toString(), textView.getText().toString());
                        gMailSender.sendMail("신고내역", "아이디 = " +items.get(0).getName()+" | 내용 = 적절지않은 사진", "liam11th@naver.com");

                        Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                    } catch (SendFailedException e) {
                        Log.e("email리포트", e.toString());
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    } catch (MessagingException e) {
                        Log.e("email리포트", e.toString());
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
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


//        btn_report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(!items.isEmpty()) {
//
//                    try {
//                        gmailsender gMailSender = new gmailsender("ggambuk@gmail.com", "ojfuyyrucajyyqow");
//                        //GMailSender.sendMail(제목, 본문내용, 받는사람);
////                    gMailSender.sendMail("제목입니다", message.getText().toString(), textView.getText().toString());
//                        gMailSender.sendMail("제목입니다", "아이디 = " +items.get(0).getName(), "liam11th@naver.com");
//
//                        Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
//                    } catch (SendFailedException e) {
//                        Log.e("email리포트", e.toString());
//                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
//                    } catch (MessagingException e) {
//                        Log.e("email리포트", e.toString());
//                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });

        goprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gopro = new Intent(MatchingActivity.this, profile.class);
                startActivity(gopro);
                finish();
            }
        });

        btn_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator((Interpolator) new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardview.swipe();
                users = adapter.getItems();
                disLikeWithphone = users.get(0).getPhone();
                dislikes(disLikeWithphone, num);
                items.remove(0);
                adapter.notifyItemRemoved(0);


            }
        });


        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator((Interpolator) new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardview.swipe();
                Log.d(TAG, "좋아요 버튼클릭 =");
                // 좋아요를 클릭하여 매칭이되면 다시 카드스택뷰로가서도 매칭된 유저를 보는 이상한 상황이생기기에 핸들러를 통해 애니메이션이 끝이나면 아이템제거 노티스를 알린다
                Handler hd = new Handler();
                hd.postDelayed(new SwipeRight(), 200);


            }
        });

        ///////////////////////////////
        Intent intent = getIntent();
        removephone = intent.getStringExtra("removephone");
        removedisphone = intent.getStringExtra("removedisphone");

        if (removephone == null) {
            removephone = "";
        }
        if (removedisphone == null) {
            removedisphone = "";
        }

///////////////////////////

        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

                Log.d(TAG, "onCardDragging: d=" + direction.name() + " 비율=" + ratio);

            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);

                if (direction == Direction.Right) {
                    Log.d(TAG, "좋아요 슬라이드 오른쪽 ");
                    users = adapter.getItems();
                    Log.d(TAG, "좋아요표시상대=" + users.get(0).getName() + " d=" + direction);
                    likeWithphone = users.get(0).getPhone();
                    String tk = users.get(0).getToken();
                    String someimg = users.get(0).getImage();
                    likes(likeWithphone, num);

                    sendFCM(users.get(0).getToken());
                    items.remove(0);
                    adapter.notifyItemRemoved(0);
                    adapter.notifyDataSetChanged();
                    checkRowItem();
                    //////// getLikelist메소드를통해 Matchedcouple_Activity로이동 Matchedcouple_Activity에서 fcm매칭푸쉬메세지로 보낸다.
                    getLikelist(likeWithphone, tk, someimg);



                }
                if (direction == Direction.Top) {

                    items.remove(0);
                    adapter.notifyDataSetChanged();
                    checkRowItem();

                }
                if (direction == Direction.Left) {
                    users = adapter.getItems();
                    disLikeWithphone = users.get(0).getPhone();
                    Log.d(TAG, "싫어요 표시상대=" + disLikeWithphone + " d=" + direction);
                    dislikes(disLikeWithphone, num);
                    items.remove(0);
                    adapter.notifyDataSetChanged();
                    checkRowItem();


                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound2: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", name: " + tv.getText());

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCardDisappeared(View view, int position) {
                tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardDisappeared: " + position + ", name: " + tv.getText());

            }
        });





    }//..onCreate

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onReasume");
        // 상대 유저정보를 다불러오는 메소드이다.
        // 카드스택뷰에들어갈 유저를 정보와 메소드를 통해 걸러낸다.
        loadmamberInfo();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        // 내가 싫어요 체크한유저 번호를 통신을통해 받아온다.
        //getDislikes(num);
        // 나의 좋아요 리스트를 다받아온다음 싫어요리스트를 받는 통신메소드이다.
      //  getLikelist(num);
        MyloadmamberInfo(num);
    }


    private void SaveCurrentLo() {
        gpsTracker = new GpsTracker(MatchingActivity.this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        lati = String.valueOf(latitude);
        longit = String.valueOf(longitude);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "FCM토큰 받기 실패", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        getMytoken = task.getResult();

                        savelocation(lati, longit, num, getMytoken);
                        // Log
                        Log.e(TAG,"토큰 : " + getMytoken.toString());

                    }
                });

    }



    private void getLikelist(String phone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {
//                        Toast.makeText(MatchingActivity.this, "겟라이크 리스트: "+success, Toast.LENGTH_SHORT).show();
                        mylikes = success;

                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.remove("likes");
                        autoLogin.putString("likes",mylikes);
                        autoLogin.commit();
                        Log.d(TAG, "리스트: " + success);


                    }else{
                        Log.d(TAG, "좋아요 번호 리스폰스 실패: " + success);
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

    private void getDislikes(String phone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {
//                        Toast.makeText(seeProfileinfoActivity.this, "dislist성공: " + success, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "내가 싫어한 사람리스트: " + success);
                        Dislikes = success;

                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.remove("dislikes");
                        autoLogin.putString("dislikes", Dislikes);
                        autoLogin.commit();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        getDislikes add= new getDislikes(phone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }
    /**
     * botton 툴바 설치 void 주요 엑티비티마다 붙여 넣는다
     */
    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    // 좋아요 표시한 번호 DB에저장한다
    private void likes(String likeswithnum, String phone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null) {
//                        Toast.makeText(MatchingActivity.this, "호감가는분 추가 성공", Toast.LENGTH_SHORT).show();


                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        likes add = new likes(likeswithnum, phone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }

    private void dislikes(String likeswithnum, String phone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
//                        Toast.makeText(MatchingActivity.this, "싫어요추가", Toast.LENGTH_SHORT).show();

                    } else {
                        return;
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

    private void getLikelist(String phone, String tk, String someimg) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {
//                        Toast.makeText(MatchingActivity.this, "라이크 리스트업데이트: " + success, Toast.LENGTH_SHORT).show();
                        Llist = success.split(",");

                        try {

                            for (int i = 0; i < Llist.length; i++) {

                                if (num.equals(Llist[i])) {

                                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);

                                    Boolean Matchsischecked = auto.getBoolean("newMatch", false);

                                    if (Matchsischecked)
                                    {
                                        createNotificationChannel();
                                        sendNotification();

                                    }
                                    items = new ArrayList<>();
                                    Intent love = new Intent(MatchingActivity.this, Matchedcouple_Activity.class);
                                    love.putExtra("이성이미지", someimg);
                                    love.putExtra("내이미지", img1);
                                    love.putExtra("token", tk);
                                    love.putExtra("이름", myname);
                                    love.putExtra("내번호", num);
                                    love.putExtra("상대번호", likeWithphone);
                                    love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(love);
                                    finish();
                                }
                            }
                        } catch (Exception e) {

                        }

                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        getlikelist add = new getlikelist(phone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }


    private void savelocation(String lati, String longit, String phone, String Mytoken) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null) {
//                        Toast.makeText(MatchingActivity.this, "거리정보업데이트 성공", Toast.LENGTH_SHORT).show();


                    } else {
//                        Toast.makeText(MatchingActivity.this, "거리정보업데이트 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        savemLocation add = new savemLocation(lati, longit, phone, Mytoken, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

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
        String TAG_unmatched = "unmatched";
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
                        unmatched = item.getString(TAG_unmatched);
                        workPlace = item.getString(TAG_work);

                        String hobbiesReplaced = hobbies.replace("[", " ").trim();
                        HobbiesReplaced = hobbiesReplaced.replace("]", " ").trim();

                        likes = likes.replace(" ", "");
                        likeslist = likes.split(",");

          ;

                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                        Boolean unlimitD = auto.getBoolean("unlimitDis",false);
                        String minAge = auto.getString("min", "0");
                        String maxAge = auto.getString("max", "50");

                        Dislikes = auto.getString("dislikes", null);
                        Dislikes = Dislikes.replace(" ", "");
                        mdislike = Dislikes.split(",");


                        int prog = auto.getInt("distance", 1);
                        int mw = auto.getInt("mw", 2);


                        if (mw == 2) {
                            setG = G;
                        } else {
                            setG = String.valueOf(mw);

                        }

                        mylikes = auto.getString("likes", null);
                        mylikes = mylikes.replace(" ", "");
                        mlike = mylikes.split(",");

                        int Age = Integer.parseInt(age);
                        int min = Integer.parseInt(minAge);
                        int max = Integer.parseInt(maxAge);

                        // 설정으로 나이 숨기기

                        if(hideage.equals("h")){
                                  age="";
                        }
                       // 설정
                        try{

                        if (Long.equals("") && la.equals("")) {
                            dist = 0;
                        } else if(unlimitD){
                            gd = getDistance(latitude, longitude, Double.parseDouble(la), Double.parseDouble(Long));
                            distance = String.valueOf(Math.round(gd));
                            dist = 0;
                        }else {
                            gd = getDistance(latitude, longitude, Double.parseDouble(la), Double.parseDouble(Long));
                            distance = String.valueOf(Math.round(gd));
                            dist = Integer.parseInt(distance);
                        }

                            if(gd == 0){
                                distance = "1";
                            }


                        }catch (NumberFormatException e){
                            Log.d(TAG, "거리설정= " + e);
                        }


                        String nnum = findnum(phone);
                        String disnum = findDislike(phone);
                        if (setG.equals(gender) || phone.equals(num) || nnum != null || disnum != null || Age < min || max < Age || dist > prog ||
                                removephone.equals(phone) ||  removedisphone.equals(phone) || phone.equals("+1004")) {

                            // 어레이리스트에 못들어간다.
                        } else if (Long.equals("") && la.equals("")) {
                            items.add(new ItemModel(phone ,img, gender, school, intro, job, name, age, location, HobbiesReplaced, token, distance+" km", img2, img3, img4, img5, img6, likeslist,"",countDownTimer,"여기선필요없는 인덱스", workPlace));
                            Log.d(TAG, "이름, 번호=" + name + phone);

                            Log.d(TAG, "items 개수 = " + items.size());
                        }
                        else {

                            items.add(new ItemModel(phone ,img, gender, school, intro, job, name, age, location, HobbiesReplaced, token, distance+" km", img2, img3, img4, img5, img6, likeslist,"",countDownTimer,"여기선필요없는 인덱스", workPlace));
                            Log.d(TAG, "이름, 번호=" + name + phone);
                            Log.d(TAG, "items 개수 = " + items.size());
                        }


                    } // ..for


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                }

                // mdislike.length [] = length=1; index=1
                // mdislike.length [+821033334422] = length=1; index=1
                //위처럼 인덱스 개수로 조건을주기 어려워서 옆에와 같은 방식사용  if(mdislike[0].startsWith("+"))


                manager.setStackFrom(StackFrom.None);
                manager.setVisibleCount(3);
                manager.setTranslationInterval(8.0f);
                manager.setScaleInterval(0.95f);
                manager.setSwipeThreshold(0.3f);
                manager.setMaxDegree(20.0f);
                manager.setDirections(Direction.FREEDOM);
                manager.setCanScrollHorizontal(true);
                manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
                manager.setOverlayInterpolator(new LinearInterpolator());
                adapter = new CardStackAdapter(items);
                cardview.setLayoutManager(manager);
                cardview.setAdapter(adapter);
                cardview.setItemAnimator(new DefaultItemAnimator());

                checkRowItem();

            }

        };
        loadInfo add = new loadInfo(responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }

    ////////////////////////////My loadmamberInfo/////////////////////////////////////////////////
    public void MyloadmamberInfo(String myphone) {


        String TAG_JSON="webnautes";

        String TAG_likes = "likes";
        String TAG_dislikes = "dislikes";



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

                        mlikes = item.getString(TAG_likes);
                        mdislikes = item.getString(TAG_dislikes);

                        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.remove("likes");
                        autoLogin.putString("likes", mlikes);

                        autoLogin.remove("dislikes");
                        autoLogin.putString("dislikes", mdislikes);
                        autoLogin.commit();

                        Log.d(TAG, "내가 좋아요한 리스트: " + mlikes);
                        Log.d(TAG, "내가 싫어요한 리스트: " + mdislikes);


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
        RequestQueue queue = Volley.newRequestQueue(MatchingActivity.this);
        queue.add(add);

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


    private void checkRowItem() {
        if (items.isEmpty()) {
            moreFrame.setVisibility(View.VISIBLE);
            cardFrame.setVisibility(View.GONE);
            imgNavi.setVisibility(View.VISIBLE);
        }
    }

    public String findnum(String n) {
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


// 아래는 노티피케이션
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID2, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID2)
                .setSmallIcon(R.drawable.notification_app_icon)
                .setContentTitle("알림")
                .setContentText(tv.getText()+"님과 매칭되었습니다!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setAutoCancel(true); //notification을 탭 했을경우 notification을 없앤다
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(notificationId, builder.build());
    }

    void sendFCM(String token){
        Gson gson = new Gson();

        NotiModel notiModel = new NotiModel();
        notiModel.to = token;
//        notiModel.notification.title = " (nofi)누군가 좋아요를 해주셨습니다.";
//        notiModel.notification.text = " (nofi)누군가 좋아요를 해주셨습니다.";
        notiModel.data.title = "좋아요 확인!";
        notiModel.data.text = " 누군가좋아요를 해주셨습니다.";
        notiModel.data.body = " 누군가좋아요를 해주셨습니다.";
        notiModel.data.clickAction = "seelikes_Activity";

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



    // GPS 관련메소드모음

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MatchingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MatchingActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MatchingActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MatchingActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MatchingActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MatchingActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }



    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAdminArea().toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchingActivity.this);
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

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    class SwipeRight implements Runnable {
        public void run() {
            adapter.notifyItemRemoved(0);

        }
    }
}