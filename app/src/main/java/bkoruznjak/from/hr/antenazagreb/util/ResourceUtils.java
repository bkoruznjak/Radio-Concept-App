package bkoruznjak.from.hr.antenazagreb.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import bkoruznjak.from.hr.antenazagreb.constants.AntenaConstants;

/**
 * Created by bkoruznjak on 11/10/2016.
 */

public class ResourceUtils {

    public static int imgResIdFromName(Context context, String imgFileName) {
        if (context == null || imgFileName == null) {
            Log.e("bbb", "Input params cannot be null!");
            return context.getResources().getIdentifier(AntenaConstants.GENERIC_STREAM_ICON_NAME, "drawable", context.getPackageName());
        }
        String fileNameWithPrefix = String.format("img_%s", imgFileName);
        int identifier = context.getResources().getIdentifier(fileNameWithPrefix, "drawable", context.getPackageName());
        if (identifier == 0) {
            fileNameWithPrefix = String.format("ic_%s", imgFileName);
            identifier = context.getResources().getIdentifier(fileNameWithPrefix, "drawable", context.getPackageName());
            if (identifier == 0) {
                identifier = context.getResources().getIdentifier(imgFileName, "drawable", context.getPackageName());
                if (identifier == 0) {
                    return context.getResources().getIdentifier(AntenaConstants.GENERIC_STREAM_ICON_NAME, "drawable", context.getPackageName());
                }
            }
        }
        return identifier;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
