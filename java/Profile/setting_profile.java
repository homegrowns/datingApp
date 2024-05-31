package Profile;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.honeybee.MainActivity;
import com.example.honeybee.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class setting_profile  extends AppCompatActivity  {
    private static final String TAG = "SettingsActivity";
    FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String CHANNEL_ID = "matchingCH";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    String phone,MyGender,RoomNums;
    int setg;
    String RV;
    SeekBar distance;
    SwitchCompat man, woman, messegelikes, newMatchs,noMsg,Cancelmatch;
    CheckBox unlimit;
    RangeSeekBar rangeSeekBar;
    TextView gender, distance_text, age_rnge,btn_logout, btn_revoke;
    private static ArrayList<CouplesAndchat.MdateAndRoom> MdateAndRoom;
    Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        customDialog = new Dialog(setting_profile.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        customDialog.setContentView(R.layout.revoke_dialog);             // xml 레이아웃 파일과 연결

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        MyGender = auto.getString("gender", null);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        TextView toolbar = findViewById(R.id.toolbartag);
//        toolbar.setText("Profile");
        ImageButton back = findViewById(R.id.btn_backSetting);
        Cancelmatch = findViewById(R.id.widget_CancelMatch);
        noMsg = findViewById(R.id.widget_noMsg);
        newMatchs = findViewById(R.id.widget_newMatchs);
        messegelikes = findViewById(R.id.widget_nolikes);
        distance = findViewById(R.id.distance);
        man = findViewById(R.id.switch_man);
        woman = findViewById(R.id.switch_woman);
        distance_text = findViewById(R.id.distance_text);
        age_rnge = findViewById(R.id.age_range);
        rangeSeekBar = findViewById(R.id.rangeSeekbar);
        btn_logout = findViewById(R.id.btn_logout);
        btn_revoke = findViewById(R.id.btn_revoke);
        gender = findViewById(R.id.gender_text);
        unlimit = findViewById(R.id.checkbox_unlimitdistance);


        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance_text.setText(progress + " Km");

                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor saveSettings = auto.edit();
                saveSettings.putInt("distance",progress);
                saveSettings.apply();
                distance.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        if(unlimit.isChecked()){
//            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//            SharedPreferences.Editor saveSettings = auto.edit();
//            saveSettings.remove("unlimitDis");
//            saveSettings.putBoolean("unlimitDis",true);
//            saveSettings.apply();
//        }else if(!unlimit.isChecked()){
//            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//            SharedPreferences.Editor saveSettings = auto.edit();
//            saveSettings.remove("unlimitDis");
//            saveSettings.putBoolean("unlimitDis",false);
//            saveSettings.apply();
//
//        }
        if(MyGender.equals("1")){
            man.setChecked(false);
            woman.setChecked(true);
        }
        if(MyGender.equals("0")){
            man.setChecked(true);
            woman.setChecked(false);
        }

        man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences setgender = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setgender.edit();
                    saveSettings.remove("mw");
                    saveSettings.putInt("mw",0);
                    saveSettings.apply();
                    man.setChecked(true);
                    woman.setChecked(false);
                    gender.setText("men");
                }
            }
        });
        woman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences setgender = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setgender.edit();
                    saveSettings.remove("mw");
                    saveSettings.putInt("mw",1);
                    saveSettings.apply();
                    woman.setChecked(true);
                    man.setChecked(false);
                    gender.setText("women");
                }
            }
        });

        Cancelmatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setmessegelikes.edit();
                    saveSettings.putBoolean("CancelMatch",true);
                    saveSettings.apply();
                    Cancelmatch.setChecked(true);
                }else {
                    Cancelmatch.setChecked(false);
                    SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setmessegelikes.edit();
                    saveSettings.remove("CancelMatch");
                    saveSettings.apply();
                }
            }
        });

        newMatchs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setmessegelikes.edit();
                    saveSettings.putBoolean("newMatch",true);
                    saveSettings.apply();
                    newMatchs.setChecked(true);
                }else {
                    newMatchs.setChecked(false);
                    SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setmessegelikes.edit();
                    saveSettings.remove("newMatch");
                    saveSettings.apply();
                }
            }
        });
        noMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setmessegelikes.edit();
                    saveSettings.putBoolean("noMsg",true);
                    saveSettings.apply();
                    noMsg.setChecked(true);
                }else {
                    noMsg.setChecked(false);
                    SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setmessegelikes.edit();
                    saveSettings.remove("noMsg");
                    saveSettings.apply();
                }
            }
        });

        messegelikes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setmessegelikes.edit();
                    saveSettings.putBoolean("melikes",true);
                    saveSettings.apply();
                    messegelikes.setChecked(true);
                }else {
                    messegelikes.setChecked(false);
                    SharedPreferences setmessegelikes = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = setmessegelikes.edit();
                    saveSettings.remove("melikes");
                    saveSettings.apply();
                }
            }
        });

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                age_rnge.setText(minValue + "-" + maxValue);

                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor saveSettings = auto.edit();
                saveSettings.putString("min", String.valueOf(minValue));
                saveSettings.putString("max", String.valueOf(maxValue));
                saveSettings.apply();
                //minValue, maxValue 스트링값으로 쉐어드에저장 상대나이범위를 조절한다.

                String x = String.valueOf(maxValue);
                int max = Integer.parseInt(x);
                String m = String.valueOf(minValue);
                int min = Integer.parseInt(m);
                rangeSeekBar.setSelectedMinValue(min);
                rangeSeekBar.setSelectedMaxValue(max);
            }
        });
        
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unlimit.isChecked()){
                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = auto.edit();
                    saveSettings.remove("unlimitDis");
                    saveSettings.putBoolean("unlimitDis",true);
                    saveSettings.apply();
                }else if(!unlimit.isChecked()){
                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = auto.edit();
                    saveSettings.remove("unlimitDis");
                    saveSettings.putBoolean("unlimitDis",false);
                    saveSettings.apply();

                }

                Intent back = new Intent(setting_profile.this, profile.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {

                Log.v("알림", "구글 LOGOUT");

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());

                alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(false)

                        .setPositiveButton("아니오",

                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int id) {

                                        // 아니오 클릭. dialog 닫기.
                                        dialog.cancel();

                                    }

                                }).setNegativeButton("네",

                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                // 네 클릭

                                // 로그아웃 함수 call

                                dialog.cancel();
                                SharedPreferences Hauto = getSharedPreferences("hobbies", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor hobbyeditor = Hauto.edit();
                                hobbyeditor.clear();
                                hobbyeditor.commit();

                                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = auto.edit();
                                editor.clear();
                                editor.commit();

                                mGoogleSignInClient.signOut();
                                firebaseAuth.getInstance().signOut();
                                Intent logout = new Intent(setting_profile.this, MainActivity.class);
                                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(logout);
                                finish();
                            }

                        });

                AlertDialog alert = alt_bld.create();


                // 대화창 클릭시 뒷 배경 어두워지는 것 막기

                //alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


                // 대화창 제목 설정

                alert.setTitle("로그아웃");


                // 대화창 아이콘 설정

                alert.setIcon(R.drawable.bee);


                // 대화창 배경 색 설정

                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255,62,79,92)));

                alert.show();


            }

        });


