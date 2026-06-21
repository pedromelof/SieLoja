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
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Grupo;
import br.salt.sieloja.dao.DatabaseHelper;
import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoGrupo;

@EBean
public class GrupoController extends DatabaseManager {

    @RestService
    Request request;

    @Bean
    ConfiguracoesController configuracoesController;

    public GrupoController(Context context) {
        super(context);
    }

    /**
     * Persiste ou atualiza o Grupo especificada no banco.
     *
     * @param grupo
     * @throws SQLException
     */
    public void salvarGrupo(Grupo grupo) throws SQLException {
        getHelper().getGrupoDao().createOrUpdate(grupo);
    }


    /**
     * Retorna uma List<String> com o nome de todos os grupos.
     *
     * @return List<String> com o nome de todos os grupos.
     * @throws SQLException
     */
    public List<String> getAllGrupoArrayList() throws SQLException{
        List<String> grupos = new ArrayList<String>();
        List<Grupo> grupoAll = getHelper().getGrupoDao().queryForAll();
        for (Grupo grupo : grupoAll) {
            grupos.add(grupo.getGrupo());
        }
        return grupos;
    }

    /**
     * Retorna uma String[] com o nome de todos os grupos.
     *
     * @return String[] com o nome de todos os grupos.
     * @throws SQLException
     */
    public String[] getAllGrupo() throws SQLException{
        List<Grupo> gruposAll = getHelper().getGrupoDao().queryForAll();
        String[] grupos = new String[gruposAll.size()];
        for (int i = 0; i < gruposAll.size(); i++) {
            grupos[i] = gruposAll.get(i).getGrupo();
        }
        return grupos;
    }

    /**
     * Retorna o Grupo que tem o código especificada.
     *
     * @param codigo
     * @return Grupo que tem o código especificada.
     * @throws SQLException
     */
    public Grupo getGrupoCodigo(String codigo) throws SQLException{
        PreparedQuery<Grupo> query = getHelper().getGrupoDao().queryBuilder().where().eq("codigo", codigo).prepare();
        List<Grupo> grupos = getHelper().getGrupoDao().query(query);
        if(grupos.size() > 0){
            return grupos.get(0);
        }
        return null;
    }

    /**
     * Retorna o Grupo que tem o nome especificada.
     *
     * @param nome
     * @return Grupo que tem o nome especificada.
     * @throws SQLException
     */
    public Grupo getGrupoNome(String nome) throws SQLException{
        PreparedQuery<Grupo> query = getHelper().getGrupoDao().queryBuilder().where().eq("grupo", nome).prepare();
        List<Grupo> grupos = getHelper().getGrupoDao().query(query);
        if(grupos.size() > 0){
            return grupos.get(0);
        }
        return null;
    }

    public void restGrupo() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        request.setRootUrl(configuracoes.getIpWebService());
        RetornoGrupo retorno = request.requestGrupo(envio);
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<Grupo, Integer> deleteBuilder = getHelper().getGrupoDao().deleteBuilder();
            deleteBuilder.delete();
            for (Grupo grupo : retorno.getGrupos()) {
                getHelper().getGrupoDao().createOrUpdate(grupo);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }
}
