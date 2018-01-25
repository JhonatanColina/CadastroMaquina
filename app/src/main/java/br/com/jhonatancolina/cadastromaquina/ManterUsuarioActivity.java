package br.com.jhonatancolina.cadastromaquina;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import br.com.jhonatancolina.cadastromaquina.model.Usuario;
import br.com.jhonatancolina.cadastromaquina.service.SessionRepository;
import br.com.jhonatancolina.cadastromaquina.service.UsuarioAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManterUsuarioActivity extends AppCompatActivity {

    private Usuario u;

    // Referencias
    private AutoCompleteTextView etIdUsuarioDados, etNomeUsuarioDados,
            etSenhaDados, etRepetirSenhaDados;
    private Switch switchNivelAdm;
    private FloatingActionButton fab;
    private Button btSalvarDadosUsuario;
    private ProgressBar login_progressConf;
    private SessionRepository sr = new SessionRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // retorna usuario serializado da activity anterior
        u = (Usuario) getIntent().getSerializableExtra("Usuario");

        //Referencias
        etIdUsuarioDados = (AutoCompleteTextView) findViewById(R.id.etIdUsuarioDados);
        etNomeUsuarioDados = (AutoCompleteTextView) findViewById(R.id.etNomeUsuarioDados);
        switchNivelAdm = (Switch) findViewById(R.id.switchNivelAdm);
        etSenhaDados = (AutoCompleteTextView) findViewById(R.id.etSenhaDados);
        etRepetirSenhaDados = (AutoCompleteTextView) findViewById(R.id.etRepetirSenhaDados);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btSalvarDadosUsuario = (Button) findViewById(R.id.btSalvarDadosUsuario);
        login_progressConf = (ProgressBar) findViewById(R.id.login_progressConf);
        preencheCampos();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void preencheCampos() {
        etIdUsuarioDados.setText(u.getId());
        etNomeUsuarioDados.setText(u.getUsuario());
        switchNivelAdm.setChecked(u.isAdmin());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public void liberaCamposEdicao(View v) {
        etNomeUsuarioDados.setEnabled(true);
        etSenhaDados.setEnabled(true);
        etRepetirSenhaDados.setEnabled(true);
        switchNivelAdm.setEnabled(true);
        etNomeUsuarioDados.requestFocus();
        Snackbar.make(v, getString(R.string.campoLiberado), Snackbar.LENGTH_LONG).show();
        fab.setVisibility(View.GONE);
        btSalvarDadosUsuario.setVisibility(View.VISIBLE);
        etRepetirSenhaDados.setVisibility(View.VISIBLE);
    }

    private void bloqueaCampos() {
        etNomeUsuarioDados.setEnabled(false);
        etSenhaDados.setEnabled(false);
        etRepetirSenhaDados.setEnabled(false);
        switchNivelAdm.setEnabled(false);
        fab.setVisibility(View.VISIBLE);
        btSalvarDadosUsuario.setVisibility(View.GONE);
        etRepetirSenhaDados.setVisibility(View.GONE);
        etRepetirSenhaDados.setText("");
        etSenhaDados.setText("");
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://cadastromaquina.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void escondeTeclado(View v) {
        //esconde teclado
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


        }
    }

    private boolean verificaCampos() {
        if (etNomeUsuarioDados.getText().toString().length() == 0) {
            etNomeUsuarioDados.setError(getString(R.string.campoUsuarioObg));
            return false;
        } else if (etSenhaDados.getText().toString().length() == 0) {
            etSenhaDados.setError(getString(R.string.campoSenhObg));
            return false;
        } else if (etRepetirSenhaDados.getText().toString().length() == 0) {
            etRepetirSenhaDados.setError(getString(R.string.campoRepSenhaObg));
            return false;
        } else {
            return true;
        }
    }

    public void salvarDadosUsuario(final View v)
    {
        //mostra barra de progresso
        login_progressConf.setVisibility(View.VISIBLE);
        if(verificaCampos())
        {
            if(etSenhaDados.getText().toString().equals(etRepetirSenhaDados.getText().toString()))
            {
                UsuarioAPI api = getRetrofit().create(UsuarioAPI.class);
                Usuario u = new Usuario(etIdUsuarioDados.getText().toString(),
                        etNomeUsuarioDados.getText().toString(),etSenhaDados.getText().toString(),switchNivelAdm.isChecked());
                api.atualizar(u).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)
                    {
                        Snackbar.make(v, getString(R.string.usuarioAtualizado), Snackbar.LENGTH_LONG).show();
                        bloqueaCampos();
                        escondeTeclado(v);
                        login_progressConf.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t)
                    {
                        System.out.println(t);
                        Snackbar.make(v, getString(R.string.erroConexao), Snackbar.LENGTH_LONG).show();
                        escondeTeclado(v);
                        login_progressConf.setVisibility(View.GONE);
                    }
                });
            }
            else
            {
                Snackbar.make(v, getString(R.string.campoSenhaRepeteSenha), Snackbar.LENGTH_LONG).show();
                etRepetirSenhaDados.setText("");
                etRepetirSenhaDados.requestFocus();
                login_progressConf.setVisibility(View.GONE);
            }
        }
        else
        {
            login_progressConf.setVisibility(View.GONE);
        }
    }
}
