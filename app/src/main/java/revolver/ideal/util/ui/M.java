package revolver.ideal.util.ui;

import android.util.DisplayMetrics;

import static revolver.ideal.util.logic.Conditions.checkNotNull;

import revolver.ideal.util.logic.Conditions;

public class M {

    private static DisplayMetrics sDisplayMetrics;

    public static Float dp(float dp) {
        Conditions.checkNotNull(sDisplayMetrics);
        return dp * sDisplayMetrics.density;
    }

    public static int screenWidth() {
        Conditions.checkNotNull(sDisplayMetrics);
        return sDisplayMetrics.widthPixels;
    }

    public static int screenHeight() {
        Conditions.checkNotNull(sDisplayMetrics);
        return sDisplayMetrics.heightPixels;
    }

    public static void setDisplayMetrics(DisplayMetrics metrics) {
        sDisplayMetrics = new DisplayMetrics();
        sDisplayMetrics.setTo(metrics);
    }

}
