package br.salt.sieloja.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

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
import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.databinding.ActivityParcialBinding;
import br.salt.sieloja.view.adapter.ConsumoAdapter;
import br.salt.sieloja.view.adapter.Parcial1Adapter;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

public class ParcialActivity extends BaseActivity {

    Date data;
    private ActivityParcialBinding binding;
    private Parcial1Adapter adapter;
    private Template template;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityParcialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("data")) {
            data = (Date) getIntent().getSerializableExtra("data");
        }

        afterView();
    }

    public void afterView(){
        try {
            adapter = new Parcial1Adapter(this);
            binding.listView.setAdapter(adapter);

            DecimalFormat form  = new DecimalFormat("###,##0.00");
            double bruto = parcialController.getTotal();
            double taxa = parcialController.getTotalTaxa();
            double liquido = bruto - taxa;
            binding.textValorBruto.setText(String.valueOf(form.format(bruto)));
            binding.textValorTaxa.setText(String.valueOf(form.format(taxa)));
            binding.textValorLiquido.setText(String.valueOf(form.format(liquido)));
            binding.textValorTotalItens.setText(String.valueOf((int) parcialController.getTotalItens()));

            binding.buttonSalvar.setOnClickListener(v -> buttonSalvar());
            binding.buttonImpri.setOnClickListener(v -> buttonImpri());
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
        params.height = totalHeight + (binding.listView.getDividerHeight()) * (adapter.getCount() - 1) + 20;
        binding.listView.setLayoutParams(params);
        binding.listView.requestLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void buttonSalvar(){
        try {
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            createRelatorio(configuracoes);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        }
    }

    public void buttonImpri(){
        try {
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            if(isConnectedInternet(this) && isConnectedWS(configuracoes.getIpWebService())){
                imprimirParcial();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        }
    }

    public void imprimirParcial(){
        new Thread(() -> {
            runOnUiThread(() -> startProgress());
            try {
                parcialController.restParcialImpre(data);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createRelatorio(Configuracoes configuracoes){
        new Thread(() -> {
            runOnUiThread(() -> startProgress());
            try {
                String unidade = configuracoes.getUnidadeAdm();
                GregorianCalendar gc = new GregorianCalendar();
                SimpleDateFormat sd1 = new SimpleDateFormat("yyMMdd - H mm");
                Date date = gc.getTime();
                boolean alternateBG = false;
                int currentY = 84;
                template = new Template();

                setHeaderTemplate(date, unidade);
                Document objDocument = new Document();
                objDocument.setTemplate(template);
                Page objPage = new Page(PageSize.LETTER, PageOrientation.PORTRAIT, 54.0f);

                File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File appDir = new File(baseDir, "SIE EasyBox");

                if (!appDir.exists()) {
                    if (!appDir.mkdirs()) {
                        throw new Exception("Falha ao criar pasta: " + appDir.getAbsolutePath());
                    }
                }

                String file = new File(appDir, "RelVenda - " + sd1.format(this.data) + ".pdf").getAbsolutePath();

                for (Parcial parcial : parcialController.getForAll()) {
                    if (parcial.getCodItem().equals("TX. ADM")) continue;
                    Label codigo = new Label(parcial.getCodItem(), 2, currentY, 60, 11,Font.getTimesRoman(), 11);
                    Label item = new Label(parcial.getDecItem(), 70, currentY, 320, 11,Font.getTimesRoman(), 11);
                    Label qtd = new Label(parcial.getQtdTxt(), 380, currentY, 50, 11,Font.getTimesRoman(), 11, TextAlign.RIGHT);
                    Label valor = new Label(parcial.getValorTxt(), 440, currentY, 50, 11,Font.getTimesRoman(), 11, TextAlign.RIGHT);
                    if (alternateBG) {
                        objPage.getElements().add(new Rectangle(0, currentY-1, 500, 15, new WebColor("E0E0FF"),new WebColor("E0E0FF")));
                    }
                    alternateBG = !alternateBG;
                    objPage.getElements().add(codigo);
                    objPage.getElements().add(item);
                    objPage.getElements().add(qtd);
                    objPage.getElements().add(valor);
                    currentY = currentY + 21;
                    if(currentY > 680){
                        currentY = 84;
                        alternateBG = false;
                        objDocument.getPages().add(objPage);
                        objPage = new Page(PageSize.LETTER, PageOrientation.PORTRAIT, 54.0f);
                    }
                }

                if (alternateBG) {
                    objPage.getElements().add(new Rectangle(0, currentY-1, 500, 15, new WebColor("E0E0FF"),new WebColor("E0E0FF")));
                }
                Label total = new Label("Total:", 2, currentY, 320, 11,Font.getTimesRoman(), 11);
                Label qtd = new Label(String.format("%.2f", parcialController.getTotalItens()), 380, currentY, 50, 12,Font.getTimesRoman(), 12, TextAlign.RIGHT);
                Label val = new Label(String.format("%.2f", parcialController.getTotal()), 440, currentY, 50, 12,Font.getTimesRoman(), 12, TextAlign.RIGHT);
                objPage.getElements().add(total);
                objPage.getElements().add(qtd);
                objPage.getElements().add(val);

                objDocument.getPages().add(objPage);
                objDocument.draw(file);

                runOnUiThread(() -> stopProgress(getString(R.string.salvo_com_sucesso)));
            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> stopProgress(getString(R.string.erro_no_sql) + e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage()));
            }
        }).start();
    }

    private void setHeaderTemplate(Date date, String unidade) throws SQLException {
        SimpleDateFormat sd1 = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat sd2 = new SimpleDateFormat("H:mm:ss");
        template.getElements().add(new Label("Salt Informatica - SIE EasyBox", 0, 0, 500, 12, Font.getHelvetica(), 12, TextAlign.LEFT));
        template.getElements().add(new Label("Empresa - Unidade Administrativa " + unidade, 0, 21, 500, 12, Font.getHelvetica(), 12, TextAlign.LEFT));
        template.getElements().add(new Label("Relatorio de Vendas do dia " + sd1.format(this.data), 0, 42, 500, 12, Font.getHelvetica(), 12, TextAlign.LEFT));
        template.getElements().add(new Label(sd1.format(date), 0, 21, 500, 12, Font.getHelvetica(), 12, TextAlign.RIGHT));
        template.getElements().add(new Label(sd2.format(date), 0, 42, 500, 12, Font.getHelvetica(), 12, TextAlign.RIGHT));
        template.getElements().add(new Rectangle(0, 63, 500, 16, new WebColor("0000A0"),new WebColor("0000A0")));
        template.getElements().add(new Label("Codigo", 2, 63, 60, 12, Font.getHelveticaBold(), 12, TextAlign.LEFT, Grayscale.getWhite()));
        template.getElements().add(new Label("Item", 70, 63, 300, 12, Font.getHelveticaBold(), 12, TextAlign.LEFT, Grayscale.getWhite()));
        template.getElements().add(new Label("Qtd", 380, 63, 50, 12, Font.getHelveticaBold(), 12, TextAlign.RIGHT, Grayscale.getWhite()));
        template.getElements().add(new Label("Valor", 440, 63, 50, 12, Font.getHelveticaBold(), 12, TextAlign.RIGHT, Grayscale.getWhite()));
    }
}

