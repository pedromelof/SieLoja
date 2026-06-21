package br.salt.sieloja.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class Consumo implements Serializable {

    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String cartao;

    @DatabaseField
    private String mesa;

    @DatabaseField
    private String codigoGarcom;

    @DatabaseField
    private Date date;

    @DatabaseField
    private String codCliente;

    @DatabaseField
    private String codFormaPag;

    @DatabaseField
    private  String codTipoPag;

    public Consumo() {
        super();
    }

    public Consumo(String cartao, String mesa, String codigoGarcom, Date date, String codCliente, String codFormaPag, String codTipoPag) {
        super();
        this.cartao = cartao;
        this.mesa = mesa;
        this.date = date;
        this.codigoGarcom = codigoGarcom;
        this.codCliente = codCliente;
        this.codFormaPag = codFormaPag;
        this.codTipoPag = codTipoPag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCartao() {
        return cartao;
    }

    public void setCartao(String cartao) {
        this.cartao = cartao;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getCodigoGarcom() {
        return codigoGarcom;
    }

    public void setCodigoGarcom(String codigoGarcom) {
        this.codigoGarcom = codigoGarcom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public String getCodFormaPag() {
        return codFormaPag;
    }

    public String getCodTipoPag() {
        return codTipoPag;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public void setCodFormaPag(String codFormaPag) {
        this.codFormaPag = codFormaPag;
    }

    public void setCodTipoPag(String codTipoPag) {
        this.codTipoPag = codTipoPag;
    }
}

