package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Idioma {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String codigoItem;

    @DatabaseField
    private String codigoIdioma;

    @DatabaseField
    private String nomeItem;

    @DatabaseField
    private String descricaoItem;

    public Idioma() {
        super();
    }

    public Idioma(String codigoItem, String codigoIdioma, String nomeItem,
                  String descricaoItem) {
        super();
        this.codigoItem = codigoItem;
        this.codigoIdioma = codigoIdioma;
        this.nomeItem = nomeItem;
        this.descricaoItem = descricaoItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getCodigoIdioma() {
        return codigoIdioma;
    }

    public void setCodigoIdioma(String codigoIdioma) {
        this.codigoIdioma = codigoIdioma;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricao_item) {
        this.descricaoItem = descricao_item;
    }

    /**
     * (código do item) - (nome do item)
     *
     */
    public String toString(){
        return Integer.parseInt(codigoItem) + " - " + nomeItem;
    }
}

