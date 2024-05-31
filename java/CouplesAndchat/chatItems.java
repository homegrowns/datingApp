package CouplesAndchat;

import android.graphics.Bitmap;
import android.net.Uri;

public class chatItems {
    public String date,room,time;
    public String message;
    public String nickname;
    public Bitmap Mybitmap;
    public Uri partnerImg,receivedImg;
    public boolean isMe,Imgok,load,conn;

    public chatItems(String date, String room, String time, String message, String nickname, Bitmap mybitmap, Uri partnerImg, Uri receivedImg, boolean isMe, boolean imgok, boolean load, boolean conn) {
        this.date = date;
        this.room = room;
        this.time = time;
        this.message = message;
        this.nickname = nickname;
        Mybitmap = mybitmap;
        this.partnerImg = partnerImg;
        this.receivedImg = receivedImg;
        this.isMe = isMe;
        Imgok = imgok;
        this.load = load;
        this.conn = conn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Bitmap getMybitmap() {
        return Mybitmap;
    }

    public void setMybitmap(Bitmap mybitmap) {
        Mybitmap = mybitmap;
    }

    public Uri getPartnerImg() {
        return partnerImg;
    }

    public void setPartnerImg(Uri partnerImg) {
        this.partnerImg = partnerImg;
    }

    public Uri getReceivedImg() {
        return receivedImg;
    }

    public void setReceivedImg(Uri receivedImg) {
        this.receivedImg = receivedImg;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public boolean isImgok() {
        return Imgok;
    }

    public void setImgok(boolean imgok) {
        Imgok = imgok;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public boolean isConn() {
        return conn;
    }

    public void setConn(boolean conn) {
        this.conn = conn;
    }
}
