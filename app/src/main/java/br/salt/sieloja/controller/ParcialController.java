package br.salt.sieloja.controller;

import android.content.Context;


import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;


import org.json.JSONException;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Item;
import br.salt.sieloja.bean.ItemConsumo;
import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.bean.Usuario;

import br.salt.sieloja.dao.DatabaseManager;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.rest.RequestClient;
import br.salt.sieloja.rest.responseobject.EnvioConsumo;
import br.salt.sieloja.rest.responseobject.EnvioItemConsumo;
import br.salt.sieloja.rest.responseobject.EnvioParcial;
import br.salt.sieloja.rest.responseobject.Retorno;
import br.salt.sieloja.rest.responseobject.RetornoParcial;
import br.salt.sieloja.rest.responseobject.RetornoSubgrupo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ParcialController extends DatabaseManager {

    private static ParcialController instance;


    ItemController itemController;


    ConsumoController consumoController;


    ConfiguracoesController configuracoesController;


    UsuarioController usuarioController;

    public static synchronized ParcialController getInstance(Context context) {
        if (instance == null) {
            instance = new ParcialController(context.getApplicationContext());
            instance.configuracoesController = ConfiguracoesController.getInstance(context.getApplicationContext());
            instance.consumoController = ConsumoController.getInstance(context.getApplicationContext());
            instance.itemController = ItemController.getInstance(context.getApplicationContext());

        }
        return instance;
    }

    public ParcialController(Context context) {
        super(context);
        this.usuarioController = UsuarioController.getInstance(context);
    }

    /**
     * Persiste ou atualiza o Parcial especificada no banco.
     *
     * @param parcial
     * @throws SQLException
     */
    public void salvarParcial(Parcial parcial) throws SQLException {
        getHelper().getParcialDao().createOrUpdate(parcial);
    }

    /**
     * Deleta todas as Parciais cadastradas no banco.
     *
     * @throws SQLException
     */
    public void deletarAll() throws SQLException {
        DeleteBuilder<Parcial, Integer> deleteBuilder = getHelper().getParcialDao().deleteBuilder();
        deleteBuilder.delete();
    }

    /**
     * Retorna um List<Parcial> com todas as Parcias cadastradas.
     *
     * @return List<Parcial>
     * @throws SQLException
     */
    public List<Parcial> getForAll() throws SQLException {
        PreparedQuery<Parcial> query = getHelper().getParcialDao().queryBuilder().orderBy("decItem", true).prepare();
        return getHelper().getParcialDao().query(query);
    }

    public List<Parcial> getForAllPedido() throws SQLException {
        PreparedQuery<Parcial> query = getHelper()
                .getParcialDao()
                .queryBuilder()
                .groupBy("numPed")
                .prepare();
        return getHelper().getParcialDao().query(query);
    }

    public List<Parcial> getItemPedido(String numped) throws SQLException {
        PreparedQuery<Parcial> query = getHelper().getParcialDao().queryBuilder().orderBy("decItem", true).where().eq("numPed", numped).and().ne("codItem", "TX. ADM").prepare();
        return getHelper().getParcialDao().query(query);
    }

    public List<Parcial> getItemPedidoTaxa(String numped) throws SQLException {
        PreparedQuery<Parcial> query = getHelper().getParcialDao().queryBuilder().orderBy("decItem", true).where().eq("numPed", numped).and().eq("codItem", "TX. ADM").prepare();
        return getHelper().getParcialDao().query(query);
    }

    /**
     * Calcula o valor total do consumo.
     *
     * @return valor total.
     * @throws SQLException
     */
    public double getTotal() throws SQLException {
        double totalConsumo = 0;
        PreparedQuery<Parcial> query = getHelper().getParcialDao().queryBuilder().orderBy("decItem", true).where().ne("codItem", "TX. ADM").prepare();
        List<Parcial> parciais = getHelper().getParcialDao().query(query);
        for (Parcial parcial : parciais) {
            totalConsumo = totalConsumo + parcial.getValor();
        }
        return totalConsumo;
    }

    public double getTotalTaxa() throws SQLException {
        double totalConsumo = 0;
        PreparedQuery<Parcial> query = getHelper().getParcialDao().queryBuilder().orderBy("decItem", true).where().eq("codItem", "TX. ADM").prepare();
        List<Parcial> parciais = getHelper().getParcialDao().query(query);
        for (Parcial parcial : parciais) {
            totalConsumo = totalConsumo + parcial.getValor();
        }
        return totalConsumo;
    }

    /**
     * Calcula a quantidade total de itens vendidos.
     *
     * @return valor total.
     * @throws SQLException
     */
    public double getTotalItens() throws SQLException {
        double totalItens = 0;
        for (Parcial parcial : getHelper().getParcialDao().queryForAll()) {
            totalItens = totalItens + parcial.getQtd();
        }
        return totalItens;
    }

    /**
     * Verifica se existe alguma parcial cadastreda.
     *
     * @return true se existir alguma parcial; false se não existir.
     * @throws SQLException
     */
    public boolean isVerificaSeExisteParcial() throws SQLException {
        List<Parcial> parciais = getHelper().getParcialDao().queryForAll();
        if (parciais.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Transforma o consumo que esta sendo lansado em parcial e persiste a informação.
     *
     * @throws SQLException
     */
    public void transformarConsumoEmParcial() throws SQLException {
        DeleteBuilder<Parcial, Integer> deleteBuilder = getHelper().getParcialDao().deleteBuilder();
        deleteBuilder.delete();
        for (ItemConsumo itemConsumo : consumoController.getAllItemConsumo()) {
            Item item = itemController.getItemFilterCodigo(itemConsumo.getCodigoItem());
            double valor = item.getPreco() * itemConsumo.getQuantidade();
            Parcial parcial = new Parcial(itemConsumo.getCodigoItem(), item.getDescricao(), "0", valor, itemConsumo.getQuantidade(), 0);
            getHelper().getParcialDao().createOrUpdate(parcial);
        }
    }

    /**
     * @throws SQLException
     * @throws JSONException
     */
    public void restParcial(Date date) throws SQLException, JSONException, Exception {
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Usuario usuario = usuarioController.getUsuarioLogado();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        EnvioParcial envio = new EnvioParcial();
        envio.setData(dateFormat.format(date));
        envio.setIpBanco(configuracoes.getIpBancoDeDados());
        envio.setNomeBanco(configuracoes.getNomeBancoDeDados());
        envio.setUnidade(getCodigo(configuracoes.getUnidadeAdm(), 2));
        envio.setCartao(usuario.getCodigo());
        Request request = RequestClient.getRequest(configuracoes.getIpWebService());
        Call<RetornoParcial> call = request.requestParcial(envio);
        RetornoParcial retorno = call.execute().body();
        if (retorno.isOperacaoFinalizada()) {
            DeleteBuilder<Parcial, Integer> deleteBuilder = getHelper().getParcialDao().deleteBuilder();
            deleteBuilder.delete();
            for (Parcial parcial : retorno.getParcial()) {
                getHelper().getParcialDao().createOrUpdate(parcial);
            }
        } else {
            throw new Exception(retorno.getMensagem());
        }
    }

    /**
     * @throws SQLException
     * @throws JSONException
     */
    public void restParcialImpre(Date date) throws SQLException, JSONException, Exception {
        Configuracoes configuracoes = configuracoesController.getConfiguracoes();
        Usuario usuario = usuarioController.getUsuarioLogado();
        EnvioConsumo envio = new EnvioConsumo();
        envio.setIpBanco(configuracoes.getIpBancoDeDados());
        envio.setNomeBanco(configuracoes.getNomeBancoDeDados());
        envio.setCodigoUsuario(usuario.getCodigo());
        envio.setNomeUsuario(usuario.getNome());
        envio.setEquipamento(getCodigo(configuracoes.getEquipamento(), 5));
        envio.setCodigoEmpresa(getCodigo(configuracoes.getEmpresa(), 2));
        envio.setCodigoLoja(getCodigo(configuracoes.getLoja(), 5));
        envio.setCodigoUnidade(getCodigo(configuracoes.getUnidadeAdm(), 2));
        envio.setMesa("00000");
        envio.setCartao("0000000000");
        envio.setValorTotal(0);
        envio.setUnidaeSolicitante(getCodigo(configuracoes.getUnidadeAdm(), 2));
        envio.setListItemConsumos(new ArrayList<EnvioItemConsumo>());
        envio.setTipoConsumo("V");
        envio.setPraza(0);
        envio.setValorDes(0);
        envio.setComissao(0);
        envio.setStatus("IMPRE");
        envio.setOrigem("IMPPV");
        envio.setIp(configuracoes.getIp());
        envio.setDate(date);
        Request request = RequestClient.getRequest(configuracoes.getIpWebService());
        Call<Retorno> call = request.requestImprimirParcial(envio);
        Retorno retorno = call.execute().body();
        if (!retorno.isOperacaoFinalizada()) throw new Exception(retorno.getMensagem());
    }

    /**
     * @param codigo
     * @param tamanho
     * @return
     */
    private String getCodigo(String codigo, int tamanho) {
        for (int i = codigo.length(); i < tamanho; i++) {
            codigo = "0".concat(codigo);
        }
        return codigo;
    }
}
