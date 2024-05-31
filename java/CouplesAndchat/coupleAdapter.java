package CouplesAndchat;


import static CouplesAndchat.Matched_Activity.Finaltargetnums;
import static CouplesAndchat.Matched_Activity.findmylove;
import static CouplesAndchat.Matched_Activity.mylike;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.honeybee.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Matching.itemMoFormsg;
import Matching.loadInfo;
import Utills.IPadress;
import Utills.NotiModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class coupleAdapter extends RecyclerView.Adapter<coupleAdapter.ViewHolder> {
    public static List<itemMoFormsg> itemss;
    private static ArrayList<CouplesAndchat.MdateAndRoom> MdateAndRoom;
    private static final String TAG = "coupeleAdapter";
    IPadress ipadress = new IPadress();
    private String IP_ADDRESS = ipadress.ip.IP_Adress;
    Matched_Activity matched_activity;
    Boolean cancelM=false;

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
    double latitude;
    double longitude;
    String ihaveMsg = "";
    String unmatched;
    String numOfmatch;
    //loadmamberInfo()//

    static Context context;
    static String num;
    static String roomFordate;
    String Changetime = "-1";
    static String matchedday=null;
    public static int ItemPosition;
    private String currTime;
    public static String ChangedCtime = "";
    int GoneCount = 0;
    int ChatCount = 0;
    static String roomnum;
    static long hour,min=1;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.

    private static DatabaseReference ref = database.getReference();
    public coupleAdapter(List<itemMoFormsg> items,Context context,String num ) {
        this.itemss = items;
        coupleAdapter.context = context;
        this.num = num;

    }

    @NonNull
    @Override
    public coupleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.couplesitem, parent, false) ;
        coupleAdapter.ViewHolder Vhold = new coupleAdapter.ViewHolder(view);
        return Vhold;
    }

    @Override
    public void onBindViewHolder(@NonNull coupleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ItemPosition = holder.getAbsoluteAdapterPosition();
        Log.d(TAG, "유저수 = " + itemss.size() + " | 유저이름=" + itemss.get(position).getName());
        itemss.get(position).getImage();
        Picasso.get()
                .load(Uri.parse(IP_ADDRESS+itemss.get(position).getImage()))
                .fit()
                .centerCrop()
                .into(holder.image);

        holder.name.setText(itemss.get(ItemPosition).getName());

        int Mynum = Integer.parseInt(num.replace("+8210", "1"));
        int somenum = Integer.parseInt(itemss.get(position).getPhone().replace("+8210", "1"));
        roomnum = String.valueOf(Mynum+somenum);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        int Mynum2 = Integer.parseInt(num.replace("+8210", "2"));
        int somenum2 = Integer.parseInt(itemss.get(position).getPhone().replace("+8210", "2"));
        roomFordate= String.valueOf(Mynum2+somenum2);
        Log.e(TAG, "coupleAdapter 방번호 = " +roomFordate);
        Log.e(TAG, "coupleAdapter 이름= "+itemss.get(position).getName()+", 매치된날짜 ="+itemss.get(ItemPosition).getCday());

///////////////////////아이템이 매치날짜가없으면 숨긴다/////////////////////////////////////////////////////////////////////////////////
          if(!itemss.get(position).getCday().equals("")){
              holder.linLayout.setVisibility(View.VISIBLE);
          }


//        ref.child(roomFordate+"/생성날짜").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String value = snapshot.child("msg").getValue(String.class);
//
//                if (value == null) {
//                    ChatCount += 1;
//                    Log.e(TAG, "날짜메세지 없다");
//                    Log.e(TAG, "대화중매칭 수 = " + ChatCount + "| DB저장 대화방수= " + (msgItems.size() - items.size()));
//
//                    if(msgItems.size()-items.size() == ChatCount || msgItems.size() == ChatCount) {
////                        // 어레이리스트를 아이템하나만 남기고 Gone으로 해당 아이템레이아웃을 지운다음 allChat텍스트 레이아웃을 보여준다.
//
//                        for (int i = items.size()-1; i > 0; i--){
//                            items.remove(i);
//                        }
//
////
//
//                    }
//                }
//                else if(value != null)
//                {
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "실시간 메세지 cancel");
//            }
//        });
//////////////////////////////// 브로드 캐스트에도 if(!items.get(position).getCday().equals(""))달기//////////////////////////////////////////////////////////////////////////////////

        if(!itemss.get(position).getCday().equals("")) {

            Date currDate = null;
            String DateAfter1h = null;

            try {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

//                             현재 시스템 시간 구하기
                long systemTime = System.currentTimeMillis();
//                             출력 형태를 위한 formmater
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
//
//                               format에 맞게 출력하기 위한 문자열 변환
                String currTime = formatter.format(systemTime);

                currDate = formatter.parse(currTime);

                Log.e(TAG, "해당날짜 = " + itemss.get(ItemPosition).getCday());

                // 매치된날짜//
                Date matchedDate = formatter.parse(itemss.get(ItemPosition).getCday());

                cal.setTime(matchedDate);
//
//                        // 1일 후
                ///////////////////// matchedDate=매치생성날짜에 plusDays(1)=>하루 더한다//////////
                cal.add(Calendar.DATE, 1);
                DateAfter1h = simpleDateFormat.format(cal.getTime());
//
//                        ///////1일 후 날짜//////////////////////////////////////////////////////////////////////////
                Date MdayAfter1 = simpleDateFormat.parse(DateAfter1h);

                hour = (MdayAfter1.getTime() - currDate.getTime()) / (60 * 60 * 1000);
                min = (MdayAfter1.getTime() - currDate.getTime()) / (60 * 1000);

                if (hour > 0) {
                    itemss.get(ItemPosition).setHour(hour);
                    holder.timer.setText("매칭취소까지 " + itemss.get(ItemPosition).getHour() + "시간");
                    Log.e(TAG, "매칭취소까지 " + hour + "시간" + "|| 이름 =" + itemss.get(ItemPosition).getName());

                } else {
                    if (min < 0) {
                        min = 0;
                    }

                    itemss.get(ItemPosition).setHour(min);
                    holder.timer.setText("매칭취소까지 " + itemss.get(ItemPosition).getHour() + "시간");
                    Log.e(TAG, "매칭취소까지 " + min + "분");

                }

                if (min == 0 || min < 0 && !itemss.get(ItemPosition).getHaveMsg().equals("")) {
                    Log.e(TAG, "삭제될 이름 " + itemss.get(position).getName() + "| 메세지있니?== " + itemss.get(ItemPosition).getHaveMsg());

                    //1 푸시알람으로 매칭취소 알린다

                    database = FirebaseDatabase.getInstance();

                    Log.e(TAG, "삭제될 번호 " + itemss.get(position).getPhone() + "| Room== " + roomnum);

                    ref = database.getReference(roomFordate);
                    ref.removeValue();

                    CancelMatch(num, itemss.get(ItemPosition).getPhone(), itemss.get(ItemPosition).getToken(), itemss.get(ItemPosition).getName(), roomnum);
                    Log.e(TAG, "adapter 내부 자동삭제 제어문 동작");
                    Intent love = new Intent(context, Matched_Activity.class);
                    love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    love.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(love);

                }

            }//..try
            catch (ParseException e) {
                e.printStackTrace();
            }

        }//..if  no cday

        if (min == 0) {
            holder.timer.setText("매칭이 취소됬습니다.");
            Log.e(TAG, "min == 0 매칭이 취소됬습니다");

        }


