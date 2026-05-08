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
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = 1,
    description = "A simple WebView component powered by Lyra Intelligence.",
    category = ComponentCategory.EXTENSION,
    nonVisible = false,
    iconName = "images/extension.png")
@SimpleObject(external = true)
public class LyraWebView extends AndroidViewComponent {

    private final WebView webView;

    public LyraWebView(ComponentContainer container) {
        super(container);
        this.webView = new WebView(container.$context());
        this.webView.setWebViewClient(new WebViewClient());
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
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

    @SimpleFunction(description = "Goes back in history.")
    public void GoBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SimpleFunction(description = "Goes forward in history.")
    public void GoForward() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    @SimpleFunction(description = "Reloads the current page.")
    public void Reload() {
        webView.reload();
    }

    @SimpleFunction(description = "Stops the current loading.")
    public void StopLoading() {
        webView.stopLoading();
    }

    @SimpleProperty(description = "Returns the current URL.")
    public String CurrentUrl() {
        return webView.getUrl();
    }

    @SimpleProperty(description = "Returns true if the WebView can go back.")
    public boolean CanGoBack() {
        return webView.canGoBack();
    }

    @SimpleProperty(description = "Returns true if the WebView can go forward.")
    public boolean CanGoForward() {
        return webView.canGoForward();
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
    @SimpleProperty
    public void HomeUrl(String url) {
        webView.loadUrl(url);
    }
}