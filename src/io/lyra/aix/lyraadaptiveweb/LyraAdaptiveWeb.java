package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

import android.app.Activity;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

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

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$form();
    }

    @SimpleFunction(description = "Adapta a tela para que o teclado virtual não cubra o conteúdo da webviewer.")
    public void AdaptToKeyboard() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    );
                }
            });
        }
    }

    @SimpleFunction(description = "Aplica configurações avançadas ao WebViewer (DOM Storage, JavaScript, Mixed Content, etc).")
    public void SetupAdvancedFeatures(WebViewer webViewer) {
        if (webViewer != null) {
            WebView view = (WebView) webViewer.getView();
            if (view != null) {
                WebSettings settings = view.getSettings();
                settings.setDomStorageEnabled(true);
                settings.setDatabaseEnabled(true);
                settings.setJavaScriptEnabled(true);
                settings.setUseWideViewPort(true);
                settings.setLoadWithOverviewMode(true);
                
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                }
            }
        }
    }

    @SimpleFunction(description = "Inicializa o WebViewer com recursos avançados e adapta a tela ao teclado virtual em um único passo.")
    public void InitializeAdaptiveWeb(WebViewer webViewer) {
        AdaptToKeyboard();
        SetupAdvancedFeatures(webViewer);
    }

    @SimpleFunction(description = "Executa um script JavaScript no WebViewer de forma simplificada.")
    public void EvaluateJavaScript(WebViewer webViewer, final String script) {
        if (webViewer != null) {
            final WebView view = (WebView) webViewer.getView();
            if (view != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            view.evaluateJavascript(script, null);
                        } else {
                            view.loadUrl("javascript:" + script);
                        }
                    }
                });
            }
        }
    }
}