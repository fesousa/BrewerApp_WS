package br.com.fernandosousa.brewerapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fsousa on 14/10/2017.
 */

public class GetCervejasTask extends AsyncTask<Void, Void, List<Cerveja>> {


//    public interface RespostaGetCerveja {
//        void finalizaGet(String resposta);
//    }
//
//    public RespostaGetCerveja respostaGet = null;

//    public GetCervejasTask(RespostaGetCerveja respostaGet){
//        this.respostaGet = respostaGet;
//    }

    private Context contexto;

    public GetCervejasTask(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    protected List<Cerveja> doInBackground(Void... voids) {
        String url = "http://localhost/cerveja/listar";
        HttpHelper http = new HttpHelper();
        List<Cerveja> cervejas = null;
        try {
            String json = http.doGet(url);
            Log.d("Servico", json);
            cervejas = this.parseJSON(json);
        }
        catch(IOException ex) { }

        return cervejas;
    }

    protected  void onPostExecute(List<Cerveja> retorno) {
        //Toast.makeText(contexto, "Retorno WS", Toast.LENGTH_LONG).show();
        ListView lista = (ListView) ((Activity)contexto).findViewById(R.id.listaElementos);
        lista.setAdapter(new CervejasAdapter(contexto,retorno ));

    }

    // transformar json na lista de Cervejas
    private List<Cerveja> parseJSON(String json) {
        List<Cerveja> cervejas = new ArrayList<Cerveja>();

        try {

            JSONArray jsonCervejas = new JSONObject(json).optJSONArray("cervejas");
            for(int i = 0; i < jsonCervejas.length(); i++) {
                JSONObject jsonCerveja = jsonCervejas.getJSONObject(i);
                Cerveja c = new Cerveja();

                c.id = jsonCerveja.optLong("id");
                Log.d("Servico",Long.toString(c.id));
                c.nome = jsonCerveja.optString("nome");
                c.imagem = jsonCerveja.optString("imagem");
                c.tipo = jsonCerveja.optString("tipo");
                c.pais = jsonCerveja.optString("pais");
                c.endereco = jsonCerveja.optString("endereco");
                c.latitude = jsonCerveja.optString("latitude");
                c.longitude = jsonCerveja.optString("longitude");
                c.preco = jsonCerveja.optDouble("preco");
                c.favorita = jsonCerveja.optInt("favorita");
                c.origem = jsonCerveja.optInt("origem");
                c.brilho = jsonCerveja.optInt("brilho");

                cervejas.add(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cervejas;
    }



}
