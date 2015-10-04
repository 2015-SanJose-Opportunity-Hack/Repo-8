package com.app.service4seniors.service4seniors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.service4seniors.service4seniors.gcm.QuickstartPreferences;
import com.app.service4seniors.service4seniors.gcm.RegistrationIntentService;
import com.app.service4seniors.service4seniors.senior.Me;
import com.app.service4seniors.service4seniors.server.NodejsCall;
import com.app.service4seniors.service4seniors.server.SeniorDetailActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private TextView mInformationTextView;

    private ImageButton seniorButton;
    private ImageButton caretakerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        //mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        seniorButton = (ImageButton) findViewById(R.id.senior_button);
        caretakerButton = (ImageButton) findViewById(R.id.caretaker_button);

        seniorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SeniorData().execute();

            }
        });

        caretakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Me.getInstance().setType("Caretaker");
                Intent intent = new Intent(MainActivity.this, SeniorListActivity.class);
                startActivity(intent);
            }
        });

    }

//    private class Senior extends AsyncTask<Void, Void, JSONObject> {
//
//        @Override
//        protected JSONObject doInBackground(Void... params) {
//            String url = "/caretaker/" + Me.getInstance().getCareTaker() + "/lovedOne/" + Me.getInstance().getPid();
//
//            JSONObject jsonObject = NodejsCall.get(url);
//            return jsonObject;
//
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            try {
//                Me.getInstance().setName(jsonObject.getString("name"));
//                Me.getInstance().setAge(jsonObject.getString("age"));
//                Me.getInstance().setHeight(jsonObject.getString("height"));
//                Me.getInstance().setPhone(jsonObject.getString("seniorphone"));
//                Me.getInstance().setAddress(jsonObject.getString("address"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private class SeniorData extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            String url = "/caretaker/" + Me.getInstance().getCareTaker() + "/lovedOne";
            JSONObject token = new JSONObject();
            try {
                token.put("seniortoken", Me.getInstance().getPid());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = NodejsCall.post(url, token);
            String url2 = "/caretaker/" + Me.getInstance().getCareTaker() + "/lovedOne/" + Me.getInstance().getPid();

            JSONObject jsonObject2 = NodejsCall.get(url2);
            return jsonObject2;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONObject person = jsonObject.getJSONObject("response");
                Me.getInstance().setName(person.getString("name"));
                Me.getInstance().setAge(person.getString("age"));
                Me.getInstance().setHeight(person.getString("height"));
                Me.getInstance().setPhone(person.getString("seniorphone"));
                Me.getInstance().setAddress(person.getString("address"));
                Me.getInstance().setType("senior");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(MainActivity.this, SeniorDetailActivity.class);
            startActivity(intent);

        }
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }

        /**
         * Check the device to make sure it has the Google Play Services APK. If
         * it doesn't, display a dialog that allows users to download the APK from
         * the Google Play Store or enable it in the device's system settings.
         */
        private boolean checkPlayServices() {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                            .show();
                } else {
                    Log.i(TAG, "This device is not supported.");
                    finish();
                }
                return false;
            }
            return true;
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
