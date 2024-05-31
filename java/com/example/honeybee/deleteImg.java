package com.example.honeybee;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import Utills.IPadress;


public class deleteImg extends StringRequest {
    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;
    final static private String URL =IP_ADDRESS+"removeImg.php";


    private Map<String,String> map;

    public deleteImg(String uri, String phone, Response.Listener<String>listener) {
        super(Method.POST, URL, listener, null);


        map=new HashMap<>();
        map.put("myimg", uri);
        map.put("phone", phone);

    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
