package br.salt.sieloja.controller;

import android.content.Context;


import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;





import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Subgrupo;

import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoSubgrupo;
import retrofit2.Call;


public class SubgrupoController extends DatabaseManager {

    @RestService

    private static SubgrupoController instance;
    Request request;

    
    ConfiguracoesController configuracoesController;

    public static SubgrupoController getInstance(Context context) {
        if (instance == null) instance = new SubgrupoController(context);
        return instance;
    }
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
        Call<RetornoSubgrupo> call = request.requestSubgrupo(envio);
        RetornoSubgrupo retorno = call.execute().body();
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
