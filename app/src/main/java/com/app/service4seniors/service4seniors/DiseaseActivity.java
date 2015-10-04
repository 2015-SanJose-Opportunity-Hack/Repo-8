package com.app.service4seniors.service4seniors;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.service4seniors.service4seniors.senior.Me;
import com.app.service4seniors.service4seniors.server.NodejsCall;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.util.Date;

public class DiseaseActivity extends AppCompatActivity {

    private ListView diseaseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        diseaseListView = (ListView) findViewById(R.id.disease_list);
        diseaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Me.getInstance().getType().equals("senior")) {
                    String disease = diseaseListView.getItemAtPosition(position).toString();
                    Toast.makeText(DiseaseActivity.this, "Sent!!", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private class DiseaseSend extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String path = "/lovedOne/" + Me.getInstance().getPid() + "/healthissue";

            JSONObject jsonObject = new JSONObject();
            try {
            jsonObject.put("token", Me.getInstance().getPid());
            jsonObject.put("disease", params[0]);
            jsonObject.put("severity", "Medium");

                jsonObject.put("date", new Date());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonObject1 = NodejsCall.post(path, jsonObject);
            return jsonObject1;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_disease, menu);
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
