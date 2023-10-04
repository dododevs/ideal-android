package revolver.ideal.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.io.InputStream;

import revolver.ideal.R;
import revolver.ideal.ui.fragment.UserOwnProfilePersonalDataFragment;
import revolver.ideal.ui.fragment.UserOwnProfileSocialFragment;

public class UserOwnProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_own_profile);

        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        final ImageView profilePictureView = findViewById(R.id.activity_user_own_profile_picture);
        try (final InputStream is = getAssets().open("images/test.jpg")) {
            final Bitmap b = BitmapFactory.decodeStream(is);
            profilePictureView.setImageBitmap(b);
        } catch (IOException e) {
        }

        final ViewPager2 infoPagerView = findViewById(R.id.pager);
        infoPagerView.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return new UserOwnProfilePersonalDataFragment();
                } else {
                    return new UserOwnProfilePersonalDataFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        final TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, infoPagerView, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.activity_user_own_profile_tab_data);
            } else if (position == 1) {
                tab.setText(R.string.activity_user_own_profile_tab_social);
            }
        }).attach();
    }
}
