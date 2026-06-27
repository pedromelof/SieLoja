package br.salt.sieloja.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import java.sql.SQLException;
import java.util.ArrayList;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.controller.EmpresaController;
import br.salt.sieloja.controller.UsuarioController;
import br.salt.sieloja.databinding.ActivityLoginBinding;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

public class LoginActivity extends BaseActivity {
    private String ip;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        afterView();
    }
    
    private void afterView(){
        try {
            empresaController = EmpresaController.getInstance(this);
            usuarioController = UsuarioController.getInstance(this);

            ArrayList<String> unidade = empresaController.getListUnidade();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unidade);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.editTextUnidade.setAdapter(adapter);

            if(usuarioController.isExisteUsuarioLogado()){
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                Configuracoes configuracoes = configuracoesController.getConfiguracoes();
                if(isConnectedInternet(this) && isConnectedWS(configuracoes.getIpWebService())) {
                    backgroundWS();
                }
                this.ip = intToIp();
                binding.textIp.setText(ip);
            }

            binding.buttonEntrar.setOnClickListener(v -> button_entrar());

        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    private void backgroundWS() {
        new Thread(() -> {
            try {
                usuarioController.restUsuario();
                empresaController.restEmprese();
            } catch (Exception e) {
                runOnUiThread(() -> stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage()));
            }
        }).start();
    }

    public void button_entrar(){
        try {
            String usuario = binding.editTextUsuario.getText().toString();
            String senha = binding.editTextSenha.getText().toString();
            String unidade = empresaController.getCodUnidade(binding.editTextUnidade.getSelectedItem().toString());

            if(usuario.equalsIgnoreCase("") || senha.equalsIgnoreCase("")){
                Alert.dialog(this, getString(R.string.todos_os_campos_sao_de_preenchimento_obrigatorio));
            } else {
                if(usuarioController.login(usuario, senha)){
                    configuracoesController.setUnidade(unidade);
                    configuracoesController.setIP(ip);
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Alert.dialog(this, getString(R.string.usuario_senha_incoretos));
                }
            }
        } catch (SQLException e) {
            Alert.dialog(this, getString(R.string.erro_no_sql) + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }
}