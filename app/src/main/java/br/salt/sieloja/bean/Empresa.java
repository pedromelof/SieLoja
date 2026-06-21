package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String codEmpresa;

    @DatabaseField
    private String fantasia;

    @DatabaseField
    private String razaoSocial;

    @DatabaseField
    private String codLoja;

    @DatabaseField
    private String loja;

    @DatabaseField
    private String codUnidade;

    @DatabaseField
    private String unidade;

    public Empresa() {
        super();
    }

    public Empresa(String codEmpresa, String fantasia,
                   String razaoSocial, String codLoja, String loja, String codUnidade,
                   String unidade) {
        super();
        this.codEmpresa = codEmpresa;
        this.fantasia = fantasia;
        this.razaoSocial = razaoSocial;
        this.codLoja = codLoja;
        this.loja = loja;
        this.codUnidade = codUnidade;
        this.unidade = unidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCodLoja() {
        return codLoja;
    }

    public void setCodLoja(String codLoja) {
        this.codLoja = codLoja;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public String getCodUnidade() {
        return codUnidade;
    }

    public void setCodUnidade(String codUnidade) {
        this.codUnidade = codUnidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}

