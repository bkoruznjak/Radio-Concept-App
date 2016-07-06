package bkoruznjak.from.hr.antenazagreb.util;

import com.bluelinelabs.logansquare.LoganSquare;

import java.util.Date;

import bkoruznjak.from.hr.antenazagreb.articles.AntenaDateTypeConverter;

/**
 * Created by bkoruznjak on 06/07/16.
 */
public class NetworkUtils {

    public static void registerLoganSquareTypeConverters(){
        LoganSquare.registerTypeConverter(Date.class, new AntenaDateTypeConverter());
    }
}
