package com.robalascott.qshake.qshake;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.robalascott.qshake.qshake.Fragment.CurrentGameFragment;
import com.robalascott.qshake.qshake.Fragment.DatabaseFragment;
import com.robalascott.qshake.qshake.Fragment.MenuFragment;
import com.robalascott.qshake.qshake.Fragment.SettingFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    // A reg methods would be a better option over Hard code implementation

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MenuFragment tab1 = new MenuFragment();
                return tab1;
            case 1:
                CurrentGameFragment game = new CurrentGameFragment();
                return game;
            case 2:
                SettingFragment setting = new SettingFragment();
                return setting;
            case 3:
                DatabaseFragment database = new DatabaseFragment();
                return database;
            default:return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        //Not good methods
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Main Menu";
            case 1:
                return "Game";
            case 2:
                return "Setting";
            case 3:
                return "Database";

        }
        return null;
    }
}