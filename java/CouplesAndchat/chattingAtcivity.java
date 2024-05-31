package CouplesAndchat;


import static CouplesAndchat.Receiverr.isOnline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.honeybee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import Utills.IPadress;
import Utills.MyEditText;
import Utills.NetworkStatus;
import Utills.NotiModel;
import gun0912.tedkeyboardobserver.TedKeyboardObserver;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class chattingAtcivity extends AppCompatActivity implements View.OnClickListener, Receiverr.OnReceiveListener {
    final static int REQUEST_CAMERA_CODE= 1;
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    private String IP = ipadress.ip.IP;
    Uri im;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    HashMap<String, Object> childUpdates = null;

    private static FrameLayout fmlayout;
    private static LinearLayout layout_cancel, editLinearlayout;
    private RecyclerView mrecyclerView;
    private static Senderr sender;
    @SuppressLint("StaticFieldLeak")
    static Receiverr receiver;
    private LinearLayoutManager chatRoomlayoutManager;
    private ArrayList<chatItems> mChatDataList;
    private chatroomAdapter chatroomAdapter;

    private ArrayList<String> chatdata = new ArrayList<>();// 서버저장용
    private static final String TAG = "chatting";
    private TextView partnername,msgforman,msgforwoman;
    private MyEditText msg ;
    protected String MyMsg;
    private ImageButton fileSend;
    private  String imgPath = "";
    private String bitmap;
    public static String FirstDate;
    private boolean InKeyboard;
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 10;// 한 페이지마다 로드할 데이터 갯수.
    public String getFile = "";
    public String pnum;
    ImageView displayView;
    static ImageView loverimg;
    ImageView loverprofile;
    ImageButton back,cancel;
    static String roomnum,roomFordate;
    static String name;
    static String Name;
    String img;
    String img2;
    String img3;
    String img4;
    String img5;
    String img6;
    String downimg = "";
    String Mygender;
    String hobbies;
    String gender;
    String school;
    String age;
    static String phonewithlikes;
    static String Myphone;
    String intro;
    String job;
    String workplace;
    String city;
    String distance;
    static String token;
    static Socket socket = null;
    String Message;
    String hourandm,ChatMessage,chatroom,hourAndm;
    String message;
    Boolean isMe,imgok;
    Uri partnerImg= null,receivedImg = null;
    Boolean editopen,enter = true;
    Boolean downpic = false;
    Boolean Netiscon = true;
    private Handler mHandler;
    private static final String CHANNEL_ID3 = "matchCH";
    ProgressDialog customProgressDialog;
    public String chat;
    static String convertedDate;
    String CurrentDate;
    String today, date ;
    protected static boolean conn = false;
    Bitmap bp = null;
    String TCPmsg = null;
    int TwoIsHide = 0;
//    SoftKeyboard softKeyboard;
    ConstraintLayout ll;
    static BroadcastReceiver Mreceiver;
    IntentFilter filter = new IntentFilter();

    Bitmap BP = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_atcivity);
        receivedImg = null;
        mChatDataList = new ArrayList<>();
        Log.e(TAG, "OnCreate");

     //   ShareCompat.IntentBuilder.from(chattingAtcivity.this)
     //           .setStream(uri) // uri from FileProvider
      //          .setType("text/html")
     //           .getIntent()
    //            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);


////////////////////////////////// 소프트키보드가 에딧텍스트를 겹치게 하지안게한다///////////////////
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////////////////////////////////// 소프트키보드가 에딧텍스트를 겹치게 하지안게한다///////////////////

///////////////////////////////////////////날짜구하기///////////////////////////////////////

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        Date date = new Date();
        convertedDate = simpleDateFormat.format(date);

        Calendar time = Calendar.getInstance();
        int isAMorPM = time.get(Calendar.AM_PM);
        int hour = time.get(Calendar.HOUR);

        if(hour > 12){
            hour = hour-12;
        }
        switch(isAMorPM) {
            case Calendar.AM:
                hourAndm = "오전 "+hour+"시 "+ time.get(Calendar.MINUTE)+"분";

                break;
            case Calendar.PM:

                hourAndm = "오후 "+hour+"시 "+ time.get(Calendar.MINUTE)+"분";

                break;
        }


///////////////////////////////////////////////////////////////////////////

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        Name = auto.getString("name", null);
        Myphone = auto.getString("phone", null);
        Mygender = auto.getString("gender", null);

        ll = (ConstraintLayout)findViewById(R.id.ll);
        fileSend = findViewById(R.id.imgbtn_file);
        msgforman = findViewById(R.id.tv_msgforman);
        msgforwoman = findViewById(R.id.tv_msgforwoman);
        fmlayout = findViewById(R.id.img_frame);
        editLinearlayout = findViewById(R.id.linearLayout2);
        layout_cancel = findViewById(R.id.layout_cancel);
        mrecyclerView = findViewById(R.id.recycler_chatroom);
//        msg = (EditText) findViewById(R.id.et_msg);
        msg = findViewById(R.id.et_msg);
        partnername = findViewById(R.id.tv_lover);
        loverimg = findViewById(R.id.img_lover);
        loverprofile = findViewById(R.id.img_go_loverprofile);
        back = findViewById(R.id.btn_back1);
        cancel = findViewById(R.id.btn_MatchingCancel);
        displayView = findViewById(R.id.display_view);

        findViewById(R.id.btn_send).setOnClickListener(this);
        ///////////////////////////////////////////////////////////////////////////////////
        msg.setKeyBoardInputCallbackListener(new MyEditText.KeyBoardInputCallbackListener() {
            @Override
            public void onCommitContent(InputContentInfoCompat inputContentInfo,
                                        int flags, Bundle opts) {
                //you will get your gif/png/jpg here in opts bundle
                Log.e(TAG, "keyboard showURI= "+inputContentInfo.getDescription());
                Uri uri = inputContentInfo.getLinkUri();

                inputContentInfo.releasePermission();
                Log.e(TAG, "keyboard BP= "+BP+" |opts ="+opts );

//                    if(BP != null) {
//                        sender.sendMessage(imgPath, true, String.valueOf(uri));
//                        bitmap = "";
//                    }


                    sender.sendMessage(String.valueOf(uri), false, bitmap);

                if (mChatDataList == null || mChatDataList.size() == 0 || !mChatDataList.get(0).getDate().equals(convertedDate)) {
                    chatItems chatting = new chatItems(convertedDate, roomnum, hourAndm, MyMsg, Myphone, BP, partnerImg, uri, true, true, false, conn);
                    mChatDataList.add(chatting);

                } else {
                    chatItems chatting = new chatItems("no", roomnum, hourAndm, MyMsg, Myphone, BP, partnerImg, uri, true, true, false, conn);
                    mChatDataList.add(chatting);
                }

                chatRoomlayoutManager = new LinearLayoutManager(chattingAtcivity.this);
                chatRoomlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                chatRoomlayoutManager.setStackFromEnd(true);
                chatroomAdapter = new chatroomAdapter(mChatDataList,Myphone,chattingAtcivity.this, name,age, city,hobbies,img,img2,img3,img4,img5,img6,intro,job,school,distance,gender,phonewithlikes,token,workplace);
                mrecyclerView.setLayoutManager(chatRoomlayoutManager);
                mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount() - 1);
                receivedImg = null;
            }
        });


        //////////////////////////////////////////////////키보드 동작감지 코드///////////////////////////////////////////////
        ////////////// 키보드 보일때 콜백함수 작동이 느려서 키보드보여주기 감지는 라이브러리로대체//////////////
        new TedKeyboardObserver(this) .listen(isShow -> {
            // do something });
            TwoIsHide += 1;
            Log.e(TAG, "keyboard show");
            chatRoomlayoutManager.setStackFromEnd(true);
            mrecyclerView.setLayoutManager(chatRoomlayoutManager);
            mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount()-1);
            InKeyboard = true;
            if(TwoIsHide == 2)
            {
                TwoIsHide = 0;
//                Log.e(TAG, "keyboard hide = "+TwoIsHide);
//                        chatRoomlayoutManager.setStackFromEnd(false);
//                        mrecyclerView.setLayoutManager(chatRoomlayoutManager);
//                        mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount()-1);
//                        InKeyboard = false;
            }
        });

       //////////////////////////////////////////////////키보드 동작감지 코드///////////////////////////////////////////////
        getintent();


        int mynum = Integer.parseInt(Myphone.replace("+8210", "1"));
        int somenum = Integer.parseInt(phonewithlikes.replace("+8210", "1"));
        roomnum = String.valueOf(mynum+somenum);

        int mynum2 = Integer.parseInt(Myphone.replace("+8210", "2"));
        int somenum2 = Integer.parseInt(phonewithlikes.replace("+8210", "2"));
        roomFordate= String.valueOf(mynum2+somenum2);
        //내 성별이 남성이면 에딧텍스트를 가리고 상대방여성이 메세지를 보냈을때 채팅이 가능해진다.
        if(Mygender.equals("0") || editopen){
            editLinearlayout.setVisibility(View.VISIBLE);

        }
        if(Mygender.equals("1")){
            msgforman.setVisibility(View.VISIBLE);
        }else{
            msgforwoman.setVisibility(View.VISIBLE);
        }


        im = Uri.parse(IP_ADDRESS + img);

        Glide.with(this).load(im).into(loverimg);

        Glide.with(this).load(im).into(loverprofile);



        partnername.setText(name);
        fmlayout.setVisibility(View.VISIBLE);
        mrecyclerView.setVisibility(View.INVISIBLE);



        database = FirebaseDatabase.getInstance();
