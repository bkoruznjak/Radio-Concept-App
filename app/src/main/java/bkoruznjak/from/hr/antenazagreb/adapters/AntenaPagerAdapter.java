package bkoruznjak.from.hr.antenazagreb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import bkoruznjak.from.hr.antenazagreb.fragments.NewsFragment;
import bkoruznjak.from.hr.antenazagreb.fragments.PodcastFragment;
import bkoruznjak.from.hr.antenazagreb.fragments.RadioFragment;

/**
 * Created by bkoruznjak on 07/07/16.
 */
public class AntenaPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public AntenaPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewsFragment tab1 = new NewsFragment();
                return tab1;
            case 1:
                RadioFragment tab2 = new RadioFragment();
                return tab2;
            case 2:
                PodcastFragment tab3 = new PodcastFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}