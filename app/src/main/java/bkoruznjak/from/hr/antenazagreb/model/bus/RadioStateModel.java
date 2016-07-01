package bkoruznjak.from.hr.antenazagreb.model.bus;

import java.io.Serializable;

import bkoruznjak.from.hr.antenazagreb.enums.RadioStateEnum;

/**
 * Created by bkoruznjak on 29/06/16.
 */
public class RadioStateModel implements Serializable{

    private boolean isServiceUp;
    private boolean isMusicPlaying;

    private String streamUri;
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
        this.setSongAuthor("Unknown");
        this.setSongTitle("Unknown");
        this.setStateEnum(RadioStateEnum.UNKNOWN);
    }

    public void setServiceUp(boolean isServiceUp){
        this.isServiceUp = isServiceUp;
    }

    public void setMusicPlaying(boolean isMusicPlaying){
        this.isMusicPlaying = isMusicPlaying;
    }

    public boolean isServiceUp(){
        return isServiceUp;
    }

    public boolean isMusicPlaying(){
        return isMusicPlaying;
    }

    public String getStreamUri() {
        return streamUri;
    }

    public void setStreamUri(String streamUri) {
        this.streamUri = streamUri;
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

    @Override
    public String toString() {
        return ""
                .concat("service running: " + isServiceUp)
                .concat(", music playing: " + isMusicPlaying)
                .concat(", stream uri: " + streamUri)
                .concat(", stream author: " + songAuthor)
                .concat(", stream title: " + songTitle);
    }
}
