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
import android.content.Intent;
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

//        ArrayList<Card> cards = new ArrayList<Card>();
//
//        for (int i = 0; i<5; i++) {
//            Event event = new Event("Tag"+i,"Location"+i,"Time"+i,"Title"+i,"Attendance"+i);
//            Card card = new CustomCard(this,event);
//
//            card.setOnClickListener(new Card.OnCardClickListener() {
//                @Override
//                public void onClick(Card card, View view) {
//                    Intent openLandingPage = new Intent(CardListActivity.this,EventDescription.class);
//                    startActivity(openLandingPage);
//                    //Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
//                }
//            });
//
//
//            //CardThumbnail thumb = new CardThumbnail(this);
//            //thumb.setDrawableResource(listImages[i]);
//            //card.addCardThumbnail(thumb);
//
//            cards.add(card);
//        }
//
//        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
//
//        CardListView listView = (CardListView) this.findViewById(R.id.myList);
//        if (listView != null) {
//            listView.setAdapter(mCardArrayAdapter);
//        }
    }
}