package Profile;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import Utills.IPadress;

public class CancelallMatch extends StringRequest {
    static IPadress ipadress = new IPadress();
    private static String IP_ADDRESS = ipadress.ip.IP_Adress;
    final static private String URL =IP_ADDRESS +"/CancelAllMatch.php";

    private Map<String,String> map;

    public CancelallMatch(String mynum, Response.Listener<String>listener) {
        super(Method.POST, URL, listener, null);

        map=new HashMap<>();
        map.put("phone", mynum);



    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}