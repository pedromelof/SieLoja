package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.FormaPagamento;
import br.salt.sieloja.bean.TipoPagamento;

public class RetornoTipoPag  extends Retorno  {

    private List<TipoPagamento> tipoPag;
    public RetornoTipoPag() {
        super();
    }
    public RetornoTipoPag(boolean operacaoFinalizada, String mensagem,
                                 List<TipoPagamento> tipoPag) {
        super(operacaoFinalizada, mensagem, "");
        this.tipoPag = tipoPag;
    }

    public void setTipoPag(List<TipoPagamento> tipoPag) {
        this.tipoPag = tipoPag;
    }

    public List<TipoPagamento> getTipoPag() {
        return tipoPag;
    }
}
