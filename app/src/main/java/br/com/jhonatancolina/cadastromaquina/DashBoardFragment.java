package br.com.jhonatancolina.cadastromaquina;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.jhonatancolina.cadastromaquina.service.MaquinaAPI;
import br.com.jhonatancolina.cadastromaquina.service.UsuarioAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    private TextView tvNumeroMaquinas,tvNumeroUsuarios;

    public DashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dash_board, container, false);

        tvNumeroMaquinas = (TextView) v.findViewById(R.id.tvNumeroMaquinas);
        tvNumeroUsuarios = (TextView) v.findViewById(R.id.tvNumeroUsuarios);
        contagemMaquinas();
        contagemUsuarios();
        return v;
    }

    public Retrofit getRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl("https://cadastromaquina.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void contagemMaquinas()
    {
        MaquinaAPI api = getRetrofit().create(MaquinaAPI.class);

        api.countMaquinas().enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                tvNumeroMaquinas.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                tvNumeroMaquinas.setText("Erro");
            }
        });
    }

    private void contagemUsuarios()
    {
        UsuarioAPI api = getRetrofit().create(UsuarioAPI.class);

        api.countUsuario().enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                tvNumeroUsuarios.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                tvNumeroUsuarios.setText("Erro");
            }
        });
    }

}
