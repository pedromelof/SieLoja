package br.salt.sieloja.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.salt.sieloja.R;
import br.salt.sieloja.view.adapter.CardapioAdapter;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

public class CardapioActivity extends BaseActivity {

    @ViewById
    ListView list_view_pesquisa;

    @ViewById
    AutoCompleteTextView autocomplete_pesquisa;

    @ViewById
    ImageButton button_filtro_idioma;

    @Bean
    CardapioAdapter adapterList;

    private Context context;
    private String cod_idioma;
    private List<String> grupos;
    private List<String> selectedItens;
    private String[] listGrupos;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
    }

    final OnClickListener onClickListenerIdioma = new OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int which) {
            try {
                switch (which) {
                    case 0:
                        cod_idioma = "en_us";
                        adapterList.refresh(grupos, cod_idioma);
                        button_filtro_idioma.setImageResource(R.mipmap.ic_bandeira_us);
                        break;
                    case 1:
                        cod_idioma = "fr_fr";
                        adapterList.refresh(grupos, cod_idioma);
                        button_filtro_idioma.setImageResource(R.mipmap.ic_bandeira_fr);
                        break;
                    case 2:
                        cod_idioma = "it_it";
                        adapterList.refresh(grupos, cod_idioma);
                        button_filtro_idioma.setImageResource(R.mipmap.ic_bandeira_it);
                        break;
                    case 3:
                        cod_idioma = "pt_br";
                        adapterList.refresh(grupos, cod_idioma);
                        button_filtro_idioma.setImageResource(R.mipmap.ic_bandeira_br);
                        break;
                    case 4:
                        cod_idioma = "es_es";
                        adapterList.refresh(grupos, cod_idioma);
                        button_filtro_idioma.setImageResource(R.mipmap.ic_bandeira_es);
                        break;
                    default:
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert.dialog(context, getString(R.string.erro_no_sql));
            } catch (Exception e) {
                Alert.dialog(context, getString(R.string.erro_procurar_administrador) + e.getMessage());
                e.printStackTrace();
            }
        }
    };

    final OnClickListener onClickListenerGrupo = new OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int which) {
            try {
                grupos = selectedItens;
                adapterList.refresh(selectedItens, cod_idioma);
            } catch (SQLException e) {
                e.printStackTrace();
                Alert.dialog(context, getString(R.string.erro_no_sql));
            } catch (Exception e) {
                Alert.dialog(context, getString(R.string.erro_procurar_administrador) + e.getMessage());
                e.printStackTrace();
            }
        }
    };

    final OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            if(isChecked){
                selectedItens.add(listGrupos[which]);
            } else if (selectedItens.contains(listGrupos[which])){
                selectedItens.remove(listGrupos[which]);
            }
        }
    };

    @AfterViews
    void afiterViews(){
        context = this;
        cod_idioma = "pt_br";
        selectedItens = new ArrayList<String>();

        try {
            String[] itens = itemController.getAllItem();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_list_autocomplete, itens);
            autocomplete_pesquisa.setAdapter(adapter);
            autocomplete_pesquisa.setThreshold(1);
            autocomplete_pesquisa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        adapterList.refresh(adapter.getItem(position), cod_idioma);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert.dialog(context, getString(R.string.erro_no_sql));
                    } catch (Exception e) {
                        Alert.dialog(context, getString(R.string.erro_procurar_administrador) + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

            listGrupos = grupoController.getAllGrupo();
            grupos = grupoController.getAllGrupoArrayList();
            adapterList.refresh(grupos, cod_idioma);
            list_view_pesquisa.setAdapter(adapterList);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    @Click
    public void button_filtro_grupo(){
        selectedItens = new ArrayList<String>();
        Alert.dialogMultiChoiceItems(this, onClickListenerGrupo, listener, listGrupos);
    }

    @Click
    public void button_filtro_idioma(){
        final String[] idiomas = {"Inglês (Estados Unidos)", "Francês (França)", "Italiano (Itália)",
                "Português (Brasil)", "Espanhol (Espanha)" };
        Alert.dialogListItems(this, onClickListenerIdioma, idiomas);
    }
}
