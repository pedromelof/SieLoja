package br.salt.sieloja.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.Calendar;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

public class MenuActivity extends BaseActivity {

    @RestService
    
    Request request;

    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    
    final OnClickListener onClickListenerLogaut = new OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            logaut();
        }
    };

    final OnClickListener onClickListenerParcial = new OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            parcial();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Click
    public void button_consumo(){
        try {
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            if(configuracoes.getTypeKey().equalsIgnoreCase(Configuracoes.TYPE_KEY_NUMBER)){
                ConsumoNumberActivity_.intent(this).start();

            } else {
                ConsumoTextActivity_.intent(this).start();
            }
        } catch (SQLException e) {
            Alert.dialog(this, getString(R.string.erro_no_sql) + e.getMessage());
        }
    }

    @Click
    public void button_parcial(){
        View view;
        view = (View) LayoutInflater.from(this).inflate(R.layout.dialog_data, null);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        Alert.dialogPersonalizado(this, onClickListenerParcial, view);
    }

    @Click
    public void button_cardapio(){
        CardapioActivity_.intent(this).start();
    }

    @Click
    public void button_configuracoes(){ ConfiguracoesActivity_.intent(this).start(); }

    @Click
    public void button_sincronizar(){
        try {
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            if(isConnectedInternet(this) && isConnectedWS(configuracoes.getIpWebService())){
                sincronizar();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        }
    }

    @Click
    public void button_sair(){
        try {
            if(consumoController.isVerificaSeTemAlgumConsumoAberto()){
                Alert.dialogValidation(this, getString(R.string.confirmar_sair_sistema_com_venda_aberto), onClickListenerLogaut);
            } else {
                Alert.dialogValidation(this, getString(R.string.confirmar_sair_sistema), onClickListenerLogaut);
            }
        } catch (SQLException e) {
            Alert.dialog(this, getString(R.string.erro_no_sql) + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Background
    public void sincronizar(){
        startProgress();
        try {
            grupoController.restGrupo();
            subgrupoController.restSubgrupo();
            idiomaController.restIdioma();
            itemController.restItem();
            codBarraController.restCodBarra();
            tabelaController.restFormaPag();
            tabelaController.restTipoPag();
            clienteController.restCliente();
            stopProgress(getString(R.string.sincronizado_com_sucesso));
        } catch (SQLException e) {
            stopProgress(getString(R.string.erro_no_sql) + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Background
    public void procutandoParcial(){
        startProgress();
        try {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            parcialController.restParcial(calendar.getTime());
            if(parcialController.isVerificaSeExisteParcial()){
                ParcialActivity_.intent(this).data(calendar.getTime()).start();
                stopProgress();
            } else {
                stopProgress(getString(R.string.nenhuma_venda_foi_encontrado));
            }
        } catch (JSONException e) {
            stopProgress(getString(R.string.erro_no_json) + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            stopProgress(getString(R.string.erro_no_sql) + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    private void parcial(){
        try {
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            if(isConnectedInternet(this) && isConnectedWS(configuracoes.getIpWebService())){
                procutandoParcial();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        }
    }

    private void logaut() {
        try {
            consumoController.deletarAll();
            usuarioController.logout();
            LoginActivity_.intent(this)
                    .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .start();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }
}