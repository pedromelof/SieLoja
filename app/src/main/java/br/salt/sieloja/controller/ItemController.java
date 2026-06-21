package br.salt.sieloja.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Grupo;
import br.salt.sieloja.bean.Item;
import br.salt.sieloja.dao.DatabaseHelper;
import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoItem;
import br.salt.sieloja.view.util.BaseActivity;

@EBean
public class ItemController extends DatabaseManager {

    @Bean
    GrupoController grupoController;

    @Bean
    ConfiguracoesController configuracoesController;

    @RestService
    Request request;

    public ItemController(Context context) {
        super(context);
    }

    /**
     * Persiste ou atualiza o Item especificada no banco.
     *
     * @param item
     * @throws SQLException
     */
    public void salvarParcial(Item item) throws SQLException {
        getHelper().getItemDao().createOrUpdate(item);
    }

    /**
     * Retornat o Item filtrando pelo código.
     *
     * @param codigo
     * @return Item filtrando pelo código.
     * @throws SQLException
     */
    public Item getItemFilterCodigo(String codigo) throws SQLException{
        PreparedQuery<Item> query = getHelper().getItemDao().queryBuilder().where().eq("codigo", codigo).prepare();
        List<Item> itens = getHelper().getItemDao().query(query);
        if(itens.size() > 0){
            return itens.get(0);
        }
        return null;
    }

    /**
     * Retornat o Item filtrando pelo nome.
     *
     * @param nome
     * @return Item filtrando pelo nome
     * @throws SQLException
     */
    public Item getItemFilterNome(String nome) throws SQLException{
        PreparedQuery<Item> query = getHelper().getItemDao().queryBuilder().where().eq("nome", nome).prepare();
        List<Item> itens = getHelper().getItemDao().query(query);
        if(itens.size() > 0){
            return itens.get(0);
        }
        return null;
    }

    /**
     * Retornat o Item filtrando pelo toString (código - nome).
     *
     * @param toString
     * @return Item filtrando pelo toString (código - nome).
     * @throws SQLException
     */
    public Item getItemFilterToString(String toString) throws SQLException{
        int index = toString.indexOf(" - ");
        String codigo = toString.substring(0, index);
        codigo = BaseActivity.getCodigo(codigo, 10);
        return getItemFilterCodigo(codigo);
    }

    /**
     * Retorna List<Item> ordenados pelo nome e filtrado pelo grupo.
     *
     * @param nomeGrupo
     * @return List<Item> ordenados pelo nome e filtrado pelo grupo.
     * @throws SQLException
     */
    public List<Item> getItensFilterGrupos(String nomeGrupo) throws SQLException{
        Grupo grupo = grupoController.getGrupoNome(nomeGrupo);
        PreparedQuery<Item> query = getHelper().getItemDao().queryBuilder().orderBy("nome", true).where().eq("codigoGrupo", grupo.getCodigo()).prepare();
        List<Item> itens = getHelper().getItemDao().query(query);
        return itens;
    }

    /**
     * Retorna String[] com o nome de todos os itens ordenados pelo nome e filtrado pelo grupo.
     *
     * @param nomeGrupo
     * @return String[] com o nome de todos os itens ordenados pelo nome e filtrado pelo grupo.
     * @throws SQLException
     */
    public String[] getItensFilterGrupo(String nomeGrupo) throws SQLException{
        Grupo grupo = grupoController.getGrupoNome(nomeGrupo);
        PreparedQuery<Item> query = getHelper().getItemDao().queryBuilder().orderBy("nome", true).where().eq("codigoGrupo", grupo.getCodigo()).prepare();
        List<Item> itensFilterGrupo = getHelper().getItemDao().query(query);
        String[] itens = new String[itensFilterGrupo.size()];
        for (int i = 0; i < itensFilterGrupo.size(); i++) {
            itens[i] = itensFilterGrupo.get(i).getNome();
        }
        return itens;
    }

    /**
     * Retorna String[] com o nome de todos os itens ordenados pelo nome.
     *
     * @return String[] com o nome de todos os itens ordenados pelo nome.
     * @throws SQLException
     */
    public String[] getAllItem() throws SQLException{
        QueryBuilder<Item, Integer> queryBuilder = getHelper().getItemDao().queryBuilder().orderBy("codigo", true);
        List<Item> itens = queryBuilder.query();
        String[] listItens = new String[itens.size()];
        for (int i = 0; i < listItens.length; i++) {
            listItens[i] = itens.get(i).toString();
        }
        return listItens;
    }

    /**
     *
     *
     * @throws SQLException
     * @throws JSONException
     */
    public void restItem() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        request.setRootUrl(configuracoes.getIpWebService());
        RetornoItem retorno = request.requestItem(envio);
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<Item, Integer> deleteBuilder = getHelper().getItemDao().deleteBuilder();
            deleteBuilder.delete();
            for (Item item : retorno.getItens()) {
                getHelper().getItemDao().createOrUpdate(item);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }
}