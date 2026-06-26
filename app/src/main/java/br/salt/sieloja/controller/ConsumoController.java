package br.salt.sieloja.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Consumo;
import br.salt.sieloja.bean.Item;
import br.salt.sieloja.bean.ItemConsumo;
import br.salt.sieloja.bean.Usuario;
import br.salt.sieloja.dao.DatabaseHelper;
import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.EnvioConsumo;
import br.salt.sieloja.rest.responseobject.EnvioItemConsumo;
import br.salt.sieloja.rest.responseobject.EnvioItemConsumoIns;
import br.salt.sieloja.rest.responseobject.Retorno;

@EBean
public class ConsumoController extends DatabaseManager {

    @RestService

    private static ConsumoController instance;
    Request request;

    @Bean
    ConfiguracoesController configuracoesController;

    @Bean
    ItemController itemController;

    public static synchronized ConsumoController getInstance(Context context) {
        if (instance == null) {
            instance = new ConsumoController(context.getApplicationContext());
        }
        return instance;
    }
    public ConsumoController(Context context) {
        super(context);
    }

    /**
     * Persiste ou atualiza o Consumo especificada no banco
     *
     * @param consumo
     * @throws SQLException
     */
    public void salvarConsumo(Consumo consumo) throws SQLException {
        getHelper().getConsumoDao().createOrUpdate(consumo);
    }

    /**
     * Persiste ou atualiza o ItemConsumo especificada no banco
     *
     * @param itemConsumo
     * @throws SQLException
     */
    public void salvarItemConsumo(ItemConsumo itemConsumo) throws SQLException{
        getHelper().getItemConsumoDao().createOrUpdate(itemConsumo);
    }

    /**
     * Deleta todos os Consumos e ItensConsumo que estão cadastrados no banco.
     *
     * @throws SQLException
     */
    public void deletarAll() throws SQLException{
        DeleteBuilder<Consumo, Integer> deleteBuilderConsumo = getHelper().getConsumoDao().deleteBuilder();
        deleteBuilderConsumo.delete();
        DeleteBuilder<ItemConsumo, Integer> deleteBuilderItemConsumo = getHelper().getItemConsumoDao().deleteBuilder();
        deleteBuilderItemConsumo.delete();
    }

    /**
     * Deleta todos os Consumos e ItensConsumo que estão cadastrados no banco.
     *
     * @throws SQLException
     */
    public void deletarItemConsumo(ItemConsumo itemConsumo) throws SQLException{
        getHelper().getItemConsumoDao().delete(itemConsumo);
    }

    /**
     * Retorna o Consumo que esta aberto.
     *
     * @return Consumo que esta aberto.
     * @throws SQLException
     */
    public Consumo getConsumoAberto() throws SQLException{
        List<Consumo> consumos = getHelper().getConsumoDao().queryForAll();
        if(consumos.size() > 0){
            return consumos.get(0);
        }
        return null;
    }

