package com.teamup.teamup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class SplashActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        startNextActivity();
    }

    protected void startNextActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    protected boolean isLoggedIn() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token != null;
    }

    protected void updateUI() {
        if(isLoggedIn()) {
            startNextActivity();
            return;
        }

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_splash);
        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        //Register callbacks to handle results by the facebook login
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Successfully logged in");
                System.out.println(loginResult.getAccessToken().getToken());
                System.out.println(loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                System.out.println("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("Error while trying to login");
                System.out.println(error);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook SDK needs to be initialized before using it
        FacebookSdk.sdkInitialize(getApplicationContext());

        AccessTokenTracker accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                System.out.println("Access token changed");
                updateUI();
            }
        };
    }
}
