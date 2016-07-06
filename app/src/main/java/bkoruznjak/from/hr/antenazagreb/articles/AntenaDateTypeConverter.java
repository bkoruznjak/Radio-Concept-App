package bkoruznjak.from.hr.antenazagreb.articles;

import com.bluelinelabs.logansquare.typeconverters.DateTypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by bkoruznjak on 06/07/16.
 */
public class AntenaDateTypeConverter extends DateTypeConverter{
    private DateFormat mDateFormat;

    public AntenaDateTypeConverter() {
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public DateFormat getDateFormat() {
        return mDateFormat;
    }

}
