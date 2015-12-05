package com.teamup.teamup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by rohan on 2/12/15.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent openLandingPage = new Intent(Splash.this,CardListActivity.class);
                    startActivity(openLandingPage);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

