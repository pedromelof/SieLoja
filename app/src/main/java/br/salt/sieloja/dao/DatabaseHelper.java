package br.salt.sieloja.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.salt.sieloja.bean.Cliente;
import br.salt.sieloja.bean.CodBarra;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Consumo;
import br.salt.sieloja.bean.Empresa;
import br.salt.sieloja.bean.FormaPagamento;
import br.salt.sieloja.bean.Grupo;
import br.salt.sieloja.bean.Idioma;
import br.salt.sieloja.bean.Item;
import br.salt.sieloja.bean.ItemConsumo;
import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.bean.Subgrupo;
import br.salt.sieloja.bean.TipoPagamento;
import br.salt.sieloja.bean.Usuario;
import br.salt.sieloja.controller.ConfiguracoesController;
import br.salt.sieloja.controller.ConfiguracoesController_;
import br.salt.sieloja.controller.UsuarioController;
import br.salt.sieloja.controller.UsuarioController_;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something
    // appropriate for your app
    private static final String DATABASE_NAME = "sieMobileComsumo.db";
    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 22;

    private Dao<Cliente, Integer> clienteDao = null;
    private Dao<CodBarra, Integer> codBarrasDao = null;
    private Dao<Configuracoes, Integer> configuracoesDao = null;
    private Dao<Consumo, Integer> consumoDao = null;
    private Dao<Empresa, Integer> empresaDao = null;
    private Dao<FormaPagamento, Integer> formaPagDao = null;
    private Dao<Grupo, Integer> grupoDao = null;
    private Dao<Idioma, Integer> idiomaDao = null;
    private Dao<Item, Integer> itemDao = null;
    private Dao<ItemConsumo, Integer> itemConsumoDao = null;
    private Dao<Parcial, Integer> parcialDao = null;
    private Dao<Subgrupo, Integer> subgrupoDao = null;
    private Dao<TipoPagamento, Integer> tipoPagDao = null;
    private Dao<Usuario, Integer> usuarioDao = null;

    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Cliente.class);
            TableUtils.createTable(connectionSource, CodBarra.class);
            TableUtils.createTable(connectionSource, Consumo.class);
            TableUtils.createTable(connectionSource, Configuracoes.class);
            TableUtils.createTable(connectionSource, Empresa.class);
            TableUtils.createTable(connectionSource, FormaPagamento.class);
            TableUtils.createTable(connectionSource, Grupo.class);
            TableUtils.createTable(connectionSource, Idioma.class);
            TableUtils.createTable(connectionSource, Item.class);
            TableUtils.createTable(connectionSource, ItemConsumo.class);
            TableUtils.createTable(connectionSource, Parcial.class);
            TableUtils.createTable(connectionSource, Subgrupo.class);
            TableUtils.createTable(connectionSource, TipoPagamento.class);
            TableUtils.createTable(connectionSource, Usuario.class);

            setup();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Cliente.class, true);
            TableUtils.dropTable(connectionSource, CodBarra.class, true);
            TableUtils.dropTable(connectionSource, Consumo.class, true);
            TableUtils.dropTable(connectionSource, Configuracoes.class, true);
            TableUtils.dropTable(connectionSource, Empresa.class, true);
            TableUtils.dropTable(connectionSource, FormaPagamento.class, true);
            TableUtils.dropTable(connectionSource, Grupo.class, true);
            TableUtils.dropTable(connectionSource, Idioma.class, true);
            TableUtils.dropTable(connectionSource, Item.class, true);
            TableUtils.dropTable(connectionSource, ItemConsumo.class, true);
            TableUtils.dropTable(connectionSource, Parcial.class, true);
            TableUtils.dropTable(connectionSource, Subgrupo.class, true);
            TableUtils.dropTable(connectionSource, TipoPagamento.class, true);
            TableUtils.dropTable(connectionSource, Usuario.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public void clearUserData() throws SQLException {
        try {
            TableUtils.clearTable(connectionSource, Cliente.class);
            TableUtils.clearTable(connectionSource, CodBarra.class);
            TableUtils.clearTable(connectionSource, Consumo.class);
            TableUtils.clearTable(connectionSource, Configuracoes.class);
            TableUtils.clearTable(connectionSource, Empresa.class);
            TableUtils.clearTable(connectionSource, FormaPagamento.class);
            TableUtils.clearTable(connectionSource, Grupo.class);
            TableUtils.clearTable(connectionSource, Idioma.class);
            TableUtils.clearTable(connectionSource, Item.class);
            TableUtils.clearTable(connectionSource, ItemConsumo.class);
            TableUtils.clearTable(connectionSource, Parcial.class);
            TableUtils.clearTable(connectionSource, Subgrupo.class);
            TableUtils.clearTable(connectionSource, TipoPagamento.class);
            TableUtils.clearTable(connectionSource, Usuario.class);

            setup();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setup() throws SQLException {
        Configuracoes configuracoes = new Configuracoes("01", "00001", "01", "01",
                "http://192.168.1.114:8080/Des_SieWS/", "192.168.1.2:5433", "p_sie_biolux",
                "p_seg", "0.0.0.0", 2, Configuracoes.TYPE_KEY_NUMBER, true);
        /*Configuracoes configuracoes = new Configuracoes("01", "00001", "01", "01",
                "http:/saltinfo.no-ip.com:7782/Des_SieWS/", "192.168.1.2:5432", "p_sie_jeri_filial", "p_seg_jeriarte",
                "0.0.0.0", 2, Configuracoes.TYPE_KEY_NUMBER, true);*/
        ConfiguracoesController controller = ConfiguracoesController_.getInstance_(context);
        getConfiguracoesDao().createOrUpdate(configuracoes);

        Usuario usuario = new Usuario("00000", "SALT", "196", "01960", false, "99");
        UsuarioController usuarioController = UsuarioController_.getInstance_(context);
        getUsuarioDao().createOrUpdate(usuario);
    }

    public Dao<CodBarra, Integer> getCodBarrasDao() throws SQLException {
        if (codBarrasDao==null) codBarrasDao = getDao(CodBarra.class);
        return codBarrasDao;
    }

    public Dao<Usuario, Integer> getUsuarioDao() throws SQLException {
        if (usuarioDao==null) usuarioDao = getDao(Usuario.class);
        return usuarioDao;
    }

    public Dao<Configuracoes, Integer> getConfiguracoesDao() throws SQLException {
        if (configuracoesDao==null) configuracoesDao = getDao(Configuracoes.class);
        return configuracoesDao;
    }

    public Dao<Consumo, Integer> getConsumoDao() throws SQLException {
        if (consumoDao==null) consumoDao = getDao(Consumo.class);
        return consumoDao;
    }

    public Dao<Empresa, Integer> getEmpresaDao() throws SQLException {
        if (empresaDao==null) empresaDao = getDao(Empresa.class);
        return empresaDao;
    }

    public Dao<Grupo, Integer> getGrupoDao() throws SQLException {
        if (grupoDao==null) grupoDao = getDao(Grupo.class);
        return grupoDao;
    }

    public Dao<Idioma, Integer> getIdiomaDao() throws SQLException {
        if (idiomaDao==null) idiomaDao = getDao(Idioma.class);
        return idiomaDao;
    }

    public Dao<Item, Integer> getItemDao() throws SQLException {
        if (itemDao==null) itemDao = getDao(Item.class);
        return itemDao;
    }

    public Dao<ItemConsumo, Integer> getItemConsumoDao() throws SQLException {
        if (itemConsumoDao==null) itemConsumoDao = getDao(ItemConsumo.class);
        return itemConsumoDao;
    }

    public Dao<Parcial, Integer> getParcialDao() throws SQLException {
        if (parcialDao==null) parcialDao = getDao(Parcial.class);
        return parcialDao;
    }

    public Dao<Subgrupo, Integer> getSubgrupoDao() throws SQLException {
        if (subgrupoDao==null) subgrupoDao = getDao(Subgrupo.class);
        return subgrupoDao;
    }

    public Dao<Cliente, Integer> getClienteDao() throws SQLException {
        if (clienteDao==null) clienteDao = getDao(Cliente.class);
        return clienteDao;
    }

    public Dao<FormaPagamento, Integer> getFormaPagDao() throws SQLException {
        if (formaPagDao==null) formaPagDao = getDao(FormaPagamento.class);
        return formaPagDao;
    }

    public Dao<TipoPagamento, Integer> getTipoPagDao() throws SQLException {
        if (tipoPagDao==null) tipoPagDao = getDao(TipoPagamento.class);
        return tipoPagDao;
    }
}