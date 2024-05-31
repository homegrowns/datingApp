package Profile;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.honeybee.R;
import com.example.honeybee.deleteImg;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Utills.IPadress;


public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    DragLinearLayout dragLinearLayout;
    RecyclerView recyclerView1;
    MultiImageAdapter multiImageAdapter;
    ArrayList items;
    Uri[] imgs = new Uri[6];
    Uri im;
    Uri im2;
    Uri im3;
    Uri im4;
    Uri im5;
    Uri im6;

    String HobbiesReplaced;

    private String myJsonString;

    public static final int getimagesetting=1001;//for request intent
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;

    String name;
    String img;
    String img2;
    String img3;
    String img4;
    String img5;
    String img6;
    String intro;
    String gender;
    String age;
    String mjob;
    String mworkpalce;
    String hobbies;
    String mschool;
    String myRecentlocation=" ", location = " ";
    String phone;
    String myintro;
    String Myworkplace;
    String Myjob;
    String Myschool;

    private RadioGroup radioGroup;
    RadioButton man, woman;
    ImageButton back;
    private EditProfileActivity.phpgetinfo task;

    EditText myIntro, myjob, myworkplace, myschool;
    TextView hobby,mylocation;
    SwitchCompat Hideage, Hidedis;
    private ProgressDialog dialog;
    private Context mContext = EditProfileActivity.this;
    boolean Manchecked;
    boolean Womanchecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        tedPermission();

        SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        phone = getPhone.getString("phone",null);

        dragLinearLayout = (DragLinearLayout) findViewById(R.id.container);
        recyclerView1 = findViewById(R.id.recycler_edit);
        mylocation = (TextView) findViewById(R.id.tv_locatin);
        myschool = (EditText) findViewById(R.id.et_school);
        myworkplace = (EditText) findViewById(R.id.et_workplace);
        myjob = (EditText) findViewById(R.id.et_job);
        myIntro = (EditText) findViewById(R.id.et_myintro);
        hobby = (TextView) findViewById(R.id.tv_hobby);
        back = (ImageButton) findViewById(R.id.btn_back);

        Hideage = findViewById(R.id.btn_hideAge);
        Hidedis = findViewById(R.id.btn_hideDistance);

        man = (RadioButton) findViewById(R.id.rbtn_man);
        woman = (RadioButton) findViewById(R.id.rbtn_woman);


        radioGroup = (RadioGroup) findViewById(R.id.rg_gender);

        Manchecked = man.isChecked();
        Womanchecked = woman.isChecked();

       // task = new phpgetinfo();
       // task.execute(IP_ADDRESS + "Getname.php", phone);

        im =  Uri.parse(IP_ADDRESS+img);
        im2 =  Uri.parse(IP_ADDRESS+img2);
        im3 =  Uri.parse(IP_ADDRESS+img3);
        im4 =  Uri.parse(IP_ADDRESS+img4);
        im5 =  Uri.parse(IP_ADDRESS+img5);
        im6 =  Uri.parse(IP_ADDRESS+img6);

        // Hidage 통신으로 자료불러와서 설정해야 로그아웃후 로그인할때 설정유지된다.
        Hideage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                            Hideage.setChecked(true);
                    SharedPreferences set = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = set.edit();
                    saveSettings.putBoolean("hideage",true);
                    saveSettings.apply();

                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    String phone = auto.getString("phone",null);
                    hidemyage("h",phone);


                }else if(!isChecked){

                    Hideage.setChecked(false);
                    SharedPreferences set = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = set.edit();
                    saveSettings.remove("hideage");
                    saveSettings.apply();

                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    String phone = auto.getString("phone",null);
                    hidemyage("n",phone);
                }
            }
        });

        Hidedis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    Hidedis.setChecked(true);
                    SharedPreferences set = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = set.edit();
                    saveSettings.putBoolean("거리숨기기",true);
                    saveSettings.apply();
                    
                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    String phone = auto.getString("phone",null);

                    setdis("","",phone);

                }else {
                    Hidedis.setChecked(false);
                    SharedPreferences set = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor saveSettings = set.edit();
                    saveSettings.putBoolean("거리숨기기",false);
                    saveSettings.apply();
                    
                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    String phone = auto.getString("phone",null);
                    String lati = auto.getString("lati",null);
                    String longit = auto.getString("longit",null);
                    setdis(lati,longit,phone);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rbtn_man :
                        gender = "1";
                    break;
                    case R.id.rbtn_woman :
                        gender = "0";
                        break;
                  default:
                    break;
                }
            }
        });




        hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHobby = new Intent(EditProfileActivity.this, chooseHobby.class);
                goHobby.putExtra("취미",HobbiesReplaced);
                startActivity(goHobby);
            }
        });

        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myintro = myIntro.getText().toString().trim();
                Myjob = myjob.getText().toString().trim();
                Myworkplace = myworkplace.getText().toString().trim();
                Myschool = myschool.getText().toString().trim();
                if(myintro.equals("")){

                    SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    phone = getPhone.getString("phone",null);

                    savemyInfo(intro ,gender,Myjob,Myworkplace,Myschool,myRecentlocation,phone);
                } else {
                    savemyInfo(myintro, gender,Myjob,Myworkplace, Myschool,myRecentlocation,phone);
                }

                Intent golocation = new Intent(EditProfileActivity.this, getLocation.class);
                startActivity(golocation);
            }
        });

        // 데이터 통신저장후 프로필 액티비티로 넘어간다.
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myintro = myIntro.getText().toString().trim();
                Myjob = myjob.getText().toString().trim();
                Myworkplace = myworkplace.getText().toString().trim();
                Myschool = myschool.getText().toString().trim();
                if(myintro.equals("")){

                    SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    phone = getPhone.getString("phone",null);

                    savemyInfo(intro ,gender,Myjob,Myworkplace,Myschool,myRecentlocation,phone);
                } else {
                    savemyInfo(myintro, gender,Myjob,Myworkplace, Myschool,myRecentlocation,phone);
                }

                //        // 데이터 통신저장후 프로필 액티비티로 넘어간다.

                Intent back = new Intent(EditProfileActivity.this, profile.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
                finish();
            }
        });

    }


    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }


            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }



    private void proceedAfterPermission() {

        Intent intent = new Intent(mContext, SetImage.class);
        mContext.startActivity(intent);
        startActivityForResult(intent, getimagesetting);



    }

