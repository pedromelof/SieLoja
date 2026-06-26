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
import br.salt.sieloja.bean.Idioma;
import br.salt.sieloja.bean.TipoPagamento;
import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoFormaPagamento;
import br.salt.sieloja.rest.responseobject.RetornoIdioma;
import br.salt.sieloja.rest.responseobject.RetornoSubgrupo;
import br.salt.sieloja.rest.responseobject.RetornoTipoPag;
import retrofit2.Call;


public class TabelaController extends DatabaseManager {

    @RestService

    private static TabelaController instance;
    Request request;

    
    ConfiguracoesController configuracoesController;

    public static TabelaController getInstance(Context context) {
        if (instance == null) instance = new TabelaController(context);
        return instance;
    }
    public TabelaController(Context context) {
        super(context);
    }

    /**
     * Persiste ou atualiza o Tipo Pagamento especificada no banco.
     *
     * @param tipo
     * @throws SQLException
     */
    public void salvarTipoPag(TipoPagamento tipo) throws SQLException {
        getHelper().getTipoPagDao().createOrUpdate(tipo);
    }

    /**
     * Persiste ou atualiza o Forma Pagamento especificada no banco.
     *
     * @param forma
     * @throws SQLException
     */
    public void salvarFormaPag(FormaPagamento forma) throws SQLException {
        getHelper().getFormaPagDao().createOrUpdate(forma);
    }

    /**
     * Deleta todas as Tipos de pagamento cadastradas no banco.
     *
     * @throws SQLException
     */
    public void deletarAllTipoPag() throws SQLException{
        DeleteBuilder<TipoPagamento, Integer> deleteBuilder = getHelper().getTipoPagDao().deleteBuilder();
        deleteBuilder.delete();
    }

    /**
     * Deleta todas as Forma de pagamento cadastradas no banco.
     *
     * @throws SQLException
     */
    public void deletarAllFormaPag() throws SQLException{
        DeleteBuilder<FormaPagamento, Integer> deleteBuilder = getHelper().getFormaPagDao().deleteBuilder();
        deleteBuilder.delete();
    }

    public String[] getListFormaPag() throws SQLException {
        List<FormaPagamento> formaPag = getHelper().getFormaPagDao().queryForAll();
        String[] list = new String[formaPag.size()];
        for(int i = 0; i < formaPag.size(); i++){
            FormaPagamento fp = formaPag.get(i);
            list[i] = fp.getDesfpg();
        }
        return list;
    }

    public String[] getListTipoPag() throws SQLException {
        List<TipoPagamento> tipoPag = getHelper().getTipoPagDao().queryForAll();
        String[] list = new String[tipoPag.size()];
        for(int i = 0; i < tipoPag.size(); i++){
            TipoPagamento tp = tipoPag.get(i);
            list[i] = tp.getDestpg();
        }
        return list;
    }

    public FormaPagamento getFormaPagCod(String forpag) throws SQLException {
        PreparedQuery<FormaPagamento> query = getHelper().getFormaPagDao().queryBuilder().where().eq("forpag", forpag).prepare();
        List<FormaPagamento> c = getHelper().getFormaPagDao().query(query);
        if(c.size() > 0){
            return c.get(0);
        }
        return null;
    }

    public FormaPagamento getFormaPagDes(String desfpg) throws SQLException {
        PreparedQuery<FormaPagamento> query = getHelper().getFormaPagDao().queryBuilder().where().eq("desfpg", desfpg).prepare();
        List<FormaPagamento> c = getHelper().getFormaPagDao().query(query);
        if(c.size() > 0){
            return c.get(0);
        }
        return null;
    }

    public TipoPagamento getTipoPagCod(String tippag) throws SQLException {
        PreparedQuery<TipoPagamento> query = getHelper().getTipoPagDao().queryBuilder().where().eq("tippag", tippag).prepare();
        List<TipoPagamento> c = getHelper().getTipoPagDao().query(query);
        if(c.size() > 0){
            return c.get(0);
        }
        return null;
    }

    public TipoPagamento getTipoPagDes(String destpg) throws SQLException {
        PreparedQuery<TipoPagamento> query = getHelper().getTipoPagDao().queryBuilder().where().eq("destpg", destpg).prepare();
        List<TipoPagamento> c = getHelper().getTipoPagDao().query(query);
        if(c.size() > 0){
            return c.get(0);
        }
        return null;
    }

    public void restFormaPag() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        request.setRootUrl(configuracoes.getIpWebService());
        Call<RetornoFormaPagamento> call = request.requestFormaPag(envio);
        RetornoFormaPagamento retorno = call.execute().body();
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<FormaPagamento, Integer> deleteBuilder = getHelper().getFormaPagDao().deleteBuilder();
            deleteBuilder.delete();
            for (FormaPagamento fp : retorno.getFormaPag()) {
                getHelper().getFormaPagDao().createOrUpdate(fp);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }

    public void restTipoPag() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        request.setRootUrl(configuracoes.getIpWebService());
        Call<RetornoTipoPag> call = request.requestTipoPag(envio);
        RetornoTipoPag retorno = call.execute().body();
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<TipoPagamento, Integer> deleteBuilder = getHelper().getTipoPagDao().deleteBuilder();
            deleteBuilder.delete();
            for (TipoPagamento tp : retorno.getTipoPag()) {
                getHelper().getTipoPagDao().createOrUpdate(tp);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }

}
