package bkoruznjak.from.hr.antenazagreb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.StreamModel;
import bkoruznjak.from.hr.antenazagreb.streams.StreamStore;

public class LaunchActivity extends AppCompatActivity {
    private RadioBus myBus;
    private ArrayList<StreamModel> mStreamList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        myBus = RadioApplication.getInstance().getBus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myBus.register(this);
        new StreamStore().fetchAllStreams();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myBus.unregister(this);
    }

    @Subscribe
    public void handleStreamDataAndStartMainActivity(final ArrayList<StreamModel> streamData) {
        if (streamData.get(0) instanceof StreamModel) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            //special case error
            if (streamData.size() > 0 && !NetworkConstants.ERROR_MESSAGE.equals(streamData.get(0).name)) {
                RadioApplication.getInstance().setStreamList(streamData);
            }
            startActivity(mainActivityIntent);
            finish();
        }
    }
}
