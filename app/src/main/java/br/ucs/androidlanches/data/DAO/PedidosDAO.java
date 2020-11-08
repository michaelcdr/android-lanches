package br.ucs.androidlanches.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.iterfaces.IPedidoDAO;
import br.ucs.androidlanches.data.Helpers.CursorHelper;
import br.ucs.androidlanches.data.Helpers.SqliteHelper;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.models.Produto;

public class PedidosDAO implements IPedidoDAO
{
    private SQLiteDatabase db;

    public PedidosDAO(Context context)
    {
        SqliteHelper helper = new SqliteHelper(context);
        db = helper.getWritableDatabase();
    }

    public List<Pedido> obterTodosPedidosSemPagamentoEfetuado()
    {
        List<Pedido> pedidos = new ArrayList<>();

        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                       "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                       "WHERE Pedidos.pago = 0 ORDER BY  Pedidos.numero";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            do {
                Pedido pedido = CursorHelper.cursorToPedido(cursor);
                pedidos.add(pedido);
            } while (cursor.moveToNext());
        }

        return pedidos;
    }

    public Pedido obterPedido(int numeroPedido)
    {
        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                "WHERE Pedidos.numero = ? ";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(numeroPedido) });

        if (cursor == null)
            return null;
        else
        {
            cursor.moveToFirst();
            Pedido pedido = CursorHelper.cursorToPedido(cursor);

            List<PedidoItem> pedidoItems = obterItensPedido(numeroPedido);
            pedido.setItens(pedidoItems);
            return pedido;
        }
    }

    private List<PedidoItem> obterItensPedido(int numeroPedido)
    {
        ArrayList<PedidoItem> itens = new ArrayList<>();

        String query = "SELECT pedidositens.pedidoItemId, pedidositens.Quantidade , pedidositens.produtoId , produtos.Nome, " +
                        "produtos.Foto , produtos.Preco " +
                        " FROM pedidositens " +
                        " INNER JOIN produtos ON produtos.produtoId  = pedidositens.produtoId "+
                        " WHERE pedidositens.numero = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(numeroPedido)});

        if (cursor.moveToFirst())
        {
            do {
                PedidoItem pedidoItem = CursorHelper.cursorToPedidoItem(cursor);
                itens.add(pedidoItem);
            } while (cursor.moveToNext());
        }
        return itens;
    }

    public int atualizarPedidoItem(PedidoItem pedidoItem)
    {
        ContentValues values = new ContentValues();
        values.put("Quantidade", pedidoItem.getQuantidade());

        int linhasAfetadas = db.update(
            "pedidositens", values,
            "pedidoItemId = ? ",  new String[] { String.valueOf(pedidoItem.getPedidoItemId()) }
        );
        db.close();
        return linhasAfetadas;
    }

    public void deletarPedidoItem(PedidoItem pedidoItem)
    {
        db.delete("pedidositens", " pedidoItemId = ? ", new String[] { String.valueOf(pedidoItem.getPedidoItemId()) });
        db.close();
    }

    public int criarPedido(int mesaId, Produto produto)
    {
        ContentValues values = new ContentValues();

        values.put("mesaId", mesaId);
        values.put("pago", 0);
        db.insert("Pedidos", null, values);

        int numeroPedido = obterUltimoNumeroPedidoDaMesa(mesaId);
        adicionarPedidoItem(numeroPedido, produto.getProdutoId());

        db.close();
        return numeroPedido;
    }

    public void adicionarPedidoItem(int numeroPedido, int produtoId)
    {
        ContentValues values = new ContentValues();

        if (temPedidoItem(numeroPedido, produtoId))
            incrementarQuantidadeProdutoItem(numeroPedido,produtoId);
        else
        {
            values.put("numero", numeroPedido);
            values.put("quantidade", 1);
            values.put("produtoId", produtoId);
            db.insert("pedidositens", null, values);
            db.close();
        }
    }

    private void incrementarQuantidadeProdutoItem(int numeroPedido, int produtoId)
    {
        String query = "SELECT quantidade FROM pedidositens WHERE numero = ? AND produtoId = ? ";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(numeroPedido), String.valueOf(produtoId) });

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int qtd = Integer.parseInt(cursor.getString(0));
                qtd = qtd + 1;
                atualizarQuantidadePedidoItem(numeroPedido,produtoId, qtd);
            }
        }
    }

    private int atualizarQuantidadePedidoItem(int numeroPedido, int produtoId, int qtd)
    {
        ContentValues values = new ContentValues();
        values.put("quantidade", qtd);

        int linhasAfetadas = db.update(
                "pedidositens",
                values,
                 " numero = ? and produtoId  = ? ",  new String[] { String.valueOf(numeroPedido), String.valueOf(produtoId) }
        );

        db.close();
        return linhasAfetadas;
    }

    private boolean temPedidoItem(int numeroPedido, int produtoId)
    {
        String query = "SELECT quantidade FROM pedidositens WHERE numero = ? AND produtoId = ? ";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(numeroPedido), String.valueOf(produtoId) });

        if (cursor == null )
            return false;
        else {
            if(cursor.moveToFirst()){

                int qtd = cursor.getInt(0);
                return true;
            }

            return false;
        }
    }

    private int obterUltimoNumeroPedidoDaMesa(int mesaId)
    {
        String query = "SELECT numero FROM pedidos WHERE mesaId = ? ORDER BY numero desc";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(mesaId) });

        int numero = 0;
        if (cursor.moveToFirst())
        {
            numero = cursor.getInt(0);
        }
        return numero;
    }

    public int pagarPedido(Pedido pedido)
    {
        ContentValues values = new ContentValues();
        values.put("gorjeta", pedido.getGorjeta());
        values.put("pago", 1);

        int linhasAfetadas = db.update(
                "pedidos",
                values,
                 " numero = ? ",  new String[] { String.valueOf(pedido.getNumero()) }
        );

        db.close();
        return linhasAfetadas;
    }

    @Override
    public void deletarTodos() {
        db.delete("PedidosItens", null, null);
        db.delete("Pedidos", null, null);
        db.close();
    }
}