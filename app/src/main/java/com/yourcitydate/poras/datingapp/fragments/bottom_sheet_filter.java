package com.yourcitydate.poras.datingapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;
import com.yourcitydate.poras.datingapp.MainActivity;
import com.yourcitydate.poras.datingapp.R;


public class bottom_sheet_filter extends BottomSheetDialogFragment {
    TextView textView;
    private final String SharedPrefs = "prefs";
    private SharedPreferences sharedPreferences;
    int max,min;
    SharedPreferences.Editor editor;


    public bottom_sheet_filter() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs,getActivity().MODE_PRIVATE);
        min = sharedPreferences.getInt("minAge",16);
        max = sharedPreferences.getInt("maxAge",55);
        editor = sharedPreferences.edit();

        textView = (TextView)view.findViewById(R.id.ageSelection);
        textView.setText(min + "-" + max);

        RangeSeekBar seekBar1 = (RangeSeekBar)view.findViewById(R.id.seekBar_age);
        seekBar1.setRangeValues(16, 45);

        seekBar1.setSelectedMinValue(min);
        seekBar1.setSelectedMaxValue(max);



        seekBar1.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                //Now you have the minValue and maxValue of your RangeSeekbar
               // Toast.makeText(getActivity(), minValue + "-" + maxValue, Toast.LENGTH_LONG).show();


                textView.setText(minValue + "-" + maxValue);
                editor.putInt("maxAge",maxValue);
                editor.putInt("minAge",minValue);

                editor.commit();

            }
        });

// Get noticed while dragging
        seekBar1.setNotifyWhileDragging(true);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}