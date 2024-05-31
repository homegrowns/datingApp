package Profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.honeybee.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Utills.IPadress;

public class chooseHobby extends AppCompatActivity {
    ImageButton back;
    FirebaseAuth firebaseAuth;
    private String myJsonString;
    String hobb;
    String[] splitText;
    String gatherH,phone,h1;
    String[] ho = new String[5];

    private TextView NumOfhobbies;

    boolean sportChecked,travelChecked,gameChecked,languageChecked,volunteerChecked,hikeChecked,constellationChecked,conversationChecked,INFPChecked,ESTJChecked,
        ENTJChecked,ISTJChecked,nexflixChecked,disneyChecked,petownerChecked,makingfirendChecked;

    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;

    private Button sport,travel,game,language,volunteer,hike,constellation,conversation,INFP,ESTJ,
    ENTJ,ISTJ,netflix,disney,petowner,makingfirend;

    String 운동,여행,게임,언어교환,봉사활동,산책,별자리,대화,infp,estj,
            entj,istj,넷플릭스,디즈니,애견인,친구만들기;


    List<String> hobbies = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_hobby);


        initWidgets();

        Intent gethobby = getIntent();
      String Hobbyarr = gethobby.getStringExtra("취미");
        gatherH = Hobbyarr.replace(" ", "");
        splitText = gatherH.split(",");


        List<String> list = Arrays.asList(splitText);

        for (int h = 0; h < list.size(); h++){
            if (null != list.get(h)) {
                ho[h] = list.get(h);
            }
        }

        for (int i = 0; i < 5; i++){
            if(ho[i] == null){
                ho[i] = " ";
            }
        }


        SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
