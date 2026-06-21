package br.salt.sieloja.view.adapter.item;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.controller.ItemController;
import br.salt.sieloja.view.util.Alert;

@EViewGroup(R.layout.item_parcial)
public class ParcialItem extends LinearLayout {

    @ViewById
    TextView textItem;

    @ViewById
    TextView textQuantidade;

    @ViewById
    TextView textValorTotal;

    @ViewById
    TextView textUnidade;

    @Bean
    ItemController itemController;

    public ParcialItem(Context context) {
        super(context);
    }

    public void bind(Parcial parcial){
        DecimalFormat form = new DecimalFormat("###,##0.00");
//		int width = this.getResources().getDisplayMetrics().widthPixels/2;
//		textItem.setWidth(width);
        try {
            textItem.setText(parcial.toString());
            textUnidade.setText(parcial.getUnidade());
            textQuantidade.setText(String.valueOf((int) parcial.getQtd()));
            textValorTotal.setText(String.valueOf(form.format(parcial.getValor())));
        } catch (Exception e) {
            Alert.dialog(getContext(), getContext().getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }
}

