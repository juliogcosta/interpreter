package com.yc.utils;

public class PasswordUtils
{
   public boolean strongVerification(String password)
   {
      boolean temMinuscula = false;
      boolean temMaiuscula = false;
      boolean temNumero = false;
//      boolean caracteresRepetidos = false;
      
      // Verifica se a string possui pelo menos 6 caracteres
      if (password.length() < 6)
      {
         return false;
      }
      
      // Verifica se a string possui pelo menos 1 letra minúscula
      for (char caracter : password.toCharArray())
      {
         if (Character.isLowerCase(caracter))
         {
            temMinuscula = true;
            break;
         }
      }
      
      // Verifica se a string possui pelo menos 1 letra maiúscula
      for (char caracter : password.toCharArray())
      {
         if (Character.isUpperCase(caracter))
         {
            temMaiuscula = true;
            break;
         }
      }
      
      // Verifica se a string possui pelo menos 1 número
      for (char caracter : password.toCharArray())
      {
         if (Character.isDigit(caracter))
         {
            temNumero = true;
            break;
         }
      }
      
      // Verifica se 5 dos caracteres não se repetem
      //Set<Character> caracteresUnicos = new HashSet<>();
      //for (char caracter : password.toCharArray())
      {
         //if (!caracteresUnicos.add(caracter))
         {
            //caracteresRepetidos = true;
            //break;
         }
      }
      
      // Retorna true se todas as condições forem satisfeitas
      return temMinuscula && temMaiuscula && temNumero;// && !caracteresRepetidos;
   }
}
