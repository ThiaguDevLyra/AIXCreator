package io.lyra.aix.lyraadaptiveweb;

import android.app.Activity;
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
    private WebView webView;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        initializeWebView();
    }

    private void initializeWebView() {
        webView = new WebView(activity);
        WebSettings settings = webView.getSettings();
        
        // Recursos avançados simplificados
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
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
    }

    @SimpleFunction(description = "Cria o WebViewer dentro de um arranjo (Horizontal ou Vertical) e adapta a tela para o teclado virtual.")
    public void CreateWebView(AndroidViewComponent layout) {
        ViewGroup viewGroup = (ViewGroup) layout.getView();
        if (webView.getParent() != null) {
            ((ViewGroup) webView.getParent()).removeView(webView);
        }
        viewGroup.addView(webView, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));

        // Adapta a janela para redimensionar quando o teclado virtual aparecer
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @SimpleFunction(description = "Carrega uma URL no WebViewer.")
    public void GoToUrl(String url) {
        webView.loadUrl(url);
    }

    @SimpleFunction(description = "Volta para a página anterior, se possível.")
    public void GoBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SimpleFunction(description = "Avança para a próxima página, se possível.")
    public void GoForward() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    @SimpleFunction(description = "Recarrega a página atual.")
    public void Reload() {
        webView.reload();
    }

    @SimpleFunction(description = "Executa um código JavaScript na página atual.")
    public void EvaluateJavaScript(String script) {
        webView.evaluateJavascript(script, null);
    }

    @SimpleProperty(description = "Retorna a URL atual carregada no WebViewer.")
    public String CurrentUrl() {
        return webView.getUrl() != null ? webView.getUrl() : "";
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
    public void ErrorOccurred(String error) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", error);
    }
}