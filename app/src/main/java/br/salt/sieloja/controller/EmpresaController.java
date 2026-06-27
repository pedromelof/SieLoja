package br.salt.sieloja.controller;

import android.content.Context;


import com.j256.ormlite.stmt.DeleteBuilder;





import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Empresa;

import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.RequestClient;
import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.RetornoEmpresa;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EmpresaController extends DatabaseManager {

    
    

    
    ConfiguracoesController configuracoesController;

    private static EmpresaController instance;

    public EmpresaController(Context context) {
        super(context);
        this.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());
    }

    public static synchronized EmpresaController getInstance(Context context) {
        if (instance == null) {
            instance = new EmpresaController(context.getApplicationContext());
            instance.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Retorna a List de unidades administrativas cadastradas.
     *
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getListUnidade() throws SQLException{
        ArrayList<String> unidade = new ArrayList<String>();
        List<Empresa> empresas = getHelper().getEmpresaDao().queryBuilder().distinct().selectColumns("unidade").query();
        for (Empresa empresa : empresas) {
            unidade.add(empresa.getUnidade());
        }
        if(unidade.size() == 0){
            unidade.add("Configuração");
        }
        return unidade;
    }

    public String getCodUnidade(String unidade) throws SQLException{
        String codeUnidade = "";
        if(unidade.equalsIgnoreCase("Configuração")){
            codeUnidade = "01";
        } else {
            List<Empresa> empresas = getHelper().getEmpresaDao().queryForAll();
            for (Empresa empresa : empresas) {
                if(unidade.equalsIgnoreCase(empresa.getUnidade())){
                    codeUnidade = empresa.getCodUnidade();
                }
            }
        }
        return codeUnidade;
    }

    /**
     *
     * @throws SQLException
     * @throws JSONException
     */
    public void restEmprese() throws SQLException, JSONException, Exception{
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Envio envio = new Envio(configuracoes.getIpBancoDeDados(), configuracoes.getNomeBancoDeDados());
        Request request = RequestClient.getRequest(configuracoes.getIpWebService());
        Call<RetornoEmpresa> call = request.requestEmpresa(envio);
        RetornoEmpresa retorno = call.execute().body();
        if(retorno.isOperacaoFinalizada()){
            DeleteBuilder<Empresa, Integer> deleteBuilder = getHelper().getEmpresaDao().deleteBuilder();
            deleteBuilder.delete();
            for (Empresa empresa : retorno.getEmpresas()) {
                getHelper().getEmpresaDao().createOrUpdate(empresa);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }
}
