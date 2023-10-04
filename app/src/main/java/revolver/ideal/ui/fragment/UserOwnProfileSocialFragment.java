package revolver.ideal.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.io.IOException;
import java.io.InputStream;

import revolver.ideal.R;

public class UserOwnProfileSocialFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final GridView rootView = (GridView)
                inflater.inflate(R.layout.fragment_user_own_profile_social, container, false);
        try {
            final String[] logos = requireContext().getAssets().list("images/social");

            rootView.setAdapter(new ArrayAdapter<String>(requireContext(), R.layout.activity_main, logos) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    ShapeableImageView roundLogoView = (ShapeableImageView) convertView;
                    if (roundLogoView == null) {
                        roundLogoView = new ShapeableImageView(requireContext());
                    }
                    try {
                        final InputStream is = requireContext()
                                .getAssets()
                                .open("images/social/" + logos[position]);
                        final Bitmap logoBitmap = BitmapFactory.decodeStream(is);
                        roundLogoView.setShapeAppearanceModel(ShapeAppearanceModel
                                .builder(requireContext(), R.style.ShapeAppearance_Image_Round, 0)
                                .build());
                        roundLogoView.setImageBitmap(logoBitmap);
                        is.close();
                    } catch (IOException e) {
                        Log.w("getView", "fuck", e);
                    }
                    return roundLogoView;
                }
            });
        } catch (IOException e) {
            Log.w("onCreateView", "fuck", e);
        }

        return rootView;
    }
}
