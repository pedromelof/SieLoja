package br.salt.sieloja.controller;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Cliente;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.FormaPagamento;
import br.salt.sieloja.bean.Grupo;
import br.salt.sieloja.bean.TipoPagamento;
import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoCliente;
import br.salt.sieloja.rest.responseobject.RetornoFormaPagamento;
import br.salt.sieloja.rest.responseobject.RetornoSubgrupo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ClienteController extends DatabaseManager {

    private static ClienteController instance;
    Request request;

    
    ConfiguracoesController configuracoesController;

    public static synchronized ClienteController getInstance(Context context) {
            if (instance == null) {
                instance = new ClienteController(context.getApplicationContext());
                instance.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());

                instance.request = new Retrofit.Builder()
                        .baseUrl("http://192.168.3.6:7781/SieWS/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(Request.class);
            }
            return instance;
    }
    public ClienteController(Context context) {
        super(context);
    }

    /**
     * Persiste ou atualiza o Cliente especificada no banco.
     *
     * @param cliente
     * @throws SQLException
     */
    public void salvar(Cliente cliente) throws SQLException {
        getHelper().getClienteDao().createOrUpdate(cliente);
    }

    /**
     * Deleta todas as clientes cadastradas no banco.
     *
     * @throws SQLException
     */
    public void deletarAll() throws SQLException{
        DeleteBuilder<Cliente, Integer> deleteBuilder = getHelper().getClienteDao().deleteBuilder();
        deleteBuilder.delete();
    }

    public Cliente getClienteCod(String codigo) throws SQLException {
        PreparedQuery<Cliente> query = getHelper().getClienteDao().queryBuilder().where().eq("codigo", codigo).prepare();
        List<Cliente> c = getHelper().getClienteDao().query(query);
        if(c.size() > 0){
            return c.get(0);
        }
        return null;
    }

    public Cliente getClienteNom(String nome) throws SQLException {
        PreparedQuery<Cliente> query = getHelper().getClienteDao().queryBuilder().where().eq("nome", nome).prepare();
        List<Cliente> c = getHelper().getClienteDao().query(query);
        if(c.size() > 0){
            return c.get(0);
        }
        return null;
    }

    public String[] getList() throws SQLException {
        PreparedQuery<Cliente> query = getHelper().getClienteDao().queryBuilder().orderBy("nome", true).prepare();
        List<Cliente> clientes = getHelper().getClienteDao().query(query);
        String[] list = new String[clientes.size()];
        for(int i = 0; i < clientes.size(); i++){
            Cliente cliente = clientes.get(i);
            list[i] = cliente.getNome();
        }
        return list;
    }

    public void restCliente() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        Call<RetornoCliente> call = request.requestCliente(envio);
        RetornoCliente retorno = call.execute().body();
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<Cliente, Integer> deleteBuilder = getHelper().getClienteDao().deleteBuilder();
            deleteBuilder.delete();
            for (Cliente cliente : retorno.getClientes()) {
                getHelper().getClienteDao().createOrUpdate(cliente);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }
}
