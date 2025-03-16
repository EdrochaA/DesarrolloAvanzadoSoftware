package com.example.stonksviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stonksviewer.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CryptoChartActivity extends AppCompatActivity {

    private WebView webViewChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_chart);

        webViewChart = findViewById(R.id.webViewChart);
        setupWebView();

        // Recuperar el símbolo de la criptomoneda y cargar el gráfico
        String cryptoSymbol = getIntent().getStringExtra("CRYPTO_SYMBOL");
        if (cryptoSymbol != null) {
            loadChart(cryptoSymbol);
        }

        // Configurar la barra de navegación inferior
        setupBottomNavigation();
    }

    private void setupWebView() {
        WebSettings webSettings = webViewChart.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webViewChart.setWebViewClient(new WebViewClient());
    }

    private void loadChart(String cryptoSymbol) {
        String url = "https://www.tradingview.com/chart/?symbol=BINANCE:" + cryptoSymbol + "USDT";
        webViewChart.loadUrl(url);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_graphs) {
                startActivity(new Intent(this, MarketActivity.class));
                return true;
            }
            return false;
        });
    }
}
