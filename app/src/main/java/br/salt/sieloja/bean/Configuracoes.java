package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Configuracoes {

    public static final String TYPE_KEY_NUMBER = "Numerico";
    public static final String TYPE_KEY_TEXT = "Texto";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String empresa;

    @DatabaseField
    private String loja;

    @DatabaseField
    private String unidadeAdm;

    @DatabaseField
    private String equipamento;

    @DatabaseField
    private String ipWebService;

    @DatabaseField
    private String ipBancoDeDados;

    @DatabaseField
    private String nomeBancoDeDados;

    @DatabaseField
    private String nomeSeguranca;

    @DatabaseField
    private String ip;

    @DatabaseField
    private String typeKey;

    @DatabaseField
    private boolean alteraData;

    public Configuracoes() {
        super();
    }

    public Configuracoes(String empresa, String unidadeAdm, String loja,
                         String equipamento, String ipWebService, String ipBancoDeDados,
                         String nomeBancoDeDados, String nomeSeguranca, String ip,
                         int tipo, String typeKey, boolean alteraData) {
        super();
        this.empresa = empresa;
        this.loja = loja;
        this.unidadeAdm = unidadeAdm;
        this.equipamento = equipamento;
        this.ipWebService = ipWebService;
        this.ipBancoDeDados = ipBancoDeDados;
        this.nomeBancoDeDados = nomeBancoDeDados;
        this.nomeSeguranca = nomeSeguranca;
        this.ip = ip;
        this.typeKey = typeKey;
        this.alteraData = alteraData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public String getUnidadeAdm() {
        return unidadeAdm;
    }

    public void setUnidadeAdm(String unidade_adm) {
        this.unidadeAdm = unidade_adm;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getIpBancoDeDados() {
        return ipBancoDeDados;
    }

    public void setIpBancoDeDados(String ipBancoDeDados) {
        this.ipBancoDeDados = ipBancoDeDados;
    }

    public String getNomeBancoDeDados() {
        return nomeBancoDeDados;
    }

    public void setNomeBancoDeDados(String nomeBancoDeDados) {
        this.nomeBancoDeDados = nomeBancoDeDados;
    }

    public String getNomeSeguranca() {
        return nomeSeguranca;
    }

    public void setNomeSeguranca(String nomeSeguranca) {
        this.nomeSeguranca = nomeSeguranca;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getIpWebService() {
        return ipWebService;
    }

    public void setIpWebService(String ipWebService) {
        this.ipWebService = ipWebService;
    }

    public boolean isAlteraData() {
        return alteraData;
    }

    public void setAlteraData(boolean alteraData) {
        this.alteraData = alteraData;
    }
}

