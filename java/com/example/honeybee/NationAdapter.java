package com.example.honeybee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NationAdapter extends BaseAdapter {
    List<ListView_Item> items = null;
    Context contx;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ListView_Item> item;
    public NationAdapter(Context context, ArrayList<ListView_Item> items) {
        contx = context;
        item = items;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_custom, null);
        TextView nation = (TextView)view.findViewById(R.id.tv_nation);
        TextView Nums = (TextView)view.findViewById(R.id.tv_numbers);


        nation.setText(item.get(position).getNation());
        Nums.setText(item.get(position).getPhoneNum());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(contx, PhonAuth.class);
                reg.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                reg.putExtra("번호", item.get(position).getPhoneNum());
                reg.putExtra("나라", item.get(position).getNation());
                contx.startActivity(reg);

            }
        });

        return view;
    }

    protected void  filterList(ArrayList<ListView_Item> filteredList) {

        item = filteredList;
        notifyDataSetChanged();

    }
}
