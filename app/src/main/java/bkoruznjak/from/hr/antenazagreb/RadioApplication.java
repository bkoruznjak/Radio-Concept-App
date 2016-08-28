package bkoruznjak.from.hr.antenazagreb;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;

import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.PreferenceKeyConstants;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.util.NetworkUtils;
import io.fabric.sdk.android.Fabric;

/**
 * Created by bkoruznjak on 29/06/16.
 */
public class RadioApplication extends Application {
    private static RadioApplication instance;
    private static RadioBus myBus;
    private static RadioStateModel myStateModel;
    private int radioVolume;

    public static RadioApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        LeakCanary.install(this);
        NetworkUtils.registerLoganSquareTypeConverters();
        Fabric.with(this, new Crashlytics());


        SharedPreferences preferences = getSharedPreferences(PreferenceKeyConstants.PREFERENCE_NAME, MODE_PRIVATE);
        if (!preferences.contains(PreferenceKeyConstants.KEY_AUTOPLAY)) {
            Log.d("BBB", "SETTING AUTOPLAY FOR FIRST TIME TO TRUE");
            preferences.edit().putBoolean(PreferenceKeyConstants.KEY_AUTOPLAY, true).commit();
        }
        if (!preferences.contains(PreferenceKeyConstants.KEY_DEFAULT_STATION)) {
            Log.d("BBB", "SETTING DEF STATIONS FOR FIRST TIME TO MAIN");
            preferences.edit().putString(PreferenceKeyConstants.KEY_DEFAULT_STATION, StreamUriConstants.ANTENA_MAIN).commit();
        }
        if (!preferences.contains(PreferenceKeyConstants.KEY_STORE_ARTICLES)) {
            Log.d("BBB", "SETTING STRE ARTICLES FOR FIRST TIME TO FALSE");
            preferences.edit().putBoolean(PreferenceKeyConstants.KEY_STORE_ARTICLES, false).commit();
        }
        if (!preferences.contains(PreferenceKeyConstants.KEY_VOLUME)) {
            Log.d("BBB", "SETTING VOLUME FOR FIRST TIME TO 5");
            preferences.edit().putInt(PreferenceKeyConstants.KEY_VOLUME, 5).commit();
        }
        radioVolume = preferences.getInt(PreferenceKeyConstants.KEY_VOLUME, 5);
    }

    public RadioBus getBus() {
        if (myBus == null) {
            myBus = new RadioBus();
        }
        return myBus;
    }

    public RadioStateModel getRadioStateModel(){
        if(myStateModel == null){
            myStateModel = new RadioStateModel();
            myStateModel.setStreamUri(StreamUriConstants.ANTENA_MAIN);
        }
        return myStateModel;
    }

    public int getRadioVolume() {
        return this.radioVolume;
    }

    public void setRadioVolume(int radioVolume) {
        this.radioVolume = radioVolume;
    }
}
