package br.salt.sieloja.view.adapter.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
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

    public ParcialItem1(Context context) {
        super(context);

        inflate(context, R.layout.item_parcial1, this);
        textPedido = findViewById(R.id.textPedido);
        textStatus = findViewById(R.id.textStatus);
        textCliente = findViewById(R.id.textCliente);
        textBruto = findViewById(R.id.textBruto);
        textLiquido = findViewById(R.id.textLiquido);
        textTaxa = findViewById(R.id.textTaxa);
        listView = findViewById(R.id.listView);

        this.adapter = new ParcialAdapter(context);
    }
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
        if (adapter == null || adapter.getCount() == 0) return;

        listView.post(() -> {
            int totalHeight = 0;
            int listWidth = listView.getWidth();

            if (listWidth == 0) {
                calculeHeightListView();
                return;
            }

            int widthSpec = View.MeasureSpec.makeMeasureSpec(
                    listWidth, View.MeasureSpec.EXACTLY
            );
            int heightSpec = View.MeasureSpec.makeMeasureSpec(
                    0, View.MeasureSpec.UNSPECIFIED
            );

            for (int i = 0; i < adapter.getCount(); i++) {
                View item = adapter.getView(i, null, listView);

                item.setLayoutParams(new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT,
                        AbsListView.LayoutParams.WRAP_CONTENT
                ));

                item.measure(widthSpec, heightSpec);

                totalHeight += item.getMeasuredHeight();

                ViewGroup.LayoutParams lp = item.getLayoutParams();
                if (lp instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
                    totalHeight += mlp.topMargin + mlp.bottomMargin;
                }
            }

            int dividersHeight = listView.getDividerHeight()
                    * Math.max(0, adapter.getCount() - 1);

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + dividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        });
    }
}
