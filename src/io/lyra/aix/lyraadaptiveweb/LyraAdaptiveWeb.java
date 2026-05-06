package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.graphics.Rect;
import android.app.Activity;
import android.widget.FrameLayout;
import android.content.Context;

@DesignerComponent(
    version = 1,
    description = "Gerado pelo Lyra AIX Creator - Extensão para Lyra Adaptive Web com ajuste automático de teclado.",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private final Context context;
    private final Activity activity;
    private WebView webView;
    private ViewGroup containerView;
    private boolean isKeyboardVisible = false;
    private int originalHeight = -1;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.activity = (Activity) container.$context();
    }

    @SimpleFunction(description = "Inicializa o WebViewer adaptativo dentro de um container (ex: Legenda ou Arranjo).")
    public void Initialize(AndroidViewComponent container) {
        this.containerView = (ViewGroup) container.getView();
        
        webView = new WebView(context);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

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

        containerView.addView(webView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        setupKeyboardListener();
    }

    private void setupKeyboardListener() {
        final View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // Teclado aberto
                    if (!isKeyboardVisible) {
                        isKeyboardVisible = true;
                        KeyboardStatusChanged(true, keypadHeight);
                        adjustWebViewHeight(screenHeight - keypadHeight);
                    }
                } else { // Teclado fechado
                    if (isKeyboardVisible) {
                        isKeyboardVisible = false;
                        KeyboardStatusChanged(false, 0);
                        resetWebViewHeight();
                    }
                }
            }
        });
    }

    private void adjustWebViewHeight(int newHeight) {
        if (webView != null) {
            ViewGroup.LayoutParams params = webView.getLayoutParams();
            if (originalHeight == -1) originalHeight = params.height;
            params.height = newHeight;
            webView.setLayoutParams(params);
            webView.requestLayout();
        }
    }

    private void resetWebViewHeight() {
        if (webView != null) {
            ViewGroup.LayoutParams params = webView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            webView.setLayoutParams(params);
            webView.requestLayout();
        }
    }

    @SimpleFunction(description = "Carrega uma URL no WebViewer.")
    public void LoadUrl(String url) {
        if (webView != null) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            webView.loadUrl(url);
        }
    }

    @SimpleFunction(description = "Executa um código JavaScript no WebViewer.")
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

    @SimpleFunction(description = "Recarrega a página atual.")
    public void Reload() {
        if (webView != null) webView.reload();
    }

    @SimpleFunction(description = "Volta para a página anterior se possível.")
    public void GoBack() {
        if (webView != null && webView.canGoBack()) webView.goBack();
    }

    @SimpleFunction(description = "Avança para a próxima página se possível.")
    public void GoForward() {
        if (webView != null && webView.canGoForward()) webView.goForward();
    }

    @SimpleProperty(description = "Define se o JavaScript está habilitado.")
    public void JavaScriptEnabled(boolean enabled) {
        if (webView != null) webView.getSettings().setJavaScriptEnabled(enabled);
    }

    @SimpleEvent(description = "Evento disparado quando uma página termina de carregar.")
    public void PageLoaded(String url) {
        EventDispatcher.dispatchEvent(this, "PageLoaded", url);
    }

    @SimpleEvent(description = "Evento disparado quando ocorre um erro no carregamento.")
    public void ErrorOccurred(String message) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", message);
    }

    @SimpleEvent(description = "Evento disparado quando o status do teclado muda.")
    public void KeyboardStatusChanged(boolean visible, int height) {
        EventDispatcher.dispatchEvent(this, "KeyboardStatusChanged", visible, height);
    }

    @SimpleProperty(description = "Retorna a URL atual.")
    public String CurrentUrl() {
        return webView != null ? webView.getUrl() : "";
    }
}