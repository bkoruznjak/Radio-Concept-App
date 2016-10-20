package bkoruznjak.from.hr.antenazagreb.model.network;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bkoruznjak on 20/10/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class PromoModel implements Serializable {

    public String id;
    public String name;
    public String description;
    @JsonField(name = "human_diff_time")
    @SerializedName("human_diff_time")
    public String humanReadableExpiresTime;
    @JsonField(name = "image_url")
    @SerializedName("image_url")
    public String imageUrl;
    @JsonField(name = "expires_at")
    @SerializedName("expires_at")
    public String expiresTime;
    @JsonField(name = "created_at")
    @SerializedName("created_at")
    public String createdAt;
    @JsonField(name = "updated_at")
    @SerializedName("updated_at")
    public String updatedAt;
}