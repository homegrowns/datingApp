package CouplesAndchat;

import static CouplesAndchat.coupleAdapter.itemss;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.honeybee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Matching.getlikelist;
import Matching.itemMoFormsg;
import Matching.loadInfo;
import Utills.IPadress;

public class couplechatAdapter extends RecyclerView.Adapter<couplechatAdapter.ViewHolder>  {
    protected static ArrayList<itemMoFormsg> Items;

    public ArrayList<dayAndnumFromchat> MsgAndday;

    IPadress ipadress = new IPadress();

    private static final String TAG = "ChatAdapter";
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    static Context context;
    Boolean editopen = false;
    String mynum,pnum;
    public int itemsSize;
    public int ItemPosition;
    boolean MeSent = true;
    String roomnum;

    //GetlikeList//
    String Llist;
    ArrayList<String> mlike = new ArrayList<>();;
    //GetlikeList//

    //loadmamberInfo()//
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
    String phone;
    String location;
    String token;
    String la;
    String Long;
    String intro;
    String job;
    String distance;
    String hideage;
    String likes;
    String NEWmsg="";
    String CreatedDay="";
    String Mdate="";
    String[] somelist;
    String somelikeme = null;
    String ihaveMsg = "";
    String unmatched;
    String numOfmatch;
    //loadmamberInfo()//

    static ArrayList<String> Finaltargetnums = new ArrayList<>();
    private ArrayList<String> targetnums = new ArrayList<>();
    private ArrayList<dayAndnumFromchat> keyAndday = new ArrayList<>();

    int removeChatRoom = 0;
//    Date date1 = null, date0 = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();



    public couplechatAdapter(ArrayList<itemMoFormsg> items, Context context, String mynum,int itemsSize) {
        this.Items = items;
        this.context = context;
        this.mynum = mynum;
        this.itemsSize = itemsSize;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.couplechatitem, parent, false) ;
        couplechatAdapter.ViewHolder Vhold = new couplechatAdapter.ViewHolder(view);
        return Vhold;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemPosition = holder.getAbsoluteAdapterPosition();
        int Mynum = Integer.parseInt(mynum.replace("+8210", "1"));
        int somenum = Integer.parseInt(Items.get(position).getPhone().replace("+8210", "1"));
        roomnum = String.valueOf(Mynum+somenum);

        MsgAndday = new ArrayList<>();


        Log.e("실시간메세지coupleA", "아이템수  "+Items.size());
        Log.e("실시간메세지coupleA", "누가들어있나?  "+Items.get(position).getName());
        holder.linearLayout.setOnClickListener(null);
        holder.linearLayout.setClickable(false);

        /////////////////////////메세지 보여주기//////////////////////////////////////////////////////////

        if(!Items.get(position).getTextFornums().equals("")){
            holder.linearLayout.setVisibility(View.VISIBLE);

           if(Items.get(position).getTextFornums().length() > 15){
               String M = Items.get(position).getTextFornums();
               String MSG = M.substring(0,15);
               Items.get(position).setTextFornums(MSG+"...");
               holder.msg.setText(Items.get(position).getTextFornums());

           }
           else
           {
               holder.msg.setText(Items.get(position).getTextFornums());

           }

        }

        ref.child(Items.get(position).getRoom() +"/최신메세지상태").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.child("msgState").getValue(String.class);

