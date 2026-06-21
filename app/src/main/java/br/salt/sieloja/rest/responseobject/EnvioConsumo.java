package br.salt.sieloja.rest.responseobject;

import java.util.Date;
import java.util.List;

public class EnvioConsumo extends Envio{

    private String codigoUsuario;
    private String nomeUsuario;
    private String equipamento;
    private String codigoEmpresa;
    private String codigoLoja;
    private String codigoUnidade;
    private String mesa;
    private String cartao;
    private double valorTotal;
    private String unidaeSolicitante;
    private List<EnvioItemConsumo> listItemConsumos;
    private String tipoConsumo;
    private int praza;
    private int valorDes;
    private int comissao;
    private String status;
    private String origem;
    private String ip;
    private Date date;
    private String codCliente;
    private String forpag;
    private String tippag;

    public EnvioConsumo() {
        super();
    }
    public EnvioConsumo(String codigoUsuario, String nomeUsuario,
                        String equipamento, String codigoEmpresa, String codigoLoja,
                        String codigoUnidade, String mesa, String cartao,
                        double valorTotal, String unidaeSolicitante,
                        List<EnvioItemConsumo> listItemConsumos, String tipoConsumo,
                        int praza, int valorDes, int comissao, String status,
                        String origem, String ip, Date date, String codCliente,
                        String ipBanco, String nomeBanco, String forpag, String tippag) {
        super(ipBanco, nomeBanco);
        this.codigoUsuario = codigoUsuario;
        this.nomeUsuario = nomeUsuario;
        this.equipamento = equipamento;
        this.codigoEmpresa = codigoEmpresa;
        this.codigoLoja = codigoLoja;
        this.codigoUnidade = codigoUnidade;
        this.mesa = mesa;
        this.cartao = cartao;
        this.valorTotal = valorTotal;
        this.unidaeSolicitante = unidaeSolicitante;
        this.listItemConsumos = listItemConsumos;
        this.tipoConsumo = tipoConsumo;
        this.praza = praza;
        this.valorDes = valorDes;
        this.comissao = comissao;
        this.status = status;
        this.origem = origem;
        this.ip = ip;
        this.date = date;
        this.codCliente = codCliente;
        this.forpag = forpag;
        this.tippag = tippag;
    }

    public String getTippag() {
        return tippag;
    }

    public String getForpag() {
        return forpag;
    }

    public void setTippag(String tippag) {
        this.tippag = tippag;
    }

    public void setForpag(String forpag) {
        this.forpag = forpag;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }
    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    public String getEquipamento() {
        return equipamento;
    }
    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }
    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }
    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }
    public String getCodigoLoja() {
        return codigoLoja;
    }
    public void setCodigoLoja(String codigoLoja) {
        this.codigoLoja = codigoLoja;
    }
    public String getCodigoUnidade() {
        return codigoUnidade;
    }
    public void setCodigoUnidade(String codigoUnidade) {
        this.codigoUnidade = codigoUnidade;
    }
    public String getMesa() {
        return mesa;
    }
    public void setMesa(String mesa) {
        this.mesa = mesa;
    }
    public String getCartao() {
        return cartao;
    }
    public void setCartao(String cartao) {
        this.cartao = cartao;
    }
    public double getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    public String getUnidaeSolicitante() {
        return unidaeSolicitante;
    }
    public void setUnidaeSolicitante(String unidaeSolicitante) {
        this.unidaeSolicitante = unidaeSolicitante;
    }
    public List<EnvioItemConsumo> getListItemConsumos() {
        return listItemConsumos;
    }
    public void setListItemConsumos(List<EnvioItemConsumo> listItemConsumos) {
        this.listItemConsumos = listItemConsumos;
    }
    public String getTipoConsumo() {
        return tipoConsumo;
    }
    public void setTipoConsumo(String tipoConsumo) {
        this.tipoConsumo = tipoConsumo;
    }
    public int getPraza() {
        return praza;
    }
    public void setPraza(int praza) {
        this.praza = praza;
    }
    public int getValorDes() {
        return valorDes;
    }
    public void setValorDes(int valorDes) {
        this.valorDes = valorDes;
    }
    public int getComissao() {
        return comissao;
    }
    public void setComissao(int comissao) {
        this.comissao = comissao;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getOrigem() {
        return origem;
    }
    public void setOrigem(String origem) {
        this.origem = origem;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getCodCliente() {
        return codCliente;
    }
    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }
}
