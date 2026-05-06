package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

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

    private final Activity activity;
    private final Context context;
    private WebView webView;
    private boolean followKeyboard = true;
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        this.context = container.$context();
    }

    @SimpleFunction(description = "Inicializa o WebViewer adaptativo dentro de um container (Ex: VerticalArrangement).")
    public void Initialize(AndroidViewComponent container) {
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
        }

        webView = new WebView(context);
        setupWebViewSettings();
        setupWebViewClients();

        View containerView = container.getView();
        if (containerView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) containerView;
            viewGroup.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ));
        }

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setupAdaptiveLogic();
    }

    private void setupWebViewSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
    }

    private void setupWebViewClients() {
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
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    ErrorOccurred(error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
                }
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

    private void setupAdaptiveLogic() {
        final View decorView = activity.getWindow().getDecorView();
        layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!followKeyboard || webView == null) return;

                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int screenHeight = decorView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // Teclado visível: Ajusta o tamanho para caber na área visível
                    int availableHeight = r.bottom - r.top;
                    ViewGroup.LayoutParams params = webView.getLayoutParams();
                    if (params.height != availableHeight) {
                        params.height = availableHeight;
                        webView.setLayoutParams(params);
                        KeyboardStatusChanged(true, keypadHeight);
                    }
                } else {
                    // Teclado oculto: Volta ao tamanho original
                    ViewGroup.LayoutParams params = webView.getLayoutParams();
                    if (params.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        webView.setLayoutParams(params);
                        KeyboardStatusChanged(false, 0);
                    }
                }
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
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

    @SimpleFunction(description = "Executa código JavaScript na página atual.")
    public void EvaluateJavaScript(String script) {
        if (webView != null) {
            webView.evaluateJavascript(script, null);
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

    @SimpleFunction(description = "Para o carregamento da página.")
    public void StopLoading() {
        if (webView != null) {
            webView.stopLoading();
        }
    }

    @SimpleFunction(description = "Limpa o cache do WebViewer.")
    public void ClearCache() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    @SimpleProperty(description = "Define se o WebViewer deve se adaptar automaticamente ao teclado.")
    public void FollowKeyboard(boolean enabled) {
        this.followKeyboard = enabled;
    }

    @SimpleProperty
    public boolean FollowKeyboard() {
        return this.followKeyboard;
    }

    @SimpleProperty(description = "Retorna a URL atual.")
    public String CurrentUrl() {
        return webView != null ? webView.getUrl() : "";
    }

    @SimpleProperty(description = "Retorna o título da página atual.")
    public String CurrentTitle() {
        return webView != null ? webView.getTitle() : "";
    }

    @SimpleEvent(description = "Evento disparado quando o carregamento da página inicia.")
    public void PageStarted(String url) {
        EventDispatcher.dispatchEvent(this, "PageStarted", url);
    }

    @SimpleEvent(description = "Evento disparado quando o carregamento da página termina.")
    public void PageFinished(String url) {
        EventDispatcher.dispatchEvent(this, "PageFinished", url);
    }

    @SimpleEvent(description = "Evento disparado quando o progresso de carregamento muda.")
    public void ProgressChanged(int progress) {
        EventDispatcher.dispatchEvent(this, "ProgressChanged", progress);
    }

    @SimpleEvent(description = "Evento disparado quando ocorre um erro no carregamento.")
    public void ErrorOccurred(int errorCode, String description, String failingUrl) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", errorCode, description, failingUrl);
    }

    @SimpleEvent(description = "Evento disparado quando o status do teclado muda.")
    public void KeyboardStatusChanged(boolean visible, int height) {
        EventDispatcher.dispatchEvent(this, "KeyboardStatusChanged", visible, height);
    }
}