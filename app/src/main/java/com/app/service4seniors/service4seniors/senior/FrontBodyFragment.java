package com.app.service4seniors.service4seniors.senior;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.service4seniors.service4seniors.R;
import com.app.service4seniors.service4seniors.server.NodejsCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by ymoswal on 10/4/2015.
 */
public class FrontBodyFragment extends Fragment  {
    String[] emot_text = {"Low","Medium","High"};
    Integer[] emot_image = {R.drawable.oneimg,
            R.drawable.twoimg,
            R.drawable.threeimg};

    String bodyPart;
    String painlevel;

    public FrontBodyFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inputFragmentView = inflater.inflate(R.layout.front_body, container, false);
        final View frontHead = inputFragmentView.findViewById(R.id.frontHeadView);
        final View chest = inputFragmentView.findViewById(R.id.chestView);
        final View neck = inputFragmentView.findViewById(R.id.neckView);
        final View stomach = inputFragmentView.findViewById(R.id.stomachView);
        final View leftHand = inputFragmentView.findViewById(R.id.leftHandView);
        final View rightHand = inputFragmentView.findViewById(R.id.rightHandView);
        final View leftThigh = inputFragmentView.findViewById(R.id.leftThighView);
        final View rightThigh = inputFragmentView.findViewById(R.id.rightThighView);
        final View leftLeg = inputFragmentView.findViewById(R.id.leftLegView);
        final View rightLeg = inputFragmentView.findViewById(R.id.rightLegView);

       /* frontHead.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

               /* int x = (int)event.getX();
                int y = (int)event.getY();
                int pixel = bitmap.getPixel(x, y);

                    String coords = "head selected!!    ";//"Touch coordinates : " +
                    //String.valueOf(event.getX()) + "x" + String.valueOf(event.getY());

                    Toast.makeText(getActivity(), coords, Toast.LENGTH_SHORT).show();


            }
        });*/

        frontHead.setOnTouchListener(new MyOnTouchListener());
        chest.setOnTouchListener(new MyOnTouchListener());
        neck.setOnTouchListener(new MyOnTouchListener());
        stomach.setOnTouchListener(new MyOnTouchListener());
        leftHand.setOnTouchListener(new MyOnTouchListener());
        rightHand.setOnTouchListener(new MyOnTouchListener());
        leftThigh.setOnTouchListener(new MyOnTouchListener());
        rightThigh.setOnTouchListener(new MyOnTouchListener());
        leftLeg.setOnTouchListener(new MyOnTouchListener());
        rightLeg.setOnTouchListener(new MyOnTouchListener());
        return inputFragmentView;
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics

                    builder.setTitle(R.string.dialog_title);
                    final SeniorPainEmoticon adapter = new
                            SeniorPainEmoticon(getActivity(), emot_text, emot_image);
                    builder.setNegativeButton(
                            "cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.setAdapter(adapter,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            painlevel = adapter.getItem(which);
                            new PainPoint().execute();
                        }
                    });

                    builder.show();
                    break;
            }
            return true;
        }

    }

    private class PainPoint extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject1 = new JSONObject();
            String url = "/lovedOne/" + Me.getInstance().getPid() + "/healthissue";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "Body pain");
                jsonObject.put("description", bodyPart);
                jsonObject.put("severity", painlevel);
                jsonObject.put("date", new Date());
                jsonObject.put("token", Me.getInstance().getPid());

                jsonObject1 = NodejsCall.post(url, jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject1;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

        }
    }

    public void touchDown(View v){
        //touch down happened
        int resId;
        switch(v.getId()){
            case R.id.frontHeadView:
                bodyPart = "Head Pain";
                // Toast.makeText(getActivity(), bodyPart,Toast.LENGTH_SHORT).show();
                break;
            case R.id.neckView:
                bodyPart = "Neck Pain";
                // Toast.makeText(getContext(), bodyPart, Toast.LENGTH_SHORT).show();
                break;
            case R.id.chestView:
                bodyPart = "Chest Pain";
                // Toast.makeText(getContext(), bodyPart, Toast.LENGTH_SHORT).show();
                break;
            case R.id.stomachView:
                bodyPart = "Stomach Pain";
                // Toast.makeText(getContext(), bodyPart, Toast.LENGTH_SHORT).show();
                break;
            case R.id.leftHandView:
                bodyPart = "Left hand Pain";
                break;
            case R.id.rightHandView:
                bodyPart = "Right hand Pain";
                break;
            case R.id.rightThighView:
                bodyPart = "Right thigh Pain";
                break;
            case R.id.leftThighView:
                bodyPart = "Left thigh Pain";
                break;
            case R.id.leftLegView:
                bodyPart = "Left Leg Pain";
                break;
            case R.id.rightLegView:
                bodyPart = "Right Leg Pain";
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
