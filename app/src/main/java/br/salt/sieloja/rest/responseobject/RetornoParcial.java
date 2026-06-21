package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Parcial;

public class RetornoParcial extends Retorno{

    private List<Parcial> parcial;

    public RetornoParcial() {
        super();
    }

    public RetornoParcial(boolean operacaoFinalizada, String mensagem, List<Parcial> parcial) {
        super(operacaoFinalizada, mensagem);
        this.parcial = parcial;
    }

    public List<Parcial> getParcial() {
        return parcial;
    }

    public void setParcial(List<Parcial> parcial) {
        this.parcial = parcial;
    }
}
