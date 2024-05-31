package com.example.honeybee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RegisterHobby extends AppCompatActivity {
    private static final String TAG = "RegisterHobby";

    ProgressBar pb;
    Button hobbiesContinueButton;
    private TextView NumOfhobbies;
    private Button sport,travel,game,language,volunteer,hike,constellation,conversation,INFP,ESTJ,
            ENTJ,ISTJ,netflix,disney,petowner,makingfirend;

    String 운동,여행,게임,언어교환,봉사활동,산책,별자리,대화,infp,estj,
            entj,istj,넷플릭스,디즈니,애견인,친구만들기;


    List<String> hobbies = new ArrayList<String>();

    private Context mContext;

    String age;
    String email;
    String name;
    String phone;
    int gender;

    private String append = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hobby);
        mContext = RegisterHobby.this;

        Log.d(TAG, "onCreate: started");

        Intent info = getIntent();
        name = info.getStringExtra("이름");
        phone = info.getStringExtra("phone");
        age = info.getStringExtra("나이");
        gender = info.getIntExtra("성별",0);
        email = info.getStringExtra("email");

        initWidgets();

        init();

        updateProgress();
    }

    private void updateProgress() {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor probarRemove = auto.edit();
        probarRemove.remove("ProBar");
        probarRemove.commit();

        int ProBar = pb.getProgress();
        //60%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }

    private void initWidgets() {
        sport = findViewById(R.id.btn_sport);
        travel = findViewById(R.id.btn_travel);
        game = findViewById(R.id.btn_game);
        language = findViewById(R.id.btn_language);
        volunteer = findViewById(R.id.btn_volunteer);
        hike = findViewById(R.id.btn_hike);
        constellation = findViewById(R.id.btn_constellation);
        conversation = findViewById(R.id.btn_conversation);
        INFP = findViewById(R.id.btn_INFP);
        ESTJ = findViewById(R.id.btn_ESTJ);
        ENTJ = findViewById(R.id.btn_ENTJ);
        ISTJ = findViewById(R.id.btn_ISTJ);
        netflix = findViewById(R.id.btn_netflix);
        disney = findViewById(R.id.btn_disney);
        petowner = findViewById(R.id.btn_petowner);
        makingfirend = findViewById(R.id.btn_makingfriend);
        NumOfhobbies = findViewById(R.id.numOfhobbies1);
        pb = findViewById(R.id.progressBarHobby);

        sport.setAlpha(1.0f);
        sport.setBackgroundColor(Color.WHITE);

        travel.setAlpha(1.0f);
        travel.setBackgroundColor(Color.WHITE);

        game.setAlpha(1.0f);
        game.setBackgroundColor(Color.WHITE);

        language.setAlpha(1.0f);
        language.setBackgroundColor(Color.WHITE);

        volunteer.setAlpha(1.0f);
        volunteer.setBackgroundColor(Color.WHITE);

        hike.setAlpha(1.0f);
        hike.setBackgroundColor(Color.WHITE);

        constellation.setAlpha(1.0f);
        constellation.setBackgroundColor(Color.WHITE);

        conversation.setAlpha(1.0f);
        conversation.setBackgroundColor(Color.WHITE);

        INFP.setAlpha(1.0f);
        INFP.setBackgroundColor(Color.WHITE);

        ESTJ.setAlpha(1.0f);
        ESTJ.setBackgroundColor(Color.WHITE);

        ENTJ.setAlpha(1.0f);
        ENTJ.setBackgroundColor(Color.WHITE);

        ISTJ.setAlpha(1.0f);
        ISTJ.setBackgroundColor(Color.WHITE);

        netflix.setAlpha(1.0f);
        netflix.setBackgroundColor(Color.WHITE);

        disney.setAlpha(1.0f);
        disney.setBackgroundColor(Color.WHITE);

        petowner.setAlpha(1.0f);
        petowner.setBackgroundColor(Color.WHITE);

        makingfirend.setAlpha(1.0f);
        makingfirend.setBackgroundColor(Color.WHITE);

        hobbiesContinueButton = findViewById(R.id.hobbiesContinueButton);

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sportClicked();
                if(hobbies.size() == 5 && sport.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travelClicked();
                if(hobbies.size() == 5 && travel.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();

            }
        });

        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClicked();

                if(hobbies.size() == 5 && game.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageClicked();
                if(hobbies.size() == 5 && language.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volunteerClicked();
                if(hobbies.size() == 5 && volunteer.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        hike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hikeClicked();
                if(hobbies.size() == 5 && hike.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        constellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constellationClicked();
                if(hobbies.size() == 5 && constellation.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversationClicked();
                if(hobbies.size() == 5 && conversation.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        INFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INFPClicked();
                if(hobbies.size() == 5 && INFP.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        ESTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ESTJClicked();
                if(hobbies.size() == 5 && ESTJ.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        INFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INFPClicked();
                if(hobbies.size() == 5 && INFP.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        ENTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ENTJClicked();
                if(hobbies.size() == 5 && ESTJ.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        ISTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISTJClicked();
                if(hobbies.size() == 5 && ISTJ.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        netflix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nexflixClicked();
                if(hobbies.size() == 5 && netflix.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        disney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disneyClicked();
                if(hobbies.size() == 5 && disney.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        petowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petownerClicked();
                if(hobbies.size() == 5 && petowner.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        makingfirend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makingfirendClicked();
                if(hobbies.size() == 5 && makingfirend.getAlpha() == 1.0f){
                    Toast.makeText(RegisterHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        String ho = String.valueOf(hobbies.size());
        NumOfhobbies.setText(ho);
    }


    public void sportClicked() {

        if (sport.getAlpha() == .5f) {
            sport.setAlpha(1.0f);
            sport.setBackgroundColor(Color.WHITE);
            hobbies.remove(운동);

        } else if(hobbies.size() < 5){
            sport.setBackgroundColor(Color.GRAY);
            sport.setAlpha(0.5f);
            운동 = "운동";
            hobbies.add(운동);

        }
    }

    public void travelClicked() {

        if (travel.getAlpha() == .5f) {
            travel.setAlpha(1.0f);
            travel.setBackgroundColor(Color.WHITE);
            hobbies.remove(여행);

        } else if(hobbies.size() < 5){
            travel.setBackgroundColor(Color.GRAY);
            travel.setAlpha(0.5f);
            여행 = "여행";
            hobbies.add(여행);

        }
    }

    public void gameClicked() {

        if (game.getAlpha() == .5f) {
            game.setAlpha(1.0f);
            game.setBackgroundColor(Color.WHITE);
            hobbies.remove(게임);

        } else if(hobbies.size() < 5){
            game.setBackgroundColor(Color.GRAY);
            game.setAlpha(0.5f);
            게임 = "게임";
            hobbies.add(게임);

        }
    }

    public void languageClicked() {

        if (language.getAlpha() == .5f) {
            language.setAlpha(1.0f);
            language.setBackgroundColor(Color.WHITE);
            hobbies.remove(언어교환);


        } else if(hobbies.size() < 5){
            language.setBackgroundColor(Color.GRAY);
            language.setAlpha(.5f);
            언어교환 = "언어교환";
            hobbies.add(언어교환);

        }
    }

    public void volunteerClicked() {

        if (volunteer.getAlpha() == .5f) {
            volunteer.setAlpha(1.0f);
            volunteer.setBackgroundColor(Color.WHITE);
            hobbies.remove(봉사활동);

        } else if(hobbies.size() < 5){
            volunteer.setBackgroundColor(Color.GRAY);
            volunteer.setAlpha(.5f);
            봉사활동 = "봉사활동";
            hobbies.add(봉사활동);

        }
    }

    public void  hikeClicked() {

        if (hike.getAlpha() == .5f) {
            hike.setAlpha(1.0f);
            hike.setBackgroundColor(Color.WHITE);
            hobbies.remove(산책);

        } else if(hobbies.size() < 5){
            hike.setBackgroundColor(Color.GRAY);
            hike.setAlpha(.5f);
            산책 = "산책";
            hobbies.add(산책);

        }
    }

    public void  constellationClicked() {

        if (constellation.getAlpha() == .5f) {
            constellation.setAlpha(1.0f);
            constellation.setBackgroundColor(Color.WHITE);
            hobbies.remove(별자리);


        } else if(hobbies.size() < 5){
            constellation.setBackgroundColor(Color.GRAY);
            constellation.setAlpha(.5f);
            별자리 ="별자리";
            hobbies.add(별자리);

        }
    }

    public void  conversationClicked() {

        if (conversation.getAlpha() == .5f) {
            conversation.setAlpha(1.0f);
            conversation.setBackgroundColor(Color.WHITE);
            hobbies.remove(대화);

        } else if(hobbies.size() < 5){
            conversation.setBackgroundColor(Color.GRAY);
            conversation.setAlpha(.5f);
            대화 = "대화";
            hobbies.add(대화);

        }
    }

    public void  INFPClicked() {

        if (INFP.getAlpha() == .5f) {
            INFP.setAlpha(1.0f);
            INFP.setBackgroundColor(Color.WHITE);
            hobbies.remove(infp);


        } else if(hobbies.size() < 5) {
            INFP.setBackgroundColor(Color.GRAY);
            INFP.setAlpha(.5f);
            infp = "INFP";
            hobbies.add(infp);

        }
    }

    public void  ESTJClicked() {

        if (ESTJ.getAlpha() == .5f) {
            ESTJ.setAlpha(1.0f);
            ESTJ.setBackgroundColor(Color.WHITE);
            hobbies.remove(estj);

        } else if(hobbies.size() < 5) {
            ESTJ.setBackgroundColor(Color.GRAY);
            ESTJ.setAlpha(.5f);
            estj = "ESTJ";
            hobbies.add(estj);

        }
    }

    public void ISTJClicked() {

        if (ISTJ.getAlpha() == .5f) {
            ISTJ.setAlpha(1.0f);
            ISTJ.setBackgroundColor(Color.WHITE);
            hobbies.remove(istj);


        } else {
            ISTJ.setBackgroundColor(Color.GRAY);
            ISTJ.setAlpha(.5f);
            istj = "ISTJ";
            hobbies.add(istj);

        }
    }

    public void ENTJClicked() {

        if (ENTJ.getAlpha() == .5f) {
            ENTJ.setAlpha(1.0f);
            ENTJ.setBackgroundColor(Color.WHITE);
            hobbies.remove(entj);


        } else if(hobbies.size() < 5){
            ENTJ.setBackgroundColor(Color.GRAY);
            ENTJ.setAlpha(.5f);
            entj = "ENTJ";
            hobbies.add(entj);

        }
    }

    public void nexflixClicked() {

        if (netflix.getAlpha() == .5f) {
            netflix.setAlpha(1.0f);
            netflix.setBackgroundColor(Color.WHITE);
            hobbies.remove(넷플릭스);


        } else if(hobbies.size() < 5) {
            netflix.setBackgroundColor(Color.GRAY);
            netflix.setAlpha(.5f);
            넷플릭스 = "넷플릭스";
            hobbies.add(넷플릭스);

        }
    }

    public void disneyClicked() {

        if (disney.getAlpha() == .5f) {
            disney.setAlpha(1.0f);
            disney.setBackgroundColor(Color.WHITE);
            hobbies.remove(디즈니);

        } else if(hobbies.size() < 5){
            disney.setBackgroundColor(Color.GRAY);
            disney.setAlpha(.5f);
            디즈니 = "디즈니";
            hobbies.add(디즈니);

        }
    }

    public void petownerClicked() {

        if (petowner.getAlpha() == .5f) {
            petowner.setAlpha(1.0f);
            petowner.setBackgroundColor(Color.WHITE);
            hobbies.remove(애견인);


        } else if(hobbies.size() < 5){
            petowner.setBackgroundColor(Color.GRAY);
            petowner.setAlpha(.5f);
            애견인 = "애견인";
            hobbies.add(애견인);
        }
    }

    public void makingfirendClicked() {

        if (makingfirend.getAlpha() == .5f) {
            makingfirend.setAlpha(1.0f);
            makingfirend.setBackgroundColor(Color.WHITE);
            hobbies.remove(친구만들기);


        } else if(hobbies.size() < 5){
            makingfirend.setBackgroundColor(Color.GRAY);
            makingfirend.setAlpha(.5f);
            친구만들기 = "친구만들기";
            hobbies.add(친구만들기);

        }
    }



    public void init() {
        hobbiesContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("취미", String.valueOf(hobbies));
                autoLogin.commit();

                Intent intent = new Intent(RegisterHobby.this, RegisterSchool.class);
                intent.putExtra("성별", gender);
                intent.putExtra("phone", phone);
                intent.putExtra("이름", name);
                intent.putExtra("나이", age);
                intent.putExtra("email",email);
                intent.putExtra("취미", String.valueOf(hobbies));
                startActivity(intent);
                finish();

            }
        });
    }


    //----------------------------------------Firebase----------------------------------------


}