//        offchat offchat = new offchat("enter");
//        ref.child(roomnum+"/취소/"+Myphone).setValue(offchat);
////////////////파이어베이스 최신 메세지를받아 상대방이 채팅방에 입장했는지 확인 메시지fcm을 보내지않는다////////////
        ref.child(roomnum + "/"+phonewithlikes).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{

                String value = snapshot.child("onOroff").getValue(String.class);

                if(value.equals("on")){
                    enter = false;
                }

                if(value.equals("off")){
                    enter = true;
                }
                }catch (NullPointerException e){
                   e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("실시간메세지", "실시간 메세지 cancel");

            }
        });

////////////////파이어베이스 최신 메세지를받아 여성이 대회를 시작하면 남성의 에딧텍스트가열린다.////////////
        ref.child(roomnum+"/최신메세지").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.child("msg").getValue(String.class);

                if(!(value == null)){
                    editLinearlayout.setVisibility(View.VISIBLE);
                    msgforman.setVisibility(View.GONE);
                    loverimg.setVisibility(View.GONE);
                    mrecyclerView.setVisibility(View.VISIBLE);


                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference(roomFordate);
                    ref.removeValue();
/////////////////test 이미지 전송관련
//                    RemoveMday(roomnum);
                    if(value.equals("매칭이 취소됬습니다..")){

                        Intent out = new Intent(chattingAtcivity.this, Matched_Activity.class);
                        out.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(out);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("실시간메세지", "실시간 메세지 cancel");

            }
        });

///////////////////////////////////////////////////////
        ////////////////파이어베이스 최신 메세지를받아 여성이 대회를 시작하면 남성의 에딧텍스트가열린다.////////////

        ref.child(roomnum +"/최신메세지상태").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.child("msgState").getValue(String.class);

                if (value != null) {
                    String[] vArr = value.split("[|]");
                    String msgCheck = vArr[1];
                    pnum = vArr[0];


                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatRoomlayoutManager = new LinearLayoutManager(chattingAtcivity.this);
        chatRoomlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatRoomlayoutManager.setStackFromEnd(true);
        chatroomAdapter = new chatroomAdapter(mChatDataList,Myphone,chattingAtcivity.this, name,age, city,hobbies,img,img2,img3,img4,img5,img6,intro,job,school,distance,gender,phonewithlikes,token,workplace);
        mrecyclerView.setLayoutManager(chatRoomlayoutManager);
        mrecyclerView.setAdapter(chatroomAdapter);
        fmlayout.setVisibility(View.VISIBLE);
        loverimg.setVisibility(View.VISIBLE);


        //와이파이 상태변화 수신
        Mreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(intent.getAction()){
                    //와이파이 상태변화
                    case WifiManager.WIFI_STATE_CHANGED_ACTION:
                        //와이파이 상태값 가져오기
                        int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                        switch(wifistate){
                            case WifiManager.WIFI_STATE_DISABLING: //와이파이 비활성화중
                                Log.e(TAG, "와이파이 비활성화중");

                                //ㄴㄴ
                                break;
                            case WifiManager.WIFI_STATE_DISABLED:  //와이파이 비활성화
                                Log.e(TAG, "와이파이 비활성화");

                                break;
                            case WifiManager.WIFI_STATE_ENABLING:  //와이파이 활성화중
                                Log.e(TAG, "와이파이 활성화중");

                                break;
                            case WifiManager.WIFI_STATE_ENABLED:   //와이파이 활성화
                                Log.e(TAG, "와이파이 활성화");
                                Netiscon = true;
                                break;
                            default:
                                Log.e(TAG, "알수없음");

                                break;
                        }
                        break;

                    //네트워크 상태변화
                    case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                        NetworkInfo info = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                        //네트워크 상태값 가져오기
                        NetworkInfo.DetailedState state = info.getDetailedState();

                        String typename = info.getTypeName();
                        if(state==NetworkInfo.DetailedState.CONNECTED){ //네트워크 연결

//                            Log.e(TAG, "넷연결");
//                            Log.e(TAG, "NET connected: " + isOnline());
//                            Log.e(TAG, "sender NET connected: " + isOnline);
//                            getStatusOfNet();

                                SharedPreferences getChat = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                String chatData = getChat.getString(roomnum, null);
                                if (chatData != null) {

//                                    Handler hd = new Handler();
//                                    hd.postDelayed(new GetWifiOn(), 200);

                            }

                        }
                        else if(state==NetworkInfo.DetailedState.DISCONNECTED){ //네트워크 끊음
                            Log.e(TAG, "넷 끈음");
//                            ThreadStart();
//                            Toast.makeText(getApplicationContext(), "네트워크 끊어짐.", Toast.LENGTH_SHORT).show();
//                            //getStatusOfNet();
                        }
                        break;
                }
            }
        };


        registerReceiver(Mreceiver,filter);

///////////////////////////////////////////////////////////////////////////////////
        ////////////////채팅 로드
      loadchat(roomnum);
        //////////////////

        loverprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent love = new Intent(chattingAtcivity.this, SeePartnerInfo.class);
                love.putExtra("name",name);
                love.putExtra("age",age);
                love.putExtra("city",city);
                love.putExtra("hobbies",hobbies);
                love.putExtra("img1",img);
                love.putExtra("img2",img2);
                love.putExtra("img3",img3);
                love.putExtra("img4",img4);
                love.putExtra("img5",img5);
                love.putExtra("img6",img6);
                love.putExtra("intro",intro);
                love.putExtra("job",job);
                love.putExtra("school",school);
                love.putExtra("distance",distance);
                love.putExtra("gender",gender);
                love.putExtra("phone",phonewithlikes);
                love.putExtra("token",token);
                love.putExtra("work",workplace);
                love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(love);

            }
        });

