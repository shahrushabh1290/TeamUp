package com.teamup.teamup;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by rohan on 19/12/15.
 */
public class EditInterests extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_interests);
        final TextView getInterests = (TextView) findViewById(R.id.et_get_interests);

        Intent myintent = getIntent();
        final String userId = myintent.getStringExtra("user_id");
        displayInterests(userId);


        FancyButton editInterests = (FancyButton) findViewById(R.id.btn_updateInterests);
        FancyButton removeInterests = (FancyButton) findViewById(R.id.btn_removeInterests);

        editInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = getInterests.getText().toString().trim();
                List<String> updateInterestList = Arrays.asList(str.split(","));
                updateInterests(updateInterestList, userId);
            }
        });

        removeInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=getInterests.getText().toString().trim();
                List<String> updateInterestList = Arrays.asList(str.split(","));
                removeInterests(updateInterestList,userId);
            }
        });
    }

    protected void displayInterests(String userId) {
        final TextView showInterests=(TextView) findViewById(R.id.tv_display_interests);
        HashMap<String, String> params = new HashMap<>();
        //REMOVE THIS
        userId="0";
        //
        params.put("user_id", userId);
        String url = Utilities.createUrl("http://7f9c7ffd.ngrok.com/get_interest", params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String interestsString = response.getString("interests");
                            showInterests.setText(interestsString);
                        }
                        catch(Exception e) {
                            System.out.println("There was an parsing json for get_interests");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utilities.handleErrors("Fetching events failed", error);
                    }
                });

        // Add the request to the queue
        Service.getInstance().getRequestQueue().add(stringRequest);
    }

    protected void updateInterests(final List<String> interests, final String userId) {

        String url = "http://7f9c7ffd.ngrok.com/add_interest";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Result handling
                        System.out.println(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong in update event!");
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray interestArray = new JSONArray(interests);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_fb_id", userId);
                params.put("new_interest", interestArray.toString());
                return params;
            }
        };

        // Add the request to the queue
        Service.getInstance().getRequestQueue().add(stringRequest);

    }


    protected void removeInterests(final List<String> interests, final String userId) {

        String url = "http://7f9c7ffd.ngrok.com/remove_interest";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Result handling
                        System.out.println(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong in update event!");
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray interestArray = new JSONArray(interests);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_fb_id", userId);
                params.put("interests_to_remove", interestArray.toString());
                return params;
            }
        };

        // Add the request to the queue
        Service.getInstance().getRequestQueue().add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_simple, menu);
        return true;
    }
}
