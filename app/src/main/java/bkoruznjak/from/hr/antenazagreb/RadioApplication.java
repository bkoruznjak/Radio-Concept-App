package bkoruznjak.from.hr.antenazagreb;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;

import java.util.Locale;

import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.AntenaConstants;
import bkoruznjak.from.hr.antenazagreb.constants.PreferenceKeyConstants;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.util.FontsOverrideUtils;
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
    private int radioNotificationIcon;
    private Drawable mSocialIconFacebook;
    private Drawable mSocialIconTwitter;
    private Drawable mSocialIconInstagram;
    private Locale locale = null;

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

        FontsOverrideUtils.setDefaultFont(this, "DEFAULT", "avenir_book.ttf");
        FontsOverrideUtils.setDefaultFont(this, "MONOSPACE", "avenir_light.ttf");
        FontsOverrideUtils.setDefaultFont(this, "SERIF", "nunito_regular.ttf");
        FontsOverrideUtils.setDefaultFont(this, "SANS_SERIF", "roboto_regular.ttf");


        SharedPreferences preferences = getSharedPreferences(PreferenceKeyConstants.PREFERENCE_NAME, MODE_PRIVATE);
        if (!preferences.contains(PreferenceKeyConstants.KEY_AUTOPLAY)) {
            Log.d("BBB", "SETTING AUTOPLAY FOR FIRST TIME TO TRUE");
            preferences.edit().putBoolean(PreferenceKeyConstants.KEY_AUTOPLAY, false).commit();
        }
        if (!preferences.contains(PreferenceKeyConstants.KEY_DEFAULT_STATION)) {
            Log.d("BBB", "SETTING DEF STATIONS FOR FIRST TIME TO MAIN");
            preferences.edit().putString(PreferenceKeyConstants.KEY_DEFAULT_STATION, StreamUriConstants.ANTENA_MAIN).commit();
        }
        if (!preferences.contains(PreferenceKeyConstants.KEY_STORE_ARTICLES)) {
            Log.d("BBB", "SETTING STORE ARTICLES FOR FIRST TIME TO FALSE");
            preferences.edit().putBoolean(PreferenceKeyConstants.KEY_STORE_ARTICLES, false).commit();
        }
        if (!preferences.contains(PreferenceKeyConstants.KEY_VOLUME)) {
            Log.d("BBB", "SETTING VOLUME FOR FIRST TIME TO 5");
            preferences.edit().putInt(PreferenceKeyConstants.KEY_VOLUME, 5).commit();
        }

        String defaultLocaleLanguage = AntenaConstants.DEFAULT_LOCALE;
        if (!preferences.contains(PreferenceKeyConstants.KEY_LANGUAGE)) {
            Log.d("BBB", "SETTING LANGUAGE FOR THE FIRST TIME TO HR");
            preferences.edit().putString(PreferenceKeyConstants.KEY_LANGUAGE, defaultLocaleLanguage).commit();
        } else {
            defaultLocaleLanguage = preferences.getString(PreferenceKeyConstants.KEY_LANGUAGE, AntenaConstants.DEFAULT_LOCALE);
        }
        Locale defaultLocale = new Locale(defaultLocaleLanguage);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = defaultLocale;
        res.updateConfiguration(conf, dm);

        radioVolume = preferences.getInt(PreferenceKeyConstants.KEY_VOLUME, 5);

        boolean useVector = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        radioNotificationIcon = useVector ? R.drawable.antena_icon_vector : R.drawable.antena_icon;
        mSocialIconFacebook = getResources().getDrawable(useVector ? R.drawable.icon_logo_facebook : R.drawable.icon_png_facebook);
        mSocialIconTwitter = getResources().getDrawable(useVector ? R.drawable.icon_logo_twitter : R.drawable.icon_png_twitter);
        mSocialIconInstagram = getResources().getDrawable(useVector ? R.drawable.icon_logo_instagram : R.drawable.icon_png_instagram);
    }

    public RadioBus getBus() {
        if (myBus == null) {
            myBus = new RadioBus();
        }
        return myBus;
    }

    public RadioStateModel getRadioStateModel() {
        if (myStateModel == null) {
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

    public int getRadioNotificationIcon() {
        return this.radioNotificationIcon;
    }

    public Drawable getSocialIconFacebook() {
        return this.mSocialIconFacebook;
    }

    public Drawable getSocialIconTwitter() {
        return this.mSocialIconTwitter;
    }

    public Drawable getSocialIconInstagram() {
        return this.mSocialIconInstagram;
    }
}
