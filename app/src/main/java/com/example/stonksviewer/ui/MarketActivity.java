package com.example.stonksviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksviewer.R;
import com.example.stonksviewer.adapters.MarketAdapter;
import com.example.stonksviewer.model.Crypto;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MarketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MarketAdapter adapter;
    private List<Crypto> cryptoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        // Configurar la barra de navegación inferior
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            }
            return false;
        });

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCryptos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar datos de criptomonedas
        loadCryptoData();
    }

    private void loadCryptoData() {
        cryptoList = new ArrayList<>();
        cryptoList.add(new Crypto("BTC", 82764.4, -0.01, "btc"));
        cryptoList.add(new Crypto("ETH", 1893.46, -0.72, "eth"));
        cryptoList.add(new Crypto("BNB", 312.54, 3.21, "bnb"));
        cryptoList.add(new Crypto("XRP", 0.52, -2.43, "xrp"));
        cryptoList.add(new Crypto("SOL", 146.32, 1.47, "sol"));
        cryptoList.add(new Crypto("AVAX", 27.83, -0.96, "avax"));
        cryptoList.add(new Crypto("DOGE", 0.12, 4.58, "doge"));
        cryptoList.add(new Crypto("ADA", 0.41, -1.62, "cardano"));

        // Configurar adaptador
        adapter = new MarketAdapter(this, cryptoList);
        recyclerView.setAdapter(adapter);
    }
}
