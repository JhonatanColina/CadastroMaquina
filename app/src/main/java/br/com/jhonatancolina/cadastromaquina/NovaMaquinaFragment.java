package br.com.jhonatancolina.cadastromaquina;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import br.com.jhonatancolina.cadastromaquina.core.MaskEditUtil;
import br.com.jhonatancolina.cadastromaquina.model.Maquina;
import br.com.jhonatancolina.cadastromaquina.service.MaquinaAPI;
import br.com.jhonatancolina.cadastromaquina.service.SessionRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NovaMaquinaFragment extends Fragment implements View.OnClickListener {

    private SessionRepository sr = new SessionRepository();
    // Referencias da tela
    private EditText etNomeMaquinaNovMaq,etMacMaquinaNovMaq,etIPNovMaq,
            etLocalMaquinaNovMaq,etObservacaoMaquinaNovMaq;
    private Button btSalvarDadosUsuarioNovoMaq;
    private ProgressBar login_progressNovoUsu;


    public NovaMaquinaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nova_maquina, container, false);

        etNomeMaquinaNovMaq = (EditText) v.findViewById(R.id.etNomeMaquinaNovMaq);
        etMacMaquinaNovMaq = (EditText) v.findViewById(R.id.etMacMaquinaNovMaq);
        etIPNovMaq = (EditText) v.findViewById(R.id.etIPNovMaq);
        etLocalMaquinaNovMaq = (EditText) v.findViewById(R.id.etLocalMaquinaNovMaq);
        etObservacaoMaquinaNovMaq = (EditText) v.findViewById(R.id.etObservacaoMaquinaNovMaq);
        btSalvarDadosUsuarioNovoMaq = (Button) v.findViewById(R.id.btSalvarDadosUsuarioNovoMaq);
        btSalvarDadosUsuarioNovoMaq.setOnClickListener(this);

        login_progressNovoUsu = (ProgressBar) v.findViewById(R.id.login_progressNovoUsu);
        etMacMaquinaNovMaq.addTextChangedListener(MaskEditUtil.insert(MaskEditUtil.MAC_MASK, etMacMaquinaNovMaq));
        etIPNovMaq.addTextChangedListener(MaskEditUtil.insert(MaskEditUtil.IP_MASK, etIPNovMaq));
        return v;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btSalvarDadosUsuarioNovoMaq:
                salvarDadosNovaMaquina(v);
                break;
        }
    }

    public void escondeTeclado(View v) {
        //esconde teclado
        if (v != null)
        {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://cadastromaquina.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void salvarDadosNovaMaquina(final View v)
    {
        login_progressNovoUsu.setVisibility(View.VISIBLE);
        if(verificaCampos()) // se estiver preenchido
        {
            MaquinaAPI api = getRetrofit().create(MaquinaAPI.class);
            Maquina m = new Maquina(etNomeMaquinaNovMaq.getText().toString(),
                    etMacMaquinaNovMaq.getText().toString(),
                    etIPNovMaq.getText().toString(),
                    etLocalMaquinaNovMaq.getText().toString(),
                    etObservacaoMaquinaNovMaq.getText().toString());

            api.salvar(m).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response)
                {
                    Snackbar.make(v, getString(R.string.maquinaSalva), Snackbar.LENGTH_LONG).show();
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
            login_progressNovoUsu.setVisibility(View.GONE);
        }
    }

    private boolean verificaCampos()
    {
        if(etNomeMaquinaNovMaq.getText().toString().length()==0)
        {
            etNomeMaquinaNovMaq.setError(getString(R.string.campoNomeMaquinaObg));
            return false;
        }
        else if(etMacMaquinaNovMaq.getText().toString().length()==0)
        {
            etMacMaquinaNovMaq.setError(getString(R.string.campoMacMaquinaObg));
            return false;
        }
        else if(etIPNovMaq.getText().toString().length()==0)
        {
            etIPNovMaq.setError(getString(R.string.campoIPMaquinaObg));
            return false;
        }
        else if(etLocalMaquinaNovMaq.getText().toString().length()==0)
        {
            etLocalMaquinaNovMaq.setError(getString(R.string.campoLocalMaquinaObg));
            return false;
        }
        else
        {
            return true;
        }
    }

    private void limpaCampos()
    {
        etNomeMaquinaNovMaq.setText("");
        etMacMaquinaNovMaq.setText("");
        etIPNovMaq.setText("");
        etLocalMaquinaNovMaq.setText("");
        etObservacaoMaquinaNovMaq.setText("");
        etNomeMaquinaNovMaq.requestFocus();
    }



}
