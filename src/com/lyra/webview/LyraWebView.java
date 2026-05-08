package com.lyra.webview;

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
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = 1,
    description = "A simple WebView extension for MIT App Inventor created by Lyra Intelligence.",
    category = ComponentCategory.EXTENSION,
    nonVisible = false,
    iconName = "images/extension.png")
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

    @SimpleFunction(description = "Loads the specified URL.")
    public void GoToUrl(String url) {
        webView.loadUrl(url);
    }

    @SimpleFunction(description = "Reloads the current page.")
    public void Reload() {
        webView.reload();
    }

    @SimpleFunction(description = "Go back in history.")
    public void GoBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SimpleProperty(description = "Returns the current URL.")
    public String CurrentUrl() {
        return webView.getUrl();
    }
}