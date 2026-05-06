package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.graphics.Rect;

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

    private final Activity activity;
    private final Context context;
    private WebView webView;
    private boolean isKeyboardVisible = false;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        this.context = container.$context();
    }

    @SimpleFunction(description = "Inicializa o WebView adaptativo dentro de um container (ex: VerticalArrangement).")
    public void InitializeWebView(AndroidViewComponent container) {
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
        }

        webView = new WebView(context);
        View containerView = container.getView();
        
        if (containerView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) containerView;
            viewGroup.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ));
        }

        configureSettings();
        setupClients();
        setupAdaptiveKeyboard();
    }

    private void configureSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    private void setupClients() {
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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                ProgressChanged(newProgress);
            }
        });
    }

    private void setupAdaptiveKeyboard() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int screenHeight = decorView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    if (!isKeyboardVisible) {
                        isKeyboardVisible = true;
                        OnKeyboardStatusChanged(true, keypadHeight);
                    }
                } else {
                    if (isKeyboardVisible) {
                        isKeyboardVisible = false;
                        OnKeyboardStatusChanged(false, 0);
                    }
                }
            }
        });
    }

    @SimpleFunction(description = "Carrega uma URL no WebView.")
    public void LoadUrl(String url) {
        if (webView != null) {
            webView.loadUrl(url);
        }
    }

    @SimpleFunction(description = "Executa JavaScript no contexto da página atual.")
    public void EvaluateJavaScript(final String script) {
        if (webView != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.evaluateJavascript(script, null);
                }
            });
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

    @SimpleFunction(description = "Limpa o cache do WebView.")
    public void ClearCache() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    @SimpleProperty(description = "Retorna se o teclado está visível no momento.")
    public boolean IsKeyboardVisible() {
        return isKeyboardVisible;
    }

    @SimpleProperty(description = "Retorna a URL atual.")
    public String CurrentUrl() {
        return webView != null ? webView.getUrl() : "";
    }

    @SimpleEvent(description = "Evento disparado quando o status do teclado muda.")
    public void OnKeyboardStatusChanged(boolean visible, int height) {
        EventDispatcher.dispatchEvent(this, "OnKeyboardStatusChanged", visible, height);
    }

    @SimpleEvent(description = "Evento disparado quando o carregamento da página inicia.")
    public void PageStarted(String url) {
        EventDispatcher.dispatchEvent(this, "PageStarted", url);
    }

    @SimpleEvent(description = "Evento disparado quando o carregamento da página termina.")
    public void PageFinished(String url) {
        EventDispatcher.dispatchEvent(this, "PageFinished", url);
    }

    @SimpleEvent(description = "Evento disparado quando ocorre um erro no carregamento.")
    public void ErrorOccurred(int errorCode, String description, String failingUrl) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", errorCode, description, failingUrl);
    }

    @SimpleEvent(description = "Evento disparado quando o progresso de carregamento muda.")
    public void ProgressChanged(int progress) {
        EventDispatcher.dispatchEvent(this, "ProgressChanged", progress);
    }
}