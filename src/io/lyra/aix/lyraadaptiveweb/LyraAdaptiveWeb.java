package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;

@DesignerComponent(
    version = 1, 
    description = "Um custom webviewer, com recursos avançados simplificados, que se adapta ao teclado virtual do dispositivo, não permitindo que o teclado cubra o conteúdo da webviewer.", 
    category = ComponentCategory.EXTENSION, 
    nonVisible = true, 
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private Activity activity;
    private WebView webView;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        
        // Adapta a janela do aplicativo para redimensionar quando o teclado virtual aparecer
        this.activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @SimpleFunction(description = "Cria o WebViewer adaptativo dentro de um arranjo (Horizontal ou Vertical).")
    public void CreateWebView(AndroidViewComponent container) {
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
        } else {
            webView = new WebView(activity);
            
            // Configurações avançadas simplificadas
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    PageStarted(url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    PageFinished(url);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    ErrorOccurred(errorCode, description, failingUrl);
                }
            });

            webView.setWebChromeClient(new WebChromeClient());
        }

        ViewGroup viewGroup = (ViewGroup) container.getView();
        viewGroup.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @SimpleFunction(description = "Navega para a URL especificada.")
    public void GoToUrl(String url) {
        if (webView != null) {
            webView.loadUrl(url);
        }
    }

    @SimpleFunction(description = "Volta para a página anterior, se possível.")
    public void GoBack() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SimpleFunction(description = "Avança para a próxima página, se possível.")
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

    @SimpleFunction(description = "Executa um código JavaScript na página atual.")
    public void EvaluateJavaScript(String script) {
        if (webView != null) {
            webView.evaluateJavascript(script, null);
        }
    }

    @SimpleFunction(description = "Ativa ou desativa a adaptação automática da tela ao teclado virtual em tempo de execução.")
    public void SetKeyboardAdaptation(boolean enable) {
        if (enable) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    @SimpleEvent(description = "Disparado quando a página começa a carregar.")
    public void PageStarted(String url) {
        EventDispatcher.dispatchEvent(this, "PageStarted", url);
    }

    @SimpleEvent(description = "Disparado quando a página termina de carregar.")
    public void PageFinished(String url) {
        EventDispatcher.dispatchEvent(this, "PageFinished", url);
    }

    @SimpleEvent(description = "Disparado quando ocorre um erro ao carregar a página.")
    public void ErrorOccurred(int errorCode, String description, String failingUrl) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", errorCode, description, failingUrl);
    }
}