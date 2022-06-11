package com.example.laba8.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba8.EditOrganizationActivity;
import com.example.laba8.R;

import java.util.ArrayList;

import Network.NetworkManager;
import Organizations.Organization;

public class CollectionFragment extends Fragment {

    private TableLayout tableLayout;
    private ArrayList<Organization> organizations = null;
    private RecyclerView table;
    private Context context;
    private boolean[] selected;

    public CollectionFragment() {
        // Required empty public constructor
    }

    public CollectionFragment(Context context){

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
        View v = inflater.inflate(R.layout.fragment_collection, container, false);
        tableLayout = v.findViewById(R.id.tlGridTable);
        selected = new boolean[13];
        context = getContext();
        addInformationToTable();

        for (int i = 0; i < ((TableRow)tableLayout.getChildAt(0)).getChildCount(); i++){
            setOnCLickListeners(i);
        }

        return v;
    }

    public void addInformationToTable(){
        Activity activity = getActivity();
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                if (organizations == null){
                    organizations = new ArrayList<>();
                    Organization.resetCounter();
                    String[] orgs = getInformation().split("\n");
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
                sortTable();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clearTable();
                        for (Organization o : organizations){
                            Log.e("njcdn", o.toCSV());
                            tableLayout.addView(createRow(o),
                                    new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        }
                        Log.e("132", "Updated");
                    }
                });
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortTable(){
        organizations.sort(new Organization.OrgComparator(selected));
    }

    private void clearTable(){
        tableLayout.removeViews(1, tableLayout.getChildCount()-1);
        Organization.resetCounter();
    }

    private String getInformation(){
        return NetworkManager.getInstance().executeCommand("info");
    }

    private TableRow createRow(Organization o){
        TableRow row = new TableRow(context);
        row.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        row.addView(createTextView(String.valueOf(o.getID()), 50));
        row.addView(createTextView(o.getName(), 100));
        row.addView(createTextView(o.getFullName(), 175));
        row.addView(createTextView(String.valueOf(o.getX()), 50));
        row.addView(createTextView(String.valueOf(o.getY()), 50));
        row.addView(createTextView(String.valueOf(o.getAnnualTurnover()), 275));
        row.addView(createTextView(String.valueOf(o.getType()), 275));
        row.addView(createTextView(o.getStreet(), 125));
        row.addView(createTextView(o.getZipCode(), 200));
        row.addView(createTextView(String.valueOf(o.getLocationX()), 200));
        row.addView(createTextView(String.valueOf(o.getLocationY()), 200));
        row.addView(createTextView(o.getLocationName(), 250));
        row.addView(createTextView(o.getOwner(), 150));
        row.setClickable(true);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: переход на активность с изменением
                Intent i = new Intent(context, EditOrganizationActivity.class);
                i.putExtra("ID", ((TextView)row.getChildAt(0)).getText().toString());
                i.putExtra("NAME", ((TextView)row.getChildAt(1)).getText().toString());
                i.putExtra("FULL_NAME", ((TextView)row.getChildAt(2)).getText().toString());
                i.putExtra("X", ((TextView)row.getChildAt(3)).getText().toString());
                i.putExtra("Y", ((TextView)row.getChildAt(4)).getText().toString());
                i.putExtra("ANNUAL_TURNOVER", ((TextView)row.getChildAt(5)).getText().toString());
                i.putExtra("ORGANIZATION_TYPE", ((TextView)row.getChildAt(6)).getText().toString());
                i.putExtra("STREET", ((TextView)row.getChildAt(7)).getText().toString());
                i.putExtra("ZIP_CODE", ((TextView)row.getChildAt(8)).getText().toString());
                i.putExtra("LOCATION_X", ((TextView)row.getChildAt(9)).getText().toString());
                i.putExtra("LOCATION_Y", ((TextView)row.getChildAt(10)).getText().toString());
                i.putExtra("LOCATION_NAME", ((TextView)row.getChildAt(11)).getText().toString());
                i.putExtra("OWNER", ((TextView)row.getChildAt(12)).getText().toString());
                startActivity(i);
                //Log.e("132", ((TextView)row.getChildAt(0)).getText().toString());
            }
        });
        return row;
    }

    private TextView createTextView(String text, float width){
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        textView.setTextColor(Color.BLACK);
        textView.setBackground(getResources().getDrawable(R.drawable.border));
        textView.setGravity(Gravity.CENTER);
        //textView.setLayoutParams(new ViewGroup.LayoutParams(
        //        (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getContext().getResources().getDisplayMetrics()) * width),
        //        ViewGroup.LayoutParams.WRAP_CONTENT
        //));
        return textView;
    }

    private void setOnCLickListeners(int index){
        TableRow row1 = (TableRow)tableLayout.getChildAt(0);
        row1.getChildAt(index).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = ((TextView)row1.getChildAt(index));
                selected[index] = !selected[index];
                if (selected[index])
                    textView.setBackground(context.getResources().getDrawable(R.drawable.selected_border));
                else
                    textView.setBackground(context.getResources().getDrawable(R.drawable.border));
                addInformationToTable();
            }
        });
    }
}