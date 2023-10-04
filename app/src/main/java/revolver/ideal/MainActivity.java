package revolver.ideal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.reactnativehce.managers.HceViewModel;
import com.reactnativehce.managers.PrefManager;
import com.reactnativehce.services.CardService;

import revolver.ideal.ui.fragment.UserSettingsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new UserSettingsFragment())
                .commit();

        final HceViewModel model = HceViewModel.getInstance(getApplicationContext());
        model.getLastState().observeForever(s -> {
            Log.d("MainActivity", s);
        });

        final PrefManager prefManager = PrefManager.getInstance(getApplicationContext());
        prefManager.setEnabled(true);
        prefManager.setWritable(false);
        prefManager.setType("url");
        prefManager.setContent("https://dododevs.github.io/cv");

        model.getLastState().postValue(HceViewModel.HCE_STATE_UPDATE_APPLICATION);
        getPackageManager().setComponentEnabledSetting(new ComponentName(
                getApplicationContext(), CardService.class
        ), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        model.getLastState().postValue(HceViewModel.HCE_STATE_ENABLED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPackageManager().setComponentEnabledSetting(new ComponentName(
                getApplicationContext(), CardService.class
        ), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        HceViewModel.getInstance(getApplicationContext())
                .getLastState().postValue(HceViewModel.HCE_STATE_DISABLED);
    }
}