//        btn_revoke.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.v("알림", "구글 LOGOUT and 탈퇴");
//
//                AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());
//
//                alt_bld.setMessage("탈퇴 하시겠습니까?").setCancelable(false)
//
//                        .setPositiveButton("아니오",
//
//                                new DialogInterface.OnClickListener() {
//
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                        // 아니오 클릭. dialog 닫기.
//
//                                        dialog.cancel();
//
//
//
//                                    }
//
//                                }).setNegativeButton("네",
//
//                        new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                Intent logout2 = new Intent(setting_profile.this, MainActivity.class);
//                                logout2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                SharedPreferences Hauto = getSharedPreferences("hobbies", Activity.MODE_PRIVATE);
//                                SharedPreferences.Editor hobbyeditor = Hauto.edit();
//                                hobbyeditor.clear();
//                                hobbyeditor.commit();
//
//                                SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                                phone = getPhone.getString("phone",null);
//                                deletemember(phone);
//                                CancelAllMatch(phone);
//
//                                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = auto.edit();
//                                editor.clear();
//                                editor.commit();
//                                firebaseAuth.getCurrentUser().delete();
//                                mGoogleSignInClient.signOut();
//
//                                // 회원 제거 code ....
//                                startActivity(logout2);
//                                finish();
//
//                            }
//
//                        });
//
//                AlertDialog alert = alt_bld.create();
//
//
//                // 대화창 클릭시 뒷 배경 어두워지는 것 막기
//
//                //alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//
//
//                // 대화창 제목 설정
//
//                alert.setTitle("로그아웃");
//
//
//                // 대화창 아이콘 설정
//
//                alert.setIcon(R.drawable.bee);
//
//
//                // 대화창 배경 색 설정
//
//                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255,62,79,92)));
//
//                alert.show();
//
//                }
//
//        });

        btn_revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlog();
