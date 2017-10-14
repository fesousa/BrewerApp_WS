package br.com.fernandosousa.brewerapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fsousa on 10/09/2017.
 */

public class Prefs {
    public static final String PREF_ID = "brewerapp";

    public static void setBoolean(Context context, String chave, boolean valor) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(chave, valor);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String chave) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        boolean b = pref.getBoolean(chave, false);
        return b;
    }

    public static void setString(Context context, String chave, String valor) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(chave, valor);
        editor.commit();
    }

    public static String getString(Context context, String chave) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        String s = pref.getString(chave, "");
        return s;
    }
}
