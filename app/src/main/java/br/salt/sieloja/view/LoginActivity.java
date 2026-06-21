package br.salt.sieloja.view;

import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

@WindowFeature({ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById
    Spinner editText_unidade;

    @ViewById
    EditText editText_usuario;

    @ViewById
    EditText editText_senha;

    @ViewById
    TextView text_ip;

    @RestService
    Request request;

    private String ip;

    @AfterViews
    void afterView(){
        try {
            ArrayList<String> unidade = empresaController.getListUnidade();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unidade);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editText_unidade.setAdapter(adapter);

            if(usuarioController.isExisteUsuarioLogado()){
                MenuActivity_.intent(this).start();
            } else {
                Configuracoes configuracoes = configuracoesController.getConfiguracoes();
                if(isConnectedInternet(this) && isConnectedWS(configuracoes.getIpWebService())) {
                    bachgroundWS();
                }
                this.ip = intToIp();
                text_ip.setText(ip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Background
    public void bachgroundWS(){
        try {
            usuarioController.restUsuario();
            empresaController.restEmprese();
        } catch (SQLException e) {
            stopProgress(getString(R.string.erro_no_sql) + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            stopProgress(getString(R.string.erro_no_json) + e.getMessage());
            e.printStackTrace();
        }  catch (Exception e) {
            stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Click
    public void button_entrar(){
        try {
            String usuario = editText_usuario.getText().toString();
            String senha = editText_senha.getText().toString();
            String unidade = empresaController.getCodUnidade(editText_unidade.getSelectedItem().toString());

            if(usuario.equalsIgnoreCase("") || senha.equalsIgnoreCase("")){
                Alert.dialog(this, getString(R.string.todos_os_campos_sao_de_preenchimento_obrigatorio));
            } else {
                if(usuarioController.login(usuario, senha)){
                    configuracoesController.setUnidade(unidade);
                    configuracoesController.setIP(ip);
                    MenuActivity_.intent(this).start();
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