package bkoruznjak.from.hr.antenazagreb.model.network;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bkoruznjak on 11/10/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class StreamModel {
    public String id;
    public String name;
    public String url;
    @JsonField(name = "stream_image_url")
    @SerializedName("stream_image_url")
    public String streamImageUrl;
    @JsonField(name = "stream_icon_name")
    @SerializedName("stream_icon_name")
    public String streamIconName;
    @JsonField(name = "created_at")
    @SerializedName("created_at")
    public String createdAt;
    @JsonField(name = "updated_at")
    @SerializedName("updated_at")
    public String updatedAt;
}

