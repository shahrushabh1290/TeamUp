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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by rohan on 5/12/15.
 */
public class EventDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        Bundle extras = getIntent().getExtras();
        JSONObject event = null;
        try {
            event = new JSONObject(extras.getString("EventVal"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String userId=extras.getString("user_id");


        FancyButton joinBtn = (FancyButton) findViewById(R.id.btn_joinEvent);
        final JSONObject finalEvent = event;
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Join this event", Toast.LENGTH_SHORT).show();
                try {
                    joinEvent(v,userId, finalEvent.getString("_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        FancyButton leaveBtn = (FancyButton) findViewById(R.id.btn_leaveEvent);
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Leave this event", Toast.LENGTH_SHORT).show();
               // leaveEvent(v);
            }
        });

        TextView eventName = (TextView) findViewById(R.id.tv_eventNameVariable);
        TextView eventTag = (TextView) findViewById(R.id.tv_eventTagsVar);
        TextView eventDesc = (TextView) findViewById(R.id.tv_eventDescVar);
        TextView eventLoc = (TextView) findViewById(R.id.tv_eventLocVar);
        TextView eventTimeStart = (TextView) findViewById(R.id.tv_eventTimeVar);
        TextView eventTimeEnd = (TextView) findViewById(R.id.tv_eventEndTimeVar);
        TextView eventpplGoing = (TextView) findViewById(R.id.tv_eventAttendanceVar);



        try {
            eventName.setText(event.getString("title"));
            eventTag.setText("#"+event.getString("tag"));
            eventDesc.setText(event.getString("description"));
            eventLoc.setText(event.getString("loc_raw"));
            Date date = new Date(Long.parseLong(event.getString("start_time")));
            eventTimeStart.setText(date.toString());
            Date date1 = new Date(Long.parseLong(event.getString("end_time")));
            eventTimeEnd.setText(date1.toString());
            eventpplGoing.setText(event.getJSONArray("enrolment").length()+"/"+event.getInt("capacity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void joinEvent(View v, final String userId, final String eventId) {
        String url = "http://6dbbede.ngrok.com/subscribe";
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
                System.out.println("Something went wrong! in join event");
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", userId);
                params.put("event_id", eventId);

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
