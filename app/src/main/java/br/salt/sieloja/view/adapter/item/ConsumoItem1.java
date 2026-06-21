package br.salt.sieloja.view.adapter.item;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.CodBarra;
import br.salt.sieloja.bean.Item;
import br.salt.sieloja.bean.ItemConsumo;
import br.salt.sieloja.bean.Usuario;
import br.salt.sieloja.controller.CodBarraController;
import br.salt.sieloja.controller.ConsumoController;
import br.salt.sieloja.controller.ItemController;
import br.salt.sieloja.controller.UsuarioController;
import br.salt.sieloja.view.adapter.ConsumoAdapter;
import br.salt.sieloja.view.util.Alert;

@EViewGroup(R.layout.item_consumo_1)
public class ConsumoItem1 extends LinearLayout {

    @ViewById
    TextView produto;

    @ViewById
    EditText qtd;

    @ViewById
    EditText valor;

    @ViewById
    TextView tvCodBarra;

    @Bean
    ItemController itemController;

    @Bean
    ConsumoController consumoController;

    @Bean
    CodBarraController codBarraController;

    @Bean
    UsuarioController usuarioController;

    private ItemConsumo itemConsumo;
    private ConsumoAdapter consumoAdapter;

    public ConsumoItem1(Context context) {
        super(context);
    }

    public void bind(ItemConsumo itemConsumo, ConsumoAdapter consumoAdapter){
        try {
            this.consumoAdapter = consumoAdapter;
            this.itemConsumo = itemConsumo;
            qtd.setText(Double.toString(itemConsumo.getQuantidade()));
            valor.setText(Double.toString(itemConsumo.getPreco()));

            CodBarra c = codBarraController.getCodBarra(itemConsumo.getCodbar());
            if(c != null) { tvCodBarra.setText(c.getCodbar() + " - " + c.getUnid() + " " + c.getQtde()); }

            Item item = itemController.getItemFilterCodigo(itemConsumo.getCodigoItem());
            produto.setText(item.toString());

            Usuario usuario = usuarioController.getUsuarioLogado();
            if(Integer.parseInt(usuario.getNivelDeAcesso()) < 22){
                valor.setEnabled(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(getContext(), getContext().getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(getContext(), getContext().getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @AfterViews
    public void afterView(){
        qtd.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text_qtd = qtd.getText().toString();
                if(!text_qtd.equalsIgnoreCase("")){
                    try {
                        double qtd = Double.parseDouble(text_qtd);
                        itemConsumo.setQuantidade(qtd);
                        consumoController.salvarItemConsumo(itemConsumo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert.dialog(getContext(), getContext().getString(R.string.erro_no_sql));
                    } catch (Exception e) {
                        Alert.dialog(getContext(), getContext().getString(R.string.erro_procurar_administrador) + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });
        valor.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt_valor = valor.getText().toString();
                txt_valor = txt_valor.replace(",", ".");
                if(!txt_valor.equalsIgnoreCase("")){
                    try {
                        double valor = Double.parseDouble(txt_valor);
                        itemConsumo.setPreco(valor);
                        consumoController.salvarItemConsumo(itemConsumo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert.dialog(getContext(), getContext().getString(R.string.erro_no_sql));
                    } catch (Exception e) {
                        Alert.dialog(getContext(), getContext().getString(R.string.erro_procurar_administrador) + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });
    }

    @Click
    public void deletar(){
        try {
            consumoController.deletarItemConsumo(itemConsumo);
            consumoAdapter.refresh(itemConsumo.getConsumo());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(getContext(), getContext().getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(getContext(), getContext().getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }
}