                if (value != null) {
                    holder.linearLayout.setVisibility(View.VISIBLE);

                    String[] vArr = value.split("[|]");
                    String msgCheck = vArr[1];
                    pnum = vArr[0];

                    Log.e(TAG, "Item.get : pnum= " + Items.get(position).getPhone() +"||"+pnum);

                    if (msgCheck.equals("new") && !pnum.equals(mynum) && pnum.equals(Items.get(position).getPhone())) {
                        holder.newMsg.setVisibility(View.VISIBLE);
                        holder.MeSentM.setVisibility(View.GONE);
                        Log.e(TAG, "pnum= " + pnum+"||"+mynum);
                        Log.e("chatA최신메세지상태", "새로운메세지 불 들어오기");
                        Items.get(position).setMsg("new");
                        MeSent = false;
                    }


                    if (msgCheck.equals("red") && !pnum.equals(mynum)) {
                        holder.newMsg.setVisibility(View.GONE);
                        holder.MeSentM.setVisibility(View.GONE);

                        MeSent = false;
                    }

                    if (pnum.equals(mynum)) {
                        holder.MeSentM.setVisibility(View.VISIBLE);
                        holder.newMsg.setVisibility(View.GONE);
                    }

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ////////////////////////////////////////////////////////////////////////////////

        if(Items.size() < 3){

            for (int i = 0; i < Items.size(); i++){
                if(!Items.get(i).getTextFornums().equals("")){
                    Log.e("chatA최신메세지상태", "메시지가진 회원이름"+Items.get(i).getName());
                    Log.e("chatA최신메세지상태", "메시지 "+Items.get(i).getTextFornums());
//                    notifyItemMoved(i, 0);
                }
            }
        }
///////////////////////////////////////////////////////////////////////////////////////////
        holder.name.setText(Items.get(position).getName());
        Items.get(position).getImage();
        Picasso.get()
                .load(Uri.parse(IP_ADDRESS+Items.get(position).getImage()))
                .fit()
                .centerCrop()
                .into(holder.image);



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!mynum.equals(pnum) && roomkey.equals(roomnum)){
//                    /// 채팅창오픈시 파이어베이스 서버에 번호를뺀 가공된 메세지를 보낸다 그리고 읽음표시가 된다.
//                    LatestMsg latestMsg = new LatestMsg(""+"|"+MSG+"|"+roomkey);
//                    ref.child(roomnum+"/최신메세지").setValue(latestMsg);
//                    holder.newMsg.setVisibility(View.GONE);
//                }
                Log.e(TAG, "첫번째제어문 " + MeSent + "||" + Items.get(position).getPhone());
                if (!MeSent && Items.get(position).getMsg().equals("new")) {

                    int Mynum = Integer.parseInt(mynum.replace("+8210", "1"));
                    int somenum = Integer.parseInt(pnum.replace("+8210", "1"));
                    String Roomnum = String.valueOf(Mynum + somenum);


                    /// 채팅창오픈시 파이어베이스 서버에 번호를뺀 가공된 메세지를 보낸다 그리고 읽음표시가 된다.
                    chackNewmsg newMsg = new chackNewmsg(Items.get(position).getPhone() + "|red");
                    ref.child(Items.get(position).getRoom() + "/최신메세지상태").setValue(newMsg);
                    holder.newMsg.setVisibility(View.GONE);
                    Items.get(position).setMsg("");

                }
                    Intent love = new Intent(v.getContext(), chattingAtcivity.class);
                    love.putExtra("name", Items.get(position).getName());
                    love.putExtra("age", Items.get(position).getAge());
                    love.putExtra("city", Items.get(position).getCity());
                    love.putExtra("hobbies", Items.get(position).getHobbies());
                    love.putExtra("img1", Items.get(position).getImage());
                    love.putExtra("img2", Items.get(position).getImg2());
                    love.putExtra("img3", Items.get(position).getImg3());
                    love.putExtra("img4", Items.get(position).getImg4());
                    love.putExtra("img5", Items.get(position).getImg5());
                    love.putExtra("img6", Items.get(position).getImg6());
                    love.putExtra("intro", Items.get(position).getIntro());
                    love.putExtra("job", Items.get(position).getJob());
                    love.putExtra("school", Items.get(position).getSchool());
                    love.putExtra("distance", Items.get(position).getDistance());
                    love.putExtra("gender", Items.get(position).getGender());
                    love.putExtra("phone", Items.get(position).getPhone());
                    love.putExtra("token", Items.get(position).getToken());
                    love.putExtra("workplace", Items.get(position).getWorkPlace());
//                    love.putExtra("msg", Items.get(position).getTextFornums());
//                    love.putExtra("date",updatedDay);
                    love.putExtra("openedit", editopen);
                    v.getContext().startActivity(love);
                    ((Activity) context).overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);

//                else{
//                   try{
//                       Intent out = new Intent(v.getContext(), ChattingCancelActivity.class);
//                       out.putExtra("roomnum",roomnum);
//                       out.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                       v.getContext().startActivity(out);
//                       ((Activity)context).finish();
//                      }
//                       catch (NullPointerException e){
//                         e.printStackTrace();
//                    }
            }
        });

    }



    @Override
    public int getItemCount() {
        return (null != Items ?  Items.size() : 0);

    }


    public void  filterList(ArrayList<itemMoFormsg> filteredList) {
        Items = filteredList;
        for (int i = 0; i < Items.size(); i++) {
            if (Items.get(i).getTextFornums().equals("") || Items.get(i).getCday().equals("")) {
                Items.remove(i);
            }
        }
        notifyDataSetChanged();

    }


    public void ChangeOrder() {

                Log.e("chatA실시간메세지", "update");


        for (int i = 0; i < Items.size(); i++){
            Log.e("아이템순서", "= "+ Items.get(i).getName());
        }

        Collections.sort(Items, new sortbydateInAdapt());
        Collections.reverse(Items);
        notifyItemRangeChanged(0,Items.size());
//        notifyDataSetChanged();
        for (int z = 0; z < Items.size(); z++){
            Log.e("아이템순서z", "= "+ Items.get(z).getName());
        }
        MsgAndday = new ArrayList<>();
    }


    static class sortbydateInAdapt implements Comparator<itemMoFormsg> {

        @Override
        public int compare(itemMoFormsg o1, itemMoFormsg o2) {
            return o1.getCday().compareTo(o2.getCday());
        }
    }

