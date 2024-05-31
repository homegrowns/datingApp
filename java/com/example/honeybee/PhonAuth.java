package com.example.honeybee;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.honeybee.databinding.ActivityPhonAuthBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import Profile.GetMyinfo;
import Utills.IPadress;
import Utills.SplashActivity;

public class PhonAuth extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;

    protected String phonenum;
    String phone;
    private ActivityPhonAuthBinding binding;

//    인증번호를 받지못하면 다시 인증번호를 전송하게된다
//the ForceResendingToken obtained from onCodeSent(String, PhoneAuthProvider.ForceResendingToken)
//    callback to force re-sending another verification SMS before the auto-retrieval timeout.
    private PhoneAuthProvider.ForceResendingToken ResendingTK;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String myVerificationID;
    String email;

    private static final String TAG = "Phone_TAG";

    private FirebaseAuth firebaseAuth;

    SharedPreferences sharedPreferences;

    private ProgressDialog pd;

//    private FusedLocationProviderClient fusedLocationClient;

    private TextView resultView ;
    private TextView tvresult;
    private String SelectedNum ="";
    private String SelectedNation ="";
    private String myJsonString;
    private CountDownTimer mCountDown;
    int se;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] country = {"-국가선택-", "한국", " 싱가포르", "홍콩"};
        binding =ActivityPhonAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        resultView = (TextView)findViewById(R.id.textView_main_result);

        binding.phoneLi.setVisibility(View.VISIBLE); // 핸드폰 레이아웃을 보여준다
        binding.codeLi.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        pd = new ProgressDialog(this);
        pd.setTitle("기다려주세요..");
        pd.setCancelable(true);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                // 이 콜백 함수는 2가지상황에 적용된다.
                // 일종의 자동로그인
//                이 메서드는 2가지 상황에서 호출됩니다.
//
//                 즉시 인증: 때에 따라서는 인증 코드를 보내거나 입력하지 않고 전화번호를 즉시 인증할 수 있습니다.
//                자동 검색: 일부 기기에서는 수신되는 인증 SMS를 Google Play 서비스가 자동으로 감지하여 사용자의 개입 없이 인증을 수행할 수 있습니다.
//                 일부 이동통신사에서는 이 기능을 제공하지 않을 수 있습니다. SMS 메시지 끝에 11자리 해시를 포함하는 SMS Retriever API를 사용합니다.
//                 두 경우 모두 사용자의 전화번호가 정상적으로 인증된 것이므로
//                콜백에 전달된 PhoneAuthCredential 객체를 사용하여 사용자를 로그인 처리할 수 있습니다.
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
             // 콜백함수가 입증되지 않은 요청에 적용됬을때 예를들어 전화번호가 검증되지 않았을시 호출된다.
                pd.dismiss();
                Toast.makeText(PhonAuth.this, "잘못된 전화번호포맷"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String VerificationID, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(VerificationID, ResendingTK);

//                선택사항) 이 메서드는 제공된 전화번호로 인증 코드가 SMS를 통해 전송된 후에 호출됩니다.
//                        이 메서드가 호출되면 대부분의 앱은 사용자에게 SMS 메시지로 받은 인증 코드를 입력하라는 UI를 표시합니다.
//                        !!이와 동시에 백그라운드에서 자동 인증이 진행될 수도 있습니다. !!
//                        사용자가 인증 코드를 입력하면 인증 코드와 이 메서드에 전달된 인증 ID를 사용하여 PhoneAuthCredential 객체를 만들고
//                         이 객체로 사용자를 로그인 처리할 수 있습니다. ex) VerifyPhoneWithCode(myVerificationID, code);
//                        앱에 따라서는 onCodeAutoRetrievalTimeOut이 호출될 때까지 기다린 후에 인증 코드 UI를 표시할 수도 있지만,
//                        이 방법은 권장되지 않습니다

                Log.d(TAG,"인증번호 전송됨"+ VerificationID);

//                Sender ID = VerificationID
                myVerificationID = VerificationID;
                ResendingTK = token;

                //번호입력란을 숨기고 인증번호 레이아웃을 띄운다.
                binding.phoneLi.setVisibility(View.GONE);
                binding.codeLi.setVisibility(View.VISIBLE);
                Toast.makeText(PhonAuth.this, "인증번호 전송됨", Toast.LENGTH_SHORT).show();

                pd.dismiss();
                binding.CodeNumberDescription.setText("인증번호를 입력해주세요");

                mCountDown =  new CountDownTimer(300000, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        binding.tvTimer.setVisibility(View.INVISIBLE);

                        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
                        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

                        // 분,초 단위로 변경
                        int min = (int) (millisUntilFinished / 100000 % 60);
                        String sec = String.valueOf(millisUntilFinished / 1000 % 60);

                        if(sec.length() == 3){
                            String second = sec.substring(1, 3);
                            se = Integer.parseInt(second);
                        }
                        else
                        {

                            se = Integer.parseInt(sec);
                        }

                        if(min == 0){
                        //    binding.tvTimer.setText(se+"초");

                        }
                        else
                        {
                            binding.tvTimer.setText(Integer.toString(min)+"분"+se+"초");

                        }
                    }


                    @Override
                    public void onFinish() {
                        binding.tvTimer.setVisibility(View.GONE);
                        binding.tvResend.setVisibility(View.VISIBLE);
//                        binding.etOTP.setVisibility(View.GONE);
                    }

                }.start();
            }
        };



        binding.tvSelectNation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(PhonAuth.this, SelectNation.class);
                PhonAuth.this.startActivity(reg);

            }
        });

