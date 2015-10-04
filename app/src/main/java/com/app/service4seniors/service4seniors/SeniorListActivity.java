package com.app.service4seniors.service4seniors;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.service4seniors.service4seniors.senior.Me;
import com.app.service4seniors.service4seniors.senior.Senior;
import com.app.service4seniors.service4seniors.senior.SeniorListAdapter;
import com.app.service4seniors.service4seniors.server.NodejsCall;
import com.app.service4seniors.service4seniors.server.SeniorDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeniorListActivity extends AppCompatActivity {

    private List<Senior> seniorList;
    private ListView seniorListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_list);
        seniorListView = (ListView) findViewById(R.id.senior_list);
        seniorList = new ArrayList<>();

        seniorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Senior senior = seniorList.get(position);
                Intent intent = new Intent(SeniorListActivity.this, SeniorDetailActivity.class);
                intent.putExtra("phone", senior.getPhone());
                intent.putExtra("pid", senior.getPid());
                startActivity(intent);
            }
        });

        new SeniorList().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_senior_list, menu);
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

        if (id == R.id.notification) {
            Intent intent = new Intent(SeniorListActivity.this, NotificationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class SeniorList extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            String path = "/lovedOneAll/"+ Me.getInstance().getPid();
            JSONObject jsonObject = NodejsCall.get(path);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Senior senior;
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++) {
                    senior = new Senior(jsonArray.getJSONObject(i).getString("name"),
                            jsonArray.getJSONObject(i).getString("seniorphone"),
                            jsonArray.getJSONObject(i).getString("age"),
                            jsonArray.getJSONObject(i).getString("height"),
                            jsonArray.getJSONObject(i).getString("weight"),
                            jsonArray.getJSONObject(i).getString("address"),
                            jsonArray.getJSONObject(i).getString("token"));

                    seniorList.add(senior);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SeniorListAdapter seniorListAdapter = new SeniorListAdapter(SeniorListActivity.this, seniorList);
            seniorListView.setAdapter(seniorListAdapter);
        }
    }
}
