package bkoruznjak.from.hr.antenazagreb.model.network;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bkoruznjak on 13/10/2016.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class PodcastModel implements Serializable {
    public String id;
    public String name;
    public String summary;
    public String author;
    public String url;
    @JsonField(name = "created_at")
    @SerializedName("created_at")
    public String createdAt;
    @JsonField(name = "updated_at")
    @SerializedName("updated_at")
    public String updatedAt;
}
