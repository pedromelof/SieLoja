package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Usuario {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String codigo;

    @DatabaseField
    private String nome;

    @DatabaseField
    private String usuario;

    @DatabaseField
    private String senha;

    @DatabaseField
    private boolean logado;

    @DatabaseField
    private String nivelDeAcesso;

    public Usuario() {
        super();
    }

    public Usuario(String codigo, String nome, String usuario, String senha,
                   boolean logado, String nivelDeAcesso) {
        super();
        this.codigo = codigo;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.logado = logado;
        this.nivelDeAcesso = nivelDeAcesso;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isLogado() {
        return logado;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }

    public String getNivelDeAcesso() { return nivelDeAcesso; }

    public void setNivelDeAcesso(String nivelDeAcesso) {
        this.nivelDeAcesso = nivelDeAcesso;
    }
}
