package br.salt.sieloja.rest.responseobject;

import java.util.Date;

public class EnvioParcial extends Envio {

    private String mesa;
    private String cartao;
    private String dataInicio;
    private String dataFim;
    private String unidade;

    public EnvioParcial() {
        super();
    }
    public EnvioParcial(String ipBanco, String nomeBanco, String mesa,
                        String cartao, String data, String unidade) {
        super(ipBanco, nomeBanco);
        this.mesa = mesa;
        this.cartao = cartao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.unidade = unidade;
    }
    public String getMesa() {
        return mesa;
    }
    public void setMesa(String mesa) {
        this.mesa = mesa;
    }
    public String getCartao() {
        return cartao;
    }
    public void setCartao(String cartao) {
        this.cartao = cartao;
    }
    public String getDataInicio() {
        return dataInicio;
    }
    public String getDataFim() {
        return dataFim;
    }
    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }
    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
    public String getUnidade() {
        return unidade;
    }
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}

