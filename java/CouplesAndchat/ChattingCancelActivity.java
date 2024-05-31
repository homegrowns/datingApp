package CouplesAndchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.honeybee.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChattingCancelActivity extends AppCompatActivity {

    ImageButton back,cancel;
    String roomnum;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_cancel);

        back = findViewById(R.id.btn_back1);
        cancel = findViewById(R.id.btn_MatchingCancel);

        Intent getnum = getIntent();
        roomnum = getnum.getStringExtra("roomnum");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                ref = database.getReference(roomnum);
                ref.removeValue();
                Intent love = new Intent(ChattingCancelActivity.this, Matched_Activity.class);
//                love.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(love);
                finish();
                }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog();
            }
        });
    }

    private void makeDialog(){

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChattingCancelActivity.this,R.style.AlertDialogTheme);

        alt_bld.setTitle("신고 및 짝해제").setIcon(R.drawable.ic_baseline_smartphone_24).setCancelable(

                false)
                .setNegativeButton("신고선택",

                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int id) {

                                Log.v("알림", "다이얼로그 > 신고선택");

                                ////////////////////////신고 프로세스////////////////

                                ///////////////////////////////////////////////
                                database = FirebaseDatabase.getInstance();
                                ref = database.getReference(roomnum);
                                ref.removeValue();

                                Intent love = new Intent(ChattingCancelActivity.this, Matched_Activity.class);
                                love.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(love);
                                finish();

                            }

                        }).
                setPositiveButton("취소",

                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                Log.v("알림", "다이얼로그 > 취소");

                                //취소한다


                            }

                        });

        AlertDialog alert = alt_bld.create();

        alert.show();
    }
}