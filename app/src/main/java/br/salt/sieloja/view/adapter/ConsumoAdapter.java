package br.salt.sieloja.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.salt.sieloja.bean.Consumo;
import br.salt.sieloja.bean.ItemConsumo;
import br.salt.sieloja.controller.ConsumoController;
import br.salt.sieloja.view.adapter.item.ConsumoItem1;
import br.salt.sieloja.view.adapter.item.ConsumoItem2;
import br.salt.sieloja.view.util.ConsumoActivity;

public class ConsumoAdapter extends BaseAdapter {

    private List<ItemConsumo> itensConsumo;
    private ConsumoActivity consumoActivity;

    
    Context context;

    
    ConsumoController consumoController;

    public ConsumoAdapter(Context context, ConsumoController consumoController) {
        this.context = context;
        this.consumoController = consumoController;
    }

    public void refresh(Consumo consumo, ConsumoActivity consumoActivity) throws SQLException {
        this.consumoActivity = consumoActivity;
        itensConsumo = consumoController.getAllItemConsumo();
        if(itensConsumo == null){
            itensConsumo = new ArrayList<ItemConsumo>();
        }
        notifyDataSetChanged();
    }

    public void refresh(Consumo consumo) throws SQLException{
        itensConsumo = consumoController.getAllItemConsumo();
        if(itensConsumo == null){
            itensConsumo = new ArrayList<ItemConsumo>();
        }
        notifyDataSetChanged();
        this.consumoActivity.calculeHeightListView();
    }

    public void finaliza_consumo(){
        itensConsumo.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itensConsumo.size();
    }

    @Override
    public Object getItem(int arg0) {
        return itensConsumo.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        if(position % 2 == 0){
            ConsumoItem1 consumoItem;
            if(convertView ==  null){
                consumoItem = new ConsumoItem1(context);
            }else{
                consumoItem = (ConsumoItem1) convertView;
            }
            consumoItem.bind(itensConsumo.get(position), this);
            return consumoItem;
        } else {
            ConsumoItem2 consumoItem;
            if(convertView ==  null){
                consumoItem = new ConsumoItem2(context);
            }else{
                consumoItem = (ConsumoItem2) convertView;
            }
            consumoItem.bind(itensConsumo.get(position), this);
            return consumoItem;
        }
    }
}
