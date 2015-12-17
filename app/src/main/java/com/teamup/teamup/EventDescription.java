package com.teamup.teamup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by rohan on 5/12/15.
 */
public class EventDescription extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_description);

        FancyButton joinBtn = (FancyButton) findViewById(R.id.btn_createEvent);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Add the user to this event here", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
