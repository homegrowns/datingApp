package CouplesAndchat;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import Utills.IPadress;

public class CancelMatch extends StringRequest {
    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;
    final static private String URL =IP_ADDRESS +"/CancelMatch.php";

    private Map<String,String> map;

    public CancelMatch(String mynum,String Pnum ,Response.Listener<String>listener) {
        super(Method.POST, URL, listener, null);

        map=new HashMap<>();
        map.put("phone", mynum);
        map.put("pnum", Pnum);



    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}