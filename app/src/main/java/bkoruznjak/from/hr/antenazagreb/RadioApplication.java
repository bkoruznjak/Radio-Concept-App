package bkoruznjak.from.hr.antenazagreb;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.util.NetworkUtils;

/**
 * Created by bkoruznjak on 29/06/16.
 */
public class RadioApplication extends Application {
    private static RadioApplication instance;
    private static RadioBus myBus;
    private static RadioStateModel myStateModel;

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

}
