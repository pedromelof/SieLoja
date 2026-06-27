package br.salt.sieloja.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;





import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.salt.sieloja.bean.Item;
import br.salt.sieloja.controller.ItemController;
import br.salt.sieloja.view.adapter.item.CardapioItem;


public class CardapioAdapter extends BaseAdapter {

    List<Item> itens = new ArrayList<Item>();


    Context context;

    
    ItemController itemController;

    private String codigoIdioma;

    public CardapioAdapter(Context context, ItemController itemController) {
        this.context = context;
        this.itemController = itemController;
    }
    public void refresh(List<String> grupos, String codigoIdioma) throws SQLException {
        this.codigoIdioma = codigoIdioma;
        itens.clear();
        for(String nomeGrupo : grupos){
            List<Item> itens2 = itemController.getItensFilterGrupos(nomeGrupo);
            itens.addAll(itens2);
        }
        notifyDataSetChanged();
    }

    public void refresh(String nomeItem, String codigoIdioma) throws SQLException{
        this.codigoIdioma = codigoIdioma;
        Item item = itemController.getItemFilterToString(nomeItem);
        itens.clear();
        if(item != null){
            itens.clear();
            itens.add(item);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int arg0) {
        return itens.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardapioItem cardapioItem;
        if(convertView ==  null){
            cardapioItem = new CardapioItem(context);
        }else{
            cardapioItem = (CardapioItem) convertView;
        }

        cardapioItem.bind(itens.get(position), codigoIdioma);
        return cardapioItem;
    }
}

