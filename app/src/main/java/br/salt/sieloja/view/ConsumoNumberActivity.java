package br.salt.sieloja.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;










import org.json.JSONException;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Cliente;
import br.salt.sieloja.bean.CodBarra;
import br.salt.sieloja.bean.Configuracoes;
import br.salt.sieloja.bean.Consumo;
import br.salt.sieloja.bean.FormaPagamento;
import br.salt.sieloja.bean.Item;
import br.salt.sieloja.bean.ItemConsumo;
import br.salt.sieloja.bean.TipoPagamento;
import br.salt.sieloja.bean.Usuario;
import br.salt.sieloja.rest.Request;
import br.salt.sieloja.view.adapter.ConsumoAdapter;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;
import br.salt.sieloja.view.util.ConsumoActivity;

public class ConsumoNumberActivity extends BaseActivity implements ConsumoActivity {

    @ViewById
    AutoCompleteTextView btn_item;

    @ViewById
    Button btn_data;

    @ViewById
    Button btn_total;

    @ViewById
    EditText btn_qtd;

    @ViewById
    EditText btn_valor;

    @ViewById
    ListView listView;

    @ViewById
    Spinner spinnerCliente;

    @ViewById
    Spinner spinnerTipoP;

    @ViewById
    Spinner spinnerFormaP;

    @Bean
    ConsumoAdapter adapter;

    @RestService
    Request request;

