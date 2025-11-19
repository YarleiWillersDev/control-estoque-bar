package br.com.barghesla.barestoque;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//CRIAR HASH DE SENHA BCRYPT PARA INSERT DE USUARIOS MANUAIS
public class BCryptHashGen {
  public static void main(String[] args) {
    var encoder = new BCryptPasswordEncoder();          // ajuste o strength se usa outro
    String raw = "@antena2000ACG";                      // sua senha em texto
    String hash = encoder.encode(raw);
    System.out.println(hash);                           // copie e cole na migration
  }
}

