package com.teamup.teamup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohan on 5/12/15.
 */
public class EventDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        TextView eventName = (TextView) findViewById(R.id.tv_eventNameVariable);
        TextView eventTag = (TextView) findViewById(R.id.tv_eventTagsVar);
        TextView eventDesc = (TextView) findViewById(R.id.tv_eventDescVar);
        TextView eventLoc = (TextView) findViewById(R.id.tv_eventLocVar);
        TextView eventTime = (TextView) findViewById(R.id.tv_eventTimeVar);
        TextView eventpplGoing = (TextView) findViewById(R.id.tv_eventAttendanceVar);

        Intent myintent=getIntent();
        JSONObject event = null;
        try {
            event = new JSONObject(myintent.getStringExtra("EventVal"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            eventName.setText(event.getString("title"));
            eventTag.setText("#"+event.getString("tag"));
            eventDesc.setText(event.getString("description"));
            eventLoc.setText(event.getString("locRaw"));
            eventpplGoing.setText(event.getJSONArray("enrolment").length()+"/"+event.getInt("capacity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_simple, menu);
        return true;
    }
}