////////////////////////////////////////////////////////////////////////////////////
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //로딩창 객체 생성
                customProgressDialog = new ProgressDialog(chattingAtcivity.this);

                customProgressDialog.setTitle("잠시만 기다려주세요..");
//                sender.sendMessage("escape//*"+roomnum);
                 if(Message != null) {

//                     String[] split = Message.split("[|]");
//
//                     String MyNum = split[1]; //
//                     String m = split[2];
                     if (!pnum.equals(Myphone)) {

                         database = FirebaseDatabase.getInstance();
                         ref = database.getReference();

                         chackNewmsg newMsg = new chackNewmsg(pnum+"|red");
                         ref.child(roomnum + "/최신메세지상태").setValue(newMsg);

//////////////////////////////////////////////////////////////////////////////////////////////

                     }

                 }

                Intent out = new Intent(chattingAtcivity.this, Matched_Activity.class);
                out.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                out.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(out);
                finish();
                overridePendingTransition(R.anim.anim_left_enter, R.anim.slide_left_exit);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog();
            }
        });



        fileSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialogForfile();
            }
        });




        if(downpic){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                sender.sendMessage(downimg);
                Log.e(TAG, "다운이미지 시작");

            }

        }

        ThreadStart();
    } //onCreate


    public void ThreadStart() {
        new Thread() {
            public void run() {
                try {
                    String ServerIP = IP;
                    socket = new Socket(ServerIP, 8081); //소켓 객체 생성
                    Log.e("응답", "채팅방서버와 연결이 되었습니다......");
                    conn = true;
                    isOnline = true;
                    /////////////////////////////////////////////////////////////

                    sender = new Senderr(socket, Myphone, phonewithlikes,token,Name,convertedDate);
                    receiver = new Receiverr(socket, Myphone,phonewithlikes,name,token);

                    sender.start(); //스레드 시동
                    receiver.start(); //스레드 시동

                    Receiverr.setOnReceiveListener(chattingAtcivity.this);
                } catch (Exception e) {
                    System.out.println("예외[MultiClient class]:" + e);
                }
            }

        }.start();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class sendcancelFCM implements Runnable {
        public void run() {

//            Toast.makeText(chattingAtcivity.this, "매치취소", Toast.LENGTH_LONG).show();
//            editLinearlayout.setVisibility(View.GONE);
            CancelMatch(Myphone,phonewithlikes);

        }
    }

    class GetWifiOn implements Runnable {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void run() {
            SharedPreferences getChat = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
            String chatData = getChat.getString(roomnum,null);
            String cD = chatData.replace("[", " ").trim();
            String CD = cD.replace("]", " ").trim();
            String[] chatdata = CD.split(",");

            if (chatdata.length > 0) {
//                                ResendM(TCPmsg);
//                                ResendM(TCPmsg);


                String Tcpmsg = "WIFI," + CD;
                Log.e(TAG, "Tcpmsg = "+Tcpmsg);

                sender.sendMessage(Tcpmsg, false, bitmap);


                if (enter) {
                    sendFCM(token, Name, "", Myphone);
                }

                SharedPreferences.Editor saveSettings = getChat.edit();
                saveSettings.remove(roomnum);
                saveSettings.apply();
            }
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    //딜레이 후 시작할 코드 작성
                    onStart();
                }
            }, 600);// 0.4초 정도 딜레이를 준 후 시작

        }
    }


    private void makeDialogForfile(){

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(chattingAtcivity.this,R.style.AlertDialogTheme);

        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.ic_baseline_smartphone_24).setCancelable(

                false).setPositiveButton("취소",

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 취소 선택");
                        // 취소 클릭. dialog 닫기.

                        dialog.cancel();


                    }

                }).setNeutralButton("앨범선택",

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int id) {

                        Log.v("알림", "다이얼로그 > 앨범선택 선택");

                        //앨범에서 선택

                        AddImage();

                    }

                }).setNegativeButton("사진촬영",

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        Log.v("알림", "다이얼로그 > 사진촬영 선택");

                        // 사진 촬영 클릭
                        takePhoto();
                    }

                });

        AlertDialog alert = alt_bld.create();

        alert.show();

    }

    //사진 찍기 클릭

    public void takePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);

    }

   private void AddImage(){

        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,100);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100) {

            // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String pathImg = "uploads/"+timeStamp+Name+"_"+roomnum+".jpg";
                imgPath = pathImg.replace(" ", "");
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {

                        bp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        bitmap = imageToString(bp);
                        ImageToDB(imgPath,bitmap);
                        //send 메시지는 주소만
                        if(bp != null) {
                            sender.sendMessage(imgPath, true, bitmap);
                        }

                        if(enter) {
                            sendFCM(token, Name, imgPath, Myphone);
                        }
                        msgforwoman.setVisibility(View.GONE);

                    } else {
                        bp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        bitmap = imageToString(bp);
                        // 서버에다 png파일 업로드 한다.
                        ImageToDB(imgPath,bitmap);
                        if(bp != null) {
                            sender.sendMessage(imgPath, true, bitmap);
                        }

                        ////////////////////////////////////////////////////
                        if(enter) {
                            sendFCM(token, Name, imgPath, Myphone);
                        }
                        msgforwoman.setVisibility(View.GONE);

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

         if (requestCode == REQUEST_CAMERA_CODE) {
            bp = (Bitmap) intent.getExtras().get("data");

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            // 이미지 경로(uri)=imgpath 를 임의로만들어 불러올경로를 서버에 저장한다
             String pathImg = "uploads/"+timeStamp+"_"+roomnum+"_pic.png";
             imgPath = pathImg.replace(" ", "");
            bitmap = imageToString(bp);

             msgforwoman.setVisibility(View.GONE);
             ImageToDB(imgPath,bitmap);
//             loadDay(roomnum,"이미지");
             if(enter) {
                 sendFCM(token, Name, imgPath, Myphone);
             }
             if(bp != null)
             {
                 sender.sendMessage(imgPath,true,bitmap);
             }


         }


//        Log.d(TAG, "이미지 NET connected: " + isOnline());
//        if(!isOnline)
//        {
//            conn = false;
//            hourAndm = "";
//            chatdata.add(imgPath+"|"+bitmap);
//            String ChatDt = String.valueOf(chatdata);
//            SharedPreferences set = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//            SharedPreferences.Editor saveMSG = set.edit();
//            saveMSG.putString(roomnum, ChatDt);
//            saveMSG.apply();
//        }
         if(!imgPath.equals(""))
         {
             // 비트맵 into 어뎁
             if (mChatDataList == null || mChatDataList.size() == 0 || !mChatDataList.get(0).getDate().equals(convertedDate)) {
                 chatItems chatting = new chatItems(convertedDate, roomnum, hourAndm, MyMsg, Myphone, bp, partnerImg, receivedImg, true, true, false, conn);
                 mChatDataList.add(chatting);

             } else {
                 chatItems chatting = new chatItems("no", roomnum, hourAndm, MyMsg, Myphone, bp, partnerImg, receivedImg, true, true, false, conn);
                 mChatDataList.add(chatting);
             }
             chatRoomlayoutManager = new LinearLayoutManager(chattingAtcivity.this);
             chatRoomlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
             chatRoomlayoutManager.setStackFromEnd(true);
             chatroomAdapter = new chatroomAdapter(mChatDataList,Myphone,chattingAtcivity.this, name,age, city,hobbies,img,img2,img3,img4,img5,img6,intro,job,school,distance,gender,phonewithlikes,token,workplace);
             mrecyclerView.setLayoutManager(chatRoomlayoutManager);
             mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount() - 1);
             receivedImg = null;

             // imgPath 초기화 시켜서 사진 선택안했는데 산진 업로드되는 오류를 잡는다.
             imgPath = "";
         }//if..
    }//..protected void onActivityResult

    private void ImageToDB(String imgPath, String bitmap) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("myimg", imgPath)
                .addFormDataPart("file", bitmap)
                .build();

        Request request = new Request.Builder() .url(IP_ADDRESS+"/ImageSendInchat.php")// Server URL 은 본인 IP를 입력
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {

                Log.d("TEST : ", response.body().string());



            }

            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }


        });

    }




    public boolean isOnline() {
        ConnectivityManager connM = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connM.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    /* * String형을 BitMap으로 변환시켜주는 함수 * */
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /* * Bitmap을 String형으로 변환 * */
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream Stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,Stream);
        byte[] imgBytes = Stream.toByteArray();
        String image = Base64.encodeToString(imgBytes,Base64.DEFAULT);

        return image;
    }

    private String GIFToString(Bitmap bitmap) {
        ByteArrayOutputStream Stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,Stream);
        byte[] imgBytes = Stream.toByteArray();
        String image = Base64.encodeToString(imgBytes,Base64.DEFAULT);

        return image;
    }

    private void makeDialog(){

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(chattingAtcivity.this,R.style.AlertDialogTheme);

        alt_bld.setTitle("짝해제").setIcon(R.drawable.ic_baseline_smartphone_24).setCancelable(

                false)
                .setNegativeButton("매치 취소",

                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onClick(DialogInterface dialogInterface, int id) {

                                Log.v("알림", "다이얼로그 > 매치취소 선택");
                                // 로딩창 보여주기
                                Toast.makeText(chattingAtcivity.this, "매치취소", Toast.LENGTH_LONG).show();

                                ConversationDelete(roomnum);

                                //매칭날짜삭제 RemoveMday
                                RemoveMday(roomnum);
                                Handler hd = new Handler();
                                hd.postDelayed(new sendcancelFCM(), 500);

                                //////// 소켓에서 방지우기 V
                                /////// DB에서 like 지우고 싫어요 추가 V
                                /////// DB에서 대화기록 삭제하던가 휴면처리 V
//                        CancelMatch(Myphone,phonewithlikes);
//                        database = FirebaseDatabase.getInstance();
//                        ref = database.getReference();
//                        LatestMsg latestMsg = new LatestMsg("매칭이 취소됬습니다..");
//                        ref.child(roomnum+"/최신메세지").setValue(latestMsg);


                            }

                        }).
