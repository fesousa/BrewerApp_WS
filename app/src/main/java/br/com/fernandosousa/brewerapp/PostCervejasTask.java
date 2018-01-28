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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fsousa on 14/10/2017.
 */

public class PostCervejasTask extends AsyncTask<Cerveja, Void, ReturnSucesso> {


    private Context contexto;

    public PostCervejasTask(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    protected ReturnSucesso doInBackground(Cerveja... cerveja) {
        //android.os.Debug.waitForDebugger();
        Log.d("Servico", "Chamada ao servico" + cerveja[0].id);
        String url = cerveja[0].id == 0? "http://fernandosousa.com.br/mobile/app/cerveja/adicionar":"http://fernandosousa.com.br/mobile/app/cerveja/alterar";
        HttpHelper http = new HttpHelper();
        ReturnSucesso sucesso = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", Long.toString(cerveja[0].id));
            params.put("nome", cerveja[0].nome);
            params.put("preco", Double.toString(cerveja[0].preco));
            params.put("imagem", cerveja[0].imagem);
            params.put("tipo", cerveja[0].tipo);
            params.put("pais", cerveja[0].pais);
            params.put("endereco", cerveja[0].endereco);
            params.put("favorita", Integer.toString(cerveja[0].favorita));
            params.put("origem", Integer.toString(cerveja[0].origem));
            params.put("brilho", Integer.toString(cerveja[0].brilho));
            params.put("ra", Integer.toString(cerveja[0].ra));

            String json = http.doPost(url, params, "UTF8");
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
