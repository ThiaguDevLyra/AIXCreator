package io.lyra.aix.lyraadaptiveweb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
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

    private Context context;
    private Activity activity;
    private WebView webView;
    private boolean isKeyboardListenerAttached = false;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.activity = (Activity) context;
    }

    @SimpleFunction(description = "Inicializa o WebViewer adaptativo dentro de um arranjo (Horizontal ou Vertical).")
    public void InitializeInLayout(AndroidViewComponent layout) {
        if (webView == null) {
            webView = new WebView(context);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    PageLoaded(url);
                }
            });
            webView.setWebChromeClient(new WebChromeClient());
        } else {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
        }

        View view = layout.getView();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            viewGroup.addView(webView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setupKeyboardListener();
    }

    private void setupKeyboardListener() {
        if (isKeyboardListenerAttached) return;
        
        final View rootView = activity.findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();
                    rootView.getWindowVisibleDisplayFrame(r);
                    int screenHeight = rootView.getRootView().getHeight();
                    int keypadHeight = screenHeight - r.bottom;

                    if (keypadHeight > screenHeight * 0.15) {
                        if (webView != null) {
                            webView.setPadding(0, 0, 0, keypadHeight);
                        }
                    } else {
                        if (webView != null) {
                            webView.setPadding(0, 0, 0, 0);
                        }
                    }
                }
            });
            isKeyboardListenerAttached = true;
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

    @SimpleFunction(description = "Limpa o cache do WebViewer.")
    public void ClearCache() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    @SimpleProperty(description = "Define o User Agent do WebViewer.")
    public void UserAgent(String userAgent) {
        if (webView != null) {
            webView.getSettings().setUserAgentString(userAgent);
        }
    }

    @SimpleProperty(description = "Retorna o User Agent atual do WebViewer.")
    public String UserAgent() {
        if (webView != null) {
            return webView.getSettings().getUserAgentString();
        }
        return "";
    }

    @SimpleEvent(description = "Disparado quando a página termina de carregar.")
    public void PageLoaded(String url) {
        EventDispatcher.dispatchEvent(this, "PageLoaded", url);
    }
}