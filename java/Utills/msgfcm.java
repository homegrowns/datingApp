package Utills;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class msgfcm {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    String MSG,pnum,roomkey;
    String CHANNEL_ID2 = "서비스채널";


    public void getMsg(String token,String partner,String roomnum){

        ///////////////////////채널생성//////////////////////////
//
//        chattingAtcivity chatt = new chattingAtcivity();
//        chatt.makingChanel();
        ///////////////////////채널생성//////////////////////////


        ref.child(roomnum + "/최신메세지").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.child("msg").getValue(String.class);

                try {


                    if (value != null && !value.equals("매칭이 취소됬습니다..")) {

                        String[] vArr = value.split("[|]");
                        MSG = vArr[1];
                        pnum = vArr[0];
                        roomkey = vArr[2];
                        sendFCM(token,partner,roomnum,MSG);

                    } else if (value.equals("매칭이 취소됬습니다..")) {


                    }

                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }



    public void sendFCM(String token,String partner,String room,String msg) {
                Gson gson = new Gson();

        NotiModel notiModel = new NotiModel();
        notiModel.to = token;
//        notiModel.notification.title = "메세지 from "+ partner;
//        notiModel.notification.body = msg;

        notiModel.data.title = "메세지 from "+ partner;
        notiModel.data.text =  msg;


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
}
