package com.example.honeybee;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class profileAdapter  extends RecyclerView.Adapter<profileAdapter.ViewHolder2>{
    private List<String> mData = null;
    private Context mContext = null;

    // 생성자에서 데이터 리스트 객체, Context를 전달받음.
    profileAdapter(List<String> list, Context context) {
        mData = list;
        mContext = context;
    }



    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder2 extends RecyclerView.ViewHolder {

        ImageView image;

        ViewHolder2(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조.
            image = itemView.findViewById(R.id.image);
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    // LayoutInflater - XML에 정의된 Resource(자원) 들을 View의 형태로 반환.
    @Override
    public profileAdapter.ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view1 = inflater.inflate(R.layout.multi_image_item, parent, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        profileAdapter.ViewHolder2 Vhold = new profileAdapter.ViewHolder2(view1);
        return Vhold ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(profileAdapter.ViewHolder2 holder2, int position) {
        String image_uri = mData.get(position) ;

        Glide.with(mContext)
                .load(image_uri)
                .into(holder2.image);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }


}