package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.app.Activity;

@DesignerComponent(
    version = 1,
    description = "Gerado pelo Lyra AIX Creator - Custom WebView with Keyboard Adaptation",
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
    private FrameLayout webContainer;
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener;
    private boolean isKeyboardVisible = false;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.activity = (Activity) container.$context();
    }

    @SimpleFunction(description = "Initializes the adaptive webview inside a layout component (e.g., VerticalArrangement).")
    public void InitializeWebView(AndroidViewComponent layout) {
        if (webView != null) {
            return;
        }

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
                PageFinished(url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ErrorOccurred(errorCode, description, failingUrl);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        ViewGroup viewGroup = (ViewGroup) layout.getView();
        viewGroup.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        setupKeyboardListener();
    }

    private void setupKeyboardListener() {
        final View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    if (!isKeyboardVisible) {
                        isKeyboardVisible = true;
                        KeyboardStateChanged(true, keypadHeight);
                        adjustWebViewHeight(r.bottom - getStatusBarHeight());
                    }
                } else {
                    if (isKeyboardVisible) {
                        isKeyboardVisible = false;
                        KeyboardStateChanged(false, 0);
                        resetWebViewHeight();
                    }
                }
            }
        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void adjustWebViewHeight(int newHeight) {
        if (webView != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = webView.getLayoutParams();
                    params.height = newHeight;
                    webView.setLayoutParams(params);
                    webView.requestLayout();
                }
            });
        }
    }

    private void resetWebViewHeight() {
        if (webView != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = webView.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    webView.setLayoutParams(params);
                    webView.requestLayout();
                }
            });
        }
    }

    @SimpleFunction(description = "Loads the specified URL.")
    public void GoToUrl(String url) {
        if (webView != null) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            webView.loadUrl(url);
        }
    }

    @SimpleFunction(description = "Loads raw HTML content.")
    public void LoadHtml(String html) {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }

    @SimpleFunction(description = "Evaluates JavaScript code in the current page.")
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

    @SimpleFunction(description = "Goes back in history.")
    public void GoBack() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }

    @SimpleFunction(description = "Goes forward in history.")
    public void GoForward() {
        if (webView != null && webView.canGoForward()) {
            webView.goForward();
        }
    }

    @SimpleFunction(description = "Reloads the current page.")
    public void Reload() {
        if (webView != null) {
            webView.reload();
        }
    }

    @SimpleFunction(description = "Stops the current loading process.")
    public void StopLoading() {
        if (webView != null) {
            webView.stopLoading();
        }
    }

    @SimpleFunction(description = "Clears the webview cache.")
    public void ClearCache() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    @SimpleProperty(description = "Returns true if the webview can go back.")
    public boolean CanGoBack() {
        return webView != null && webView.canGoBack();
    }

    @SimpleProperty(description = "Returns true if the webview can go forward.")
    public boolean CanGoForward() {
        return webView != null && webView.canGoForward();
    }

    @SimpleEvent(description = "Triggered when a page finishes loading.")
    public void PageFinished(String url) {
        EventDispatcher.dispatchEvent(this, "PageFinished", url);
    }

    @SimpleEvent(description = "Triggered when an error occurs during loading.")
    public void ErrorOccurred(int errorCode, String description, String failingUrl) {
        EventDispatcher.dispatchEvent(this, "ErrorOccurred", errorCode, description, failingUrl);
    }

    @SimpleEvent(description = "Triggered when the keyboard visibility changes.")
    public void KeyboardStateChanged(boolean isVisible, int height) {
        EventDispatcher.dispatchEvent(this, "KeyboardStateChanged", isVisible, height);
    }

    @SimpleFunction(description = "Hides the virtual keyboard manually.")
    public void HideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}