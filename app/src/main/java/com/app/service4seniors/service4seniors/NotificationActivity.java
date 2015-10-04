package com.app.service4seniors.service4seniors;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.app.service4seniors.service4seniors.notification.Notification;
import com.app.service4seniors.service4seniors.notification.NotificationAdapter;
import com.app.service4seniors.service4seniors.senior.Me;
import com.app.service4seniors.service4seniors.server.NodejsCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private List<Notification> notificationList;
    private ListView notificationListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationList = new ArrayList<>();
        notificationListView = (ListView) findViewById(R.id.notification_list);

        new NotificationList().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    private class NotificationList extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            String url = "/person/" + Me.getInstance().getPid() + "/notifications";
            JSONObject jsonObject = NodejsCall.get(url);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Notification notification;
            try {
                JSONArray notificationArray = jsonObject.getJSONArray("response");
                for (int i=0;i<notificationArray.length();i++){
                    notification = new Notification(notificationArray.getJSONObject(i).getString("msg"),
                            notificationArray.getJSONObject(i).getString("date"),
                            notificationArray.getJSONObject(i).getString("sender"),
                            "",
                            "");

                    notificationList.add(notification);
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }

            NotificationAdapter notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationList);
            notificationListView.setAdapter(notificationAdapter);
        }
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
