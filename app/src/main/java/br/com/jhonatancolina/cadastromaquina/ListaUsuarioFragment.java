package br.com.jhonatancolina.cadastromaquina;


import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.jhonatancolina.cadastromaquina.adapter.UsuarioAdapter;
import br.com.jhonatancolina.cadastromaquina.core.ClickRecyclerView_Interface;
import br.com.jhonatancolina.cadastromaquina.model.Usuario;
import br.com.jhonatancolina.cadastromaquina.service.SessionRepository;
import br.com.jhonatancolina.cadastromaquina.service.UsuarioAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaUsuarioFragment extends Fragment implements ClickRecyclerView_Interface
{
    //Referencias
    private SessionRepository sr = new SessionRepository();

    private EditText etFiltroListaUsuario;
    private RecyclerView rvUsuario;
    private RecyclerView.LayoutManager mLayoutManager;
    private UsuarioAdapter adapter;
    private List<Usuario> users = new ArrayList<>();

    public ListaUsuarioFragment() {
        // Required empty public constructor
    }

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_lista_usuario, container, false);

        onKeyUpEditText();
        preencheRecyclerView("");
        return v;
    }

    private void preencheRecyclerView(String pesquisa)
    {
        if(!pesquisa.equals("") && pesquisa !=null)
        {
            retornaUsuariosSincronizado(pesquisa);
            rvUsuario = (RecyclerView) v.findViewById(R.id.rvUsuario);
            mLayoutManager = new LinearLayoutManager(getActivity());
            rvUsuario.setLayoutManager(mLayoutManager);
            adapter = new UsuarioAdapter(getActivity(),users,this);
            rvUsuario.setAdapter(adapter);
        }
        else
        {
            retornaUsuariosSincronizado();
            rvUsuario = (RecyclerView) v.findViewById(R.id.rvUsuario);
            mLayoutManager = new LinearLayoutManager(getContext());
            rvUsuario.setLayoutManager(mLayoutManager);
            adapter = new UsuarioAdapter(getActivity(),users,this);
            rvUsuario.setAdapter(adapter);
        }
    }

    public void onKeyUpEditText()
    {
        etFiltroListaUsuario = (EditText) v.findViewById(R.id.etFiltroListaUsuario);
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                preencheRecyclerView(etFiltroListaUsuario.getText().toString().trim());
            }
        };
        etFiltroListaUsuario.addTextChangedListener(fieldValidatorTextWatcher);
    }

    /**
     * Aqui é o método onde trata o clique em um item da lista
     */
    @Override
    public void onCustomClick(Object object)
    {
        Usuario u = (Usuario) object;

        if(u==null)
        {
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), getString(R.string.erroEncontrarUsuario), Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(getActivity(),ManterUsuarioActivity.class);
            intent.putExtra("Usuario",u);
            startActivity(intent);
        }
    }

    @Override
    public void onCloseButton(Object object)
    {
        final Usuario u = (Usuario) object;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(getActivity().getWindow().getDecorView().getRootView().getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity().getWindow().getDecorView().getRootView().getContext());
        }
        builder.setTitle(getString(R.string.deletarUsuario))
                .setMessage(getString(R.string.msgDeletarUsuario))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        apagaUsuario(u.getId());
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

    public Retrofit getRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl("https://cadastromaquina.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void retornaUsuariosSincronizado()
    { // metodo sincrono precisa estar em uma thread, start para iniciar e join pra esperar terminar
        Thread t = (new Thread()
        {
            public void run()
            {
                try
                {
                    UsuarioAPI api = getRetrofit().create(UsuarioAPI.class);
                    users = api.findAll().execute().body();
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

    private void retornaUsuariosSincronizado(final String nomeUsuario)
    { // metodo sincrono precisa estar em uma thread, start para iniciar e join pra esperar terminar
        Thread t = (new Thread()
        {
            public void run()
            {
                try
                {
                    UsuarioAPI api = getRetrofit().create(UsuarioAPI.class);
                    users = api.findByUsuarioContaining(nomeUsuario).execute().body();
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

    private void apagaUsuario(String id)
    {
        UsuarioAPI api = getRetrofit().create(UsuarioAPI.class);

        api.deleteUsuario(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), getString(R.string.msgDeletarUsuarioOk), Snackbar.LENGTH_LONG).show();
                preencheRecyclerView("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), getString(R.string.erroConexao), Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
