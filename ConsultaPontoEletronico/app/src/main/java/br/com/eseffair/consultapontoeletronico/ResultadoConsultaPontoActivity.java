package br.com.eseffair.consultapontoeletronico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ResultadoConsultaPontoActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_consulta_ponto);
        
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        
        WebView webview = (WebView) this.findViewById( R.id.webView2);
        webview.setWebViewClient(new MyWebViewClient());
        
        WebSettings wsettings = webview.getSettings();
        wsettings.setSavePassword( false );
        wsettings.setSaveFormData( true );
        wsettings.setSupportZoom( true );
        
        webview.loadUrl( url );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_resultado_consulta_ponto, menu);
        return true;
    }
    
}
