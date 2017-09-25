package com.robalascott.qshake.qshake.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robalascott.qshake.qshake.R;

/**
 * Created by robscott on 2017-09-21.
 */

public class DatabaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_game_menu, container, false);
        return rootView;
    }

}
