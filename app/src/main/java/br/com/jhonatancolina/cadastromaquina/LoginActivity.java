package br.com.jhonatancolina.cadastromaquina;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import br.com.jhonatancolina.cadastromaquina.model.Usuario;
import br.com.jhonatancolina.cadastromaquina.service.SessionRepository;
import br.com.jhonatancolina.cadastromaquina.service.UsuarioAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity
{
    // Referencias.
    private ProgressBar mProgressView;
    private EditText etUsuario;
    private EditText etSenha;
    private Button btLimpar,btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etSenha = (EditText) findViewById(R.id.etSenha);
        btLimpar = (Button) findViewById(R.id.btLimpar);
        btLogin = (Button) findViewById(R.id.btLogin);
        mProgressView = (ProgressBar) findViewById(R.id.login_progressL);
    }

    public void limpaCampos(View v)
    {
        etUsuario.setText("");
        etSenha.setText("");
        etUsuario.requestFocus();
    }

    public void fazLogin(View v)
    {
        mProgressView.setVisibility(View.VISIBLE);
        if(verificaCampos())
        {
            escondeTeclado(v);
            UsuarioAPI api = getRetrofit().create(UsuarioAPI.class);

            Usuario u = new Usuario();
            u.setUsuario(etUsuario.getText().toString());
            u.setSenha(etSenha.getText().toString());
            api.fazLogin(u).enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response)
                {
                    Usuario u = response.body();
                    // Joga Classe usuario na sessao estatica
                    //sessao publica
                    SessionRepository sr = new SessionRepository(u);
                    Intent intent = new Intent(LoginActivity.this,
                            PrincipalActivity.class);
                    LoginActivity.this.finish();
                    startActivity(intent);
                    mProgressView.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t)
                {
                    Toast.makeText(LoginActivity.this,getString(R.string.UsuarioSenhaIncorreto),Toast.LENGTH_LONG).show();
                    etSenha.setText("");
                    etSenha.requestFocus();
                    mProgressView.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            mProgressView.setVisibility(View.GONE);
        }
    }

    private Retrofit getRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl("https://cadastromaquina.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void escondeTeclado(View v)
    {
        //esconde teclado
        if (v != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private boolean verificaCampos()
    {
        if(etUsuario.getText().toString().length()==0)
        {
            etUsuario.setError(getString(R.string.campoUsuarioObg));
            return false;
        }
        else if((etSenha.getText().toString().length()==0))
        {
            etSenha.setError(getString(R.string.campoSenhObg));
            return false;
        }
        else
        {
            return true;
        }
    }
}

