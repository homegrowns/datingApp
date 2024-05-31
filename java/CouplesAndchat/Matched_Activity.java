package CouplesAndchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.honeybee.R;
import com.example.honeybee.TopNavigationViewHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Matching.getDislikes;
import Matching.getlikelist;
import Matching.itemMoFormsg;
import Matching.loadInfo;
import Profile.profile;
import Utills.IPadress;
import likesListAndActivity.seelikes_Activity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Matched_Activity extends AppCompatActivity  {
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    Uri im;
    RecyclerView recyclerChat,recyclermatch;
    static coupleAdapter coupleAdapter;
    @SuppressLint("StaticFieldLeak")
    private static couplechatAdapter ChatAdapter;

    Context context;
    private TextView NumOflike;
    private TextView allchat;
    private static TextView nomatch;
    private EditText searchMatch;
    public static boolean getMsg = false;
    protected boolean matchitemsOn = true, chatRecitemsOn = true;
    boolean chatok = false;
    public static boolean NoMatch = false;
//    List<ItemModel> items;
    ArrayList<itemMoFormsg> filteredList;
    ArrayList<itemMoFormsg> FiterMatch;
    static ArrayList<itemMoFormsg> msgItems;

     static List<itemMoFormsg> items;

    List<itemMoFormsg> toSeelikesitems = new ArrayList<>();

    //    private ArrayList<ItemModel> SendingUserto = new ArrayList<>();
    private ArrayList<String> RoomNums = new ArrayList<>();
    private ArrayList<String> targetnums = new ArrayList<>();
    private ArrayList<dayAndnumFromchat> keyAndday;
    protected static ArrayList<MdateAndRoom> MdateAndRoom;

    private ArrayList<String> newMsg = new ArrayList<>();
    static ArrayList<String> Finaltargetnums = new ArrayList<>();
    static ArrayList<String> ChatUsernums;

    private Context mContext = Matched_Activity.this;
    private static final int ACTIVITY_NUM = 2;
    private static final String TAG = "Matched_Activity";
    private CountDownTimer countDownTimer;
    ImageView imgbtn;
    ImageButton goToSeeLikes;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.

    // 1페이지에 10개씩 데이터를 불러온다
    int page = 0, limit = 5, nums =0;
    int pageInchat = 0, Dlimit = 5;
    int disNums = 0;
    private DatabaseReference ref = database.getReference();

    String[] somelist,mlike;
    String somelikeme, disLikeme = null;
    double latitude;
    double longitude;
    String ihaveMsg = "no";
    String unmatched;
    String numOfmatch;
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
    String location;
    String token;
    String la;
    String Long;
    String intro;
    String job;
    String distance;
    String hideage;
    String likes;
    String NEWmsg="";
    String CreatedDay="";
    String Mdate="";

    static ArrayList<String> mylike = new ArrayList<>();
    private  String Dislikes;
    private ArrayList<String> mdislike = new ArrayList<>();

    public static  String Likelist;

     ArrayList<String> numOflikes = new ArrayList<>();;

    ProgressBar progressBarUp,progressBarDown;
    static String myphone;
    static String pm, dis;
    static boolean ShowWechat = false;
    MyReceiver mr = new MyReceiver();
    public static boolean goBack = false;
    public static boolean Controllfilter = false;
    static String MSGFromFcm;
    static String numFromFcm;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        setupTopNavigationView();

        Log.e(TAG, "onCreate");
        getMsg = true;
        NoMatch = false;
        Log.e(TAG, "getMsg : "+getMsg );

        items = new ArrayList<>();
        FiterMatch = new ArrayList<>();
        filteredList = new ArrayList<>();
        msgItems = new ArrayList<>();
        keyAndday = new ArrayList<>();
        MdateAndRoom = new ArrayList<>();
        ////////////////////////////////// 소프트키보드가 에딧텍스트를 겹치게 하지안게한다///////////////////
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        goToSeeLikes = findViewById(R.id.imgbtn_likesInMatchedA);
        recyclerChat = findViewById(R.id.recycler_chat);
        recyclermatch = findViewById(R.id.recycler_newMatch);
        imgbtn = findViewById(R.id.img_go_profile);
        NumOflike = findViewById(R.id.tv_NumOflike);
        searchMatch = findViewById(R.id.edit_searchFormatch);
        allchat = findViewById(R.id.tv_allChat);
        nomatch = findViewById(R.id.tv_noMatch);
        progressBarUp = findViewById(R.id.progress_bar);
        progressBarDown = findViewById(R.id.progress_barDown);

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        myphone = auto.getString("phone",null);
        userInchat(myphone);
        ChatDatafirst(pageInchat, Dlimit,myphone);
//        String pa = String.valueOf(page);
//        String lim = String.valueOf(limit);
//        GetDate(pa,lim);
        getData(page,limit);
//        getLikelist(myphone);

        String img1 = auto.getString("img1",null);
        im =  Uri.parse(IP_ADDRESS+img1);
        Glide.with(this).load(im).into(imgbtn);
        Glide.with(this).load(R.drawable.ic_baseline_whatshot_24).into(goToSeeLikes);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gopro = new Intent(Matched_Activity.this, profile.class);
                startActivity(gopro);
                finish();
            }
        });


        goToSeeLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seelike = new Intent(v.getContext(), seelikes_Activity.class);
                v.getContext().startActivity(seelike);
                finish();
            }
        });

        searchMatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = searchMatch.getText().toString();

                searchFilter(searchText);

            }
        });

     //       nestedScrollView1.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
       //         @Override
       //         public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
       //             int it = v.getChildAt(0).getMeasuredWidth()-v.getMeasuredWidth();
       //             Log.d(TAG, "scrollX= "+scrollX+"  v.getChildAt = "+it);
       //             if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
       //             {

       //                 page++;
       //                 progressBarUp.setVisibility(View.VISIBLE);
        //                getData(page, limit);
        //            }
        //        }
       //     });

        recyclermatch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // 최상단, 최하단 감지해야 할 때
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollHorizontally(-1)) {
                   Log.e("페이징 로그 1 ", "왼 부분 : " + recyclerView.canScrollHorizontally(-1));
                } else if (!recyclerView.canScrollHorizontally(1)) {
                    Log.e("페이징 로그 2 ", "오른 부분 : " + recyclerView.canScrollHorizontally(1));
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 현재 보이는 것 중 마지막 아이템의 포지션 +1
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                Log.e("페이징 로그 3 ", "무엇일까? : " + lastVisibleItemPosition);
                // 전체 아이템 갯수  || 현재 아이템 갯수
                int itemTotalCount = recyclerView.getAdapter().getItemCount();
//                Log.e("페이징 로그 4 ", "무엇일까? : " + itemTotalCount);
                if (lastVisibleItemPosition == itemTotalCount -1 && page < itemTotalCount-1 && itemTotalCount > 4) {

                    Log.e("게시판 끝 부분 ? : ", String.valueOf(lastVisibleItemPosition));
                    Log.e("페이징 로그 5 ", "무엇일까? : " + itemTotalCount);
                    Log.e("페이징 로그 6 ", "무엇일까? : " + lastVisibleItemPosition);
                    Log.e("페이징 로그 7 ", "무엇일까? : " + page);
                     progressBarUp.setVisibility(View.VISIBLE);

                    // page++;
                    new Handler().postDelayed(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            //딜레이 후 시작할 코드 작성
                            page += 5;
//                              userInchat(myphone);
                            ChatDatafirst(page, limit,myphone);
                                getData(page, limit);
//                                String pa = String.valueOf(page);
//                                String lim = String.valueOf(limit);
//                                GetDate(pa,lim);
//                                Log.e("전체 아이템 갯수=", String.valueOf(itemTotalCount));
//                                Log.e("page ", "무엇일까? : " + page);


                        }
                    }, 300);// 0.6초 정도 딜레이를 준 후 시작

                }
            }
        });
        // todo

        recyclerChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // 최상단, 최하단 감지해야 할 때
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollHorizontally(-1)) {
                    Log.e("페이징 로그 1 ", "왼 부분 : " + recyclerView.canScrollHorizontally(-1));
                } else if (!recyclerView.canScrollHorizontally(1)) {
                    Log.e("페이징 로그 2 ", "오른 부분 : " + recyclerView.canScrollHorizontally(1));
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 현재 보이는 것 중 마지막 아이템의 포지션 +1
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                Log.e("페이징 로그 3 ", "무엇일까? : " + lastVisibleItemPosition);
                // 전체 아이템 갯수  || 현재 아이템 갯수
                int itemTotalCount = recyclerView.getAdapter().getItemCount();
//                Log.e("페이징 로그 4 ", "무엇일까? : " + itemTotalCount);
                if (lastVisibleItemPosition == itemTotalCount -1 && pageInchat < itemTotalCount-1 && itemTotalCount > 3) {

                    Log.e("게시판 끝 부분 ? : ", String.valueOf(lastVisibleItemPosition));
                    Log.e("페이징 로그 5 ", "무엇일까? : " + itemTotalCount);
                    Log.e("페이징 로그 6 ", "무엇일까? : " + lastVisibleItemPosition);
                    Log.e("페이징 로그 7 ", "무엇일까? : " + pageInchat);
                    progressBarDown.setVisibility(View.VISIBLE);


                    new Handler().postDelayed(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            //딜레이 후 시작할 코드 작성

                           // pageInchat += 5;
                            Dlimit +=5;
                            chatok = true;
                            getChatData(pageInchat, Dlimit,myphone);

                        }
                    }, 300);// 0.6초 정도 딜레이를 준 후 시작

                }
            }
        });
    } // ..onCreate

