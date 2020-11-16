package br.ucs.androidlanches.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public List<Pedido> obterTodosNaoIntegrados()
    {
        List<Pedido> pedidos = new ArrayList<>();

        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                        "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                        "WHERE Pedidos.pedidoIdApi = null ORDER BY  Pedidos.numero";

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

    public List<Pedido> obterTodos()
    {
        List<Pedido> pedidos = new ArrayList<>();

        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                " ORDER BY  Pedidos.numero";

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

    @Override
    public void pagarComGorjeta(Long numeroPedido) {
        ContentValues values = new ContentValues();
        values.put("pago", 1);
        values.put("gorjeta", 1);

        int linhasAfetadas = conexao().update(
            "pedidos", values, " numero = ?  ",
            new String[] { String.valueOf(numeroPedido) }
        );
        db.close();
    }

    @Override
    public void pagarSemGorjeta(Long numeroPedido) {
        ContentValues values = new ContentValues();
        values.put("pago", 1);
        values.put("gorjeta", 0);

        int linhasAfetadas = conexao().update(
                "pedidos", values, " numero = ?  ",
                new String[] { String.valueOf(numeroPedido) }
        );
        db.close();
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

    public Pedido obterPedido(Long numeroPedido)
    {
        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                "WHERE Pedidos.numero = ? ";

        Cursor cursor = conexao().rawQuery(query, new String[] { String.valueOf(numeroPedido) });

        if (cursor == null)
        {
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

    private List<PedidoItem> obterItensPedido(Long numeroPedido)
    {
        ArrayList<PedidoItem> itens = new ArrayList<>();

        String query = "SELECT pedidositens.pedidoItemId, pedidositens.Quantidade , pedidositens.produtoId , produtos.Nome, " +
                        "produtos.Foto , produtos.Preco " +
                        " FROM pedidositens " +
                        " INNER JOIN produtos ON produtos.produtoId  = pedidositens.produtoId "+
                        " INNER JOIN pedidos on pedidositens.pedidoid = pedidos.pedidoid " +
                        " WHERE pedidos.numero = ?";

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
        conexao().delete("pedidositens", " pedidoItemId = ? ", new String[] {
            String.valueOf(pedidoItem.getPedidoItemId())
        });
        db.close();
    }

    private Long gerarNumeroPedido()
    {
        Date data = new Date();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String dataFormatada = df.format(data);
        return Long.parseLong(dataFormatada);
    }

    public Long criar(int mesaId, Produto produto)
    {
        Long numeroPedido = gerarNumeroPedido();
        ContentValues values = new ContentValues();

        values.put("mesaId", mesaId);
        values.put("pago", 0);
        values.put("gorjeta", 0);
        values.put("numero", numeroPedido);

        long pedidoId = conexao().insert("Pedidos", null, values);

        adicionarPedidoItem(numeroPedido, produto.getProdutoId());
        db.close();

        return numeroPedido;
    }

    public void adicionarPedidoItem(Long numeroPedido, int produtoId)
    {
        ContentValues values = new ContentValues();
        Cursor cursor = conexao().rawQuery(
        "select pedidoId from pedidos where numero = ? ", new String[]{ String.valueOf(numeroPedido) }
        );

        cursor.moveToFirst();
        int pedidoId = cursor.getInt(0);

        if (temPedidoItem(numeroPedido, produtoId))
            incrementarQuantidadeProdutoItem(numeroPedido, produtoId);
        else
        {
            values.put("pedidoId", pedidoId);
            values.put("quantidade", 1);
            values.put("produtoId", produtoId);
            conexao().insert("pedidositens", null, values);
            db.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int criarUsandoPedidoApi(Pedido pedidoApi)
    {
        // deletando antigos...
       /*
        int linhasAfetadas = conexao().delete(
              "pedidositens",
        " pedidoId in (select pedidoId from pedidos where pedidoIdApi = ? ) ",
                new String[] { String.valueOf(pedidoApi.getId())
        });
        conexao().delete("pedidos", " pedidoIdApi = ? ", new String[] { String.valueOf(pedidoApi.getId()) });
        db.close();*/

        ContentValues values = new ContentValues();
        values.put("mesaId", pedidoApi.getMesaId());
        values.put("pago", pedidoApi.getPago());
        values.put("pedidoIdApi",pedidoApi.getId());
        values.put("numero",pedidoApi.getNumero());

        int pedidoId = (int)conexao().insert("Pedidos", null, values);

        if (pedidoApi != null && pedidoApi.getItens() != null)
        {
            pedidoApi.getItens().forEach(pedidoItem -> {
                pedidoItem.setPedidoId(pedidoId);
                adicionarPedidoItem(pedidoItem);
            });
        }
        return pedidoId;
    }

    private void adicionarPedidoItem(PedidoItem pedidoItem)
    {
        ContentValues values = new ContentValues();
        values.put("pedidoId", pedidoItem.getPedidoId());
        values.put("quantidade", pedidoItem.getQuantidade());
        values.put("produtoId", pedidoItem.getProduto().getProdutoId());
        conexao().insert("pedidositens", null, values);
        db.close();
    }

    private void incrementarQuantidadeProdutoItem(Long numeroPedido, int produtoId)
    {
        String query = "SELECT quantidade, pedidositens.pedidoId FROM pedidositens " +
                       "INNER JOIN pedidos on pedidositens.pedidoid = pedidos.pedidoid " +
                       "WHERE pedidos.numero = ? AND pedidositens.produtoId = ? ";

        Cursor cursor = conexao().rawQuery(query, new String[] { String.valueOf(numeroPedido), String.valueOf(produtoId) });

        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int qtd = Integer.parseInt(cursor.getString(0));
                int pedidoId = cursor.getInt(1);
                qtd = qtd + 1;
                db.close();
                atualizarQuantidadePedidoItem(pedidoId, produtoId, qtd);
            }
            db.close();
        } else
            db.close();
    }

    private int atualizarQuantidadePedidoItem(int pedidoId, int produtoId, int qtd)
    {
        ContentValues values = new ContentValues();
        values.put("quantidade", qtd);

        int linhasAfetadas = conexao().update(
            "pedidositens",
            values,
            " pedidoId = ? and produtoId  = ? ",  new String[] { String.valueOf(pedidoId), String.valueOf(produtoId) }
        );
        db.close();

        return linhasAfetadas;
    }

    private boolean temPedidoItem(Long numeroPedido, int produtoId)
    {
        String query = "SELECT quantidade FROM pedidositens " +
                       "INNER JOIN pedidos on pedidositens.pedidoid = pedidos.pedidoid WHERE pedidos.numero = ? AND pedidositens.produtoId = ? ";

        Cursor cursor = conexao().rawQuery(query, new String[] { String.valueOf(numeroPedido), String.valueOf(produtoId) });

        if (cursor == null)
        {
            db.close();
            return false;
        }
        else {
            if (cursor.moveToFirst()){
                int qtd = cursor.getInt(0);
                db.close();
                return true;
            }
            db.close();
            return false;
        }
    }

    private Long obterUltimoNumeroPedidoDaMesa(int mesaId)
    {
        String query = "SELECT numero FROM pedidos WHERE mesaId = ? ORDER BY numero desc";
        Cursor cursor = conexao().rawQuery(query, new String[] { String.valueOf(mesaId) });

        Long numero = 0L;
        if (cursor.moveToFirst())
            numero = cursor.getLong(0);

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