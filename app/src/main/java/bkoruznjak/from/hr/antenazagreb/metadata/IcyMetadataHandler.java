package bkoruznjak.from.hr.antenazagreb.metadata;

import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.model.db.SongModel;

/**
 * Created by bkoruznjak on 30/06/16.
 */
public class IcyMetadataHandler {

    private long metadataRefreshRateTimeInMillis;
    private RadioStateModel stateModel;
    private Map<String, String> mMetadata;
    private RadioBus myBus;

    public IcyMetadataHandler(long refreshRate, RadioStateModel stateModel, RadioBus myBus) {
        this.metadataRefreshRateTimeInMillis = refreshRate;
        this.stateModel = stateModel;
        this.myBus = myBus;
        myBus.register(this);
    }

    public void fetchMetaData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (stateModel.isServiceUp()) {
                    try {
                        try {
                            Log.d("BBB", "______________________METADATA______________________");
                            if (stateModel.getStreamUri().equals(StreamUriConstants.ANTENA_MAIN) || stateModel.getStreamUri().equals(StreamUriConstants.ANTENA_HIT)) {
                                String songArtist = "Unknown";
                                String songName = "Unknown";
                                //todo need to expand streamModel by metadataUri and add it also in RadioStateModel
                                SongModel currentSong = new AntenaSongProvider(stateModel.getStreamModel().metadataUrl).fetchCurrentSong();
                                Log.d("bbb", "title:" + currentSong.getTitle() + ", author:" + currentSong.getmAuthor());
                                songArtist = currentSong.getmAuthor();
                                songName = currentSong.getTitle();
                                if (!stateModel.getSongAuthor().equals(songArtist) && !stateModel.getSongTitle().equals(songName)) {
                                    stateModel.setSongAuthor(songArtist);
                                    stateModel.setSongTitle(songName);
                                    myBus.post(currentSong);
                                }
                            } else {
                                URL streamURL = new URL(stateModel.getStreamUri());
                                mMetadata = new SongMetaProvider(streamURL).retreiveMetadata();
                                for (Map.Entry<String, String> entry : mMetadata.entrySet()) {
                                    Log.d("BBB", "key: " + entry.getKey() + " value:" + entry.getValue());
                                    if (entry.getKey().startsWith("StreamTitle")) {
                                        String[] metadataArray = entry.getValue().split(" - ");
                                        String songArtist = "Unknown";
                                        String songName = "Unknown";
                                        if (metadataArray.length > 1) {
                                            songArtist = metadataArray[0];
                                            songName = metadataArray[1];
                                        }
                                        //only post if data differs existing data
                                        if (!stateModel.getSongAuthor().equals(songArtist) && !stateModel.getSongTitle().equals(songName)) {
                                            stateModel.setSongAuthor(songArtist);
                                            stateModel.setSongTitle(songName);
                                            myBus.post(new SongModel(songName, songArtist));
                                        }
                                    }
                                }
                            }

                        } catch (IOException IOex) {
                            Log.e("BBB", "Exception while fetching metadata" + IOex);
                        }

                        Thread.sleep(metadataRefreshRateTimeInMillis);
                    } catch (InterruptedException interuptEx) {
                        Log.e("BBB", "Exception while sleeping meta fetch" + interuptEx);
                    }
                }
                Log.d("BBB", "METADATA DONE");
            }
        }).start();
    }
}
