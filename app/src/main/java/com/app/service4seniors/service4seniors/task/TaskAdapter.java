package com.app.service4seniors.service4seniors.task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.service4seniors.service4seniors.R;
import com.app.service4seniors.service4seniors.senior.Me;
import com.app.service4seniors.service4seniors.server.NodejsCall;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by sumitvalecha on 10/4/15.
 */
public class TaskAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.daily_task_row, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Task task = taskList.get(position);

        myViewHolder.taskName.setText(task.getTask());
        myViewHolder.taskDate.setText(task.getDate());

        if(!Me.getInstance().getType().equals("senior")) {
            myViewHolder.done.setVisibility(View.INVISIBLE);
        } else {

            myViewHolder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myViewHolder.done.setEnabled(false);
                    new TaskDone().execute(task.get_id());
                }
            });
        }

        return convertView;
    }

    private class TaskDone extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String pid = params[0];
            String url = "/lovedOne/" + Me.getInstance().getPid() + "/tasks/" + pid + "/status/done";

            JSONObject jsonObject = NodejsCall.post(url, null);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

        }
    }

    private class MyViewHolder {
        private TextView taskName;
        private TextView taskDate;
        private Button done;

        public MyViewHolder(View view) {
            taskName = (TextView) view.findViewById(R.id.task_name);
            taskDate = (TextView) view.findViewById(R.id.task_date);
            done = (Button) view.findViewById(R.id.done);
        }
    }
}
