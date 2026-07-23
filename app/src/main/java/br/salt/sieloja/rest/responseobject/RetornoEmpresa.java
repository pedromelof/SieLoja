package br.salt.sieloja.rest.responseobject;

import java.util.List;

import br.salt.sieloja.bean.Empresa;

public class RetornoEmpresa extends Retorno  {

    private List<Empresa> empresas;
    public RetornoEmpresa() {
        super();
    }
    public RetornoEmpresa(boolean operacaoFinalizada, String mensagem,
                          List<Empresa> empresas) {
        super(operacaoFinalizada, mensagem, "");
        this.empresas = empresas;
    }
    public List<Empresa> getEmpresas() {
        return empresas;
    }
    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }
}