//        binding.spinnerInternums.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String  phonenums = binding.spinnerInternums.getSelectedItem().toString();
//                Toast.makeText(PhonAuth.this, "선택된국가 번호 "+ phonenums, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        binding.btnCountinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // 010지우고 +8210붙이기
                phonenum = binding.etPhoneNumber.getText().toString().trim();
                if(phonenum.length() == 11){

               String numm = phonenum.substring(1, 11);
               phone = SelectedNum+numm;
                Toast.makeText(PhonAuth.this, "전화번호"+ phone, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "입력번호 ="+phone);
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(PhonAuth.this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    startPhoneNumberVerification(phone);

                 }
                }//..if
                else
                    {
                        Toast.makeText(PhonAuth.this, "올바른 전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();

                    }
            }
        });

         binding.tvResend.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 phonenum = binding.etPhoneNumber.getText().toString().trim();
                 String numm = phonenum.substring(1, 11);
                 String phone = SelectedNum+numm;
                 if(TextUtils.isEmpty(phone)){
                     Toast.makeText(PhonAuth.this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                 }else{
                     RecendVerificationcode(phone, ResendingTK);

                     //
                     binding.etOTP.setVisibility(View.VISIBLE);

                 }

             }
         });

         binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String code = binding.etOTP.getText().toString().trim();
                 if(TextUtils.isEmpty(code)){
                     Toast.makeText(PhonAuth.this, "인증번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                 }else{

                     VerifyPhoneWithCode(myVerificationID, code);


                 }
             }
         });

    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        Intent getin = getIntent();
        SelectedNum = getin.getStringExtra("번호");
        SelectedNation = getin.getStringExtra("나라");
//        Toast.makeText(PhonAuth.this, "번호.."+SelectedNum , Toast.LENGTH_SHORT).show();

        if(SelectedNum != null){
            binding.tvSelectNation.setText(SelectedNation+" "+SelectedNum);
        }
        else
        {
            SelectedNum = "+82";
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDown.cancel();
        try{
            mCountDown.cancel();
        } catch (Exception e) {}
        mCountDown = null;
        finish();
    }

    private void startPhoneNumberVerification(String phone) {
        pd.setMessage("전화번호를 인증합니다.");
        pd.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(mCallbacks)  // OnVerification State Changed Callbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void RecendVerificationcode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("인증번호를 재전송 합니다.");
        pd.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        //verifyPhoneNumber 휴대폰인증을 시작하는 함수
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void VerifyPhoneWithCode(String VerificationID, String code) {
        pd.setMessage("인증번호를 확인합니다.");
        pd.show();

        Log.d(TAG, "인증번호  : "+code);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationID, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pd.setMessage("로그인중");

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // 로그인 성공
                        pd.dismiss();

                        Log.d(TAG, "로그인 성공"+phone);
                        loadmamberInfo(phone);
                        Toast.makeText(PhonAuth.this, "로그인성공", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        pd.dismiss();
                        Log.d(TAG, "인증번호 오류 : "+e.getMessage());
                        Toast.makeText(PhonAuth.this, "인증번호가 틀렸습니니다..", Toast.LENGTH_SHORT).show();
                    }
                });

    }





    ////////////////////////////loadmamberInfo/////////////////////////////////////////////////
    public void loadmamberInfo(String myphone) {

        String TAG_JSON="webnautes";
        String TAG_phone="phone";
        String TAG_NAME = "name";


        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    myJsonString = jsonObject.getString(TAG_JSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "유저 로그인 정보 onResponse = "+myJsonString);
                if(myJsonString.equals("-1"))
                {
                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("phone", phone);
                    autoLogin.commit();

                    Intent reg = new Intent(PhonAuth.this, RegisterName.class);
                    reg.putExtra("email",email);
                    PhonAuth.this.startActivity(reg);
                    finish();

                    Toast.makeText(PhonAuth.this, "저장되지않은 계정", Toast.LENGTH_SHORT).show();
                }


                else
                    {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                        Log.d(TAG, "MatchedA 어레이갯수: d=" + jsonArray.length() + "개");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject item = jsonArray.getJSONObject(i);
                            String name = item.getString(TAG_NAME);
                            String phone = item.getString(TAG_phone);

                            if (name.length() > 0) {
                                int ProBar = 100;
                                //100%

                                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("phone", phone);
                                autoLogin.putInt("ProBar", ProBar);
                                autoLogin.commit();

                                Intent profile = new Intent(PhonAuth.this, SplashActivity.class);
                                profile.putExtra("phone", phone);
                                profile.putExtra("이름", name);
                                PhonAuth.this.startActivity(profile);
                                finish();

                                Toast.makeText(PhonAuth.this, name + "님 로그인", Toast.LENGTH_SHORT).show();

                            }
                        } // for.....

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
            }//..onResponse

        };
        GetMyinfo add = new GetMyinfo(myphone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }
}