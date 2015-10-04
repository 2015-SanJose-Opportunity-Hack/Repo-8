package com.app.service4seniors.service4seniors.senior;

/**
 * Created by ymoswal on 10/4/2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.service4seniors.service4seniors.R;

public class SeniorPainEmoticon extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public SeniorPainEmoticon(Activity context,
                              String[] web, Integer[] imageId) {
        super(context, R.layout.pain_list_view_row_item, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.pain_list_view_row_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.emoticonText);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.emoticon);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
