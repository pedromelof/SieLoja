package br.salt.sieloja.view;

import static br.salt.sieloja.view.util.Alert.dialogValidation;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;


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
import br.salt.sieloja.controller.ConsumoController;
import br.salt.sieloja.controller.ItemController;
import br.salt.sieloja.controller.ParcialController;
import br.salt.sieloja.databinding.ActivityConsumoTextBinding;
import br.salt.sieloja.view.adapter.ConsumoAdapter;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;
import br.salt.sieloja.view.util.ConsumoActivity;

public class ConsumoTextActivity extends BaseActivity implements ConsumoActivity {

    private ActivityConsumoTextBinding binding;
    ConsumoAdapter adapter;

    private List<CodBarra> codBarra;
    private Configuracoes configuracoes;
    private DatePicker datePicker;
    private Calendar calendar;
    private Consumo consumo;
    private Usuario usuario;
    private Item item;
    private String numPed;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        binding = ActivityConsumoTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inicializarViews();
    }

    public void inicializarViews() {
        try {
            adapter = new ConsumoAdapter(this, consumoController);
            itemController = ItemController.getInstance(this);
            consumoController = ConsumoController.getInstance(this);
            parcialController = ParcialController.getInstance(this);


            String[] clientes = clienteController.getList();
            ArrayAdapter<String> adapterCliente = new ArrayAdapter<String>(this, R.layout.spinner_item, clientes);
            adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerCliente.setAdapter(adapterCliente);

            String[] formaPag = tabelaController.getListFormaPag();
            ArrayAdapter<String> adapterFormaPag = new ArrayAdapter<String>(this, R.layout.spinner_item, formaPag);
            adapterFormaPag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerFormaP.setAdapter(adapterFormaPag);

            String[] tipoPag = tabelaController.getListTipoPag();
            ArrayAdapter<String> adapterTipoPag = new ArrayAdapter<String>(this, R.layout.spinner_item, tipoPag);
            adapterTipoPag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerTipoP.setAdapter(adapterTipoPag);

            item = null;
            adapter.refresh(consumo, this);
            binding.listView.setAdapter(adapter);
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
                binding.btnValor.setEnabled(false);
            }

            Cliente c = clienteController.getClienteCod(consumo.getCodCliente());
            FormaPagamento fp = tabelaController.getFormaPagCod(consumo.getCodFormaPag());
            TipoPagamento tp = tabelaController.getTipoPagCod(consumo.getCodTipoPag());
            binding.spinnerCliente.setSelection(getIdItemArray(clientes, c.getNome()));
            binding.spinnerFormaP.setSelection(getIdItemArray(formaPag, fp.getDesfpg()));
            binding.spinnerTipoP.setSelection(getIdItemArray(tipoPag, tp.getDestpg()));

            if(configuracoes.isAlteraData()){
                calendar.setTime(consumo.getDate());
            }

            double total = consumoController.getTotal();
            binding.btnTotal.setText(String.format("R$ %.2f", total));
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            binding.btnData.setText(dateFormat.format(calendar.getTime()));
            String[] itens = itemController.getAllItem();
            final ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this, R.layout.item_list_autocomplete, itens);
            binding.btnItem.setAdapter(adapterList);
            binding.btnItem.setThreshold(1);
            binding.btnItem.setOnItemClickListener((parent, view, position, id) -> {
                try {
                    item = itemController.getItemFilterToString(adapterList.getItem(position));
                    DecimalFormat form  = new DecimalFormat("#####0.00");
                    binding.btnValor.setText(String.valueOf(form.format(item.getPreco())));
                    binding.btnQtd.requestFocus();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            binding.btnData.setOnClickListener(v -> btn_data());
            binding.btnAdd.setOnClickListener(v -> btn_add());
            binding.buttonFinalizar.setOnClickListener(v -> button_finalizar());
            binding.buttonCancelar.setOnClickListener(v -> button_cancelar());
            binding.buttonParcial.setOnClickListener(v -> button_parcial());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
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

    public void btn_data(){
        if(configuracoes.isAlteraData()){
            View view;
            view = (View) LayoutInflater.from(this).inflate(R.layout.dialog_data, null);
            datePicker = (DatePicker) view.findViewById(R.id.datePicker);
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            Alert.dialogPersonalizado(this, onClickListenerData, view);
        }
    }

    public void btn_add(){
        String quantidade = binding.btnQtd.getText().toString();
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
            String quantidade = binding.btnQtd.getText().toString();
            String txt_valor = binding.btnValor.getText().toString();
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
            binding.btnItem.setText("");
            binding.btnQtd.setText("");
            binding.btnValor.setText("");
            binding.btnItem.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

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

    public void button_cancelar(){
        dialogValidation(this, getString(R.string.confirmar_cancelamento_venda), onClickListener);
    }

    public void button_parcial(){
        parcial();
    }

    public void parcial(){
        new Thread(() -> {
            runOnUiThread(() -> startProgress());
            try {
                if(consumoController.getAllItemConsumo().size() > 0){
                    parcialController.transformarConsumoEmParcial();
                    Intent intent = new Intent(this, ParcialAcumuladoActivity.class);
                    intent.putExtra("data", calendar.getTime());
                    startActivity(intent);
                    runOnUiThread(() -> stopProgress());
                } else {
                    runOnUiThread(() -> stopProgress(getString(R.string.nenhum_item_foi_lancado)));
                }
            } catch (SQLException e) {
                runOnUiThread(() -> stopProgress(getString(R.string.erro_no_sql) + e.getMessage()));
                e.printStackTrace();
            } catch (Exception e) {
                runOnUiThread(() -> stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
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

    public void limparTela(){
        try {
            consumoController.deletarAll();
            consumo = new Consumo("", "", usuario.getCodigo(), calendar.getTime(), "", "", "");
            item = null;
            adapter.refresh(consumo);
            binding.listView.setAdapter(adapter);
            binding.btnItem.setText("");
            binding.btnQtd.setText("");
            binding.btnValor.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    public void finalizarConsumo(){
        new Thread(() -> {
            runOnUiThread(() -> startProgress());
            try {
                String cliente = binding.spinnerCliente.getSelectedItem().toString();
                String formaPg = binding.spinnerFormaP.getSelectedItem().toString();
                String tipoPg = binding.spinnerTipoP.getSelectedItem().toString();
                String codCliente = clienteController.getClienteNom(cliente).getCodigo();
                String codForma = tabelaController.getFormaPagDes(formaPg).getForpag();
                String codTipo = tabelaController.getTipoPagDes(tipoPg).getTippag();
                consumo.setCodCliente(codCliente);
                consumo.setCodFormaPag(codForma);
                consumo.setCodTipoPag(codTipo);
                if (configuracoes.isAlteraData()) {
                    consumoController.restConsumo(consumo, usuario, calendar.getTime(), calendar.getTime());
                } else {
                    consumoController.restConsumo(consumo, usuario, null, null);
                }
                runOnUiThread(() -> {
                    limparTela();
                    stopProgress();
                    dialogValidation(this, "Venda finalizada com sucesso!\nDeseja imprimir a parcial?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ConsumoTextActivity.this, ParcialIndividualActivity.class);
                                    intent.putExtra("numPed", numPed);
                                    startActivity(intent);
                                }
                            });
                });
            } catch (JSONException | SQLException e) {
                runOnUiThread(() -> stopProgress(getString(R.string.erro_no_sql) + e.getMessage()));
                e.printStackTrace();
            } catch (Exception e) {
                runOnUiThread(() -> stopProgress(getString(R.string.erro_procurar_administrador) + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    public void calculeHeightListView() {
        int totalHeight = 0;
        adapter = (ConsumoAdapter) binding.listView.getAdapter();
        int lenght = adapter.getCount();

        for (int i = 0; i < lenght; i++) {
            View listItem = adapter.getView(i, null, binding.listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = binding.listView.getLayoutParams();
        params.height = totalHeight + (binding.listView.getDividerHeight()) * (adapter.getCount() - 1);
        binding.listView.setLayoutParams(params);
        binding.listView.requestLayout();

        try {
            double total = consumoController.getTotal();
            binding.btnTotal.setText(String.format("R$ %.2f", total));
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
            binding.btnData.setText(dateFormat.format(consumo.getDate()));
            consumoController.salvarConsumo(consumo);
        } catch (SQLException e) {
            Alert.dialog(this, getString(R.string.erro_no_sql));
            e.printStackTrace();
        }
    }
}