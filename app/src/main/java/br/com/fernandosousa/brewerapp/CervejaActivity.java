package br.com.fernandosousa.brewerapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class CervejaActivity extends AppCompatActivity {

    private Cerveja cerveja;
    public static final int  RETORNO_ALTERA_ACTIVITY = 3;

    ImageView img;
    TextView nome;
    CheckBox favorito;
    TextView tipo;
    TextView pais;
    TextView preco;
    TextView endereco;
    TextView latitude;
    TextView longitude;
    TextView origem;
    TextView brilho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerveja);

        // recuperar objeto da cerveja da Intent
        Intent it = getIntent();
        // variável da classe
        cerveja = (Cerveja)it.getSerializableExtra("cerveja");
        // finalize o código para preencher seus campos do layout
        // com os dados do Objeto Cerveja

        // preecher campos do layout
        img = (ImageView)findViewById(R.id.imagemDados);
        nome = (TextView)findViewById(R.id.nomeDados);
        favorito = (CheckBox) findViewById(R.id.favoritoDados);
        tipo = (TextView)findViewById(R.id.tipoDados);
        pais = (TextView)findViewById(R.id.paisDados);
        preco = (TextView)findViewById(R.id.precoDados);
        endereco = (TextView)findViewById(R.id.enderecoDados);
        latitude = (TextView)findViewById(R.id.latitudeDados);
        longitude = (TextView)findViewById(R.id.longitudeDados);
        origem = (TextView)findViewById(R.id.origemDados);
        brilho = (TextView)findViewById(R.id.brilhoDados);

        setCerveja();

    }

    private void setCerveja() {
        Picasso.with(CervejaActivity.this).load(cerveja.imagem).into(img);
        nome.setText(cerveja.nome);
        if(cerveja.favorita == 1) {
            favorito.setVisibility(View.VISIBLE);
        } else {
            favorito.setVisibility(View.INVISIBLE);
        }
        tipo.setText(cerveja.tipo);
        pais.setText(cerveja.pais);
        preco.setText(Double.toString(cerveja.preco));
        endereco.setText(cerveja.endereco);
        latitude.setText(cerveja.latitude);
        longitude.setText(cerveja.longitude);
        origem.setText(cerveja.origem==1?"Importada":"Nacional");
        brilho.setText(cerveja.brilho== R.id.radioBrilhante?"Brilhante":(cerveja.brilho== R.id.radioOpaca?"Opaca":"Turva"));
    }

    @Override
    // Método para colocar o menu na ActionBar
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflar o menu na view
        getMenuInflater().inflate(R.menu.menu_cerveja, menu);
        return true;
    }

    // Tratar os eventos dos botões do menu
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_alterar) {
            Toast.makeText(CervejaActivity.this, "Alterar",Toast.LENGTH_SHORT).show();
            Intent cadastroIt = new Intent(CervejaActivity.this, CadastroActivity.class);
            cadastroIt.putExtra("cerveja", cerveja);
            startActivityForResult(cadastroIt, RETORNO_ALTERA_ACTIVITY);
        } else if (id == R.id.action_remover) {
            Toast.makeText(CervejaActivity.this, "Remover", Toast.LENGTH_SHORT).show();
            CervejaDB cervejaDB = new CervejaDB(CervejaActivity.this);
            cervejaDB.delete(cerveja);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("msg","Cerveja excluída com sucesso");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RETORNO_ALTERA_ACTIVITY ) {
            cerveja = (Cerveja)data.getSerializableExtra("cerveja");
            setCerveja();


        }
    }
}
