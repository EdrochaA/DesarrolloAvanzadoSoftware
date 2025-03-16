package com.example.stonksviewer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksviewer.R;
import com.example.stonksviewer.model.Crypto;
import com.example.stonksviewer.ui.CryptoChartActivity;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {

    private List<Crypto> cryptoList;
    private Context context;

    public MarketAdapter(Context context, List<Crypto> cryptoList) {
        this.context = context;
        this.cryptoList = cryptoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crypto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Crypto crypto = cryptoList.get(position);

        holder.tvCryptoName.setText(crypto.getSymbol());
        holder.tvCryptoPrice.setText("€" + String.format("%.2f", crypto.getPrice()));
        holder.tvCryptoChange.setText(String.format("%.2f", crypto.getChange24h()) + "%");

        if (crypto.getChange24h() >= 0) {
            holder.tvCryptoChange.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.tvCryptoChange.setTextColor(context.getResources().getColor(R.color.red));
        }

        int imageResId = context.getResources().getIdentifier(crypto.getImageName(), "drawable", context.getPackageName());
        holder.ivCryptoIcon.setImageResource(imageResId);

        // Evento al hacer click en la criptomoneda
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CryptoChartActivity.class);
            intent.putExtra("CRYPTO_SYMBOL", crypto.getSymbol());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    // ViewHolder interno para MarketAdapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCryptoName, tvCryptoPrice, tvCryptoChange;
        ImageView ivCryptoIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCryptoName = itemView.findViewById(R.id.tvCryptoName);
            tvCryptoPrice = itemView.findViewById(R.id.tvCryptoPrice);
            tvCryptoChange = itemView.findViewById(R.id.tvCryptoChange);
            ivCryptoIcon = itemView.findViewById(R.id.ivCryptoIcon);
        }
    }
}
