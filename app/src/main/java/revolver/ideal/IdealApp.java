package revolver.ideal;

import android.app.Application;
import android.graphics.Typeface;

import revolver.ideal.util.ui.M;

public class IdealApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        M.setDisplayMetrics(getResources().getDisplayMetrics());

        Fonts.Inter.Black = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-Black.ttf");
        Fonts.Inter.Bold = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-Bold.ttf");
        Fonts.Inter.ExtraBold = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-ExtraBold.ttf");
        Fonts.Inter.ExtraLight = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-ExtraLight.ttf");
        Fonts.Inter.Light = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-Light.ttf");
        Fonts.Inter.Medium = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-Medium.ttf");
        Fonts.Inter.Regular = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-Regular.ttf");
        Fonts.Inter.SemiBold = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-SemiBold.ttf");
        Fonts.Inter.Thin = Typeface.createFromAsset(getAssets(), "fonts/Inter/Inter-Thin.ttf");
    }

    public static final class Fonts {
        public static final class Inter {
            public static Typeface Black, Bold, ExtraBold, ExtraLight, Light, Medium, Regular, SemiBold, Thin;
        }
    }
}