package br.ucs.androidlanches.data.DAO.iterfaces;

import java.util.List;

import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.models.Produto;

public interface IPedidoDAO
{
    List<Pedido> obterTodosPedidosSemPagamentoEfetuado();
    Pedido obterPedido(Long numeroPedido);
    int atualizarPedidoItem(PedidoItem pedidoItem);
    void deletarPedidoItem(PedidoItem pedidoItem);
    void adicionarPedidoItem(Long numeroPedido, int produtoId);
    Long criar(int mesaId, Produto produto);
    int pagarPedido(Pedido pedido);
    int criarUsandoPedidoApi(Pedido pedido);
    void deletarTodos();

    List<Pedido> obterTodosNaoIntegrados();

    void pagarComGorjeta(Long numeroPedido);

    void pagarSemGorjeta(Long numeroPedido);

    List<Pedido> obterTodos();
}
