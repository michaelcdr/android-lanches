package br.ucs.androidlanches.rest.services;

import java.util.List;

import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Pedido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IPedidoService
{
    @GET("/v1/pedidos/ObterTodosSemPagamentoEfetuado")
    Call<List<Pedido>> obterTodosSemPagamentoEfetuado();

    @GET("/v1/pedidos/ObterPorNumero/{numeroPedido}")
    Call<Pedido> obter(@Path("numeroPedido") Long numeroPedido);

    @PUT("/v1/pedidos/IncrementarQuantidadeProduto/{pedidoItemId}")
    Call<Void> incrementarQtdProduto(@Path("pedidoItemId") int pedidoItemId);

    @PUT("/v1/pedidos/DecrementarQuantidadeProduto/{pedidoItemId}")
    Call<Void> decrementarQtdProduto(@Path("pedidoItemId") int pedidoItemId);

    @POST("/v1/pedidos/{mesaId}/{produtoId}")
    Call<Long> criar(@Path("mesaId") int mesaId, @Path("produtoId") int produtoId);

    @POST("/v1/pedidos/")
    Call<Long> criar(@Body Pedido pedido);

    @POST("/v1/pedidos/AdicionarItem/{numeroPedido}/{produtoId}")
    Call<Void> adicionarItem(@Path("numeroPedido") Long numeroPedido, @Path("produtoId") int produtoId);

    @GET("/v1/pedidos/")
    Call<List<Pedido>> obterTodos();

    @PUT("/v1/pedidos/PagarComGorjeta/{numeroPedido}")
    Call<Void> pagarComGorjeta(@Path("numeroPedido") Long numeroPedido);

    @PUT("/v1/pedidos/Pagar/{numeroPedido}")
    Call<Void> pagar(@Path("numeroPedido") Long numeroPedido);
}
