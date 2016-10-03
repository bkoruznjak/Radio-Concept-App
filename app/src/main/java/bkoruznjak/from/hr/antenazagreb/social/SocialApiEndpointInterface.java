package bkoruznjak.from.hr.antenazagreb.social;

import bkoruznjak.from.hr.antenazagreb.model.network.SocialModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by bkoruznjak on 02/10/2016.
 */

public interface SocialApiEndpointInterface {

    @GET("http://api.antenazagreb.xyz/social/getRecent?api_key=myapikey")
    Call<SocialModel> getSocialFeed();
}
