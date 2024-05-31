package CouplesAndchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.honeybee.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skydoves.balloon.Balloon;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Utills.IPadress;

public class chatroomAdapter extends RecyclerView.Adapter<chatroomAdapter.ViewHolder> {
    private ArrayList<chatItems> Items;
    IPadress ipadress = new IPadress();
    private static final String TAG = "채팅룸";
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    private Context mContext = null;
    chattingAtcivity chatA;
    String mynum,MSG,pnum,roomkey;
    String name;
    String img;
    String img2;
    String img3;
    String img4;
    String img5;
    String img6;
    String hobbies;
    String gender;
    String school;
    String age;
    String Phonewithlikes;
    String intro;
    String job;
    String city;
    String distance;
    String token;
    String work;
    Balloon balloon;
    public int ItemPosition;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();

    public chatroomAdapter(ArrayList<chatItems> items, String mynum, Context context, String name,String age,String city,String hobbies,String img,String img2,String img3,String img4,String img5,String img6
    ,String intro,String job,String school,String distance,String gender,String Phonewithlikes,String token,String work) {
        this.Items = items;
        this.mynum = mynum;
        this.name =name;
        this.age = age;
        this.city = city;
        this.hobbies=hobbies;
        this.gender= gender;
        this.school=school;
       this.Phonewithlikes = Phonewithlikes;
       this.intro = intro;
        this.job = job;
      this.distance = distance;
        this.token = token;
        this.img = img;
        this.img2 = img2;
        this.img3 = img3;
        this.img4= img4;
        this.img5= img5;
        this.img6 = img6;
        this.work = work;
        mContext = context;

    }

    @NonNull
    @Override
    public chatroomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.item_chat, parent, false) ;
        chatroomAdapter.ViewHolder Vhold = new chatroomAdapter.ViewHolder(view);
        return Vhold;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemPosition = holder.getAbsoluteAdapterPosition();
        chatItems chat = Items.get(position);
        Log.e("채팅방", "아이템수 /"+Items.size());

        if(chat.nickname.equals("data") && !chat.Imgok){
            holder.today.setVisibility(View.VISIBLE);
            holder.today.setText(chat.date);
            holder.layoutMe.setVisibility(View.GONE);
            holder.layoutYou.setVisibility(View.GONE);
            holder.partnerView.setVisibility(View.GONE);
                holder.bubbleMe.setVisibility(View.GONE);
                holder.bubbleYou.setVisibility(View.GONE);

        }
        /// 첫번째 메세지 위에 날짜 띄우기
        else if(!chat.nickname.equals("data") && !chat.date.equals("no") && !chat.load)
        {
            holder.today.setVisibility(View.VISIBLE);
            holder.today.setText(chat.date);
         }

        if (!chat.nickname.equals("data")){

            ////////////////////////no => 날자로변경 ec2에서 현재시간이랑비교후 같으면 no유지 아니면 그날짜로변경
            if (chat.date == null || chat.date.equals("no")) {
                holder.today.setVisibility(View.GONE);
            }
            else{

            holder.today.setVisibility(View.VISIBLE);
//                    viewHolder.today.setText("오늘");
//            holder.layoutMe.setVisibility(View.GONE);
//            holder.layoutYou.setVisibility(View.GONE);

        }
    }



//            //////// 나 and 이미지
//            if(chat.Imgok && chat.isMe){
//                holder.getImageview.setVisibility(View.VISIBLE);
//
//                Glide.with(mContext)
//                        .load(chat.receivedImg).fitCenter().placeholder(R.drawable.bee)
//                        .into(holder.getImageview);
//
//                holder.dateme.setText(chat.time);
//
//                holder.layoutYou.setVisibility(View.GONE);
//                holder.bubbleMe.setVisibility(View.GONE);
//            }
//
//
//
/////// 상대방and 이미지
//        if(chat.Imgok && !chat.isMe){
//            holder.getImageviewforyou.setVisibility(View.VISIBLE);
//
//            Picasso.get()
//                    .load(chat.partnerImg)
//                    .fit()
//                    .centerCrop()
//                    .into(holder.partnerView);
//
//
//            Glide.with(mContext)
//                    .load(chat.receivedImg).fitCenter().placeholder(R.drawable.bee)
//                    .into(holder.getImageviewforyou);
//
//
//            holder.dateyou.setText(chat.time);
//            holder.bubbleYou.setVisibility(View.GONE);
//            holder.layoutMe.setVisibility(View.GONE);
//
//        }