//                View view= LayoutInflater.from(setting_profile.this).inflate(R.layout.revoke_dialog,null);
//                final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(setting_profile.this);
//
//               EditText revoke = view.findViewById(R.id.et_revoke);
//                String RV = revoke.toString().trim();
//                view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        // 삭제 눌렸을 떄
//                        if(RV.equals("삭제")) {
//
//
//                            Intent logout2 = new Intent(setting_profile.this, MainActivity.class);
//                            logout2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            SharedPreferences Hauto = getSharedPreferences("hobbies", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor hobbyeditor = Hauto.edit();
//                            hobbyeditor.clear();
//                            hobbyeditor.commit();
//
//                            SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                            phone = getPhone.getString("phone", null);
//                            deletemember(phone);
//                            CancelAllMatch(phone);
//
//                            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = auto.edit();
//                            editor.clear();
//                            editor.commit();
//                            firebaseAuth.getCurrentUser().delete();
//                            mGoogleSignInClient.signOut();
//
//                            // 회원 제거 code ....
//                            startActivity(logout2);
//                            finish();
//                        }
//                    }
//                });
//
//                view.findViewById(R.id.btn_nope).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bt.dismiss();
//                        finish();
//                    }
//                });
//
//                bt.setContentView(view);
//                bt.show();

            }
        });

    }

    private void showlog() {
        customDialog.show();
        EditText revoke = customDialog.findViewById(R.id.et_revoke);

        Button noBtn = customDialog.findViewById(R.id.btn_nope);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        //제거
        customDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(revoke.getText().toString().equals("삭제")) {


                    Intent logout2 = new Intent(setting_profile.this, MainActivity.class);
                    logout2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences Hauto = getSharedPreferences("hobbies", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor hobbyeditor = Hauto.edit();
                    hobbyeditor.clear();
                    hobbyeditor.commit();

                    SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    phone = getPhone.getString("phone", null);
                    RoomNums = getPhone.getString("RoomNums",null);
                    deletemember(phone);
                    RemoveMatchdata(RoomNums);
                    RemoveChatdata(RoomNums);

                    CancelAllMatch(phone);

                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = auto.edit();
                    editor.clear();
                    editor.commit();
                    firebaseAuth.getCurrentUser().delete();
//                    mGoogleSignInClient.signOut();
                    revokeAccess();
                    // 회원 제거 code ....
                    startActivity(logout2);
                    customDialog.dismiss();
                    finish();
                }
                else
                {
                    Log.d(TAG, "입력 값 = " +revoke.getText().toString());

                }

            }
        });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        String minAge = auto.getString("min","18");
        String maxAge = auto.getString("max","50");
        Boolean likesisChecked = auto.getBoolean("melikes",false);
        Boolean newMatch = auto.getBoolean("newMatch",false);
        Boolean noMsgs = auto.getBoolean("noMsg",false);
        Boolean CancelMatch = auto.getBoolean("CancelMatch",false);
        Boolean unlimitD = auto.getBoolean("unlimitDis",false);
        int prog = auto.getInt("distance", 15);
        setg = auto.getInt("mw",2);

        age_rnge.setText(minAge + "-" + maxAge);

        // 나이범위 설정저장 불러오기

        String x = String.valueOf(maxAge);
        int max = Integer.parseInt(x);
        String m = String.valueOf(minAge);
        int min = Integer.parseInt(m);
        rangeSeekBar.setSelectedMinValue(min);
        rangeSeekBar.setSelectedMaxValue(max);

        // 거리 설정저장 불러오기
        distance_text.setText(prog + " Km");
        distance.setProgress(prog);

        //상대 거리 무제한

        if(unlimitD){
            unlimit.setChecked(true);
        } else {
            unlimit.setChecked(false);

        }

        // 좋아요메세지 안받기
        if(likesisChecked){
            messegelikes.setChecked(true);
        } else {
            messegelikes.setChecked(false);

        }
         // 커플매칭 fcm안받기
        if(newMatch){
            newMatchs.setChecked(true);
        } else {
            newMatchs.setChecked(false);

        }

        // 새로운메세지 fcm안받기
        if(noMsgs){
            noMsg.setChecked(true);
        } else {
            noMsg.setChecked(false);

        }
        
        // 매치취소 fcm안받기
        if(CancelMatch){
            Cancelmatch.setChecked(true);
        } else {
            Cancelmatch.setChecked(false);

        }

        // 상대성별 산택 고정한다.

        if(setg == 0){
            man.setChecked(true);
            woman.setChecked(false);
        } else if(setg == 1){

            woman.setChecked(true);
            man.setChecked(false);
        }
    }



