package br.salt.sieloja.rest.responseobject;

public class EnvioItemConsumoIns {

    private String codInsumo;
    private String desInsumo;
    private int quantidade;
    private String unidade;
    public EnvioItemConsumoIns() {
        super();
    }
    public EnvioItemConsumoIns(String codInsumo, String desInsumo,
                               int quantidade, String unidade) {
        super();
        this.codInsumo = codInsumo;
        this.desInsumo = desInsumo;
        this.quantidade = quantidade;
        this.unidade = unidade;
    }
    public String getCodInsumo() {
        return codInsumo;
    }
    public void setCodInsumo(String codInsumo) {
        this.codInsumo = codInsumo;
    }
    public String getDesInsumo() {
        return desInsumo;
    }
    public void setDesInsumo(String desInsumo) {
        this.desInsumo = desInsumo;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public String getUnidade() {
        return unidade;
    }
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
