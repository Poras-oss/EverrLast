package com.yourcitydate.poras.datingapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yourcitydate.poras.datingapp.Chat.globe_chat;
import com.yourcitydate.poras.datingapp.Chat.matches_chat;

public class viewPagerAdapter extends FragmentPagerAdapter {


    public viewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new matches_chat();
        }
        else if (position == 1)
        {
            fragment =  new globe_chat();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Matches";
        }
        else if (position == 1)
        {
            title = "Globe";
        }
        return title;
    }

}
