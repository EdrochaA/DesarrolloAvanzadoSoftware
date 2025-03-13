package com.example.stonksviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksviewer.R;
import com.example.stonksviewer.model.Crypto;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

    private List<Crypto> cryptoList;
    private Context context;

    public MarketAdapter(Context context, List<Crypto> cryptoList) {
        this.context = context;
        this.cryptoList = cryptoList;
    }

    @NonNull
    @Override
    public CryptoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crypto, parent, false);
        return new CryptoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoAdapter.ViewHolder holder, int position) {
        Crypto crypto = cryptoList.get(position);

        // Asignar valores
        holder.tvCryptoName.setText(crypto.getSymbol());
        holder.tvCryptoPrice.setText("€" + String.format("%.2f", crypto.getPrice()));
        holder.tvCryptoChange.setText(String.format("%.2f", crypto.getChange24h()) + "%");

        // Asignar color al porcentaje de cambio
        if (crypto.getChange24h() >= 0) {
            holder.tvCryptoChange.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.tvCryptoChange.setTextColor(context.getResources().getColor(R.color.red));
        }

        // Cargar imagen desde drawable
        int imageResId = context.getResources().getIdentifier(crypto.getImageName(), "drawable", context.getPackageName());
        holder.ivCryptoIcon.setImageResource(imageResId);

        // Estrella favorita (no implementado aún)
        holder.ivFavorite.setImageResource(R.drawable.ic_estrella);
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }
}
