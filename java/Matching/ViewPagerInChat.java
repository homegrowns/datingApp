package Matching;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeybee.R;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Utills.IPadress;

public class ViewPagerInChat extends RecyclerView.Adapter<ViewPagerInChat.ViewHolder> {
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    Context context;
    static int page = 0;
    List<String> listData = new ArrayList<>();

    public ViewPagerInChat(List<String> data) {
        this.listData = data;
    }




    @NonNull
    @Override
    public ViewPagerInChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_view_pager_in_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewPagerInChat.ViewHolder holder, int position) {
        if(holder instanceof ViewPagerInChat.ViewHolder){
            ViewPagerInChat.ViewHolder viewHolder = (ViewPagerInChat.ViewHolder) holder;
            viewHolder.onBind(listData.get(position));
//            imglist = new ArrayList<>();
//            for (int i = 0; i < listData.size(); i++){
//                if(!listData.get(i).getImg1().equals("null")){
//                    imglist.add(listData.get(i).getImg1());
//                }
//
//            }
            Log.e("ViewPagerA", "사진수 = "+listData.size());

            holder.pageIndicatorView.setCount(listData.size());
            page = 0;
            holder.pageIndicatorView.setSelection(page);

            Picasso.get()
                    .load(Uri.parse(IP_ADDRESS+listData.get(position)))
                    .fit()
                    .centerCrop()
                    .into(viewHolder.image);

            viewHolder.btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(page < listData.size()-1) {
                        page++;
                        Picasso.get()
                                .load(Uri.parse(IP_ADDRESS + listData.get(page)))
                                .fit()
                                .centerCrop()
                                .into(viewHolder.image);
                        holder.pageIndicatorView.setSelection(page);
                    }
                }
            });

            viewHolder.btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(page > 0) {
                        page--;
                        Picasso.get()
                                .load(Uri.parse(IP_ADDRESS + listData.get(page)))
                                .fit()
                                .centerCrop()
                                .into(viewHolder.image);
                        holder.pageIndicatorView.setSelection(page);


                    }
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private Button btn_left,btn_right;
        PageIndicatorView pageIndicatorView;
        String data;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.myimageView);
            btn_left = itemView.findViewById(R.id.btn_left);
            btn_right = itemView.findViewById(R.id.btn_right);
            pageIndicatorView = itemView.findViewById(R.id.indicator_view);
        }

        public void onBind(String data){
            this.data = data;


        }
    }
}