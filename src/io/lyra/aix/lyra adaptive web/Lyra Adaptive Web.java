package io.lyra.aix.lyraadaptiveweb;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@DesignerComponent(
    version = 1,
    description = "Extensão para: Lyra Adaptive Web. Um custom webviewer, com recursos avançados simplificados, que se adapta ao teclado virtual do dispositivo, não permitindo que o teclado cubra o conteúdo da webviewer.",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private Activity activity;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
    }

    @SimpleFunction(description = "Ativa a adaptação da tela para que o teclado virtual não cubra o conteúdo do WebViewer (Ajusta o redimensionamento da janela).")
    public void EnableKeyboardAdaptation() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            });
        }
    }

    @SimpleFunction(description = "Desativa a adaptação da tela para o teclado virtual, retornando ao comportamento padrão.")
    public void DisableKeyboardAdaptation() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
                }
            });
        }
    }

    @SimpleFunction(description = "Aplica configurações avançadas a um componente WebViewer existente (ex: DOM Storage, Database, ViewPort, etc). Passe o componente WebViewer como parâmetro.")
    public void ApplyAdvancedSettings(AndroidViewComponent webViewer) {
        if (webViewer != null) {
            View view = webViewer.getView();
            if (view instanceof WebView) {
                final WebView webView = (WebView) view;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebSettings settings = webView.getSettings();
                        
                        // Recursos avançados simplificados
                        settings.setDomStorageEnabled(true);
                        settings.setDatabaseEnabled(true);
                        settings.setAllowFileAccess(true);
                        settings.setJavaScriptEnabled(true);
                        settings.setLoadWithOverviewMode(true);
                        settings.setUseWideViewPort(true);
                        settings.setBuiltInZoomControls(true);
                        settings.setDisplayZoomControls(false);
                        
                        // Permite conteúdo misto (HTTP em HTTPS) em versões mais recentes do Android
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                        }
                    }
                });
            }
        }
    }
    
    @SimpleFunction(description = "Limpa o cache do WebViewer especificado.")
    public void ClearWebViewCache(AndroidViewComponent webViewer) {
        if (webViewer != null) {
            View view = webViewer.getView();
            if (view instanceof WebView) {
                final WebView webView = (WebView) view;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.clearCache(true);
                    }
                });
            }
        }
    }
}