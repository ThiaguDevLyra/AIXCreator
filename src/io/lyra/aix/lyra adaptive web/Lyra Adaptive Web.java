package io.lyra.aix.lyraadaptiveweb;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.WebViewer;

@DesignerComponent(version = 1,
        description = "Um custom webviewer, com recursos avançados simplificados, que se adapta ao teclado virtual do dispositivo, não permitindo que o teclado cubra o conteúdo da webviewer.",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
public class LyraAdaptiveWeb extends AndroidNonvisibleComponent {

    private Activity activity;
    private boolean isKeyboardVisible = false;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardListener;

    public LyraAdaptiveWeb(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
    }

    @SimpleFunction(description = "Adapta a tela para evitar que o teclado virtual cubra o WebViewer (Ativa o modo Resize da Janela).")
    public void EnableKeyboardResize() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
    }

    @SimpleFunction(description = "Desativa a adaptação do teclado (Retorna ao modo Pan padrão).")
    public void DisableKeyboardResize() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
        });
    }

    @SimpleFunction(description = "Aplica configurações avançadas a um componente WebViewer existente (DOM Storage, Database, Viewport, etc).")
    public void EnhanceWebViewer(final WebViewer webViewer) {
        final WebView view = (WebView) webViewer.getView();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebSettings settings = view.getSettings();
                settings.setDomStorageEnabled(true);
                settings.setDatabaseEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setJavaScriptEnabled(true);
                settings.setLoadWithOverviewMode(true);
                settings.setUseWideViewPort(true);
            }
        });
    }

    @SimpleFunction(description = "Avalia e executa um código JavaScript no WebViewer especificado.")
    public void EvaluateJavaScript(final WebViewer webViewer, final String script) {
        final WebView view = (WebView) webViewer.getView();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.evaluateJavascript(script, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        JavaScriptEvaluated(value);
                    }
                });
            }
        });
    }

    @SimpleFunction(description = "Limpa o cache do WebViewer especificado.")
    public void ClearCache(final WebViewer webViewer) {
        final WebView view = (WebView) webViewer.getView();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.clearCache(true);
            }
        });
    }

    @SimpleFunction(description = "Inicia o monitoramento do estado do teclado virtual para disparar eventos.")
    public void StartKeyboardListener() {
        final View rootView = activity.findViewById(android.R.id.content);
        if (keyboardListener == null) {
            keyboardListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();
                    rootView.getWindowVisibleDisplayFrame(r);
                    int screenHeight = rootView.getRootView().getHeight();
                    int keypadHeight = screenHeight - r.bottom;

                    boolean isVisible = keypadHeight > screenHeight * 0.15;
                    if (isVisible != isKeyboardVisible) {
                        isKeyboardVisible = isVisible;
                        KeyboardStateChanged(isVisible, keypadHeight);
                    }
                }
            };
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardListener);
        }
    }

    @SimpleFunction(description = "Para o monitoramento do estado do teclado virtual.")
    public void StopKeyboardListener() {
        if (keyboardListener != null) {
            final View rootView = activity.findViewById(android.R.id.content);
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardListener);
            keyboardListener = null;
        }
    }

    @SimpleEvent(description = "Disparado quando a avaliação do JavaScript é concluída com um resultado.")
    public void JavaScriptEvaluated(String result) {
        EventDispatcher.dispatchEvent(this, "JavaScriptEvaluated", result);
    }

    @SimpleEvent(description = "Disparado quando o estado do teclado virtual muda (Aberto/Fechado).")
    public void KeyboardStateChanged(boolean isVisible, int keyboardHeight) {
        EventDispatcher.dispatchEvent(this, "KeyboardStateChanged", isVisible, keyboardHeight);
    }
}