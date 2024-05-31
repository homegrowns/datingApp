package com.example.honeybee;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SelectNation extends AppCompatActivity {
    private Context cont;
    ListView listView;
    private EditText SearchNum;
    NationAdapter nationAdapter;
    ArrayList<ListView_Item> items;

    ArrayList<ListView_Item> Fiternation;

    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_nation);
        InitializePhoneNumbersData();

        listView = (ListView)findViewById(R.id.listview_list);
        back = findViewById(R.id.btn_backtoAuth);
        SearchNum = findViewById(R.id.et_searching);

        nationAdapter = new NationAdapter(this,items);
        listView.setAdapter(nationAdapter);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SearchNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = SearchNum.getText().toString();

                searchFilter(searchText);

            }
        });
    }

    private void InitializePhoneNumbersData() {
        items = new ArrayList<ListView_Item>();
        items.add(new ListView_Item("대한민국","+82"));
        items.add(new ListView_Item("싱가포르", "+65"));
        items.add(new ListView_Item("대만", "+886"));
        items.add(new ListView_Item("중국", "+86"));
        items.add(new ListView_Item("태국", "+66"));
        items.add(new ListView_Item("필리핀", "+63"));
        items.add(new ListView_Item("일본" ,"+81"));
        items.add(new ListView_Item("독일", "+49"));
        items.add(new ListView_Item("홍콩", "+852"));
        items.add(new ListView_Item("몽골", "+976"));
        items.add(new ListView_Item("베트남", "+84"));
        items.add(new ListView_Item("캄보디아", "+855"));
        items.add(new ListView_Item("미국", "+1"));
        items.add(new ListView_Item("캐나다", "+1"));
        items.add(new ListView_Item("멕시코" ,"+52"));
        items.add(new ListView_Item("네덜란드" ,"+31"));
    }

    public void searchFilter(String searchText) {
        Fiternation = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            /////////// 입렵한 텍스트를 아이템 이름과 비교하여 이름이 텍스트값을 포함하면 아래 어레이리스트(비어있는)에 해당값을 담는다.
            // 그리고 어뎁터메소드에 값전달//
            Log.e("SelectNation", "searchFilter= "+items.size());

            if (items.get(i).getNation().toString().toLowerCase().contains(searchText.toLowerCase())) {
                Fiternation.add(items.get(i));
                Log.e("SelectNation", "searchFilter= "+items.get(i).getNation());

            }
        }
        nationAdapter.filterList(Fiternation);
    }
}