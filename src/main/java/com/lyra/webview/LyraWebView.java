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
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(
    version = 1,
    description = "Lyra WebView: Uma simples extensão de webview customizada.",
    category = ComponentCategory.EXTENSION,
    nonVisible = false,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
public class LyraWebView extends AndroidViewComponent {
    private final WebView webView;

    public LyraWebView(ComponentContainer container) {
        super(container);
        this.webView = new WebView(container.$context());
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.setWebViewClient(new WebViewClient());
        container.$add(this);
    }

    @Override
    public View getView() {
        return this.webView;
    }

    @SimpleFunction(description = "Carrega a URL especificada na WebView.")
    public void GoToUrl(String url) {
        this.webView.loadUrl(url);
    }

    @SimpleProperty(description = "Retorna a URL atual.")
    public String CurrentUrl() {
        return this.webView.getUrl();
    }

    @SimpleFunction(description = "Recarrega a página atual.")
    public void Reload() {
        this.webView.reload();
    }

    @SimpleFunction(description = "Volta para a página anterior, se possível.")
    public void GoBack() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        }
    }
}