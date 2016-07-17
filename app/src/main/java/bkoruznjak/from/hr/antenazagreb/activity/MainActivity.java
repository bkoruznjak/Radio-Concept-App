package bkoruznjak.from.hr.antenazagreb.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.adapters.AntenaPagerAdapter;
import bkoruznjak.from.hr.antenazagreb.views.AntenaTabFactory;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static int counter = 1;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.antenaTabLayout)
    TabLayout antenaTabLayout;
    @BindView(R.id.antenaToolbar)
    Toolbar antenaToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.antena_menu, menu);
        return true;
    }

    private void init() {
        ButterKnife.bind(this);
        setupActionBar();
        setupTabBar();

    }

    private void setupActionBar() {
        setSupportActionBar(antenaToolbar);
    }

    private void setupTabBar() {

        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        AntenaTabFactory tabFactory = new AntenaTabFactory(this);
        antenaTabLayout.getTabAt(0).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.radio_tab), getResources().getDrawable(R.drawable.ic_radio_white_24dp)));
        antenaTabLayout.getTabAt(1).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.news_tab), getResources().getDrawable(R.drawable.ic_news_white_24dp)));
        antenaTabLayout.getTabAt(2).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.podcast_tab), getResources().getDrawable(R.drawable.ic_mic_white_24dp)));
        antenaTabLayout.getTabAt(3).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.promo_tab), getResources().getDrawable(R.drawable.ic_promo_border_white_24dp)));
        antenaTabLayout.getTabAt(4).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.social_tab), getResources().getDrawable(R.drawable.ic_social_white_24dp)));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.antenaViewPager);
        final AntenaPagerAdapter adapter = new AntenaPagerAdapter
                (getSupportFragmentManager(), antenaTabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(antenaTabLayout));
        antenaTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //set the initial load on the radio screen
        viewPager.setCurrentItem(0);
    }


}
