package com.teamup.teamup;

import android.app.Activity;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by rohan on 15/12/15.
 */
public class CreateEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    GregorianCalendar cal = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        TextView view = (TextView) findViewById(R.id.et_eventDateForm);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateEvent.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        TextView timeView = (TextView) findViewById(R.id.et_eventTimeForm);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CreateEvent.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        TextView timeEndView = (TextView) findViewById(R.id.et_eventEndTimeForm);
        timeEndView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CreateEvent.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        FancyButton createBtn = (FancyButton) findViewById(R.id.btn_createEvent);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello this event", Toast.LENGTH_SHORT).show();
                createEvent(v);
            }
        });
    }

    private void createEvent(View v) {
        String url = "http://7f9c7ffd.ngrok.com/create_event";

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
                System.out.println("Something went wrong!");
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                EditText name = (EditText) findViewById(R.id.et_eventNameForm);
                EditText desc = (EditText) findViewById(R.id.et_eventDescForm);
                EditText tags = (EditText) findViewById(R.id.et_eventTagForm);
                EditText location = (EditText) findViewById(R.id.et_eventLocForm);
                EditText nppl = (EditText) findViewById(R.id.tv_eventAttendanceVar);


                Address locAddress=null;

                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> address;

                try {
                    address = coder.getFromLocationName(location.getText().toString(),5);
                    if (address==null) {
                        return null;
                    }
                    locAddress=address.get(0);
                }
                catch (Exception e){

                }

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", "0");
                params.put("title", name.getText().toString());
                params.put("capacity", nppl.getText().toString());
                params.put("tag", tags.getText().toString());
                params.put("locationRaw", location.getText().toString());
                params.put("description", desc.getText().toString());

                long start_ts=cal.getTimeInMillis();
                params.put("startTime", Long.toString(start_ts));

                params.put("endTime", Long.toString(start_ts));

                if(locAddress != null) {
                    params.put("lat", String.valueOf(locAddress.getLatitude()));
                    params.put("long", String.valueOf(locAddress.getLongitude()));
                }
                else
                {
                    params.put("lat", "0");
                    params.put("long", "0");
                }
                return params;
            }
        };

        // Add the request to the queue
        Service.getInstance().getRequestQueue().add(stringRequest);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.YEAR, year);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_simple, menu);
        return true;
    }

}
