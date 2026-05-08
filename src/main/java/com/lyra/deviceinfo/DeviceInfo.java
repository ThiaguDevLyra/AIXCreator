package com.lyra.deviceinfo;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import android.os.Build;

@DesignerComponent(
    version = 1,
    description = "Extension to retrieve basic device information",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
public class DeviceInfo extends AndroidNonvisibleComponent {

    public DeviceInfo(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleProperty(description = "Returns the Android version of the device")
    public String AndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    @SimpleProperty(description = "Returns the device model")
    public String Model() {
        return Build.MODEL;
    }
}