package br.salt.sieloja.rest.responseobject;

public class Envio {

    protected String ipBanco;
    protected String nomeBanco;

    public Envio() {
        super();
    }
    public Envio(String ipBanco, String nomeBanco) {
        super();
        this.ipBanco = ipBanco;
        this.nomeBanco = nomeBanco;
    }
    public String getIpBanco() {
        return ipBanco;
    }
    public void setIpBanco(String ipBancoDeDados) {
        this.ipBanco = ipBancoDeDados;
    }
    public String getNomeBanco() {
        return nomeBanco;
    }
    public void setNomeBanco(String nomeBancoDeDados) {
        this.nomeBanco = nomeBancoDeDados;
    }
}
