package com.robalascott.qshake.qshake.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.robalascott.qshake.qshake.MainMenuActivity;
import com.robalascott.qshake.qshake.R;

/**
 * Created by robscott on 2017-09-21.
 */

public class MenuFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public Button btnGame, btnSetting;

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_main_menu, container, false);
        btnGame = (Button)rootView.findViewById(R.id.game_Btn);
        btnSetting = (Button)rootView.findViewById(R.id.setting_Btn);
        btnGame.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_Btn:
                ((MainMenuActivity) getActivity()).setmViewPager(1);
                break;
            case R.id.setting_Btn:
                ((MainMenuActivity) getActivity()).setmViewPager(2);
                break;
            default:
                break;
        }
    }

    private void MakeToast(String string) {
        Toast.makeText(getActivity(), "Going to Fragment " + string, Toast.LENGTH_SHORT).show();
    }

}