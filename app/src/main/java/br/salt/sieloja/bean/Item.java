package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Item {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String codigo;

    @DatabaseField
    private String nome;

    @DatabaseField
    private String descricao;

    @DatabaseField
    private String codigoGrupo;

    @DatabaseField
    private String codigoSubgrupo;

    @DatabaseField
    private double preco;

    @DatabaseField
    private String imagem;

    @DatabaseField
    private String unidade;

    @DatabaseField
    private double comicao;

    @DatabaseField
    private String itemComposto;

    @DatabaseField
    private String tipoInsumo;

    @DatabaseField
    private int qtdMaxInsumo;

    public Item() {
        super();
    }

    public Item(String codigo, String nome, String descricao,
                String codigoGrupo, String codigoSubgrupo, double preco,
                String imagem,String unidade, double comicao) {
        super();
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.codigoGrupo = codigoGrupo;
        this.codigoSubgrupo = codigoSubgrupo;
        this.preco = preco;
        this.imagem = imagem;
        this.unidade = unidade;
        this.comicao = comicao;
    }

    public String getItemComposto() {
        return itemComposto;
    }

    public void setItemComposto(String itemComposto) {
        this.itemComposto = itemComposto;
    }

    public String getTipoInsumo() {
        return tipoInsumo;
    }

    public void setTipoInsumo(String tipoInsumo) {
        this.tipoInsumo = tipoInsumo;
    }

    public int getQtdMaxInsumo() {
        return qtdMaxInsumo;
    }

    public void setQtdMaxInsumo(int qtdMaxInsumo) {
        this.qtdMaxInsumo = qtdMaxInsumo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getCodigoSubgrupo() {
        return codigoSubgrupo;
    }

    public void setCodigoSubgrupo(String codigoSubgrupo) {
        this.codigoSubgrupo = codigoSubgrupo;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getComicao() {
        return comicao;
    }

    public void setComicao(double comicao) {
        this.comicao = comicao;
    }

    /**
     * (código do item) - (nome do item)
     *
     */
    public String toString(){
        return Integer.parseInt(codigo) + " - " + nome;
    }
}
