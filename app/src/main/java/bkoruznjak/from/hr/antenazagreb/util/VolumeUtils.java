package bkoruznjak.from.hr.antenazagreb.util;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import bkoruznjak.from.hr.antenazagreb.listener.OnSoundVolumeChangeListener;

/**
 * Created by bkoruznjak on 01/07/16.
 */
public class VolumeUtils {

    /**
     * Example
     *             TimerUtils.startSoundIncreaseTimer(new OnSoundVolumeChangeListener() {
                        @Override
                        public void volumeChanged(float volume) {
                        soundPool.setVolume(touchTrailStreamId, volume, volume);
                         }
                    });
     *
     *
     */

    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
    private static ScheduledFuture<?> soundFuture;
    private static float volume = 0f;

    public static void startSoundIncreaseTimer(final OnSoundVolumeChangeListener soundVolumeChangeListener) {
        volume = 0f;
        final float volumeDelta = 0.05f;
        if (soundFuture != null) {
            soundFuture.cancel(true);
        }
        soundFuture = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                volume += volumeDelta;
                if (soundVolumeChangeListener != null) {
                    soundVolumeChangeListener.volumeChanged(volume);
                }
                if (volume >= 1f) {
                    if (soundVolumeChangeListener != null) {
                        soundVolumeChangeListener.volumeChanged(1f);
                    }
                    soundFuture.cancel(true);
                    soundFuture = null;
                }
            }
        }, 50, 50, TimeUnit.MILLISECONDS);
    }

    public static void startSoundFadeOutTimer(final OnSoundVolumeChangeListener soundVolumeChangeListener) {
        volume = 1f;
        final float volumeDelta = 0.05f;
        if (soundFuture != null) {
            soundFuture.cancel(true);
        }
        soundFuture = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                volume -= volumeDelta;
                if (soundVolumeChangeListener != null) {
                    soundVolumeChangeListener.volumeChanged(volume);
                }
                if (volume <= 0f) {
                    if (soundVolumeChangeListener != null) {
                        soundVolumeChangeListener.volumeChanged(0f);
                    }
                    soundFuture.cancel(true);
                    soundFuture = null;
                }
            }
        }, 50, 50, TimeUnit.MILLISECONDS);
    }

}
