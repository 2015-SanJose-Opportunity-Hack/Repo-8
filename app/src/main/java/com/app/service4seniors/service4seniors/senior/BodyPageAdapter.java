package com.app.service4seniors.service4seniors.senior;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ymoswal on 10/4/2015.
 */
public class BodyPageAdapter extends FragmentPagerAdapter {
    public BodyPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new FrontBodyFragment();
            case 1:
                // Games fragment activity
                return new BackBodyFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
