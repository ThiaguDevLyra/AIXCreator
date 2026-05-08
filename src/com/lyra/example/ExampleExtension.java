package com.lyra.example;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;

@DesignerComponent(version = 1,
    description = "Corrected extension by Lyra Intelligence",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png")
@SimpleObject(external = true)
public class ExampleExtension extends AndroidNonvisibleComponent {
    public ExampleExtension(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Returns a validation message")
    public String Status() {
        return "Extension compiled successfully with Java 8 and Apache Ant.";
    }
}