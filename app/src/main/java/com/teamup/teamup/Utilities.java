package com.teamup.teamup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Rushabh on 12/5/15.
 */
public class Utilities {
    public static String createUrl(String baseUrl, HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder(baseUrl);

        Iterator iterator = params.entrySet().iterator();
        builder.append("?");
        while(iterator.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry)iterator.next();
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }

        String url = builder.toString();
        url = url.substring(0, url.length()-1); //Trim the last &
        return url;
    }

    public static void handleErrors(String message, VolleyError error) {
        System.out.println(message);
        error.printStackTrace();
    }

    public static void handleException(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }
}
