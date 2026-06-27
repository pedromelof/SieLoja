package br.salt.sieloja.rest;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
import retrofit2.http.Url;

public interface Request {
    @POST("webresources/usuario")
    Call<RetornoUsuario> requestUsuario(@Body Envio envio);

    @PUT("webresources/item/cardapio")
    Call<RetornoItem> requestItem(@Body Envio envio);

    @PUT("webresources/item/codbarra")
    Call<RetornoCodBarra> requestCodBarra(@Body Envio envio);

    @PUT("webresources/idioma")
    Call<RetornoIdioma> requestIdioma(@Body Envio envio);

    @PUT("webresources/grupo/tudo")
    Call<RetornoGrupo> requestGrupo(@Body Envio envio);

    @PUT("webresources/subgrupo/tudo")
    Call<RetornoSubgrupo> requestSubgrupo(@Body Envio envio);

    @PUT("webresources/empresa")
    Call<RetornoEmpresa> requestEmpresa(@Body Envio envio);

    @PUT("webresources/parcial/comercio")
    Call<RetornoParcial> requestParcial(@Body EnvioParcial envio);

    @PUT("webresources/parcial/imprimir")
    Call<Retorno> requestImprimirParcial(@Body EnvioConsumo envio);

    @PUT("webresources/lancamento/venda")
    Call<Retorno> requestVenda(@Body EnvioConsumo envio);

    @PUT("webresources/cliente")
    Call<RetornoCliente> requestCliente(@Body Envio envio);

    @POST("webresources/tab/formaPag")
    Call<RetornoFormaPagamento> requestFormaPag(@Body Envio envio);

    @POST("webresources/tab/tipoPag")
    Call<RetornoTipoPag> requestTipoPag(@Body Envio envio);
}
