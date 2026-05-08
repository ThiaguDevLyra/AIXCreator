package com.lyra.webview;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(version = 1,
    description = "A simple WebView extension created by Lyra Intelligence.",
    category = ComponentCategory.EXTENSION,
    nonVisible = false,
    iconName = "images/extension.png")
@SimpleObject(external = true)
public class LyraWebView extends AndroidViewComponent {

    private final WebView webView;

    public LyraWebView(ComponentContainer container) {
        super(container);
        webView = new WebView(container.$context());
        
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        
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

    @SimpleProperty(description = "Returns true if the browser can go back.")
    public boolean CanGoBack() {
        return webView.canGoBack();
    }

    @SimpleProperty(description = "Returns true if the browser can go forward.")
    public boolean CanGoForward() {
        return webView.canGoForward();
    }

    @SimpleFunction(description = "Reloads the current page.")
    public void Reload() {
        webView.reload();
    }

    @SimpleFunction(description = "Stops the current load.")
    public void StopLoading() {
        webView.stopLoading();
    }
}