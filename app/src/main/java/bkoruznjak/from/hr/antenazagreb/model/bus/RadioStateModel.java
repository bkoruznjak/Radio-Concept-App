package bkoruznjak.from.hr.antenazagreb.model.bus;

import java.io.Serializable;

import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.enums.RadioStateEnum;
import bkoruznjak.from.hr.antenazagreb.model.network.StreamModel;

/**
 * Created by bkoruznjak on 29/06/16.
 */
public class RadioStateModel implements Serializable {

    private boolean isServiceUp;
    private boolean isMusicPlaying;
    //added for testing when something breaks
    private boolean isStreamInterrupted;

    private String streamUri;
    private String radioStationName;
    private String radioStationNameImage;
    private String radioStationIconId;
    private String songAuthor;
    private String songTitle;
    private StreamModel currentStreamModel;

    private RadioStateEnum stateEnum;

    public RadioStateModel() {
        this(false, false);
    }

    public RadioStateModel(boolean isServiceUp, boolean isMusicPlaying) {
        this.isServiceUp = isServiceUp;
        this.isMusicPlaying = isMusicPlaying;

        this.setStreamUri(StreamUriConstants.ANTENA_MAIN);
        this.setRadioStationName(StreamUriConstants.NAME_ANTENA_MAIN);
        this.setSongAuthor("Unknown");
        this.setSongTitle("Unknown");
        this.setStateEnum(RadioStateEnum.UNKNOWN);
        this.currentStreamModel = new StreamModel();
        currentStreamModel.id = "1";
        currentStreamModel.name = StreamUriConstants.NAME_ANTENA_MAIN;
        currentStreamModel.url = StreamUriConstants.ANTENA_MAIN;
        currentStreamModel.iconId = "ic_live_stream_icon_beige";
    }

    public boolean isServiceUp() {
        return isServiceUp;
    }

    public void setServiceUp(boolean isServiceUp) {
        this.isServiceUp = isServiceUp;
    }

    public boolean isMusicPlaying() {
        return isMusicPlaying;
    }

    public void setMusicPlaying(boolean isMusicPlaying) {
        this.isMusicPlaying = isMusicPlaying;
    }

    public StreamModel getStreamModel() {
        return this.currentStreamModel;
    }

    public void setStreamModel(StreamModel streamModel) {
        this.currentStreamModel = streamModel;
        this.radioStationName = streamModel.name;
        this.radioStationNameImage = streamModel.imageUrl;
    }

    public String getStreamUri() {
        return streamUri;
    }

    public void setStreamUri(String streamUri) {
        this.streamUri = streamUri;
    }

    public String getRadioStationName() {
        return radioStationName;
    }

    public void setRadioStationName(String radioStationName) {
        this.radioStationName = radioStationName;
    }

    public String getRadioStationNameImage() {
        return radioStationNameImage;
    }

    public void setRadioStationNameImage(String radioStationNameImage) {
        this.radioStationNameImage = radioStationNameImage;
    }

    public String getRadioStationIconId() {
        return radioStationIconId;
    }

    public void setRadioStationIconId(String radioStationIconId) {
        this.radioStationIconId = radioStationIconId;
    }

    public String getSongAuthor() {
        return songAuthor;
    }

    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public RadioStateEnum getStateEnum() {
        return stateEnum;
    }

    public void setStateEnum(RadioStateEnum stateEnum) {
        this.stateEnum = stateEnum;
    }

    public boolean isStreamInterrupted() {
        return this.isStreamInterrupted;
    }

    public void setStreamInterrupted(boolean interrupted) {
        this.isStreamInterrupted = interrupted;
    }

    @Override
    public String toString() {
        return ""
                .concat("service running: " + isServiceUp)
                .concat(", music playing: " + isMusicPlaying)
                .concat(", radio station: " + radioStationName)
                .concat(", radio image: " + radioStationNameImage)
                .concat(", radio icon: " + radioStationIconId)
                .concat(", stream uri: " + streamUri)
                .concat(", stream author: " + songAuthor)
                .concat(", stream title: " + songTitle);
    }
}