//    private void proceedAfterPermission() {
//
//
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
//
//        builder.setTitle("Add Photo!");
//
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//
//            @Override
//
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (options[item].equals("Take Photo"))
//
//                {
//
//                    cameraIntent();
//
//                } else if (options[item].equals("Choose from Gallery"))
//
//                {
//
//                    galleryIntent();
//
//
//                } else if (options[item].equals("Cancel")) {
//
//                    dialog.dismiss();
//
//                }
//
//            }
//
//        });
//
//        builder.show();
//
//    }

//    오리지날
//    private void galleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(intent, gallery);
//    }
//
//    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, camera);
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_PERMISSION_SETTING) {
//            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == gallery)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == camera)
//                onCaptureImageResult(data);
//        }
//    }
//
//    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        imageView.setImageBitmap(thumbnail);
//    }
//
//    @SuppressWarnings("deprecation")
//    private void onSelectFromGalleryResult(Intent data) {
//
//        Bitmap bm = null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        imageView.setImageBitmap(bm);
//    }
// 오리지날




//    private String imageToString( Bitmap bitmap) {
//        ByteArrayOutputStream Stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,60,Stream);
//        byte[] imgBytes = Stream.toByteArray();
//        String image = Base64.encodeToString(imgBytes,Base64.DEFAULT);
//
//        return image;
//    }




    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        Boolean hideDisisChecked = getPhone.getBoolean("거리숨기기",false);
        Boolean hideage = getPhone.getBoolean("hideage",false);

        loadmamberInfo(phone);


        if(hideage){
            Hideage.setChecked(true);
        }else {
            Hideage.setChecked(false);

        }

        if(hideDisisChecked){
            Hidedis.setChecked(true);
        }else {
            Hidedis.setChecked(false);

        }

    }



    // 여기서 통신저장을한다.
    private void savemyInfo(String myintro, String gender,String myjob, String myWorkplace, String myschool, String location, String phone) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null ) {
                        Log.d("imgtoDB", "정보업데이트 성공");


                    }else {
                        Log.d("imgtoDB", "정보업데이트 실패");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        personalInfosave add=new personalInfosave(myintro, gender,myjob,myWorkplace, myschool, location, phone ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);
        Intent back = new Intent(EditProfileActivity.this, profile.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getimagesetting) {    //if image change

            if (resultCode == RESULT_OK) {
                Bitmap selPhoto = null;
                selPhoto = (Bitmap) data.getParcelableExtra("bitmap");



            }
        }
    }



    class phpgetinfo extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);


            if(TextUtils.isEmpty(result)){
                Log.d("프로필정보받기", "정보업데이트 실패");

            }else {
                myJsonString = result;
                showResult();
            }

        }



        @Override
        protected String doInBackground(String... params) {

            String phone1 = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "phone="+phone1;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    private void showResult() { // 발리써서 삭제각

        String TAG_JSON="webnautes";
        String TAG_phone="phone";
        String TAG_NAME = "name";
        String TAG_AGE = "age";
        String TAG_IMG = "img";
        String TAG_gender = "gender";
        String TAG_IMG2 = "img2";
        String TAG_IMG3 = "img3";
        String TAG_IMG4 = "img4";
        String TAG_IMG5 = "img5";
        String TAG_IMG6 = "img6";
        String TAG_Intro = "intro";
        String TAG_myjob = "job";
        String TAG_workplace = "workplace";
        String TAG_hobbies = "hobbies";
        String TAG_school = "school";
        String TAG_loca = "location";


        try {
            JSONObject jsonObject = new JSONObject(myJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject item = jsonArray.getJSONObject(i);


                name = item.getString(TAG_NAME);
                age = item.getString(TAG_AGE);
                img = item.getString(TAG_IMG);
                gender = item.getString(TAG_gender);
                img2 = item.getString(TAG_IMG2);
                img3 = item.getString(TAG_IMG3);
                img4 = item.getString(TAG_IMG4);
                img5 = item.getString(TAG_IMG5);
                img6 = item.getString(TAG_IMG6);
                intro = item.getString(TAG_Intro);
                mjob = item.getString(TAG_myjob);
                mworkpalce = item.getString(TAG_workplace);
                hobbies = item.getString(TAG_hobbies);
                mschool = item.getString(TAG_school);
                location = item.getString(TAG_loca);

                im =  Uri.parse(img);
                im2 =  Uri.parse(img2);
                im3 =  Uri.parse(img3);
                im4 =  Uri.parse(img4);
                im5 =  Uri.parse(img5);
                im6 =  Uri.parse(img6);


                imgs[0] = im;
                imgs[1] = im2;
                imgs[2] = im3;
                imgs[3] = im4;
                imgs[4] = im5;
                imgs[5] = im6;

                for (int z = 0; z < 6 ; z++){
                    items.add(z,imgs[z]);
                }


                if(Integer.parseInt(gender) == 1){
                    man.setChecked(true);
                }else {
                    woman.setChecked(true);
                }


                myIntro.setText(intro);
                myjob.setText(mjob);
                myworkplace.setText(mworkpalce);
                myschool.setText(mschool);

                if(myRecentlocation == " "){
                    mylocation.setText(location);
                }
                SharedPreferences location = getSharedPreferences( "hobbies" , MODE_PRIVATE);
                myRecentlocation = location.getString("지역","");
                mylocation.setText(myRecentlocation);

                String hobbiesReplaced = hobbies.replace("["," ").trim();
                HobbiesReplaced= hobbiesReplaced.replace("]"," ").trim();

                hobby.setText(HobbiesReplaced);


            }

            multiImageAdapter = new MultiImageAdapter(items,EditProfileActivity.this, phone);
            recyclerView1.setLayoutManager(new GridLayoutManager(EditProfileActivity.this,3));

            //item drag&drop, swipe 설정
            ItemTouchHelper.Callback callback = new ItemMoveCallback(multiImageAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            recyclerView1.setAdapter(multiImageAdapter);
            touchHelper.attachToRecyclerView(recyclerView1);
            recyclerView1.setHasFixedSize(true);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }


    ////////////////////////////loadmamberInfo/////////////////////////////////////////////////
    public void loadmamberInfo(String myphone) {


        String TAG_JSON="webnautes";
        String TAG_NAME = "name";
        String TAG_AGE = "age";
        String TAG_IMG = "img";
        String TAG_gender = "gender";
        String TAG_IMG2 = "img2";
        String TAG_IMG3 = "img3";
        String TAG_IMG4 = "img4";
        String TAG_IMG5 = "img5";
        String TAG_IMG6 = "img6";
        String TAG_likes = "likes";
        String TAG_lati = "lati";
        String TAG_longti = "longit";
        String TAG_dislikes = "dislikes";
        String TAG_school = "school";
        String TAG_Intro = "intro";
        String TAG_job = "job";
        String TAG_work = "workplace";
        String TAG_hobby = "hobbies";
        String TAG_location = "location";


        items = new ArrayList<>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "MatchedA 어레이갯수: d=" + jsonArray.length() + "개");

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject item = jsonArray.getJSONObject(i);


                        name = item.getString(TAG_NAME);
                        age = item.getString(TAG_AGE);
                        img = item.getString(TAG_IMG);
                        gender = item.getString(TAG_gender);
                        img2 = item.getString(TAG_IMG2);
                        img3 = item.getString(TAG_IMG3);
                        img4 = item.getString(TAG_IMG4);
                        img5 = item.getString(TAG_IMG5);
                        img6 = item.getString(TAG_IMG6);
                        intro = item.getString(TAG_Intro);
                        mjob = item.getString(TAG_job);
                        mworkpalce = item.getString(TAG_work);
                        hobbies = item.getString(TAG_hobby);
                        mschool = item.getString(TAG_school);
                        location = item.getString(TAG_location);

                        im =  Uri.parse(img);
                        im2 =  Uri.parse(img2);
                        im3 =  Uri.parse(img3);
                        im4 =  Uri.parse(img4);
                        im5 =  Uri.parse(img5);
                        im6 =  Uri.parse(img6);


                        imgs[0] = im;
                        imgs[1] = im2;
                        imgs[2] = im3;
                        imgs[3] = im4;
                        imgs[4] = im5;
                        imgs[5] = im6;

                        for (int z = 0; z < 6 ; z++){
                            items.add(z,imgs[z]);
                        }


                        if(Integer.parseInt(gender) == 1){
                            man.setChecked(true);
                        }else {
                            woman.setChecked(true);
                        }


                        myIntro.setText(intro);
                        myjob.setText(mjob);
                        myworkplace.setText(mworkpalce);
                        myschool.setText(mschool);

                        if(myRecentlocation == " "){
                            mylocation.setText(location);
                        }
                        SharedPreferences location = getSharedPreferences( "hobbies" , MODE_PRIVATE);
                        myRecentlocation = location.getString("지역","");
                        mylocation.setText(myRecentlocation);

                        String hobbiesReplaced = hobbies.replace("["," ").trim();
                        HobbiesReplaced= hobbiesReplaced.replace("]"," ").trim();

                        hobby.setText(HobbiesReplaced);


                    }

                    multiImageAdapter = new MultiImageAdapter(items,EditProfileActivity.this, phone);
                    recyclerView1.setLayoutManager(new GridLayoutManager(EditProfileActivity.this,3));

                    //item drag&drop, swipe 설정
                    ItemTouchHelper.Callback callback = new ItemMoveCallback(multiImageAdapter);
                    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                    recyclerView1.setAdapter(multiImageAdapter);
                    touchHelper.attachToRecyclerView(recyclerView1);
                    recyclerView1.setHasFixedSize(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e1) {
                    e1.printStackTrace();
                }

            }//..onResponse

        };
        GetMyinfo add = new GetMyinfo(myphone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(EditProfileActivity.this);
        queue.add(add);

    }


    private void deleteImg(String uri ) {

        SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        phone = getPhone.getString("phone",null);
        dialog.show();

        imgToDB(uri,phone);


    }


    private void imgToDB(String uri, String phone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null ) {

                        Log.d("imgtoDB", "정보업데이트 성공");


                    }else {
                        Log.d("imgtoDB", "정보업데이트 실패");

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        deleteImg cancelimg=new deleteImg(uri, phone ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(cancelimg);


        Intent intent = new Intent(EditProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();

    }

    private void hidemyage(String status,String phone) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        setmyage add= new setmyage(status,phone ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }

    private void setdis(String lati, String longit, String phone) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        setdistance add= new setdistance(lati,longit,phone ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(add);

    }

}