//////////////////////////////////////////////////
        if (!chat.Imgok &&chat.isMe) {
            // 본인, 이미지 없음
          holder.bubbleMe.setBackgroundResource(R.drawable.thm_chatroom_message_bubble_you_bg_no_tail);
          holder.bubbleMe.setText(chat.message);


            holder.layoutMe.setVisibility(View.VISIBLE);
            holder.layoutYou.setVisibility(View.GONE);
            holder.dateme.setText(chat.time);
            holder.dateme.setVisibility(View.VISIBLE);

//            holder.layoutimgforme.setVisibility(View.GONE);
//            holder.layoutimgforyou.setVisibility(View.GONE);
            Log.e("채팅방", "이미지no " + chat.message + "/" + chat.receivedImg + "/" + chat.Imgok);

            if(!chat.conn)
            {
                holder.noSend.setVisibility(View.VISIBLE);
            }

/// 상대방 메세지 이미지없음
        }
        else if (!chat.Imgok && !chat.isMe && !chat.nickname.equals("data")){
            holder.bubbleYou.setBackgroundResource(R.drawable.thm_chatroom_message_bubble_you_bg);
            holder.bubbleYou.setText(chat.message);

            holder.layoutMe.setVisibility(View.GONE);
            holder.layoutYou.setVisibility(View.VISIBLE);
            holder.dateyou.setText(chat.time);
            holder.dateyou.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load(chat.partnerImg)
                    .fit()
                    .centerCrop()
                    .into(holder.partnerView);

            Log.e("채팅방", "상대이미지no " + chat.message + " === " + chat.receivedImg + " /이미지/" + chat.Imgok);

            //////////////////////상대 프로필 이미지클릭 -> 상세프로필보기//////////////////////////////////
            holder.partnerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent love = new Intent(v.getContext(), SeePartnerInfo.class);
                    love.putExtra("name", name);
                    love.putExtra("work", work);
                    love.putExtra("age", age);
                    love.putExtra("city", city);
                    love.putExtra("hobbies", hobbies);
                    love.putExtra("img1", img);
                    love.putExtra("img2", img2);
                    love.putExtra("img3", img3);
                    love.putExtra("img4", img4);
                    love.putExtra("img5", img5);
                    love.putExtra("img6", img6);
                    love.putExtra("intro", intro);
                    love.putExtra("job", job);
                    love.putExtra("school", school);
                    love.putExtra("distance", distance);
                    love.putExtra("gender", gender);
                    love.putExtra("phone", Phonewithlikes);
                    love.putExtra("token", token);
                    love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    v.getContext().startActivity(love);
                }
            });
