package br.com.jhonatancolina.cadastromaquina.service;

import br.com.jhonatancolina.cadastromaquina.model.Usuario;

public class SessionRepository
{
    static Usuario u;

    public SessionRepository(){}

    public SessionRepository(Usuario u)
    {
        this.u = u;
    }

    public Usuario getUsuario()
    {
        return this.u;
    }

    public void setUsuario(Usuario u) {this.u = u;}
}
