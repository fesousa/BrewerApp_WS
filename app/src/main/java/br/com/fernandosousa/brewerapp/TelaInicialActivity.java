package br.com.fernandosousa.brewerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TelaInicialActivity extends DebugActivity {

    private List<Cerveja> cervejas = new ArrayList<Cerveja>();
    private ListView lista ;
    public static final int  RETORNO_CERVEJA_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define qual será a View da Activity
        setContentView(R.layout.activity_tela_inicial);


        // Recupera a intent e os dados enviados na chamda da Activity
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if(params != null && params.containsKey("nome")) {
            String nome = params.getString("nome");
            // Mostra o nome do usuário enviado no log e no Toast
            Log.d(DEBUG_TAG, "Nome do usuário: " + nome);
            //Toast.makeText(TelaInicialActivity.this, "Nome do usuário: " + nome, Toast.LENGTH_LONG).show();

        }

        //Alterar texto da ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Início");

        // tela com a listView
        setContentView(R.layout.activity_tela_inicial);

        lista = (ListView)findViewById(R.id.listaElementos);

        // Criar objeto de CervejaDB
        //CervejaDB cervejaDB = new CervejaDB(TelaInicialActivity.this);
        // Procurar cervejas e armazenar na
        // variavel de classe cervejas
        //cervejas = cervejaDB.findAll();

        // chamar um WS com a lista de Cervejas - não funciona se estiver na Thread Principal
//        String url = "http://fernandosousa.com.br/mobile/app/cerveja/listar";
//        HttpHelper http = new HttpHelper();
//        String json = "";
//        try {
//            json = http.doGet(url);
//        }
//        catch(IOException ex) { }
//        Toast.makeText(TelaInicialActivity.this, json, Toast.LENGTH_LONG);

        // chamar um WS com a lista de Cervejas utilizando AsyncTask
        new GetCervejasTask(TelaInicialActivity.this).execute();

        // Adapater de cervejas
        lista.setAdapter(new CervejasAdapter(TelaInicialActivity.this,cervejas ));

        lista.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                // recuperar lista de cervejas da listview
                List<Cerveja> listaFiltrada = ((CervejasAdapter) adapterView.getAdapter()).getList();
                Intent cervejaIntent = new Intent(TelaInicialActivity.this, CervejaActivity.class);
                cervejaIntent.putExtra("cerveja", listaFiltrada.get(index));
                // constante RETORNO_CERVEJA_ACTIVITY == 2
                startActivityForResult(cervejaIntent, RETORNO_CERVEJA_ACTIVITY);
            }
        });
    }

    // Tratamento do evento de clique no botao de sair
    public View.OnClickListener clickSair() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "Saída do BrewerApp");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        };
    }

    @Override
    // Método para colocar o menu na ActionBar
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflar o menu na view
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Recuperar SearchView
        MenuItem item = menu.findItem(R.id.action_buscar);
        SearchView searchView = (SearchView) item.getActionView();
        // Listener que espera a ação de buscar
        searchView.setOnQueryTextListener(onSearch());

        // Recuperar  botão de compartilhar
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider share = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //Listener que espera a ação de compartilhar
        share.setShareIntent(getDefautIntent());
        return true;
    }

    // Tratar os eventos dos botões do menu
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_atualizar) {
            Toast.makeText(TelaInicialActivity.this, "Atualizar",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_buscar) {
            Toast.makeText(TelaInicialActivity.this, "Buscar", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_adicionar) {
            Toast.makeText(TelaInicialActivity.this, "Buscar", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(TelaInicialActivity.this, CadastroActivity.class);
            startActivityForResult(it, 1);
        } else if (id == R.id.action_config) {
            Toast.makeText(TelaInicialActivity.this, "Config", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TelaInicialActivity.this, ConfiguracoesActivity.class));
        } else if (id == R.id.action_share) {
            Toast.makeText(TelaInicialActivity.this, "Compartilhar", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    // Recuperar resultado de CadastroActivity após ela ser finalizada
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 || requestCode == RETORNO_CERVEJA_ACTIVITY) {
            // para trabalhar com BD
//            CervejaDB cervejaDB = new CervejaDB(TelaInicialActivity.this);
//            cervejas = cervejaDB.findAll();
//            lista.setAdapter(new CervejasAdapter(TelaInicialActivity.this,cervejas ));
            new GetCervejasTask(TelaInicialActivity.this).execute();
            // para trabalhar com WS
        }
        if (requestCode == RETORNO_CERVEJA_ACTIVITY && resultCode == Activity.RESULT_OK) {
            String mensagem = data.getStringExtra("msg");
            //Toast.makeText(TelaInicialActivity.this, mensagem, Toast.LENGTH_SHORT).show();
        }

    }

    // Funcao para retornar o tratamento do evento no SearchView
    private SearchView.OnQueryTextListener onSearch() {

        return new SearchView.OnQueryTextListener() {
            @Override
            // Tratamento do evento quando termina de escrever
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                //Toast.makeText(TelaInicialActivity.this, query, Toast.LENGTH_SHORT).show();
                buscaCervejas(query);
                return false;
            }

            @Override
            // Tratamento do evento enquanto ainda está escrevendo
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(TelaInicialActivity.this, newText, Toast.LENGTH_SHORT).show();
                buscaCervejas(newText);
                return false;
            }
        };
    }

    private void buscaCervejas(String query) {

        //cervejas = ((CervejasAdapter) lista.getAdapter()).getList();
        query = query.toLowerCase();
        List<Cerveja> results = new ArrayList<Cerveja>();
        for (Cerveja cerveja: cervejas) {
            if(cerveja.nome.toLowerCase().contains(query)){
                Log.i("MYTASK", "match");
                results.add(cerveja);
            }
        }

        lista.setAdapter(new CervejasAdapter(TelaInicialActivity.this,results));
    }

    // Funcao para retornar o tratamento do evento de compartilhamento
    private Intent getDefautIntent() {
        // Intent ACTION_SEND. Qualquer app que responde essa intenção pode ser chamado
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        String textoShare = "Minha Lista de cervejas! \n";

        for (Cerveja cerveja: cervejas) {
            textoShare += cerveja.nome +"\n";
        }
        intent.putExtra(Intent.EXTRA_TEXT, textoShare);
        return intent;
    }
}
