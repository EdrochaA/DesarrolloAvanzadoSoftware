package com.example.stonksviewer.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionHelper {

    // Método para encriptar contraseñas con SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para verificar si la contraseña ingresada coincide con la almacenada
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        String hashedInput = hashPassword(plainPassword); // Hashear la contraseña ingresada
        return hashedInput != null && hashedInput.equals(hashedPassword);
    }
}
