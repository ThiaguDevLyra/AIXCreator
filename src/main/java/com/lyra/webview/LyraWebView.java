package com.lyra.webview;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.AndroidViewComponent;

@DesignerComponent(
    version = 1,
    description = "A simple WebView extension by Lyra Intelligence",
    category = ComponentCategory.EXTENSION,
    nonVisible = false,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
public class LyraWebView extends AndroidViewComponent {

    private final WebView webView;

    public LyraWebView(ComponentContainer container) {
        super(container);
        webView = new WebView(container.$context());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        container.$add(this);
    }

    @Override
    public View getView() {
        return webView;
    }

    @SimpleFunction(description = "Navigates to the specified URL.")
    public void GoToUrl(String url) {
        webView.loadUrl(url);
    }

    @SimpleFunction(description = "Goes back to the previous page.")
    public void GoBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SimpleProperty(description = "Returns true if the WebView can go back.")
    public boolean CanGoBack() {
        return webView.canGoBack();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "https://www.google.com")
    @SimpleProperty
    public void HomeUrl(String url) {
        webView.loadUrl(url);
    }
}