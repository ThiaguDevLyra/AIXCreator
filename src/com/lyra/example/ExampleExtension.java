package com.lyra.example;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

@DesignerComponent(version = 1,
                   description = "Fixed extension after compilation error analysis.",
                   category = ComponentCategory.EXTENSION,
                   nonVisible = true,
                   iconName = "images/extension.png")
@SimpleObject(external = true)
public class ExampleExtension extends AndroidNonvisibleComponent {

    public ExampleExtension(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Returns a success message.")
    public String Status() {
        return "Extension compiled and running successfully!";
    }
}