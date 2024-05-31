package Matching;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.honeybee.R;

import java.util.ArrayList;
import java.util.List;

import Utills.IPadress;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder>{
    private List<ItemModel> items;
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    Context context;
    List<String> Imagelist = new ArrayList<>();
    CardStackAdapter(List<ItemModel> items) {
        this.items = items;

    }


    @NonNull
    @Override
    public CardStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.carditem, parent, false) ;
        CardStackAdapter.ViewHolder Vhold = new CardStackAdapter.ViewHolder(view);
        return Vhold;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 페이져뷰로넘어갈 자료 구성후 페이져뷰로 리스트데이터 넘긴다,
        List<imageData> list = new ArrayList<>();
        list.add(new imageData(items.get(position).getImage()));
        list.add(new imageData(items.get(position).getImg2()));
        list.add(new imageData(items.get(position).getImg3()));
        list.add(new imageData(items.get(position).getImg4()));
        list.add(new imageData(items.get(position).getImg5()));
        list.add(new imageData(items.get(position).getImg6()));

        Imagelist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if(!list.get(i).getImg1().equals("")) {

                if (!list.get(i).getImg1().equals("null")) {
                    Imagelist.add(list.get(i).getImg1());
                }
            }
        }
        holder.viewPager2.setAdapter(new ViewPagerAdapter(Imagelist));
        Log.e("CardStackA", "어뎁터로 넘기는 사진수 = "+Imagelist.size());

        // 페이져뷰로넘어갈 자료 구성후 페이져뷰로 리스트데이터 넘긴다,
//        pageIndicatorView.setCount(Imagelist.size());


//////////////////////// 상세정보 액티비티로이동
        holder.myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent love = new Intent(v.getContext(), seePartnerProfileinfoActivity.class);
                love.putExtra("name",items.get(position).getName());
                love.putExtra("age",items.get(position).getAge());
                love.putExtra("city",items.get(position).getCity());
                love.putExtra("hobbies",items.get(position).getHobbies());
                love.putExtra("img1",items.get(position).getImage());
                love.putExtra("img2",items.get(position).getImg2());
                love.putExtra("img3",items.get(position).getImg3());
                love.putExtra("img4",items.get(position).getImg4());
                love.putExtra("img5",items.get(position).getImg5());
                love.putExtra("img6",items.get(position).getImg6());
                love.putExtra("intro",items.get(position).getIntro());
                love.putExtra("job",items.get(position).getJob());
                love.putExtra("school",items.get(position).getSchool());
                love.putExtra("distance",items.get(position).getDistance());
                love.putExtra("gender",items.get(position).getGender());
                love.putExtra("phone",items.get(position).getPhone());
                love.putExtra("token",items.get(position).getToken());
                love.putExtra("work",items.get(position).getWork());

                v.getContext().startActivity(love);

            }
        });

        holder.name.setText(items.get(position).getName());
        holder.age.setText(items.get(position).getAge());
        holder.hobbies.setText(items.get(position).getHobbies());
        holder.km.setText(items.get(position).getDistance());
        holder.city.setText(items.get(position).getCity());

//            holder.pageIndicatorView.setCount(imglist.size());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,age,city,hobbies,km;
        ImageButton myinfo;
        ViewPager2 viewPager2;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조.
            myinfo = itemView.findViewById(R.id.btn_myinfo);
            viewPager2 = itemView.findViewById(R.id.item_image);
//            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            age = itemView.findViewById(R.id.item_age);
            hobbies = itemView.findViewById(R.id.item_hobbies);
            city = itemView.findViewById(R.id.item_city);
            km = itemView.findViewById(R.id.item_distance);


        }


    }

    public List<ItemModel> getItems() {
        return items;
    }
    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}


