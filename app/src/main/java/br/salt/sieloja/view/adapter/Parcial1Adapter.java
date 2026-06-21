package br.salt.sieloja.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.sql.SQLException;
import java.util.List;

import br.salt.sieloja.bean.Parcial;
import br.salt.sieloja.controller.ParcialController;
import br.salt.sieloja.view.adapter.item.ParcialItem1;
import br.salt.sieloja.view.adapter.item.ParcialItem1_;

@EBean
public class Parcial1Adapter extends BaseAdapter {

    List<Parcial> parcial;

    @RootContext
    Context context;

    @Bean
    ParcialController parcialController;

    @AfterInject
    public void setListItemParcial(){
        try {
            this.parcial = parcialController.getForAllPedido();
            notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ParcialItem1 parcialItem;
        if(convertView ==  null){
            parcialItem = ParcialItem1_.build(context);
        }else{
            parcialItem = (ParcialItem1) convertView;
        }


        parcialItem.bind(parcial.get(position));
        return parcialItem;
    }
}
