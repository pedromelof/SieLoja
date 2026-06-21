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
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Subgrupo;
import br.salt.sieloja.dao.DatabaseHelper;
import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoSubgrupo;

@EBean
public class SubgrupoController extends DatabaseManager {

    @RestService
    Request request;

    @Bean
    ConfiguracoesController configuracoesController;

    public SubgrupoController(Context context) {
        super(context);
    }

    /**
     * Persiste ou atualiza o Subgrupo especificada no banco.
     *
     * @param subgrupo
     * @throws SQLException
     */
    public void salvarSubgrupo(Subgrupo subgrupo) throws SQLException {
        getHelper().getSubgrupoDao().createOrUpdate(subgrupo);
    }

    /**
     * Deleta todas as Subgrupo cadastradas no banco.
     *
     * @throws SQLException
     */
    public void deletarAll() throws SQLException{
        DeleteBuilder<Subgrupo, Integer> deleteBuilder = getHelper().getSubgrupoDao().deleteBuilder();
        deleteBuilder.delete();
    }

    /**
     * Retorna o Subgrupo que tem o código especificada.
     *
     * @param codigo
     * @return Subgrupo que tem o código especificada.
     * @throws SQLException
     */
    public Subgrupo getSubgrupo(String codigo) throws SQLException{
        PreparedQuery<Subgrupo> query = getHelper().getSubgrupoDao().queryBuilder().where().eq("codigo", codigo).prepare();
        List<Subgrupo> subgrupos = getHelper().getSubgrupoDao().query(query);
        if(subgrupos.size() > 0){
            return subgrupos.get(0);
        }
        return null;
    }

    /**
     *
     *
     * @throws SQLException
     * @throws JSONException
     */
    public void restSubgrupo() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        request.setRootUrl(configuracoes.getIpWebService());
        RetornoSubgrupo retorno = request.requestSubgrupo(envio);
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<Subgrupo, Integer> deleteBuilder = getHelper().getSubgrupoDao().deleteBuilder();
            deleteBuilder.delete();
            for (Subgrupo subgrupo : retorno.getSubgrupos()) {
                getHelper().getSubgrupoDao().createOrUpdate(subgrupo);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }
}