/////////////////////////////////////////////////////////////////////////////////////////////
    private void getData(int page, int limit) {
        // 레트로핏 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_ADDRESS + "/GetMatchDay.php/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.string_call(page, limit);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response)
            {

                if (response.isSuccessful() && response.body() != null) {
                    progressBarUp.setVisibility(View.GONE);

                    try {
                        final String responseData = response.body();
                        JSONObject jsonObject = new JSONObject(responseData);

                            Log.e(TAG, "data received= "+responseData);

                            parseResult(jsonObject);
                            matchitemsOn = true;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Log.e("에러 : ", t.getMessage());
            }
        });
    }

    private void parseResult(JSONObject jsonObject)
    {

        String TAG_JSON = "webnautes";
        String TAG_MatchDate = "Mday";
        String TAG_room = "room";
        MdateAndRoom = new ArrayList<>();

        JSONArray jsonArray = null;

        try {
            jsonArray = jsonObject.getJSONArray(TAG_JSON);

            Log.d(TAG, "페이지GetDate 어레이 수 (parseResult) =" + jsonArray.length() + "개");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String Mday = item.getString(TAG_MatchDate);
                String room = item.getString(TAG_room);

                ///// 방번호와 방생성날자를 어레이에 넣어서 비교한다/////
                MdateAndRoom.add(new MdateAndRoom(Mday, room));
                Log.e(TAG, "매치데이 = " + MdateAndRoom.get(i).getMday());
                Log.e(TAG, "방번호 = " + MdateAndRoom.get(i).getRoom());
            }
            getLikelist(myphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    ///////////////////// 처음 시작 챗 아이템 받기////////////////////////////////////////////

    private void ChatDatafirst(int page, int limit,String phone) {
        // 레트로핏 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_ADDRESS + "/findChatroomInfo.php/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.string_call(page, limit);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response)
            {

                if (response.isSuccessful() && response.body() != null) {

                    try {
                        final String responseData = response.body();
                        Log.e(TAG, "data received= "+responseData);

                        if(responseData.equals("1")){
                            Log.e(TAG, "data received In chatdat findChatroomInfo.php= "+responseData);

                        }
                        else
                        {
                            Log.e(TAG, "data received In chatdata findChatroomInfo.php= "+responseData);

                            JSONObject jsonObject = new JSONObject(responseData);
                            parseResultchat(jsonObject, phone);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Log.e("에러 : ", t.getMessage());
            }
        });
    }

    private void parseResultchat(JSONObject jsonObject,String Myphone)
    {

        String TAG_JSON = "webnautes";
        String TAG_phoneNums = "member";
        String TAG_Cday = "day";
        String TAG_room = "room";
        String TAG_msg = "msg";
        Finaltargetnums = new ArrayList<>();
        targetnums = new ArrayList<>();
        JSONArray jsonArray = null;

        try {
            jsonArray = jsonObject.getJSONArray(TAG_JSON);

            Log.d(TAG, "parseResult2 어레이 수: =" + jsonArray.length() + "개");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String TargetNum = item.getString(TAG_phoneNums);
                String createdDay = item.getString(TAG_Cday);
                String roomkey = item.getString(TAG_room);
                String msg = item.getString(TAG_msg);

                //////////////////////////////////////////////////

                String[] array = TargetNum.split("[|]");
//                        String[] Target = new String[jsonArray.length()];
                Log.e(TAG, "메세지어뎁터용번호 자료받기 확인 "+array);

                ////////////1차 거르기/////////////
                if (array[0].equals(Myphone) || array[1].equals(Myphone)) {
                    targetnums.add(TargetNum);
                }

                ///// 방번호와 방생성날자를 어레이에 넣어서 비교한다/////
                keyAndday.add(new dayAndnumFromchat(roomkey,createdDay,msg));
                Log.e(TAG, "메세지어뎁터용번호 자료받기 확인 "+createdDay);

                //////////////////////////////////////////////////
//                        for (int z = 0; z < array.length; z++) {
//                            if (Myphone.equals(array[z])) {
//                                Target[z] = TargetNum;
//                            }
//                        }
            }
//////////////////////////////////////////////////////////////////////////////////////////////


            for (int i = 0; i < targetnums.size(); i++){
                String[] finalTarget = targetnums.get(i).split("[|]");
                if(!finalTarget[0].equals(Myphone)){
                    Finaltargetnums.add(finalTarget[0]);
                    // 객체만들어서 번호와 날짜저장
                }
                else if(!finalTarget[1].equals(Myphone)){
                    Finaltargetnums.add(finalTarget[1]);
                }
            }

            Log.d(TAG, "메세지어뎁터용번호 자료받기 확인");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
/////////////////////////////////////////////////////////////////////////////////////


    private void getChatData(int page, int limit,String phone) {
        // 레트로핏 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_ADDRESS + "/findChatroomInfo.php/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.string_call(page, limit);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response)
            {

                if (response.isSuccessful() && response.body() != null) {
                    progressBarDown.setVisibility(View.GONE);

                    try {
                        final String responseData = response.body();
                        Log.e(TAG, "data received= "+responseData);

                        if(responseData.equals("1")){
                            Log.e(TAG, "data received In chatdata= "+responseData);

                        }
                        else
                        {
                            Log.e(TAG, "data received In chatdata= "+responseData);

                            JSONObject jsonObject = new JSONObject(responseData);
                            parseResult2(jsonObject, phone);
                            chatRecitemsOn = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Log.e("에러 : ", t.getMessage());
            }
        });
    }

    private void parseResult2(JSONObject jsonObject,String Myphone)
    {

        String TAG_JSON = "webnautes";
        String TAG_phoneNums = "member";
        String TAG_Cday = "day";
        String TAG_room = "room";
        String TAG_msg = "msg";
        Finaltargetnums = new ArrayList<>();
        targetnums = new ArrayList<>();
        JSONArray jsonArray = null;

        try {
            jsonArray = jsonObject.getJSONArray(TAG_JSON);

            Log.d(TAG, "parseResult2 어레이 수: =" + jsonArray.length() + "개");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String TargetNum = item.getString(TAG_phoneNums);
                String createdDay = item.getString(TAG_Cday);
                String roomkey = item.getString(TAG_room);
                String msg = item.getString(TAG_msg);

                //////////////////////////////////////////////////

                String[] array = TargetNum.split("[|]");
//                        String[] Target = new String[jsonArray.length()];
                Log.e(TAG, "메세지어뎁터용번호 자료받기 확인 "+array);

                ////////////1차 거르기/////////////
                if (array[0].equals(Myphone) || array[1].equals(Myphone)) {
                    targetnums.add(TargetNum);
                }

                ///// 방번호와 방생성날자를 어레이에 넣어서 비교한다/////
                keyAndday.add(new dayAndnumFromchat(roomkey,createdDay,msg));
                Log.e(TAG, "메세지어뎁터용번호 자료받기 확인 "+createdDay);

                //////////////////////////////////////////////////
//                        for (int z = 0; z < array.length; z++) {
//                            if (Myphone.equals(array[z])) {
//                                Target[z] = TargetNum;
//                            }
//                        }
            }
//////////////////////////////////////////////////////////////////////////////////////////////


            for (int i = 0; i < targetnums.size(); i++){
                String[] finalTarget = targetnums.get(i).split("[|]");
                if(!finalTarget[0].equals(Myphone)){
                    Finaltargetnums.add(finalTarget[0]);
                    // 객체만들어서 번호와 날짜저장
                }
                else if(!finalTarget[1].equals(Myphone)){
                    Finaltargetnums.add(finalTarget[1]);
                }
            }

            Log.d(TAG, "메세지어뎁터용번호 자료받기 확인");

            getLikelist(myphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    //////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onStart() {
        super.onStart();
        goBack = true;

        // 내가 싫어요 체크한유저 번호를 통신을통해 받아온다.
        getDislikes(myphone);

        if(ShowWechat){
            allchat.setVisibility(View.VISIBLE);
            Log.e(TAG, "show all chat");

        }
//        searchMatch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String searchText = searchMatch.getText().toString();
//
//                    searchFilter(searchText);
//                    searchFilterforM(searchText);
//
//            }
//        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy : 브로드캐스트 제거");
         unregisterReceiver(mr);


    }

    @Override
    protected void onResume() {
        super.onResume();

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            registerReceiver(mr, filter);
            //        현재 시스템 시간 구하기
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mr, filter2);

    }


    public void searchFilter(String searchText) {
        FiterMatch.clear();

        for (int i = 0; i < items.size(); i++) {
            /////////// 입렵한 텍스트를 아이템 이름과 비교하여 이름이 텍스트값을 포함하면 아래 어레이리스트(비어있는)에 해당값을 담는다.
            // 그리고 어뎁터메소드에 값전달//
            Log.e(TAG, "searchFilter= "+items.size());

            if (items.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                FiterMatch.add(items.get(i));
                Log.e(TAG, "searchFilter= "+items.get(i).getName()+"| 날짜="+items.get(i).getCday());

            }
        }

        coupleAdapter.filterList(FiterMatch);

        searchFilterforM(searchText);
    }


    public void searchFilterforM(String searchText) {
        filteredList.clear();
        Log.e(TAG, "searchFilterforM = "+msgItems.size());
        for (int i = 0; i < msgItems.size(); i++) {
            if (msgItems.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(msgItems.get(i));
            }
        }

        ChatAdapter.filterList(filteredList);
    }


    public static void changeOrder(String msg,String num) {
        MSGFromFcm = msg;
        numFromFcm = num;

        Log.d("fcm 인자전달", "chatAdapter.loadDay ");

       /// 삭제 fcm 메세지 받음 어뎁터 에서 통신으로 정보업데이트된다.
        if(msg.equals("") && num.equals("")){
//            ChatAdapter.GetLikelist(myphone);
            Log.d(TAG, "coupleAdapter.Getdate" + MSGFromFcm);

            coupleAdapter.Getdate(myphone);

        }
        /// 메세지 fcm 메세지 받음 어뎁터 에서 통신으로 정보업데이트된다.
        if(!msg.equals("") && !num.equals("")) {
            Log.d("fcm 인자전달", "adapter 메소드 2개다");
            coupleAdapter.Getdate(myphone);
            ChatAdapter.GetLikelist(myphone);

        }

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

    public static String findmylove(String n) {
        for (int z = 0; z < mylike.size(); z++) {
            if (mylike.get(z).equals(n)) {
                pm = mylike.get(z);
                return pm;
            }
        }
        return null;
    }

    public String findDislike(String n) {
        for (int z = 0; z < mdislike.size(); z++) {
            if (mdislike.get(z).equals(n)) {
                dis = mdislike.get(z);
                return dis;
            }
        }
        return null;
    }

    public void userInchat(String Myphone) {

        String TAG_JSON = "webnautes";
        String TAG_phoneNums = "member";
        String TAG_Cday = "day";
        String TAG_room = "room";
        String TAG_msg = "msg";
        ChatUsernums = new ArrayList<>();
        ArrayList<String> target = new ArrayList<>();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "MatchedA 어레이갯수: UI=" + jsonArray.length() + "개");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);

                        String TargetNum = item.getString(TAG_phoneNums);
                        String createdDay = item.getString(TAG_Cday);
                        String roomkey = item.getString(TAG_room);
                        String msg = item.getString(TAG_msg);

                        //////////////////////////////////////////////////

                        String[] array = TargetNum.split("[|]");
//                        String[] Target = new String[jsonArray.length()];
                        Log.e(TAG, "메세지어뎁터용번호 자료받기 확인 "+array);

                        ////////////1차 거르기/////////////
                        if (array[0].equals(Myphone) || array[1].equals(Myphone)) {
                            target.add(TargetNum);
                        }

                        ///// 방번호와 방생성날자를 어레이에 넣어서 비교한다/////
                        keyAndday.add(new dayAndnumFromchat(roomkey,createdDay,msg));
                        Log.e(TAG, "메세지어뎁터용번호 자료받기 확인 "+createdDay);

                        //////////////////////////////////////////////////
//                        for (int z = 0; z < array.length; z++) {
//                            if (Myphone.equals(array[z])) {
//                                Target[z] = TargetNum;
//                            }
//                        }
                    }


                    for (int i = 0; i < target.size(); i++){
                        String[] finalTarget = target.get(i).split("[|]");
                        if(!finalTarget[0].equals(Myphone)){
                            ChatUsernums.add(finalTarget[0]);
                            // 객체만들어서 번호와 날짜저장
                        }
                        else if(!finalTarget[1].equals(Myphone)){
                            ChatUsernums.add(finalTarget[1]);
                        }
                    }

                    Log.d(TAG, "메세지어뎁터용번호 자료받기 확인");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        userInChat add =new userInChat(responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
    }
////////////////////// userInchat //////////////////////////////

    /////////////////GetDate//////////////////////////////////
//    public void GetDate(String page,String limit) {
//
//        String TAG_JSON = "webnautes";
//        String TAG_MatchDate = "Mday";
//        String TAG_room = "room";
//        MdateAndRoom = new ArrayList<>();
//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//                    Log.d(TAG, "GetDate 어레이 수: d=" + jsonArray.length() + "개");
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject item = jsonArray.getJSONObject(i);
//
//                        String Mday = item.getString(TAG_MatchDate);
//                        String room = item.getString(TAG_room);
//
//                        ///// 방번호와 방생성날자를 어레이에 넣어서 비교한다/////
//                        MdateAndRoom.add(new MdateAndRoom(Mday,room));
//                        Log.e(TAG, "매치데이 = "+MdateAndRoom.get(i).getMday());
//                        Log.e(TAG, "방번호 = "+MdateAndRoom.get(i).getRoom());
//
//                    }
//                    getLikelist(myphone);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        };
//        MatchDayPage add =new MatchDayPage(page, limit,responseListener);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(add);
//    }
  ////////////////////////////////GetDate////////////////////////////////////////

    ///////////// page match리사이클/////////////
//    public void PageMatchInfo() { //안씀
//        String TAG_JSON = "webnautes";
//        String TAG_NAME = "name";
//        String TAG_p = "phone";
//        String TAG_AGE = "age";
//        String TAG_IMG = "img";
//        String TAG_gender = "gender";
//        String TAG_school = "school";
//        String TAG_intro = "intro";
//        String TAG_job = "job";
//        String TAG_work = "work";
//        String TAG_IMG2 = "img2";
//        String TAG_IMG3 = "img3";
//        String TAG_IMG4 = "img4";
//        String TAG_IMG5 = "img5";
//        String TAG_IMG6 = "img6";
//        String TAG_hobby = "hobbies";
//        String TAG_location = "location";
//        String TAG_tk = "token";
//        String TAG_likes = "likes";
//        String TAG_lati = "lati";
//        String TAG_longit = "longit";
//        String TAG_hideage = "hideage";
//        String TAG_unmatched = "unmatched";
//        String TAG_numOfmatch = "numOfmatch";
//
//        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//
//        progressBarUp.setVisibility(View.INVISIBLE);
//        numOflikes = new ArrayList<>();
//        nums = 0;
//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//
//            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
//            @Override
//            public void onResponse(String response) {
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//                    Log.d(TAG, "MatchedA 어레이갯수: =" + jsonArray.length() + "개");
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject item = jsonArray.getJSONObject(i);
//
//                        name = item.getString(TAG_NAME);
//                        age = item.getString(TAG_AGE);
//                        img = item.getString(TAG_IMG);
//                        gender = item.getString(TAG_gender);
//                        school = item.getString(TAG_school);
//                        img2 = item.getString(TAG_IMG2);
//                        img3 = item.getString(TAG_IMG3);
//                        img4 = item.getString(TAG_IMG4);
//                        img5 = item.getString(TAG_IMG5);
//                        img6 = item.getString(TAG_IMG6);
//                        hobbies = item.getString(TAG_hobby);
//                        location = item.getString(TAG_location);
//                        likes = item.getString(TAG_likes);
//                        phone = item.getString(TAG_p);
//                        token = item.getString(TAG_tk);
//                        la = item.getString(TAG_lati);
//                        Long = item.getString(TAG_longit);
//                        intro = item.getString(TAG_intro);
//                        job = item.getString(TAG_job);
//                        String workPlace = item.getString(TAG_work);
//                        hideage = item.getString(TAG_hideage);
//                        unmatched = item.getString(TAG_unmatched);
//                        numOfmatch = item.getString(TAG_numOfmatch);
//
//                        likes = likes.replace(" ", "");
//                        somelist = likes.split(",");
//
//
//                        if(mylike.size() > 0) {
//
//                            somelikeme = findmylove(phone);
//
//                            int mynum = Integer.parseInt(myphone.replace("+8210", "1"));
//                            int somenum = Integer.parseInt(phone.replace("+8210", "1"));
//                            String loc = Integer.toString(mynum+somenum);
//                            RoomNums.add(loc);
//                            for (int k = 0; k <  keyAndday.size(); k++){
//                                if(keyAndday.get(k).getKey().equals(loc)){
//                                    CreatedDay = keyAndday.get(k).getCday();
//                                    NEWmsg = keyAndday.get(k).getMsg();
//
//                                    /// 이미지 메세지일시 "이미지"로 최신메세지 받을수 있도록 걸러낸다.
//                                    if(NEWmsg.startsWith("uploads/")){
//                                        NEWmsg = "이미지 전송";
//                                        Log.d(TAG, "새로운 메시지| "+NEWmsg);
//                                    }
//
//                                }
//                            }
//
////////////////////////////////////////////////// 아래 2개의 리사이클러뷰 들어갈 유저 데이터 걸러내기 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                            Log.e(TAG, "Finaltargetnums사이즈= " + Finaltargetnums.size());
//
//                            for (int z = 0; z < somelist.length; z++) {
//
//                                if (somelist[z].equals(myphone)) {
//
//
//                                    if (somelikeme != null) {
//                                        Log.e(TAG, "somelikeme 오브젝트= " + name + "|| " + phone);
//                                        Log.e(TAG, "MdateAndRoom.size() = " + MdateAndRoom.size());
//
//                                        if(MdateAndRoom.size() > 0) {
//                                            //if(MdateAndRoom.size() > 0) 채팅리사이크러뷰 아이템 페이징시 상단의 매칭 리사이클러뷰 아이템 중복 불러오기 막기
//                                            for (int m = 0; m < MdateAndRoom.size(); m++) {
//                                                if (MdateAndRoom.get(m).getRoom().equals(loc)) {
//                                                    Mdate = MdateAndRoom.get(m).getMday();
//                                                    Log.e(TAG, "매치생성날짜= " + Mdate + " |유저= " + name + " 직업 = " + job);
//
//                                                    items.add(new itemMoFormsg(phone, img, gender, school, intro, job, name, age, location, hobbies, token, distance + " km", img2, img3, img4, img5, img6, somelist, unmatched, Mdate, "", NEWmsg, loc, 24, workPlace));
//                                                    Log.e(TAG, "items 넣은 폰= " + items.size());
//
//                                                }//..if
//                                            }//..  for (int m = 0; m <  MdateAndRoom.size(); m++)
//                                        }//..if(MdayeAnd)
//                                        /////////////// 매치된유저리스트용 데이터 걸러내기끝  coupleAdapter에 파라미터로들어간다.////////////////////////////////////////
//
////////////////////////////////////////////////// 아래 메세지 리스트(ChatAdapter) 리사이클러뷰 들어갈 유저 데이터 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                        ///////////////////새로운 메세지를 받았는지 확인후  ihaveMsg = "yes" 인지 "no"인지 확인후 어레이 리스트에 포함한다.///////////////////
//                                        //////////////////1."no"=> 메시지 리스트에서 보이지않음 "yes"=> 메세지 리스트에서 볼수있다. 2.메세지를 받은유저를 우선으로 리스트상단에 위치시킨다.//////////
//
//
//
//                                    }
//
//                                }
//                            }
//
//                            CreatedDay = "";
//                            NEWmsg = "";
//
//                        }//..,Likes if문
//
//                        //
//
//                        for (int z = 0; z < somelist.length; z++) {
//                            if (somelist[z].equals(myphone)) {
//                                numOflikes.add(phone);
//                            }
//                        }
//
//                    } // for.....
//
//                    /// 라이크 갯수 구하기 코드/////////////
//                    String mylikes = auto.getString("likes", null);
//                    mylikes = mylikes.replace(" ", "");
//                    mlike = mylikes.split(",");
//
//
//                    Log.e(TAG, "mlike.length "+mlike.length+"| numOflikes.size() ="+numOflikes.size());
//
//
//                    for (int l = 0; l < numOflikes.size(); l++)  {
//                        String numlikes= findmylove(numOflikes.get(l));
//                        if(numlikes == null){
//                            nums ++;
//                        }
//                    }
//
//
//                    ///////////// 라이크 갯수 구하기 코드/////////////
//
//                    Log.e(TAG, "items.size= "+items.size());
//
//
//                    ////
//                    Log.e(TAG, "라이크갯수= " + nums);
//
//                    Collections.sort(items, new SortByDate());
//                    for (int p = 0; p < items.size(); p++){
//                        Log.e(TAG, "items Object after sorting is : " + items.get(p).getName()+"| "+items.get(p).getCday()+"| "+items.get(p).getGender());
//                    }
//
//
//
//                    String n = String.valueOf(nums);
//                    NumOflike.setText( n+" likes");
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                    coupleAdapter = new coupleAdapter(items,Matched_Activity.this,myphone);
//                    recyclermatch.setLayoutManager(layoutManager);
//                    recyclermatch.setAdapter(coupleAdapter);
//                    //간격벌리기 숫자 클수록 더벌어진다
////                    recyclermatch.addItemDecoration(new RecyclerViewDecoration(20));
//
//                    if(page > 0 && MdateAndRoom.size() > 0){
//
//                        recyclermatch.scrollToPosition(coupleAdapter.getItemCount() - 1);
//
//                        for (int p = 0; p < items.size(); p++){
//                            Log.e(TAG, "items Object : " + items.get(p).getName());
//                        }
//                    }
//
//                    if(Finaltargetnums.size() == msgItems.size()){
//                        nomatch.setVisibility(View.GONE);
////                        allchat.setVisibility(View.VISIBLE);
//                        Controllfilter = true;
//                    }
//                    else
//                    {
//                        Controllfilter = false;
//
//                    }
//                    if(items.size() == 0 ){
//                        allchat.setVisibility(View.GONE);
//                        nomatch.setVisibility(View.VISIBLE);
//
//                    }
//                    LinearLayoutManager chatlayoutManager = new LinearLayoutManager(context);
//                    chatlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    ChatAdapter = new couplechatAdapter( msgItems,Matched_Activity.this,myphone,items.size());
//                    recyclerChat.setLayoutManager(chatlayoutManager);
//                    recyclerChat.setAdapter(ChatAdapter);
//
//                    if(Finaltargetnums.size()>1){
//                        String NumOfmatch = "현재 매치중 검색";
//                        searchMatch.setHint(NumOfmatch);
//                    }else {
//                        String NumOfmatch = "현재 매치중 검색";
//                        searchMatch.setHint(NumOfmatch);
//                    }
//
//                    if(pageInchat > 0){
//
//                        recyclerChat.scrollToPosition(ChatAdapter.getItemCount() - 1);
//
//                        for (int p = 0; p <  msgItems.size(); p++){
//                            Log.e(TAG, " msgItems Object : " +  msgItems.get(p).getName());
//                        }
//                    }
//
//                    SharedPreferences.Editor saveRoomnums = auto.edit();
//
//                    saveRoomnums.remove("RoomNums");
//                    saveRoomnums.commit();
//
//                    saveRoomnums.putString("RoomNums", String.valueOf(RoomNums).trim());
//                    saveRoomnums.apply();
//                    Log.d(TAG, "내방번호들 = "+ String.valueOf(RoomNums).trim());
//
//                    MdateAndRoom = new ArrayList<>();
//                    Finaltargetnums = new ArrayList<>();
//                    //MdateAndRoom, Finaltargetnums 어레이 비우고 채팅리사이크러뷰 아이템 페이징시 상단의 매칭 리사이클러뷰 아이템 중복 불러오기 막기
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                catch (NullPointerException e1) {
//                    e1.printStackTrace();
//                }
//
//            }
//
//        };
//        loadInfo add = new loadInfo(responseListener);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(add);
//
//    }
///////////////////////////////////////////////////////안씀

    /////////////////////////////////////////////////////////////////////////////
    public void loadmamberInfo() {
        String TAG_JSON = "webnautes";
        String TAG_NAME = "name";
        String TAG_p = "phone";
        String TAG_AGE = "age";
        String TAG_IMG = "img";
        String TAG_gender = "gender";
        String TAG_school = "school";
        String TAG_intro = "intro";
        String TAG_job = "job";
        String TAG_work = "work";
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
        String TAG_numOfmatch = "numOfmatch";

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);

        progressBarUp.setVisibility(View.INVISIBLE);
        numOflikes = new ArrayList<>();
        nums = 0;

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "MatchedA 어레이갯수: =" + jsonArray.length() + "개");

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
                        String workPlace = item.getString(TAG_work);
                        hideage = item.getString(TAG_hideage);
                        unmatched = item.getString(TAG_unmatched);
                        numOfmatch = item.getString(TAG_numOfmatch);

                        likes = likes.replace(" ", "");
                        somelist = likes.split(",");


//                        if (mylike.size() > 0) {

                            somelikeme = findmylove(phone);
                            disLikeme = findDislike(phone);
                            int mynum = Integer.parseInt(myphone.replace("+8210", "1"));
                            int somenum = Integer.parseInt(phone.replace("+8210", "1"));
                            String loc = Integer.toString(mynum + somenum);
                            RoomNums.add(loc);
                            for (int k = 0; k < keyAndday.size(); k++) {
                                if (keyAndday.get(k).getKey().equals(loc)) {
                                    CreatedDay = keyAndday.get(k).getCday();
                                    NEWmsg = keyAndday.get(k).getMsg();

                                    /// 이미지 메세지일시 "이미지"로 최신메세지 받을수 있도록 걸러낸다.
                                    if (NEWmsg.startsWith("uploads/")) {
                                        NEWmsg = "이미지 전송";
                                        Log.d(TAG, "새로운 메시지| " + NEWmsg);
                                    }
                                   else if (NEWmsg.startsWith("https://media.tenor.com"))
                                    {
                                        NEWmsg = "GIF";
                                        Log.d(TAG, "새로운 메시지| " + NEWmsg);
                                    }

                                }
                            }
                             Log.d(TAG, "새로운 메시지| ");

//////////////////////////////////////////////// 아래 2개의 리사이클러뷰 들어갈 유저 데이터 걸러내기 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            Log.e(TAG, "Finaltargetnums사이즈= " + Finaltargetnums.size());

                            for (int z = 0; z < somelist.length; z++) {

                                if (somelist[z].equals(myphone)) {


                                    if (somelikeme != null) {
                                        Log.e(TAG, "somelikeme 오브젝트= " + name + "|| " + phone);
                                        Log.e(TAG, "MdateAndRoom.size() = " + MdateAndRoom.size());


                                            if (MdateAndRoom.size() > 0) {
                                                //if(MdateAndRoom.size() > 0) 채팅리사이크러뷰 아이템 페이징시 상단의 매칭 리사이클러뷰 아이템 중복 불러오기 막기
                                                for (int m = 0; m < MdateAndRoom.size(); m++) {
                                                    if (MdateAndRoom.get(m).getRoom().equals(loc)) {
                                                        Mdate = MdateAndRoom.get(m).getMday();
                                                        Log.e(TAG, "매치생성날짜= " + Mdate + " |유저= " + name + " 직업 = " + job);

                                                        items.add(new itemMoFormsg(phone, img, gender, school, intro, job, name, age, location, hobbies, token, distance + " km", img2, img3, img4, img5, img6, somelist, unmatched, Mdate, "", NEWmsg, loc, 24, workPlace));
                                                        Log.e(TAG, "items 넣은 폰= " + items.size());

                                                    }//..if
                                                }//..  for (int m = 0; m <  MdateAndRoom.size(); m++)
                                            }//..if(MdayeAnd)


                                        /////////////// 매치된유저리스트용 데이터 걸러내기끝  coupleAdapter에 파라미터로들어간다.////////////////////////////////////////

//////////////////////////////////////////////// 아래 메세지 리스트(ChatAdapter) 리사이클러뷰 들어갈 유저 데이터 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        ///////////////////새로운 메세지를 받았는지 확인후  ihaveMsg = "yes" 인지 "no"인지 확인후 어레이 리스트에 포함한다.///////////////////
                                        //////////////////1."no"=> 메시지 리스트에서 보이지않음 "yes"=> 메세지 리스트에서 볼수있다. 2.메세지를 받은유저를 우선으로 리스트상단에 위치시킨다.//////////
                                        if (Finaltargetnums.size() > 0) {

                                            for (int a = 0; a < Finaltargetnums.size(); a++) {
                                                if (Finaltargetnums.get(a).equals(phone)) {
                                                    Log.e(TAG, "ihaveMsg = " + Finaltargetnums.get(a));
                                                    Log.e(TAG, "ihaveMsg" + "= yes |" + phone);
//                                                    numMsgdate ++;
                                                    ihaveMsg = "yes";
                                                    msgItems.add(new itemMoFormsg(phone, img, gender, school, intro, job, name, age, location, hobbies, token, distance + " km", img2, img3, img4, img5, img6, somelist, "", CreatedDay, NEWmsg, ihaveMsg, loc, 24, workPlace));
                                                    // 새로운 메세지 유무확인 데이터 추가한다음 리셋
                                                    Log.e(TAG, "msgItems 오브젝트= " + name + "|| " + phone);


                                                } else {
                                                    Log.e(TAG, "ihaveMsg = " + Finaltargetnums.get(a));
                                                    Log.e(TAG, "ihaveMsg" + "= no |" + phone);

                                                }
                                            }
                                        }



                                    } //...somelikeme != null

                                    if(somelikeme != null || disLikeme != null){

                                    }
                                    else {
                                        toSeelikesitems.add(new itemMoFormsg(phone, img, gender, school, intro, job, name, age, location, hobbies, token, distance + " km", img2, img3, img4, img5, img6, somelist, unmatched, Mdate, "", NEWmsg, loc, 24, workPlace));
                                    }

                                } //..if (somelist[z].equals(myphone)) {

                            }

                            CreatedDay = "";
                            NEWmsg = "";

                            for (int z = 0; z < somelist.length; z++) {
                                if (somelist[z].equals(myphone)) {
                                    Log.e(TAG, "somelist[z] " + somelist[z]);

                                    numOflikes.add(phone);
                                }
                            }
//                        }//..,Likes if문

                    } // for.....

                    String numoflikes = toSeelikesitems.size() + " likes";
                    NumOflike.setText(numoflikes);

                    for (int a = 0; a < Finaltargetnums.size(); a++) {
                        Log.d(TAG, "Finaltargetnums 메시지번호 =" + Finaltargetnums.get(a) );

                    }
                    /// 라이크 갯수 구하기 코드/////////////
                    String mylikes = auto.getString("likes", null);
                    mylikes = mylikes.replace(" ", "");
                    mlike = mylikes.split(",");


                    Log.e(TAG, "mlike.length " + mlike.length + "| numOflikes.size() =" + numOflikes.size());


                    for (int l = 0; l < numOflikes.size(); l++) {
                        Log.e(TAG, "numOflikes.get(l) =" +numOflikes.get(l));
                        String numlikes = findmylove(numOflikes.get(l));
                        Log.e(TAG, "numlikes =" +numlikes);
                        if (numlikes == null) {

                            nums++;
                        }
                    }


                    for (int o = 0; o < msgItems.size(); o++) {
                        if (msgItems.get(o).getHaveMsg().equals("no") || msgItems.get(o).getCday().equals("")) {
                            Log.e(TAG, "채팅시작못한자 msgItems| " + msgItems.get(o).getName());
                            msgItems.remove(o);

                        }
                    }

                    ///////////// 라이크 갯수 구하기 코드/////////////

//                    if (Finaltargetnums.size() > 0) {
//                        if (items.size() == msgItems.size()) {
//                            Log.e(TAG, "items clear");
//
//                            items.clear();
//                        }
//                    }//..if


//                    if (Finaltargetnums.size() > 0) {
//                        for (int a = 0; a < Finaltargetnums.size(); a++) {
//
//                            for (int i = 0; i < items.size(); i++) {
//                                if (Finaltargetnums.get(a).equals(items.get(i).getPhone())) {
//                                    items.remove(i);
//                                    Log.e(TAG, "채팅시작못한자 items 제거 | " + items.get(i).getName());
//
//                                }
//                            }
//                        }
//                    }

                    if (Finaltargetnums.size() == 0) {
                        msgItems.clear();
                    }
          //          if(mdislike[0].startsWith("+")){
            //            disNums = mdislike.length;
            //        }
                    Log.d(TAG, "msgItems.size= " + msgItems.size());
                    Log.d(TAG, "items.size= " + items.size());
                    Log.d(TAG, "Finaltargetnums.size= " + Finaltargetnums.size());


                    ////
                    Log.d(TAG, "라이크갯수= " + nums);
                    Log.d(TAG, "toSeelikesitems.size() 라이크갯수= " + toSeelikesitems.size());

                    Collections.sort(items, new SortByDate());
//                    Collections.reverse(items);
                    for (int p = 0; p < items.size(); p++) {
                        Log.e(TAG, "items Object after sorting is : " + items.get(p).getName() + "| " + items.get(p).getCday() + "| " + items.get(p).getGender());
                    }

                    Collections.sort(msgItems, new SortByDate());
                    Collections.reverse(msgItems);
                    for (int p = 0; p < msgItems.size(); p++) {
                        Log.e(TAG, "The Object after sorting is : " + msgItems.get(p).getName() + "| " + msgItems.get(p).getCday());
                    }


//                    if(nums > 0){
 //                       int numberOflikes = toSeelikesitems.size() ;
  //                      if(disNums > numberOflikes){
  //                          NumOflike.setText(numberOflikes  + " likes");
    //                    }else {
      //                      int total = numberOflikes - disNums;
       //                     NumOflike.setText(total  + " likes");
             //           }

            //        }
            //        else {
              //          NumOflike.setText(nums + " likes");
                //    }


                    if (matchitemsOn)
                    {

                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    coupleAdapter = new coupleAdapter(items, Matched_Activity.this, myphone);
                    recyclermatch.setLayoutManager(layoutManager);
                    recyclermatch.setAdapter(coupleAdapter);
                    //간격벌리기 숫자 클수록 더벌어진다
//                    recyclermatch.addItemDecoration(new RecyclerViewDecoration(20));

                    if (page > 0 && MdateAndRoom.size() > 0) {

                        recyclermatch.scrollToPosition(coupleAdapter.getItemCount() - 1);

                        for (int p = 0; p < items.size(); p++) {
                            Log.e(TAG, "items Object : " + items.get(p).getName());
                        }
                    }

                    } //.matchitemsOn=true

                    if (Finaltargetnums.size() == msgItems.size()) {
                        nomatch.setVisibility(View.GONE);
//                        allchat.setVisibility(View.VISIBLE);
                        Controllfilter = true;
                    } else {
                        Controllfilter = false;

                    }
                    if (items.size() == 0) {
                        allchat.setVisibility(View.GONE);
                        nomatch.setVisibility(View.VISIBLE);

                    }

                    if (chatRecitemsOn)
                    {

                    LinearLayoutManager chatlayoutManager = new LinearLayoutManager(context);
                    chatlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    ChatAdapter = new couplechatAdapter(msgItems, Matched_Activity.this, myphone, items.size());
                    recyclerChat.setLayoutManager(chatlayoutManager);
                    recyclerChat.setAdapter(ChatAdapter);

//                        int matchNum = ChatUsernums.size()+MdateAndRoom.size();
                        String NumOfmatch = "새로운 매치 검색";
                        searchMatch.setHint(NumOfmatch);


                    if (pageInchat > 0) {

                        recyclerChat.scrollToPosition(ChatAdapter.getItemCount() - 1);

                        for (int p = 0; p < msgItems.size(); p++) {
                            Log.e(TAG, " msgItems Object : " + msgItems.get(p).getName());
                        }
                    }

                  }//..chatRecitemsOn
                    SharedPreferences.Editor saveRoomnums = auto.edit();

                    saveRoomnums.remove("RoomNums");
                    saveRoomnums.commit();

                    saveRoomnums.putString("RoomNums", String.valueOf(RoomNums).trim());
                    saveRoomnums.apply();
                    Log.d(TAG, "내방번호들 = "+ String.valueOf(RoomNums).trim());

                    if(chatok){

                        recyclerChat.scrollToPosition(ChatAdapter.getItemCount() - 2);

                    }

                    MdateAndRoom = new ArrayList<>();
                    Finaltargetnums = new ArrayList<>();
//                    msgItems = new ArrayList<>();
                    matchitemsOn = false;
                    chatRecitemsOn = false;
                    chatok = false;
                    //MdateAndRoom, Finaltargetnums 어레이 비우고 채팅리사이크러뷰 아이템 페이징시 상단의 매칭 리사이클러뷰 아이템 중복 불러오기 막기
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e1) {
                    e1.printStackTrace();
                }

            }

        };
        loadInfo add = new loadInfo(responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }
