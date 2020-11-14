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
    private Context context;
    public PedidosDAO(Context context)
    {
        this.context = context;
        SqliteHelper helper = new SqliteHelper(context);
        db = helper.getWritableDatabase();
    }

    private SQLiteDatabase conexao()
    {
        if (this.db == null || !this.db.isOpen())
        {
            SqliteHelper helper = new SqliteHelper(this.context);
            this.db = helper.getWritableDatabase();
        }
        return this.db;
    }

    public List<Pedido> obterTodosPedidosSemPagamentoEfetuado()
    {
        List<Pedido> pedidos = new ArrayList<>();

        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                       "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                       "WHERE Pedidos.pago = 0 ORDER BY  Pedidos.numero";

        Cursor cursor = conexao().rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            do {
                Pedido pedido = CursorHelper.cursorToPedido(cursor);
                pedidos.add(pedido);

                List<PedidoItem> pedidoItems = obterItensPedido(pedido.getNumero());
                pedido.setItens(pedidoItems);
            } while (cursor.moveToNext());
        }
        db.close();
        return pedidos;
    }

    public Pedido obterPedido(int numeroPedido)
    {
        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                "WHERE Pedidos.numero = ? ";

        Cursor cursor = conexao().rawQuery(query, new String[] { String.valueOf(numeroPedido) });

        if (cursor == null) {
            db.close();
            return null;
        }
        else
        {
            cursor.moveToFirst();
            Pedido pedido = CursorHelper.cursorToPedido(cursor);

            List<PedidoItem> pedidoItems = obterItensPedido(numeroPedido);
            pedido.setItens(pedidoItems);
            db.close();
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

        Cursor cursor = conexao().rawQuery(query, new String[]{String.valueOf(numeroPedido)});

        if (cursor.moveToFirst())
        {
            do {
                PedidoItem pedidoItem = CursorHelper.cursorToPedidoItem(cursor);
                itens.add(pedidoItem);
            } while (cursor.moveToNext());
        }
        db.close();
        return itens;
    }

    public int atualizarPedidoItem(PedidoItem pedidoItem)
    {
        ContentValues values = new ContentValues();
        values.put("Quantidade", pedidoItem.getQuantidade());

        int linhasAfetadas = conexao().update(
            "pedidositens", values,
            "pedidoItemId = ? ",  new String[] { String.valueOf(pedidoItem.getPedidoItemId()) }
        );
        db.close();
        return linhasAfetadas;
    }

    public void deletarPedidoItem(PedidoItem pedidoItem)
    {
        conexao().delete("pedidositens", " pedidoItemId = ? ", new String[] { String.valueOf(pedidoItem.getPedidoItemId()) });
        db.close();
    }

    public int criarPedido(int mesaId, Produto produto)
    {
        ContentValues values = new ContentValues();

        values.put("mesaId", mesaId);
        values.put("pago", 0);
        conexao().insert("Pedidos", null, values);

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
            conexao().insert("pedidositens", null, values);
            db.close();
        }
    }

    private void incrementarQuantidadeProdutoItem(int numeroPedido, int produtoId)
    {
        String query = "SELECT quantidade FROM pedidositens WHERE numero = ? AND produtoId = ? ";
        Cursor cursor = conexao().rawQuery(query, new String[] { String.valueOf(numeroPedido), String.valueOf(produtoId) });
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int qtd = Integer.parseInt(cursor.getString(0));
                qtd = qtd + 1;
                db.close();
                atualizarQuantidadePedidoItem(numeroPedido,produtoId, qtd);
            }
            db.close();
        } else
            db.close();
    }

    private int atualizarQuantidadePedidoItem(int numeroPedido, int produtoId, int qtd)
    {
        ContentValues values = new ContentValues();
        values.put("quantidade", qtd);

        int linhasAfetadas = conexao().update(
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
        Cursor cursor = conexao().rawQuery(query, new String[] { String.valueOf(numeroPedido), String.valueOf(produtoId) });


        if (cursor == null) {
            db.close();
            return false;
        }else {
            if (cursor.moveToFirst()){
                int qtd = cursor.getInt(0);
                db.close();
                return true;
            }
            db.close();
            return false;
        }
    }

    private int obterUltimoNumeroPedidoDaMesa(int mesaId)
    {
        String query = "SELECT numero FROM pedidos WHERE mesaId = ? ORDER BY numero desc";
        Cursor cursor = conexao().rawQuery(query, new String[] { String.valueOf(mesaId) });

        int numero = 0;
        if (cursor.moveToFirst())
            numero = cursor.getInt(0);

        db.close();
        return numero;
    }

    public int pagarPedido(Pedido pedido)
    {
        ContentValues values = new ContentValues();
        values.put("gorjeta", pedido.getGorjeta());
        values.put("pago", 1);

        int linhasAfetadas = conexao().update(
                "pedidos",
                values,
                 " numero = ? ",  new String[] { String.valueOf(pedido.getNumero()) }
        );
        db.close();
        return linhasAfetadas;
    }

    public void deletarTodos()
    {
        conexao().delete("PedidosItens", null, null);
        conexao().delete("Pedidos", null, null);
        db.close();
    }
}