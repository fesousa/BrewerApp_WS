package br.com.fernandosousa.brewerapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fsousa on 09/09/2017.
 */

public class Cerveja implements Serializable{
    public long id;
    public String nome;
    public String imagem;
    public String tipo;
    public String pais;
    public String endereco;
    public String latitude;
    public String longitude;
    public double preco;
    public int favorita;
    public int origem;
    public int brilho;


    public Cerveja() {

    }


}
