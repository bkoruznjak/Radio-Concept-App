package bkoruznjak.from.hr.antenazagreb.streams;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.StreamModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bkoruznjak on 15/10/2016.
 */

public class StreamStore {
    private static RadioBus myBus;

    public StreamStore() {
        myBus = RadioApplication.getInstance().getBus();
        myBus.register(this);
    }

    public void fetchAllStreams() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkConstants.API_STREAMS_URI.concat(NetworkConstants.API_KEY))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ArrayList<StreamModel> streamList = (ArrayList<StreamModel>) LoganSquare.parseList(response.body().byteStream(), StreamModel.class);
                    response.body().byteStream().close();
                    myBus.post(streamList);
                } catch (IOException IoEx) {
                    ArrayList<StreamModel> streamListError = new ArrayList<StreamModel>();
                    StreamModel errorStreamModel = new StreamModel();
                    errorStreamModel.name = NetworkConstants.ERROR_MESSAGE;
                    streamListError.add(errorStreamModel);
                    myBus.post(streamListError);
                    Log.e("BBB", IoEx.toString());
                }
            }
        }).start();
    }
}
