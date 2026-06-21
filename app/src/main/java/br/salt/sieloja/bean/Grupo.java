package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Grupo {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String codigo;

    @DatabaseField
    private String grupo;

    public Grupo() {
        super();
    }

    public Grupo(String codigo, String grupo) {
        super();
        this.codigo = codigo;
        this.grupo = grupo;
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

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}

