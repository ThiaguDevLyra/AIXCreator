package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@DesignerComponent(
    version = 1, 
    description = "Gerado pelo Lyra AIX Creator - Um custom webviewer, com recursos avançados simplificados, que se adapta ao teclado virtual do dispositivo.", 
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
    }

    @SimpleFunction(description = "Cria o WebViewer customizado dentro de um arranjo (ex: HorizontalArrangement ou VerticalArrangement).")
    public void CreateWebView(AndroidViewComponent container) {
        if (webView == null) {
            webView = new WebView(activity);
            
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setAllowFileAccess(true);

            webView.setWebViewClient(new WebViewClient() {
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

            ViewGroup viewGroup = (ViewGroup) container.getView();
            viewGroup.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ));
        }
    }

    @SimpleFunction(description = "Ativa ou desativa a adaptação da tela ao teclado virtual (Adjust Resize). Se ativado, o teclado não cobrirá o conteúdo.")
    public void AdaptToKeyboard(boolean enable) {
        Window window = activity.getWindow();
        if (window != null) {
            if (enable) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            } else {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
        }
    }

    @SimpleFunction(description = "Navega para a URL especificada.")
    public void GoToUrl(String url) {
        if (webView != null) {
            webView.loadUrl(url);
        }
    }

    @SimpleFunction(description = "Volta para a página anterior no histórico do WebViewer.")
    public void GoBack() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SimpleFunction(description = "Avança para a próxima página no histórico do WebViewer.")
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

    @SimpleProperty(description = "Retorna a URL da página atual.")
    public String CurrentUrl() {
        if (webView != null) {
            return webView.getUrl();
        }
        return "";
    }

    @SimpleEvent(description = "Disparado quando a página termina de carregar.")
    public void PageLoaded(String url) {
        EventDispatcher.dispatchEvent(this, "PageLoaded", url);
    }

    @SimpleEvent(description = "Disparado quando ocorre um erro ao carregar a página.")
    public void ErrorOccurred(String description) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", description);
    }
}