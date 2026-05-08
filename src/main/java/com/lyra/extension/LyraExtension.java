package com.lyra.extension;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import android.widget.Toast;

@DesignerComponent(version = 1,
    description = "Extension created by Lyra Intelligence for MIT App Inventor.",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png")
@SimpleObject(external = true)
public class LyraExtension extends AndroidNonvisibleComponent {

    public LyraExtension(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Shows a simple toast message on the screen.")
    public void ShowToast(String message) {
        Toast.makeText(form, message, Toast.LENGTH_SHORT).show();
    }
}