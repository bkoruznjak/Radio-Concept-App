package bkoruznjak.from.hr.antenazagreb.model.network;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bkoruznjak on 11/10/2016.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class StreamModel implements Serializable {
    public String id;
    public String name;
    public String url;
    @JsonField(name = "is_active")
    @SerializedName("is_active")
    public int isActive;
    @JsonField(name = "metadata_url")
    @SerializedName("metadata_url")
    public String metadataUrl;
    @JsonField(name = "image_url")
    @SerializedName("image_url")
    public String imageUrl;
    @JsonField(name = "icon_id")
    @SerializedName("icon_id")
    public String iconId;
    @JsonField(name = "created_at")
    @SerializedName("created_at")
    public String createdAt;
    @JsonField(name = "updated_at")
    @SerializedName("updated_at")
    public String updatedAt;

    @Override
    public String toString() {
        String modelDefinition = "id:" + this.id
                + " is active" + this.isActive
                + " name:" + this.name
                + " url:" + this.url
                + " meta url:" + this.metadataUrl
                + " image url:" + this.imageUrl
                + " icon id:" + this.iconId
                + " created at:" + this.createdAt
                + " updated at:" + this.updatedAt;
        return modelDefinition;
    }
}

