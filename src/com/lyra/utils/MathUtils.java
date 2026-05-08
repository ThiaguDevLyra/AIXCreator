package com.lyra.utils;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = 1,
    description = "Simple Math Extension for App Inventor",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png")
@SimpleObject(external = true)
public class MathUtils extends AndroidNonvisibleComponent {

    public MathUtils(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Returns the sum of two numbers.")
    public double Add(double a, double b) {
        return a + b;
    }

    @SimpleFunction(description = "Returns the difference of two numbers.")
    public double Subtract(double a, double b) {
        return a - b;
    }
}