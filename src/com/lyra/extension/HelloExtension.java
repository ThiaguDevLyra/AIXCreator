package com.lyra.extension;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = 1, 
                   category = ComponentCategory.EXTENSION, 
                   description = "Corrected Extension after Workflow fix", 
                   nonVisible = true)
@SimpleObject(external = true)
public class HelloExtension extends AndroidNonvisibleComponent {
    public HelloExtension(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Returns a hello message.")
    public String Greet(String name) {
        return "Hello, " + name;
    }
}