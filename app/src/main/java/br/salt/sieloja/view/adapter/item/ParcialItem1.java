package br.salt.sieloja.view.adapter.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;






import java.text.DecimalFormat;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.view.adapter.Parcial1Adapter;
import br.salt.sieloja.view.adapter.ParcialAdapter;
import br.salt.sieloja.view.util.Alert;

public class ParcialItem1 extends LinearLayout {

    
    TextView textPedido;

    
    TextView textStatus;

    
    TextView textCliente;

    
    TextView textBruto;

    
    TextView textLiquido;

    
    TextView textTaxa;

    
    ListView listView;

    
    ParcialAdapter adapter;

    public ParcialItem1(Context context) { super(context); }

    /*@AfterViews
    public void afterView(){

    }*/

    public void bind(Parcial parcial){
        adapter.setListItemParcial(parcial.getNumPed());
        listView.setAdapter(adapter);
        calculeHeightListView();

        textCliente.setText(parcial.getNomcli());
        textPedido.setText("Ped: " + parcial.getNumPed());
        textStatus.setText(parcial.getStatus());

        DecimalFormat form  = new DecimalFormat("###,##0.00");
        double bruto = adapter.getValorTotal();
        double taxa = adapter.getValorTotalTaxa();
        double liquido = bruto - taxa;
        textBruto.setText("R$" + String.valueOf(form.format(bruto)));
        textTaxa.setText("R$" + String.valueOf(form.format(taxa)));
        textLiquido.setText("R$" + String.valueOf(form.format(liquido)));
    }

    public void calculeHeightListView() {
        int totalHeight = 0;

        adapter = (ParcialAdapter) listView.getAdapter();
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
}
