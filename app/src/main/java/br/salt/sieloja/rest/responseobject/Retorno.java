package br.salt.sieloja.rest.responseobject;

public class Retorno {

    protected boolean operacaoFinalizada;
    protected String mensagem;

    public Retorno() {
        super();
    }
    public Retorno(boolean operacaoFinalizada, String mensagem) {
        super();
        this.operacaoFinalizada = operacaoFinalizada;
        this.mensagem = mensagem;
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
}
