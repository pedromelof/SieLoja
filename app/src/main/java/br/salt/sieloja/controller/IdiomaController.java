package br.salt.sieloja.controller;

import android.content.Context;


import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;





import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Idioma;

import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoIdioma;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class IdiomaController extends DatabaseManager {

    private static IdiomaController instance;
    
    Request request;

    ConfiguracoesController configuracoesController;

    public static synchronized IdiomaController getInstance(Context context) {
        if (instance == null) {
            instance = new IdiomaController(context.getApplicationContext());
            instance.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());

            instance.request = new Retrofit.Builder()
                    .baseUrl("http://192.168.3.6:7781/SieWS/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Request.class);
        }
        return instance;
    }
    public IdiomaController(Context context) {
        super(context);
    }

    /**
     * Persiste ou atualiza o Idioma especificada no banco.
     *
     * @param idioma
     * @throws SQLException
     */
    public void salvarIdioma(Idioma idioma) throws SQLException {
        getHelper().getIdiomaDao().createOrUpdate(idioma);
    }

    /**
     * Deleta todas as Idioma cadastradas no banco.
     *
     * @throws SQLException
     */
    public void deletarAll() throws SQLException{
        DeleteBuilder<Idioma, Integer> deleteBuilder = getHelper().getIdiomaDao().deleteBuilder();
        deleteBuilder.delete();
    }

    /**
     * Retorna o Idioma que tem o código do item e o código do idioma especificada.
     *
     * @param codigoItem
     * @param codigoIdioma
     * @return Idioma que tem o código do item e o código do idioma especificada.
     * @throws SQLException
     */
    public Idioma getIdioma (String codigoItem, String codigoIdioma) throws SQLException{
        PreparedQuery<Idioma> query = getHelper().getIdiomaDao().queryBuilder()
                .where().eq("codigoItem", codigoItem)
                .and().eq("codigoIdioma", codigoIdioma).prepare();
        List<Idioma> idiomas = getHelper().getIdiomaDao().query(query);
        if(idiomas.size() > 0){
            return idiomas.get(0);
        }
        return null;
    }

    /**
     *
     *
     * @throws SQLException
     * @throws JSONException
     */
    public void restIdioma() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        
        Call<RetornoIdioma> call = request.requestIdioma(envio);
        RetornoIdioma retorno = call.execute().body();
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<Idioma, Integer> deleteBuilder = getHelper().getIdiomaDao().deleteBuilder();
            deleteBuilder.delete();
            for (Idioma idioma : retorno.getIdioma()) {
                getHelper().getIdiomaDao().createOrUpdate(idioma);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }
}
