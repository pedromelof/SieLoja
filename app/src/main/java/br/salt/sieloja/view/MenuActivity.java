package br.salt.sieloja.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;



import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;

import java.sql.SQLException;
import java.util.Calendar;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

@WindowFeature({ Window.FEATURE_NO_TITLE })
public class MenuActivity extends BaseActivity {

    @RestService
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    
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
                Intent intent = new Intent(this, ConsumoNumberActivity.class);
                startActivity(intent);

            } else {
                Intent intent = new Intent(this, ConsumoTextActivity.class);
                startActivity(intent);
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
        Intent intent = new Intent(this, CardapioActivity.class);
        startActivity(intent);
    }

    @Click
    public void button_configuracoes(){
        Intent intent = new Intent(this, ConfiguracoesActivity.class);
        startActivity(intent); }

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
                Intent intent = new Intent(this, ParcialActivity.class);
                startActivity(intent);
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
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }
}