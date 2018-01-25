package br.com.jhonatancolina.cadastromaquina.service;

import java.util.List;

import br.com.jhonatancolina.cadastromaquina.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioAPI
{
    @POST("/usuario/login")
    Call<Usuario> fazLogin(@Body Usuario u);

    @POST("/usuario")
    Call<Void> atualizar(@Body Usuario u);

    @POST("/usuario")
    Call<Void> salvar(@Body Usuario u);

    @GET("/usuario")
    Call<List<Usuario>> findAll();

    @GET("/usuario/nomeUsuario/{nomeUsuario}")
    Call<List<Usuario>> findByUsuarioContaining(@Path("nomeUsuario") String nomeUsuario);

    @GET("/usuario/id/{id}")
    Call<Usuario> findById(@Path("id") String id);

    @DELETE("/usuario/apagar/{id}")
    Call<Void> deleteUsuario(@Path("id") String id);

    @GET("/usuario/count")
    Call<Long> countUsuario();
}
