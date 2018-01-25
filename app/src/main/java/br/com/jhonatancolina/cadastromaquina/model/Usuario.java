package br.com.jhonatancolina.cadastromaquina.model;

import java.io.Serializable;

public class Usuario implements Serializable
{
    private String id;
    private String usuario;
    private String senha;
    private boolean admin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", usuario='" + usuario + '\'' +
                ", senha='" + senha + '\'' +
                ", admin=" + admin +
                '}';
    }

    public Usuario(){}

    public Usuario(String id, String usuario, String senha, boolean admin)
    {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
        this.admin = admin;
    }

    public Usuario(String usuario, String senha, boolean admin)
    {
        this.usuario = usuario;
        this.senha = senha;
        this.admin = admin;
    }
}
