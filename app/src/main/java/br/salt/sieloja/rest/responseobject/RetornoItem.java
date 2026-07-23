package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Item;

public class RetornoItem extends Retorno{

    private List<Item> itens;

    public RetornoItem() {
        super(true, "", "");
        this.itens = null;
    }

    public RetornoItem(boolean operacaoFinalizada, String mensagem, List<Item> itens) {
        super(operacaoFinalizada, mensagem, "");
        this.itens = itens;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
}