//                .setNeutralButton("매치 취소",
//
//                new DialogInterface.OnClickListener() {
//
//                    @RequiresApi(api = Build.VERSION_CODES.O)
//                    public void onClick(DialogInterface dialog, int id) {
//                        Log.v("알림", "다이얼로그 > 매치취소 선택");
//                        // 로딩창 보여주기
//                        Toast.makeText(chattingAtcivity.this, "매치취소", Toast.LENGTH_LONG).show();
////                        CancelMatch(Myphone,phonewithlikes);
//                        ConversationDelete(roomnum);
////                        Intent love = new Intent(chattingAtcivity.this, Matched_Activity.class);
////                        startActivity(love);
////                        finish();
//                        Handler hd = new Handler();
//                        hd.postDelayed(new sendcancelFCM(), 800);
//
//                        //////// 소켓에서 방지우기 V
//                     /////// DB에서 like 지우고 싫어요 추가 V
//                     /////// DB에서 대화기록 삭제하던가 휴면처리 V
////                        CancelMatch(Myphone,phonewithlikes);
////                        database = FirebaseDatabase.getInstance();
////                        ref = database.getReference();
////                        LatestMsg latestMsg = new LatestMsg("매칭이 취소됬습니다..");
////                        ref.child(roomnum+"/최신메세지").setValue(latestMsg);
//
//
//                    }
//
//                }).
                setPositiveButton("취소",

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        Log.v("알림", "다이얼로그 > 취소");

                        //취소한다


                    }

                });

        AlertDialog alert = alt_bld.create();

        alert.show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        chatdata = new ArrayList<>();
        /////////loadch///////채팅 로드
       // loadchat(roomnum);
        //////////////////

    }

    @Override
    protected void onStop() {
        super.onStop();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        childUpdates = new HashMap<>();

        UserOnOff userOnOff = new UserOnOff("off");
        childUpdates.put(roomnum +"/"+Myphone,userOnOff);

        ref.updateChildren(childUpdates);

        Log.e(" onStop fcm채팅방", "상태 "+roomnum+"| "+Myphone);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();

        if(Message != null) {

            String[] split = Message.split("[|]");

            String MyNum = split[1]; //
            String m = split[2];
            if (!Myphone.equals(MyNum)) {

                database = FirebaseDatabase.getInstance();
                ref = database.getReference();



//////////////////////////////////////////////////////////////////////////////////////////////


            }

        }
    }

    void cancelFCM(String token, String myname){
        Gson gson = new Gson();

        NotiModel notiModel = new NotiModel();
        notiModel.to = token;
        notiModel.data.title = "매칭취소.";
        notiModel.data.text = myname +  "님과 매칭이취소됬습니다";
        notiModel.data.clickAction = roomnum;

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

    // 여기서 통신을한다.
    private void RemoveMday(String roomnum) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") ) {

                        Log.d("응답", "매치데이삭제");
                    }else {

                        Log.d("응답", "매치데이삭제 실패");

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        RemoveMday add=new RemoveMday(roomnum ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(chattingAtcivity.this);
        queue.add(add);
    }
    ///////////////////////////RemoveMday/////////////////////////////////////////

    public void makingChanel() {

/////////////////////////채널생성//////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 채널에 필요한 정보 제공
            CharSequence name = "msg";
            String description = "sendMsg";

            /**
             [알림음 중요도 참고]
             1. IMPORTANCE_HIGH : 알림음 발생 및 헤드업 알림 표시
             2. IMPORTANCE_DEFAULT : 알림음 발생
             3. IMPORTANCE_LOW : 알림음 없음
             4. IMPORTANCE_MIN : 알림음 없고, 상태 표시줄에 나타나지 않음
             */

            // 중요도 설정, Android7.1 이하는 다른 방식으로 지원한다.(위에서 설명)
            int importance = NotificationManager.IMPORTANCE_HIGH;

            // 채널 생성
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID3, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void getintent() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        city = intent.getStringExtra("city");
        hobbies = intent.getStringExtra("hobbies");
        intro = intent.getStringExtra("intro");
        job = intent.getStringExtra("job");
        workplace = intent.getStringExtra("workplace");
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
        downimg = intent.getStringExtra("pic");
//        String MSG = intent.getStringExtra("msg");
//        String date = intent.getStringExtra("date");
        editopen = intent.getBooleanExtra("openedit",false);
        Log.e(TAG, "인텐트 파악 ="+name);

//        int mynum = Integer.parseInt(Myphone.replace("+8210", "1"));
//        int somenum = Integer.parseInt(phonewithlikes.replace("+8210", "1"));
//        String room = String.valueOf(mynum+somenum);
//
//         FirebaseDatabase database = FirebaseDatabase.getInstance();
//         DatabaseReference ref = database.getReference();
//        if(!Myphone.equals(phonewithlikes)){
//            /// 채팅창오픈시 파이어베이스 서버에 번호를뺀 가공된 메세지를 보낸다 그리고 읽음표시가 된다.
//            LatestMsg latestMsg = new LatestMsg(""+"|"+MSG+"|"+room+"|"+date,Myphone);
//            ref.child(room+"/최신메세지").setValue(latestMsg);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(String message) {
      // 메세지받기
        Message = message;
        Log.d("message응답 =", message);

        String[] split = message.split("[|]");



        String MyNum = split[1]; //
        String msg = split[2];
        hourandm = split[4];
        FirstDate = split[5];

        if (split.length < 2 || !split[0].equals("say")) {
            return;

        }
        else if(split[0].equals("say") && !Myphone.equals(MyNum))
        {

            today = split[3];

          // 상대번호 메세지만 받기
//            if (Myphone.equals(MyNum)) {
//                // 나
//                isMe = true;
//                ChatMessage = msg;
//                chatroom = roomnum;
//            } else {
//                // 남
//                isMe = false;
//                ChatMessage = msg;
//                partnerImg = im;
//                chatroom = roomnum;
//
//            }

            // 남
            isMe = false;
            ChatMessage = msg;
            partnerImg = im;
            chatroom = roomnum;
            Log.d("message응답 = ", "msg 받았습니다."+msg);

            if(msg.startsWith("https://media.tenor")){
                ChatMessage = "";
                receivedImg = Uri.parse(msg);
                imgok = true;
                Log.d("msge응답 = ", "GIF 받았습니다.");

            }
            else if(msg.startsWith("uploads/") || msg.endsWith("png")){
                receivedImg = Uri.parse(IP_ADDRESS+msg);
                imgok = true;
                Log.d("msge응답 = ", "이미지 받았습니다.");

            }
            else
                {
                receivedImg = null;
                imgok = false;
                Log.d("msge응답 = ", "이미지 없어.");

                }



            if(!Myphone.equals(MyNum)) {


                if (mChatDataList == null || mChatDataList.size() == 0) {
                    chatItems chatting = new chatItems(FirstDate, chatroom, hourandm, ChatMessage, MyNum,bp, partnerImg, receivedImg, isMe, imgok, false,conn);
                    mChatDataList.add(chatting);

                } else {

                    chatItems chatting = new chatItems(today, chatroom, hourandm, ChatMessage, MyNum,bp, partnerImg, receivedImg, isMe, imgok, false,conn);
                    mChatDataList.add(chatting);
                }
                for (int i = 0; i < mChatDataList.size(); i++){
                    Log.d("message응답msglist = ", "msg 받았습니다."+mChatDataList.get(i).message);

                }
            }
        }
        ///////////////용도가다름//////////

        // UI 스레드로 실행
    runOnUiThread(new Runnable() {
        @Override
        public void run () {
            // 메세지 갱신


//                if (mChatDataList == null || mChatDataList.size() == 0) {
//                    chatItems chatting = new chatItems(FirstDate, chatroom, hourandm, ChatMessage, MyNum,bp, partnerImg, receivedImg, isMe, imgok, false,conn);
//                    mChatDataList.add(chatting);
//
//                } else {
//
//                    chatItems chatting = new chatItems(today, chatroom, hourandm, ChatMessage, MyNum,bp, partnerImg, receivedImg, isMe, imgok, false,conn);
//                    mChatDataList.add(chatting);
//                }
//            chatroomAdapter.notifyDataSetChanged();
            if(!Myphone.equals(MyNum)) {


                if (isMe && InKeyboard) {
                    chatRoomlayoutManager = new LinearLayoutManager(chattingAtcivity.this);
                    chatRoomlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    chatRoomlayoutManager.setStackFromEnd(true);
                    chatroomAdapter = new chatroomAdapter(mChatDataList, Myphone, chattingAtcivity.this, name, age, city, hobbies, img, img2, img3, img4, img5, img6, intro, job, school, distance, gender, phonewithlikes, token,workplace);
                    mrecyclerView.setLayoutManager(chatRoomlayoutManager);
                    mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount() - 1);
                    receivedImg = null;
                } else if (InKeyboard) {
                    chatRoomlayoutManager = new LinearLayoutManager(chattingAtcivity.this);
                    chatRoomlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    chatRoomlayoutManager.setStackFromEnd(true);
                    chatroomAdapter = new chatroomAdapter(mChatDataList, Myphone, chattingAtcivity.this, name, age, city, hobbies, img, img2, img3, img4, img5, img6, intro, job, school, distance, gender, phonewithlikes, token, workplace);
                    mrecyclerView.setLayoutManager(chatRoomlayoutManager);
                    mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount() - 1);
                    receivedImg = null;
                } else {
                    chatRoomlayoutManager = new LinearLayoutManager(chattingAtcivity.this);
                    chatRoomlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    chatRoomlayoutManager.setStackFromEnd(true);
                    chatroomAdapter = new chatroomAdapter(mChatDataList, Myphone, chattingAtcivity.this, name, age, city, hobbies, img, img2, img3, img4, img5, img6, intro, job, school, distance, gender, phonewithlikes, token,workplace);
                    mrecyclerView.setLayoutManager(chatRoomlayoutManager);
                    mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount() - 1);
                    receivedImg = null;
                }
            }
        }
    });

}

