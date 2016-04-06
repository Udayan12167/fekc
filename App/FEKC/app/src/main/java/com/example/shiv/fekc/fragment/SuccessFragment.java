package com.example.shiv.fekc.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.fekc.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

/**
 * Created by udayantandon on 4/6/16.
 */
public class SuccessFragment extends Fragment {
    DecoView decoView;

    public static SuccessFragment newInstance() {
        SuccessFragment fragment = new SuccessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.success_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        decoView = (DecoView) view.findViewById(R.id.successArcView);
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, 50, 0)
                .build();

        int backIndex = decoView.addSeries(seriesItem);
        int series1Index = decoView.addSeries(seriesItem);

        decoView.addEvent(new DecoEvent.Builder(50)
                .setIndex(backIndex)
                .build());

        decoView.addEvent(new DecoEvent.Builder(16.3f)
                .setIndex(series1Index)
                .setDelay(5000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(30f)
                .setIndex(series1Index)
                .setDelay(10000)
                .build());

    }
}
