package com.teamup.teamup;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Rushabh on 12/5/15.
 */
public class Service extends Application {

    private static Service ourInstance = new Service();
    private RequestQueue reqeustQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        reqeustQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized Service getInstance() {
        return ourInstance;
    }

    public RequestQueue getRequestQueue() {
        return reqeustQueue;
    }

    public Context getContext() {
        return ourInstance;
    }
}
