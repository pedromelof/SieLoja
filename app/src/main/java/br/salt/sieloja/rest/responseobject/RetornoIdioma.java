package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Idioma;

public class RetornoIdioma extends Retorno{

    private List<Idioma> idioma;
    public RetornoIdioma() {
        super(true, "", "");
        this.idioma = null;
    }
    public RetornoIdioma(boolean operacaoFinalizada, String mensagem,
                         List<Idioma> idioma) {
        super(operacaoFinalizada, mensagem, "");
        this.idioma = idioma;
    }
    public List<Idioma> getIdioma() {
        return idioma;
    }
    public void setIdioma(List<Idioma> idioma) {
        this.idioma = idioma;
    }
}
