package br.salt.sieloja.controller;

import android.content.Context;


import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;





import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Grupo;

import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoGrupo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GrupoController extends DatabaseManager {

    private static GrupoController instance;
    
    Request request;

    
    ConfiguracoesController configuracoesController;

    public static synchronized GrupoController getInstance(Context context) {
        if (instance == null) {
            instance = new GrupoController(context.getApplicationContext());
            instance.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());

            instance.request = new Retrofit.Builder()
                    .baseUrl("http://192.168.3.6:7781/SieWS/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Request.class);
        }
        return instance;
    }
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
        
        Call<RetornoGrupo> call = request.requestGrupo(envio);
        RetornoGrupo retorno = call.execute().body();
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
