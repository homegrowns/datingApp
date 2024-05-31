package com.example.honeybee;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Profile.MultiImageAdapter;

public class SetimgRe extends Activity implements View.OnClickListener {
    private static final String TAG = "MultiImageActivity";
    public static ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    private ArrayList<Bitmap> bpList = new ArrayList<>();     // 이미지의 String Bitmap을 담을 ArrayList 객체

    JSONObject bpimg = new JSONObject();
    private ProgressDialog pd;
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    FirebaseAuth firebaseAuth;
    static final int camera = 2001;
    static final int gallery = 2002;
    public static String imgPath;

    private String phone;

    String path;
    private Button ClosepopBtn1, camerapopBtn1, gallerypopBtn1;

    Bitmap bp;
    private String bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setimg_regi);
        init();

        SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        phone = getPhone.getString("phone", null);
    }

    private void init() {
        camerapopBtn1 = (Button) findViewById(R.id.camerapopBtnr);
        gallerypopBtn1 = (Button) findViewById(R.id.gallerypopBtnr);
        ClosepopBtn1 = (Button) findViewById(R.id.ClosepopBtnr);

        gallerypopBtn1.setOnClickListener(this);
        camerapopBtn1.setOnClickListener(this);
        ClosepopBtn1.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.camerapopBtnr:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, camera);
                break;
            case R.id.gallerypopBtnr:

                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/-");
                startActivityForResult(intent, gallery);
                break;

            case R.id.ClosepopBtnr:
                setResult(RESULT_CANCELED, intent);

                Intent goback3 = new Intent(SetimgRe.this, ImageDB.class);
                startActivity(goback3);
                finish();
                break;
            default:
                break;
        }

    }

    @SuppressLint("NewApi")
    private Bitmap resize(Bitmap bm) {

        Configuration config = getResources().getConfiguration();
        /*if(config.smallestScreenWidthDp>=800)
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);//이미지 크기로 인해 용량초과
        else */
        if (config.smallestScreenWidthDp >= 600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if (config.smallestScreenWidthDp >= 400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if (config.smallestScreenWidthDp >= 360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);

        return bm;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        Bitmap bm;
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case camera:
                /*
                try {

                    bm= Images.Media.getBitmap(getContentResolver(), data.getData());

                    bm=resize(bm);
                    intent.putExtra("bitmap",bm);
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }*/
                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    // 이미지 경로(uri)=imgpath2 를 임의로만들어 불러올경로를 서버에 저장한다
                    imgPath = timeStamp + ".png";
                    bm = (Bitmap) data.getExtras().get("data");
                    bm = resize(bm);
                    bitmap = imageToString(bm);
                    intent.putExtra("bitmap", bm);
                    dataToDB(imgPath, bitmap, phone,"1");

//                    setResult(RESULT_OK, intent);
                    pd = new ProgressDialog(this);
                    pd.setMessage("잠시만 기다려주세요.");
                    pd.show();
                    Handler hd = new Handler();
                    hd.postDelayed(new imgUp(), 1000);


                 break;
                case gallery:
                    try {
                        @SuppressLint("SimpleDateFormat") String timeStamp1 = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        // 이미지 경로(uri)=imgpath2 를 임의로만들어 불러올경로를 서버에 저장한다
                        imgPath = timeStamp1;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                            bm = resize(bm);
                            Log.d(TAG, "imgPath IN Gallery= " + imgPath);

                            bitmap = imageToString(bm);

                            intent.putExtra("bitmap", bm);
                            dataToDB(imgPath, bitmap, phone,"1");
                            pd = new ProgressDialog(this);
                            pd.setMessage("잠시만 기다려주세요.");
                            pd.show();
                            Handler hd1 = new Handler();
                            hd1.postDelayed(new imgUp(), 1000);
                            // Intent goback2 = new Intent(SetimgRe.this, ImageDB.class);
                          //  goback2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                           // goback2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                          //  startActivity(goback2);
                          //  finish();

                        }

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(getApplicationContext(), "이미지 용량이 너무 큽니다.", Toast.LENGTH_SHORT).show();
                    }


                default:
                    setResult(RESULT_CANCELED, intent);
                    break;

            }


        }

//
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser fireUser = firebaseAuth.getCurrentUser();
//        String phone = fireUser.getPhoneNumber();

//        dataToDB(imgPath, bitmap, phone);

    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream Stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, Stream);
        byte[] imgBytes = Stream.toByteArray();
        String image = Base64.encodeToString(imgBytes, Base64.DEFAULT);

        return image;
    }

    public String getPathFromUri(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        cursor.moveToNext();

        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex("_data"));

        cursor.close();

        return path;
    }

    // bitmap jsonobject저장후 통신전송준비
    private void jsonObject(String num, String bpL) {

        try {

            bpimg.put(num, bpL);


        } catch (JSONException e1) {

            // TODO Auto-generated catch block

            e1.printStackTrace();

        }
    }

    // 여기서 통신저장을한다.
    private void dataToDB(String urilist, String bitmap, String phone,String multi) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null) {
                        Log.d(TAG, "dataToDB= " + success);

//                        Intent goback = new Intent(SetimgRe.this, ImageDB.class);
//                        goback.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(goback);
//                        finish();

                    } else {

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        myInfoSave adddb = new myInfoSave(urilist, bitmap, phone,multi, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(adddb);
    }

    class imgUp implements Runnable {
        public void run() {

            Intent goback = new Intent(SetimgRe.this, ImageDB.class);
            goback.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            goback.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pd.dismiss();

            startActivity(goback);


        }
    }
}
