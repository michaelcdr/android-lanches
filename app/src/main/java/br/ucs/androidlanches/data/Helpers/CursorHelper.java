package br.ucs.androidlanches.data.Helpers;

import android.database.Cursor;

import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.models.Produto;

public class CursorHelper
{
    public static Prato cursorToPrato(Cursor cursor)
    {
        Prato produto = new Prato();
        produto.setProdutoId(Integer.parseInt(cursor.getString(0)));
        produto.setNome(cursor.getString(1));
        produto.setDescricao(cursor.getString(2));
        produto.setPreco(Double.parseDouble(cursor.getString(3)));
        produto.setFoto(cursor.getString(4));
        produto.setServeQuantasPessoas(Integer.parseInt(cursor.getString(6)));
        produto.setTipo(cursor.getString(7));
        return produto;
    }

    public static Mesa cursorToMesa(Cursor cursor)
    {
        Mesa mesa = new Mesa();
        mesa.setMesaId(Integer.parseInt(cursor.getString(0)));
        mesa.setNumero(Integer.parseInt(cursor.getString(1)));
        return mesa;
    }

    public static Produto cursorToProduto(Cursor cursor)
    {
        Produto produto = new Produto();
        produto.setProdutoId(Integer.parseInt(cursor.getString(0)));
        produto.setNome(cursor.getString(1));
        produto.setDescricao(cursor.getString(2));
        produto.setPreco(Double.parseDouble(cursor.getString(3)));
        return produto;
    }

    public static Bebida cursorToBebida(Cursor cursor)
    {
        Bebida produto = new Bebida();
        //{PRODUTO_ID, PRODUTO_NOME, PRODUTO_DESCRICAO, PRODUTO_PRECO, PRODUTO_FOTO, PRODUTO_EMBALAGEM, PRODUTO_SERVE_QUANTAS_PESSOA, PRODUTO_TIPO};
        produto.setProdutoId(Integer.parseInt(cursor.getString(0)));
        produto.setNome(cursor.getString(1));
        produto.setDescricao(cursor.getString(2));
        produto.setPreco(Double.parseDouble(cursor.getString(3)));
        produto.setFoto(cursor.getString(4));
        produto.setEmbalagem(cursor.getString(5));
        produto.setTipo(cursor.getString(7));
        return produto;
    }

    public static PedidoItem cursorToPedidoItem(Cursor cursor)
    {
        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setPedidoItemId(Integer.parseInt(cursor.getString(0)));
        pedidoItem.setQuantidade(Integer.parseInt(cursor.getString(1)));

        Produto produto = new Produto();
        produto.setProdutoId(Integer.parseInt(cursor.getString(2)));
        produto.setNome(cursor.getString(3));
        produto.setFoto(cursor.getString(4));
        produto.setPreco(Double.parseDouble(cursor.getString(5)));

        pedidoItem.setProduto(produto);
        return pedidoItem;
    }

    public static Pedido cursorToPedido(Cursor cursor)
    {
        Mesa mesa = new Mesa();
        mesa.setMesaId(Integer.parseInt(cursor.getString(2)));
        mesa.setNumero(Integer.parseInt(cursor.getString(3)));

        Pedido pedido = new Pedido(
                Long.parseLong(cursor.getString(0)),
                Boolean.parseBoolean(cursor.getString(1)),
                mesa
        );

        return pedido;
    }
}
