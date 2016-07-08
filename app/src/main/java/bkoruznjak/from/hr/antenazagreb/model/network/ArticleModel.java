package bkoruznjak.from.hr.antenazagreb.model.network;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by bkoruznjak on 06/07/16.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class ArticleModel implements Serializable {

    public int id;
    public int user_id;
    public String title;
    public String body;
    public Date published_at;
    public Date created_at;
    public Date updated_at;

    public List<Image> images;

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class Image{

        public int id;
        public String title;
        public String caption;
        public int size;
        public String file_name;
        public Date created_at;
        public Date updated_at;
        public String url;
    }
}
