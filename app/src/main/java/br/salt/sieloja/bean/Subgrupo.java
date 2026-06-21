package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Subgrupo {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String codigo;

    @DatabaseField
    private String codigoGrupo;

    @DatabaseField
    private String subgrupo;

    public Subgrupo() {
        super();
    }

    public Subgrupo(String codigo, String codigoGrupo, String subgrupo) {
        super();
        this.codigo = codigo;
        this.codigoGrupo = codigoGrupo;
        this.subgrupo = subgrupo;
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

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }
}

