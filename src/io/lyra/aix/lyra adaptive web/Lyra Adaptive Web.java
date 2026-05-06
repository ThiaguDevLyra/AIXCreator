package io.lyra.aix.lyraadaptiveweb;

import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.WebViewer;

@DesignerComponent(
    version = 1,
    description = "Um custom webviewer, com recursos avançados simplificados, que se adapta ao teclado virtual do dispositivo, não permitindo que o teclado cubra o conteúdo da webviewer.",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png"
)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private Form form;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.form = container.$form();
    }

    @SimpleFunction(description = "Ativa a adaptação da tela para o teclado virtual, redimensionando a janela para evitar que o teclado cubra o conteúdo do WebViewer.")
    public void EnableKeyboardAdaptation() {
        try {
            form.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    form.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SimpleFunction(description = "Desativa a adaptação da tela para o teclado virtual, retornando ao comportamento padrão do sistema.")
    public void DisableKeyboardAdaptation() {
        try {
            form.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    form.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SimpleFunction(description = "Aplica configurações avançadas a um componente WebViewer existente (ex: DOM Storage, ViewPort, reprodução de mídia, etc).")
    public void EnhanceWebViewer(WebViewer webViewer) {
        if (webViewer != null) {
            try {
                View view = webViewer.getView();
                if (view instanceof WebView) {
                    WebView wv = (WebView) view;
                    WebSettings settings = wv.getSettings();
                    
                    // Configurações avançadas para melhorar a experiência web
                    settings.setDomStorageEnabled(true);
                    settings.setDatabaseEnabled(true);
                    settings.setAllowFileAccess(true);
                    settings.setAllowContentAccess(true);
                    settings.setUseWideViewPort(true);
                    settings.setLoadWithOverviewMode(true);
                    settings.setJavaScriptCanOpenWindowsAutomatically(true);
                    settings.setMediaPlaybackRequiresUserGesture(false);
                    settings.setSupportZoom(true);
                    settings.setBuiltInZoomControls(true);
                    settings.setDisplayZoomControls(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}