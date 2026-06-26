package br.salt.sieloja.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;







import java.sql.SQLException;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

@SuppressLint("NonConstantResourceId")
public class ConfiguracoesActivity extends BaseActivity {

    
    EditText editTextNumeroEquipamento;

    
    EditText editTextUnidade;

    
    EditText editTextIpWebService;

    
    EditText editTextIpBancoDeDados;

    
    EditText editTextNomeBancoDeDados;

    
    EditText editTextSeguranca;

    
    TextView textViewVercaoApp;

    
    TextView textVercaoWebService;

    
    TextView textViewVercaoAndroid;

    
    TextView textViewRedeWifi;

    
    TextView textVercaoApp;

    
    TextView textVercaoAndroid;

    
    TextView textNumeroDoModelo;

    
    TextView textWifi;

    
    TextView textVelocidade;

    
    TextView textIP;

    
    Spinner spinnerTypeKey;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    
    Switch switchAlterarData;

    private Configuracoes configuracoes;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
    }
    @SuppressLint("SetTextI18n")
    @AfterViews
    public void afterView(){
        int width = this.getResources().getDisplayMetrics().widthPixels/2;
        textViewVercaoApp.setWidth(width);
        textViewVercaoAndroid.setWidth(width);
        textViewRedeWifi.setWidth(width);
        textVercaoAndroid.setText(Build.VERSION.RELEASE);
        textNumeroDoModelo.setText(Build.MODEL);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typeKey, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeKey.setAdapter(adapter);

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        textWifi.setText(wifiInfo.getSSID().replace("\"", ""));
        textVelocidade.setText(wifiInfo.getLinkSpeed() + " " + WifiInfo.LINK_SPEED_UNITS);
        textIP.setText(String.valueOf(intToIp()));

        try {
            textVercaoApp.setText(String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }


        try {
            if(!usuarioController.getUsuarioLogado().getNivelDeAcesso().equalsIgnoreCase("99")){
                editTextNumeroEquipamento.setEnabled(false);
                editTextUnidade.setEnabled(false);
                editTextIpWebService.setEnabled(false);
                editTextIpBancoDeDados.setEnabled(false);
                editTextNomeBancoDeDados.setEnabled(false);
                editTextSeguranca.setEnabled(false);
            }

            configuracoes = configuracoesController.getConfiguracoes();
            editTextNumeroEquipamento.setText(configuracoes.getEquipamento());
            editTextUnidade.setText(configuracoes.getUnidadeAdm());
            editTextIpWebService.setText(configuracoes.getIpWebService());
            editTextIpBancoDeDados.setText(configuracoes.getIpBancoDeDados());
            editTextNomeBancoDeDados.setText(configuracoes.getNomeBancoDeDados());
            editTextSeguranca.setText(configuracoes.getNomeSeguranca());
            switchAlterarData.setChecked(configuracoes.isAlteraData());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Click
    public void buttonSalvar() {
        String equipamento = editTextNumeroEquipamento.getText().toString();
        String unidade = editTextUnidade.getText().toString();
        String ipWebService = editTextIpWebService.getText().toString();
        String ipBancoDeDados = editTextIpBancoDeDados.getText().toString();
        String nomeBancoDeDados = editTextNomeBancoDeDados.getText().toString();
        String nomeSeguranca = editTextSeguranca.getText().toString();
        String typeKey = spinnerTypeKey.getSelectedItem().toString();
        boolean alteraData = switchAlterarData.isChecked();

        if(equipamento.equalsIgnoreCase("") || unidade.equalsIgnoreCase("") || nomeSeguranca.equalsIgnoreCase("") ||
                ipBancoDeDados.equalsIgnoreCase("") || nomeBancoDeDados.equalsIgnoreCase("")){
            Alert.dialog(this, getString(R.string.todos_os_campos_sao_de_preenchimento_obrigatorio));
        } else {
            try {
                configuracoes.setEquipamento(equipamento);
                configuracoes.setUnidadeAdm(unidade);
                configuracoes.setIpWebService(ipWebService);
                configuracoes.setIpBancoDeDados(ipBancoDeDados);
                configuracoes.setNomeBancoDeDados(nomeBancoDeDados);
                configuracoes.setNomeSeguranca(nomeSeguranca);
                configuracoes.setTypeKey(typeKey);
                configuracoes.setAlteraData(alteraData);
                configuracoesController.salvarConfiguracoes(configuracoes);
                Alert.dialog(this, getString(R.string.salvo_com_sucesso));
            } catch (SQLException e) {
                e.printStackTrace();
                Alert.dialog(this, getString(R.string.erro_no_sql));
            } catch (Exception e) {
                e.printStackTrace();
                Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            }
        }
    }
}
