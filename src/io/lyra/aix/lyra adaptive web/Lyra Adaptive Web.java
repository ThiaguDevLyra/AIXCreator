package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import java.util.Locale;

@DesignerComponent(
    version = 1,
    description = "Extensão para: Lyra Adaptive Web",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.ACCESS_NETWORK_STATE")
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private Context context;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

    @SimpleFunction(description = "Checks if the device is currently connected to the internet, useful for adaptive web content.")
    public boolean IsConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SimpleFunction(description = "Gets the screen width in pixels to help adapt web layouts.")
    public int GetScreenWidth() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    @SimpleFunction(description = "Gets the screen height in pixels to help adapt web layouts.")
    public int GetScreenHeight() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    @SimpleFunction(description = "Gets the device's current language code (e.g., 'en', 'pt').")
    public String GetDeviceLanguage() {
        return Locale.getDefault().getLanguage();
    }
}