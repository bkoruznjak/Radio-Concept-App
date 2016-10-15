package bkoruznjak.from.hr.antenazagreb.model.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bkoruznjak on 15/10/2016.
 */

public class StreamPacket implements Serializable {
    private ArrayList<StreamModel> streamList;

    public StreamPacket(ArrayList<StreamModel> streamList) {
        this.streamList = streamList;
    }

    public ArrayList<StreamModel> getStreamList() {
        return this.streamList;
    }
}
