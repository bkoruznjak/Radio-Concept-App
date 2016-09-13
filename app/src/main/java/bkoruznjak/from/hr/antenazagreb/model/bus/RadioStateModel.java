package bkoruznjak.from.hr.antenazagreb.model.bus;

import java.io.Serializable;

import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.enums.RadioStateEnum;

/**
 * Created by bkoruznjak on 29/06/16.
 */
public class RadioStateModel implements Serializable{

    private boolean isServiceUp;
    private boolean isMusicPlaying;
    //added for testing when something breaks
    private boolean isStreamInterrupted;

    private String streamUri;
    private String radioStationName;
    private String songAuthor;
    private String songTitle;

    private RadioStateEnum stateEnum;

    public RadioStateModel(){
        this(false,false);
    }

    public RadioStateModel(boolean isServiceUp,boolean isMusicPlaying){
        this.isServiceUp = isServiceUp;
        this.isMusicPlaying = isMusicPlaying;

        this.setStreamUri("Unknown");
        this.setRadioStationName("Unknown");
        this.setSongAuthor("Unknown");
        this.setSongTitle("Unknown");
        this.setStateEnum(RadioStateEnum.UNKNOWN);
    }

    public boolean isServiceUp(){
        return isServiceUp;
    }

    public void setServiceUp(boolean isServiceUp) {
        this.isServiceUp = isServiceUp;
    }

    public boolean isMusicPlaying(){
        return isMusicPlaying;
    }

    public void setMusicPlaying(boolean isMusicPlaying) {
        this.isMusicPlaying = isMusicPlaying;
    }

    public String getStreamUri() {
        return streamUri;
    }

    public void setStreamUri(String streamUri) {
        this.streamUri = streamUri;
        if (StreamUriConstants.ANTENA_MAIN.equals(streamUri)) {
            radioStationName = "Antena Live";
        } else if (StreamUriConstants.ANTENA_80.equals(streamUri)) {
            radioStationName = "Antena 80's";
        } else if (StreamUriConstants.ANTENA_ROCK.equals(streamUri)) {
            radioStationName = "Antena Rock";
        }
    }

    public String getRadioStationName() {
        return radioStationName;
    }

    public void setRadioStationName(String radioStationName) {
        this.radioStationName = radioStationName;
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
                .concat(", stream uri: " + streamUri)
                .concat(", stream author: " + songAuthor)
                .concat(", stream title: " + songTitle);
    }
}
