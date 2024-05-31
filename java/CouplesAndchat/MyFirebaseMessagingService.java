package CouplesAndchat;

import static CouplesAndchat.Matched_Activity.getMsg;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.honeybee.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import likesListAndActivity.seelikes_Activity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "matchingCH";
//    private static final String CHANNEL_ID2 = "likes";

    NotificationManager notificationManager;
    private String title,text,click_action;
    private String msg;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManager = getSystemService(NotificationManager.class);
        createChannel();
        // 채널 생성
        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        Boolean isChecked = auto.getBoolean("melikes", false);
        Boolean MatchsisChecked = auto.getBoolean("newMatch", false);
        Boolean noMsgisChecked = auto.getBoolean("noMsg", false);
        Boolean CancelMatchisChecked = auto.getBoolean("CancelMatch", false);



        // Check if message contains a data payload.
        // 좋아요data 메세지
        if (remoteMessage.getData().size() > 0) {
            title = remoteMessage.getData().get("title").toString();
            text = remoteMessage.getData().get("text").toString();
            click_action = remoteMessage.getData().get("clickAction");


                if (isChecked && title.equals("좋아요 확인!")) {

                    SendNotification(title, text, click_action);
                }


                if (MatchsisChecked && title.equals("매칭확인.")) {

                    SendNotification(title, text, click_action);
                }

                 if (CancelMatchisChecked && title.equals("매칭취소.")) {

                SendNotification(title, text, click_action);
                }

               if (noMsgisChecked && text.equals("새로운 메시지를 받았습니다.")) {


                SendNotification(title, text, click_action);
               }

            if(text.equals("새로운 메시지를 받았습니다.")){
                   Log.d("fcm테스트", "새로운 메시지title | "+ click_action);
                   Log.d("fcm테스트", "getMsg | "+ getMsg);

                   // public static void changeOrder()// 해당 엑티비티안의 메소드를 static화 하여 그안에 어뎁터 에소드를 넣으면 어뎁터 메소드를 static화하지않고도 사용할수있어
                   //  notifyItemMoved();, notifyItemRangeChanged();(해당 메소드들은 static메소드안에서 쓸수없다) 등의 메소드를 쓸수있다.////
                   String[] vArr = click_action.split("[|]");

                   String MSG = vArr[0];
                   String num = vArr[1];

                   Log.d("fcm테스트", "새로운 메시지| "+MSG +"=="+ num);

                   if(getMsg)
                   {
                       /// 이미지 메세지일시 "이미지"로 최신메세지 받을수 있도록 걸러낸다.
                       if(MSG.startsWith("uploads/")){
                         msg = "이미지 전송";
                           Log.d("fcm이미지", "새로운 메시지| "+msg);

                       }
                       else
                       {
                         msg = MSG;
                       }
                       Matched_Activity.changeOrder(msg,num);
                   }

               }//..if새로운 메시지를 받았습니다

               if(title.equals("매칭취소.")){
                   Log.d("fcm테스트", "getMsg | "+ getMsg);
                   if(getMsg)
                   {
                       Matched_Activity.changeOrder("", "");
                   }
               }

                else {
                notificationManager = getSystemService(NotificationManager.class);

                }

        }
//        // 매칭data 메세지
//        if (remoteMessage.getData().size() > 0 && Matchsischecked) {
//           title = remoteMessage.getData().get("title").toString();
//           text = remoteMessage.getData().get("text").toString();
//           click_action = remoteMessage.getData().get("clickAction");
//            SendNotification(title,text,click_action);
//        }
//        // 새매세지data 메세지
//        if (remoteMessage.getData().size() > 0 && noMsgischecked) {
//            title = remoteMessage.getData().get("title").toString();
//            text = remoteMessage.getData().get("text").toString();
//            click_action = remoteMessage.getData().get("clickAction");
//            SendNotification(title,text,click_action);
//        }

    }


    private void createChannel() {

        ///////////////////////////////////////////////////////////////////////////////////////////
        // Android8.0 이상인지 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 채널에 필요한 정보 제공
            CharSequence name = "matchingChannel";
            String description = "matching app";

            // 중요도 설정, Android7.1 이하는 다른 방식으로 지원한다.(위에서 설명)
            int importance = NotificationManager.IMPORTANCE_HIGH;

//             notificationManager = getSystemService(NotificationManager.class);
//            // 채널 생성
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Configure the notification channel.

            channel.enableLights(true);
            channel.setLightColor(R.color.white);
            channel.setVibrationPattern(new long[]{0, 1000});
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);


        }
    }

    private void SendNotification(String title, String text, String click_action) {
//        createChannel();
/// 좋아요 푸쉬noti를 받고 좋아요 확인 엑티미티로 넘어갈수있도록한다.

        try{

        Intent intent = new Intent();
        if (click_action.equals("seelikes_Activity")) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(this, seelikes_Activity.class);
        }else if(click_action.equals("Matched_Activity")){//이런 액티비티 이름을 잘못 타이핑했네.ㅋ
//            intent = new Intent(this, Matched_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(this,Matched_Activity.class);

        }
//        else {
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setClass(this,Matched_Activity.class);
//
//        }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationbuilder = new
                    NotificationCompat.Builder(this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_app_icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setTicker("setTicker")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent);



            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1 /* ID of notification */, notificationbuilder.build());

            /////// 매칭 푸시noti 메세지 받은후 클릭하면 매치된 엑티비티로가서 채팅을 할수있도록한다,

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}