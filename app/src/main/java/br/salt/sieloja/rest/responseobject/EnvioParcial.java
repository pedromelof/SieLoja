package br.salt.sieloja.rest.responseobject;

public class EnvioParcial extends Envio {

    private String mesa;
    private String cartao;
    private String data;
    private String unidade;

    public EnvioParcial() {
        super();
    }
    public EnvioParcial(String ipBanco, String nomeBanco, String mesa,
                        String cartao, String data, String unidade) {
        super(ipBanco, nomeBanco);
        this.mesa = mesa;
        this.cartao = cartao;
        this.data = data;
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
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getUnidade() {
        return unidade;
    }
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}

