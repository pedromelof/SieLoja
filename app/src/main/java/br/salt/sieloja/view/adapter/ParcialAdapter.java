package br.salt.sieloja.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.controller.ParcialController;
import br.salt.sieloja.view.adapter.item.ParcialItem;
import br.salt.sieloja.view.adapter.item.ParcialItem_;

@EBean
public class ParcialAdapter extends BaseAdapter {

    List<Parcial> parcial;
    List<Parcial> parcialTaxa;

    @RootContext
    Context context;

    @Bean
    ParcialController parcialController;

    public void setListItemParcial(String numped) {
        try {
            this.parcial = parcialController.getItemPedido(numped);
            this.parcialTaxa = parcialController.getItemPedidoTaxa(numped);
            notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getValorTotal(){
        double total = 0;
        for(Parcial p : parcial){
            total = total + p.getValor();
        }
        return total;
    }

    public double getValorTotalTaxa(){
        double total = 0;
        for(Parcial p : parcialTaxa){
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
    public long getItemId(int arg0) { return arg0; }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        ParcialItem parcialItem;
        if(convertView ==  null){
            parcialItem = ParcialItem_.build(context);
        }else{
            parcialItem = (ParcialItem) convertView;
        }

        parcialItem.bind(parcial.get(position));
        return parcialItem;
    }
}

