package com.app.service4seniors.service4seniors.gcm;

/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.service4seniors.service4seniors.MainActivity;
import com.app.service4seniors.service4seniors.NotificationActivity;
import com.app.service4seniors.service4seniors.R;
import com.app.service4seniors.service4seniors.senior.Me;
import com.app.service4seniors.service4seniors.server.SeniorDetailActivity;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class MyGCMListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);


        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = null;
        if(!Me.getInstance().getType().equals("senior")) {
            intent = new Intent(this, NotificationActivity.class);
        } else {
            intent = new Intent(this, SeniorDetailActivity.class);
            intent.putExtra("pid", Me.getInstance().getPid());
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String msg = "";
        String date = "";
        try {
            msg = jsonObject.getString("msg");
            date = jsonObject.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NotificationCompat.Builder notificationBuilder = null;

            notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(msg)
                    .setContentText(date)
                    .setSmallIcon(R.drawable.icon)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
