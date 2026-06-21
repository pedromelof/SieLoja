package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Empresa;
import br.salt.sieloja.bean.FormaPagamento;

public class RetornoFormaPagamento extends Retorno {

    private List<FormaPagamento> formaPag;
    public RetornoFormaPagamento() {
        super();
    }
    public RetornoFormaPagamento(boolean operacaoFinalizada, String mensagem,
                          List<FormaPagamento> formaPag) {
        super(operacaoFinalizada, mensagem);
        this.formaPag = formaPag;
    }

    public List<FormaPagamento> getFormaPag() {
        return formaPag;
    }

    public void setFormaPag(List<FormaPagamento> formaPag) {
        this.formaPag = formaPag;
    }
}
