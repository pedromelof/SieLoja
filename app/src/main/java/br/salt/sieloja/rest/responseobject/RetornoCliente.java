package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Cliente;

public class RetornoCliente extends Retorno {

    private List<Cliente> clientes;
    public RetornoCliente(){ super(); }
    public RetornoCliente(boolean operacaoFinalizada, String mensagem,List<Cliente> clientes){
        super(operacaoFinalizada, mensagem);
        this.clientes = clientes;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
