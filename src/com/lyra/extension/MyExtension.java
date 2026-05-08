package com.lyra.extension;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

@DesignerComponent(version = 1,
    description = "Extension built by Lyra Intelligence",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "https://appinventor.mit.edu/images/logo.png")
@SimpleObject(external = true)
public class MyExtension extends AndroidNonvisibleComponent {

    public MyExtension(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Returns a greeting message.")
    public String SayHello(String name) {
        return "Hello " + name + " from Lyra!";
    }
}