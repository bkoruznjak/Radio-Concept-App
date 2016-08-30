package bkoruznjak.from.hr.antenazagreb.model.bus;

import java.io.Serializable;

/**
 * Created by bkoruznjak on 30/08/16.
 */
public class RadioVolumeModel implements Serializable {

    private int mVolume;

    public RadioVolumeModel(int volume) {
        this.mVolume = volume;
    }

    public int getVolume() {
        return mVolume;
    }

    public void setVolume(int volume) {
        this.mVolume = volume;
    }
}
