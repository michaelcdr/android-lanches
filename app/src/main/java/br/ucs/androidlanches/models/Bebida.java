package br.ucs.androidlanches.models;

import java.io.Serializable;

public class Bebida extends Produto implements Serializable
{
     private int embalagem;

     public int getEmbalagem() {
          return embalagem;
     }

     public void setEmbalagem(int embalagem) {
          this.embalagem = embalagem;
     }
}
