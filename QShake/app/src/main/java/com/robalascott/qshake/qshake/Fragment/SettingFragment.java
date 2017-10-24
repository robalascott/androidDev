package com.robalascott.qshake.qshake.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.robalascott.qshake.qshake.R;

import java.util.ArrayList;

/**
 * Created by robscott on 2017-09-21.
 */

public class SettingFragment extends Fragment{
    private Spinner spinnerTime;
    private CheckBox checkBoxType;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_setting_menu, container, false);
        spinnerTime = (Spinner)rootView.findViewById(R.id.spinner_time);
        Integer values[] = {15,30,40,60};
        ArrayAdapter<Integer> adapter = new  ArrayAdapter<Integer>(this.getActivity(),android.R.layout.simple_spinner_item,values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerTime.setAdapter(adapter);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                /*Plugin for select*/

            }
        });
        checkBoxType= (CheckBox)rootView.findViewById(R.id.checkBoxType);
        linearLayout = (LinearLayout)rootView.findViewById(R.id.linearl_layout);
        return rootView;
    }



}