    /**
     * Verifica se tem algum consumo em aberto com itens já lansados.
     *
     * @return true se existir algum consumo aberto com itens já lansados; false se não existir.
     * @throws SQLException
     */
    public boolean isVerificaSeTemAlgumConsumoAberto() throws SQLException{
        List<ItemConsumo> consumos = getHelper().getItemConsumoDao().queryForAll();
        if(consumos.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * Retorna List<ItemConsumo> que estão cadastrados no banco.
     *
     * @return List<ItemConsumo> que estão cadastrados no banco.
     * @throws SQLException
     */
    public List<ItemConsumo> getAllItemConsumo() throws SQLException{
        return getHelper().getItemConsumoDao().queryForAll();
    }

    /**
     * Verifica se todos os Itens do consumo foram lansados coretamente.
     *
     * @return true se todos os Itens forão lansados coretamente; false se não forão.
     * @throws SQLException
     */
    public boolean isValidationItensLancados() throws SQLException{
        boolean validation = true;
//		PreparedQuery<ItemConsumo> query = itemConsumoDao.queryBuilder().where().eq("codigoItem", "").or().lt("quantidade", 1).prepare();
        PreparedQuery<ItemConsumo> query = getHelper().getItemConsumoDao().queryBuilder().where().eq("codigoItem", "").prepare();
        List<ItemConsumo> itensConsumo = getHelper().getItemConsumoDao().query(query);
        if(itensConsumo.size() > 0){
            validation = false;
        }
        return validation;
    }

    /**
     * Calcula o valor total referente a List<ItemConsumo> que estão cadastrados no banco.
     *
     * @return valor total referente a List<ItemConsumo> que estão cadastrados no banco.
     * @throws SQLException
     */
    public double getTotal() throws SQLException{
        double totalConsumo = 0;
        for (ItemConsumo itemConsumo : getAllItemConsumo()) {
            double qtd = itemConsumo.getQuantidade();
            //if(itemConsumo.getQtddeemb() > 0){ qtd = qtd * itemConsumo.getQtddeemb(); }
            totalConsumo = totalConsumo + (qtd * itemConsumo.getPreco());
        }
        return totalConsumo;
    }

    public void restConsumo(Consumo consumo, Usuario usuario, Date date) throws JSONException, SQLException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        EnvioConsumo envio = new EnvioConsumo();
        envio.setIpBanco(configuracoes.getIpBancoDeDados());
        envio.setNomeBanco(configuracoes.getNomeBancoDeDados());
        envio.setCodigoUsuario(usuario.getCodigo());
        envio.setNomeUsuario(usuario.getNome());
        envio.setEquipamento(getCodigo(configuracoes.getEquipamento(), 5));
        envio.setCodigoEmpresa(getCodigo(configuracoes.getEmpresa(), 2));
        envio.setCodigoLoja(getCodigo(configuracoes.getLoja(), 5));
        envio.setCodigoUnidade(getCodigo(configuracoes.getUnidadeAdm(), 2));
        envio.setMesa(getCodigo(consumo.getMesa(), 5));
        envio.setCartao(consumo.getCartao());
        envio.setUnidaeSolicitante(getCodigo(configuracoes.getUnidadeAdm(), 2));
        envio.setListItemConsumos(restItemConsumo());
        envio.setValorTotal(getTotal());
        envio.setTipoConsumo("V");
        envio.setPraza(0);
        envio.setValorDes(0);
        envio.setComissao(0);
        envio.setStatus("CONFI");
        envio.setOrigem("PEDVENDA");
        envio.setCodCliente(consumo.getCodCliente());
        envio.setForpag(consumo.getCodFormaPag());
        envio.setTippag(consumo.getCodTipoPag());
        envio.setIp(configuracoes.getIp());
        envio.setDate(date);
        request.setRootUrl(configuracoes.getIpWebService());
        Retorno retorno = request.requestVenda(envio);
        if(!retorno.isOperacaoFinalizada())
            throw new Exception(retorno.getMensagem());
    }

    public List<EnvioItemConsumo> restItemConsumo() throws SQLException, JSONException{
        List<ItemConsumo> itensConsumo = getAllItemConsumo();
        List<EnvioItemConsumo> envio = new ArrayList<EnvioItemConsumo>();
        for (int i = 0; i < itensConsumo.size(); i++) {
            ItemConsumo itemConsumo = itensConsumo.get(i);
            Item item = itemController.getItemFilterCodigo(itemConsumo.getCodigoItem());
            item.setPreco(itemConsumo.getPreco());

            double qtddeemb = 0;
            double quantidade = itemConsumo.getQuantidade();
            if(itemConsumo.getQtddeemb() > 0){
                qtddeemb   = itemConsumo.getQuantidade();
                quantidade = itemConsumo.getQuantidade()*itemConsumo.getQtddeemb();
                item.setPreco(itemConsumo.getPreco()/itemConsumo.getQtddeemb());
            }

            EnvioItemConsumo envioItemConsumo = new EnvioItemConsumo();
            envioItemConsumo.setSequencial(getCodigo(String.valueOf(i+1), 5));
            envioItemConsumo.setItem(item);
            envioItemConsumo.setQuantidade(quantidade);
            envioItemConsumo.setQtddeemb(qtddeemb);
            envioItemConsumo.setObservacao(itemConsumo.getObservacao());
            envioItemConsumo.setCodbar(itemConsumo.getCodbar());
            envioItemConsumo.setTipoConsumo("V");
            envioItemConsumo.setQuantidadeRec(0);
            envioItemConsumo.setNumeroCom(0);
            envioItemConsumo.setStatus("CONFI");
            envioItemConsumo.setVenpro("V");
            envioItemConsumo.setItemConsumoIns(new ArrayList<EnvioItemConsumoIns>());
            envio.add(envioItemConsumo);
        }

        return envio;
    }

    private String getCodigo(String codigo, int tamanho){
        for (int i = codigo.length(); i < tamanho; i++) {
            codigo = "0".concat(codigo);
        }
        return codigo;
    }
}