//////////////////////////////////////////////////////////////////////////////////

        }

  //////// 나 and 이미지
        if(chat.Imgok && chat.isMe && !chat.load){
            holder.getImageview.setVisibility(View.VISIBLE);

            if(chat.getMybitmap() == null){
                Glide.with(mContext)
                        .load(chat.getReceivedImg())
                        .error(R.drawable.bee)
                        .into(holder.getImageview);

            }
            else
            {
                Glide.with(mContext)
                        .load(chat.getMybitmap())
                        .into(holder.getImageview);
            }

            Log.e("채팅방", "내가보낸 이미지yes " + chat.message + " === " + chat.receivedImg + "/이미지/" + chat.Imgok);

            holder.dateme.setText(chat.time);
            holder.layoutYou.setVisibility(View.GONE);
            holder.bubbleMe.setVisibility(View.GONE);

            holder.getImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(chat.getMybitmap() == null){
                        String img = String.valueOf(chat.getReceivedImg());
                        Intent love = new Intent(v.getContext(), ImageZoomin.class);
                        love.putExtra("img",img);
                        v.getContext().startActivity(love);
                    }
                    else
                    {
                        String img = String.valueOf(chat.getReceivedImg());
                        Intent love = new Intent(v.getContext(), ImageZoomin.class);
                        love.putExtra("bpimg",chat.getMybitmap());
                        v.getContext().startActivity(love);
                    }

                }
            });

            if(!chat.conn)
            {
                holder.noSend.setVisibility(View.VISIBLE);
            }
        }
        // DB에서 대화 목록 불러올때
        else if(chat.Imgok && chat.isMe && chat.load)
        {
            holder.getImageview.setVisibility(View.VISIBLE);

            ///// 포스트 딜레이로 시간을줘야 이미지가 화면에 나타난다.
            holder.getImageview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mContext)
                            .load(chat.receivedImg)
                            .into(holder.getImageview);

                }
            }, 400);


            Log.e("채팅방", "내가보낸 이미지yes " + chat.message + " === " + chat.receivedImg + "/이미지/" + chat.Imgok);

            holder.dateme.setText(chat.time);
            holder.layoutYou.setVisibility(View.GONE);
            holder.bubbleMe.setVisibility(View.GONE);

            holder.getImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String img = String.valueOf(chat.getReceivedImg());
                    Intent love = new Intent(v.getContext(), ImageZoomin.class);
                    love.putExtra("img",img);
                    v.getContext().startActivity(love);

                }
            });
         }



        ///// 상대방and 이미지(불러올시)
        if(chat.Imgok && !chat.isMe && chat.load){
            holder.getImageviewforyou.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load(chat.partnerImg)
                    .fit()
                    .centerCrop()
                    .into(holder.partnerView);
//////////////////////상대 프로필 이미지클릭 -> 상세프로필보기//////////////////////////////////
            holder.partnerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent love = new Intent(v.getContext(), SeePartnerInfo.class);
                    love.putExtra("name", name);
                    love.putExtra("age", age);
                    love.putExtra("work", work);
                    love.putExtra("city", city);
                    love.putExtra("hobbies", hobbies);
                    love.putExtra("img1", img);
                    love.putExtra("img2", img2);
                    love.putExtra("img3", img3);
                    love.putExtra("img4", img4);
                    love.putExtra("img5", img5);
                    love.putExtra("img6", img6);
                    love.putExtra("intro", intro);
                    love.putExtra("job", job);
                    love.putExtra("school", school);
                    love.putExtra("distance", distance);
                    love.putExtra("gender", gender);
                    love.putExtra("phone", Phonewithlikes);
                    love.putExtra("token", token);
                    love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    v.getContext().startActivity(love);
                }
            });
//////////////////////////////////////////////////////////////////////////////////
            holder.getImageview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mContext)
                            .load(chat.receivedImg)
                            .into(holder.getImageviewforyou);

                }
            }, 600);

            Log.e("채팅방", "상대가보낸 이미지yes " + chat.message + "/" + chat.receivedImg + "/" + chat.Imgok);

            ///////////////////////// 이미지 다운로드////////////////////////////
            holder.getImageviewforyou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //String img= String.valueOf(chat.getReceivedImg());
