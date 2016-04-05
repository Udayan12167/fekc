package com.example.shiv.fekc.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.item.ViolationAggregateItem;
import com.example.shiv.fekc.item.ViolationItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit.http.Path;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    private DBAdapter dbAdapter;

    private Spinner spinner;

    private ArrayList<TaskItem> taskItemArrayList;
    private ArrayList<String> stringArrayList;

    private ArrayList<ViolationAggregateItem> violationAggregateItemList;

    private BarChart barChart;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbAdapter = new DBAdapter();

        taskItemArrayList = dbAdapter.getAllTasksFromTaskInfo();
        stringArrayList = new ArrayList<String>();
        for (TaskItem taskItem : taskItemArrayList) {
            stringArrayList.add(taskItem.getTaskName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_layout, stringArrayList);

        spinner = (Spinner) view.findViewById(R.id.fragment_stats_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plotBarChart(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        barChart = (BarChart) view.findViewById(R.id.fragment_stats_bar_chart);
    }

    private void plotBarChart(int position) {
        ArrayList<ViolationItem> violationItemList =
                dbAdapter.getAllViolationsMatchingTaskId(taskItemArrayList.get(position).getTaskID());

        violationAggregateItemList = new ArrayList<ViolationAggregateItem>();
        getAggregatedList(violationItemList);
        for (ViolationAggregateItem violationAggregateItem : violationAggregateItemList) {
            Log.d(getClass().toString(), violationAggregateItem.toString());
        }
        barChart.setData(getBarData());
        barChart.setDescription("My Chart");
        barChart.animateXY(2000, 2000);
        barChart.invalidate();


    }

    private void getAggregatedList(ArrayList<ViolationItem> violationItemList) {
        ArrayList<ViolationAggregateItem> violationAggregateItemList = new ArrayList<ViolationAggregateItem>();
        for (ViolationItem violationItem : violationItemList) {
            add(violationItem);
        }
    }

    private void add(ViolationItem violationItem) {
        for (ViolationAggregateItem violationAggregateItem : violationAggregateItemList) {
            if (violationAggregateItem.getDate().equals(violationItem.getDate())) {
                if (violationItem.getViolationType() == Constants.WIN_CODE) {
                    violationAggregateItem.increaseWins();
                } else {
                    violationAggregateItem.increaseViolations();
                }
                return;
            }
        }
        ViolationAggregateItem violationAggregateItem = new ViolationAggregateItem();
        violationAggregateItem.setDate(violationItem.getDate());
        if (violationItem.getViolationType() == Constants.WIN_CODE) {
            violationAggregateItem.increaseWins();
        } else {
            violationAggregateItem.increaseViolations();
        }
        violationAggregateItemList.add(violationAggregateItem);
    }

    private BarData getBarData() {
        ArrayList<BarEntry> violationSet = new ArrayList<>();
        ArrayList<BarEntry> winSet = new ArrayList<>();
        ArrayList<String> xAxisValues = new ArrayList<String>();

        for (int i = 0; i < violationAggregateItemList.size(); i++) {
            ViolationAggregateItem violationAggregateItem = violationAggregateItemList.get(i);
            BarEntry barEntryViolation = new BarEntry((float) violationAggregateItem.getViolations(), i);
            violationSet.add(barEntryViolation);
            BarEntry barEntryWin = new BarEntry((float) violationAggregateItem.getWins(), i);
            winSet.add(barEntryWin);
            xAxisValues.add(violationAggregateItem.getDate().toString());
        }

        BarDataSet violationDataSet = new BarDataSet(violationSet, "Violations");
        violationDataSet.setColor(Color.rgb(155, 0, 0));

        BarDataSet winDataSet = new BarDataSet(winSet, "Wins");
        winDataSet.setColor(Color.rgb(0, 155, 0));

        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        dataSet.add(violationDataSet);
        dataSet.add(winDataSet);

        BarData barData = new BarData(xAxisValues, dataSet);
        return barData;
    }
}
