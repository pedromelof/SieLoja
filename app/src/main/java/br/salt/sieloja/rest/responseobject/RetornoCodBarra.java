package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Cliente;
import br.salt.sieloja.bean.CodBarra;

public class RetornoCodBarra extends Retorno {

    private List<CodBarra> codBarras;

    public RetornoCodBarra(){ super(); }

    public RetornoCodBarra(boolean operacaoFinalizada, String mensagem,List<CodBarra> codBarras){
        super(operacaoFinalizada, mensagem, "");
        this.codBarras = codBarras;
    }

    public List<CodBarra> getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(List<CodBarra> codBarras) {
        this.codBarras = codBarras;
    }
}