////////// 전송버튼/////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {

    if(!Objects.requireNonNull(msg.getText()).toString().equals(""))
    {


        //////// 와이파이나 데이터가 끊기면 임시저장으로 메세지를 저장한뒤 다시 연결되면 메시지를 재전송한다.//////////
        if(!isOnline())
        {
            conn = false;
            hourAndm = "";
            chatdata.add(msg.getText().toString());
            String ChatDt = String.valueOf(chatdata);
            SharedPreferences set = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
            SharedPreferences.Editor saveMSG = set.edit();
            saveMSG.putString(roomnum, ChatDt);
            saveMSG.apply();
        }
        else
            {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

            Date date = new Date();
            CurrentDate = simpleDateFormat.format(date);
            Log.e(TAG, "현재날짜 ="+CurrentDate);

            Calendar time = Calendar.getInstance();
            int isAMorPM = time.get(Calendar.AM_PM);
            int hour = time.get(Calendar.HOUR);

            if(hour > 12){
                hour = hour-12;
            }
            switch(isAMorPM) {
                case Calendar.AM:
                    hourAndm = "오전 "+hour+"시 "+ time.get(Calendar.MINUTE)+"분";

                    break;
                case Calendar.PM:

                    hourAndm = "오후 "+hour+"시 "+ time.get(Calendar.MINUTE)+"분";

                    break;
            }
        }

        Log.d(TAG, "msg NET connected: " + isOnline());

            sender.sendMessage(msg.getText().toString(), false, bitmap);

            MyMsg = msg.getText().toString();

//////////////////////////////////////////////////////////////////////////////////////////////////////////
        Log.e(TAG, "chatdata리스트= "+chatdata);

        int NumOfsameDate = AddDate(CurrentDate);

        if(mChatDataList == null || mChatDataList.size() == 0 || NumOfsameDate == 0){
            chatItems chatting = new chatItems(CurrentDate,roomnum,hourAndm,MyMsg,Myphone,bp,partnerImg,receivedImg,true,false,false,conn);
            mChatDataList.add(chatting);

        }
        else if(NumOfsameDate > 0)
            {
            chatItems chatting = new chatItems("no", roomnum, hourAndm, MyMsg, Myphone,bp, partnerImg, receivedImg, true, false, false,conn);
            mChatDataList.add(chatting);
            }
        chatRoomlayoutManager = new LinearLayoutManager(chattingAtcivity.this);
        chatRoomlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatRoomlayoutManager.setStackFromEnd(true);
        chatroomAdapter = new chatroomAdapter(mChatDataList,Myphone,chattingAtcivity.this, name,age, city,hobbies,img,img2,img3,img4,img5,img6,intro,job,school,distance,gender,phonewithlikes,token,workplace);
        mrecyclerView.setLayoutManager(chatRoomlayoutManager);
        mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount() - 1);
        receivedImg = null;

        msgforwoman.setVisibility(View.GONE);

        if(enter){
            sendFCM(token,Name,msg.getText().toString(),Myphone);
        }
        msg.setText("");

    }
    }

    private void getStatusOfNet()
    {

        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if(status == NetworkStatus.TYPE_MOBILE){
            Log.e(TAG, "모바일로");


        }else if (status == NetworkStatus.TYPE_WIFI){
            Log.e(TAG, "와이파이로");

        }else {
            Log.e(TAG, "연결안됨");
            ThreadStart();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downpic(String pic) {
        sender.sendMessage(msg.getText().toString(), false,bitmap);
        msgforwoman.setVisibility(View.GONE);
        msg.setText("");

        }


    ////////////////////////////////////////////////////
    public void sendFCM(String token,String myname,String msg,String num) {
        Gson gson = new Gson();

        NotiModel notiModel = new NotiModel();
        notiModel.to = token;
//            notiModel.notification.title = "메세지 from "+ partner;
//            notiModel.notification.body = "새로운 메시지를 받았습니다.";

        notiModel.data.title = "메시지 from "+ myname;
        notiModel.data.text = "새로운 메시지를 받았습니다.";
        notiModel.data.clickAction = msg+"|"+num;

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


    public int AddDate(String n) {
        for (int z = 0; z < mChatDataList.size(); z++) {
            if (mChatDataList.get(z).getDate().equals(n)) {
               int dateNums = +1;
                return dateNums;
            }
        }
        return 0;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        softKeyboard.unRegisterSoftKeyboardCallback();
//        try {
//              socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }


    ////////////////////////////////////////////////////////////
    // 여기서 통신저장을한다.
    private void CancelMatch(String Myphone, String phonewithlikes) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") ) {

                        try{
                            cancelFCM(token,Name);

                            Log.d("매칭삭제응답 =", "매칭삭제");
//                            ref = database.getReference(roomnum);
//                            ref.removeValue();
//////////////////////////////////////////////////////////////////////////////////////////
//                            offchat offchat = new offchat("off");
//                            ref.child(roomnum+"/취소/"+Myphone).setValue(offchat);
/////////////////////////////////////////////////////////////////////////////////////////
                            ref = database.getReference();
                            LatestMsg latestMsg = new LatestMsg("매칭이 취소됬습니다..",Myphone);
                            ref.child(roomnum+"/최신메세지").setValue(latestMsg);

//                            ref = database.getReference(roomnum+"/최신메세지상태");
//                            ref.removeValue();

                            Intent love = new Intent(chattingAtcivity.this, Matched_Activity.class);
                            love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            love.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(love);
                            overridePendingTransition(R.anim.anim_left_enter, R.anim.slide_left_exit);


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {

                        Log.d("응답", "매칭취소 실패");

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        CancelMatch add=new CancelMatch(Myphone,phonewithlikes ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 여기서 통신저장을한다.
    private void  ConversationDelete(String room) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") ) {


                    }else {

                        Log.d("매칭삭제응답", "실패");

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        ConversationDelete add=new ConversationDelete(room,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    // 여기서 채팅불러온다
    private void loadchat(String key) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {
                        Log.d("load응답", ": "+success);
                        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.

                        mChatDataList = new ArrayList<>();
                        chat = success;
                        String Msg[] = chat.split(",");
                           //// Msg == [+821033334444|하이 , +821035636442|ㅗㅗ]
                        receivedImg = null;


                        for (int i = 0; i < Msg.length; i++){
                            //// parts == [+821033334444 , 하이]
                            String parts[] = Msg[i].split("[|]");


                            if(parts[0].equals("체크")){
                                date = parts[1];
                                Uri uri = im;
                                chatItems chatting = new chatItems(date,key,"","","data",bp,uri,null,false,false,true,true);
                                mChatDataList.add(chatting);
                            }
                            else
                                {

                                String nickname = parts[0];
                                String time = parts[2];
                                date = parts[3];

                                message = parts[1];
                                Uri partnerImg = im;
                                Log.e(TAG, "메세지로드 ="+message);

                                if (Myphone.equals(nickname)) {
                                    // 나
                                    isMe = true;
                                } else {
                                    // 남
                                    isMe = false;

                                }

                                    if(parts[1].startsWith("https://media.tenor.com"))
                                {
                                    receivedImg = Uri.parse(parts[1]);
                                    imgok = true;
                                }
                                else if(parts[1].startsWith("uploads/") || parts[1].endsWith("png")){
                                    receivedImg = Uri.parse(IP_ADDRESS+parts[1]);
                                    imgok = true;
                                }
//
                                else{
                                    receivedImg = null;
                                    imgok = false;
                                }
                                if (date == null) {
                                    date = "no";
                                }

////
                                chatItems chattingforload = new chatItems (date, key,time, message, nickname,bp, partnerImg, receivedImg,isMe,imgok,true,true);
                                mChatDataList.add(chattingforload);
                            }
                        }

                        loverimg.setVisibility(View.INVISIBLE);

                        fmlayout.setVisibility(View.VISIBLE);
                        mrecyclerView.setVisibility(View.VISIBLE);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

                        Date date = new Date();
                        CurrentDate = simpleDateFormat.format(date);
                        Log.e(TAG, "현재날짜 ="+CurrentDate);


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        SharedPreferences getChat = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                        String chatData = getChat.getString(roomnum,null);
                        Log.e(TAG, "chatData = "+chatData);

                        if(chatData != null) {
                            String cD = chatData.replace("[", " ").trim();
                            String CD = cD.replace("]", " ").trim();
                            String[] chatdata = CD.split(",");

                            for (int i = 0; i < chatdata.length; i++) {
                                String msg = chatdata[i];

                                int NumOfsameDate = AddDate(CurrentDate);

                                if (mChatDataList == null || mChatDataList.size() == 0 || NumOfsameDate == 0) {

                                    if(msg.startsWith("uploads/"))
                                    {
                                        String[] IMG = msg.split("[|]");
                                        bp = StringToBitmap(IMG[1]);

                                        chatItems chatting = new chatItems(CurrentDate, roomnum, "noWifi", msg, Myphone, bp, partnerImg, receivedImg, true, true, false, conn);
                                        mChatDataList.add(chatting);
                                    }
                                    else
                                        {
                                        chatItems chatting = new chatItems(CurrentDate, roomnum, "noWifi", msg, Myphone, bp, partnerImg, receivedImg, true, false, false, conn);
                                        mChatDataList.add(chatting);
                                        }

                                } else if (NumOfsameDate > 0) {

                                    if(msg.startsWith("uploads/"))
                                    {
                                        String[] IMG = msg.split("[|]");
                                        bp = StringToBitmap(IMG[1]);

                                        chatItems chatting = new chatItems(CurrentDate, roomnum, "noWifi", msg, Myphone, bp, partnerImg, receivedImg, true, true, false, conn);
                                        mChatDataList.add(chatting);
                                    }
                                    else
                                        {
                                        chatItems chatting = new chatItems("no", roomnum, "noWifi", msg, Myphone, bp, partnerImg, receivedImg, true, false, false, conn);
                                        mChatDataList.add(chatting);
                                    }
                                }
                            }
                        }
                        chatRoomlayoutManager = new LinearLayoutManager(chattingAtcivity.this);
                        chatRoomlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        chatRoomlayoutManager.setStackFromEnd(true);
                        chatroomAdapter = new chatroomAdapter(mChatDataList,Myphone,chattingAtcivity.this, name,age, city,hobbies,img,img2,img3,img4,img5,img6,intro,job,school,distance,gender,phonewithlikes,token,workplace);
                        mrecyclerView.setLayoutManager(chatRoomlayoutManager);
                        mrecyclerView.setAdapter(chatroomAdapter);
                        mrecyclerView.scrollToPosition(chatroomAdapter.getItemCount() - 1);

                        msgforman.setVisibility(View.GONE);
                        msgforwoman.setVisibility(View.GONE);
                    }else {

                        Log.d("load응답", "없음");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                 }
            }
        };


        loadchat add=new loadchat(key, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
    }



}// ChattiingActivity


    class Receiverr extends Thread {
        Socket socket;
        DataInputStream in;
        String phone;
        String partnerphone;
        String name,token;
        String roomnum;
        public static Boolean isOnline = true;

        private FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
        //현재 연결은 데이터베이스에만 딱 연결해놓고
        //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.

        private DatabaseReference ref = database.getReference();

        interface OnReceiveListener {
            void onReceive(String message);
        }

        static OnReceiveListener mListener;

        public static void setOnReceiveListener(OnReceiveListener listener) {
            mListener = listener;
        }

        public Receiverr(Socket socket,String phone,String partnerphone,String name, String token) {
            this.socket = socket;
            this.phone = phone;
            this.partnerphone = partnerphone;
            this.name = name;
            this.token = token;

            try{
//                in = new DataInputStream(this.socket.getInputStream());
                in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            }catch(Exception e){
                System.out.println("예외:"+e);
            }
        }


        /**
         * 메시지 파서
         */
        public String[] getMsgParse(String msg) {
            //System.out.println("msgParse()=>msg?"+ msg);
            String[] tmpArr = msg.split("[|]");

            return tmpArr;
        }

        @Override
        public void run() {

           while (in != null) {
            try {
                String msg = in.readUTF(); //입력스트림을 통해 읽어온 문자열을 msg에 할당.

                String[] msgArr = getMsgParse(msg.substring(msg.indexOf("|")+1));

                String[] msgJoinArr = getMsgParse(msg.substring(msg.indexOf("|")));

                Log.d("receiver", "run: ");
                isOnline = true;
//                    메세지 처리 ----------------------------------------------
                    if (msg.startsWith("enterRoom#yes")) { //그룹입장

                        //enterRoom#yes|지역

                        Log.e("응답 채팅방", "[##] 채팅방 (" + msgJoinArr[1] + ") 님 입장하였습니다. ");

                              int mynum = Integer.parseInt(phone.replace("+8210", "1"));
                              int somenum = Integer.parseInt(partnerphone.replace("+8210", "1"));
                              roomnum = String.valueOf(mynum+somenum);

                              UserOnOff userOnOff = new UserOnOff("on");
                              ref.child(roomnum +"/"+phone).setValue(userOnOff);
                              Log.e("응답 fcm채팅방", "상태 "+roomnum+"| "+phone);


                    }


                    if (msg.startsWith("say")) { //대화내용
                        //say|아이디|대화내용

                        mListener.onReceive(msg);
                        Log.e("응답채팅방", "상태 "+msg);

                        UserOnOff userOnOff = new UserOnOff("on");
                        ref.child(roomnum +"/"+phone).setValue(userOnOff);
                        Log.e("응답 fcm채팅방", "상태 "+roomnum+"| "+phone);


                    } else {
                        Log.d("응답", "없음");


                    }
                }
            catch(SocketException e){
                System.out.println("예외:"+e);
                System.out.println("##접속중인 서버와 연결이 끊어졌습니다.");
                isOnline = false;
                Log.d("receiver", "isOnline ="+isOnline);
                Log.e("receiver", "넷트워크 끊어짐");
                return;

            }catch (ArrayIndexOutOfBoundsException e){
                Log.e("recive arrayindexout", "run: " + e.getMessage());
            }
            catch (EOFException e) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            } catch (Exception e) {
                Log.e("chatrecive", "run: " + e.getMessage());
               }

            }
        }

    }


/////////////////////////////////////////////////////////////////////
//
//    //서버로부터 메시지를 읽는 클래스
//    class Receiverr1 extends Thread {
//
//        Socket socket;
//        DataInputStream in;
//
//        //Socket을 매개변수로 받는 생성자.
//        public Receiverr1(Socket socket) {
//            this.socket = socket;
//
//            try {
//                in = new DataInputStream(this.socket.getInputStream());
//            } catch (Exception e) {
//                System.out.println("예외:" + e);
//            }
//        }//생성자 --------------------
//
//
//        /**
//         * 메시지 파서
//         */
//        public String[] getMsgParse(String msg) {
//            //System.out.println("msgParse()=>msg?"+ msg);
//
//            String[] tmpArr = msg.split("[|]");
//
//            return tmpArr;
//        }
//
//
//        @Override
//        public void run() { //run()메소드 재정의
//
//            while (in != null) { //입력스트림이 null이 아니면..반복
//                try {
//
//
//                    String msg = in.readUTF(); //입력스트림을 통해 읽어온 문자열을 msg에 할당.
//
//                    String[] msgArr = getMsgParse(msg.substring(msg.indexOf("|") + 1));
//
//                    //메세지 처리 ----------------------------------------------
//                    if (msg.startsWith("enterRoom#yes")) { //그룹입장
//
//                        //enterRoom#yes|지역
//                        Log.d("응답[enterRoom#yes]", "채팅방 (" + msgArr[0] + ") 에 입장하였습니다.");
//
//                    } else if (msg.startsWith("say")) { //대화내용
//                        //say|아이디|대화내용
//                        System.out.println("[" + msgArr[0] + "] " + msgArr[1]);
//                        Log.d("응답", "[" + msgArr[0] + "]" + msgArr[1]);
//
////                else if(msg.startsWith("req_fileSend")){ //상대방이 현재 사용자에게 파일전송 수락 요청
////                    //req_fileSend|출력내용
////                    //req_fileSend|[##] name 님께서 파일 전송을 시도합니다. 수락하시겠습니까?(Y/N)
////                    MultiClient.chatState = 5; //상태 변경 (상대방이 현재사용자에게 파일전송을 수락요청한 상태)
////                    System.out.println(msgArr[0]); //메세지만 추출
////                    System.out.print("▶선택:");
////                    sleep(100);
////
////                }
////                else if(msg.startsWith("fileSender")){ //파일을 보내기위해 파일서버 준비
////
////                    //fileSender|filepath;
////                    System.out.println("fileSender:"+InetAddress.getLocalHost().getHostAddress());
////                    System.out.println("fileSender:"+msgArr[0]);
////                    //String ip=InetAddress.getLocalHost().getHostAddress();
////
////                    try {
////                        new FileSender(msgArr[0]).start(); //쓰레드 실행.
////                    } catch (Exception e) {
////                        System.out.println("FileSender 쓰레드 오류:");
////                        e.printStackTrace();
////                    }
////
////                }
////                else if(msg.startsWith("fileReceiver")){ //파일받기
////                    //fileReceiver|ip|fileName;
////
////                    System.out.println("fileReceiver:"+InetAddress.getLocalHost().getHostAddress());
////                    System.out.println("fileReceiver:"+msgArr[0]+"/"+msgArr[1]);
////
////                    String ip = msgArr[0];  //서버의 아이피를 전달 받음
////                    String fileName = msgArr[1]; //서버에서 전송할 파일이름.
////
////                    try {
////                        new FileReceiver(ip,fileName).start(); //쓰레드 실행.
////                    } catch (Exception e) {
////                        System.out.println("FileSender 쓰레드 오류:");
////                        e.printStackTrace();
////                    }
//
//
//                    } else if (msg.startsWith("req_exit")) { //종료
//
//                        Log.d("응답[req_exit]: ", "채팅방 (" + msgArr[0] + ") 에 입장하였습니다.");
//                    }
//
//                } catch (SocketException e) {
//                    System.out.println("예외:" + e);
//                    System.out.println("##접속중인 서버와 연결이 끊어졌습니다.");
//                    return;
//
//                } catch (EOFException e) {
//                    try {
//                        socket.close();
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//
//                } catch (Exception e) {
//                    System.out.println("Receiver:run() 예외:" + e);
//                    try {
//                        socket.close();
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                }
//            }//while----
//        }//run()-----
//
//
//    }//class Receiver -------


/////////////////////////////////////////////////////////////////////

    //서버로 메시지를 전송하는 클래스
    class Senderr extends Thread {
        IPadress ipadress = new IPadress();
        String IP_ADDRESS = ipadress.ip.IP_Adress;
        Socket socket;
        DataOutputStream out;
        String partner = ""; // 내번호 저장
        String me = ""; // 내번호 저장
        String MSG; // 메세지
        String Name,token;
        String date;
        static boolean Reconnet = true;
        private FirebaseDatabase database = FirebaseDatabase.getInstance();

        //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
        //현재 연결은 데이터베이스에만 딱 연결해놓고
        //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
        private DatabaseReference ref = database.getReference();

        //생성자 ( 매개변수로 소켓과 사용자 이름 받습니다. )
        public Senderr(Socket socket, String m, String p,String token,String name,String date) { //소켓과 사용자 이름을 받는다.
            this.socket = socket;
            this.partner = p;
            this.me = m;
            this.token = token;
            this.Name = name;
            this.date = date;

//        this.MSG =msg;

            try {
                out = new DataOutputStream(this.socket.getOutputStream());
//                  out = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
            } catch (Exception e) {
                System.out.println("예외:" + e);
            }
        }

        @Override
        public void run() {

                try {
                    ////////////////////채팅 입장 사인"||>"

                    out.writeUTF(partner + "|" + me + "|" + "**>"+"|say");

                    Log.e("Senderr", me+" say");

               } catch (IOException e) {
                    e.printStackTrace();
                }
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public  void sendMessage(String msg, Boolean noimg,String Stringbit) { //run()메소드 재정의
            MSG = msg;

            new Thread(new Runnable() {
                @Override
                public void run() {


                    while (!MSG.equals("")) { //출력스트림이 null이 아니면..반복
                        int mynum = Integer.parseInt(me.replace("+8210", "1"));
                        int somenum = Integer.parseInt(partner.replace("+8210", "1"));
                        String roomnum = String.valueOf(mynum+somenum);

                        int mynum2 = Integer.parseInt(me.replace("+8210", "2"));
                        int somenum2 = Integer.parseInt(partner.replace("+8210", "2"));
                        String roomnum2 = String.valueOf(mynum2+somenum2);
                        try { //while문 안에 try-catch문을 사용한 이유는 while문 내부에서 예외가 발생하더라도
                            //계속 반복할수있게 하기위해서이다.

                            if (MSG == null || MSG.trim().equals("")) {

                                MSG = " ";
                                //continue; //콘솔에선 공백으로 넘기는것이 좀더 효과적임.
                                //System.out.println("공백");
                            }



                            if (!noimg && !MSG.trim().equals("") && !MSG.equals("exit//*"+roomnum) && !MSG.equals("escape//*"+roomnum)) {

                                    out.writeUTF(partner + "|" + me + "|" + MSG.trim() + "|" + "say" + "|" + "");
                                      Log.e("Senderr", MSG.trim());

                                    String mynumber = roomnum2;
//                                    String message = me + "|" + MSG + "|" +"wechat"+ "|" + date+ "|" +roomnum;
                                String message = "대화중";
                                LatestMsg latestMsg = new LatestMsg(message,mynumber);
                                ref.child(roomnum+"/최신메세지").setValue(latestMsg);

                                chackNewmsg newMsg = new chackNewmsg(me+"|new");
                                ref.child(roomnum + "/최신메세지상태").setValue(newMsg);

                                MSG = "";

                            }
                           else if(noimg){
//                                ImageToDB(MSG,Stringbit,roomnum,roomnum2);

                                String mynumber = roomnum2;

                                Log.e("Senderr", MSG.trim());

                                String message = "대화중";
                                LatestMsg latestMsg = new LatestMsg(message,mynumber);
                                ref.child(roomnum+"/최신메세지").setValue(latestMsg);
                                out.writeUTF(partner + "|" + me + "|" + MSG+"|"+"say" + "|" + Stringbit);

                                chackNewmsg newMsg = new chackNewmsg(me+"|new");
                                ref.child(roomnum + "/최신메세지상태").setValue(newMsg);


                                MSG = "";

                            }

                            /// 방탈퇴
                            else if (MSG.equals("exit//*"+roomnum)) {
                                cancelFCM(token,Name);
                                System.out.println("유저 :채팅방종료.");
                                out.writeUTF(partner + "|" + me + "|" + MSG+"|"+"exit");
                                System.exit(0);


                            }
                            // 방나오기
                            else if (MSG.equals("escape//*"+roomnum)) {
                                System.out.println("유저 :방나가기.");
                                out.writeUTF(partner + "|" + me + "|" + MSG+ "|" + date);
                                System.exit(0);


                            }

//                            else if(MSG.startsWith("files_server/")){
//                                out.writeUTF( partner + "|" + me + "|" + MSG+"|"+"img");
//
//                            }
                            else {

//                                Toast.makeText(contest, "[##] 공백을 입력할수없습니다.\r\n" + "▶다시 입력해 주세요:", Toast.LENGTH_SHORT).show();

                            }



                        } catch (SocketException e) {
                            System.out.println("Sender:run()예외:" + e);
                            System.out.println("##접속중인 서버와 연결이 끊어졌습니다.");
                            Reconnet = false;
                            return;
                        } catch (IOException e) {
                            System.out.println("예외:" + e);
                        }
//                        try {
//                            socket.close();        // 클라이언트와 연결된 서버가 터지면 flush에서 catch로 넘어가면서 exception발생 파이널로간다.
//                        } catch (IOException ioException) {
//                            ioException.printStackTrace();
//                        }
                    }//while------


                }//run()------
            }).start();
        }//class Sender-------




        void cancelFCM(String token, String myname){
            Gson gson = new Gson();

            NotiModel notiModel = new NotiModel();
            notiModel.to = token;
            notiModel.data.title = "매칭취소.";
            notiModel.data.text = myname +  "님과 매칭이취소됬습니다";
            notiModel.data.clickAction = "MatchingActivity";

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


//
//        private void ImageToDB(String imgPath,String bitmap,String) {
//
//
//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("myimg", imgPath)
//                    .addFormDataPart("file", bitmap)
//                    .build();
//
//            Request request = new Request.Builder() .url(IP_ADDRESS+"/ImageSendInchat.php")// Server URL 은 본인 IP를 입력
//                   .post(requestBody)
//                    .build();
//                    OkHttpClient client = new OkHttpClient();
//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
//
//                            Log.d("TEST : ", response.body().string());
//
//
//
//                        }
//
//                        @Override public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    });
//
//        }
    }







