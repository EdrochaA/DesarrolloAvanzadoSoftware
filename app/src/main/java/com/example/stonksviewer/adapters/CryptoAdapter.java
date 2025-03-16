package com.example.stonksviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksviewer.R;
import com.example.stonksviewer.model.Crypto;

import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

    private List<Crypto> cryptoList;
    private Context context;

    public CryptoAdapter(Context context, List<Crypto> cryptoList) {
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

        // Asignar valores
        holder.tvCryptoName.setText(crypto.getSymbol());  // Siglas
        holder.tvCryptoPrice.setText("€" + String.format("%.2f", crypto.getPrice()));
        holder.tvCryptoChange.setText(String.format("%.2f", crypto.getChange24h()) + "%");

        // Asignar color al % de cambio
        if (crypto.getChange24h() >= 0) {
            holder.tvCryptoChange.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.tvCryptoChange.setTextColor(context.getResources().getColor(R.color.red));
        }

        // Asignar imagen de la cripto
        int imageResId = context.getResources().getIdentifier(crypto.getImageName(), "drawable", context.getPackageName());
        holder.ivCryptoIcon.setImageResource(imageResId);

    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCryptoIcon;
        TextView tvCryptoName, tvCryptoPrice, tvCryptoChange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCryptoIcon = itemView.findViewById(R.id.ivCryptoIcon);
            tvCryptoName = itemView.findViewById(R.id.tvCryptoName);
            tvCryptoPrice = itemView.findViewById(R.id.tvCryptoPrice);
            tvCryptoChange = itemView.findViewById(R.id.tvCryptoChange);
        }
    }
}