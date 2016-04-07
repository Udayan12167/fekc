package com.example.shiv.fekc.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.ViolationAggregateItem;
import com.example.shiv.fekc.item.ViolationItem;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by udayantandon on 4/6/16.
 */
public class FailureFragment extends Fragment {
    private DecoView decoView;
    private DBAdapter dbAdapter;
    private ArrayList<ViolationAggregateItem> violationAggregateItemList;


    public static FailureFragment newInstance() {
        FailureFragment fragment = new FailureFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        dbAdapter = new DBAdapter();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.failure_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<ViolationItem> violationItemList =
                dbAdapter.getAllViolations();

        createViolationAggregateItemList();
        getAggregatedList(violationItemList);
        int wins=0;
        int violations=0;
        for (ViolationAggregateItem violationAggregateItem : violationAggregateItemList){
            wins += violationAggregateItem.getWins();
            violations += violationAggregateItem.getViolations();
        }
        super.onViewCreated(view, savedInstanceState);
        decoView = (DecoView) view.findViewById(R.id.failureArcView);
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, wins+violations, 0)
                .build();

        int backIndex = decoView.addSeries(seriesItem);

        final SeriesItem series1Item = new SeriesItem.Builder(Color.parseColor("#c71215"))
                .setRange(0, wins+violations, 0)
                .build();

        int series1Index = decoView.addSeries(series1Item);

        final TextView failureStats = (TextView) view.findViewById(R.id.arc_view_failure_stats);
        failureStats.setText("You had "+violations+" violations\n in the past week.");

        final TextView textPercentage = (TextView) view.findViewById(R.id.textPercentage);
        series1Item.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - series1Item.getMinValue()) / (series1Item.getMaxValue() - series1Item.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        decoView.addEvent(new DecoEvent.Builder(wins+violations)
                .setIndex(backIndex)
                .build());

        decoView.addEvent(new DecoEvent.Builder(violations)
                .setIndex(series1Index)
                .setDelay(1000)
                .build());

    }


    private void createViolationAggregateItemList(){
        violationAggregateItemList = new ArrayList<>();
        for(int i = 6 ; i >= 0 ; i--){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY , 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.add(Calendar.HOUR_OF_DAY, -24 * i);
            Date date = new Date(c.getTimeInMillis());
            ViolationAggregateItem violationAggregateItem = new ViolationAggregateItem();
            violationAggregateItem.setDate(date);
            violationAggregateItemList.add(violationAggregateItem);
        }

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
    }

}
