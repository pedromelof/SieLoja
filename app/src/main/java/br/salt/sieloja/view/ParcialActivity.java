package br.salt.sieloja.view;

import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONException;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.view.adapter.Parcial1Adapter;
import br.salt.sieloja.view.adapter.ParcialAdapter;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

@WindowFeature({ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.activity_parcial)
public class ParcialActivity extends BaseActivity {

    @Extra
    Date data;

    @ViewById
    TextView textValorBruto;

    @ViewById
    TextView textValorTaxa;

    @ViewById
    TextView textValorLiquido;

    @ViewById
    TextView textValorTotalItens;

    @ViewById
    ListView listView;

    @Bean
    Parcial1Adapter adapter;

    private Template template;

    @AfterViews
    public void afterView(){
        try {
            listView.setAdapter(adapter);
            calculeHeightListView();

            DecimalFormat form  = new DecimalFormat("###,##0.00");
            double bruto = parcialController.getTotal();
            double taxa = parcialController.getTotalTaxa();
            double liquido = bruto - taxa;
            textValorBruto.setText(String.valueOf(form.format(bruto)));
            textValorTaxa.setText(String.valueOf(form.format(taxa)));
            textValorLiquido.setText(String.valueOf(form.format(liquido)));
            textValorTotalItens.setText(String.valueOf((int) parcialController.getTotalItens()));
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

        adapter = (Parcial1Adapter) listView.getAdapter();
        int lenght = adapter.getCount();

        for (int i = 0; i < lenght; i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
//			totalHeight += 38;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight()) * (adapter.getCount() - 1) + 20;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Click
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

    @Click
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

    @Background
    public void imprimirParcial(){
        startProgress();
        try {
            parcialController.restParcialImpre(data);
            stopProgress(getString(R.string.impressao_realizada_com_sucesso));
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

    @Background
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createRelatorio(Configuracoes configuracoes){
        try {
            startProgress();
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
            String file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + "/SIE EasyBox/RelVenda - " + sd1.format(this.data) + ".pdf";

            for (Parcial parcial : parcialController.getForAll()) {
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
            stopProgress(getString(R.string.salvo_com_sucesso));
        } catch (SQLException e) {
            e.printStackTrace();
            stopProgress(getString(R.string.erro_no_sql) + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage());
        }
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

