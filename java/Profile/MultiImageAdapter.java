package Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.honeybee.R;
import com.example.honeybee.deleteImg;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import Utills.IPadress;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.CustomViewHolder> implements ItemMoveCallback.ItemTouchHelperAdapter
 {
    private ArrayList<Uri> mData;
    private Context mContext = null;
    public int ItemPosition;
    String mphone;
     IPadress ipadress = new IPadress();
     private String IP_ADDRESS = ipadress.ip.IP_Adress;
     private static final String TAG = "adapter";

    // 생성자에서 데이터 리스트 객체, Context를 전달받음.
    public MultiImageAdapter(ArrayList<Uri> list, Context context, String phone) {
        mData = list;
        mContext = context;
        mphone = phone;
    }

    @NotNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_image_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        ItemPosition = holder.getAbsoluteAdapterPosition();

        if (mData.size() < 7) {


            for (int i = 0; i <= mData.size(); i++) {
                String image_uri = String.valueOf(mData.get(ItemPosition));
                if (!image_uri.toString().equals("null") && image_uri.toString().length() > 10) {
                    Glide.with(mContext)
                            .load(IP_ADDRESS+image_uri)
                            .into(holder.image);
                    holder.imageDelet.setVisibility(View.VISIBLE);
                    holder.imagePlus.setVisibility(View.INVISIBLE);
                } else if (image_uri.toString().equals("null") || image_uri.equals(null)) {

                    holder.imagePlus.setVisibility(View.VISIBLE);
                    holder.imageDelet.setVisibility(View.INVISIBLE);

                }

                    Log.d(TAG, "item=" + mData);
            }
        } else {
            Toast.makeText(mContext, "사진초과: " + mData.size() + "개", Toast.LENGTH_SHORT).show();

        }

        holder.imagePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, SetImage.class);
                mContext.startActivity(intent);
            }
        });
        holder.imageDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemPosition = holder.getAbsoluteAdapterPosition();
                imgToDB(String.valueOf(mData.get(ItemPosition)), mphone);

            }

        });
    }



    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return (null != mData ? mData.size() : 0);

    }

    // fromPos == 드래그하는이미지번호 , targetPos == 자리바꿀 이미지번호.
     @Override
     public void onItemMove(int fromPos, int targetPos) {
         if (fromPos < targetPos) {
             for (int i = fromPos; i < targetPos; i++) {
                 // Collections.swap == array안 인덱스의 자리를바꾸는함수
                 Collections.swap( mData, i, i+1);
             }

         }else{
             for (int i = fromPos; i > targetPos; i--) {
                 Collections.swap( mData, i, i-1);
             }
         }
         String trimming = String.valueOf(mData);
         String trimming2 = trimming.replace("[", " ").trim();
         String uris = trimming2.replace("]", " ").trim();

         String data = uris.replace(" ", "");
         imgTosaveDB(data,mphone);
         notifyItemMoved(fromPos, targetPos);
    }


     @Override
     public void onItemDismiss(int pos) {
         mData.remove(pos);
         notifyItemRemoved(pos);


     }

     public static class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        ImageButton imageDelet;
        ImageButton imagePlus;


        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            imageDelet = itemView.findViewById(R.id.mbtn_itemcancel);
            imagePlus = itemView.findViewById(R.id.mbtn_plusitemView);

        }
    }

    private void imgToDB(String uri, String phone) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") && success != null) {
                        Toast.makeText(mContext, "정보업데이트 성공", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(mContext, "정보업데이트 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        deleteImg cancelimg = new deleteImg(uri, phone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(cancelimg);

        Intent intent = new Intent(mContext, EditProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

     private void imgTosaveDB(String uris, String phone) {

         Response.Listener<String> responseListener = new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONObject jsonObject = new JSONObject(response);
                     String success = jsonObject.getString("success");
                     if (success.equals("1") && success != null) {
                         Toast.makeText(mContext, "정보업데이트 성공", Toast.LENGTH_SHORT).show();


                     } else {
                         Toast.makeText(mContext, "정보업데이트 실패", Toast.LENGTH_SHORT).show();
                         return;
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }

         };
         saveUris saveimg = new saveUris(uris, phone, responseListener);
         RequestQueue queue = Volley.newRequestQueue(mContext);
         queue.add(saveimg);

         Intent intent = new Intent(mContext, EditProfileActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         mContext.startActivity(intent);

     }
}
