package com.teamup.teamup;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by rohan on 4/12/15.
 */
public class CustomCard extends Card {

    protected TextView tvTag;
    protected TextView tvLocation;
    protected TextView tvTime;
    protected TextView tvAttendance;
    protected TextView tvTitle;
    protected Event event;


    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public CustomCard(Context context, Event event , String userId) {
        this(context, R.layout.row_card);
        this.event=event;
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public CustomCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        tvTag = (TextView) parent.findViewById(R.id.card_eventTag);
        tvLocation = (TextView) parent.findViewById(R.id.card_eventLocation);
        tvTime = (TextView) parent.findViewById(R.id.card_eventTime);
        tvTitle = (TextView) parent.findViewById(R.id.card_eventTitle);
        tvAttendance = (TextView) parent.findViewById(R.id.card_eventAttendance);

        tvTag.setText("Tags : #"+event.getTag());
        tvLocation.setText("Location : "+event.getRawLoc());

        if(event.getStartTime() != null) {
            tvTime.setText("Starts at : "+event.getStartTime().getTime().toString());
        }

        tvTitle.setText(event.getTitle().toUpperCase());
        tvAttendance.setText("People going : "+Integer.toString(event.getEnrolment()));

//        final FloatingActionButton fabAddPerson = (FloatingActionButton) view.findViewById(R.id.fab);
//        fabAddPerson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fabAddPerson.setImageResource(R.drawable.ic_done_white);
//                //Toast.makeText(view.getContext(), "Directly add the user to the event", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}