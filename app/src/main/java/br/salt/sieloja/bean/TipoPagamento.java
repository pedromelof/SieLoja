package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class TipoPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String tippag;

    @DatabaseField
    private String destpg;

    @DatabaseField
    private String abrev;

    @DatabaseField
    private String tippag_nfe;

    public TipoPagamento(){ super(); }

    public TipoPagamento(String tippag, String destpg, String abrev, String tippag_nfe){
        this.tippag = tippag;
        this.destpg = destpg;
        this.abrev = abrev;
        this.tippag_nfe = tippag_nfe;
    }

    public int getId() {
        return id;
    }

    public String getAbrev() {
        return abrev;
    }

    public String getDestpg() {
        return destpg;
    }

    public String getTippag() {
        return tippag;
    }

    public String getTippag_nfe() {
        return tippag_nfe;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAbrev(String abrev) {
        this.abrev = abrev;
    }

    public void setDestpg(String destpg) {
        this.destpg = destpg;
    }

    public void setTippag(String tippag) {
        this.tippag = tippag;
    }

    public void setTippag_nfe(String tippag_nfe) {
        this.tippag_nfe = tippag_nfe;
    }
}
