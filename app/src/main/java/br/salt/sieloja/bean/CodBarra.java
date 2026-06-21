package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class CodBarra implements Serializable {

    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String item;

    @DatabaseField
    private String codbar;

    @DatabaseField
    private String unid;

    @DatabaseField
    private int qtde;

    public CodBarra(){ super(); }

    public CodBarra(String item, String codbar, String unid, int qtde){
        this.item = item;
        this.codbar = codbar;
        this.unid = unid;
        this.qtde = qtde;
    }

    public int getQtde() {
        return qtde;
    }

    public int getId() {
        return id;
    }

    public String getCodbar() {
        return codbar;
    }

    public String getItem() {
        return item;
    }

    public String getUnid() {
        return unid;
    }

    public void setCodbar(String codbar) {
        this.codbar = codbar;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public void setUnid(String unid) {
        this.unid = unid;
    }
}
