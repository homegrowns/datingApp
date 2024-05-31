package likesListAndActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeybee.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Matching.ItemModel;
import Matching.seePartnerProfileinfoActivity;
import Utills.IPadress;

public class mlikeslistAdapter extends RecyclerView.Adapter<mlikeslistAdapter.ViewHolder> {
    private List<ItemModel> items;
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    Context context;

    public mlikeslistAdapter(List<ItemModel> items,Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public mlikeslistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.mlikesitem, parent, false) ;
        mlikeslistAdapter.ViewHolder Vhold = new mlikeslistAdapter.ViewHolder(view);
        return Vhold;
    }

    @Override
    public void onBindViewHolder(@NonNull mlikeslistAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
          items.get(position).getImage();
        Picasso.get()
                .load(Uri.parse(IP_ADDRESS+items.get(position).getImage()))
                .fit()
                .centerCrop()
                .error(R.drawable.add_img)
                .into(holder.image);

        holder.name.setText(items.get(position).getName());
        holder.age.setText(items.get(position).getAge());
        holder.hobbies.setText(items.get(position).getHobbies());
        holder.km.setText(items.get(position).getDistance());
        holder.city.setText(items.get(position).getCity());

        holder.image.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,age,city,hobbies,km;
        ImageView image;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.img_useritem);
            name = itemView.findViewById(R.id.item_name2);
            age = itemView.findViewById(R.id.item_age2);
            hobbies = itemView.findViewById(R.id.item_hobbies2);
            city = itemView.findViewById(R.id.item_city2);
            km = itemView.findViewById(R.id.item_distance2);
        }
    }
}
