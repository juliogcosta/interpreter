package br.com.comigo.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    public String generateHash(String input, Integer length) {
       try {
          MessageDigest digest = MessageDigest.getInstance("SHA-512");
          byte[] hashBytes = digest.digest(input.getBytes());
          StringBuilder hexStringBuilder = new StringBuilder();
          for (byte b : hashBytes) {
             hexStringBuilder.append(String.format("%02x", b));
          }
          String hexString = hexStringBuilder.toString();
          return hexString.substring(0, length);
       } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
          return null;
       }
    }
}
