package com.lyra.example;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = 1,
    description = "A simple extension to demonstrate fixed build process",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png")
@SimpleObject(external = true)
public class ExampleExtension extends AndroidNonvisibleComponent {

    public ExampleExtension(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Returns a greeting message")
    public String Greet(String name) {
        return "Hello, " + name + "! Lyra is here.";
    }
}