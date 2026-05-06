package io.lyra.aix.lyraadaptiveweb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

@DesignerComponent(
    version = 1,
    description = "Gerado pelo Lyra AIX Creator",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private Activity activity;
    private WebView webView;
    private ComponentContainer container;
    private boolean adaptKeyboard = true;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.container = container;
        this.activity = container.$context();
        
        // Apply default keyboard adaptation
        AdaptToKeyboard(true);
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "True")
    @SimpleProperty(description = "Se verdadeiro, a tela se redimensiona para não ser coberta pelo teclado virtual.")
    public void AdaptToKeyboard(boolean adapt) {
        this.adaptKeyboard = adapt;
        if (adapt) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    @SimpleProperty(description = "Retorna se a adaptação ao teclado está ativada.")
    public boolean AdaptToKeyboard() {
        return this.adaptKeyboard;
    }

    @SimpleFunction(description = "Cria o WebViewer customizado dentro de um arranjo (ex: HorizontalArrangement ou VerticalArrangement).")
    public void CreateWebView(AndroidViewComponent layout) {
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
        } else {
            webView = new WebView(activity);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PageStarted(url);
                        }
                    });
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PageLoaded(url);
                        }
                    });
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ErrorOccurred(errorCode, description, failingUrl);
                        }
                    });
                }
            });

            webView.setWebChromeClient(new WebChromeClient());
        }

        View view = layout.getView();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            viewGroup.addView(webView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
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

    @SimpleFunction(description = "Executa um código JavaScript na página atual.")
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

    @SimpleEvent(description = "Disparado após a execução de um código JavaScript, retornando o resultado.")
    public void JavaScriptEvaluated(String result) {
        EventDispatcher.dispatchEvent(this, "JavaScriptEvaluated", result);
    }
}