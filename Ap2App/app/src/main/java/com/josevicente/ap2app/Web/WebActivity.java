package com.josevicente.ap2app.Web;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.josevicente.ap2app.Activities.MenuActivity;
import com.josevicente.ap2app.Activities.StartActivity;
import com.josevicente.ap2app.EcoActivities.EcoActivity;
import com.josevicente.ap2app.GestActivities.GestMainActivity;
import com.josevicente.ap2app.MapsActivities.MapMainActivity;
import com.josevicente.ap2app.R;
import com.josevicente.ap2app.ShopActivities.ListMainActivity;

public class WebActivity extends AppCompatActivity {

    //Variables
    private WebView webView;
    private WebSettings webSettings;

    //Variables para Autenticar al Usuario
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        //Instanciamos las Variables
        webView = findViewById(R.id.webView1);
        mAuth = FirebaseAuth.getInstance();

        webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient()); //Habilitamos un ViewClient nuevo
        webView.loadUrl("http://www.antena3.com/noticias/"); //Añadimos la página web a mostrar
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Método que permite volver hacia atrás desde el webview sin que se cierre la aplicación
        WebView mWebView;
        mWebView = findViewById(R.id.webView1);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //Agregamos el menu3.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mUser = mAuth.getCurrentUser();
        switch (item.getItemId()){
            case R.id.itemLogout:
                if(mUser !=null && mAuth !=null){
                    mAuth.signOut(); //Logout por parte del usuario para salir de la aplicación
                    Intent intentStart = new Intent(WebActivity.this,StartActivity.class);
                    startActivity(intentStart); //Nos devuelve a la pantalla Principal de Inicio
                    Toast.makeText(WebActivity.this,"Has Salido de AP2APP",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case R.id.itemMain:
                    //Pasamos a MenuActivity
                    Intent intentMain = new Intent(WebActivity.this,MenuActivity.class);
                    startActivity(intentMain);
                break;

            case R.id.itemEco:
                    //Nos devuelve a la pantalla EcoActivity
                    Intent intentEco = new Intent(WebActivity.this,EcoActivity.class);
                    startActivity(intentEco);
                break;

            case R.id.itemMapa:
                    //Nos devuelve a la pantalla de MapMainActivity
                    Intent intentMap = new Intent(WebActivity.this, MapMainActivity.class);
                    startActivity(intentMap);
                break;

            case R.id.itemGest:
                    //Nos devuelve a la pantalla GestMainActivity
                    Intent intentGest = new Intent(WebActivity.this, GestMainActivity.class);
                    startActivity(intentGest);
                break;

            case R.id.itemLista:
                    //Nos devuelve a la pantalla ListMainActivity
                    Intent intentList = new Intent(WebActivity.this, ListMainActivity.class);
                    startActivity(intentList);
                break;

            case R.id.itemWeb:
                    //Estamos en la Opción Elegida
                    Toast.makeText(WebActivity.this,"Estás en la Opción Elegida",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
