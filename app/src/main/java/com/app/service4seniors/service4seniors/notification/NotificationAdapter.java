package com.app.service4seniors.service4seniors.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.service4seniors.service4seniors.R;
import com.app.service4seniors.service4seniors.senior.Senior;

import java.util.List;

/**
 * Created by sumitvalecha on 10/4/15.
 */
public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        layoutInflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.senior_list_row, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        Notification notification = notificationList.get(position);

        myViewHolder.task.setText(notification.getTask());
        myViewHolder.senior.setText(notification.getSender());

        return convertView;
    }

    public class MyViewHolder {
        private TextView task;
        private TextView senior;

        public MyViewHolder(View view) {
            task = (TextView) view.findViewById(R.id.task);
            senior = (TextView) view.findViewById(R.id.senior);
        }
    }
}
