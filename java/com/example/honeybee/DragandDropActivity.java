package com.example.honeybee;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DragandDropActivity extends AppCompatActivity {

    Button btn_source;

    ImageView Target;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragand_drop);
        btn_source = (Button)findViewById(R.id.source);

        //버턴의 롱클릭 리스너 등록

        btn_source.setOnLongClickListener(new View.OnLongClickListener() {



            @Override

            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub

                //롱클릭시 클립데이터를 만듬

                ClipData clip = ClipData.newPlainText("이미지바꿀래", "이미지");

                //드래그할 데이터, 섀도우 지정, 드래그 앤 드롭 관련 데이터를 가지는 객체 지정, 0
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);

                return false;

            }

        });
        Button btnSource2 = (Button)findViewById(R.id.source2);

        btnSource2.setOnLongClickListener(new View.OnLongClickListener() {



            @Override

            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub

                ClipData clip = ClipData.newRawUri("uri",

                        Uri.parse("http://13.125.157.230/uploads/download.jpeg.jpg"));

                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);

                return false;

            }

        });

        Target = (ImageView) findViewById(R.id.target);

        Target.setOnDragListener(mDragListener);


    }

    View.OnDragListener mDragListener = new View.OnDragListener() {



        @SuppressLint("WrongConstant")
        @Override

        public boolean onDrag(View v, DragEvent event) {

            // TODO Auto-generated method stub

            ImageView img;

            //드래그 객체가 버튼인지 확인

            if(v instanceof ImageView){

                img = (ImageView) v;

            }else{

                return false;

            }


            //이벤트를 받음

            switch(event.getAction()){

                //드래그가 시작되면

                case DragEvent.ACTION_DRAG_STARTED:

                    //클립 설명이 텍스트면

                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST)){

//                        img.setImageBitmap(R.drawable.bee);//버튼의 글자를 바꿈
                        Glide.with(DragandDropActivity.this).load(R.drawable.bee).into(img);

                        return true;

                    }else{//인텐트의 경우 이쪽으로 와서 드래그를 받을 수가 없다.

                        return false;

                    }

                    //드래그가 뷰의 경계안으로 들어오면

                case DragEvent.ACTION_DRAG_ENTERED:

                    Target.setImageResource(R.drawable.circle_background);//버튼 글자 바꿈

                    return true;

                //드래그가 뷰의 경계밖을 나가면

                case DragEvent.ACTION_DRAG_EXITED:

                    Target.setImageResource(R.drawable.add_img);//버튼 글자 바꿈

                    return true;

                //드래그가 드롭되면

                case DragEvent.ACTION_DROP:

                    //클립데이터의 값을 가져옴

                    String text = event.getClipData().getItemAt(0).getText().toString();

                    Target.setImageResource(R.drawable.default_man);

                    btn_source.setText("전달성공");

                    return true;

                //드래그 성공 취소 여부에 상관없이 모든뷰에게

                case DragEvent.ACTION_DRAG_ENDED:

                    if(event.getResult()){//드래그 성공시

                        Toast.makeText(DragandDropActivity.this, "Drag & Drop 완료", 0).show();

                    }else{//드래그 실패시

                        Target.setImageResource(R.drawable.ic_add_circle_black_24dp);

                    }

                    return true;

            }

            return true;

        }

    };

}