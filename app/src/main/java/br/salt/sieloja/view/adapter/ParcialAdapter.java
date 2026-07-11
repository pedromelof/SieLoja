package br.salt.sieloja.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.controller.ParcialController;
import br.salt.sieloja.view.adapter.item.ParcialItem;


public class ParcialAdapter extends BaseAdapter {

    List<Parcial> parcial;
    List<Parcial> parcialTaxa;


    Context context;


    ParcialController parcialController;

    public ParcialAdapter(Context context) {
        this.context = context;
        this.parcialController = ParcialController.getInstance(context.getApplicationContext());
    }

    public void setListItemParcial(String numped) {
        try {
            this.parcial = parcialController.getItemPedido(numped);
            this.parcialTaxa = parcialController.getItemPedidoTaxa(numped);
            notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setListItemParcialAgregado(List<Parcial> listaOriginal) {
        Map<String, Parcial> mapaAgrupado = new LinkedHashMap<>();

        for (Parcial p : listaOriginal) {
            String codigo = p.getCodItem();

            if (mapaAgrupado.containsKey(codigo)) {
                Parcial existente = mapaAgrupado.get(codigo);
                existente.setQtd(existente.getQtd() + p.getQtd());
                existente.setValor(existente.getValor() + p.getValor());
            } else {
                mapaAgrupado.put(codigo, p);
            }
        }

        ArrayList<Parcial> novaLista = new ArrayList<>(mapaAgrupado.values());
        this.parcial = novaLista;
    }

    public double getQtdItens() {
        double total = 0;
        for (Parcial p : parcial) {
            total = total + p.getQtd();
        }
        return total;
    }

    public double getValorTotal() {
        double total = 0;
        for (Parcial p : parcial) {
            total = total + p.getValor();
        }
        return total;
    }

    public double getValorTotalTaxa() {
        double total = 0;
        for (Parcial p : parcialTaxa) {
            total = total + p.getValor();
        }
        return total;
    }

    @Override
    public int getCount() {
        return parcial.size();
    }

    @Override
    public Object getItem(int arg0) {
        return parcial.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ParcialItem parcialItem;
        if (convertView == null) {
            parcialItem = new ParcialItem(context);
        } else {
            parcialItem = (ParcialItem) convertView;
        }

        parcialItem.bind(parcial.get(position));
        return parcialItem;
    }
}

