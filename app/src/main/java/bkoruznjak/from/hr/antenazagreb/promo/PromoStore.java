package bkoruznjak.from.hr.antenazagreb.promo;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.PromoModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bkoruznjak on 20/10/2016.
 */

public class PromoStore {
    private static RadioBus myBus;

    public PromoStore() {
        myBus = RadioApplication.getInstance().getBus();
        myBus.register(this);
    }

    public void fetchAllPromotions() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkConstants.API_PROMOTIONS_URI.concat(NetworkConstants.API_KEY))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ArrayList<PromoModel> promoList = (ArrayList<PromoModel>) LoganSquare.parseList(response.body().byteStream(), PromoModel.class);
                    response.body().byteStream().close();
                    myBus.post(promoList);
                } catch (IOException IoEx) {
                    Log.e("BBB", IoEx.toString());
                }
            }
        }).start();
    }
}
