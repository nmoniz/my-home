package com.natercio.myhome.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 8/14/13
 * Time: 4:54 PM
 */
public class PixelConverter {

    int density;
    DisplayMetrics metrics;

    ////////////////////
    // PUBLIC METHODS //
    ////////////////////


    public PixelConverter(Context context) {
        metrics = context.getResources().getDisplayMetrics();
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public int pxToDp(int px) {
        return 0;
    }

    public int dpToPx(int dp) {
        return 0;
    }
}
