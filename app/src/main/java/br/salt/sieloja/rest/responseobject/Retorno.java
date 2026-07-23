package br.salt.sieloja.rest.responseobject;

public class Retorno {

    protected boolean operacaoFinalizada;
    protected String mensagem;
    protected String numPed;

    public Retorno() {
        super();
    }
    public Retorno(boolean operacaoFinalizada, String mensagem, String numPed) {
        super();
        this.operacaoFinalizada = operacaoFinalizada;
        this.mensagem = mensagem;
        this.numPed = numPed;
    }
    public boolean isOperacaoFinalizada() {
        return operacaoFinalizada;
    }
    public void setOperacaoFinalizada(boolean operacaoFinalizada) {
        this.operacaoFinalizada = operacaoFinalizada;
    }
    public String getMensagem() {
        return mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public String getNumPed() {
        return numPed;
    }
    public void setNumPed(String numPed) {
        this.numPed = numPed;
    }
}
