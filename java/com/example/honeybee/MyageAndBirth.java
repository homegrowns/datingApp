package com.example.honeybee;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import Utills.IPadress;

public class MyageAndBirth extends StringRequest {
    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;
    final static private String URL =IP_ADDRESS+"/Imageinsert.php";

        private Map<String,String> map;

        public MyageAndBirth(String phone, int mage,String name,String imgPath,String bitmap1, Response.Listener<String>listener) {
            super(Method.POST, URL, listener, null);

            map=new HashMap<>();
            map.put("phone", phone);
            map.put("name", name);

            map.put("myimg", imgPath);
            map.put("file", bitmap1);
            map.put("mage", String.valueOf(mage));

        }

        @Nullable
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }