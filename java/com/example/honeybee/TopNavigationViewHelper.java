package com.example.honeybee;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import CouplesAndchat.Matched_Activity;
import likesListAndActivity.seelikes_Activity;
import Matching.MatchingActivity;


public class TopNavigationViewHelper {

    private static final String TAG = "TopNavigationViewHelper";

    public static void setupTopNavigationView(BottomNavigationViewEx tv) {
        Log.d(TAG, "setupTopNavigationView: setting up navigationview");


    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_main:
                        Intent intent2 = new Intent(context, MatchingActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_seewho:
                        Intent intent1 = new Intent(context, seelikes_Activity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_matched:
                        Intent intent3 = new Intent(context, Matched_Activity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent3);

                        break;
                }

                return false;
            }
        });
    }
}
