package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class FormaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String forpag;

    @DatabaseField
    private String desfpg;

    @DatabaseField
    private String vezfpg;

    public FormaPagamento() { super(); }

    public FormaPagamento(String forpag, String desfpg, String vezfpg) {
        this.forpag = forpag;
        this.desfpg = desfpg;
        this.vezfpg = vezfpg;
    }

    public int getId() {
        return id;
    }

    public String getDesfpg() {
        return desfpg;
    }

    public String getForpag() {
        return forpag;
    }

    public String getVezfpg() {
        return vezfpg;
    }

    public void setDesfpg(String desfpg) {
        this.desfpg = desfpg;
    }

    public void setForpag(String forpag) {
        this.forpag = forpag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVezfpg(String vezfpg) {
        this.vezfpg = vezfpg;
    }
}
