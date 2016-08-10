package bkoruznjak.from.hr.antenazagreb.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.squareup.picasso.Picasso;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;

/**
 * Created by bkoruznjak on 08/08/16.
 */
public class AntenaMenuFragment extends MenuFragment {


    private RadioBus myBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
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
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getGroupId() == R.id.stream_menu_group) {
                    //handle checking logic
                    if (!item.isChecked()) {
                        switch (item.getItemId()) {
                            case R.id.menu_main_stream:
                                Log.d("BBB", "MAIN STREAM PRESSED!");
                                myBus.post(StreamUriConstants.ANTENA_MAIN);
                                break;
                            case R.id.menu_hit_stream:
                                Log.d("BBB", "HIT STREAM PRESSED!");
                                myBus.post(StreamUriConstants.ANTENA_HIT);
                                break;
                            case R.id.menu_rock_stream:
                                Log.d("BBB", "ROCK STREAM PRESSED!");
                                myBus.post(StreamUriConstants.ANTENA_ROCK);
                                break;
                            case R.id.menu_2000_stream:
                                Log.d("BBB", "2000 STREAM PRESSED!");
                                myBus.post(StreamUriConstants.ANTENA_2000);
                                break;
                            case R.id.menu_90s_stream:
                                Log.d("BBB", "90S STREAM PRESSED!");
                                myBus.post(StreamUriConstants.ANTENA_90);
                                break;
                            case R.id.menu_80s_stream:
                                Log.d("BBB", "80S STREAM PRESSED!");
                                myBus.post(StreamUriConstants.ANTENA_80);
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
        ImageView drawerHeaderImage = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);
        setupHeader(drawerHeaderImage);
        return setupReveal(view);
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