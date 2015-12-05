package com.teamup.teamup;

/**
 * Created by rohan on 4/12/15.
 */

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CardListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        //int listImages[] = new int[]{R.drawable.angry_1, R.drawable.angry_2,
        //        R.drawable.angry_3, R.drawable.angry_4, R.drawable.angry_5};

        ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 0; i<5; i++) {
            // Create a Card
            Card card = new Card(this,R.layout.row_card);

            card.setClickable(true);
            final int finalI = i;
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getApplicationContext(),"Clicked on Event : "+ finalI, Toast.LENGTH_LONG).show();
                }
            });
            // Create a CardHeader
            //CardHeader header = new CardHeader(this);
            // Add Header to card
            //header.setTitle("Angry bird: " + i);
            //card.setTitle("sample title");
            //card.addCardHeader(header);

            //CardThumbnail thumb = new CardThumbnail(this);
            //thumb.setDrawableResource(listImages[i]);
            //card.addCardThumbnail(thumb);

            cards.add(card);
        }

        ArrayList<Event> eventList = new ArrayList<>();

        Event event = new Event("Event 1","tag 1");
        eventList.add(event);

        Event event2 = new Event("Event 2","tag 2");
        eventList.add(event2);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

        CardListView listView = (CardListView) this.findViewById(R.id.myList);
        if (listView != null) {
            //listView.setAdapter(mCardArrayAdapter);
            listView.setAdapter(new CustomAdapter(eventList));
        }
    }


    public class CustomAdapter extends BaseAdapter{

        ArrayList<Event> events;
        public CustomAdapter(ArrayList<Event> events) {
            this.events = events;
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int position) {
            return events.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_card_row, parent, false);


            TextView eventTitle = (TextView) rowView.findViewById(R.id.card_eventTitle);
            TextView eventTag = (TextView) rowView.findViewById(R.id.card_eventTag);
            TextView eventTime = (TextView) rowView.findViewById(R.id.card_eventTime);
            TextView eventLocation = (TextView) rowView.findViewById(R.id.card_eventLocation);

            Event event = events.get(position);

            eventTitle.setText(event.getEventName());
            eventTag.setText(event.getEventTag());



            return rowView;
        }
    }
}