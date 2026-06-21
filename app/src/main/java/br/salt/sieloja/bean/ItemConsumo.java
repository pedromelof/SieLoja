package br.salt.sieloja.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ItemConsumo {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Consumo consumo;

    @DatabaseField
    private String codigoItem;

    @DatabaseField
    private double quantidade;

    @DatabaseField
    private String observacao;

    @DatabaseField
    private String codbar;

    @DatabaseField
    private double qtddeemb;

    @DatabaseField
    private double preco;

    public ItemConsumo() {
        super();
    }

    public ItemConsumo(Consumo consumo, String codigoItem, double quantidade,
                       String observacao, String codbar, double qtddeemb, double preco) {
        super();
        this.consumo = consumo;
        this.codigoItem = codigoItem;
        this.quantidade = quantidade;
        this.observacao = observacao;
        this.codbar = codbar;
        this.qtddeemb = qtddeemb;
        this.preco = preco;
    }

    public double getPreco() { return preco; }

    public void setPreco(double preco) { this.preco = preco; }

    public String getCodbar() { return codbar; }

    public double getQtddeemb() { return qtddeemb; }

    public void setCodbar(String codbar) { this.codbar = codbar; }

    public void setQtddeemb(int qtddeemb) { this.qtddeemb = qtddeemb; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Consumo getConsumo() {
        return consumo;
    }

    public void setConsumo(Consumo consumo) {
        this.consumo = consumo;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigo_item) {
        this.codigoItem = codigo_item;
    }

    public double getQuantidade() { return quantidade; }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
