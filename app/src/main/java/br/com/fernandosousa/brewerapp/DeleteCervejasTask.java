package br.com.fernandosousa.brewerapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fsousa on 14/10/2017.
 */

public class DeleteCervejasTask extends AsyncTask<Integer, Void, ReturnSucesso> {


    private Context contexto;

    public DeleteCervejasTask(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    protected ReturnSucesso doInBackground(Integer... params) {
        //android.os.Debug.waitForDebugger();

        String url = "http://localhost/cerveja/remover/"+params[0]+"/"+params[1];
        HttpHelper http = new HttpHelper();
        ReturnSucesso sucesso = null;
        try {
            String json = http.doGet(url);
            Log.d("Servico", json);
            sucesso = this.parseJSON(json);
        }
        catch(IOException ex) {
            //Toast.makeText(contexto, "Erro ao chamar o serviço: "+ ex.toString(), Toast.LENGTH_LONG).show();
            Log.d("Servico", "Erro chamada ao servico");
        }
        //ReturnSucesso sucesso = new ReturnSucesso();
        Log.d("Servico", "Fim Chamada");
        return sucesso;
    }

    protected  void onPostExecute(ReturnSucesso retorno) {
        Log.d("Servico", ""+retorno.mensagem);
        //android.os.Debug.waitForDebugger();
        //Toast.makeText(contexto, "Retorno WS POST: "+ retorno.toString(), Toast.LENGTH_LONG).show();
        //ListView lista = (ListView) ((Activity)contexto).findViewById(R.id.listaElementos);
        //lista.setAdapter(new CervejasAdapter(contexto,retorno ));
    }

    // transformar json na lista de Cervejas
    private ReturnSucesso parseJSON(String json) {
        //android.os.Debug.waitForDebugger();
        ReturnSucesso retorno = new ReturnSucesso();

        try {

            JSONObject jsonRetorno = new JSONObject(json);
            retorno.sucesso = jsonRetorno.getInt("sucesso");
            if (jsonRetorno.has(" mensagem")){
                retorno.mensagem = jsonRetorno.getString("mensagem");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno;
    }



}
