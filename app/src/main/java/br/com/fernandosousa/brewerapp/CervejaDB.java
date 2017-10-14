package br.com.fernandosousa.brewerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fsousa on 24/09/2017.
 */

public class CervejaDB extends SQLiteOpenHelper {

    // Nome do BD
    public static final String NOME_BANCO = "brewerapp.sqlite";
    // Versao do BD
    public static final int VERSAO_BANCO = 2;

    public CervejaDB(Context context){
         super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists cerveja (" +
                "_id integer primary key autoincrement, " +
                "nome text, imagem text, tipo text, " +
                "pais text, endereco text, " +
                "preco real, favorita integer, " +
                "origem integer, brilho integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {
        // método para executar quando a versão do BD for alterasa
        if (versaoAntiga == 1 && versaoNova == 2) {
            db.execSQL("alter table cerveja add column latitude text;");
            db.execSQL("alter table cerveja add column longitude text;");
        }
    }

    // inserir uma nova Cerveja ou atualizar existente
    public long save(Cerveja cerveja){
        long id = cerveja.id;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nome", cerveja.nome);
            values.put("imagem", cerveja.imagem);
            values.put("tipo", cerveja.tipo);
            values.put("pais", cerveja.pais);
            values.put("endereco", cerveja.endereco);
            values.put("latitude", cerveja.latitude);
            values.put("longitude", cerveja.longitude);
            values.put("preco", cerveja.preco);
            values.put("favorita", cerveja.favorita);
            values.put("origem", cerveja.origem);
            values.put("brilho", cerveja.brilho);
            if (id != 0) {
                String _id = String.valueOf(id);
                String [] argsFiltro = new String[]{_id};
                // criar update cerveja set values = ... where _id = ?
                int count = db.update("cerveja", values, "_id=?", argsFiltro);
                return count;
            } else {
                // criar insert into cerveja values (...)
                id = db.insert("cerveja","", values);
                return id;
            }
        } finally {
            db.close();
        }
    }

    // remover cerveja
    public int delete(Cerveja cerveja){
        SQLiteDatabase db = getWritableDatabase();
        try{
            // criar delete from cerveja where _id = ?
            int count = db.delete("cerveja", "_id=?", new String[]{String.valueOf(cerveja.id)});
            return count;
        } finally {
            db.close();
        }
    }

    // retornar todas as cervejas
    public List<Cerveja> findAll() {
        SQLiteDatabase db =  getWritableDatabase();
        try {
            // criar select * from cerveja
            Cursor c = db.query("cerveja", null, null, null, null, null, null, null);
            return  toList(c);
        } finally {
            db.close();
        }
    }

    // lê o cursor e cria a lista de cervejas
    private List<Cerveja> toList(Cursor c) {
        List<Cerveja> cervejas = new ArrayList<Cerveja>();
        if (c.moveToFirst()) {
            do {
                Cerveja cerveja = new Cerveja();
                cervejas.add(cerveja);
                cerveja.id = c.getLong(c.getColumnIndex("_id"));
                cerveja.nome = c.getString(c.getColumnIndex("nome"));
                cerveja.imagem = c.getString(c.getColumnIndex("imagem"));
                cerveja.tipo = c.getString(c.getColumnIndex("tipo"));
                cerveja.pais = c.getString(c.getColumnIndex("pais"));
                cerveja.endereco = c.getString(c.getColumnIndex("endereco"));
                cerveja.latitude = c.getString(c.getColumnIndex("latitude"));
                cerveja.longitude = c.getString(c.getColumnIndex("longitude"));
                cerveja.preco = c.getDouble(c.getColumnIndex("preco"));
                cerveja.favorita = c.getInt(c.getColumnIndex("favorita"));
                cerveja.origem = c.getInt(c.getColumnIndex("origem"));
                cerveja.brilho = c.getInt(c.getColumnIndex("brilho"));

            } while (c.moveToNext());
        }
        return cervejas;
    }

    // executa um SQL qualquer
    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        }finally {
            db.close();
        }
    }

    // executa um SQL qualquer, com parâmetros
    public void execSQL(String sql, Object [] args) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql, args);
        }finally {
            db.close();
        }
    }
}
