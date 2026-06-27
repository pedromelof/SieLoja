package br.salt.sieloja.controller;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;




import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Cliente;
import br.salt.sieloja.bean.CodBarra;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Grupo;
import br.salt.sieloja.bean.Item;
import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoCliente;
import br.salt.sieloja.rest.responseobject.RetornoCodBarra;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CodBarraController extends DatabaseManager {

    private static CodBarraController instance;
    Request request;

    
    ConfiguracoesController configuracoesController;

    public static synchronized CodBarraController getInstance(Context context) {
        if (instance == null) {
            instance = new CodBarraController(context.getApplicationContext());
            instance.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());

            instance.request = new Retrofit.Builder()
                    .baseUrl("http://192.168.3.6:7781/SieWS/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Request.class);
        }
        return instance;
    }
    public CodBarraController(Context context) { super(context); }

    public List<CodBarra> getListCodBarra(String codItem) throws SQLException{
        PreparedQuery<CodBarra> query = getHelper().getCodBarrasDao().queryBuilder().where().eq("item", codItem).prepare();
        return getHelper().getCodBarrasDao().query(query);
    }

    public String[] getListCodBarra(List<CodBarra> codBarras) throws SQLException{
        String[] codBarra = new String[codBarras.size()];
        for (int i = 0; i < codBarras.size(); i++) {
            CodBarra c = codBarras.get(i);
            codBarra[i] = c.getCodbar() + " - " + c.getUnid() + " " + c.getQtde();
        }
        return codBarra;
    }

    public CodBarra getCodBarra(String codBarra) throws SQLException{
        PreparedQuery<CodBarra> query = getHelper().getCodBarrasDao().queryBuilder().where().eq("codbar", codBarra).prepare();
        List<CodBarra> cb = getHelper().getCodBarrasDao().query(query);
        if (cb.size() > 0) { return cb.get(0); }
        return null;
    }

    public void restCodBarra() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        
        Call<RetornoCodBarra> call = request.requestCodBarra(envio);
        RetornoCodBarra retorno = call.execute().body();
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<CodBarra, Integer> deleteBuilder = getHelper().getCodBarrasDao().deleteBuilder();
            deleteBuilder.delete();
            for (CodBarra codBarra : retorno.getCodBarras()) {
                getHelper().getCodBarrasDao().createOrUpdate(codBarra);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }
}
