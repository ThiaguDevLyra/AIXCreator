package com.lyra.hello;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = 1,
    description = "Extension for Lyra Intelligence",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png")
@SimpleObject(external = true)
public class HelloLyra extends AndroidNonvisibleComponent {
    public HelloLyra(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Returns a greeting")
    public String Greet(String name) {
        return "Hello, " + name + " from Lyra!";
    }
}