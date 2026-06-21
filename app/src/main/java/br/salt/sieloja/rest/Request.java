package br.salt.sieloja.rest;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import br.salt.sieloja.rest.responseobject.Envio;
import br.salt.sieloja.rest.responseobject.EnvioConsumo;
import br.salt.sieloja.rest.responseobject.EnvioParcial;
import br.salt.sieloja.rest.responseobject.Retorno;
import br.salt.sieloja.rest.responseobject.RetornoCliente;
import br.salt.sieloja.rest.responseobject.RetornoCodBarra;
import br.salt.sieloja.rest.responseobject.RetornoEmpresa;
import br.salt.sieloja.rest.responseobject.RetornoFormaPagamento;
import br.salt.sieloja.rest.responseobject.RetornoGrupo;
import br.salt.sieloja.rest.responseobject.RetornoIdioma;
import br.salt.sieloja.rest.responseobject.RetornoItem;
import br.salt.sieloja.rest.responseobject.RetornoParcial;
import br.salt.sieloja.rest.responseobject.RetornoSubgrupo;
import br.salt.sieloja.rest.responseobject.RetornoTipoPag;
import br.salt.sieloja.rest.responseobject.RetornoUsuario;

@Rest(converters = {MappingJackson2HttpMessageConverter.class})
public interface Request {

    @Post("webresources/usuario")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoUsuario requestUsuario(@Body Envio envio);

    @Put("webresources/item/cardapio")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoItem requestItem(@Body Envio envio);

    @Put("webresources/item/codbarra")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoCodBarra requestCodBarra(@Body Envio envio);

    @Put("webresources/idioma")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoIdioma requestIdioma(@Body Envio envio);

    @Put("webresources/grupo/tudo")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoGrupo requestGrupo(@Body Envio envio);

    @Put("webresources/subgrupo/tudo")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoSubgrupo requestSubgrupo(@Body Envio envio);

    @Put("webresources/empresa")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoEmpresa requestEmpresa(@Body Envio envio);

//	@Put("webresources/parcialVenda")
//	@Accept(MediaType.APPLICATION_JSON)
//	List<Parcial> requestParcial(String string);
//
//	@Put("webresources/parcialRestaurante")
//	@Accept(MediaType.APPLICATION_JSON)
//	List<Parcial> requestImprimirParcial(String string);

    @Put("webresources/parcial/comercio")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoParcial requestParcial(@Body EnvioParcial envio);

    @Put("webresources/parcial/imprimir")
    @Accept(MediaType.APPLICATION_JSON)
    Retorno requestImprimirParcial(@Body EnvioConsumo envio);

    @Put("webresources/lancamento/venda")
    @Accept(MediaType.APPLICATION_JSON)
    Retorno requestVenda(@Body EnvioConsumo envio);

    @Put("webresources/cliente")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoCliente requestCliente(@Body Envio envio);

    @Post("webresources/tab/formaPag")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoFormaPagamento requestFormaPag(@Body Envio envio);

    @Post("webresources/tab/tipoPag")
    @Accept(MediaType.APPLICATION_JSON)
    RetornoTipoPag requestTipoPag(@Body Envio envio);

    void setRootUrl(String rootUrl);
}
