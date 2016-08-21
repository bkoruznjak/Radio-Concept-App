package bkoruznjak.from.hr.antenazagreb.metadata;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import bkoruznjak.from.hr.antenazagreb.model.db.SongModel;

/**
 * Created by bkoruznjak on 21/08/16.
 */
public class AntenaSongProvider {

    private String streamUriString;
    private OkHttpClient okHttpClient;

    public AntenaSongProvider(String streamUriString) {
        this.streamUriString = streamUriString;
        okHttpClient = new OkHttpClient();
    }

    public SongModel fetchCurrentSong() throws IOException {
        Request request = new Request.Builder()
                .url(streamUriString)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String body = response.body().string();
        String songData = body.substring(0, body.indexOf('\n'));
        String[] songRawData = songData.split(";");
        return new SongModel(songRawData[1], songRawData[0]);
    }

    public void setStreamUriString(String streamUriString) {
        this.streamUriString = streamUriString;
    }
}
