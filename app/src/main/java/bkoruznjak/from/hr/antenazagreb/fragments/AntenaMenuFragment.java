package bkoruznjak.from.hr.antenazagreb.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.squareup.picasso.Picasso;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.PreferenceKeyConstants;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.views.CircleTransformation;

/**
 * Created by bkoruznjak on 08/08/16.
 */
public class AntenaMenuFragment extends MenuFragment {


    private RadioBus myBus;
    private RadioStateModel mRadioStateModel;
    private SharedPreferences mPreferences;

    private boolean isAutoplayOn;
    private boolean isStoringArticleData;
    private String defaultStation;
    private int volume;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
        mRadioStateModel = ((RadioApplication) getActivity().getApplication()).getRadioStateModel();
        mPreferences = getActivity().getSharedPreferences(PreferenceKeyConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);

        isAutoplayOn = mPreferences.getBoolean(PreferenceKeyConstants.KEY_AUTOPLAY, true);
        isStoringArticleData = mPreferences.getBoolean(PreferenceKeyConstants.KEY_STORE_ARTICLES, false);
        defaultStation = mPreferences.getString(PreferenceKeyConstants.KEY_DEFAULT_STATION, StreamUriConstants.ANTENA_MAIN);
        volume = mPreferences.getInt(PreferenceKeyConstants.KEY_VOLUME, 5);
    }

    @Override
    public void onResume() {
        super.onResume();
        myBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        myBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        final NavigationView naviView = (NavigationView) view.findViewById(R.id.vNavigation);
        View headerView = naviView.getHeaderView(0);
        final Menu drawerMenu = naviView.getMenu();
        final int menuSize = drawerMenu.size();
        //initial checkup to set active stream
        handleStreamMenuList(mRadioStateModel.getStreamUri(), drawerMenu);

        /** OLD NAVIGATION - LEAVING IT COMENTED CAUSE YOU NEVER KNOW

         naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.stream_menu_group) {
        //handle checking logic
        if (!item.isChecked()) {
        switch (item.getItemId()) {
        case R.id.menu_main_stream:
        handleStreamURI(StreamUriConstants.ANTENA_MAIN);
        break;
        case R.id.menu_hit_stream:
        handleStreamURI(StreamUriConstants.ANTENA_HIT);
        break;
        case R.id.menu_rock_stream:
        handleStreamURI(StreamUriConstants.ANTENA_ROCK);
        break;
        case R.id.menu_2000_stream:
        handleStreamURI(StreamUriConstants.ANTENA_2000);
        break;
        case R.id.menu_90s_stream:
        handleStreamURI(StreamUriConstants.ANTENA_90);
        break;
        case R.id.menu_80s_stream:
        handleStreamURI(StreamUriConstants.ANTENA_80);
        break;
        }

        for (int menuItemIndex = 0; menuItemIndex < menuSize; menuItemIndex++) {
        MenuItem tempMenuItem = drawerMenu.getItem(menuItemIndex);
        if (item.getItemId() == tempMenuItem.getItemId() && tempMenuItem.getGroupId() == R.id.stream_menu_group) {
        tempMenuItem.setChecked(true);
        tempMenuItem.setIcon(R.drawable.ic_pause_black_24dp);
        } else if (tempMenuItem.getGroupId() == R.id.stream_menu_group) {
        tempMenuItem.setChecked(false);
        tempMenuItem.setIcon(R.drawable.ic_play_arrow_black_24dp);
        }
        }
        }
        }

        return false;
        }
        });

         **/
        ImageView drawerHeaderImage = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);
        setupHeader(drawerHeaderImage);

        SwitchCompat autoplaySetting = (SwitchCompat) naviView.getMenu().getItem(2).getActionView();
        if (isAutoplayOn) {
            autoplaySetting.setChecked(true);
        } else {
            autoplaySetting.setChecked(false);
        }
        autoplaySetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("bbb", "stavljam stream autoplay na" + isChecked);
                mPreferences.edit().putBoolean(PreferenceKeyConstants.KEY_AUTOPLAY, isChecked).commit();
            }
        });

        SwitchCompat keepArticlesSetting = (SwitchCompat) naviView.getMenu().getItem(3).getActionView();
        if (isStoringArticleData) {
            keepArticlesSetting.setChecked(true);
        } else {
            keepArticlesSetting.setChecked(false);
        }
        keepArticlesSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("bbb", "stavljam keep articles na" + isChecked);
                mPreferences.edit().putBoolean(PreferenceKeyConstants.KEY_STORE_ARTICLES, isChecked).commit();
            }
        });

        return setupReveal(view);
    }

    /**
     * Insurance method in case the service is down, user is still able to change streams
     *
     * @param streamURI
     */
    private void handleStreamURI(String streamURI) {
        Log.d("BBB", "StreamURI:" + streamURI);
        if (mRadioStateModel.isServiceUp()) {
            myBus.post(streamURI);
        } else {
            mRadioStateModel.setStreamUri(streamURI);
        }
    }

    /**
     * Method handles the current selected stream and then goes through all menu icons and
     * updates the one stream that is currently selected / playing
     *
     * @param streamURI
     * @param streamMenu
     */
    private void handleStreamMenuList(String streamURI, Menu streamMenu) {
        for (int menuItemIndex = 0; menuItemIndex < streamMenu.size(); menuItemIndex++) {
            switch (streamMenu.getItem(menuItemIndex).getItemId()) {
                case R.id.menu_main_stream:
                    if (StreamUriConstants.ANTENA_MAIN.equals(streamURI)) {
                        streamMenu.getItem(menuItemIndex).setChecked(true);
                        streamMenu.getItem(menuItemIndex).setIcon(R.drawable.ic_pause_black_24dp);
                    }
                    break;
                case R.id.menu_hit_stream:
                    if (StreamUriConstants.ANTENA_HIT.equals(streamURI)) {
                        streamMenu.getItem(menuItemIndex).setChecked(true);
                        streamMenu.getItem(menuItemIndex).setIcon(R.drawable.ic_pause_black_24dp);
                    }
                    break;
                case R.id.menu_rock_stream:
                    if (StreamUriConstants.ANTENA_ROCK.equals(streamURI)) {
                        streamMenu.getItem(menuItemIndex).setChecked(true);
                        streamMenu.getItem(menuItemIndex).setIcon(R.drawable.ic_pause_black_24dp);
                    }
                    break;
                case R.id.menu_2000_stream:
                    if (StreamUriConstants.ANTENA_2000.equals(streamURI)) {
                        streamMenu.getItem(menuItemIndex).setChecked(true);
                        streamMenu.getItem(menuItemIndex).setIcon(R.drawable.ic_pause_black_24dp);
                    }
                    break;
                case R.id.menu_90s_stream:
                    if (StreamUriConstants.ANTENA_90.equals(streamURI)) {
                        streamMenu.getItem(menuItemIndex).setChecked(true);
                        streamMenu.getItem(menuItemIndex).setIcon(R.drawable.ic_pause_black_24dp);
                    }
                    break;
                case R.id.menu_80s_stream:
                    if (StreamUriConstants.ANTENA_80.equals(streamURI)) {
                        streamMenu.getItem(menuItemIndex).setChecked(true);
                        streamMenu.getItem(menuItemIndex).setIcon(R.drawable.ic_pause_black_24dp);
                    }
                    break;
            }
        }
    }


    private void setupHeader(ImageView ivMenuUserProfilePhoto) {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.item_height);
        Picasso.with(getActivity())
                .load(R.drawable.random_background_image)
                .placeholder(R.drawable.img_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);
    }

    public void onOpenMenu() {
        //Toast.makeText(getActivity(),"onOpenMenu",Toast.LENGTH_SHORT).show();
    }

    public void onCloseMenu() {
        //Toast.makeText(getActivity(),"onCloseMenu",Toast.LENGTH_SHORT).show();
    }
}