package br.salt.sieloja.controller;

import android.content.Context;


import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Usuario;

import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.RequestClient;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoUsuario;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UsuarioController extends DatabaseManager {

    
    

    
    ConfiguracoesController configuracoesController;

    private static UsuarioController instance;
    public UsuarioController(Context context) {
        super(context);
        this.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());
    }

    public static synchronized UsuarioController getInstance(Context context) {
        if (instance == null) {
            instance = new UsuarioController(context.getApplicationContext());
            instance.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());

        }
        return instance;
    }

    /**
     * Persiste ou atualiza o usuario especificada no banco.
     *
     * @param usuario
     * @throws SQLException
     */
    public void salvarUsuario(Usuario usuario) throws SQLException {
        getHelper().getUsuarioDao().createOrUpdate(usuario);
    }

    /**
     * Verifica se existe algum usuario logado no sistema.
     *
     * @return true se existe algum usuario no banco, falso caso contrario
     * @throws SQLException
     */
    public boolean isExisteUsuarioLogado() throws SQLException {
        PreparedQuery<Usuario> query = getHelper().getUsuarioDao().queryBuilder().where().eq("logado", true).prepare();
        if (getHelper().getUsuarioDao().query(query).size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Retorna o usuario que esta atualmente logando no sistema.
     *
     * @return usuario logado, null caso nao exista
     * @throws SQLException
     */
    public Usuario getUsuarioLogado() throws SQLException {
        PreparedQuery<Usuario> query = getHelper().getUsuarioDao().queryBuilder().where().eq("logado", true).prepare();
        List<Usuario> usuarios = getHelper().getUsuarioDao().query(query);
        if (usuarios.size() > 0) {
            return usuarios.get(0);
        }
        return null;
    }

    /**
     * Efetua o logout do aplicativo.
     *
     * @throws SQLException
     */
    public void logout() throws SQLException {
        PreparedQuery<Usuario> query = getHelper().getUsuarioDao().queryBuilder().where().eq("logado", true).prepare();
        List<Usuario> usuarios = getHelper().getUsuarioDao().query(query);
        if (usuarios.size() > 0) {
            usuarios.get(0).setLogado(false);
            salvarUsuario(usuarios.get(0));
        }
    }

    /**
     * Efetua o login do aplicativo.
     *
     * @param usuario
     * @param senha
     * @return true se o usuário e senha estiverem coretos; false se não estiver.
     * @throws SQLException
     */
    public boolean login(String usuario, String senha) throws SQLException {
        PreparedQuery<Usuario> query = getHelper().getUsuarioDao().queryBuilder()
                .where().eq("usuario", usuario)
                .and().eq("senha", senha).prepare();
        List<Usuario> usuarios = getHelper().getUsuarioDao().query(query);
        if(usuarios.size() > 0) {
            usuarios.get(0).setLogado(true);
            salvarUsuario(usuarios.get(0));
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @throws SQLException
     * @throws JSONException
     */
    public void restUsuario() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeSeguranca());
        Request request = RequestClient.getRequest(configuracoes.getIpWebService());
        Call<RetornoUsuario> call = request.requestUsuario(envio);
        RetornoUsuario retorno = call.execute().body();
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<Usuario, Integer> deleteBuilder = getHelper().getUsuarioDao().deleteBuilder();
            deleteBuilder.delete();
            Usuario usuarioADM = new Usuario("00000", "SALT", "196", "01960", false, "99");
            getHelper().getUsuarioDao().createOrUpdate(usuarioADM);
            for (Usuario usuario : retorno.getUsuarios()) {
                getHelper().getUsuarioDao().createOrUpdate(usuario);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }
}
