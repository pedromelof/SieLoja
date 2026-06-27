package br.salt.sieloja.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.salt.sieloja.R;
import br.salt.sieloja.databinding.ActivityCardapioBinding;
import br.salt.sieloja.view.adapter.CardapioAdapter;
import br.salt.sieloja.view.util.Alert;
import br.salt.sieloja.view.util.BaseActivity;

public class CardapioActivity extends BaseActivity {

    private ActivityCardapioBinding binding;
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
        binding = ActivityCardapioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inicializarViews();
    }
    private void inicializarViews() {
        context = this;
        cod_idioma = "pt_br";
        selectedItens = new ArrayList<String>();

        try {
            String[] itens = itemController.getAllItem();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_list_autocomplete, itens);
            binding.autocompletePesquisa.setAdapter(adapter);
            binding.autocompletePesquisa.setThreshold(1);
            binding.autocompletePesquisa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            binding.listViewPesquisa.setAdapter(adapterList);

            binding.buttonFiltroGrupo.setOnClickListener(v -> button_filtro_grupo());
            binding.buttonFiltroIdioma.setOnClickListener(v -> button_filtro_idioma());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert.dialog(this, getString(R.string.erro_no_sql));
        } catch (Exception e) {
            Alert.dialog(this, getString(R.string.erro_procurar_administrador) + e.getMessage());
            e.printStackTrace();
        }
    }

    final OnClickListener onClickListenerIdioma = new OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int which) {
            try {
                switch (which) {
                    case 0:
                        cod_idioma = "en_us";
                        adapterList.refresh(grupos, cod_idioma);
                        binding.buttonFiltroIdioma.setImageResource(R.mipmap.ic_bandeira_us);
                        break;
                    case 1:
                        cod_idioma = "fr_fr";
                        adapterList.refresh(grupos, cod_idioma);
                        binding.buttonFiltroIdioma.setImageResource(R.mipmap.ic_bandeira_fr);
                        break;
                    case 2:
                        cod_idioma = "it_it";
                        adapterList.refresh(grupos, cod_idioma);
                        binding.buttonFiltroIdioma.setImageResource(R.mipmap.ic_bandeira_it);
                        break;
                    case 3:
                        cod_idioma = "pt_br";
                        adapterList.refresh(grupos, cod_idioma);
                        binding.buttonFiltroIdioma.setImageResource(R.mipmap.ic_bandeira_br);
                        break;
                    case 4:
                        cod_idioma = "es_es";
                        adapterList.refresh(grupos, cod_idioma);
                        binding.buttonFiltroIdioma.setImageResource(R.mipmap.ic_bandeira_es);
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

    public void button_filtro_grupo(){
        selectedItens = new ArrayList<String>();
        Alert.dialogMultiChoiceItems(this, onClickListenerGrupo, listener, listGrupos);
    }

    public void button_filtro_idioma(){
        final String[] idiomas = {"Inglês (Estados Unidos)", "Francês (França)", "Italiano (Itália)",
                "Português (Brasil)", "Espanhol (Espanha)" };
        Alert.dialogListItems(this, onClickListenerIdioma, idiomas);
    }
}
