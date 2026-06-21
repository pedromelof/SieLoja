package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Grupo;

public class RetornoGrupo extends Retorno {

    private List<Grupo> grupos;
    public RetornoGrupo() {
        super();
    }
    public RetornoGrupo(boolean operacaoFinalizada, String mensagem,
                        List<Grupo> grupos) {
        super(operacaoFinalizada, mensagem);
        this.grupos = grupos;
    }
    public List<Grupo> getGrupos() {
        return grupos;
    }
    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }
}
