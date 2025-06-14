package com.yc.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCreator
{
   private static final String ALGORITHM = "SHA-512";
   
   public String createMD5Hash(final String input)
      throws NoSuchAlgorithmException
   {
      byte[] messageDigest = MessageDigest.getInstance("MD5")
         .digest(input.getBytes());
      
      return convertToHex(messageDigest);
   }
   
   private String convertToHex(final byte[] messageDigest)
   {
      BigInteger bigint = new BigInteger(1, messageDigest);
      String hexText = bigint.toString(16);
      while (hexText.length() < 32)
      {
         hexText = "0".concat(hexText);
      }
      
      return hexText;
   }
   
   public String generateHash(String input, Integer length)
   {
      try
      {
         MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
         byte[] hashBytes = digest.digest(input.getBytes());
         StringBuilder hexStringBuilder = new StringBuilder();
         for (byte b : hashBytes)
         {
            hexStringBuilder.append(String.format("%02x", b));
         }
         String hexString = hexStringBuilder.toString();
         return hexString.substring(0, length);
      }
      catch (NoSuchAlgorithmException e)
      {
         e.printStackTrace();
         return null;
      }
   }
}
