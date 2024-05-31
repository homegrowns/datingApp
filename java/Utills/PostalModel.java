package Utills;

import com.google.gson.annotations.SerializedName;

public class PostalModel {

    @SerializedName("imageList")
    String imageList; // 문자열로 변환된 JsonArray를 전달받을 예정

    @SerializedName("urlList")
    String urlList; // 문자열로 변환된 JsonArray를 전달받을 예정

    @SerializedName("cntLike")
    int cntLike;

    @SerializedName("publisher")
    String publisher;

    @SerializedName("uploadDate")
    String uploadDate; // mysql에서 datetime은 문자열로 전송된다.

}
