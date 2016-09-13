package bkoruznjak.from.hr.antenazagreb.util;

/**
 * Created by bkoruznjak on 13/09/16.
 */

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;
import java.util.Locale;

public final class FontsOverrideUtils {

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                String.format(Locale.US, "fonts/%s", fontAssetName));
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}