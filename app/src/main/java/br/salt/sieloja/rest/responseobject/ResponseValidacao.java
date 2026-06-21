package br.salt.sieloja.rest.responseobject;

public class ResponseValidacao {

    public boolean realizado;
    public String erro;

    public ResponseValidacao() {
        super();
    }

    public ResponseValidacao(boolean realizado, String erro) {
        super();
        this.realizado = realizado;
        this.erro = erro;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
}

