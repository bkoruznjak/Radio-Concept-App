package bkoruznjak.from.hr.antenazagreb.podcasts;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.PodcastModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bkoruznjak on 16/10/2016.
 */

public class PodcastStore {


    private static RadioBus myBus;

    public PodcastStore() {
        myBus = RadioApplication.getInstance().getBus();
        myBus.register(this);
    }

    public void fetchAllPodcasts() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkConstants.API_PODCASTS_URI.concat(NetworkConstants.API_KEY))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ArrayList<PodcastModel> podcastList = (ArrayList<PodcastModel>) LoganSquare.parseList(response.body().byteStream(), PodcastModel.class);
                    response.body().byteStream().close();
                    myBus.post(podcastList);
                } catch (IOException IoEx) {
                    Log.e("BBB", IoEx.toString());
                }
            }
        }).start();
    }
}