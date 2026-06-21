package br.salt.sieloja.view.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Cliente;
import br.salt.sieloja.controller.ClienteController;
import br.salt.sieloja.controller.CodBarraController;
import br.salt.sieloja.controller.ConfiguracoesController;
import br.salt.sieloja.controller.ConsumoController;
import br.salt.sieloja.controller.EmpresaController;
import br.salt.sieloja.controller.GrupoController;
import br.salt.sieloja.controller.IdiomaController;
import br.salt.sieloja.controller.ItemController;
import br.salt.sieloja.controller.ParcialController;
import br.salt.sieloja.controller.SubgrupoController;
import br.salt.sieloja.controller.TabelaController;
import br.salt.sieloja.controller.UsuarioController;

@EActivity
public class BaseActivity extends FragmentActivity {

    protected ImageView imageLogo;
    protected TextView nomeTela;

    protected ProgressDialog progressDialog;

    @Bean
    protected ClienteController clienteController;

    @Bean
    protected ConfiguracoesController configuracoesController;

    @Bean
    protected ConsumoController consumoController;

    @Bean
    protected EmpresaController empresaController;

    @Bean
    protected GrupoController grupoController;

    @Bean
    protected IdiomaController idiomaController;

    @Bean
    protected ItemController itemController;

    @Bean
    protected ParcialController parcialController;

    @Bean
    protected SubgrupoController subgrupoController;

    @Bean
    protected TabelaController tabelaController;

    @Bean
    protected UsuarioController usuarioController;

    @Bean
    protected CodBarraController codBarraController;

    @Override
    protected void onResume() {
        super.onResume();

        this.imageLogo = (ImageView) findViewById(R.id.imageLogo);
        this.nomeTela = (TextView) findViewById(R.id.nomeTela);

        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle(getString(R.string.app_name));
        this.progressDialog.setMessage(getString(R.string.aguarde));
        this.progressDialog.setIcon(R.mipmap.ic_logo);
        this.progressDialog.setCancelable(false);

        int current = getRequestedOrientation();
        if (current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @UiThread
    protected void startProgress() {
        progressDialog.show();
    }

    @UiThread
    protected void stopProgress(String mensagem) {
        progressDialog.hide();
        Alert.dialog(this, mensagem);
    }

    @UiThread
    protected void stopProgress() {
        progressDialog.hide();
    }

    public String intToIp() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();

        return ( ipAddress & 0xFF) + "." +
                ((ipAddress >> 8 ) & 0xFF) + "." +
                ((ipAddress >> 16 ) & 0xFF) + "." +
                ((ipAddress >> 24 ) & 0xFF );
    }

    /*public boolean isConnectedInternet() {
        try {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
                return true; // Conectado a Internet WIFI
            } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
                return true; // Conectado a Internet 3G
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Alert.dialog(this, getString(R.string.sem_cenexao_internat));
        return false;
    }*/

    public static boolean isConnectedInternet(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
                return true; // Conectado a Internet WIFI
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Alert.dialog(context, context.getString(R.string.sem_cenexao_internat));
        return false;
    }

    public boolean isConnectedWS(String ip) {
        try{
            if( Build.VERSION.SDK_INT >= 9){
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            URL url = new URL(ip);
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(2000);
            urlc.connect();
            if (urlc.getResponseCode() == 200) {
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        Alert.dialog(this, getString(R.string.falha_na_comunicacao_webservice));
        return false;
    }

    public static String getCodigo(String codigo, int tamanho){
        for (int i = codigo.length(); i < tamanho; i++) {
            codigo = "0".concat(codigo);
        }
        return codigo;
    }

    public int getIdItemArray(String[] array, String text){
        text = retiraAcentos(text);
        if(text == null){
            return 0;
        }
        for (int i = 0; i < array.length; i++) {
            String str = retiraAcentos(array[i]);
            if(str.equalsIgnoreCase(text)){
                return i;
            }
        }
        return 0;
    }

    public String retiraAcentos(String str){
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}