package bkoruznjak.from.hr.antenazagreb.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.activity.MainActivity;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.PreferenceKeyConstants;
import bkoruznjak.from.hr.antenazagreb.enums.RadioStateEnum;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioVolumeModel;
import bkoruznjak.from.hr.antenazagreb.model.db.SongModel;
import bkoruznjak.from.hr.antenazagreb.views.RippleBackground;
import bkoruznjak.from.hr.antenazagreb.views.VolumeSlider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RadioFragment extends Fragment implements VolumeSlider.OnSectorChangedListener {

    @BindView(R.id.volumeControl)
    VolumeSlider volumeControl;
    //    @BindView(R.id.radioStateTextView)
//    TextView txtRadioState;
    @BindView(R.id.authorInfoTextView)
    TextView txtAuthorName;
    @BindView(R.id.songInfoTextView)
    TextView txtSongName;
    View radioFragmentView;
    int radioVolume;
    private int mNotificationIcon;
    private RadioBus myBus;
    private RadioStateModel mRadioStateModel;
    private Animation infiniteRotateAnim;
    private RippleBackground rippleBackground;
    private SharedPreferences mPreferences;

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
        radioVolume = RadioApplication.getInstance().getRadioVolume();
        volumeControl.updateSliderWithSectorID(radioVolume);

    }

    @Override
    public void onPause() {
        super.onPause();
        myBus.unregister(this);
        mPreferences.edit().putInt(PreferenceKeyConstants.KEY_VOLUME, radioVolume).commit();
    }

    @Override
    public void changeSector(int sectorID) {
        //Log.d("BBB", "sector id:" + sectorID);
        radioVolume = sectorID;
        RadioApplication.getInstance().setRadioVolume(radioVolume);
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sectorID, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        mPreferences = getActivity().getSharedPreferences(PreferenceKeyConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        infiniteRotateAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.inf_rotate);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
        RadioApplication antenaApp = ((RadioApplication) getActivity().getApplication());
        mRadioStateModel = antenaApp.getRadioStateModel();
        mNotificationIcon = antenaApp.getRadioNotificationIcon();

        updateViewsByRadioState(mRadioStateModel);
        bindCustomListeners();
    }

    private void bindCustomListeners() {
        volumeControl.setOnSectorChangedListener(this);
    }

    private void updateViewsByRadioState(RadioStateModel stateModel) {
        Log.d("BBB", "updating views with state model:" + stateModel.toString());
        txtAuthorName.setText(stateModel.getSongAuthor().trim());
        txtSongName.setText(stateModel.getSongTitle().trim());
        txtSongName.setSelected(true);
//        txtRadioState.setText(stateModel.getStateEnum().toString());
        refreshControlButtonDrawable(stateModel, infiniteRotateAnim);
    }

    private void refreshControlButtonDrawable(RadioStateModel stateModel, Animation animation) {
        //ovo ojačaj kod jer treba maknut rucno dodavanje na gumb animacije i sranja.
        if (stateModel.isMusicPlaying() && !stateModel.isStreamInterrupted()) {
            rippleBackground.startRippleAnimation();
        } else {
            rippleBackground.stopRippleAnimation();
        }
    }

    @Subscribe
    public void handleStreamStateChange(RadioStateEnum streamState) {
        switch (streamState) {
            case BUFFERING:
//                txtRadioState.setText(RadioStateEnum.BUFFERING.toString());
                break;
            case ENDED:
//                txtRadioState.setText(RadioStateEnum.ENDED.toString());
                rippleBackground.stopRippleAnimation();
                break;
            case IDLE:
//                txtRadioState.setText(RadioStateEnum.IDLE.toString());
                rippleBackground.stopRippleAnimation();
                break;
            case PREPARING:
//                txtRadioState.setText(RadioStateEnum.PREPARING.toString());
                break;
            case READY:
                //stop buffering animation if it exists
//                txtRadioState.setText(RadioStateEnum.READY.toString());
                if (mRadioStateModel.isMusicPlaying() && !mRadioStateModel.isStreamInterrupted()) {
                    rippleBackground.startRippleAnimation();
                } else {
                    rippleBackground.stopRippleAnimation();
                }
                break;
            case UNKNOWN:
//                txtRadioState.setText(RadioStateEnum.UNKNOWN.toString());
                break;
        }
    }

    @Subscribe
    public void handleSongMetadata(SongModel song) {
        //update view song data
        txtAuthorName.setText(song.getmAuthor().trim());
        txtSongName.setText(song.getTitle().trim());
        txtSongName.setSelected(true);
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
                .setSmallIcon(mNotificationIcon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), mNotificationIcon))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        notification.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(1337, notification);
    }

    @Subscribe
    public void handleVolumeKeyPress(RadioVolumeModel volumeEvent) {
        int volume = volumeEvent.getVolume();
        volumeControl.updateSliderWithSectorID(volume);
        RadioApplication.getInstance().setRadioVolume(volume);
    }
}