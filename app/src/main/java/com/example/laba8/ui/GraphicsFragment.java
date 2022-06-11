package com.example.laba8.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.laba8.EditOrganizationActivity;
import com.example.laba8.R;

import java.util.ArrayList;

import Network.NetworkManager;
import Organizations.Organization;

public class GraphicsFragment extends Fragment {

    private HorizontalScrollView scrollView;
    private int canvasWidth = 3000,
                canvasHeight = 3000;

    public GraphicsFragment() {
        // Required empty public constructor
    }

    public GraphicsFragment(Context context){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //context = MainActivity.context;
        View v = inflater.inflate(R.layout.fragment_graphics, container, false);
        scrollView = v.findViewById(R.id.horizontalView);
        scrollView.addView(new Draw(getContext(), canvasWidth, canvasHeight));

        return v;
    }

    private class Draw extends View implements View.OnTouchListener {

        private Paint paint = new Paint();
        private int canvasWidth, canvasHeight;
        private ArrayList<Organization> organizations = null;

        public Draw(Context context, int canvasWidth, int canvasHeight) {
            super(context);
            this.canvasWidth = canvasWidth;
            this.canvasHeight = canvasHeight;
            setOnTouchListener(this);

            if (organizations == null){
                Organization.resetCounter();
                organizations = new ArrayList<>();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] orgs = NetworkManager.getInstance().executeCommand("info").split("\n");
                        for (int i = 2; i < orgs.length; i++){
                            String[] params = orgs[i].split(",");
                            try {
                                organizations.add(new Organization(
                                        params[0],
                                        params[1],
                                        Long.valueOf(params[2]),
                                        Long.parseLong(params[3]),
                                        Integer.parseInt(params[4]),
                                        params[5],
                                        params[6],
                                        params[7],
                                        Long.valueOf(params[8]),
                                        Long.valueOf(params[9]),
                                        params[10],
                                        params[11]
                                ));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // Compute the height required to render the view
            // Assume Width will always be MATCH_PARENT.
            int width = canvasWidth;
            int height = canvasHeight + 50; // 50 for padding.
            setMeasuredDimension(width, height);
        }

        @Override
        public void onDraw(Canvas canvas) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            for (Organization o : organizations){
                canvas.drawCircle(o.getX(), o.getY(), 60, paint);
            }
        }


        @Override
        public boolean onTouch(View view, MotionEvent event) {
            for (Organization o : organizations){
                if (intersectsCircle((int)o.getX().longValue(), (int)o.getY(), 60, event.getX(), event.getY())){
                    //TODO: переход на редактирование
                    Intent i = new Intent(getContext(), EditOrganizationActivity.class);
                    i.putExtra("ID", String.valueOf(o.getID()));
                    i.putExtra("NAME", String.valueOf(o.getName()));
                    i.putExtra("FULL_NAME", String.valueOf(o.getFullName()));
                    i.putExtra("X", String.valueOf(o.getX()));
                    i.putExtra("Y", String.valueOf(o.getY()));
                    i.putExtra("ANNUAL_TURNOVER", String.valueOf(o.getAnnualTurnover()));
                    i.putExtra("ORGANIZATION_TYPE", String.valueOf(o.getType()));
                    i.putExtra("STREET", String.valueOf(o.getStreet()));
                    i.putExtra("ZIP_CODE", String.valueOf(o.getZipCode()));
                    i.putExtra("LOCATION_X", String.valueOf(o.getLocationX()));
                    i.putExtra("LOCATION_Y", String.valueOf(o.getLocationY()));
                    i.putExtra("LOCATION_NAME", String.valueOf(o.getLocationName()));
                    i.putExtra("OWNER", String.valueOf(o.getOwner()));
                    startActivity(i);
                    break;
                }
            }

            return true;
        }

        private boolean intersectsCircle (int xC, int yC, int rC, float x, float y) {
            return Math.pow(xC - x, 2) + Math.pow(yC - y, 2) < Math.pow(rC, 2);
        }
    }
}