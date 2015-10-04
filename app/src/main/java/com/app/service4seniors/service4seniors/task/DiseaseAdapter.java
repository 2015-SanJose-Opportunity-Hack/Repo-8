package com.app.service4seniors.service4seniors.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.service4seniors.service4seniors.R;
import com.app.service4seniors.service4seniors.notification.NotificationAdapter;

import java.util.List;

/**
 * Created by sumitvalecha on 10/4/15.
 */
public class DiseaseAdapter extends BaseAdapter{

    private LayoutInflater layoutInflater;
    private Context context;
    private List<String> diseaseList;

    public DiseaseAdapter(Context context, List<String> diseaseList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.diseaseList = diseaseList;
    }

    @Override
    public int getCount() {
        return diseaseList.size();
    }

    @Override
    public Object getItem(int position) {
        return diseaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.disease_row, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        myViewHolder.diseaseName.setText(diseaseList.get(position));

        return convertView;

    }

    private class MyViewHolder {
        public TextView diseaseName;

        public MyViewHolder(View v) {
            diseaseName = (TextView) v.findViewById(R.id.disease_name);
        }
    }
}
