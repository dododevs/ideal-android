package revolver.ideal.ui.activity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.reactnativehce.managers.HceViewModel;
import com.reactnativehce.managers.PrefManager;
import com.reactnativehce.services.CardService;

import revolver.ideal.R;
import revolver.ideal.ui.view.CircularLayout;
import revolver.ideal.util.ui.M;
import revolver.ideal.util.ui.ViewUtils;

public class ShareActivity extends AppCompatActivity {

    private View dim;
    private View logoPlaceholder;
    private View logoIconView;
    private CircularLayout rosterView;

    private boolean hceServiceStatus = false;
    private HceViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        model = HceViewModel.getInstance(getApplicationContext());
        model.getLastState().observeForever(s -> {
            Log.d("hceStatus", s);
            hceServiceStatus = !s.equals(HceViewModel.HCE_STATE_DISABLED);
        });

        dim = findViewById(R.id.dim);
        logoPlaceholder = findViewById(R.id.activity_share_icon_placeholder);
        logoIconView = findViewById(R.id.activity_share_logo_icon);
        rosterView = findViewById(R.id.activity_share_roster);

        rosterView.setOnCenterViewClickListener(v -> {
            if (hceServiceStatus) {
                dim.animate().alpha(0.f)
                        .setDuration(150L)
                        .withEndAction(() -> {
                            dim.setVisibility(View.GONE);
                            dim.setAlpha(1.f);
                        }).start();
                stopSharing();
            } else {
                dim.animate().alpha(1.f)
                        .setDuration(340L)
                        .withStartAction(() -> dim.setVisibility(View.VISIBLE))
                        .start();
                startSharing();
            }
        });

        /*final View btnInstagram = findViewById(R.id.activity_share_btn_ig);
        btnInstagram.setOnTouchListener(new DraggableIconTouchListener());*/
    }

    private void startSharing() {
        final PrefManager prefManager = PrefManager.getInstance(getApplicationContext());
        prefManager.setEnabled(true);
        prefManager.setWritable(false);
        prefManager.setType("url");
        prefManager.setContent("https://getideal.app/");

        model.getLastState().postValue(HceViewModel.HCE_STATE_UPDATE_APPLICATION);
        getPackageManager().setComponentEnabledSetting(new ComponentName(
                getApplicationContext(), CardService.class
        ), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        model.getLastState().postValue(HceViewModel.HCE_STATE_ENABLED);
    }

    private void stopSharing() {
        getPackageManager().setComponentEnabledSetting(new ComponentName(
                getApplicationContext(), CardService.class
        ), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        HceViewModel.getInstance(getApplicationContext())
                .getLastState().postValue(HceViewModel.HCE_STATE_DISABLED);
    }

    /*private class DraggableIconTouchListener implements View.OnTouchListener {
        private float dX, dY;
        private float initialX, initialY;
        private final int[] logoLocation = new int[2];
        private final int[] draggedIconLocation = new int[2];
        private final Rect logoRect = new Rect();
        private final Rect draggedIconRect = new Rect();
        private boolean animatingLogoIcon = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                v.setElevation(M.dp(8.f));
                dim.animate().alpha(1.f)
                        .setDuration(340L)
                        .withStartAction(() -> dim.setVisibility(View.VISIBLE))
                        .start();

                initialX = v.getX();
                initialY = v.getY();

                final FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) v.getLayoutParams();
                params.gravity = Gravity.NO_GRAVITY;
                v.setLayoutParams(params);

                dX = event.getRawX() - params.leftMargin;
                dY = event.getRawY() - params.topMargin;
            } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                final FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) v.getLayoutParams();
                params.leftMargin = (int) (event.getRawX() - dX);
                params.topMargin = (int) (event.getRawY() - dY);
                v.setLayoutParams(params);

                v.getLocationInWindow(draggedIconLocation);
                draggedIconRect.set(
                        draggedIconLocation[0],
                        draggedIconLocation[1],
                        draggedIconLocation[0] + v.getWidth(),
                        draggedIconLocation[1] + v.getHeight()
                );
                logoPlaceholder.getLocationInWindow(logoLocation);
                logoRect.set(
                        logoLocation[0],
                        logoLocation[1],
                        logoLocation[0] + logoPlaceholder.getWidth(),
                        logoLocation[1] + logoPlaceholder.getHeight()
                );
                if (logoRect.intersect(draggedIconRect)) {
                    if (logoIconView.getScaleX() > 0.6f && !animatingLogoIcon) {
                        animatingLogoIcon = true;
                        logoIconView.animate().setDuration(125L)
                                .scaleX(0.6f)
                                .scaleY(0.6f)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .withEndAction(() -> animatingLogoIcon = false)
                                .start();
                    }
                } else {
                    if (logoIconView.getScaleX() < 1.f && !animatingLogoIcon) {
                        animatingLogoIcon = true;
                        logoIconView.animate().setDuration(125L)
                                .scaleX(1.f)
                                .scaleY(1.f)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .withEndAction(() -> animatingLogoIcon = false)
                                .start();
                    }
                }
            } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                v.animate().setDuration(450L)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .x(initialX)
                        .y(initialY)
                        .withEndAction(() -> {
                            v.setElevation(0);
                            if (logoRect.intersect(draggedIconRect)) {
                                *//*v.animate().scaleX(0).scaleY(0)
                                        .setDuration(150L)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .withEndAction(() -> ((ViewGroup) v.getParent()).removeView(v))
                                        .start();*//*
                            }
                        })
                        .start();
                dim.animate().alpha(0.f)
                        .setDuration(150L)
                        .withEndAction(() -> {
                            dim.setVisibility(View.GONE);
                            dim.setAlpha(1.f);
                        }).start();
                if (logoRect.intersect(draggedIconRect)) {
                    logoIconView.setBackgroundResource(R.drawable.ic_contactless);
                    final ImageView addedIcon = new ImageView(ShareActivity.this);
                    addedIcon.setImageResource(R.drawable.bg_btn_ig);
                    *//*((ViewGroup) logoPlaceholder).addView(addedIcon,
                            ViewUtils.newLayoutParams(FrameLayout.LayoutParams.class)
                                    .width(32.f).height(32.f).gravity(Gravity.CENTER)
                                        .startMargin(64.f).get());*//*
                    rosterView.addView(addedIcon, ViewUtils.newLayoutParams()
                            .width(24.f).height(24.f).get());
                } else {
                    logoIconView.animate().setDuration(175L)
                            .scaleX(1.f)
                            .scaleY(1.f)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .withEndAction(() -> animatingLogoIcon = false)
                            .start();
                }

            } else {
                return false;
            }
            return true;
        }
    }*/
}
