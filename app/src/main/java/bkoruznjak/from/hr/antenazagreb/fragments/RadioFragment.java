package bkoruznjak.from.hr.antenazagreb.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.activity.MainActivity;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.enums.RadioCommandEnum;
import bkoruznjak.from.hr.antenazagreb.enums.RadioStateEnum;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.model.db.SongModel;
import bkoruznjak.from.hr.antenazagreb.service.RadioService;
import bkoruznjak.from.hr.antenazagreb.views.RippleBackground;
import bkoruznjak.from.hr.antenazagreb.views.VolumeSlider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RadioFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, VolumeSlider.OnSectorChangedListener {

    @BindView(R.id.btnMainStream)
    Button btnMainStream;
    @BindView(R.id.btnHitStream)
    Button btnHitStream;
    @BindView(R.id.btnRockStream)
    Button btnRockStream;
    @BindView(R.id.btn2000Stream)
    Button btn2000Stream;
    @BindView(R.id.btn90Stream)
    Button btn90Stream;
    @BindView(R.id.btn80Stream)
    Button btn80Stream;
    @BindView(R.id.btnRadioController)
    ImageButton btnRadioController;

    @BindView(R.id.volumeControl)
    VolumeSlider volumeControl;

    @BindView(R.id.radioStateTextView)
    TextView txtRadioState;
    @BindView(R.id.songInfoTextView)
    TextView txtSongInfo;
    View radioFragmentView;
    private RadioBus myBus;
    private RadioStateModel mRadioStateModel;
    private Animation infiniteRotateAnim;
    private RippleBackground rippleBackground;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        radioFragmentView = inflater.inflate(R.layout.fragment_radio, container, false);
        init(radioFragmentView);
        return radioFragmentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rippleBackground != null) {
            rippleBackground.hardStopRippleAnimation();
            rippleBackground = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        myBus.unregister(this);
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        infiniteRotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.inf_rotate);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
        mRadioStateModel = ((RadioApplication) getActivity().getApplication()).getRadioStateModel();
        updateViewsByRadioState(mRadioStateModel);
        bindOnClickListeners();
        bingOnLongClickListeners();
        bindCustomListeners();
    }

    private void bindCustomListeners() {
        volumeControl.setOnSectorChangedListener(this);
    }

    private void bindOnClickListeners() {
        btnMainStream.setOnClickListener(this);
        btnHitStream.setOnClickListener(this);
        btnRockStream.setOnClickListener(this);
        btn2000Stream.setOnClickListener(this);
        btn90Stream.setOnClickListener(this);
        btn80Stream.setOnClickListener(this);
        btnRadioController.setOnClickListener(this);
    }

    private void bingOnLongClickListeners() {
        btnRadioController.setOnLongClickListener(this);
    }

    private void updateViewsByRadioState(RadioStateModel stateModel) {
        Log.d("BBB", "updating views with state model:" + stateModel.toString());
        txtSongInfo.setText(stateModel.getSongAuthor()
                .concat(" - ")
                .concat(stateModel.getSongTitle()));

        txtRadioState.setText(stateModel.getStateEnum().toString());
        refreshControlButtonDrawable(stateModel, infiniteRotateAnim);
    }

    private void refreshControlButtonDrawable(RadioStateModel stateModel, Animation animation) {
        //ovo ojačaj kod jer treba maknut rucno dodavanje na gumb animacije i sranja.
        if (stateModel.getStateEnum() == RadioStateEnum.BUFFERING || stateModel.getStateEnum() == RadioStateEnum.PREPARING) {
            btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_load));
            btnRadioController.startAnimation(infiniteRotateAnim);
        } else if (stateModel.isMusicPlaying() && !stateModel.isStreamInterrupted()) {
            btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause));
            rippleBackground.startRippleAnimation();
        } else {
            btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
            rippleBackground.stopRippleAnimation();
        }
    }


    @Override
    public void onClick(View v) {
        //todo remove tese buttons not needed
        switch (v.getId()) {
            case R.id.btnMainStream:
                Log.d("BBB", "MAIN STREAM PRESSED!");
                myBus.post(StreamUriConstants.ANTENA_MAIN);
                break;
            case R.id.btnHitStream:
                Log.d("BBB", "HIT STREAM PRESSED!");
                myBus.post(StreamUriConstants.ANTENA_HIT);
                break;
            case R.id.btnRockStream:
                Log.d("BBB", "ROCK STREAM PRESSED!");
                myBus.post(StreamUriConstants.ANTENA_ROCK);
                break;
            case R.id.btn2000Stream:
                Log.d("BBB", "2000 STREAM PRESSED!");
                myBus.post(StreamUriConstants.ANTENA_2000);
                break;
            case R.id.btn90Stream:
                Log.d("BBB", "90S STREAM PRESSED!");
                myBus.post(StreamUriConstants.ANTENA_90);
                break;
            case R.id.btn80Stream:
                Log.d("BBB", "80S STREAM PRESSED!");
                myBus.post(StreamUriConstants.ANTENA_80);
                break;
            case R.id.btnRadioController:
                Log.d("BBB", "RADIO CONTROLLER PRESSED!");
                if (mRadioStateModel.isServiceUp() && mRadioStateModel.isMusicPlaying() && !mRadioStateModel.isStreamInterrupted()) {
                    myBus.post(RadioCommandEnum.PAUSE);
                    btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
                    btnRadioController.clearAnimation();
                    rippleBackground.stopRippleAnimation();
                } else if (mRadioStateModel.getStateEnum() == RadioStateEnum.BUFFERING) {
                    //todo ovo treba malo doradit, stavio sam tu samo da mozes prekinut buffeering na naglo
                    myBus.post(RadioCommandEnum.PAUSE);
                    btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
                    btnRadioController.clearAnimation();
                    rippleBackground.stopRippleAnimation();
                } else if (mRadioStateModel.isServiceUp()) {
                    myBus.post(RadioCommandEnum.PLAY);
                } else {
                    Log.d("BBB", "starting service anew");
                    Intent startRadioServiceIntent = new Intent(getActivity(), RadioService.class);
                    getActivity().startService(startRadioServiceIntent);
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btnRadioController:
                Log.d("BBB", "stopping service!");
                myBus.post(RadioCommandEnum.STOP);
                Intent stopRadioServiceIntent = new Intent(getActivity(), RadioService.class);
                getActivity().stopService(stopRadioServiceIntent);

                btnRadioController.clearAnimation();
                btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
                rippleBackground.stopRippleAnimation();
                return true;
        }
        return false;
    }

    @Subscribe
    public void handleStreamStateChange(RadioStateEnum streamState) {
        switch (streamState) {
            case BUFFERING:
                txtRadioState.setText(RadioStateEnum.BUFFERING.toString());
                btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_load));
                btnRadioController.startAnimation(infiniteRotateAnim);
                break;
            case ENDED:
                txtRadioState.setText(RadioStateEnum.ENDED.toString());
                btnRadioController.clearAnimation();
                btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
                rippleBackground.stopRippleAnimation();
                break;
            case IDLE:
                txtRadioState.setText(RadioStateEnum.IDLE.toString());
                btnRadioController.clearAnimation();
                btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
                rippleBackground.stopRippleAnimation();
                break;
            case PREPARING:
                txtRadioState.setText(RadioStateEnum.PREPARING.toString());
                btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_load));
                btnRadioController.startAnimation(infiniteRotateAnim);
                break;
            case READY:
                //stop buffering animation if it exists
                txtRadioState.setText(RadioStateEnum.READY.toString());
                btnRadioController.clearAnimation();
                if (mRadioStateModel.isMusicPlaying() && !mRadioStateModel.isStreamInterrupted()) {
                    btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause));
                    rippleBackground.startRippleAnimation();
                } else {
                    btnRadioController.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
                    rippleBackground.stopRippleAnimation();
                }
                break;
            case UNKNOWN:
                txtRadioState.setText(RadioStateEnum.UNKNOWN.toString());
                break;
        }
    }

    @Subscribe
    public void handleSongMetadata(SongModel song) {
        //update view song data
        txtSongInfo.setText(song.getmAuthor().concat(" - ").concat(song.getTitle()));
        //update notification song data
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0,
                notificationIntent, 0);

        Notification notification = new Notification.Builder(getActivity())
                .setContentTitle("Antena Radio")
                .setContentText(song.getmAuthor().concat(" - ").concat(song.getTitle()))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(1337, notification);
    }

    @Override
    public void changeSector(int sectorID) {
        //Log.d("BBB", "sector id:" + sectorID);
        AudioManager audioManager =
                (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sectorID, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
}