    private List<CodBarra> codBarra;
    private Configuracoes configuracoes;
    private DatePicker datePicker;
    private Calendar calendar;
    private Consumo consumo;
    private Usuario usuario;
    private Item item;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_number);
    }

    final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            fecharConsumo();
        }
    };

    final OnClickListener onClickListenerData = new OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            data();
        }
    };

    public void fecharConsumo(){
        limparTela();
        Alert.dialog(this, getString(R.string.venda_cancelado_com_sucesso));
    }

    @AfterViews
    void afterView(){
        try {
            String[] clientes = clienteController.getList();
            ArrayAdapter<String> adapterCliente = new ArrayAdapter<String>(this, R.layout.spinner_item, clientes);
            adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCliente.setAdapter(adapterCliente);

            String[] formaPag = tabelaController.getListFormaPag();
            ArrayAdapter<String> adapterFormaPag = new ArrayAdapter<String>(this, R.layout.spinner_item, formaPag);
            adapterFormaPag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFormaP.setAdapter(adapterFormaPag);

            String[] tipoPag = tabelaController.getListTipoPag();
            ArrayAdapter<String> adapterTipoPag = new ArrayAdapter<String>(this, R.layout.spinner_item, tipoPag);
            adapterTipoPag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoP.setAdapter(adapterTipoPag);

            item = null;
            adapter.refresh(consumo, this);
            listView.setAdapter(adapter);
            calculeHeightListView();
            calendar = Calendar.getInstance();
            usuario = usuarioController.getUsuarioLogado();
            consumo = consumoController.getConsumoAberto();
            configuracoes = configuracoesController.getConfiguracoes();
            if(consumo == null){
                consumo = new Consumo("", "", usuario.getCodigo(), calendar.getTime(), "00000", "00001", "00001");
                consumoController.salvarConsumo(consumo);
            }

            if(Integer.parseInt(usuario.getNivelDeAcesso()) < 22){
                btn_valor.setEnabled(false);
            }

            Cliente c = clienteController.getClienteCod(consumo.getCodCliente());
            FormaPagamento fp = tabelaController.getFormaPagCod(consumo.getCodFormaPag());
            TipoPagamento tp = tabelaController.getTipoPagCod(consumo.getCodTipoPag());
            spinnerCliente.setSelection(getIdItemArray(clientes, c.getNome()));
            spinnerFormaP.setSelection(getIdItemArray(formaPag, fp.getDesfpg()));
            spinnerTipoP.setSelection(getIdItemArray(tipoPag, tp.getDestpg()));

            if(configuracoes.isAlteraData()){
                calendar.setTime(consumo.getDate());
            }

            double total = consumoController.getTotal();
            btn_total.setText(String.format("R$ %.2f", total));
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            btn_data.setText(dateFormat.format(calendar.getTime()));
            String[] itens = itemController.getAllItem();
            final ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this, R.layout.item_list_autocomplete, itens);
            btn_item.setAdapter(adapterList);
            btn_item.setThreshold(1);
            btn_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        item = itemController.getItemFilterToString(adapterList.getItem(position));
                        DecimalFormat form  = new DecimalFormat("#####0.00");
                        String preco = String.valueOf(form.format(item.getPreco()));
                        preco = preco.replace(",", ".");
                        btn_valor.setText(preco);
                        btn_qtd.requestFocus();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }});
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Click
    public void btn_data(){
        if(configuracoes.isAlteraData()){
            View view;
            view = (View) LayoutInflater.from(this).inflate(R.layout.dialog_data, null);
            datePicker = (DatePicker) view.findViewById(R.id.datePicker);
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            Alert.dialogPersonalizado(this, onClickListenerData, view);
        }
    }

    @Click
    public void btn_add(){
        String quantidade = btn_qtd.getText().toString();
        if(item == null){
            Alert.dialog(this, getString(R.string.informe_item));
        } else if(quantidade.equalsIgnoreCase("")){
            Alert.dialog(this, getString(R.string.quantidade_maior_que_zero));
        } else {
            try {
                this.codBarra = codBarraController.getListCodBarra(item.getCodigo());
                String[] cb = codBarraController.getListCodBarra(codBarra);
                if(codBarra.size() > 1){
                    Alert.dialogListItems(this, onClickListenerCodBarra, cb);
                } else {
                    addItem(-1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert.dialog(this, getString(R.string.erro_no_sql));
            } catch (Exception e) {
                Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    final OnClickListener onClickListenerCodBarra = new OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int which) {
            addItem(which);
        }
    };

    private void addItem(int which) {
        try {
            String quantidade = btn_qtd.getText().toString();
            String txt_valor = btn_valor.getText().toString();
            txt_valor = txt_valor.replace(",", ".");
            double qtd = Double.parseDouble(quantidade);
            double valor = Double.parseDouble(txt_valor);
            ItemConsumo itemConsumo;
            if (which == -1){
                itemConsumo = new ItemConsumo(consumo, item.getCodigo(), qtd, "", "", 0, valor);
            } else {
                CodBarra cb = codBarra.get(which);
                itemConsumo = new ItemConsumo(consumo, item.getCodigo(), qtd, "", cb.getCodbar(), cb.getQtde(), valor);
            }
            consumoController.salvarItemConsumo(itemConsumo);
            adapter.refresh(consumo);
            item = null;
            btn_item.setText("");
            btn_qtd.setText("");
            btn_valor.setText("");
            btn_item.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Click
    public void button_finalizar(){
        if(validaCampos()){
            try {
                finalizarConsumo();
            } catch (Exception e) {
                Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Click
    public void button_cancelar(){
        Alert.dialogValidation(this, getString(R.string.confirmar_cancelamento_venda), onClickListener);
    }

    @Click
    public void button_parcial(){
        parcial();
    }

    @Background
    public void parcial(){
        startProgress();
        try {
            if(consumoController.getAllItemConsumo().size() > 0){
                parcialController.transformarConsumoEmParcial();
                Intent intent = new Intent(this, ParcialActivity.class);
                startActivity(intent);
                stopProgress();
            } else {
                stopProgress(getString(R.string.nenhum_item_foi_lancado));
            }
        } catch (SQLException e) {
            stopProgress(getString(R.string.erro_no_sql) + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean validaCampos(){
        boolean valida = false;
        try {
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            if(!consumoController.isValidationItensLancados()){
                Alert.dialog(this, getString(R.string.item_lansado_incorretamente));
            }else if(adapter.getCount() < 1){
                Alert.dialog(this, getString(R.string.item_lansado_incorretamente));
            } else if(isConnectedInternet(this) && isConnectedWS(configuracoes.getIpWebService())){
                valida = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
        return valida;
    }

    @UiThread
    public void limparTela(){
        try {
            consumoController.deletarAll();
            consumo = new Consumo("", "", usuario.getCodigo(), calendar.getTime(), "", "", "");
            item = null;
            adapter.refresh(consumo);
            listView.setAdapter(adapter);
            btn_item.setText("");
            btn_qtd.setText("");
            btn_valor.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Background
    public void finalizarConsumo(){
        startProgress();
        try {
            String cliente = spinnerCliente.getSelectedItem().toString();
            String formaPg = spinnerFormaP.getSelectedItem().toString();
            String tipoPg = spinnerTipoP.getSelectedItem().toString();
            String codCliente = clienteController.getClienteNom(cliente).getCodigo();
            String codForma = tabelaController.getFormaPagDes(formaPg).getForpag();
            String codTipo = tabelaController.getTipoPagDes(tipoPg).getTippag();
            consumo.setCodCliente(codCliente);
            consumo.setCodFormaPag(codForma);
            consumo.setCodTipoPag(codTipo);
            if (configuracoes.isAlteraData()) {
                consumoController.restConsumo(consumo, usuario, calendar.getTime());
            } else {
                consumoController.restConsumo(consumo, usuario, null);
            }
            limparTela();
            stopProgress(getString(R.string.venda_finalizada));
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

    public void calculeHeightListView() {
        int totalHeight = 0;

        adapter = (ConsumoAdapter) listView.getAdapter();
        int lenght = adapter.getCount();

        for (int i = 0; i < lenght; i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight())	* (adapter.getCount() - 1);
        listView.setLayoutParams(params);
        listView.requestLayout();

        try {
            double total = consumoController.getTotal();
            btn_total.setText(String.format("R$ %.2f", total));
        } catch (SQLException e) {
            Alert.dialog(this, getString(R.string.erro_no_sql));
            e.printStackTrace();
        }
    }

    public void data(){
        try {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            calendar.set(year, month, day);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            consumo.setDate(calendar.getTime());
            btn_data.setText(dateFormat.format(consumo.getDate()));
            consumoController.salvarConsumo(consumo);
        } catch (SQLException e) {
            Alert.dialog(this, getString(R.string.erro_no_sql));
            e.printStackTrace();
        }
    }
}