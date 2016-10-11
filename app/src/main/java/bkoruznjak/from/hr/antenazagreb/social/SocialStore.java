package bkoruznjak.from.hr.antenazagreb.social;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.SocialModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bkoruznjak on 02/10/2016.
 */

public class SocialStore {

    private static RadioBus myBus;

    public SocialStore() {
        myBus = RadioApplication.getInstance().getBus();
        myBus.register(this);
    }

    public void fetchRecentSocials() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkConstants.API_SOCIAL_URI.concat(NetworkConstants.API_KEY))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ArrayList<SocialModel> socialList = (ArrayList<SocialModel>) LoganSquare.parseList(response.body().byteStream(), SocialModel.class);
                    response.body().byteStream().close();
                    myBus.post(socialList);
                } catch (IOException IoEx) {
                    Log.e("BBB", IoEx.toString());
                }
            }
        }).start();
    }
}