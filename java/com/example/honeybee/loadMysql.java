package com.example.honeybee;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class loadMysql extends Thread {

    public static boolean active=false;
    Handler mHandler;
    String url=null;
    String serverIP="http://13.125.157.230";  //change yours
//    String phppath="C:/Users/ggamb/AndroidStudioProjects/Honeybee";
    String load_url="/loadImage.php";


    public loadMysql(){ //이미지 추가
        url=serverIP+load_url;
        mHandler= new Handler(Looper.getMainLooper());
        Log.e("url",url);


    }



    /**
     * Calls the <code>run()</code> method of the Runnable object the receiver
     * holds. If no Runnable is set, does nothing.
     *
     * @see Thread#start
     */
    @Override
    public void run() {
        super.run();
        if(active){

            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL phpUrl = new URL(url);

                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Content-Length", Integer.toString(url.length()));

                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while ( true ) {
                            String line = br.readLine();
                            if ( line == null )
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }

        }



    }



}