package br.com.jhonatancolina.cadastromaquina;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class NovoUsuarioFragment extends Fragment implements View.OnClickListener{

    private SessionRepository sr = new SessionRepository();
    private EditText etNomeUsuarioDadosNovoUsu,etSenhaDadosNovoUsu,etRepetirSenhaDadosNovoUsu;
    private Switch switchNivelAdmNovoUsu;
    private ProgressBar login_progressNovoUsu;
    private Button btSalvarDadosUsuarioNovoUsu;


    public NovoUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_novo_usuario, container, false);

        etNomeUsuarioDadosNovoUsu = (EditText) v.findViewById(R.id.etNomeUsuarioDadosNovoUsu);
        etSenhaDadosNovoUsu = (EditText) v.findViewById(R.id.etSenhaDadosNovoUsu);
        etRepetirSenhaDadosNovoUsu = (EditText) v.findViewById(R.id.etRepetirSenhaDadosNovoUsu);
        switchNivelAdmNovoUsu = (Switch) v.findViewById(R.id.switchNivelAdmNovoUsu);
        login_progressNovoUsu = (ProgressBar) v.findViewById(R.id.login_progressNovoUsu);
        btSalvarDadosUsuarioNovoUsu = (Button) v.findViewById(R.id.btSalvarDadosUsuarioNovoUsu);
        btSalvarDadosUsuarioNovoUsu.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btSalvarDadosUsuarioNovoUsu:
                salvarDadosNovoUsuario(v);
                break;
        }
    }

    private void salvarDadosNovoUsuario(final View v)
    {
        login_progressNovoUsu.setVisibility(View.VISIBLE);
        if(verificaCampos()) // se estiver preenchido
        {
            if(etSenhaDadosNovoUsu.getText().toString().equals(etRepetirSenhaDadosNovoUsu.getText().toString())) // se os campos de senha estiverem corretos
            {
                UsuarioAPI api = getRetrofit().create(UsuarioAPI.class);
                Usuario u = new Usuario(etNomeUsuarioDadosNovoUsu.getText().toString(),
                        etSenhaDadosNovoUsu.getText().toString(),switchNivelAdmNovoUsu.isChecked());

                api.salvar(u).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)
                    {
                        Snackbar.make(v, getString(R.string.usuarioSalvo), Snackbar.LENGTH_LONG).show();
                        escondeTeclado(v);
                        limpaCampos();
                        login_progressNovoUsu.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t)
                    {
                        System.out.println(t);
                        Snackbar.make(v, getString(R.string.erroConexao), Snackbar.LENGTH_LONG).show();
                        escondeTeclado(v);
                        login_progressNovoUsu.setVisibility(View.GONE);
                    }
                });
            }
            else
            {
                Snackbar.make(v, getString(R.string.campoSenhaRepeteSenha), Snackbar.LENGTH_LONG).show();
                etRepetirSenhaDadosNovoUsu.setText("");
                etSenhaDadosNovoUsu.requestFocus();
                login_progressNovoUsu.setVisibility(View.GONE);
            }
        }
        else
        {
            login_progressNovoUsu.setVisibility(View.GONE);
        }
    }

    private void limpaCampos()
    {
        etSenhaDadosNovoUsu.setText("");
        etRepetirSenhaDadosNovoUsu.setText("");
        etNomeUsuarioDadosNovoUsu.setText("");
        switchNivelAdmNovoUsu.setChecked(false);
        etNomeUsuarioDadosNovoUsu.requestFocus();
    }

    public void escondeTeclado(View v)
    {
        //esconde teclado
        if (v != null)
        {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public Retrofit getRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl("https://cadastromaquina.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private boolean verificaCampos()
    {
        if(etNomeUsuarioDadosNovoUsu.getText().toString().length()==0)
        {
            etNomeUsuarioDadosNovoUsu.setError(getString(R.string.campoUsuarioObg));
            return false;
        }
        else if(etSenhaDadosNovoUsu.getText().toString().length()==0)
        {
            etSenhaDadosNovoUsu.setError(getString(R.string.campoSenhObg));
            return false;
        }
        else if(etRepetirSenhaDadosNovoUsu.getText().toString().length()==0)
        {
            etRepetirSenhaDadosNovoUsu.setError(getString(R.string.campoRepSenhaObg));
            return false;
        }
        else
        {
            return true;
        }
    }

}
