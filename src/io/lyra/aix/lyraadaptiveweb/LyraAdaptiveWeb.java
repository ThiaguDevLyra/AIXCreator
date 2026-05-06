package io.lyra.aix.lyraadaptiveweb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.view.View;

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

    private Activity activity;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
    }

    @SimpleFunction(description = "Configura o WebViewer com recursos avançados e adapta a tela para o teclado não cobrir o conteúdo.")
    public void SetupAdaptiveWebViewer(WebViewer webViewer) {
        EnableKeyboardAdaptation();
        EnhanceWebViewer(webViewer);
        OnSetupCompleted(webViewer);
    }

    @SimpleFunction(description = "Adapta a tela do aplicativo para que o teclado virtual não cubra o conteúdo (Adjust Resize).")
    public void EnableKeyboardAdaptation() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Window window = activity.getWindow();
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        OnError("EnableKeyboardAdaptation", e.getMessage());
                    }
                }
            });
        }
    }

    @SimpleFunction(description = "Desativa a adaptação do teclado virtual, retornando ao comportamento padrão (Adjust Pan).")
    public void DisableKeyboardAdaptation() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Window window = activity.getWindow();
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    } catch (Exception e) {
                        e.printStackTrace();
                        OnError("DisableKeyboardAdaptation", e.getMessage());
                    }
                }
            });
        }
    }

    @SimpleFunction(description = "Aplica configurações avançadas ao WebViewer (DOM Storage, JavaScript, ViewPort, etc).")
    public void EnhanceWebViewer(WebViewer webViewer) {
        if (webViewer != null) {
            try {
                View view = webViewer.getView();
                if (view instanceof WebView) {
                    WebView webView = (WebView) view;
                    WebSettings settings = webView.getSettings();
                    
                    settings.setDomStorageEnabled(true);
                    settings.setJavaScriptEnabled(true);
                    settings.setLoadWithOverviewMode(true);
                    settings.setUseWideViewPort(true);
                    settings.setBuiltInZoomControls(true);
                    settings.setDisplayZoomControls(false);
                    settings.setAllowFileAccess(true);
                    settings.setAllowContentAccess(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                OnError("EnhanceWebViewer", e.getMessage());
            }
        }
    }

    @SimpleEvent(description = "Disparado quando a configuração do WebViewer adaptativo é concluída.")
    public void OnSetupCompleted(WebViewer webViewer) {
        EventDispatcher.dispatchEvent(this, "OnSetupCompleted", webViewer);
    }

    @SimpleEvent(description = "Disparado quando ocorre um erro interno na extensão.")
    public void OnError(String functionName, String errorMessage) {
        EventDispatcher.dispatchEvent(this, "OnError", functionName, errorMessage);
    }
}