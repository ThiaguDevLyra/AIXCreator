package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@DesignerComponent(
    version = 1,
    description = "Gerado pelo Lyra AIX Creator",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE")
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private Activity activity;
    private WebView webView;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
    }

    @SimpleFunction(description = "Cria o WebViewer customizado dentro de um arranjo (Horizontal ou Vertical) e ajusta a tela para o teclado não cobrir o conteúdo.")
    public void CreateAdaptiveWebView(AndroidViewComponent layout, String startUrl) {
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
        }

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        webView = new WebView(activity);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                PageStarted(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                PageLoaded(url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ErrorOccurred(description);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        ViewGroup container = (ViewGroup) layout.getView();
        container.addView(webView, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ));

        if (startUrl != null && !startUrl.isEmpty()) {
            webView.loadUrl(startUrl);
        }
    }

    @SimpleFunction(description = "Carrega uma nova URL no WebViewer.")
    public void LoadUrl(String url) {
        if (webView != null) {
            webView.loadUrl(url);
        }
    }

    @SimpleFunction(description = "Avalia um código JavaScript no WebViewer.")
    public void EvaluateJavaScript(String script) {
        if (webView != null) {
            webView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    JavaScriptEvaluated(value);
                }
            });
        }
    }

    @SimpleFunction(description = "Volta para a página anterior.")
    public void GoBack() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SimpleFunction(description = "Avança para a próxima página.")
    public void GoForward() {
        if (webView != null && webView.canGoForward()) {
            webView.goForward();
        }
    }

    @SimpleFunction(description = "Recarrega a página atual.")
    public void Reload() {
        if (webView != null) {
            webView.reload();
        }
    }

    @SimpleFunction(description = "Limpa o cache do WebViewer.")
    public void ClearCache() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    @SimpleFunction(description = "Força o ajuste do teclado na tela atual (útil se o redimensionamento falhar em algumas telas).")
    public void EnableKeyboardResize() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @SimpleEvent(description = "Disparado quando a página começa a carregar.")
    public void PageStarted(String url) {
        EventDispatcher.dispatchEvent(this, "PageStarted", url);
    }

    @SimpleEvent(description = "Disparado quando a página termina de carregar.")
    public void PageLoaded(String url) {
        EventDispatcher.dispatchEvent(this, "PageLoaded", url);
    }

    @SimpleEvent(description = "Disparado quando ocorre um erro ao carregar a página.")
    public void ErrorOccurred(String description) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", description);
    }

    @SimpleEvent(description = "Disparado após a avaliação de um código JavaScript.")
    public void JavaScriptEvaluated(String result) {
        EventDispatcher.dispatchEvent(this, "JavaScriptEvaluated", result);
    }
}