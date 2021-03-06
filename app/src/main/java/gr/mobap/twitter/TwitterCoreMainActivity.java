package gr.mobap.twitter;

/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import gr.mobap.Base;
import gr.mobap.R;


public class TwitterCoreMainActivity extends Base {
    private FirebaseAnalytics mFirebaseAnalytics;
    private TwitterLoginButton loginButton;

    /**
     * Constructs an intent for starting an instance of this activity.
     *
     * @param packageContext A context from the same package as this activity.
     * @return Intent for starting an instance of this activity.
     */
    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, TwitterCoreMainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twittercore_activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Set up the login button by setting callback to invoke when authorization request
        // completes
        loginButton = findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //requestEmailAddress(getApplicationContext(), result.data);
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                //TwitterSession session = result.data;
                // Remove toast and use the TwitterSession's userID
                // with your app's user model

                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent i = new Intent(TwitterCoreMainActivity.this, TimelineActivity.class);
                startActivity(i);
            }

            @Override
            public void failure(TwitterException exception) {
                // Upon error, show a toast message indicating that authorization request failed.
                Toast.makeText(getApplicationContext(), exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("TwitterKit", "Login with Twitter failure", exception);

            }
        });

    }

    //   private static void requestEmailAddress(final Context context, TwitterSession session) {
    //      new TwitterAuthClient().requestEmail(session, new Callback<String>() {
    //         @Override
    //        public void success(Result<String> result) {
    //          Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show();
    //    }

    //   @Override
//            public void failure(TwitterException exception) {
    //              Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
    //        }
    //  });
    //}

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TwitterCoreMainActivity.this, TimelineActivity.class);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the saveSession button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    public com.google.firebase.appindexing.Action getAction() {
        return Actions.newView("TwitterCoreMain Page", "http://www.mobap.gr/twittercoremain");
    }

    @Override
    protected void onStart() {
        super.onStart();
   /* If you’re logging an action on an item that has already been added to the index,
   you don’t have to add the following update line. See
   https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index for
   adding content to the index */
        FirebaseAppIndex.getInstance().update();
        FirebaseUserActions.getInstance().start(getAction());
    }

    @Override
    protected void onStop() {
        FirebaseUserActions.getInstance().end(getAction());
        super.onStop();
    }
}