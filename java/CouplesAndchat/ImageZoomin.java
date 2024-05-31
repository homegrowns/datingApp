package CouplesAndchat;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.honeybee.R;

import Utills.IPadress;

public class ImageZoomin extends AppCompatActivity {
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
   ImageView Zoonimg;
   Button back;
    Bitmap bp = null;
    String img = null;
    Uri im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoomin);

        Zoonimg = findViewById(R.id.img_view);
        back = findViewById(R.id.btn_backToChat);

        Intent intent = getIntent();
        img = intent.getStringExtra("img");
        bp = intent.getParcelableExtra("bpimg");


        Log.e("ImageZoom", "이미지내용 = "+img);

        if(img == null){
            Glide.with(this).load(bp).into(Zoonimg);
        }
        else {

            im = Uri.parse(img);
            Glide.with(this).load(im).into(Zoonimg);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}