package br.com.jhonatancolina.cadastromaquina.model;

import java.io.Serializable;

public class Maquina implements Serializable
{
    private String id;
    private String hostname;
    private String mac;
    private String ip;
    private String local;
    private String observacoes;

    public Maquina(){}

    public Maquina(String id,String hostname, String mac, String ip, String local, String observacoes) {
        this.id = id;
        this.hostname = hostname;
        this.mac = mac;
        this.ip = ip;
        this.local = local;
        this.observacoes = observacoes;
    }

    public Maquina(String hostname, String mac, String ip, String local, String observacoes) {
        this.hostname = hostname;
        this.mac = mac;
        this.ip = ip;
        this.local = local;
        this.observacoes = observacoes.equals("")?null:observacoes;
    }

    @Override
    public String toString() {
        return "Maquina{" +
                "id='" + id + '\'' +
                ", hostname='" + hostname + '\'' +
                ", mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", local='" + local + '\'' +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

}