//////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////

    static class SortByDate implements Comparator<itemMoFormsg> {

        @Override
        public int compare(itemMoFormsg o1, itemMoFormsg o2) {
            return o1.getCday().compareTo(o2.getCday());
        }
    }

    private void getLikelist(String phone) {
        mylike = new ArrayList<>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {
//                        Toast.makeText(Matched_Activity.this, "겟라이크 리스트: "+success, Toast.LENGTH_SHORT).show();
                        Likelist = success;
                        Likelist = Likelist.replace(" ", "");
                        String[] templist = Likelist.split(",");

                        for (int i = 0; i < templist.length; i++){
                                    mylike.add(templist[i]);
                        }
                        Log.d(TAG, "라이크 받기 성공= "+ success);
                        loadmamberInfo();

                    }else{
                        Log.d(TAG, "라이크 받기 실패 = "+ success);
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

                        // 싫어요 표시번호들로 리스트만들기
                        Dislikes = Dislikes.replace(" ", "");
                      //  mdislike = Dislikes.split(",");

                        String[] templist = Dislikes.split(",");

                        mdislike.addAll(Arrays.asList(templist));

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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class MyReceiver extends BroadcastReceiver {
        Context co;
        String ThisOneGetDeleted = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {

//                ACTION_TIME_CHANGED는 시스템의 시간 또는 날짜가 변경될 때 전달됩니다.
//                사용자가 직접 Settings 앱에서 날짜와 시간을 변경해도 이 인텐트가 전달됩니다.
//                여기서 시간, 날짜 변경의 의미는 시간이 지나서 변경된 것이 아니라, 시스템 또는 사용자에 의해서 시간, 날짜 설정이 변경되었다는 것을 의미합니다.
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                Date currDate = null;
                String DateAfter1h = null;

                try {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

//                             현재 시스템 시간 구하기
                    long systemTime = System.currentTimeMillis();
//                             출력 형태를 위한 formmater
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
//
//                               format에 맞게 출력하기 위한 문자열 변환
                    String currTime = formatter.format(systemTime);

                    currDate = formatter.parse(currTime);

                    for (int i = 0; i < items.size(); i++) {

                        Log.e("브로드캐스트", "해당날짜 = "+items.get(i).getCday());

                          // 매치된날짜//
                          Date matchedDate = formatter.parse(items.get(i).getCday());

                          cal.setTime(matchedDate);
//
//                        // 1일 후
                          ///////////////////// matchedDate=매치생성날짜에 plusDays(1)=>하루 더한다//////////
                          cal.add(Calendar.DATE, 1);
                          DateAfter1h = simpleDateFormat.format(cal.getTime());
//
//                        ///////1일 후 날짜//////////////////////////////////////////////////////////////////////////
                          Date MdayAfter1 = simpleDateFormat.parse(DateAfter1h);

                          long hour = (MdayAfter1.getTime() - currDate.getTime()) / (60 * 60 * 1000);
                          long min = (MdayAfter1.getTime() - currDate.getTime()) / (60 * 1000);

//                             (매칭날짜 - 종료날짜)  시간 차이나는 부분을 시, 분 으로 얻음
                          coupleAdapter.AutoMatchCancel(hour, min, i);


                    }//..for
                }//..try
                catch (ParseException e) {
                    e.printStackTrace();
                }

            }//..if


        }//..onReceiver

    }

}