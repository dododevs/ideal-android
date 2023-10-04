package revolver.ideal.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.InputStream;

import revolver.ideal.IdealApp;
import revolver.ideal.R;
import revolver.ideal.ui.activity.UserOwnProfileActivity;

public class UserSettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user_settings, container, false);

        final ImageView profilePictureView = rootView.findViewById(R.id.fragment_user_settings_profile_picture);
        try (final InputStream is = requireContext().getAssets().open("images/test.jpg")) {
            final Bitmap b = BitmapFactory.decodeStream(is);
            profilePictureView.setImageBitmap(b);
        } catch (IOException e) {
        }

        final MaterialButton editProfileButton =
                rootView.findViewById(R.id.fragment_user_settings_edit_profile_btn);
        requireActivity().getWindow().setEnterTransition(new Slide());
        requireActivity().getWindow().setExitTransition(new Fade());
        editProfileButton.setOnClickListener(v -> startActivity(
                new Intent(requireContext(), UserOwnProfileActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            requireActivity(), profilePictureView, "profile_picture")
                                .toBundle()));
        editProfileButton.setTypeface(IdealApp.Fonts.Inter.SemiBold);

        return rootView;
    }
}