////////////아이템 클릭시 채팅방을 오픈한다////
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemPosition = holder.getAbsoluteAdapterPosition();
//
//                items.remove(ItemPosition);
//                notifyItemRemoved(ItemPosition);
//                notifyItemRangeChanged(ItemPosition, items.size());

                    Intent love = new Intent(v.getContext(), chattingAtcivity.class);
                    love.putExtra("name", itemss.get(ItemPosition).getName());
                    love.putExtra("age", itemss.get(ItemPosition).getAge());
                    love.putExtra("city", itemss.get(ItemPosition).getCity());
                    love.putExtra("hobbies", itemss.get(ItemPosition).getHobbies());
                    love.putExtra("img1", itemss.get(ItemPosition).getImage());
                    love.putExtra("img2", itemss.get(ItemPosition).getImg2());
                    love.putExtra("img3", itemss.get(ItemPosition).getImg3());
                    love.putExtra("img4", itemss.get(ItemPosition).getImg4());
                    love.putExtra("img5", itemss.get(ItemPosition).getImg5());
                    love.putExtra("img6", itemss.get(ItemPosition).getImg6());
                    love.putExtra("intro", itemss.get(ItemPosition).getIntro());
                    love.putExtra("job", itemss.get(ItemPosition).getJob());
                    love.putExtra("school", itemss.get(ItemPosition).getSchool());
                    love.putExtra("distance", itemss.get(ItemPosition).getDistance());
                    love.putExtra("gender", itemss.get(ItemPosition).getGender());
                    love.putExtra("phone", itemss.get(ItemPosition).getPhone());
                    love.putExtra("token", itemss.get(ItemPosition).getToken());
                    love.putExtra("workplace", itemss.get(position).getWorkPlace());

                    /////채팅 로드