//        sportChecked = preferences.getBoolean("운동",false);
//        travelChecked = preferences.getBoolean("여행",false);
        gameChecked = preferences.getBoolean("게임",false);
        languageChecked = preferences.getBoolean("언어",false);
        volunteerChecked = preferences.getBoolean("봉사",false);
        hikeChecked = preferences.getBoolean("산책",false);
        constellationChecked = preferences.getBoolean("별자리",false);
        conversationChecked = preferences.getBoolean("대화",false);
        INFPChecked = preferences.getBoolean("infp",false);
        ESTJChecked = preferences.getBoolean("estj",false);
        ENTJChecked = preferences.getBoolean("entj",false);
        ISTJChecked = preferences.getBoolean("istj",false);
        nexflixChecked = preferences.getBoolean("넷플릭스",false);
        disneyChecked = preferences.getBoolean("디즈니",false);
        petownerChecked = preferences.getBoolean("애견인",false);
        makingfirendChecked = preferences.getBoolean("친구",false);

        h1 = findhobbies("운동");
        if(h1 != null){
            sport.setAlpha(.5f);
            sport.setBackgroundColor(Color.GRAY);
            운동 = "운동";
            hobbies.add(운동);
        }else {
            sport.setBackgroundColor(Color.WHITE);
            sport.setAlpha(1.0f);
            hobbies.remove(운동);
        }
        h1 = findhobbies("여행");
        if(h1 != null){
            travel.setAlpha(.5f);
            travel.setBackgroundColor(Color.GRAY);
            여행 = "여행";
            hobbies.add(여행);
        }else {
            travel.setBackgroundColor(Color.WHITE);
            travel.setAlpha(1.0f);
            hobbies.remove(여행);
        }
        h1 = findhobbies("게임");
        if(h1 != null){
//                && list.get(0).equals("게임")&& list.get(1).equals("게임")&& list.get(2).equals("게임")&& list.get(3).equals("게임")&& list.get(4).equals("게임")){
            game.setAlpha(.5f);
            game.setBackgroundColor(Color.GRAY);
            게임 = "게임";
            hobbies.add(게임);
        }else {
            game.setBackgroundColor(Color.WHITE);
            game.setAlpha(1.0f);
            hobbies.remove(게임);
        }
        h1 = findhobbies("언어교환");
        if(h1 != null){
//                && list.get(0).equals("언어교환")&& list.get(1).equals("언어교환")&& list.get(2).equals("언어교환")&& list.get(3).equals("언어교환")&& list.get(4).equals("언어교환")){
            language.setAlpha(.5f);
            language.setBackgroundColor(Color.GRAY);
            언어교환 = "언어교환";
            hobbies.add(언어교환);
        }else {
            language.setBackgroundColor(Color.WHITE);
            language.setAlpha(1.0f);
            hobbies.remove(언어교환);
        }
        h1 = findhobbies("봉사활동");
        if(h1 != null){
            volunteer.setAlpha(.5f);
            volunteer.setBackgroundColor(Color.GRAY);
            봉사활동 = "봉사활동";
            hobbies.add(봉사활동);
        }else {
            volunteer.setBackgroundColor(Color.WHITE);
            volunteer.setAlpha(1.0f);
            hobbies.remove(봉사활동);
        }
        h1 = findhobbies("산책");
        if(h1 != null){
            hike.setAlpha(.5f);
            hike.setBackgroundColor(Color.GRAY);
            산책 = "산책";
            hobbies.add(산책);
        }else {
            hike.setBackgroundColor(Color.WHITE);
            hike.setAlpha(1.0f);
            hobbies.remove(산책);
        }
        h1 = findhobbies("별자리");
        if(h1 != null){
            constellation.setAlpha(.5f);
            constellation.setBackgroundColor(Color.GRAY);
            별자리 = "별자리";
            hobbies.add(별자리);
        }else {
            constellation.setBackgroundColor(Color.WHITE);
            constellation.setAlpha(1.0f);
            hobbies.remove(별자리);
        }
        h1 = findhobbies("대화");
        if(h1 != null){
            conversation.setAlpha(.5f);
            conversation.setBackgroundColor(Color.GRAY);
            대화 = "대화";
            hobbies.add(대화);
        }else {
            conversation.setBackgroundColor(Color.WHITE);
            conversation.setAlpha(1.0f);
            hobbies.remove(대화);
        }
        h1 = findhobbies("INFP");
        if(h1 != null){
            INFP.setAlpha(.5f);
            INFP.setBackgroundColor(Color.GRAY);
            infp = "INFP";
            hobbies.add(infp);
        }else {
            INFP.setBackgroundColor(Color.WHITE);
            INFP.setAlpha(1.0f);
            hobbies.remove(infp);
        }
        h1 = findhobbies("ESTJ");
        if(h1 != null){
            ESTJ.setAlpha(.5f);
            ESTJ.setBackgroundColor(Color.GRAY);
            estj = "ESTJ";
            hobbies.add(estj);
        }else {
            ESTJ.setBackgroundColor(Color.WHITE);
            ESTJ.setAlpha(1.0f);
            hobbies.remove(estj);
        }
        h1 = findhobbies("ENTJ");
        if(h1 != null){
            ENTJ.setAlpha(.5f);
            ENTJ.setBackgroundColor(Color.GRAY);
            entj = "ENTJ";
            hobbies.add(entj);
        }else {
            ENTJ.setBackgroundColor(Color.WHITE);
            ENTJ.setAlpha(1.0f);
            hobbies.remove(entj);
        }
        h1 = findhobbies("ISTJ");
        if(h1 != null){
            ISTJ.setAlpha(.5f);
            ISTJ.setBackgroundColor(Color.GRAY);
            istj = "ISTJ";
            hobbies.add(istj);
        }else {
            ISTJ.setBackgroundColor(Color.WHITE);
            ISTJ.setAlpha(1.0f);
            hobbies.remove(istj);
        }

        h1 = findhobbies("넷플릭스");
        if(h1 != null){
            netflix.setAlpha(.5f);
            netflix.setBackgroundColor(Color.GRAY);
            넷플릭스 = "넷플릭스";
            hobbies.add(넷플릭스);
        }else {
            netflix.setBackgroundColor(Color.WHITE);
            netflix.setAlpha(1.0f);
            hobbies.remove(넷플릭스);
        }

        h1 = findhobbies("디즈니");
        if(h1 != null){
            disney.setAlpha(.5f);
            disney.setBackgroundColor(Color.GRAY);
            디즈니 = "디즈니";
            hobbies.add(디즈니);
        }else {
            disney.setBackgroundColor(Color.WHITE);
            disney.setAlpha(1.0f);
            hobbies.remove(디즈니);
        }

        h1 = findhobbies("애견인");
        if(h1 != null){
            petowner.setAlpha(.5f);
            petowner.setBackgroundColor(Color.GRAY);
            애견인 = "애견인";
            hobbies.add(애견인);
        }else {
            petowner.setBackgroundColor(Color.WHITE);
            petowner.setAlpha(1.0f);
            hobbies.remove(애견인);
        }
        h1 = findhobbies("친구만들기");
        if(h1 != null){
            makingfirend.setAlpha(.5f);
            makingfirend.setBackgroundColor(Color.GRAY);
            친구만들기 = "친구만들기";
            hobbies.add(친구만들기);
        }else {
            makingfirend.setBackgroundColor(Color.WHITE);
            makingfirend.setAlpha(1.0f);
            hobbies.remove(친구만들기);
        }

