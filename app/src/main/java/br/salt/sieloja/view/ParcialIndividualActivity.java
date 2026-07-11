package br.salt.sieloja.view;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.cete.dynamicpdf.Document;
import com.cete.dynamicpdf.Font;
import com.cete.dynamicpdf.Grayscale;
import com.cete.dynamicpdf.Page;
import com.cete.dynamicpdf.PageOrientation;
import com.cete.dynamicpdf.PageSize;
import com.cete.dynamicpdf.Template;
import com.cete.dynamicpdf.TextAlign;
import com.cete.dynamicpdf.WebColor;
import com.cete.dynamicpdf.pageelements.Label;
import com.cete.dynamicpdf.pageelements.Rectangle;

import org.json.JSONException;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Empresa;
import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.bean.Usuario;
import br.salt.sieloja.databinding.ActivityParcialIndividualBinding;
import br.salt.sieloja.view.adapter.Parcial1Adapter;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

public class ParcialIndividualActivity extends BaseActivity {

    private ActivityParcialIndividualBinding binding;
    private Parcial1Adapter adapter;
    private Template template;
    private String numPed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParcialIndividualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("numPed")) {
            numPed = getIntent().getStringExtra("numPed");
        }

        afterView();
    }

    public void afterView() {
        try {
            adapter = new Parcial1Adapter(this);
            binding.listView.setAdapter(adapter);
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            Empresa empresa = empresaController.getEmpresa(configuracoes.getEmpresa());
            Usuario usuario = usuarioController.getUsuarioLogado();

            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm:ss");
            String dataFormatada = formatoData.format(new Date());
            String horaFormatada = formatoHora.format(new Date());
            binding.valLoja.setText(empresa.getFantasia());
            binding.valData.setText(dataFormatada);
            binding.valHora.setText(horaFormatada);
            binding.valVendedor.setText(usuario.getCodigo() + " - " + usuario.getNome().toUpperCase());

            binding.buttonImpri.setOnClickListener(v -> buttonImpri());

            calculeHeightListView();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    public void calculeHeightListView() {
        int totalHeight = 0;

        adapter = (Parcial1Adapter) binding.listView.getAdapter();
        int lenght = adapter.getCount();

        for (int i = 0; i < lenght; i++) {
            View listItem = adapter.getView(i, null, binding.listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
//			totalHeight += 38;
        }

        ViewGroup.LayoutParams params = binding.listView.getLayoutParams();
        params.height = totalHeight + (binding.listView.getDividerHeight()) * (adapter.getCount() - 1);
        binding.listView.setLayoutParams(params);
        binding.listView.requestLayout();
    }

    public void buttonImpri() {
        try {
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            if (isConnectedInternet(this) && isConnectedWS(configuracoes.getIpWebService())) {
                imprimirParcial();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        }
    }

    public void imprimirParcial() {
        new Thread(() -> {
            runOnUiThread(() -> startProgress());
            try {
                parcialController.restParcialIndividualImpre("");
                runOnUiThread(() -> stopProgress(getString(R.string.impressao_realizada_com_sucesso)));
            } catch (JSONException e) {
                runOnUiThread(() -> stopProgress(getString(R.string.erro_no_json) + e.getMessage()));
                e.printStackTrace();
            } catch (SQLException e) {
                runOnUiThread(() -> stopProgress(getString(R.string.erro_no_sql) + e.getMessage()));
                e.printStackTrace();
            } catch (Exception e) {
                runOnUiThread(() -> stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }
}

