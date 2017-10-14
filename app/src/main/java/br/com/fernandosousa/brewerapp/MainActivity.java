package br.com.fernandosousa.brewerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends DebugActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ImageView imagem = (ImageView) findViewById(R.id.imagemLogin);
        imagem.setImageResource(R.drawable.login_img);

        TextView texto = (TextView) findViewById(R.id.mensagemInicio);
        texto.setText(R.string.mensagem_login_activity);

        int branco = ContextCompat.getColor(this, R.color.branco);
        texto.setTextColor(branco);

        int vermelho = ContextCompat.getColor(this, R.color.vermelho);
        texto.setBackgroundColor(vermelho);


        TextView textoSenha = (TextView) findViewById(R.id.textoSenha);
        textoSenha.setText(R.string.texto_senha);

        Button botao = (Button) findViewById(R.id.botaoLogin);

        botao.setOnClickListener(onClickLogin());

        // recuperar nome do usuário, senha e valor do checkbox para colocar na tela
        TextView campoUsuario = (TextView) findViewById(R.id.campoUsuario);
        TextView campoSenha = (TextView) findViewById(R.id.campoSenha);
        CheckBox checkLembrar = (CheckBox) findViewById(R.id.checkLembrar);

        String nomeUsuario = Prefs.getString(MainActivity.this, "usuario");
        String senhaUsuario = Prefs.getString(MainActivity.this, "senha");
        boolean lembrar = Prefs.getBoolean(MainActivity.this, "lembrar");

        campoUsuario.setText(nomeUsuario);
        campoSenha.setText(senhaUsuario);
        checkLembrar.setChecked(lembrar);
    }

    private View.OnClickListener onClickLogin() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                TextView campoUsuario = (TextView) findViewById(R.id.campoUsuario);
                TextView campoSenha = (TextView) findViewById(R.id.campoSenha);
                CheckBox checkLembrar = (CheckBox) findViewById(R.id.checkLembrar);

                String txtUsuario = campoUsuario.getText().toString();
                String txtSenha = campoSenha.getText().toString();
                boolean lembrar = checkLembrar.isChecked();

                // salvar nas preferências o nome do usuário e senha
                if (lembrar) {
                    Prefs.setBoolean(MainActivity.this, "lembrar", lembrar);
                    Prefs.setString(MainActivity.this, "usuario", txtUsuario);
                    Prefs.setString(MainActivity.this, "senha", txtSenha);
                } else {
                    Prefs.setBoolean(MainActivity.this, "lembrar", false);
                    Prefs.setString(MainActivity.this, "usuario", "");
                    Prefs.setString(MainActivity.this, "senha", "");
                }


                //Toast.makeText(MainActivity.this, "Usuário: " + txtUsuario + "; Senha: " + txtSenha, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, TelaInicialActivity.class);

                Bundle params = new Bundle();
                params.putString("nome", txtUsuario);
                intent.putExtras(params);

                // sem resultado
                //startActivity(intent);
                // com resultado
                startActivityForResult(intent, 1);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("result");
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