//
//                    chatA.downpic(img);

                }
            });
            ///////////////////////// 이미지 다운로드////////////////////////////

            holder.dateyou.setText(chat.time);
            holder.bubbleYou.setVisibility(View.GONE);
            holder.layoutMe.setVisibility(View.GONE);

            holder.getImageviewforyou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String img = String.valueOf(chat.getReceivedImg());
                    Intent love = new Intent(v.getContext(), ImageZoomin.class);
                    love.putExtra("img", img);
                    v.getContext().startActivity(love);


                }
            });

        }

        ///// 상대방and 이미지
        if(chat.Imgok && !chat.isMe){
            holder.getImageviewforyou.setVisibility(View.VISIBLE);



            Picasso.get()
                    .load(chat.partnerImg)
                    .fit()
                    .centerCrop()
                    .into(holder.partnerView);
//////////////////////상대 프로필 이미지클릭 -> 상세프로필보기//////////////////////////////////
            holder.partnerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent love = new Intent(v.getContext(), SeePartnerInfo.class);
                    love.putExtra("work", work);
                    love.putExtra("name", name);
                    love.putExtra("age", age);
                    love.putExtra("city", city);
                    love.putExtra("hobbies", hobbies);
                    love.putExtra("img1", img);
                    love.putExtra("img2", img2);
                    love.putExtra("img3", img3);
                    love.putExtra("img4", img4);
                    love.putExtra("img5", img5);
                    love.putExtra("img6", img6);
                    love.putExtra("intro", intro);
                    love.putExtra("job", job);
                    love.putExtra("school", school);
                    love.putExtra("distance", distance);
                    love.putExtra("gender", gender);
                    love.putExtra("phone", Phonewithlikes);
                    love.putExtra("token", token);
                    love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    v.getContext().startActivity(love);
                }
            });
//////////////////////////////////////////////////////////////////////////////////
            holder.getImageview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mContext)
                            .load(chat.receivedImg)
                            .into(holder.getImageviewforyou);
                }
            }, 600);





            holder.dateyou.setText(chat.time);
            holder.bubbleYou.setVisibility(View.GONE);
            holder.layoutMe.setVisibility(View.GONE);

            holder.getImageviewforyou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String img = String.valueOf(chat.getReceivedImg());
                    Intent love = new Intent(v.getContext(), ImageZoomin.class);
                    love.putExtra("img",img);
                    v.getContext().startActivity(love);


                }
            });
        }

//        if(Items.size() < 2) {
//            // 첫메세지 만 전송시 날짜볼수있도록// 불러오기부터는 시간이 db에 저장되어있다.
//            ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
//
//            String convertedDate = nowInKorea.format(DateTimeFormatter.ofPattern("MM월 dd일"));
//
//            holder.today.setVisibility(View.VISIBLE);
//            holder.today.setText(convertedDate);
//        }


