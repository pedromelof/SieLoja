package br.salt.sieloja.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.dao.DatabaseHelper;
import br.salt.sieloja.dao.DatabaseManager;

@EBean
public class ConfiguracoesController extends DatabaseManager {

    public ConfiguracoesController(Context context) {
        super(context);
    }

    /**
     * Persiste ou atualiza a Configurações especificada no banco
     *
     * @param configuracoes
     * @throws SQLException
     */
    public void salvarConfiguracoes(Configuracoes configuracoes) throws SQLException{
        getHelper().getConfiguracoesDao().createOrUpdate(configuracoes);
    }

    /**
     * Altera o numero do equipamento.
     *
     * @param equipamento
     * @throws SQLException
     */
    public void setEquipamento(String equipamento) throws SQLException{
        List<Configuracoes> empresas = getHelper().getConfiguracoesDao().queryForAll();
        if(empresas.size() > 0){
            empresas.get(0).setEquipamento(equipamento);
            salvarConfiguracoes(empresas.get(0));
        }
    }

    /**
     * Altera a Unidade Administrativa.
     *
     * @param unidade
     * @throws SQLException
     */
    public void setUnidade(String unidade) throws SQLException{
        List<Configuracoes> empresas = getHelper().getConfiguracoesDao().queryForAll();
        if(empresas.size() > 0){
            empresas.get(0).setUnidadeAdm(unidade);
            salvarConfiguracoes(empresas.get(0));
        }
    }

    /**
     * Altera o IP.
     *
     * @param ip
     * @throws SQLException
     */
    public void setIP(String ip) throws SQLException{
        List<Configuracoes> empresas = getHelper().getConfiguracoesDao().queryForAll();
        if(empresas.size() > 0){
            empresas.get(0).setIp(ip);
            salvarConfiguracoes(empresas.get(0));
        }
    }

    /**
     * Retorna a Configuracoes que esta cadastrada no banco.
     *
     * @return Configuracoes que esta cadastrada no banco.
     * @throws SQLException
     */
    public Configuracoes getConfiguracoes() throws SQLException {
        return getHelper().getConfiguracoesDao().queryForAll().get(0);
    }
}
