package bkoruznjak.from.hr.antenazagreb.articles;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.ArticleModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bkoruznjak on 06/07/16.
 */
public class ArticleStore {

    private static RadioBus myBus;

    public ArticleStore() {
        myBus = RadioApplication.getInstance().getBus();
        myBus.register(this);
    }

    public void fetchAllArticles() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkConstants.API_URI.concat(NetworkConstants.API_KEY))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    ArrayList<ArticleModel> articleList = (ArrayList<ArticleModel>) LoganSquare.parseList(response.body().byteStream(), ArticleModel.class);
                    response.body().byteStream().close();
                    myBus.post(articleList);
                } catch (IOException IoEx) {
                    Log.e("BBB", IoEx.toString());
                }
            }
        }).start();
    }
}
