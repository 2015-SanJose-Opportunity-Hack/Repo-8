package com.app.service4seniors.service4seniors.senior;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.service4seniors.service4seniors.R;


/**
 * Created by ymoswal on 10/4/2015.
 */
public class FrontBodyFragment extends Fragment {


    public FrontBodyFragment(){

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inputFragmentView = inflater.inflate(R.layout.front_body, container, false);
        View frontHead = inputFragmentView.findViewById(R.id.frontHeadView);
        final View chest = inputFragmentView.findViewById(R.id.chestView);
        final View neck = inputFragmentView.findViewById(R.id.neckView);
        final View stomach = inputFragmentView.findViewById(R.id.stomachView);
        final View leftHand = inputFragmentView.findViewById(R.id.leftHandView);
        final View rightHand = inputFragmentView.findViewById(R.id.rightHandView);
        final View leftThigh = inputFragmentView.findViewById(R.id.leftThighView);
        final View rightThigh = inputFragmentView.findViewById(R.id.rightThighView);
        final View leftLeg = inputFragmentView.findViewById(R.id.leftLegView);
        final View rightLeg = inputFragmentView.findViewById(R.id.rightLegView);

        frontHead.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

               /* int x = (int)event.getX();
                int y = (int)event.getY();
                int pixel = bitmap.getPixel(x, y);*/

                    String coords = "head selected";//"Touch coordinates : " +
                    //String.valueOf(event.getX()) + "x" + String.valueOf(event.getY());
                getFragmentManager().popBackStack();
                    Toast.makeText(getActivity(), coords, Toast.LENGTH_SHORT).show();


            }
        });
        chest.setOnTouchListener(new MyOnTouchListener());
        neck.setOnTouchListener(new MyOnTouchListener());

        return inflater.inflate(R.layout.front_body, container, false);
    }

    class MyOnTouchListener implements View.OnTouchListener {

        public boolean onTouch(View v, MotionEvent e){
            int action = e.getAction();
            switch(action){
                case (0):
                    FrontBodyFragment.this.touchDown(v);
                    int x = (int)e.getX();
                    int y = (int)e.getY();
                    //int pixel = bitmap.getPixel(x, y);

                    break;
            }
            return true;
        }

    }

    public void touchDown(View v){
        //touch down happened
        int resId;
        String bodyPart;
        switch(v.getId()){
            case R.id.frontHeadView:
                bodyPart = "head selected";
                Toast.makeText(getActivity(), bodyPart,Toast.LENGTH_SHORT).show();
                break;
            case R.id.neckView:
                bodyPart = "neck selected";
                Toast.makeText(getContext(), bodyPart, Toast.LENGTH_SHORT).show();
                break;
            case R.id.chestView:
                bodyPart = "chest view selected";
                Toast.makeText(getContext(), bodyPart, Toast.LENGTH_SHORT).show();
                break;
            case R.id.stomachView:
                bodyPart = "stomach view selected";
                Toast.makeText(getContext(), bodyPart, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
