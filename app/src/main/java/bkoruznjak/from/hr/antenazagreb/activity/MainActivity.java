package bkoruznjak.from.hr.antenazagreb.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.mxn.soul.flowingdrawer_core.FlowingView;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Locale;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.adapters.AntenaPagerAdapter;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.PreferenceKeyConstants;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.constants.UtilConstants;
import bkoruznjak.from.hr.antenazagreb.enums.LanguagesEnum;
import bkoruznjak.from.hr.antenazagreb.enums.RadioCommandEnum;
import bkoruznjak.from.hr.antenazagreb.enums.RadioStateEnum;
import bkoruznjak.from.hr.antenazagreb.fragments.AntenaMenuFragment;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioVolumeModel;
import bkoruznjak.from.hr.antenazagreb.model.db.SongModel;
import bkoruznjak.from.hr.antenazagreb.model.network.ArticleModel;
import bkoruznjak.from.hr.antenazagreb.model.network.PodcastModel;
import bkoruznjak.from.hr.antenazagreb.model.network.SocialModel;
import bkoruznjak.from.hr.antenazagreb.model.network.StreamModel;
import bkoruznjak.from.hr.antenazagreb.service.RadioService;
import bkoruznjak.from.hr.antenazagreb.util.ResourceUtils;
import bkoruznjak.from.hr.antenazagreb.views.AntenaTabFactory;
import bkoruznjak.from.hr.antenazagreb.views.fab.CustomFloatingActionButton;
import bkoruznjak.from.hr.antenazagreb.views.fab.FloatingActionMenu;
import bkoruznjak.from.hr.antenazagreb.views.fab.SubActionButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.antenaTabLayout)
    TabLayout antenaTabLayout;
    @BindView(R.id.antenaToolbar)
    Toolbar antenaToolbar;
    @BindView(R.id.drawer_layout)
    LeftDrawerLayout drawerLayout;
    @BindView(R.id.floatingDrawer)
    FlowingView mFlowingView;
    Animation infiniteRotateAnim;
    Animation realInfiniteRotateAnim;
    RadioStateModel mRadioStateModel;
    float densityPixelCoef;
    private RadioBus myBus;
    private boolean isRadioStationPickerShown = false;
    private SharedPreferences mPreferences;
    private AudioManager mAudioManager;
    private RadioVolumeModel mRadioVolume;
    private BitmapDrawable mBackgroundBitmap;
    private BitmapDrawable mAntenaLogoCornerBitmap;
    private ArrayList<SocialModel> socialData;
    private ArrayList<ArticleModel> articleData;
    private ArrayList<PodcastModel> podcastData;
    private FloatingActionMenu rightLowerMenu;
    private ImageView mRadioMainControlImage;
    private ArrayList<StreamModel> mStreamList;
    private ArrayList<SubActionButton> mStreamButtonsList;
    private CustomFloatingActionButton rightLowerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences(PreferenceKeyConstants.PREFERENCE_NAME, MODE_PRIVATE);
        handleLocale();
        setContentView(R.layout.activity_main);
        init();
    }


    private void handleAutoPlay(boolean isAutoplayOn) {
        if (isAutoplayOn && !mRadioStateModel.isServiceUp()) {
            Intent startRadioServiceIntent = new Intent(getApplicationContext(), RadioService.class);
            startService(startRadioServiceIntent);
        }
    }

    private void handleLocale() {
        Locale locale = new Locale(mPreferences.getString(PreferenceKeyConstants.KEY_LANGUAGE, "hr"));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onResume() {
        super.onResume();
        myBus.register(this);
        handleAutoPlay(mPreferences.getBoolean(PreferenceKeyConstants.KEY_AUTOPLAY, true));

    }

    @Override
    public void onPause() {
        super.onPause();
        myBus.unregister(this);
        mPreferences.getString(PreferenceKeyConstants.KEY_DEFAULT_STATION, StreamUriConstants.NAME_ANTENA_MAIN);
        mPreferences.edit().putString(PreferenceKeyConstants.KEY_DEFAULT_STATION, mRadioStateModel.getRadioStationName()).commit();
        mRadioStateModel.setDefaultStream(mRadioStateModel.getRadioStationName());
        Log.d("bbb", "mijenjam default radio station na:" + mRadioStateModel.getRadioStationName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mRadioStateModel.isMusicPlaying() && mRadioStateModel.isServiceUp()) {
            myBus.post(RadioCommandEnum.STOP);
            Intent stopRadioServiceIntent = new Intent(getApplicationContext(), RadioService.class);
            stopService(stopRadioServiceIntent);
        }
        this.finishAffinity();
    }

    private void init() {
        ButterKnife.bind(this);
        mRadioMainControlImage = new ImageView(this);
        mBackgroundBitmap = new BitmapDrawable(ResourceUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.antena_bg, UtilConstants.BACKGROUND_BITMAP_WIDTH, UtilConstants.BACKGROUND_BITMAP_HEIGHT));
        mAntenaLogoCornerBitmap = new BitmapDrawable(ResourceUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.img_antena_logo_corner, UtilConstants.ARTICLE_IMAGE_WIDTH, UtilConstants.ARTICLE_IMAGE_HEIGHT));
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        myBus = ((RadioApplication) getApplication()).getBus();
        mRadioStateModel = ((RadioApplication) getApplication()).getRadioStateModel();
        handleStreams();
        setupAnimations();
        setupActionBar();
        setupTabBar();
        initFabButtons();
        setupDrawer();
        updateViewsByRadioState(mRadioStateModel);
    }

    /**
     * Method handles fetching streams from the web service and returns them to the device
     * in case no internet or error, local constants will be used
     * <p>
     * THIS IS A MOCK METHOD FOR NOW
     */
    private void handleStreams() {
        mStreamList = RadioApplication.getInstance().getStreamList();
        //setDefaultStream
        for (StreamModel stream : mStreamList) {
            if (mRadioStateModel.getDefaultStream().equals(stream.name)) {
                Log.d("bbb", "setting default stream:" + stream.name);
                mRadioStateModel.setStreamModel(stream);
            }
        }
    }

    /**
     * Overridden method to catch touch events outside the open floating drawer.
     * Touching the screen outside the drawer will close the same.
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            if (drawerLayout.isShownMenu()) {
                float floatingDrawerWidth = mFlowingView.getWidth();
                if (touchX > floatingDrawerWidth) {
                    drawerLayout.closeDrawer();
                    return false;
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            drawerLayout.toggle();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Overridden method to catch volume key input and notify all views to adjust acordingly
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mRadioVolume == null) {
                mRadioVolume = new RadioVolumeModel(volume);
            } else {
                mRadioVolume.setVolume(volume);
            }
            //Log.d("bbb","volume:" + volume);
            myBus.post(mRadioVolume);
            return super.dispatchKeyEvent(event);

        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mRadioVolume == null) {
                mRadioVolume = new RadioVolumeModel(volume);
            } else {
                mRadioVolume.setVolume(volume);
            }
            //Log.d("bbb","volume:" + volume);
            myBus.post(mRadioVolume);
            return super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }


    private void setupAnimations() {
        densityPixelCoef = getResources().getDisplayMetrics().widthPixels / 100;
        //animation for main control fab
        infiniteRotateAnim = AnimationUtils.loadAnimation(this, R.anim.inf_rotate);
        realInfiniteRotateAnim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //Setup anim with desired properties
        realInfiniteRotateAnim.setInterpolator(new LinearInterpolator());
        realInfiniteRotateAnim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        realInfiniteRotateAnim.setDuration(700); //Put desired duration per anim cycle here, in milliseconds
    }

    private void setupDrawer() {
        FragmentManager fm = getSupportFragmentManager();
        AntenaMenuFragment mMenuFragment = (AntenaMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new AntenaMenuFragment()).commit();
        }
        drawerLayout.setFluidView(mFlowingView);
        drawerLayout.setMenuFragment(mMenuFragment);
        //todo UNTIL YOU FIGURE OUT HOW TO SET VIEW HIERARCHY OF THE rightLowerMenu SO IT IS BEHING THE DRAWER THIS IS HOW WE WILL HIDE IT
        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (rightLowerMenu != null && event.getAction() == 2 && rightLowerMenu.isOpen()) {
                    rightLowerMenu.close(true);
                }
                return false;
            }
        });
    }

    private void setupActionBar() {
        antenaToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));
        antenaToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        setSupportActionBar(antenaToolbar);

        antenaToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.toggle();
            }
        });
    }

    private void setupTabBar() {

        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.addTab(antenaTabLayout.newTab());
        antenaTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        AntenaTabFactory tabFactory = new AntenaTabFactory(this);
        antenaTabLayout.getTabAt(0).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.promo_tab), getResources().getDrawable(R.drawable.ic_promo_border_white_24dp)));
        antenaTabLayout.getTabAt(1).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.social_tab), getResources().getDrawable(R.drawable.ic_social_white_24dp)));
        antenaTabLayout.getTabAt(2).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.radio_tab), getResources().getDrawable(R.drawable.ic_radio_white_24dp)));
        antenaTabLayout.getTabAt(3).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.podcast_tab), getResources().getDrawable(R.drawable.ic_mic_white_24dp)));
        antenaTabLayout.getTabAt(4).setCustomView(tabFactory.generateCustomTab(getResources().getString(R.string.news_tab), getResources().getDrawable(R.drawable.ic_news_white_24dp)));

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
        viewPager.setCurrentItem(2);
    }

    @Subscribe
    public void handleStreamStateChange(RadioStateEnum streamState) {
        switch (streamState) {
            case BUFFERING:
                mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_white_24dp));
                mRadioMainControlImage.clearAnimation();
                mRadioMainControlImage.startAnimation(realInfiniteRotateAnim);
                break;
            case ENDED:
                mRadioMainControlImage.clearAnimation();
                mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                break;
            case IDLE:
                mRadioMainControlImage.clearAnimation();
                mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                break;
            case PREPARING:
                mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_white_24dp));
                mRadioMainControlImage.clearAnimation();
                mRadioMainControlImage.startAnimation(realInfiniteRotateAnim);
                break;
            case READY:
                //stop buffering animation if it exists
                mRadioMainControlImage.clearAnimation();
                if (mRadioStateModel.isMusicPlaying() && !mRadioStateModel.isStreamInterrupted()) {
                    mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                } else {
                    mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                }
                break;
            case UNKNOWN:
                break;
        }
    }

    @Subscribe
    public void handleLanguageChange(LanguagesEnum languageEvent) {
        Locale myLocale = new Locale(languageEvent.toString());
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
        Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(refresh);
        finish();
    }

    @Subscribe
    public void handleData(final ArrayList data) {
        if (data.get(0) instanceof ArticleModel) {
            setArticleData(data);
        } else if (data.get(0) instanceof PodcastModel) {
            setPodcastData(data);
        } else if (data.get(0) instanceof SocialModel) {
            setSocialData(data);
        }
    }

    private void refreshControlButtonDrawable(RadioStateModel stateModel, Animation animation) {
        //ovo ojačaj kod jer treba maknut rucno dodavanje na gumb animacije i sranja.
        if (stateModel.getStateEnum() == RadioStateEnum.BUFFERING || stateModel.getStateEnum() == RadioStateEnum.PREPARING) {
            mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_white_24dp));
            mRadioMainControlImage.startAnimation(realInfiniteRotateAnim);
        } else if (stateModel.isMusicPlaying() && !stateModel.isStreamInterrupted()) {
            mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
        } else {
            mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
        }
    }

    private void refreshStreamButtons(StreamModel streamModel) {
        if (mStreamButtonsList != null) {
            for (SubActionButton button : mStreamButtonsList) {
                if (button.getStreamName().equals(streamModel.name)) {
                    Log.d("bbb", "marking stream:" + streamModel.name + " as current stream");
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_dark_orange_selector));
                } else {
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_orange_selector));
                }
            }
        }
    }

    private void updateViewsByRadioState(RadioStateModel stateModel) {
        refreshControlButtonDrawable(stateModel, infiniteRotateAnim);
        refreshStreamButtons(stateModel.getStreamModel());
    }

    /**
     * Insurance method in case the service is down, user is still able to change streams
     *
     * @param streamModel
     */
    private void handleStreamURI(StreamModel streamModel) {
        Log.d("BBB", "StreamURI:" + streamModel.url);
        if (mRadioStateModel.isServiceUp()) {
            myBus.post(streamModel.url);
        } else {
            mRadioStateModel.setStreamUri(streamModel.url);
        }
    }


    public BitmapDrawable getBackgroundBitmap() {
        if (mBackgroundBitmap == null) {
            mBackgroundBitmap = new BitmapDrawable(ResourceUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.antena_bg, UtilConstants.BACKGROUND_BITMAP_WIDTH, UtilConstants.BACKGROUND_BITMAP_HEIGHT));
        }
        return mBackgroundBitmap;
    }

    public BitmapDrawable getAntenaLogoCornerBitmap() {
        if (mAntenaLogoCornerBitmap == null) {
            mAntenaLogoCornerBitmap = new BitmapDrawable(ResourceUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.img_antena_logo_corner, UtilConstants.ARTICLE_IMAGE_WIDTH, UtilConstants.ARTICLE_IMAGE_HEIGHT));
        }
        return mAntenaLogoCornerBitmap;
    }

    public ArrayList<ArticleModel> getArticleData() {
        return this.articleData;
    }

    public void setArticleData(ArrayList<ArticleModel> articleData) {
        this.articleData = articleData;
    }

    public ArrayList<PodcastModel> getPodcastData() {
        return this.podcastData;
    }

    public void setPodcastData(ArrayList<PodcastModel> podcastData) {
        this.podcastData = podcastData;
    }

    public ArrayList<SocialModel> getSocialData() {
        return this.socialData;
    }

    public void setSocialData(ArrayList<SocialModel> socialData) {
        this.socialData = socialData;
    }


    private void initFabButtons() {
        mStreamButtonsList = new ArrayList<>();
        // Set up the orange button on the lower right corner
        // more or less with default parameter
        mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
        rightLowerButton = new CustomFloatingActionButton.Builder(this)
                .setContentView(mRadioMainControlImage)
                .build();

        FloatingActionMenu.Builder streamButtonBuilder = new FloatingActionMenu.Builder(this);
        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        if (mStreamList != null) {
            for (final StreamModel stream : mStreamList) {
                if (stream.isActive == 0) {
                    continue;
                }
                ImageView streamIcon = new ImageView(this);
                int iconId = ResourceUtils.imgResIdFromName(getApplicationContext(), stream.iconId);
                streamIcon.setImageDrawable(getResources().getDrawable(iconId));
                final SubActionButton streamButton = rLSubBuilder.setContentView(streamIcon).build();
                streamButton.setStreamName(stream.name);
                streamButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (streamButton.getStreamName().equals(mRadioStateModel.getRadioStationName())) {
                        } else {
                            mRadioStateModel.setStreamModel(stream);
                            handleStreamURI(stream);
                            refreshStreamButtons(stream);
                            myBus.post(new SongModel(mRadioStateModel.getSongTitle(), mRadioStateModel.getSongAuthor()));
                            myBus.post(stream);
                        }
                    }
                });
                mStreamButtonsList.add(streamButton);
                streamButtonBuilder.addSubActionView(streamButton);
            }
        }

        rightLowerMenu = streamButtonBuilder.attachTo(rightLowerButton).build();
        rightLowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioStateModel.isServiceUp() && mRadioStateModel.isMusicPlaying() && !mRadioStateModel.isStreamInterrupted()) {
                    myBus.post(RadioCommandEnum.PAUSE);
                    mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                    mRadioMainControlImage.clearAnimation();
                } else if (mRadioStateModel.getStateEnum() == RadioStateEnum.BUFFERING) {
                    //todo ovo treba malo doradit, stavio sam tu samo da mozes prekinut buffeering na naglo
                    myBus.post(RadioCommandEnum.PAUSE);
                    mRadioMainControlImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                    mRadioMainControlImage.clearAnimation();
                } else if (mRadioStateModel.isServiceUp()) {
                    myBus.post(RadioCommandEnum.PLAY);
                } else {
                    Intent startRadioServiceIntent = new Intent(getApplicationContext(), RadioService.class);
                    startService(startRadioServiceIntent);
                }
            }
        });
    }

    @Subscribe
    public void handlePodcastStart(RadioCommandEnum radioCommandEnum) {
        if (radioCommandEnum == RadioCommandEnum.PODCAST && rightLowerButton != null) {
            if (mRadioStateModel.isServiceUp() && mRadioStateModel.isMusicPlaying() && !mRadioStateModel.isStreamInterrupted()) {
                rightLowerButton.callOnClick();
            } else if (mRadioStateModel.getStateEnum() == RadioStateEnum.BUFFERING) {
                rightLowerButton.callOnClick();
            }
        }
    }
}
