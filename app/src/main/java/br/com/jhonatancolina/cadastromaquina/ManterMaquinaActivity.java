package br.com.jhonatancolina.cadastromaquina;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import br.com.jhonatancolina.cadastromaquina.model.Maquina;
import br.com.jhonatancolina.cadastromaquina.model.Usuario;
import br.com.jhonatancolina.cadastromaquina.service.MaquinaAPI;
import br.com.jhonatancolina.cadastromaquina.service.SessionRepository;
import br.com.jhonatancolina.cadastromaquina.service.UsuarioAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManterMaquinaActivity extends AppCompatActivity
{

    private Maquina m;
    private SessionRepository sr = new SessionRepository();
    private FloatingActionButton fab;

    private EditText etIdMaquinaManter,etNomeMaquinaManter,etMacMaquinaManter,
            etIPManter,etLocalMaquinaManter,etObservacaoMaquinaNovMaq;
    private Button btSalvarDadosMaquina;
    private ProgressBar login_progressConf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_maquina);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // retorna usuario serializado da activity anterior
        m = (Maquina) getIntent().getSerializableExtra("Maquina");

        etIdMaquinaManter = (EditText) findViewById(R.id.etIdMaquinaManter);
        etNomeMaquinaManter = (EditText) findViewById(R.id.etNomeMaquinaManter);
        etMacMaquinaManter = (EditText) findViewById(R.id.etMacMaquinaManter);
        etIPManter = (EditText) findViewById(R.id.etIPManter);
        etLocalMaquinaManter = (EditText) findViewById(R.id.etLocalMaquinaManter);
        etObservacaoMaquinaNovMaq = (EditText) findViewById(R.id.etObservacaoMaquinaNovMaq);
        btSalvarDadosMaquina = (Button) findViewById(R.id.btSalvarDadosMaquina);
        fab = (FloatingActionButton) findViewById(R.id.fab);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void preencheCampos() {
        etIdMaquinaManter.setText(m.getId());
        etNomeMaquinaManter.setText(m.getHostname());
        etMacMaquinaManter.setText(m.getMac());
        etIPManter.setText(m.getIp());
        etLocalMaquinaManter.setText(m.getLocal());
        etObservacaoMaquinaNovMaq.setText(m.getObservacoes());
    }

    public void liberaCamposEdicao(View v) {
        etNomeMaquinaManter.setEnabled(true);
        etMacMaquinaManter.setEnabled(true);
        etIPManter.setEnabled(true);
        etLocalMaquinaManter.setEnabled(true);
        etObservacaoMaquinaNovMaq.setEnabled(true);
        etNomeMaquinaManter.requestFocus();
        Snackbar.make(v, getString(R.string.campoLiberado), Snackbar.LENGTH_LONG).show();
        fab.setVisibility(View.GONE);
        btSalvarDadosMaquina.setVisibility(View.VISIBLE);
    }

    private void bloqueaCampos() {
        etNomeMaquinaManter.setEnabled(false);
        etMacMaquinaManter.setEnabled(false);
        etIPManter.setEnabled(false);
        etLocalMaquinaManter.setEnabled(false);
        etObservacaoMaquinaNovMaq.setEnabled(false);
        fab.setVisibility(View.VISIBLE);
        btSalvarDadosMaquina.setVisibility(View.GONE);
    }

    public void salvarDadosMaquina(final View v)
    {
        login_progressConf.setVisibility(View.VISIBLE);
        if(verificaCampos()) // se estiver preenchido
        {
            MaquinaAPI api = getRetrofit().create(MaquinaAPI.class);
            Maquina m = new Maquina(etIdMaquinaManter.getText().toString(),
                    etNomeMaquinaManter.getText().toString(),
                    etMacMaquinaManter.getText().toString(),
                    etIPManter.getText().toString(),
                    etLocalMaquinaManter.getText().toString(),
                    etObservacaoMaquinaNovMaq.getText().toString());

            api.salvar(m).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response)
                {
                    Snackbar.make(v, getString(R.string.maquinaAtualizada), Snackbar.LENGTH_LONG).show();
                    escondeTeclado(v);
                    bloqueaCampos();
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
            login_progressConf.setVisibility(View.GONE);
        }
    }

    private boolean verificaCampos()
    {
        if(etNomeMaquinaManter.getText().toString().length()==0)
        {
            etNomeMaquinaManter.setError(getString(R.string.campoNomeMaquinaObg));
            return false;
        }
        else if(etMacMaquinaManter.getText().toString().length()==0)
        {
            etMacMaquinaManter.setError(getString(R.string.campoMacMaquinaObg));
            return false;
        }
        else if(etIPManter.getText().toString().length()==0)
        {
            etIPManter.setError(getString(R.string.campoIPMaquinaObg));
            return false;
        }
        else if(etLocalMaquinaManter.getText().toString().length()==0)
        {
            etLocalMaquinaManter.setError(getString(R.string.campoLocalMaquinaObg));
            return false;
        }
        else
        {
            return true;
        }
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
}
