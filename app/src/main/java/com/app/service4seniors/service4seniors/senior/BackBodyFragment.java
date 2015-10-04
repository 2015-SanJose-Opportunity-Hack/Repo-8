package com.app.service4seniors.service4seniors.senior;

/**
 * Created by ymoswal on 10/4/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.service4seniors.service4seniors.R;



/**
 * Created by ymoswal on 10/4/2015.
 */
public class BackBodyFragment extends Fragment {
    public BackBodyFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.back_body, container, false);
    }
}
