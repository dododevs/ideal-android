package revolver.ideal.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import revolver.ideal.IdealApp;
import revolver.ideal.R;

public class InterEditText extends AppCompatEditText {

    public InterEditText(Context context) {
        this(context, null);
    }

    public InterEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.InterEditText, defStyleAttr, 0);
        switch (a.getInt(R.styleable.InterEditText_specimenStyle, 0)) {
            case 0:
                setTypeface(IdealApp.Fonts.Inter.Black);
                break;
            case 1:
                setTypeface(IdealApp.Fonts.Inter.Bold);
                break;
            case 2:
                setTypeface(IdealApp.Fonts.Inter.ExtraBold);
                break;
            case 3:
                setTypeface(IdealApp.Fonts.Inter.ExtraLight);
                break;
            case 4:
                setTypeface(IdealApp.Fonts.Inter.Light);
                break;
            case 5:
                setTypeface(IdealApp.Fonts.Inter.Medium);
                break;
            case 7:
                setTypeface(IdealApp.Fonts.Inter.SemiBold);
                break;
            case 8:
                setTypeface(IdealApp.Fonts.Inter.Thin);
                break;
            case 6:
            default:
                setTypeface(IdealApp.Fonts.Inter.Regular);
                break;
        }
        a.recycle();
    }

}
