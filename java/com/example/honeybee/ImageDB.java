package com.example.honeybee;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Profile.ItemMoveCallback;
import Utills.IPadress;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ImageDB extends AppCompatActivity
//        implements View.OnClickListener
{
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    private static final String TAG = "ImageActivity";
    final static int REQUEST_CAMERA_CODE= 1;
    final static int REQUEST_CAMERA_CODE2= 111;

    private ProgressBar pb;
    static ProgressDialog pd;

    private FirebaseAuth firebaseAuth;
    private Uri im,im2,im3,im4,im5,im6;
    private Uri[] imgs = new Uri[6];
    private ArrayList<Uri> items;
    private ArrayList<String> numsFornext;

    static Context mContext;
    // 이미지 이름

    private ImageView btn_myview;
    private ImageView btn_myview2;

    private Button btn_continue;
    private RecyclerView ImgRecyclerV;
    ImgAdapterRe multiImageAdapter;
    private String email,name,phone;
    private TextView mTextViewResult;
    private int count = 0;
    String imgPath;
    String imgPath2;
    Bitmap bitmap;
    Bitmap bp;
    String bitmap1;
    String bitmap2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_db);
        tedPermission();

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        email = auto.getString("email", "");
        name  = auto.getString("이름","");
        phone = auto.getString("phone","");

        init();
        updateProgress();
        MakeImgC(phone);

    }


    private void MakeImgC(String num) {

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("phone", num)
                .build();

        Request request = new Request.Builder()

                .url(IP_ADDRESS + "/GetImgForRegister.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(UserInfoCallOncreate);
    }

    private Callback UserInfoCallOncreate = new Callback() {
        String TAG_JSON="webnautes";

        @Override
        public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
            final String responseData = response.body().string();
            Log.d("TEST", "responseDatae : " + responseData);
            numsFornext = new ArrayList<>();
            // 서브 스레드 Ui 변경 할 경우 에러
            // 메인스레드 Ui 설정
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {

                        JSONObject jsonObject = new JSONObject(responseData);
                        String success = jsonObject.getString(TAG_JSON);
                        if (success.equals("-1")) {
                            Log.e(TAG, "success = " + success);
                            // 이미지 들어갈 컬럼 미리 인서트 해놓기
                            ImageToDB(phone);

                            if(items.size() == 0) {
                                for (int z = 0; z < 6; z++) {
                                    items.add(Uri.parse("Null"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("TEST", "ERROR Message : " + e.getMessage());
        }

    };



    private void ManageImg(String num) {

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("phone", num)
                .build();

        Request request = new Request.Builder()

                .url(IP_ADDRESS + "/GetImgForRegister.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(updateUserInfoCallback);
    }

    private Callback updateUserInfoCallback = new Callback() {
        String TAG_JSON="webnautes";
        String TAG_IMG = "img";
        String TAG_IMG2 = "img2";
        String TAG_IMG3 = "img3";
        String TAG_IMG4 = "img4";
        String TAG_IMG5 = "img5";
        String TAG_IMG6 = "img6";
        @Override
        public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
            final String responseData = response.body().string();
            Log.d("TEST", "responseDatae : " + responseData);
            numsFornext = new ArrayList<>();
            // 서브 스레드 Ui 변경 할 경우 에러
                    // 메인스레드 Ui 설정
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//            try {
//                    JSONObject jsonObject = new JSONObject(responseData);
//                    String success = jsonObject.getString(TAG_JSON);
//                    if (success.equals("-1")) {
//                            Log.e(TAG, "MatchedA 어레이=" + success);
//                        // 이미지 들어갈 컬럼 미리 인서트 해놓기
//                        ImageToDB(phone);
//
//                            if(items.size() == 0) {
//                                for (int z = 0; z < 6; z++) {
//                                    items.add(Uri.parse("Null"));
//                                }
//                            }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                Log.d(TAG, "MatchedA 어레이갯수: d=" + jsonArray.length() + "개");

                for(int i=0;i<jsonArray.length();i++) {

                                JSONObject item = jsonArray.getJSONObject(i);

                                String img = item.getString(TAG_IMG);
                                String img2 = item.getString(TAG_IMG2);
                                String img3 = item.getString(TAG_IMG3);
                                String img4 = item.getString(TAG_IMG4);
                                String img5 = item.getString(TAG_IMG5);
                                String img6 = item.getString(TAG_IMG6);

                                numsFornext.add(img);
                                numsFornext.add(img2);
                                numsFornext.add(img3);
                                numsFornext.add(img4);
                                numsFornext.add(img5);
                                numsFornext.add(img6);

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

                                if(items.size() == 0){
                                          for (int z = 0; z < 6; z++){
                                              items.add(Uri.parse("null"));

                                          }
                                }


                            }

                            getCount();
            } catch (JSONException e) {
                    e.printStackTrace();
                }

                            multiImageAdapter = new ImgAdapterRe(items, ImageDB.this, phone);
                            ImgRecyclerV.setLayoutManager(new GridLayoutManager(ImageDB.this,3));

                            //item drag&drop, swipe 설정
                            ItemTouchHelper.Callback callback = new ItemMoveCallback(multiImageAdapter);
                            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                            ImgRecyclerV.setAdapter(multiImageAdapter);
                            touchHelper.attachToRecyclerView(ImgRecyclerV);
                            ImgRecyclerV.setHasFixedSize(true);

                        }
                    });

        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("TEST", "ERROR Message : " + e.getMessage());
        }

    };

    private void getCount() {

        Log.d(TAG, "numsFornext.size = " + numsFornext.size());


        for (int i = 0; i < numsFornext.size(); i++){

            if (!numsFornext.get(i).equals("null") || numsFornext.get(i).startsWith("uploads/")) {
                Log.d(TAG, "numsFornext스트링 = " + numsFornext.get(i).toString());
                count += 1;
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart" );
        count = 0;
        items = new ArrayList<>();
        Handler hd = new Handler();
        hd.postDelayed(new ImageArrangement(), 700);

    }



    @Override
    protected void onResume() {
        super.onResume();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "img size = " + count );

                if(count > 2){

                    Intent BD = new Intent(ImageDB.this, BirthDate.class);
                    BD.putExtra("이름", name);
                    BD.putExtra("email", email);
                    ImageDB.this.startActivity(BD);
                    finish();
                }
                else
                {
                    Toast.makeText(ImageDB.this, "사진을 최소3개 이상 추가하세요.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void updateProgress() {

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor probarRemove = auto.edit();
        probarRemove.remove("ProBar");
        probarRemove.commit();

        int ProBar = pb.getProgress();
        //20%
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putInt("ProBar", ProBar);
        autoLogin.commit();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        btn_continue = (Button) findViewById(R.id.btn_Countinueimg);
        ImgRecyclerV = (RecyclerView) findViewById(R.id.recycler_registerIMG);
//        btn_myview = (ImageView) findViewById(R.id.imgbtn_myimage1);
//        btn_myview2 = (ImageView) findViewById(R.id.imgbtn_myimage2);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        pb = findViewById(R.id.progressBarImg);

        mContext=this;
//       btn_myview.setOnClickListener(this::onClick);
//       btn_myview2.setOnClickListener(this::onClick);


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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.d(TAG, "onRequestPermissionsResult");
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
//        }
//    }

//    void permission_init(){
//        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {	//권한 거절
//            // Request missing location permission.
//            // Check Permissions Now
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    android.Manifest.permission.CAMERA)) {
//                // 이전에 권한거절
//                // Toast.makeText(this,getString(R.string.limit),Toast.LENGTH_SHORT).show();
//
//            } else {
//                ActivityCompat.requestPermissions(
//                        this, new String[]{android.Manifest.permission.CAMERA},
//                        REQUEST_CAMERA_CODE);
//            }
//
//        } else {
//
//
//        }
//
//    }


//    @Override
//    public void onClick(View view) {
//        switch(view.getId()){
//            case R.id.imgbtn_myimage1:
//
//                makeDialog();
//                break;
//            case R.id.imgbtn_myimage2:
//                makeDialog2();
//                break;
//        }
//    }

//    private void makeDialog(){
//
//        AlertDialog.Builder alt_bld = new AlertDialog.Builder(ImageDB.this,R.style.AlertDialogTheme);
//
//        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.ic_baseline_smartphone_24).setCancelable(
//
//                false).setPositiveButton("사진촬영",
//
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
//
//                        // 사진 촬영 클릭
//
//                        takePhoto();
//
//                    }
//
//                }).setNeutralButton("앨범선택",
//
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialogInterface, int id) {
//
//                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
//
//                        //앨범에서 선택
//
//                        addImage();
//
//                    }
//
//                }).setNegativeButton("취소   ",
//
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        Log.v("알림", "다이얼로그 > 취소 선택");
//
//                        // 취소 클릭. dialog 닫기.
//
//                        dialog.cancel();
//
//                    }
//
//                });
//
//        AlertDialog alert = alt_bld.create();
//
//        alert.show();
//
//    }


//    private void makeDialog2(){
//
//        AlertDialog.Builder alt_bld = new AlertDialog.Builder(ImageDB.this,R.style.AlertDialogTheme);
//
//        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.ic_baseline_smartphone_24).setCancelable(
//
//                false).setPositiveButton("사진촬영",
//
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
//
//                        // 사진 촬영 클릭
//
//                        takePhoto2();
//
//                    }
//
//                }).setNeutralButton("앨범선택",
//
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialogInterface, int id) {
//
//                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
//
//                        //앨범에서 선택
//
//                        addImage2();
//
//                    }
//
//                }).setNegativeButton("취소   ",
//
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        Log.v("알림", "다이얼로그 > 취소 선택");
//
//                        // 취소 클릭. dialog 닫기.
//
//                        dialog.cancel();
//
//                    }
//
//                });
//
//        AlertDialog alert = alt_bld.create();
//
//        alert.show();
//    }


//    void addImage(){
//
//        Intent intent= new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent,10);
//    }
//
//    //사진 찍기 클릭
//
//    public void takePhoto(){
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
//
//
//
//    }
//
//
//    void addImage2(){
//        Intent intent= new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent,11);
//    }
//
//    //사진 찍기 클릭
//
//    public void takePhoto2(){
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE2);
//
//
//
//    }



    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream Stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60,Stream);
        byte[] imgBytes = Stream.toByteArray();
        String image = Base64.encodeToString(imgBytes,Base64.DEFAULT);

          return image;
    }


//    public String getPathFromUri(Uri uri){
//
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
//
//        cursor.moveToNext();
//
//        @SuppressLint("Range") String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
//
//        cursor.close();
//
//        return path;
//    }

    /// 사진 이미지 찍꼬 화면에업로드////
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (requestCode == 10) {
//            // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
//            if (resultCode == RESULT_OK) {
//                Uri uri = intent.getData();
//
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                imgPath = timeStamp + ".png";
//                Glide.with(getApplicationContext()).load(uri).into(btn_myview);
//
//                try {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                        bitmap1 = imageToString(bitmap);
//
//                    } else {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//
//                        bitmap1 =imageToString(bitmap);
//
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        } else if (requestCode == 11) {
//            if (resultCode == RESULT_OK) {
//                Uri uri2 = intent.getData();
//                Glide.with(getApplicationContext()).load(uri2).into(btn_myview2);
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                imgPath2 = timeStamp + ".png";
//                try {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//
//                        bp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
//                       bitmap2 = imageToString(bp);
//
//                    } else {
//                        bp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
//
//                        bitmap2 =imageToString(bp);
//
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }else if (requestCode == REQUEST_CAMERA_CODE) {
//            bitmap = (Bitmap) intent.getExtras().get("data");
//
//            imgPath = String.valueOf(bitmap);
//            Glide.with(getApplicationContext()).load(bitmap).into(btn_myview);
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            // 이미지 경로(uri)=imgpath 를 임의로만들어 불러올경로를 서버에 저장한다
//            imgPath = timeStamp + ".png";
//            bitmap1 = imageToString(bitmap);
//        }else if (requestCode == REQUEST_CAMERA_CODE2) {
//            bp = (Bitmap) intent.getExtras().get("data");
//            Glide.with(getApplicationContext()).load(bp).into(btn_myview2);
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            imgPath2 = timeStamp + ".png";
//
//            bitmap2 = imageToString(bp);
//        }
//        else {
//            setResult(RESULT_CANCELED, intent);
//            finish();
//        }
//
//    }







    private void ImageToDB(String phone) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null ) {
//                        Toast.makeText(ImageDB.this, "정보업데이트 성공", Toast.LENGTH_SHORT).show();


                    }else {
//                        Toast.makeText(ImageDB.this, "정보업데이트 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        controlMysql adddb=new controlMysql(phone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(adddb);
    }


    class ImageArrangement implements Runnable {
        public void run() {

            ManageImg(phone);

        }
    }


}



