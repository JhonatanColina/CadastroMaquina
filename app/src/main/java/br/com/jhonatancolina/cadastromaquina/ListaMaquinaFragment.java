package br.com.jhonatancolina.cadastromaquina;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.jhonatancolina.cadastromaquina.adapter.MaquinaAdapter;
import br.com.jhonatancolina.cadastromaquina.core.ClickRecyclerView_Interface;
import br.com.jhonatancolina.cadastromaquina.model.Maquina;
import br.com.jhonatancolina.cadastromaquina.service.MaquinaAPI;
import br.com.jhonatancolina.cadastromaquina.service.SessionRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListaMaquinaFragment extends Fragment implements ClickRecyclerView_Interface {

    private SessionRepository sr = new SessionRepository();
    private EditText etFiltroListaMaquina;
    private RecyclerView rvMaquina;
    private RecyclerView.LayoutManager mLayoutManager;
    private MaquinaAdapter adapter;
    private List<Maquina> maquinas = new ArrayList<>();

    public ListaMaquinaFragment() {
        // Required empty public constructor
    }


    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_lista_maquina, container, false);

        onKeyUpEditText();
        preencheRecyclerView("");
        return v;
    }

    private void onKeyUpEditText()
    {
        etFiltroListaMaquina = (EditText) v.findViewById(R.id.etFiltroListaMaquina);
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                preencheRecyclerView(etFiltroListaMaquina.getText().toString().trim());
            }
        };
        etFiltroListaMaquina.addTextChangedListener(fieldValidatorTextWatcher);
    }

    private void preencheRecyclerView(String pesquisa)
    {
        if(!pesquisa.equals("") && pesquisa !=null)
        {
            retornaMaquinasSincronizado(pesquisa);
            rvMaquina = (RecyclerView) v.findViewById(R.id.rvMaquina);
            mLayoutManager = new LinearLayoutManager(getActivity());
            rvMaquina.setLayoutManager(mLayoutManager);
            adapter = new MaquinaAdapter(getActivity(),maquinas,this);
            rvMaquina.setAdapter(adapter);
        }
        else
        {
            retornaMaquinasSincronizado();
            rvMaquina = (RecyclerView) v.findViewById(R.id.rvMaquina);
            mLayoutManager = new LinearLayoutManager(getActivity());
            rvMaquina.setLayoutManager(mLayoutManager);
            adapter = new MaquinaAdapter(getActivity(),maquinas,this);
            rvMaquina.setAdapter(adapter);
        }
    }

    @Override
    public void onCustomClick(Object object)
    {
        Maquina m = (Maquina) object;

        if(m==null)
        {
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), getString(R.string.erroEncontrarMaquina), Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(getActivity(),
                    ManterMaquinaActivity.class);

            intent.putExtra("Maquina",m);
            startActivity(intent);
        }
    }

    @Override
    public void onCloseButton(Object object) {
        final Maquina m = (Maquina) object;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(getActivity().getWindow().getDecorView().getRootView().getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity().getWindow().getDecorView().getRootView().getContext());
        }
        builder.setTitle(getString(R.string.deletarMaquina))
                .setMessage(getString(R.string.msgDeletarMaquina))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        apagaMaquina(m.getId());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Faça nada
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void retornaMaquinasSincronizado()
    { // metodo sincrono precisa estar em uma thread, start para iniciar e join pra esperar terminar
        Thread t = (new Thread()
        {
            public void run()
            {
                try
                {
                    MaquinaAPI api = getRetrofit().create(MaquinaAPI.class);
                    maquinas = api.findAll().execute().body();
                }
                catch(NetworkOnMainThreadException | IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void retornaMaquinasSincronizado(final String hostname)
    { // metodo sincrono precisa estar em uma thread, start para iniciar e join pra esperar terminar
        Thread t = (new Thread()
        {
            public void run()
            {
                try
                {
                    MaquinaAPI api = getRetrofit().create(MaquinaAPI.class);
                    maquinas = api.findByHostnameContaining(hostname).execute().body();
                }
                catch(NetworkOnMainThreadException | IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void apagaMaquina(String id)
    {
        MaquinaAPI api = getRetrofit().create(MaquinaAPI.class);

        api.deleteMaquina(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), getString(R.string.msgDeletarMaquinaOk), Snackbar.LENGTH_LONG).show();
                preencheRecyclerView("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), getString(R.string.erroConexao), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://cadastromaquina.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