//                int mynum = Integer.parseInt(num.replace("+8210", "1"));
//                int somenum = Integer.parseInt(items.get(position).getPhone().replace("+8210", "1"));
//                loadchat(String.valueOf(mynum+somenum));
                    v.getContext().startActivity(love);

            }
        });


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemCount() {
        return itemss.size();
    }

    public void  filterList(ArrayList<itemMoFormsg> filteredList) {
//        if(!Controllfilter){
            itemss = filteredList;
            notifyDataSetChanged();

//        }

    }

    public void AutoMatchCancel(long hour,long min,int po) {
       int Mynum2 = Integer.parseInt(num.replace("+8210", "2"));
       int somenum2 = Integer.parseInt(itemss.get(po).getPhone().replace("+8210", "2"));
       String removeRoom = String.valueOf(Mynum2+somenum2);

       int Mynum = Integer.parseInt(num.replace("+8210", "1"));
       int somenum = Integer.parseInt(itemss.get(po).getPhone().replace("+8210", "1"));
       String Room = String.valueOf(Mynum+somenum);

        letsgetDate(num);
       if (hour > 0) {
           itemss.get(po).setHour(hour);
           notifyItemChanged(po);

           Log.e(TAG, "매칭취소까지 " + hour + "시간"+"| 이름 ="+itemss.get(po).getName());

       } else {
           if (min < 0) {
               min = 0;
           }

           itemss.get(po).setHour(min);
           notifyItemChanged(po);
           Log.e(TAG, "매칭취소까지 " + min + "분"+" | 이름 ="+itemss.get(po).getName());

       }

           for (int l = 0; l < MdateAndRoom.size(); l++){

               if(MdateAndRoom.get(l).getRoom().equals(itemss.get(po).getRoom())){

           if (min == 0 || min < 0) {

               //1 푸시알람으로 매칭취소 알린다

               database = FirebaseDatabase.getInstance();

               Log.e(TAG, "삭제될 번호 " + itemss.get(po).getPhone() + " 삭제매칭num= " + removeRoom + "| Room== " + Room);

               ref = database.getReference(removeRoom);
               ref.removeValue();

               Log.e(TAG, "브로드캐스트 메소드AutoMatchCancel 동작");

               CancelMatch(num, itemss.get(po).getPhone(), itemss.get(po).getToken(), itemss.get(po).getName(), Room);

               Intent love = new Intent(context, Matched_Activity.class);
               love.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               love.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               context.startActivity(love);

               }
           }
       }

    }//..void


    // 여기서 통신을한다.
    private static void CancelMatch(String Myphone, String phonewithlikes, String tk, String Name, String roomnum) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1") ) {

                        try{
                            cancelFCM(tk,Name,roomnum);

                            //매칭날짜삭제 RemoveMday
                            RemoveMday(roomnum);

                            Log.e("매칭삭제응답 =", "매칭삭제");
//////////////////////////////////////////////////////////////////////////////////////////
//                            database = FirebaseDatabase.getInstance();
//                            ref = database.getReference("alreadyChat|"+roomFordate);
//                            ref.removeValue();
                            ref = database.getReference();
                            LatestMsg latestMsg = new LatestMsg("매칭이 취소됬습니다..",Myphone);
                            ref.child(roomnum+"/최신메세지").setValue(latestMsg);



                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {

                        Log.d("응답", "매칭취소 실패");

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        CancelMatch add=new CancelMatch(Myphone,phonewithlikes ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(add);
    }
//////////////////////////////////CancelMatch////////////////////////////////////////////////////

// 여기서 통신을한다.
private static void RemoveMday(String roomnum) {
    Response.Listener<String> responseListener = new Response.Listener<String>() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String success = jsonObject.getString("success");
                if (success.equals("1") ) {

                    Log.d("응답", "매치데이삭제");
                }else {

                    Log.d("응답", "매치데이삭제 실패");

                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };
    RemoveMday add=new RemoveMday(roomnum ,responseListener);
    RequestQueue queue = Volley.newRequestQueue(context);
    queue.add(add);
}
///////////////////////////RemoveMday/////////////////////////////////////////

    /////////////////letsgetDate//////////////////////////////////
    public static void letsgetDate(String myphone) {

        String TAG_JSON = "webnautes";
        String TAG_MatchDate = "Mday";
        String TAG_room = "room";

        MdateAndRoom = new ArrayList<CouplesAndchat.MdateAndRoom>();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    Log.d(TAG, "GetDate 어레이 수: d=" + jsonArray.length() + "개");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);

                        String Mday = item.getString(TAG_MatchDate);
                        String room = item.getString(TAG_room);

                        ///// 방번호와 방생성날자를 어레이에 넣어서 비교한다/////
                        MdateAndRoom.add(new MdateAndRoom(Mday,room));
                        Log.e(TAG, "매치데이 수 = "+MdateAndRoom.size());
                        Log.e(TAG, "방번호 = "+MdateAndRoom.get(i).getRoom());
                        //////////////////////////////////////////////////////////

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        GetMatchDay add =new GetMatchDay(responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(add);
    }
/////////////////GetDate//////////////////////////////////
public void Getdate(String myphone) {

    String TAG_JSON = "webnautes";
    String TAG_MatchDate = "Mday";
    String TAG_room = "room";

    MdateAndRoom = new ArrayList<CouplesAndchat.MdateAndRoom>();

    Response.Listener<String> responseListener = new Response.Listener<String>() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                Log.d(TAG, "GetDate 어레이 수: d=" + jsonArray.length() + "개");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String Mday = item.getString(TAG_MatchDate);
                    String room = item.getString(TAG_room);

                    ///// 방번호와 방생성날자를 어레이에 넣어서 비교한다/////
                    MdateAndRoom.add(new MdateAndRoom(Mday,room));
                    Log.e(TAG, "매치데이 수 = "+MdateAndRoom.size());
                    Log.e(TAG, "방번호 = "+MdateAndRoom.get(i).getRoom());
                    //////////////////////////////////////////////////////////

                }

                if(itemss.size() != jsonArray.length() || MdateAndRoom.size() != 0){
                    loadmamberInfo(myphone);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };
    GetMatchDay add =new GetMatchDay(responseListener);
    RequestQueue queue = Volley.newRequestQueue(context);
    queue.add(add);
}
    ////////////////////////////////GetDate////////////////////////////////////////

    ////////////////////////////loadmamberInfo/////////////////////////////////////////////////
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

        itemss.clear();

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

                        if(mylike.size() > 0) {

                            somelikeme = findmylove(phone);

                            int mynum = Integer.parseInt(myphone.replace("+8210", "1"));
                            int somenum = Integer.parseInt(phone.replace("+8210", "1"));
                            String loc = Integer.toString(mynum+somenum);


//////////////////////////////////////////////// 아래 리사이클러뷰 들어갈 유저 데이터 걸러내기 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                            for (int z = 0; z < somelist.length; z++) {

                                if (somelist[z].equals(myphone)) {

                                    if (somelikeme != null) {
                                        Log.e(TAG, "somelikeme 오브젝트= " + name+"|| "+phone);

                                        for (int m = 0; m <  MdateAndRoom.size(); m++){
                                            if(MdateAndRoom.get(m).getRoom().equals(loc)) {
                                                Mdate = MdateAndRoom.get(m).getMday();
                                                Log.e(TAG, "매치생성날짜= " + Mdate+" |번호= "+phone);


                                                itemss.add(new itemMoFormsg(phone, img, gender, school, intro, job, name, age, location, hobbies, token, distance + " km", img2, img3, img4, img5, img6, somelist, unmatched, Mdate, "", NEWmsg, loc, 24,workPlace));
                                                Log.e(TAG, "items 넣은 폰= " + itemss.size());
                                            }//..if
                                        }//..  for (int m = 0; m <  MdateAndRoom.size(); m++)


                                    }//..if (somelikeme != null)

                                }
                            }

                        }//..,Likes if문
                    } // for.....

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e1) {
                    e1.printStackTrace();
                }

                //////// 최신매칭순서로 나열 정리////////////
                Collections.sort(itemss, new Matched_Activity.SortByDate());
                Collections.reverse(itemss);
                for (int p = 0; p <  itemss.size(); p++){
                    Log.e(TAG, "The Object after sorting is : " +  itemss.get(p).getName()+"| "+ itemss.get(p).getCday()+" || "+itemss.get(p).getTextFornums());
                }
                //////// 최신매칭순서로 나열 정리////////////


                Log.e(TAG, "items.size= "+ itemss.size());
                notifyDataSetChanged();
            }//..onResponse

        };
        loadInfo add = new loadInfo(responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(add);

    }
