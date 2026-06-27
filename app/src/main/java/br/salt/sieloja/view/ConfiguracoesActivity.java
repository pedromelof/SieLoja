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
import br.salt.sieloja.databinding.ActivityConfiguracoesBinding;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

public class ConfiguracoesActivity extends BaseActivity {
    private ActivityConfiguracoesBinding binding;
    private Configuracoes configuracoes;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        binding = ActivityConfiguracoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inicializarViews();
    }

    @SuppressLint("SetTextI18n")
    public void inicializarViews(){
        int width = this.getResources().getDisplayMetrics().widthPixels/2;
        binding.textViewVercaoApp.setWidth(width);
        binding.textViewVercaoAndroid.setWidth(width);
        binding.textViewRedeWifi.setWidth(width);
        binding.textVercaoAndroid.setText(Build.VERSION.RELEASE);
        binding.textNumeroDoModelo.setText(Build.MODEL);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typeKey, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeKey.setAdapter(adapter);

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        binding.textWifi.setText(wifiInfo.getSSID().replace("\"", ""));
        binding.textVelocidade.setText(wifiInfo.getLinkSpeed() + " " + WifiInfo.LINK_SPEED_UNITS);
        binding.textIP.setText(String.valueOf(intToIp()));

        try {
            binding.textVercaoApp.setText(String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }

        try {
            if(!usuarioController.getUsuarioLogado().getNivelDeAcesso().equalsIgnoreCase("99")){
                binding.editTextNumeroEquipamento.setEnabled(false);
                binding.editTextUnidade.setEnabled(false);
                binding.editTextIpWebService.setEnabled(false);
                binding.editTextIpBancoDeDados.setEnabled(false);
                binding.editTextNomeBancoDeDados.setEnabled(false);
                binding.editTextSeguranca.setEnabled(false);
            }

            configuracoes = configuracoesController.getConfiguracoes();
            binding.editTextNumeroEquipamento.setText(configuracoes.getEquipamento());
            binding.editTextUnidade.setText(configuracoes.getUnidadeAdm());
            binding.editTextIpWebService.setText(configuracoes.getIpWebService());
            binding.editTextIpBancoDeDados.setText(configuracoes.getIpBancoDeDados());
            binding.editTextNomeBancoDeDados.setText(configuracoes.getNomeBancoDeDados());
            binding.editTextSeguranca.setText(configuracoes.getNomeSeguranca());
            binding.switchAlterarData.setChecked(configuracoes.isAlteraData());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }

        binding.buttonSalvar.setOnClickListener(v -> buttonSalvar());
    }

    public void buttonSalvar() {
        String equipamento = binding.editTextNumeroEquipamento.getText().toString();
        String unidade = binding.editTextUnidade.getText().toString();
        String ipWebService = binding.editTextIpWebService.getText().toString();
        String ipBancoDeDados = binding.editTextIpBancoDeDados.getText().toString();
        String nomeBancoDeDados = binding.editTextNomeBancoDeDados.getText().toString();
        String nomeSeguranca = binding.editTextSeguranca.getText().toString();
        String typeKey = binding.spinnerTypeKey.getSelectedItem().toString();
        boolean alteraData = binding.switchAlterarData.isChecked();

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
