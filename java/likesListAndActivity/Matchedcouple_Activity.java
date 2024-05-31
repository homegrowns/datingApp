package likesListAndActivity;

import android.animation.Animator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.honeybee.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import CouplesAndchat.LatestMsg;
import CouplesAndchat.Matched_Activity;
import Utills.IPadress;
import Utills.NotiModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Matchedcouple_Activity extends AppCompatActivity {
    LottieAnimationView animationView, animaLove;
    Uri im,imw;
    IPadress ipadress = new IPadress();
    private static final String CHANNEL_ID3 = "matchCH";
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    String manimg,womanimg,tk,myname;
    String num,partnerNum,roomnum,roomFordate;
    ImageView mimg,wimg;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference ref = database.getReference();
    String convertedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchedcouple);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 채널에 필요한 정보 제공
            CharSequence name = "matchingChannel";
            String description = "matching app";

            // 중요도 설정, Android7.1 이하는 다른 방식으로 지원한다.(위에서 설명)
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // 채널 생성
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID3, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent info = getIntent();
        manimg = info.getStringExtra("이성이미지");
        womanimg = info.getStringExtra("내이미지");
        tk = info.getStringExtra("token");
        myname = info.getStringExtra("이름");
        num = info.getStringExtra("내번호");
        partnerNum = info.getStringExtra("상대번호");

        mimg = findViewById(R.id.img_man);
        wimg = findViewById(R.id.img_woman);

        im =  Uri.parse(IP_ADDRESS+manimg);
        imw =  Uri.parse(IP_ADDRESS+womanimg);

        Glide.with(this).load(im).into(mimg);
        Glide.with(this).load(imw).into(wimg);

        animationView = findViewById(R.id.lottie);
        animationView.setAnimation("8307-love-icon-animation.json");
        animationView.playAnimation();
        animationView.setRepeatCount(1);

        animaLove = findViewById(R.id.lottie2);
        animaLove.setAnimation("78290-balloons-in-love.json");
        animaLove.playAnimation();
        animaLove.setRepeatCount(1);
        /////////////////////////fire 실시간데이터베이스에 매취취소후 메세지 삭제 안될시에 재매칭되면 삭제된다.///
        int mynum = Integer.parseInt(num.replace("+8210", "1"));
        int somenum = Integer.parseInt(partnerNum.replace("+8210", "1"));
        roomnum = String.valueOf(mynum+somenum);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference(roomnum);
        ref.removeValue();
        //////////////////////////////////////////////////////////////////////////////////////////////
        int mynum2 = Integer.parseInt(num.replace("+8210", "2"));
        int somenum2 = Integer.parseInt(partnerNum.replace("+8210", "2"));
        roomFordate= String.valueOf(mynum2+somenum2);

        ///////////////////////////////////////////날짜구하기///////////////////////////////////////
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        Date date = new Date();
        String currDateStr = simpleDateFormat.format(date);
        //현재시간 구하기 (시작 시간)

        convertedDate = currDateStr;

        /////////////////////////////////////////////////////////////////////////////////

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        LatestMsg latestMsg = new LatestMsg(convertedDate+"|1",num+partnerNum);
        ref.child(roomFordate+"/생성날짜").setValue(latestMsg);
        /////////////////////////////////////////////////////////////////////

        SaveMday(convertedDate,roomnum,num+"|"+partnerNum);

    }

    @Override
    protected void onStart() {
        super.onStart();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.GONE);
                sendFCMMatched(tk, myname);
                ////////////////////////////////////////////////////////////////////////////////////////////
                Intent love = new Intent(Matchedcouple_Activity.this, Matched_Activity.class);
                love.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(love);
                finish();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                animaLove.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    void sendFCMMatched(String token, String myname){
        Gson gson = new Gson();

        NotiModel notiModel = new NotiModel();
        notiModel.to = token;
//        notiModel.notification.title = myname + "님과 매칭되었습니다!";
//        notiModel.notification.text =myname + "님과 매칭되었습니다!";
        notiModel.data.title = "매칭확인.";
        notiModel.data.text = myname +  "님과 매칭되었습니다!";
        notiModel.data.body = myname +  "님과 매칭되었습니다!";
        notiModel.data.clickAction = "Matched_Activity";
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


    // 여기서 통신저장을한다.
    private void SaveMday(String MatchDay, String Room, String members) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null ) {
                        Log.d("Matchedcouple_Activity", "매칭날짜정보업데이트 성공");


                    }else {
                        Log.d("Matchedcouple_Activity", "매칭날짜정보업데이트 실패");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        MatchedTime add =new MatchedTime(MatchDay, Room, members,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }

}