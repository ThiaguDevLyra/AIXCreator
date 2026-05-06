package io.lyra.aix.lyraadaptiveweb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;

@DesignerComponent(
        version = 1,
        description = "Um custom webviewer, com recursos avançados simplificados, que se adapta ao teclado virtual do dispositivo, não permitindo que o teclado cubra o conteúdo da webviewer.",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png"
)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE")
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private Activity activity;
    private Context context;
    private WebView webView;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        this.context = container.$context();
        
        // Ativa a adaptação do teclado por padrão ao inicializar a extensão
        EnableKeyboardAdaptation(true);
    }

    @SimpleFunction(description = "Cria o WebViewer customizado dentro de um arranjo (ex: HorizontalArrangement ou VerticalArrangement).")
    public void CreateWebView(AndroidViewComponent layout) {
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
        } else {
            webView = new WebView(context);
            setupWebViewSettings();
        }

        View view = layout.getView();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            viewGroup.addView(webView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void setupWebViewSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);

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
                ErrorOccurred(errorCode, description, failingUrl);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
    }

    @SimpleFunction(description = "Ativa ou desativa a adaptação da tela ao teclado virtual (evita que o teclado cubra o WebViewer).")
    public void EnableKeyboardAdaptation(boolean enable) {
        if (activity != null) {
            if (enable) {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            } else {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
        }
    }

    @SimpleFunction(description = "Carrega uma URL no WebViewer.")
    public void GoToUrl(String url) {
        if (webView != null) {
            webView.loadUrl(url);
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

    @SimpleFunction(description = "Executa um código JavaScript na página atual.")
    public void EvaluateJavaScript(String script) {
        if (webView != null) {
            webView.evaluateJavascript(script, null);
        }
    }

    @SimpleProperty(description = "Retorna a URL atual carregada no WebViewer.")
    public String CurrentUrl() {
        return webView != null ? webView.getUrl() : "";
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
    public void ErrorOccurred(int errorCode, String description, String failingUrl) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", errorCode, description, failingUrl);
    }
}