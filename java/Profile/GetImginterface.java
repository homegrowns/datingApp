package Profile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetImginterface
{
    @GET("v2/list")
    Call<String> phone_call(
            @Query("phone") String phone

    );
}
