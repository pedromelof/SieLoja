package br.salt.sieloja.view.adapter.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.sql.SQLException;
import java.text.DecimalFormat;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.controller.ParcialController;
import br.salt.sieloja.controller.TabelaController;
import br.salt.sieloja.view.adapter.Parcial1Adapter;
import br.salt.sieloja.view.adapter.ParcialAdapter;
import br.salt.sieloja.view.util.Alert;

public class ParcialItem1 extends LinearLayout {

    Double qtdTotalItens = 0.00;
    TextView textPedido;
    TextView textStatus;
    TextView textCliente;
    TextView textQtdItens;
    TextView textForPag;
    TextView textTipPag;
    TextView textBruto;
    TextView textLiquido;
    TextView textTaxa;
    ListView listView;
    ParcialAdapter adapter;

    public ParcialItem1(Context context) {
        super(context);

        inflate(context, R.layout.item_parcial1, this);
        textPedido = findViewById(R.id.valCodPed);
//        textStatus = findViewById(R.id.textStatus);
        textCliente = findViewById(R.id.valNomeCliente);
        textBruto = findViewById(R.id.valTotal);
//        textLiquido = findViewById(R.id.textLiquido);
//        textTaxa = findViewById(R.id.textTaxa);
        textQtdItens = findViewById(R.id.valQtdItens);
        textForPag = findViewById(R.id.valForPag);
        textTipPag = findViewById(R.id.valTipPag);
        listView = findViewById(R.id.listView);

        this.adapter = new ParcialAdapter(context);
    }
    public void bind(Parcial parcial) throws SQLException {
        adapter.setListItemParcial(parcial.getNumPed());
        listView.setAdapter(adapter);
        calculeHeightListView();
        DecimalFormat formTotItens  = new DecimalFormat("#0");

        TabelaController tabelaController = TabelaController.getInstance(getContext());

        textCliente.setText(parcial.getNomcli());
        textPedido.setText(parcial.getNumPed());
//        textStatus.setText(parcial.getStatus());
        qtdTotalItens = ParcialController.getInstance(getContext()).getTotalItens();
        textQtdItens.setText(formTotItens.format(adapter.getQtdItens()));
        textForPag.setText(tabelaController.getFormaPagCod(parcial.getForPag()).getDesfpg());
        textTipPag.setText(tabelaController.getTipoPagCod(parcial.getTipPag()).getDestpg());


        DecimalFormat form  = new DecimalFormat("###,##0.00");
        double bruto = adapter.getValorTotal();
//        double taxa = adapter.getValorTotalTaxa();
//        double liquido = bruto - taxa;
        textBruto.setText("R$" + String.valueOf(form.format(bruto)));
//        textTaxa.setText("R$" + String.valueOf(form.format(taxa)));
//        textLiquido.setText("R$" + String.valueOf(form.format(liquido)));
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
