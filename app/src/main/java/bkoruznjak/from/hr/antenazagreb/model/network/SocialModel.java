package bkoruznjak.from.hr.antenazagreb.model.network;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bkoruznjak on 02/10/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class SocialModel implements Serializable {

    public String id;
    public String type;
    @JsonField(name = "posted_at")
    @SerializedName("posted_at")
    public String postedAt;
    public String text;
    @JsonField(name = "online_link")
    @SerializedName("online_link")
    public String onlineLink;
    @JsonField(name = "picture_thumb_link")
    @SerializedName("picture_thumb_link")
    public String pictureThumbLink;
    @JsonField(name = "picture_full_link")
    @SerializedName("picture_full_link")
    public String pictureFullLink;

}