package com.example.honeybee;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import Utills.IPadress;

public class GetimginRegi extends StringRequest {
    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;
    final static private String URL =IP_ADDRESS+"/GetImgForRegister.php";


    private Map<String,String> map;

    public GetimginRegi(String phone, Response.Listener<String>listener) {
        super(Request.Method.POST, URL, listener, null);


        map=new HashMap<>();
        map.put("phone", phone);

    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
