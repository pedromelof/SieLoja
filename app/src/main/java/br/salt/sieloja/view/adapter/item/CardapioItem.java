package br.salt.sieloja.view.adapter.item;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;




import java.sql.SQLException;
import java.text.DecimalFormat;

import br.salt.sieloja.R;
import br.salt.sieloja.bean.Idioma;
import br.salt.sieloja.bean.Item;
import br.salt.sieloja.controller.IdiomaController;
import br.salt.sieloja.view.util.Alert;

public class CardapioItem extends LinearLayout {

    
    TextView text_nome;

    
    TextView text_preco;

    
    IdiomaController idiomaController;

    public CardapioItem(Context context) {
        super(context);
    }

    public void bind(Item item, String codigoIdioma){
//		Resources res = getResources();
//		int resID = res.getIdentifier(item.getImagem(), "drawable", getContext().getPackageName());
//		Drawable drawable = res.getDrawable(resID );
//		image.setImageDrawable(drawable);

        DecimalFormat form = new DecimalFormat("0.00");
        Idioma idioma;
        try {
            text_preco.setText("R$ " + form.format(item.getPreco()));
            idioma = idiomaController.getIdioma(item.getCodigo(), codigoIdioma);
            if(idioma == null) {
                text_nome.setText(item.toString());
            } else {
                text_nome.setText(idioma.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(getContext(), getContext().getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(getContext(), getContext().getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }
}
