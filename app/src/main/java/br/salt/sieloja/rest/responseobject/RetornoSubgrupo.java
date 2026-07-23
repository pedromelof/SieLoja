package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Subgrupo;

public class RetornoSubgrupo extends Retorno{

    private List<Subgrupo> subgrupos;
    public RetornoSubgrupo() {
        super(true, "", "");
        this.subgrupos = null;
    }
    public RetornoSubgrupo(boolean operacaoFinalizada, String mensagem,
                           List<Subgrupo> subgrupos) {
        super(operacaoFinalizada, mensagem, "");
        this.subgrupos = subgrupos;
    }
    public List<Subgrupo> getSubgrupos() {
        return subgrupos;
    }
    public void setSubgrupos(List<Subgrupo> subgrupos) {
        this.subgrupos = subgrupos;
    }
}