//            if (chat.isMe) {
//
//            if(!chat.Imgok){
//                // no이미지
//                holder.bubbleMe.setBackgroundResource(R.drawable.thm_chatroom_message_bubble_you_bg_no_tail);
//                holder.bubbleMe.setText(chat.message);
//
//                holder.layoutMe.setVisibility(View.VISIBLE);
//                holder.layoutYou.setVisibility(View.GONE);
//                holder.dateme.setText(chat.time);
//                holder.dateme.setVisibility(View.VISIBLE);
//
//                holder.layoutimgforme.setVisibility(View.GONE);
//                holder.layoutimgforyou.setVisibility(View.GONE);
//                Log.e("채팅방", "이미지no "+chat.message+"/"+chat.receivedImg+"/"+chat.Imgok);
//
//            } else {
//                // 내가 이미지 가질시
//                holder.layoutMe.setVisibility(View.GONE);
//                holder.layoutYou.setVisibility(View.GONE);
//                holder.layoutimgforme.setVisibility(View.VISIBLE);
//                holder.bubbleMe.setVisibility(View.GONE);
//                //셋팅
//                Picasso.get()
//                        .load(chat.receivedImg)
//                        .fit()
//                        .centerCrop()
//                        .into(holder.getImageview);
////                    viewHolder.getImageview.setImageURI(chat.receivedImg);
//
//
//                holder.timeforimgtome.setText(chat.time);
//
//                Log.e("채팅방", "이미지 "+chat.message+"/"+chat.receivedImg+"/"+chat.Imgok);
//
//
//            }
//
//        } else {
//            if(!chat.Imgok){
//
//                holder.bubbleYou.setBackgroundResource(R.drawable.thm_chatroom_message_bubble_you_bg);
//                holder.bubbleYou.setText(chat.message);
//
//                holder.layoutMe.setVisibility(View.GONE);
//                holder.layoutYou.setVisibility(View.VISIBLE);
//                holder.dateyou.setText(chat.time);
//                holder.dateyou.setVisibility(View.VISIBLE);
//
//                Picasso.get()
//                        .load(chat.partnerImg)
//                        .fit()
//                        .centerCrop()
//                        .into(holder.partnerView);
//
//                Log.e("채팅방", "상대이미지no " + chat.message + "/" + chat.receivedImg + "/" + chat.Imgok);
//
//                holder.layoutimgforme.setVisibility(View.GONE);
//                holder.layoutimgforyou.setVisibility(View.GONE);
//
//            }else {
//                holder.layoutMe.setVisibility(View.GONE);
//                holder.layoutYou.setVisibility(View.GONE);
//                holder.layoutimgforyou.setVisibility(View.VISIBLE);
//                //셋팅
//                holder.getImageviewforyou.setImageURI(chat.receivedImg);
//                holder.timeforimgtoyou.setText(chat.time);
//
//                Picasso.get()
//                        .load(chat.receivedImg)
//                        .fit()
//                        .centerCrop()
//                        .into(holder.getImageviewforyou);
//
//                Picasso.get()
//                        .load(chat.partnerImg)
//                        .fit()
//                        .centerCrop()
//                        .into(holder.partnerViewforimg);
//                Log.e("채팅방", "상대이미지ok "+chat.message+"/"+chat.receivedImg+"/"+chat.Imgok);
//
//            }
//        }
//


    }




    @Override
    public int getItemCount() {
        return Items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void  filterList(ArrayList<chatItems> filteredList) {
        Items = filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView partnerView,partnerViewforimg,getImageview,getImageviewforyou;
        LinearLayout layoutMe,layoutimgforme;
        LinearLayout layoutYou, layout_time,layoutimgforyou;
        TextView dateme,dateyou,daytv, today,noSend;
        TextView bubbleYou,timeforimgtoyou;
        TextView bubbleMe,timeforimgtome;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_time = itemView.findViewById(R.id.layout_time);
            layoutMe = (LinearLayout) itemView.findViewById(R.id.layout_me);
            layoutYou = (LinearLayout) itemView.findViewById(R.id.layout_you);
            bubbleYou = (TextView) itemView.findViewById(R.id.bubble_you_text);
            bubbleMe = (TextView) itemView.findViewById(R.id.bubble_me_text);
            dateyou = (TextView) itemView.findViewById(R.id.bubble_you_time);
            dateme = (TextView) itemView.findViewById(R.id.bubble_me_time);
            daytv = (TextView) itemView.findViewById(R.id.tv_day);
            today = (TextView) itemView.findViewById(R.id.tv_today);
            noSend = (TextView) itemView.findViewById(R.id.bubble_NotSent);
            partnerView = (ImageView) itemView.findViewById(R.id.img_partner);
            ////////////////////////////////이미지 로드할시 보일 레이아웃 요소 아래//////////////////////////////////////////////////////
//            layoutimgforyou = (LinearLayout) itemView.findViewById(R.id.layout_imgforyou);
//            layoutimgforme = (LinearLayout) itemView.findViewById(R.id.layout_imgforme);
            getImageview = (ImageView) itemView.findViewById(R.id.img_tcpGetforme);
            getImageviewforyou = (ImageView) itemView.findViewById(R.id.img_tcpGetimgforyou);

//            timeforimgtome = (TextView) itemView.findViewById(R.id.bubble_me_timeforimg);
//            timeforimgtoyou = (TextView) itemView.findViewById(R.id.bubble_you_timeforimg);
//            partnerViewforimg = (ImageView) itemView.findViewById(R.id.img_partnerforimg);

        }
    }

}