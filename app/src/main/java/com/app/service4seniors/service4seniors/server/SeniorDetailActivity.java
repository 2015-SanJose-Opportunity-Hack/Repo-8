package com.app.service4seniors.service4seniors.server;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.app.service4seniors.service4seniors.R;
import com.app.service4seniors.service4seniors.senior.Me;
import com.app.service4seniors.service4seniors.senior.SeniorPainActivity;
import com.app.service4seniors.service4seniors.task.Task;
import com.app.service4seniors.service4seniors.task.TaskAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeniorDetailActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;
    private String pid;

    private ImageButton diseaseButton;
    private ListView dailyTasks;
    private List<Task> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_detail);

        ImageButton imgButton = (ImageButton)findViewById(R.id.body_pain_button);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        diseaseButton = (ImageButton) findViewById(R.id.disease_button);
        dailyTasks = (ListView) findViewById(R.id.daily_tasks);
        taskList = new ArrayList<>();
        if(!Me.getInstance().getType().equals("senior")) {
            pid = getIntent().getStringExtra("pid");
        } else {
            pid = Me.getInstance().getPid();
        }

        imgButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getBaseContext(), SeniorPainActivity.class);
                startActivity(intent);
            }
        });

        diseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call disease list

            }
        });

        new DailyTask().execute();
    }

    private class DailyTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            String url = "/lovedOne/" + pid + "/tasks";
            JSONObject jsonObject = NodejsCall.get(url);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONArray taskArray = jsonObject.getJSONArray("response");
                for(int i=0;i<taskArray.length();i++) {
                    String task = taskArray.getJSONObject(i).getString("task");
                    String date = taskArray.getJSONObject(i).getString("date");
                    String status = taskArray.getJSONObject(i).getString("status");
                    String _id = taskArray.getJSONObject(i).getString("_id");

                    taskList.add(new Task(_id, task, date, status, null));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            TaskAdapter taskAdapter = new TaskAdapter(SeniorDetailActivity.this, taskList);
            dailyTasks.setAdapter(taskAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_senior_detail, menu);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(!(Me.getInstance().getType()==null)) {
            if (!Me.getInstance().getType().equals("senior")) {
                return;
            }
        }
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("MainActivity", "Call emergency services");
                    new Emergency().execute();
                    senSensorManager.unregisterListener(this);

                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    private class Emergency extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            String url = "/lovedOne/" + Me.getInstance().getPid() + "/emergency";
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = null;
            try {
                jsonObject.put("token", Me.getInstance().getPid());
                jsonObject.put("description", "Emergency");
                jsonObject.put("severity", "Highest");
                jsonObject.put("date", new Date().toString());

                jsonObject1 = NodejsCall.post(url, jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject1;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Toast.makeText(SeniorDetailActivity.this, "Emergency services called", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