/////////////////////////////////////////////////////////////////////////////////////
    public void GetLikelist(String Myphone) {
        Finaltargetnums = new ArrayList<>();
        keyAndday = new ArrayList<>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (!success.equals("-1")) {
//                        Toast.makeText(Matched_Activity.this, "겟라이크 리스트: "+success, Toast.LENGTH_SHORT).show();
                        Llist = success;
                        Llist = Llist.replace(" ", "");
                        String[] templist = Llist.split(",");

                        for (int i = 0; i < templist.length; i++){
                            mlike.add(templist[i]);
                        }
                        userInchat(Myphone);
                    }else{
//                        Toast.makeText(Matched_Activity.this, "라이크받아오기 실패: "+success, Toast.LENGTH_SHORT).show();
                        userInchat(Myphone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        getlikelist add= new getlikelist(Myphone, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(add);

    }

    public void userInchat(String Myphone) {

        String TAG_JSON = "webnautes";
        String TAG_phoneNums = "member";
        String TAG_Cday = "day";
        String TAG_room = "room";
        String TAG_msg = "msg";

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "MatchedA 어레이갯수: d=" + jsonArray.length() + "개");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);

                        String TargetNum = item.getString(TAG_phoneNums);
                        String createdDay = item.getString(TAG_Cday);
                        String roomkey = item.getString(TAG_room);
                        String msg = item.getString(TAG_msg);

                        //////////////////////////////////////////////////

                        String[] array = TargetNum.split("[|]");
//                        String[] Target = new String[jsonArray.length()];
                        Log.e(TAG, "메세지어뎁터용번호 자료받기 확인 "+array);

                        ////////////1차 거르기/////////////
                        if (array[0].equals(Myphone) || array[1].equals(Myphone)) {
                            targetnums.add(TargetNum);
                        }

                        ///// 방번호와 방생성날자를 어레이에 넣어서 비교한다/////
                        keyAndday.add(new dayAndnumFromchat(roomkey,createdDay,msg));
                        Log.e(TAG, "메세지어뎁터용번호 자료받기 확인 "+createdDay);

                        //////////////////////////////////////////////////
//                        for (int z = 0; z < array.length; z++) {
//                            if (Myphone.equals(array[z])) {
//                                Target[z] = TargetNum;
//                            }
//                        }
                    }
//////////////////////////////////////////////////////////////////////////////////////////////


                    for (int i = 0; i < targetnums.size(); i++){
                        String[] finalTarget = targetnums.get(i).split("[|]");
                        if(!finalTarget[0].equals(Myphone)){
                            Log.d(TAG, "Finaltargetnums.add: "+finalTarget[0]);
                            Finaltargetnums.add(finalTarget[0]);
                            // 객체만들어서 번호와 날짜저장
                        }
                        else if(!finalTarget[1].equals(Myphone)){
                            Log.d(TAG, "Finaltargetnums.add: "+finalTarget[1]);
                            Finaltargetnums.add(finalTarget[1]);
                        }
                    }

                    Log.d(TAG, "메세지어뎁터용번호 자료받기 확인= "+Finaltargetnums.size());

                    loadmamberInfo(Myphone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        userInChat add =new userInChat(responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(add);
    }

    /////////////////////////////////////////////////////////////////////////////
    public void loadmamberInfo(String myphone) {
        String TAG_JSON = "webnautes";
        String TAG_NAME = "name";
        String TAG_p = "phone";
        String TAG_AGE = "age";
        String TAG_IMG = "img";
        String TAG_gender = "gender";
        String TAG_school = "school";
        String TAG_intro = "intro";
        String TAG_job = "job";
        String TAG_IMG2 = "img2";
        String TAG_IMG3 = "img3";
        String TAG_IMG4 = "img4";
        String TAG_IMG5 = "img5";
        String TAG_IMG6 = "img6";
        String TAG_hobby = "hobbies";
        String TAG_location = "location";
        String TAG_tk = "token";
        String TAG_likes = "likes";
        String TAG_lati = "lati";
        String TAG_longit = "longit";
        String TAG_hideage = "hideage";
        String TAG_unmatched = "unmatched";
        String TAG_numOfmatch = "numOfmatch";
        String TAG_work = "work";

        Items.clear();
        notifyDataSetChanged();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "MatchedA 어레이갯수: d=" + jsonArray.length() + "개");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);

                        name = item.getString(TAG_NAME);
                        age = item.getString(TAG_AGE);
                        img = item.getString(TAG_IMG);
                        gender = item.getString(TAG_gender);
                        school = item.getString(TAG_school);
                        img2 = item.getString(TAG_IMG2);
                        img3 = item.getString(TAG_IMG3);
                        img4 = item.getString(TAG_IMG4);
                        img5 = item.getString(TAG_IMG5);
                        img6 = item.getString(TAG_IMG6);
                        hobbies = item.getString(TAG_hobby);
                        location = item.getString(TAG_location);
                        likes = item.getString(TAG_likes);
                        phone = item.getString(TAG_p);
                        token = item.getString(TAG_tk);
                        la = item.getString(TAG_lati);
                        Long = item.getString(TAG_longit);
                        intro = item.getString(TAG_intro);
                        job = item.getString(TAG_job);
                        hideage = item.getString(TAG_hideage);
                        unmatched = item.getString(TAG_unmatched);
                        numOfmatch = item.getString(TAG_numOfmatch);
                        String workPlace = item.getString(TAG_work);

                        likes = likes.replace(" ", "");
                        somelist = likes.split(",");

                        Log.d(TAG, "메세지어뎁터용번호= "+Finaltargetnums.size());

                        if(mlike.size() > 0) {

                            somelikeme = findmylove(phone);

                            int mynum = Integer.parseInt(myphone.replace("+8210", "1"));
                            int somenum = Integer.parseInt(phone.replace("+8210", "1"));
                            String loc = Integer.toString(mynum+somenum);


//////////////////////////////////////////////// 아래 리사이클러뷰 들어갈 유저 데이터 걸러내기 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                            for (int z = 0; z < somelist.length; z++) {

                                if (somelist[z].equals(myphone)) {

                                    if (somelikeme != null) {
                                        Log.e(TAG, "somelikeme 오브젝트= " + name+"|| "+phone);


//////////////////////////////////////////////// 아래 메세지 리스트(ChatAdapter) 리사이클러뷰 들어갈 유저 데이터 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        ///////////////////새로운 메세지를 받았는지 확인후  ihaveMsg = "yes" 인지 "no"인지 확인후 어레이 리스트에 포함한다.///////////////////
                                        //////////////////1."no"=> 메시지 리스트에서 보이지않음 "yes"=> 메세지 리스트에서 볼수있다. 2.메세지를 받은유저를 우선으로 리스트상단에 위치시킨다.//////////
                                        if (Finaltargetnums.size() > 0) {

                                            for (int a = 0; a < Finaltargetnums.size(); a++) {
                                                if (Finaltargetnums.get(a).equals(phone)) {
                                                    Log.e(TAG, "ihaveMsg = " + Finaltargetnums.get(a));
                                                    Log.e(TAG, "ihaveMsg" + "= yes |" + phone);

                                                    ihaveMsg = "yes";

                                                } else {
                                                    Log.e(TAG, "ihaveMsg = " + Finaltargetnums.get(a));
                                                    Log.e(TAG, "ihaveMsg" + "= no |" + phone);
                                                    ihaveMsg = "no";
                                                }
                                            }
                                        }
                                        else {
                                            Log.e(TAG, "ihaveMsg in else" + "= no |" + phone);
                                            ihaveMsg = "no";
                                        }


                                        Items.add(new itemMoFormsg(phone, img, gender, school, intro, job, name, age, location, hobbies, token, distance + " km", img2, img3, img4, img5, img6, somelist, "", CreatedDay, NEWmsg, ihaveMsg,loc,24,workPlace));
                                        // 새로운 메세지 유무확인 데이터 추가한다음 리셋
                                        Log.e(TAG, "msgItems 오브젝트= " + name+"|| "+phone);

                                        for (int k = 0; k <  keyAndday.size(); k++){
                                            for (int q = 0; q < Items.size(); q++){
                                            if(keyAndday.get(k).getKey().equals(Items.get(q).getRoom())) {
                                                Items.get(q).setCday(keyAndday.get(k).getCday());
                                                Items.get(q).setTextFornums(keyAndday.get(k).getMsg());
                                             Items.get(q).setMsg("new");

                                                Log.d(TAG, "새로운 메시지| " + Items.get(q).getTextFornums());
                                                Log.d(TAG, "새로운 메시지날짜| " + Items.get(q).getCday());

                                                if(Items.get(q).getTextFornums().startsWith("https://c.tenor.com")){
                                                    NEWmsg = "GIF";
                                                    Items.get(q).setTextFornums(NEWmsg);
                                                    Log.d(TAG, "새로운 이미지 메시지가진자| " + NEWmsg+"=> "+Items.get(q).getName());
                                                }
                                                /// 이미지 메세지일시 "이미지"로 최신메세지 받을수 있도록 걸러낸다.
                                                if (Items.get(q).getTextFornums().startsWith("uploads/")) {
                                                    NEWmsg = "이미지 전송";
                                                    Items.get(q).setTextFornums(NEWmsg);
                                                    Log.d(TAG, "새로운 이미지 메시지가진자| " + NEWmsg+"=> "+Items.get(q).getName());
                                                }

                                               }
                                            }
                                        }
//                                        itemMoFormsg ob = new itemMoFormsg(phone, img, gender, school, intro, job, name, age, location, hobbies, token, distance + " km", img2, img3, img4, img5, img6, somelist, "", CreatedDay, NEWmsg, ihaveMsg,loc,24);
//

                                        Collections.sort( Items, new sortbydateInAdapt());
                                        Collections.reverse( Items);
                                        for (int p = 0; p <  Items.size(); p++){
                                            Log.e(TAG, "The Object after sorting is : " +  Items.get(p).getName()+"| "+ Items.get(p).getCday()+" || "+Items.get(p).getTextFornums());
                                        }


                                    }//..if (somelikeme != null)

                                }
                            }

                        }//..,Likes if문
                    } // for.....

                    for (int i = 0; i < Items.size(); i++){
                                if(Items.get(i).getTextFornums().equals("") || Items.get(i).getCday().equals("")){
                                    Log.e(TAG, "채팅시작못한자| " + Items.get(i).getName());
                                    Items.remove(i);
                                }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e1) {
                    e1.printStackTrace();
                }

                for (int i = 0; i < Items.size(); i++){
                    if(Items.get(i).getTextFornums().equals("") || Items.get(i).getCday().equals("")){
                        Log.e(TAG, "채팅시작못한자| " + Items.get(i).getName());
                        Items.remove(i);
                    }
                }
                Log.e(TAG, "msgItems.size= "+ Items.size());
                Log.e(TAG, "Finaltargetnums.size= "+Finaltargetnums.size());
                notifyDataSetChanged();

                if(itemss.size() == 0 && Items.size() == 0){
                    Toast.makeText(context, "현재 매치된유저가 없습니다.", Toast.LENGTH_SHORT).show();

                }

            }

        };
        loadInfo add = new loadInfo(responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(add);

    }

    public String findmylove(String n) {
        for (int z = 0; z < mlike.size(); z++) {
            if (mlike.get(z).equals(n)) {
               String pm = mlike.get(z);
                return pm;
            }
        }
        return null;
    }