//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
//        String minAge = auto.getString("min",null);
//        String maxAge = auto.getString("max",null);
//        int prog = auto.getInt("distance", 5);
//
//        age_rnge.setText(minAge + "-" + maxAge);
//
//        // 나이범위 설정저장 불러오기
//
//        String x = String.valueOf(maxAge);
//        int max = Integer.parseInt(x);
//        String m = String.valueOf(minAge);
//        int min = Integer.parseInt(m);
//        rangeSeekBar.setSelectedMinValue(min);
//        rangeSeekBar.setSelectedMaxValue(max);
//
//        // 거리 설정저장 불러오기
//        distance_text.setText(prog + " Km");
//        distance.setProgress(prog);
//    }

    /////////////////GetDate//////////////////////////////////
//    public void Getdate(String myphone) {
//
//        String TAG_JSON = "webnautes";
//        String TAG_MatchDate = "Mday";
//        String TAG_room = "room";
//
//        MdateAndRoom = new ArrayList<MdateAndRoom>();
//
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
//                        Log.e(TAG, "매치데이 수 = "+MdateAndRoom.size());
//                        Log.e(TAG, "방번호 = "+MdateAndRoom.get(i).getRoom());
//                        //////////////////////////////////////////////////////////
//
//                    }
//
//                    // 내번호와 비교해서 방번호 구한다음 어레이로 역어서 통신보낸후 해당 채팅방다 삭제한다
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        };
//        GetMatchDay add =new GetMatchDay(responseListener);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(add);
//    }

private void CancelAllMatch(String phone) {


    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null ) {


                    }else {

                        return;
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };


    CancelallMatch add= new CancelallMatch(phone ,responseListener);
    RequestQueue queue = Volley.newRequestQueue(this);
    queue.add(add);
    Intent back = new Intent(setting_profile.this, EditProfileActivity.class);
    startActivity(back);
    finish();
}


    private void RemoveChatdata(String Rnums) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null ) {



                    }else {

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        RemoveChatData add= new RemoveChatData(Rnums ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
        Intent back = new Intent(setting_profile.this, EditProfileActivity.class);
        startActivity(back);
        finish();
    }

    private void RemoveMatchdata(String Rnums) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null ) {



                    }else {

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        RemoveMatchDB add= new RemoveMatchDB(Rnums ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
        Intent back = new Intent(setting_profile.this, EditProfileActivity.class);
        startActivity(back);
        finish();
    }

    private void deletemember(String phone) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
//                    if (success.equals("1") && success != null ) {
//                        Toast.makeText(setting_profile.this, "정보업데이트 성공", Toast.LENGTH_SHORT).show();
//
//
//                    }else {
//                        Toast.makeText(setting_profile.this, "정보업데이트 실패", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        deleteMember add= new deleteMember(phone ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
        Intent back = new Intent(setting_profile.this, EditProfileActivity.class);
        startActivity(back);
        finish();
    }

    private void CancelMatch(String Myphone) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") ) {

                        try{

                            Log.d("매칭삭제응답 =", "매칭삭제");
//                            ref = database.getReference(roomnum);
//                            ref.removeValue();
//////////////////////////////////////////////////////////////////////////////////////////
//                            offchat offchat = new offchat("off");
//                            ref.child(roomnum+"/취소/"+Myphone).setValue(offchat);
/////////////////////////////////////////////////////////////////////////////////////////
//                            ref = database.getReference();
//                            LatestMsg latestMsg = new LatestMsg("매칭이 취소됬습니다..",Myphone);
//                            ref.child(roomnum+"/최신메세지").setValue(latestMsg);
//
//                            ref = database.getReference(roomnum+"/최신메세지상태");
//                            ref.removeValue();



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
        CancelallMatch add=new CancelallMatch(Myphone ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
    }
}
