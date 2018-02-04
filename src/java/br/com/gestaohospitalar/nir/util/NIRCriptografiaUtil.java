/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gestaohospitalar.nir.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author daniel
 */
public class NIRCriptografiaUtil {

    public static String md5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(senha.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();

            for (byte b : messageDigest) {
                sb.append(String.format("%02x", 0xFF & b));
            }

            senha = sb.toString();

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            System.out.println("Problemas ao criptografar com md5. Erro: " + e.getMessage());
        }

        return senha;
    }

    public static String sha256(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(senha.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();

        for (byte b : messageDigest) {
            sb.append(String.format("%02X", 0xFF & b));

        }

        String senhaHex = sb.toString();

        return senhaHex;
    }

}
