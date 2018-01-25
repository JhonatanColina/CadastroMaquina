package br.com.jhonatancolina.cadastromaquina.service;

import java.util.List;

import br.com.jhonatancolina.cadastromaquina.model.Maquina;
import br.com.jhonatancolina.cadastromaquina.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MaquinaAPI
{
    @POST("/maquina")
    Call<Void> salvar(@Body Maquina m);

    @GET("/maquina")
    Call<List<Maquina>> findAll();

    @GET("/maquina/hostname/{hostname}")
    Call<List<Maquina>> findByHostnameContaining(@Path("hostname") String hostname);

    @DELETE("/maquina/delete/{id}")
    Call<Void> deleteMaquina(@Path("id") String id);

    @GET("/maquina/count")
    Call<Long> countMaquinas();
}
