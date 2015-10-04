package com.app.service4seniors.service4seniors.senior;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.service4seniors.service4seniors.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by sumitvalecha on 10/3/15.
 */
public class SeniorListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Senior> seniorList;

    public SeniorListAdapter(Context context, List<Senior> seniorList) {
        this.context = context;
        this.seniorList = seniorList;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return seniorList.size();
    }

    @Override
    public Object getItem(int position) {
        return seniorList.get(position);
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

        final Senior senior = seniorList.get(position);

        myViewHolder.seniorName.setText(senior.getName());
        if(senior.isAnyNew()) {
            myViewHolder.seniorRow.setBackgroundColor(Color.GREEN);
        } else {
            myViewHolder.seniorRow.setBackgroundColor(Color.WHITE);
        }

        myViewHolder.phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + senior.getPhone()));
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class MyViewHolder {

        private ImageView seniorPic;
        private TextView seniorName;
        private ImageView seniorNotification;
        private LinearLayout seniorRow;
        private ImageView phoneButton;

        public MyViewHolder(View v) {
            seniorPic = (ImageView) v.findViewById(R.id.senior_pic);
            seniorName = (TextView) v.findViewById(R.id.senior_name);
            seniorNotification = (ImageView) v.findViewById(R.id.senior_notification);
            seniorRow = (LinearLayout) v.findViewById(R.id.senior_row);
            phoneButton = (ImageView) v.findViewById(R.id.phone_button);
        }
    }
}
