package br.ucs.androidlanches.data.DAO.iterfaces;

import java.util.List;

import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.models.Produto;

public interface IPedidoDAO
{
    List<Pedido> obterTodosPedidosSemPagamentoEfetuado();
    Pedido obterPedido(int numeroPedido);
    int atualizarPedidoItem(PedidoItem pedidoItem);
    void deletarPedidoItem(PedidoItem pedidoItem);
    void adicionarPedidoItem(int numeroPedido, int produtoId);
    int criarPedido(int mesaId, Produto produto);
    int pagarPedido(Pedido pedido);
}
