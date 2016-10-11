package bkoruznjak.from.hr.antenazagreb.util;

import android.content.Context;
import android.util.Log;

/**
 * Created by bkoruznjak on 11/10/2016.
 */

public class ResourceUtils {

    public static int imgResIdFromName(Context context, String imgFileName) {
        if (context == null || imgFileName == null) {
            Log.e("bbb", "Input params cannot be null!");
            return 0;
        }
        String fileNameWithPrefix = String.format("img_%s", imgFileName);
        int identifier = context.getResources().getIdentifier(fileNameWithPrefix, "drawable", context.getPackageName());
        if (identifier == 0) {
            fileNameWithPrefix = String.format("ic_%s", imgFileName);
            identifier = context.getResources().getIdentifier(fileNameWithPrefix, "drawable", context.getPackageName());
            if (identifier == 0) {
                identifier = context.getResources().getIdentifier(imgFileName, "drawable", context.getPackageName());
                if (identifier == 0) {
                    Log.e("bbb", "Resource drawable " + imgFileName + " not found!");
                }
            }
        }
        return identifier;
    }
}
