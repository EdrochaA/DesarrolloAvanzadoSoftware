package com.example.stonksviewer.model;

public class Crypto {
    private String symbol; // Siglas de la criptomoneda (BTC, ETH, etc.)
    private double price;  // Precio en euros
    private double change24h; // Variación en las últimas 24h
    private String imageName; // Nombre del archivo de imagen en drawable

    public Crypto(String symbol, double price, double change24h, String imageName) {
        this.symbol = symbol;
        this.price = price;
        this.change24h = change24h;
        this.imageName = imageName;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getChange24h() {
        return change24h;
    }

    public String getImageName() {
        return imageName;
    }
}