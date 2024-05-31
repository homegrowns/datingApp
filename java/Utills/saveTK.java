package Utills;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class saveTK extends StringRequest {

    final static private String URL ="http://13.125.157.230/saveTK.php";

    private Map<String,String> map;

    public saveTK(String tk, String phone, Response.Listener<String>listener) {
        super(Request.Method.POST, URL, listener, null);

        map=new HashMap<>();
        map.put("token", tk);
        map.put("phone", phone);


    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
