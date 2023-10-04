package revolver.ideal.util.ui;

import android.annotation.SuppressLint;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public final class AnimUtils {

    public static void runLayoutAnimation(@NonNull final RecyclerView recyclerView, @AnimRes int anim) {
        final LayoutAnimationController controller = AnimationUtils
                .loadLayoutAnimation(recyclerView.getContext(), anim);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

}
