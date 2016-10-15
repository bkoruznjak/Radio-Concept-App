package bkoruznjak.from.hr.antenazagreb.constants;

/**
 * Created by bkoruznjak on 29/06/16.
 */
public class NetworkConstants {

    //COMBINE FOR UNLIMITED POWWWWWWWWWWER
    public static final String API_KEY = "myapikey";
    public static final String API_NEWS_URI = "http://api.antenazagreb.xyz/news/getAll?api_key=";
    public static final String API_STREAMS_URI = "http://api.antenazagreb.xyz/streams/getAll?api_key=";
    public static final String API_SOCIAL_URI = "http://api.antenazagreb.xyz/social/getRecent?api_key=";
    public static final String API_PODCASTS_URI = "http://api.antenazagreb.xyz/podcasts/getAll?api_key=";

    public static final String ERROR_MESSAGE = "ERROR OCCURRED";
    public static final String STREAM_PREFIX_STRING = "http";

    public static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    public static final int BUFFER_SEGMENT_COUNT_256 = 256;
    public static final int BUFFER_SEGMENT_COUNT_160 = 160;
}
