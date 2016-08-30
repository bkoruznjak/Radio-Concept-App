package bkoruznjak.from.hr.antenazagreb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import bkoruznjak.from.hr.antenazagreb.fragments.NewsFragment;
import bkoruznjak.from.hr.antenazagreb.fragments.PodcastFragment;
import bkoruznjak.from.hr.antenazagreb.fragments.PromoFragment;
import bkoruznjak.from.hr.antenazagreb.fragments.RadioFragment;
import bkoruznjak.from.hr.antenazagreb.fragments.SocialFragment;

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
                PromoFragment tab1 = new PromoFragment();
                return tab1;
            case 1:
                SocialFragment tab2 = new SocialFragment();
                return tab2;
            case 2:
                RadioFragment tab3 = new RadioFragment();
                return tab3;
            case 3:
                PodcastFragment tab4 = new PodcastFragment();
                return tab4;
            case 4:
                NewsFragment tab5 = new NewsFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}