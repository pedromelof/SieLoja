package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DecimalFormat;

import br.salt.sieloja.view.util.BaseActivity;

@DatabaseTable
public class Parcial {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String numPed;

    @DatabaseField
    private String codVen;

    @DatabaseField
    private String status;

    @DatabaseField
    private String mesa;

    @DatabaseField
    private String cartao;

    @DatabaseField
    private String codItem;

    @DatabaseField
    private String decItem;

    @DatabaseField
    private String comItem;

    @DatabaseField
    private String nomcli;

    @DatabaseField
    private String unidade;

    @DatabaseField
    private double valor;

    @DatabaseField
    private double qtd;

    @DatabaseField
    private String tipPag;

    @DatabaseField
    private String forPag;

    @DatabaseField
    private double comissao;

    public Parcial() {
        super();
    }

    public Parcial(String codItem, String decItem, String comItem,
                   double valor, double qtd, double comissao,
                   String tipPag, String forPag) {
        super();
        this.numPed = null;
        this.codVen = null;
        this.status = "ABERTO";
        this.unidade = "UND";
        this.codItem = codItem;
        this.decItem = decItem;
        this.comItem = comItem;
        this.valor = valor;
        this.qtd = qtd;
        this.comissao = comissao;
        this.tipPag = tipPag;
        this.forPag = forPag;
    }

    public Parcial(String numPed, String codVen, String status, String mesa, String cartao, String codItem,
                       String decItem, String comItem, String nomcli, String unidade, double valor, double qtd, double comissao,
                   String tipPag, String forPag) {
        this.numPed = numPed;
        this.codVen = codVen;
        this.status = status;
        this.mesa = mesa;
        this.cartao = cartao;
        this.codItem = codItem;
        this.decItem = decItem;
        this.comItem = comItem;
        this.valor = valor;
        this.qtd = qtd;
        this.comissao = comissao;
        this.nomcli = nomcli;
        this.unidade = unidade;
        this.tipPag = tipPag;
        this.forPag = forPag;
    }

    public String getUnidade() { return unidade; }

    public void setUnidade(String unidade) { this.unidade = unidade; }

    public String getNomcli() { return nomcli; }

    public void setNomcli(String nomcli) { this.nomcli = nomcli; }

    public String getCodVen() {
        return codVen;
    }

    public String getNumPed() {
        return numPed;
    }

    public String getStatus() {
        return status;
    }

    public void setCodVen(String codVen) {
        this.codVen = codVen;
    }

    public void setNumPed(String numPed) {
        this.numPed = numPed;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCodItem() {
        return codItem;
    }

    public void setCodItem(String codItem) {
        this.codItem = codItem;
    }


    public String getDecItem() {
        return decItem;
    }

    public void setDecItem(String decItem) {
        this.decItem = decItem;
    }

    public String getComItem() {
        return comItem;
    }

    public void setComItem(String comItem) {
        this.comItem = comItem;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setQtd(double qtd) {
        this.qtd = qtd;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public double getValor() {
        return valor;
    }

    public double getQtd() {
        return qtd;
    }

    public String getValorTxt() {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(valor);
    }

    public String getQtdTxt() {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(qtd);
    }

    public double getComissao() {
        return comissao;
    }

    public String getTipPag() {
        return tipPag;
    }
    public void setTipPag(String tipPag) {
        this.tipPag = tipPag;
    }
    public String getForPag() {
        return forPag;
    }
    public void setForPag(String forPag) {
        this.tipPag = forPag;
    }

    public String toString(){
        return BaseActivity.getCodigo(Integer.parseInt(codItem)+"", 5) + " - " + decItem;
    }
}
