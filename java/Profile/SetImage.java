package Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.honeybee.R;
import com.example.honeybee.myInfoSave;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Utills.IPadress;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SetImage extends Activity implements View.OnClickListener {
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    private static final String TAG = "MultiImageActivity";
    public static ArrayList<String> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    private ArrayList<String> ImgNameList = new ArrayList<>();     // 이미지의 String 이미지제목을 담을 ArrayList 객체

    JSONObject bpimg = new JSONObject();
    int imgIhave = 0;
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    FirebaseAuth firebaseAuth;
    static final int camera = 2001;
    static final int gallery = 2002;
    public static String imgPath;
    public static String bitmap;
    String phone,multi;
    Button ClosepopBtn, camerapopBtn, gallerypopBtn;
    int shit = 0;
    Bitmap bp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);
        init();

        SharedPreferences getPhone = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        phone = getPhone.getString("phone", null);

        imgCheck(phone);
    }

    private void init() {
        camerapopBtn = (Button) findViewById(R.id.camerapopBtn);
        gallerypopBtn = (Button) findViewById(R.id.gallerypopBtn);
        ClosepopBtn = (Button) findViewById(R.id.ClosepopBtn);

        gallerypopBtn.setOnClickListener(this);
        camerapopBtn.setOnClickListener(this);
        ClosepopBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.camerapopBtn:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, camera);
                break;
            case R.id.gallerypopBtn:

                intent = new Intent(Intent.ACTION_PICK);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(
                        Intent.createChooser(intent, "사진 최대 6장 선택가능"),
                        gallery
                );

                break;

            case R.id.ClosepopBtn:
                setResult(RESULT_CANCELED, intent);
                Toast.makeText(getApplicationContext(), "이미지 선택 취소하였습니다.", Toast.LENGTH_SHORT).show();
                Intent goback3 = new Intent(SetImage.this, EditProfileActivity.class);
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
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    // 이미지 경로(uri)=imgpath2 를 임의로만들어 불러올경로를 서버에 저장한다
                    imgPath = timeStamp + ".png";
                    bm = (Bitmap) data.getExtras().get("data");
                    bm = resize(bm);
                    bitmap = imageToString(bm);
                    intent.putExtra("bitmap", bm);
                    setResult(RESULT_OK, intent);

                    dataToDB(imgPath, bitmap, phone,"1");
                    Intent goback = new Intent(SetImage.this, EditProfileActivity.class);
                    goback.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(goback);
                    break;
                case gallery:

                    if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                        Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    }
                    else {// 이미지를 하나라도 선택한 경우

                        if(data.getClipData() == null) { // 이미지를 하나만 선택한 경우
                            try {

                                String timeStamp1 = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                // 이미지 경로(uri)=imgpath2 를 임의로만들어 불러올경로를 서버에 저장한다
                                imgPath = timeStamp1;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                            bm = resize(bm);
                                    bitmap = imageToString(bm);
                                    multi = "1";
                                    dataToDB(imgPath, bitmap, phone,multi);
                                    intent.putExtra("bitmap", bm);

                                    Intent goback2 = new Intent(SetImage.this, EditProfileActivity.class);
                                    goback2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(goback2);
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
                        }
                        else // 이미지를 여러장 선택한 경우
                        {
                            int imgIcanUp = 6-imgIhave;
                            ClipData clipData = data.getClipData();

                            Log.e("clipData", String.valueOf(clipData.getItemCount()));

                            if (clipData.getItemCount() > imgIcanUp) {   // 선택한 이미지가 6장 이상인 경우


                                Toast.makeText(getApplicationContext(), "사진은 "+ imgIcanUp+"장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                            }
                            else {   // 선택한 이미지가 1장 이상 10장 이하인 경우
                                Log.e(TAG, "multiple choice");
                                multi = "multi";
                               for (int i = 0; i < clipData.getItemCount(); i++) {

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                    try {

                                        String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                        // 이미지 경로(uri)=imgpath2 를 임의로만들어 불러올경로를 서버에 저장한다
                                        shit ++;
                                        imgPath = timeStamp2+shit;
                                        String conten = String.valueOf(clipData.getItemAt(i).getUri());
                                        Log.e(TAG, "multiple 내용물 = "+conten);
                                        ImgNameList.add(imgPath);

                                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), clipData.getItemAt(i).getUri());
                                        bitmap = imageToString(bm);
                                        uriList.add(bitmap);
                                        intent.putExtra("bitmap", bm);


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                                   Log.e(TAG, "uriList bitmap= "+bitmap);

                                }//..for

                                String ImgName = String.valueOf(ImgNameList).replace("[", " ").trim();
                                String ImgNameL = ImgName.replace("]", " ").trim();
                                String ImgNameLi = ImgNameL.replace(" ", "");

                                String urilist = String.valueOf(uriList).replace("[", " ").trim();
                                String ul = urilist.replace("]", " ").trim();


                                Log.e(TAG, "ImgNameListp= "+ImgNameLi);
                                Log.e(TAG, "uriList= "+ul);
                                dataToDB(ImgNameLi, ul , phone,multi);
//                                Intent goback2 = new Intent(SetImage.this, EditProfileActivity.class);
//                                startActivity(goback2);
//                                    setResult(RESULT_OK, intent);
                                ImgNameList.clear();
                                uriList.clear();
                                Intent goback3 = new Intent(SetImage.this, EditProfileActivity.class);
                                goback3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(goback3);


                            }
                        }
                      //  break;
                    }//..else

//                    setResult(RESULT_OK, intent);
//                    finish();
//                    break;

                default:
                    setResult(RESULT_CANCELED, intent);
                    finish();
                    break;

            }
        }

//
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser fireUser = firebaseAuth.getCurrentUser();
//        String phone = fireUser.getPhoneNumber();


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
    private void dataToDB(String urilist, String Bitmap, String phone,String multi) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null) {
//                        Toast.makeText(getApplicationContext(), "멀티이미지 성공.", Toast.LENGTH_SHORT).show();
                        imgPath = "";
                        bitmap = "";
                        Log.e(TAG, "멀티이미지 성공= ");


                    }

                    else {

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        myInfoSave adddb = new myInfoSave(urilist, Bitmap, phone,multi, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(adddb);
    }



    /////////////////////////////////////////////////////////////////////////////////////////////
    private void imgCheck(String phone) {
        // 레트로핏 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_ADDRESS + "/imgCheck.php/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        GetImginterface Interface = retrofit.create(GetImginterface.class);
        Call<String> call = Interface.phone_call(phone);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response)
            {
                try {
                    final String responseData = response.body();
                    JSONObject jsonObject = new JSONObject(responseData);

                    Log.e(TAG, "data received= "+responseData);

                    parseResult(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Log.e("에러 : ", t.getMessage());
            }
        });
    }

    private void parseResult(JSONObject jsonObject)
    {

        String TAG_JSON = "success";

        String item = null;

        try {
            item = jsonObject.getString(TAG_JSON);
            Log.d(TAG, "parseResult =" + item);
            imgIhave = Integer.parseInt(item);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
