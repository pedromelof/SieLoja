package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Item;

public class EnvioItemConsumo {

    private String sequencial;
    private Item item;
    private double quantidade;
    private String tipoConsumo;
    private int quantidadeRec;
    private String observacao;
    private int numeroCom;
    private String status;
    private String venpro;
    private String codbar;
    private double qtddeemb;
    private List<EnvioItemConsumoIns> itemConsumoIns;
    public EnvioItemConsumo() {
        super();
    }
    public EnvioItemConsumo(String sequencial, Item item, double quantidade,
                            String tipoConsumo, int quantidadeRec, String observacao,
                            int numeroCom, String status, String venpro,
                            List<EnvioItemConsumoIns> itemConsumoIns, String codbar, int qtddeemb) {
        super();
        this.sequencial = sequencial;
        this.item = item;
        this.quantidade = quantidade;
        this.tipoConsumo = tipoConsumo;
        this.quantidadeRec = quantidadeRec;
        this.observacao = observacao;
        this.numeroCom = numeroCom;
        this.status = status;
        this.venpro = venpro;
        this.itemConsumoIns = itemConsumoIns;
        this.codbar = codbar;
        this.qtddeemb = qtddeemb;
    }

    public double getQtddeemb() { return qtddeemb; }
    public String getCodbar() { return codbar; }
    public void setCodbar(String codbar) { this.codbar = codbar; }
    public void setQtddeemb(double qtddeemb) { this.qtddeemb = qtddeemb; }
    public String getSequencial() {
        return sequencial;
    }
    public void setSequencial(String sequencial) {
        this.sequencial = sequencial;
    }
    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }
    public double getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
    public String getTipoConsumo() {
        return tipoConsumo;
    }
    public void setTipoConsumo(String tipoConsumo) {
        this.tipoConsumo = tipoConsumo;
    }
    public int getQuantidadeRec() {
        return quantidadeRec;
    }
    public void setQuantidadeRec(int quantidadeRec) {
        this.quantidadeRec = quantidadeRec;
    }
    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public int getNumeroCom() {
        return numeroCom;
    }
    public void setNumeroCom(int numeroCom) {
        this.numeroCom = numeroCom;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getVenpro() {
        return venpro;
    }
    public void setVenpro(String venpro) {
        this.venpro = venpro;
    }
    public List<EnvioItemConsumoIns> getItemConsumoIns() {
        return itemConsumoIns;
    }
    public void setItemConsumoIns(List<EnvioItemConsumoIns> itemConsumoIns) {
        this.itemConsumoIns = itemConsumoIns;
    }
}

