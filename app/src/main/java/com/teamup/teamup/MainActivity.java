package com.teamup.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class MainActivity extends AppCompatActivity {

    String userId="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent(view);
            }
        });

        getEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void search(View view) {
        String text = "Clicked on Search";
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        getEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent nextActivity = new Intent(MainActivity.this, EditInterests.class);
            nextActivity.putExtra("user_id",userId);
            startActivity(nextActivity);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void getEvents() {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        HashMap<String, String> params = new HashMap<>();
        params.put("lat", "0");
        params.put("long", "0");
        params.put("user_id", "0");
        params.put("search_query", editText.getText().toString().trim());
        params.put("radius", "5000000");
        String url = Utilities.createUrl("http://7f9c7ffd.ngrok.com/display_events", params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        gotEvents(response);
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

    protected void gotEvents(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray("events");
            displayEvents(jsonArray);
        }
        catch(Exception e) {
            Utilities.handleException("Error parsing JSON", e);
        }
        System.out.println(response);
    }

    protected void displayEvents(JSONArray jsonArray) {
        try {
            ArrayList<Card> cards = new ArrayList<Card>();
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject eventJson = jsonArray.getJSONObject(i);
                Event event = new Event(eventJson);
                Card card = new CustomCard(this,event,userId);
                card.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        Intent nextActivity = new Intent(MainActivity.this, EventDescription.class);
                        Bundle extras = new Bundle();
                        extras.putString("user_id",userId);
                        extras.putString("EventVal",eventJson.toString());
                        nextActivity.putExtras(extras);
                        startActivity(nextActivity);
                    }
                });
                cards.add(card);
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
            CardListView listView = (CardListView) this.findViewById(R.id.myList);

            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }
        }
        catch(JSONException e) {
            Utilities.handleException("Error Parsin JSON object", e);
        }
    }

    public void createEvent(View view) {
        Intent nextActivity = new Intent(MainActivity.this, CreateEvent.class);
        startActivity(nextActivity);
    }
}