//public void loadDay(String msg,String num) {
//    String TAG_JSON = "webnautes";
//    String TAG_day = "day";
//    String TAG_msg = "msg";
//    String TAG_mem = "room";
//
//    Response.Listener<String> responseListener = new Response.Listener<String>() {
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @SuppressLint("NotifyDataSetChanged")
//        @Override
//        public void onResponse(String response) {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//
//                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//                Log.d(TAG, "MatchedA 어레이갯수: chat=" + jsonArray.length() + "개");
//
//                for (int l = 0; l < jsonArray.length(); l++) {
//
//                    JSONObject zeroitem = jsonArray.getJSONObject(l);
//
//                    ///->
//                    String firstday = zeroitem.getString(TAG_day);
//                    String msg = zeroitem.getString(TAG_msg);
//                    String room = zeroitem.getString(TAG_mem);
//
//
//                    MsgAndday.add(new dayAndnumFromchat(room, firstday, msg));
//
//                    Log.e("로드아이템", "= "+ MsgAndday.get(l).getKey());
//
//                }//.. for
//
//
//
//                    for (int z = 0; z < Items.size(); z++) {
//                        Log.e("아이템순서in불러오기", "= " + Items.get(z).getName());
//
//                        for (int i = 0; i < MsgAndday.size(); i++) {
//
//                            if (Items.get(z).getRoom().equals(MsgAndday.get(i).getKey())) {
////                                Log.e(TAG, "Items msg |시간= "+Items.get(z).getTextFornums()+"|시간= "+ Items.get(i).getCday());
//
//                                Items.get(z).setCday(MsgAndday.get(i).getCday());
//                                Log.e(TAG, "합체아이템 "+Items.get(z).getName());
//
//                            }
//
//                            if (Items.get(z).getRoom().equals(MsgAndday.get(i).getKey())) {
////                                Log.e(TAG, "Items msg |시간= "+Items.get(z).getTextFornums()+"|시간= "+ Items.get(i).getCday());
//
//                                if(Items.get(z).getTextFornums().equals("")){
//
//                                }
//
//                            }
//                        }
//
//                        Log.e(TAG, "합체 번호 "+num+"||"+Items.get(z).getPhone());
//                        if(Items.get(z).getPhone().equals(num)){
//                            Items.get(z).setTextFornums(msg);
//                            Log.e(TAG, "합체 메세지 "+Items.get(z).getTextFornums());
//                            notifyItemChanged(z);
//                        }
//                    }
//
//
////                for (int z = 0; z < Items.size(); z++) {
////                    Log.e("아이템순서in불러오기", "= " + Items.get(z).getName());
////
////                    for (int i = 0; i < MsgAndday.size(); i++) {
////
////                        if (Items.get(z).getRoom().equals(MsgAndday.get(i).getKey())) {
//////                                Log.e(TAG, "Items msg |시간= "+Items.get(z).getTextFornums()+"|시간= "+ Items.get(i).getCday());
////
////                            if(Items.get(z).getTextFornums().equals("")){
////                                  notifyItemRemoved(z);
////                                Log.e(TAG, "메세지없는아이템 "+Items.get(z).getName());
////
////                            }
////
////                        }
////                    }
////
////                }
//                for (int z = 0; z < Items.size(); z++) {
//                    Log.e("아이템순서in불러오기", " 아이템내용= " + Items.get(z).getName() + " ||" + Items.get(z).getCday());
//                }
//                for (int z = 0; z < Items.size(); z++) {
//
//                    if (Items.get(z).getCday().equals("")) {
//                        Log.e("아이템순서in불러오기", "메세지없는 제거= " + Items.get(z).getName());
//                        Items.remove(z);
//                        //  notifyItemRangeChanged(0,Items.size()-1);
//                        notifyDataSetChanged();
//                    }
//                }
//                    //////////////
//                        Log.e("MsgAndday.size", "= " + MsgAndday.size());
//
//
//                            for (int z = 0; z < Items.size(); z++) {
//
//                                for (int k = 0; k < MsgAndday.size(); k++) {
//                                    Log.e("방번호비교후 위치변경1", "= " + Items.get(z).getRoom() + " || " + MsgAndday.get(k).getKey());
//
//                                    if (!Items.get(z).getRoom().equals(MsgAndday.get(k).getKey())) {
//                                        Log.e("방번호비교후 위치변경2", "= " + Items.get(z).getRoom() + " || " + MsgAndday.get(k).getKey());
////                                        notifyItemMoved(z, 0);
//                                        Items.remove(z);
//                                        //  notifyItemRangeChanged(0,Items.size()-1);
//                                        notifyDataSetChanged();
//                                    }
//                                }
//                            }
//
//
//
//                    Log.e(TAG, "발동전 "+ "ChangeOrder");
//
//                if (MsgAndday.size() > 1) {
//                    for (int i = 0; i < Items.size(); i++) {
//
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
//
//                        Date date1 = dateFormat.parse(Items.get(i).getCday());
//                        Date date0 = dateFormat.parse(Items.get(0).getCday());
//
//                        Log.e(TAG, "Items.get(0)= " + Items.get(0).getName() + "|" + Items.get(0).getCday() + " |Items.get(1)" + Items.get(i).getName() + "|" + Items.get(i).getCday());
//
//                        if (!date1.after(date0)) {
//                            ChangeOrder();
//                            Log.e(TAG, "발동 " + "ChangeOrder");
//
//                        }
//
//                    }
//                }
////                        else
////                        {
////                          Log.e(TAG, "발동  ChangeOrder(X) " + "notifyItemRangeChanged");
////
////                          notifyItemRangeChanged(0, Items.size());
////
////                         }
//
//
//
//            }catch (JSONException e) {
//                e.printStackTrace();
//
//                /// 전원 삭제 확인////////////////////////////////////
//                if(msg.equals("")&&num.equals("") && itemsSize==0){
//                     Log.e("채팅방전부삭제됨","삭제");
//
//                        Intent love = new Intent(context, Matched_Activity.class);
//                    love.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        context.startActivity(love);
//
//
//                }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        }
//    };
//
//
//    loadDay adddb=new loadDay(responseListener);
//    RequestQueue queue = Volley.newRequestQueue(context);
//    queue.add(adddb);
//}



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,msg,MeSentM;
        ImageView image,newMsg;
        ////// newMsg=>빨간점
        LinearLayout linearLayout;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.layout_couplechat);
            image = itemView.findViewById(R.id.img_couplechat);
            newMsg = itemView.findViewById(R.id.img_couplechat_receive);
            name = itemView.findViewById(R.id.couplechat_name);
            msg = itemView.findViewById(R.id.couplechat_msg);
            MeSentM = itemView.findViewById(R.id.couplechat_Mymassege);

        }
    }

}
