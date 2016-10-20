package bkoruznjak.from.hr.antenazagreb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.AntenaConstants;
import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.StreamModel;
import bkoruznjak.from.hr.antenazagreb.streams.StreamStore;

public class LaunchActivity extends AppCompatActivity {
    private RadioBus myBus;
    private ArrayList<StreamModel> mStreamList;
    private AlertDialog.Builder builder;
    private int retryCounter = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        myBus = RadioApplication.getInstance().getBus();
        builder = new AlertDialog.Builder(this);
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
            if (streamData.size() == 0) {
                Crashlytics.log(Log.WARN, "NO STREAMS FOUND", "No active streams found!!! Check the back end");
                builder.setTitle(R.string.no_stream_dialog_title);
                builder.setMessage(R.string.no_stream_dialog_description);
                builder.setPositiveButton(R.string.no_internet_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog noStreamDialog = builder.create();
                noStreamDialog.show();
            } else if (streamData.size() > 0 && NetworkConstants.ERROR_MESSAGE.equals(streamData.get(0).name)) {
                builder.setTitle(R.string.no_internet_dialog_title);
                builder.setMessage(R.string.no_internet_dialog_description);
                builder.setPositiveButton(R.string.no_internet_dialog_accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (retryCounter % 3 == 0) {
                            Intent sendFeedbackIntent = new Intent(Intent.ACTION_SEND);
                            sendFeedbackIntent.setData(Uri.parse("mailto:"));
                            sendFeedbackIntent.setType("text/plain");
                            String subject = getResources().getString(R.string.no_internet_mail_subject);
                            String message = getResources().getString(R.string.no_internet_mail_message);
                            String description = getResources().getString(R.string.no_internet_mail_description);
                            String noClient = getResources().getString(R.string.setting_no_mail_client);
                            sendFeedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{AntenaConstants.SUPPORT_EMAIL});
                            sendFeedbackIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                            sendFeedbackIntent.putExtra(Intent.EXTRA_TEXT, message);
                            try {
                                startActivity(Intent.createChooser(sendFeedbackIntent, description));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(), noClient, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            new StreamStore().fetchAllStreams();
                        }
                        retryCounter++;
                    }
                });
                builder.setNegativeButton(R.string.no_internet_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog noInternetDialog = builder.create();
                noInternetDialog.show();

            } else {
                RadioApplication.getInstance().setStreamList(streamData);
                startActivity(mainActivityIntent);
                finish();
            }
        }
    }
}