////////////////////////////////////cancelFCM////////////////////////////////////////////////////
    static void cancelFCM(String token, String myname, String room){
        Gson gson = new Gson();

        NotiModel notiModel = new NotiModel();
        notiModel.to = token;
        notiModel.data.title = "매칭취소.";
        notiModel.data.text = myname +  "님과 매칭이취소됬습니다";
        notiModel.data.clickAction = room;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notiModel));

        Request request = new Request.Builder().header("Content-Type","application/json")
                .addHeader("Authorization", "key=AAAAZGdBsr4:APA91bEFvMXbmYJfPKEmFyPu-vUkjfoxf1rusZwYiDWG1gGDox2qtpmPtQ2iUi-F5WGbHCjl2Fr8P_ELpE8PX0BPDwaaSDYf8ijriPpEWD0rRach955loHmmMmGgjCEvkC8gLEZ2QqFt")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Toast.makeText(MatchingActivity.this, "푸시메세지 실패", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {


            }
        });
    }

    ////////////////////////////////////cancelFCM////////////////////////////////////////////////////

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linLayout,allCaht;
        TextView name,timer;
        ImageView image;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            linLayout = itemView.findViewById(R.id.matched_layout);
            image = itemView.findViewById(R.id.img_couple);
            name = itemView.findViewById(R.id.couple_name);
            timer = itemView.findViewById(R.id.couple_timer);
//            allCaht = itemView.findViewById(R.id.Layout_allChat);

        }
    }


}
