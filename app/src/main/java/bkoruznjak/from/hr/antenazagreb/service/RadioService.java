package bkoruznjak.from.hr.antenazagreb.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.squareup.otto.Subscribe;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.activity.MainActivity;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.enums.RadioCommandEnum;
import bkoruznjak.from.hr.antenazagreb.enums.RadioStateEnum;
import bkoruznjak.from.hr.antenazagreb.metadata.IcyMetadataHandler;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import okhttp3.OkHttpClient;

/**
 * Created by bkoruznjak on 29/06/16.
 */
public class RadioService extends Service implements ExoPlayer.Listener {

    private RadioBus myBus;
    private RadioStateModel radioState;
    private ExoPlayer mExoPlayer;
    private IcyMetadataHandler icyMetadataHandler;
    private MediaCodecAudioTrackRenderer mAudioTrackRenderer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBus = ((RadioApplication) getApplication()).getBus();
        radioState = ((RadioApplication) getApplication()).getRadioStateModel();
        radioState.setServiceUp(true);
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayer.Factory.newInstance(1);
            mExoPlayer.addListener(this);
        }
        myBus.register(this);
        icyMetadataHandler = new IcyMetadataHandler(10000, radioState, myBus);
        icyMetadataHandler.fetchMetaData();
        Log.d("BBB", "service CREATED");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BBB", "service STARTED");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Antena Radio")
                .setContentText(radioState.getSongAuthor().concat(" - ").concat(radioState.getSongTitle()))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(1337, notification);
        prepareRadioStream(radioState.getStreamUri(), true);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        radioState.setServiceUp(false);
        myBus.unregister(this);
        purgeRadio();
        Log.d("BBB", "service DESTROYED");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            radioState.setStateEnum(RadioStateEnum.ENDED);
            myBus.post(RadioStateEnum.ENDED);
        }
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                radioState.setStateEnum(RadioStateEnum.BUFFERING);
                myBus.post(RadioStateEnum.BUFFERING);
                break;
            case ExoPlayer.STATE_ENDED:
                radioState.setStateEnum(RadioStateEnum.ENDED);
                myBus.post(RadioStateEnum.ENDED);
                break;
            case ExoPlayer.STATE_IDLE:
                radioState.setStateEnum(RadioStateEnum.IDLE);
                myBus.post(RadioStateEnum.IDLE);
                break;
            case ExoPlayer.STATE_PREPARING:
                radioState.setStateEnum(RadioStateEnum.PREPARING);
                myBus.post(RadioStateEnum.PREPARING);
                break;
            case ExoPlayer.STATE_READY:
                radioState.setStateEnum(RadioStateEnum.READY);
                myBus.post(RadioStateEnum.READY);
                break;
            default:
                radioState.setStateEnum(RadioStateEnum.UNKNOWN);
                myBus.post(RadioStateEnum.UNKNOWN);
                break;
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {
        Log.d("BBB", "ON PLAY WHEN READY COMMITED");

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "Selected stream appears to be down...", duration);
        toast.show();
        stop();
        radioState.setStateEnum(RadioStateEnum.ENDED);
        radioState.setMusicPlaying(false);
        myBus.post(RadioStateEnum.ENDED);
        Log.e("BBB", "on player error," + error.toString());
    }

    private void prepareRadioStream(String streamURI, boolean playWhenReady) {
        if (radioState.isMusicPlaying() && mExoPlayer != null) {
            mExoPlayer.stop();
            radioState.setMusicPlaying(false);
        }
        Allocator bufferAllocator = new DefaultAllocator(NetworkConstants.BUFFER_SEGMENT_SIZE);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpDataSource okHttpDataSource = new OkHttpDataSource(okHttpClient, streamURI, null, bandwidthMeter);
        DataSource okDataSource = new DefaultUriDataSource(this, bandwidthMeter, okHttpDataSource);
        ExtractorSampleSource extractorSampleSource = new ExtractorSampleSource(Uri.parse(streamURI), okDataSource, bufferAllocator, NetworkConstants.BUFFER_SEGMENT_COUNT_256 * NetworkConstants.BUFFER_SEGMENT_SIZE, 3);
        mAudioTrackRenderer = new MediaCodecAudioTrackRenderer(extractorSampleSource, MediaCodecSelector.DEFAULT);
        mExoPlayer.prepare(mAudioTrackRenderer);

        if (playWhenReady) {
            play();
        }
    }

    private void play() {
        if (mExoPlayer != null) {
            radioState.setMusicPlaying(true);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void pause() {
        if (mExoPlayer != null) {
            radioState.setMusicPlaying(false);
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    private void stop() {
        if (mExoPlayer != null) {
            pause();
            mExoPlayer.stop();
            mExoPlayer.seekTo(0);
        }
    }

    public void purgeRadio() {
        stop();
        if (mExoPlayer != null) {
            mExoPlayer.removeListener(this);
            mExoPlayer.release();
            mExoPlayer = null;
            System.gc();
        }
    }

    @Subscribe
    public void radioControlHandler(RadioCommandEnum command) {
        switch (command) {
            case PLAY:
                play();
                break;
            case PAUSE:
                pause();
                break;
            case STOP:
                stop();
                break;
        }
    }

    @Subscribe
    public void changeStation(String stationURI) {
        if (StreamUriConstants.ANTENA_MAIN.equals(stationURI) && !radioState.getStreamUri().equals(stationURI)) {
            radioState.setStreamUri(stationURI);
            if (radioState.isMusicPlaying()) {
                prepareRadioStream(stationURI, true);
            } else {
                prepareRadioStream(stationURI, false);
            }
        } else if (StreamUriConstants.ANTENA_HIT.equals(stationURI) && !radioState.getStreamUri().equals(stationURI)) {
            radioState.setStreamUri(stationURI);
            if (radioState.isMusicPlaying()) {
                prepareRadioStream(stationURI, true);
            } else {
                prepareRadioStream(stationURI, false);
            }
        } else if (StreamUriConstants.ANTENA_ROCK.equals(stationURI) && !radioState.getStreamUri().equals(stationURI)) {
            radioState.setStreamUri(stationURI);
            if (radioState.isMusicPlaying()) {
                prepareRadioStream(stationURI, true);
            } else {
                prepareRadioStream(stationURI, false);
            }
        } else if (StreamUriConstants.ANTENA_2000.equals(stationURI) && !radioState.getStreamUri().equals(stationURI)) {
            radioState.setStreamUri(stationURI);
            if (radioState.isMusicPlaying()) {
                prepareRadioStream(stationURI, true);
            } else {
                prepareRadioStream(stationURI, false);
            }
        } else if (StreamUriConstants.ANTENA_90.equals(stationURI) && !radioState.getStreamUri().equals(stationURI)) {
            radioState.setStreamUri(stationURI);
            if (radioState.isMusicPlaying()) {
                prepareRadioStream(stationURI, true);
            } else {
                prepareRadioStream(stationURI, false);
            }
        } else if (StreamUriConstants.ANTENA_80.equals(stationURI) && !radioState.getStreamUri().equals(stationURI)) {
            radioState.setStreamUri(stationURI);
            if (radioState.isMusicPlaying()) {
                prepareRadioStream(stationURI, true);
            } else {
                prepareRadioStream(stationURI, false);
            }
        }
    }

//    @Subscribe
//    public void handleVolume(VolumeEnum volumeDirection){
//        switch (volumeDirection){
//            case VOLUME_UP:
//                mExoPlayer.sendMessage(mAudioTrackRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, 0.2f);
//                break;
//            case VOLUME_DOWN:
//                mExoPlayer.sendMessage(mAudioTrackRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, 0.0f);
//                break;
//        }
//    }
}
