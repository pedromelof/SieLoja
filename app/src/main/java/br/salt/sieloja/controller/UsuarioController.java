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
import br.salt.sieloja.bean.Usuario;
import br.salt.sieloja.dao.DatabaseHelper;
import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoUsuario;

@EBean
public class UsuarioController extends DatabaseManager {

    @RestService
    Request request;

    @Bean
    ConfiguracoesController configuracoesController;

    public UsuarioController(Context context) {
        super(context);
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
        request.setRootUrl(configuracoes.getIpWebService());
        RetornoUsuario retorno = request.requestUsuario(envio);
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