//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser fireUser = firebaseAuth.getCurrentUser();
//        String phone = fireUser.getPhoneNumber();

        SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        phone = getPhone.getString("phone",null);

        back = (ImageButton) findViewById(R.id.btn_backinHobby);
        // 데이터 통신저장후 에딧프로필 액티비티로 넘어간다.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMYhobby(String.valueOf(hobbies),phone);



            }
        });
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
        NumOfhobbies = findViewById(R.id.numOfhobbies);

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

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    sportClicked();
                    if(hobbies.size() == 5 && sport.getAlpha() == 1.0f){
                        Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                    }
                    onStart();
            }
        });

        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    travelClicked();
                if(hobbies.size() == 5 && travel.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClicked();
                if(hobbies.size() == 5 && game.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageClicked();
                if(hobbies.size() == 5 && language.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volunteerClicked();
                if(hobbies.size() == 5 && volunteer.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        hike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hikeClicked();
                if(hobbies.size() == 5 && hike.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        constellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constellationClicked();
                if(hobbies.size() == 5 && constellation.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversationClicked();
                if(hobbies.size() == 5 && conversation.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        INFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INFPClicked();
                if(hobbies.size() == 5 && INFP.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        ESTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ESTJClicked();
                if(hobbies.size() == 5 && ESTJ.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        INFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INFPClicked();
                if(hobbies.size() == 5 && INFP.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        ENTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ENTJClicked();
                if(hobbies.size() == 5 && ENTJ.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        ISTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISTJClicked();
                if(hobbies.size() == 5 && ISTJ.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        netflix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nexflixClicked();
                if(hobbies.size() == 5 && netflix.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        disney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disneyClicked();
                if(hobbies.size() == 5 && disney.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        petowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petownerClicked();
                if(hobbies.size() == 5 && petowner.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

                }
                onStart();
            }
        });

        makingfirend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makingfirendClicked();
                if(hobbies.size() == 5 && makingfirend.getAlpha() == 1.0f){
                    Toast.makeText(chooseHobby.this, "관심사는 최대 5개까지만 선택가능 합니다.", Toast.LENGTH_SHORT).show();

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
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("운동");
            editor.apply();
        } else if(hobbies.size() < 5){
            sport.setBackgroundColor(Color.GRAY);
            sport.setAlpha(.5f);
            운동 = "운동";
            hobbies.add(운동);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("운동",true);
            editor.apply();
            editor.commit();
       }
    }

    public void travelClicked() {

        if (travel.getAlpha() == .5f) {
            travel.setAlpha(1.0f);
            travel.setBackgroundColor(Color.WHITE);
            hobbies.remove(여행);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("여행");
            editor.apply();
        } else if(hobbies.size() < 5){
            travel.setBackgroundColor(Color.GRAY);
            travel.setAlpha(.5f);
            여행 = "여행";
            hobbies.add(여행);

            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("여행",true);
            editor.apply();
            editor.commit();
        }
    }

    public void gameClicked() {

        if (game.getAlpha() == .5f) {
            game.setAlpha(1.0f);
            game.setBackgroundColor(Color.WHITE);
            hobbies.remove(게임);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("게임");
            editor.apply();
        } else if(hobbies.size() < 5){
            game.setBackgroundColor(Color.GRAY);
            game.setAlpha(.5f);
            게임 = "게임";
            hobbies.add(게임);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("게임",true);
            editor.apply();
            editor.commit();
        }
    }

    public void languageClicked() {

        if (language.getAlpha() == .5f) {
            language.setAlpha(1.0f);
            language.setBackgroundColor(Color.WHITE);
            hobbies.remove(언어교환);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("언어");
            editor.apply();

        } else if(hobbies.size() < 5){
            language.setBackgroundColor(Color.GRAY);
            language.setAlpha(.5f);
            언어교환 = "언어교환";
            hobbies.add(언어교환);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("언어",true);
            editor.apply();
            editor.commit();
        }
    }

    public void volunteerClicked() {

        if (volunteer.getAlpha() == .5f) {
            volunteer.setAlpha(1.0f);
            volunteer.setBackgroundColor(Color.WHITE);
            hobbies.remove(봉사활동);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("봉사");
            editor.apply();
        } else if(hobbies.size() < 5){
            volunteer.setBackgroundColor(Color.GRAY);
            volunteer.setAlpha(.5f);
            봉사활동 = "봉사활동";
            hobbies.add(봉사활동);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("봉사",true);
            editor.apply();
            editor.commit();
        }
    }

    public void  hikeClicked() {

        if (hike.getAlpha() == .5f) {
            hike.setAlpha(1.0f);
            hike.setBackgroundColor(Color.WHITE);
            hobbies.remove(산책);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("산책");
            editor.apply();
        } else if(hobbies.size() < 5){
            hike.setBackgroundColor(Color.GRAY);
            hike.setAlpha(.5f);
            산책 = "산책";
            hobbies.add(산책);
//            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("산책",true);
//            editor.apply();
//            editor.commit();
        }
    }

    public void  constellationClicked() {

        if (constellation.getAlpha() == .5f) {
            constellation.setAlpha(1.0f);
            constellation.setBackgroundColor(Color.WHITE);
            hobbies.remove(별자리);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("별자리");
            editor.apply();

        } else if(hobbies.size() < 5){
            constellation.setBackgroundColor(Color.GRAY);
            constellation.setAlpha(.5f);
            별자리 ="별자리";
            hobbies.add(별자리);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("별자리",true);
            editor.apply();
            editor.commit();
        }
    }

    public void  conversationClicked() {

        if (conversation.getAlpha() == .5f) {
            conversation.setAlpha(1.0f);
            conversation.setBackgroundColor(Color.WHITE);
            hobbies.remove(대화);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("대화");
            editor.apply();
        } else if(hobbies.size() < 5){
            conversation.setBackgroundColor(Color.GRAY);
            conversation.setAlpha(.5f);
            대화 = "대화";
            hobbies.add(대화);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("대화",true);
            editor.apply();
            editor.commit();
        }
    }

    public void  INFPClicked() {

        if (INFP.getAlpha() == .5f) {
            INFP.setAlpha(1.0f);
            INFP.setBackgroundColor(Color.WHITE);
            hobbies.remove(infp);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("infp");
            editor.apply();

        } else if(hobbies.size() < 5) {
            INFP.setBackgroundColor(Color.GRAY);
            INFP.setAlpha(.5f);
            infp = "INFP";
            hobbies.add(infp);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("infp",true);
            editor.apply();
            editor.commit();
        }
    }

    public void  ESTJClicked() {

        if (ESTJ.getAlpha() == .5f) {
            ESTJ.setAlpha(1.0f);
            ESTJ.setBackgroundColor(Color.WHITE);
            hobbies.remove(estj);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("estj");
            editor.apply();
        } else if(hobbies.size() < 5) {
            ESTJ.setBackgroundColor(Color.GRAY);
            ESTJ.setAlpha(.5f);
            estj = "ESTJ";
            hobbies.add(estj);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("estj",true);
            editor.apply();
            editor.commit();
        }
    }

    public void ISTJClicked() {

        if (ISTJ.getAlpha() == .5f) {
            ISTJ.setAlpha(1.0f);
            ISTJ.setBackgroundColor(Color.WHITE);
            hobbies.remove(istj);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("istj");
            editor.apply();

        } else if(hobbies.size() < 5) {
            ISTJ.setBackgroundColor(Color.GRAY);
            ISTJ.setAlpha(.5f);
            istj = "ISTJ";
            hobbies.add(istj);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("istj",true);
            editor.apply();
            editor.commit();
        }
    }

    public void ENTJClicked() {

        if (ENTJ.getAlpha() == .5f) {
            ENTJ.setAlpha(1.0f);
            ENTJ.setBackgroundColor(Color.WHITE);
            hobbies.remove(entj);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("entj");
            editor.apply();

        } else if(hobbies.size() < 5){
            ENTJ.setBackgroundColor(Color.GRAY);
            ENTJ.setAlpha(.5f);
            entj = "ENTJ";
            hobbies.add(entj);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("entj",true);
            editor.apply();
            editor.commit();
        }
    }

    public void nexflixClicked() {

        if (netflix.getAlpha() == .5f) {
            netflix.setAlpha(1.0f);
            netflix.setBackgroundColor(Color.WHITE);
            hobbies.remove(넷플릭스);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("넷플릭스");
            editor.apply();

        } else if(hobbies.size() < 5) {
            netflix.setBackgroundColor(Color.GRAY);
            netflix.setAlpha(.5f);
            넷플릭스 = "넷플릭스";
            hobbies.add(넷플릭스);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("넷플릭스",true);
            editor.apply();
            editor.commit();
        }
    }

    public void disneyClicked() {

        if (disney.getAlpha() == .5f) {
            disney.setAlpha(1.0f);
            disney.setBackgroundColor(Color.WHITE);
            hobbies.remove(디즈니);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("디즈니");
            editor.apply();

        } else if(hobbies.size() < 5){
            disney.setBackgroundColor(Color.GRAY);
            disney.setAlpha(.5f);
            디즈니 = "디즈니";
            hobbies.add(디즈니);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("디즈니",true);
            editor.apply();
            editor.commit();
        }
    }

    public void petownerClicked() {

        if (petowner.getAlpha() == .5f) {
            petowner.setAlpha(1.0f);
            petowner.setBackgroundColor(Color.WHITE);

            hobbies.remove(애견인);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("애견인");
            editor.apply();

        } else if(hobbies.size() < 5){
            petowner.setBackgroundColor(Color.GRAY);
            petowner.setAlpha(.5f);
            애견인 = "애견인";
            hobbies.add(애견인);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("애견인",true);
            editor.apply();
            editor.commit();
        }
    }

    public void makingfirendClicked() {

        if (makingfirend.getAlpha() == .5f) {
            makingfirend.setAlpha(1.0f);
            makingfirend.setBackgroundColor(Color.WHITE);

            hobbies.remove(친구만들기);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("친구");
            editor.apply();

        } else if(hobbies.size() < 5){
            makingfirend.setBackgroundColor(Color.GRAY);
            makingfirend.setAlpha(.5f);
            친구만들기 = "친구만들기";
            hobbies.add(친구만들기);
            SharedPreferences preferences = getSharedPreferences( "hobbies" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("친구",true);
            editor.apply();
            editor.commit();
        }
    }

    private void saveMYhobby(String hobbies, String phone) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null ) {
                        Toast.makeText(chooseHobby.this, "정보업데이트 성공", Toast.LENGTH_SHORT).show();


                    }else {
                        Toast.makeText(chooseHobby.this, "정보업데이트 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        insertHobbies add= new insertHobbies(hobbies, phone ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
        Intent back = new Intent(chooseHobby.this, EditProfileActivity.class);
        startActivity(back);
        finish();
    }

    public String findhobbies(String h) {
        for (int z = 0; z < ho.length; z++){
            if (ho[z].equals(h)) {
                hobb = ho[z];
                return hobb;
            }
        }
        return null;

    }

}