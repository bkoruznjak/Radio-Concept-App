package bkoruznjak.from.hr.antenazagreb.model.db;

import java.io.Serializable;

/**
 * Created by bkoruznjak on 04/06/16.
 */
public class SongModel implements Serializable {

    private String mTitle;
    private String mAuthor;

    public SongModel(){
        this("Unknown", "Unknown");
    }

    public SongModel(String title, String author){
        this.mAuthor = author;
        this.mTitle = title;
    }

    @Override
    public String toString() {
        return mAuthor.concat(" - ").concat(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setAuthor(String author){
        this.mAuthor = author;
    }

    public String getmAuthor(){
        return mAuthor;
    }
}
