package br.salt.sieloja.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
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
import br.salt.sieloja.databinding.ActivityPagamentoBinding;
import br.salt.sieloja.view.adapter.ConsumoAdapter;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;
import br.salt.sieloja.view.util.ConsumoActivity;

public class PagamentoActivity extends BaseActivity {

    private ActivityPagamentoBinding binding;
    ConsumoAdapter adapter;

    private List<CodBarra> codBarra;
    private Configuracoes configuracoes;
    private DatePicker datePicker;
    private Calendar calendar;
    private Consumo consumo;
    private Usuario usuario;
    private Item item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        binding = ActivityPagamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inicializarViews();
    }

    public void inicializarViews() {
        try {
//            adapter = new ConsumoAdapter(this, consumoController);
//            itemController = ItemController.getInstance(this);
//            consumoController = ConsumoController.getInstance(this);
//            parcialController = ParcialController.getInstance(this);
//
//
//            String[] clientes = clienteController.getList();
//            ArrayAdapter<String> adapterCliente = new ArrayAdapter<String>(this, R.layout.spinner_item, clientes);
//            adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            binding.spinnerCliente.setAdapter(adapterCliente);
//
//            String[] formaPag = tabelaController.getListFormaPag();
//            ArrayAdapter<String> adapterFormaPag = new ArrayAdapter<String>(this, R.layout.spinner_item, formaPag);
//            adapterFormaPag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            binding.spinnerFormaP.setAdapter(adapterFormaPag);
//
//            String[] tipoPag = tabelaController.getListTipoPag();
//            ArrayAdapter<String> adapterTipoPag = new ArrayAdapter<String>(this, R.layout.spinner_item, tipoPag);
//            adapterTipoPag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            binding.spinnerTipoP.setAdapter(adapterTipoPag);
//
//            item = null;
//            adapter.refresh(consumo, this);
//            binding.listView.setAdapter(adapter);
//            calculeHeightListView();
//            calendar = Calendar.getInstance();
//            usuario = usuarioController.getUsuarioLogado();
//            consumo = consumoController.getConsumoAberto();
//            configuracoes = configuracoesController.getConfiguracoes();
//            if(consumo == null){
//                consumo = new Consumo("", "", usuario.getCodigo(), calendar.getTime(), "00000", "00001", "00001");
//                consumoController.salvarConsumo(consumo);
//            }
//
//            if(Integer.parseInt(usuario.getNivelDeAcesso()) < 22){
//                binding.btnValor.setEnabled(false);
//            }
//
//            Cliente c = clienteController.getClienteCod(consumo.getCodCliente());
//            FormaPagamento fp = tabelaController.getFormaPagCod(consumo.getCodFormaPag());
//            TipoPagamento tp = tabelaController.getTipoPagCod(consumo.getCodTipoPag());
//            binding.spinnerCliente.setSelection(getIdItemArray(clientes, c.getNome()));
//            binding.spinnerFormaP.setSelection(getIdItemArray(formaPag, fp.getDesfpg()));
//            binding.spinnerTipoP.setSelection(getIdItemArray(tipoPag, tp.getDestpg()));
//
//            if(configuracoes.isAlteraData()){
//                calendar.setTime(consumo.getDate());
//            }
//
//            double total = consumoController.getTotal();
//            binding.btnTotal.setText(String.format("R$ %.2f", total));
//            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            binding.btnData.setText(dateFormat.format(calendar.getTime()));
//            String[] itens = itemController.getAllItem();
//            final ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this, R.layout.item_list_autocomplete, itens);
//            binding.btnItem.setAdapter(adapterList);
//            binding.btnItem.setThreshold(1);
//            binding.btnItem.setOnItemClickListener((parent, view, position, id) -> {
//                try {
//                    item = itemController.getItemFilterToString(adapterList.getItem(position));
//                    DecimalFormat form  = new DecimalFormat("#####0.00");
//                    binding.btnValor.setText(String.valueOf(form.format(item.getPreco())));
//                    binding.btnQtd.requestFocus();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            binding.btnData.setOnClickListener(v -> btn_data());
//            binding.btnAdd.setOnClickListener(v -> btn_add());
//            binding.buttonFinalizar.setOnClickListener(v -> button_finalizar());
//            binding.buttonCancelar.setOnClickListener(v -> button_cancelar());
//            binding.buttonParcial.setOnClickListener(v -> button_parcial());
//        } catch (SQLException e) {
//            e.printStackTrace();
//            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }


    public boolean validaCampos() {
        boolean valida = false;
        try {
            Configuracoes configuracoes = configuracoesController.getConfiguracoes();
            if (!consumoController.isValidationItensLancados()) {
                Alert.dialog(this, getString(R.string.item_lansado_incorretamente));
            } else if (adapter.getCount() < 1) {
                Alert.dialog(this, getString(R.string.item_lansado_incorretamente));
            } else if (isConnectedInternet(this) && isConnectedWS(configuracoes.getIpWebService())) {
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

    public void limparTela() {
        try {
//            consumoController.deletarAll();
//            consumo = new Consumo("", "", usuario.getCodigo(), calendar.getTime(), "", "", "");
//            item = null;
//            adapter.refresh(consumo);
//            binding.listView.setAdapter(adapter);
//            binding.btnItem.setText("");
//            binding.btnQtd.setText("");
//            binding.btnValor.setText("");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }
}