package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Usuario;

public class RetornoUsuario extends Retorno{

    private List<Usuario> usuarios;

    public RetornoUsuario() {
        super();
    }

    public RetornoUsuario(boolean operacaoFinalizada, String mensagem, List<Usuario> usuarios) {
        super(operacaoFinalizada, mensagem);
        this.usuarios = usuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
