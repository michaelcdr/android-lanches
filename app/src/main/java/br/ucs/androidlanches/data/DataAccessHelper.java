package br.ucs.androidlanches.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.models.Produto;

public class DataAccessHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 20;
    private static final String DATABASE_NAME = "AndroidLanchesDB";

    // PRODUTO ...
    private static final String PRODUTO_TABELA = "Produtos";
    private static final String PRODUTO_ID = "produtoid";
    private static final String PRODUTO_NOME = "nome";
    private static final String PRODUTO_DESCRICAO = "descricao";
    private static final String PRODUTO_PRECO = "preco";
    private static final String PRODUTO_FOTO = "foto";
    private static final String PRODUTO_SERVE_QUANTAS_PESSOA ="serveQuantasPessoas";
    private static final String PRODUTO_EMBALAGEM = "embalagem";
    private static final String PRODUTO_TIPO = "tipo";
    private static final String[] PRODUTO_COLUNAS = {PRODUTO_ID, PRODUTO_NOME, PRODUTO_DESCRICAO, PRODUTO_PRECO, PRODUTO_FOTO, PRODUTO_EMBALAGEM, PRODUTO_SERVE_QUANTAS_PESSOA, PRODUTO_TIPO};

    // PEDIDO ...
    private static final String PEDIDO_TABELA = "Pedidos";
    private static final String PEDIDO_NUMERO = "numero";
    private static final String PEDIDO_PAGO = "pago";
    private static final String PEDIDO_MESAID = "mesaId";
    private static final String PEDIDO_GORJETA = "gorjeta";
    private static final String PEDIDO_MESA_NUMERO = "numero";
    private static final String[] PEDIDO_COLUNAS = {PEDIDO_NUMERO, PEDIDO_PAGO, PEDIDO_MESAID,PEDIDO_MESA_NUMERO};

    // MESA ...
    private static final String MESA_TABELA = "Mesas";
    private static final String MESA_MESAID = "mesaId";
    private static final String MESA_NUMERO = "numero";
    private static final String[] MESA_COLUNAS = {MESA_MESAID, MESA_NUMERO};

    //TABELA PEDIDO ITENS...
    private static final String PEDIDO_ITEM_TABELA = "PedidosItens";
    private static final String PEDIDO_ITEM_PEDIDO_ITEM_ID = "pedidoItemId";
    private static final String PEDIDO_ITEM_NUMERO_PEDIDO = "numero";
    private static final String PEDIDO_ITEM_QUANTIDADE = "quantidade";
    private static final String PEDIDO_ITEM_PRODUTO_ID = "produtoId";

    public DataAccessHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sqlCreateTableMesa    = "CREATE TABLE "+ MESA_TABELA +" (mesaId INTEGER PRIMARY KEY AUTOINCREMENT, numero INTEGER) ";

        String sqlCreateTableProduto = "CREATE TABLE "+ PRODUTO_TABELA + " (" +
                PRODUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PRODUTO_NOME + " TEXT NOT NULL, " +
                PRODUTO_DESCRICAO + " TEXT NOT NULL,"+
                PRODUTO_FOTO + " TEXT,"+
                PRODUTO_PRECO + " DOUBLE, " +
                PRODUTO_TIPO + " TEXT, " +
                PRODUTO_SERVE_QUANTAS_PESSOA + " INTEGER," +
                PRODUTO_EMBALAGEM +  " TEXT)";

        String sqlCreatePedidos =       "CREATE TABLE " + PEDIDO_TABELA + " ("+
                PEDIDO_NUMERO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PEDIDO_PAGO + " INTEGER, " +
                PEDIDO_GORJETA + " DOUBLE, " +
                PEDIDO_MESAID + " INTEGER )";

        String sqlCreatePedidosItens =  "CREATE TABLE " + PEDIDO_ITEM_TABELA + " ("+
                PEDIDO_ITEM_PEDIDO_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PEDIDO_ITEM_NUMERO_PEDIDO  + " INTEGER NOT NULL, " +
                PEDIDO_ITEM_QUANTIDADE + " INTEGER NOT NULL, " +
                PEDIDO_ITEM_PRODUTO_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY ("+ PEDIDO_ITEM_NUMERO_PEDIDO +") REFERENCES "+PEDIDO_TABELA+"("+PEDIDO_NUMERO+"), " +
                "FOREIGN KEY ("+ PEDIDO_ITEM_PRODUTO_ID +") REFERENCES "+PRODUTO_TABELA+"("+PRODUTO_ID+")" +
                ")";

        db.execSQL(sqlCreateTableMesa);
        db.execSQL(sqlCreateTableProduto);
        db.execSQL(sqlCreatePedidos);
        db.execSQL(sqlCreatePedidosItens);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ PRODUTO_TABELA);
        db.execSQL("DROP TABLE IF EXISTS "+ MESA_TABELA);
        db.execSQL("DROP TABLE IF EXISTS "+ PEDIDO_ITEM_TABELA);
        db.execSQL("DROP TABLE IF EXISTS "+ PEDIDO_TABELA);
        this.onCreate(db);
    }

    public void adicionarMesa(Mesa mesa)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MESA_NUMERO, mesa.getNumero());
        db.insert(MESA_TABELA, null, values);
        db.close();
    }

    private Mesa cursorToMesa(Cursor cursor)
    {
        Mesa mesa = new Mesa();
        mesa.setMesaId(Integer.parseInt(cursor.getString(0)));
        mesa.setNumero(Integer.parseInt(cursor.getString(1)));
        return mesa;
    }

    public Mesa obterMesa(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                MESA_TABELA, MESA_COLUNAS, " " + MESA_MESAID +"  = ?",  new String[] { String.valueOf(id) },
                null, // e. group by
                null, // f. having
                null, // g. order by
                null // h. limit
        );

        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Mesa mesa = cursorToMesa(cursor);
            return mesa;
        }
    }

    public ArrayList<Mesa> obterTodasMesasDesocupadas()
    {
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();

        String query = "SELECT * FROM " + MESA_TABELA + " WHERE  "+ MESA_MESAID +
                " NOT IN (SELECT " + MESA_MESAID + " From " + PEDIDO_TABELA + " GROUP BY "+ MESA_MESAID +") ORDER BY " + MESA_NUMERO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            do {
                Mesa mesa = cursorToMesa(cursor);
                mesas.add(mesa);
            } while (cursor.moveToNext());
        }
        return mesas;
    }

    public List<Mesa> obterTodasMesas()
    {
        List<Mesa> mesas = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Mesas", null);
        if (cursor.moveToFirst())
        {
            do {
                Mesa mesa = cursorToMesa(cursor);
                mesas.add(mesa);
            } while (cursor.moveToNext());
        }
        return mesas;
    }

    public void adicionarBebida(Bebida produto)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PRODUTO_NOME, produto.getNome());
        values.put(PRODUTO_DESCRICAO, produto.getDescricao());
        values.put(PRODUTO_PRECO, produto.getPreco());
        values.put(PRODUTO_FOTO, produto.getFoto());
        values.put(PRODUTO_TIPO, "bebida");
        values.put(PRODUTO_EMBALAGEM, produto.getEmbalagem());

        db.insert(PRODUTO_TABELA, null, values);
        db.close();
    }

    public void adicionarPrato(Prato produto)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PRODUTO_NOME, produto.getNome());
        values.put(PRODUTO_DESCRICAO, produto.getDescricao());
        values.put(PRODUTO_PRECO, produto.getPreco());
        values.put(PRODUTO_FOTO, produto.getFoto());
        values.put(PRODUTO_TIPO, "prato");
        values.put(PRODUTO_SERVE_QUANTAS_PESSOA, produto.getServeQuantasPessoas());

        db.insert(PRODUTO_TABELA, null, values);
        db.close();
    }


    public Produto obterProduto(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                PRODUTO_TABELA, PRODUTO_COLUNAS, " " + PRODUTO_ID + " = ?",  new String[] { String.valueOf(id) },
                null, // e. group by
                null, // f. having
                null, // g. order by
                null // h. limit
        );

        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Produto produto = cursorToProduto(cursor);
            return produto;
        }
    }

    private Produto cursorToProduto(Cursor cursor)
    {
        Produto produto = new Produto();
        produto.setProdutoId(Integer.parseInt(cursor.getString(0)));
        produto.setNome(cursor.getString(1));
        produto.setDescricao(cursor.getString(2));
        produto.setPreco(Double.parseDouble(cursor.getString(3)));
        return produto;
    }

    private Bebida cursorToBebida(Cursor cursor)
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



    public ArrayList<Bebida> obterTodasBebidas()
    {
        ArrayList<Bebida> bebidas = new ArrayList<>();
        String colunas = android.text.TextUtils.join(",",PRODUTO_COLUNAS);
        String query = "SELECT "+colunas +" FROM " + PRODUTO_TABELA + " WHERE "+ PRODUTO_TIPO +" = 'bebida' ORDER BY " + PRODUTO_NOME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                Bebida bebida = cursorToBebida(cursor);
                bebidas.add(bebida);
            } while (cursor.moveToNext());
        }
        return bebidas;
    }

    public int atualizarProduto(Produto produto)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUTO_NOME, produto.getNome());
        values.put(PRODUTO_DESCRICAO, produto.getDescricao());
        values.put(PRODUTO_FOTO, produto.getFoto());
        values.put(PRODUTO_PRECO, new Double(produto.getPreco()));

        int linhasAfetadas = db.update(
                PRODUTO_TABELA,
                values,
                PRODUTO_ID + " = ?",  new String[] { String.valueOf(produto.getProdutoId()) }
        );

        db.close();

        return linhasAfetadas;
    }

    public int deletarProduto(Produto produto)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int linhasAfetadas = db.delete(
                PRODUTO_TABELA,
                PRODUTO_ID + " = ?", new String[] { String.valueOf(produto.getProdutoId()) }
        );
        db.close();
        return linhasAfetadas;
    }


    public List<Prato> obterTodosPratos() {
        ArrayList<Prato> pratos = new ArrayList<>();
        String colunas = android.text.TextUtils.join(",",PRODUTO_COLUNAS);
        String query = "SELECT "+colunas +" FROM " + PRODUTO_TABELA + " WHERE "+ PRODUTO_TIPO +" = 'prato' ORDER BY " + PRODUTO_NOME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                Prato prato = cursorToPrato(cursor);
                pratos.add(prato);
            } while (cursor.moveToNext());
        }
        return pratos;
    }

    private Prato cursorToPrato(Cursor cursor)
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

    public int criarPedido(int mesaId, Produto produto)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PEDIDO_MESAID, mesaId);
        values.put(PEDIDO_PAGO, 0);
        db.insert(PEDIDO_TABELA, null, values);

        int numeroPedido = obterUltimoNumeroPedidoDaMesa(mesaId);
        db.close();

        adicionarPedidoItem(numeroPedido, produto.getProdutoId());
        return numeroPedido;
    }

    public void adicionarPedidoItem(int numeroPedido, int produtoId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (temPedidoItem(numeroPedido, produtoId)) {
            incrementarQuantidadeProdutoItem(numeroPedido,produtoId);
        }  else {
            values.put(PEDIDO_ITEM_NUMERO_PEDIDO, numeroPedido);
            values.put(PEDIDO_ITEM_QUANTIDADE, 1);
            values.put(PEDIDO_ITEM_PRODUTO_ID, produtoId);
            db.insert(PEDIDO_ITEM_TABELA, null, values);
            db.close();
        }
    }

    private void incrementarQuantidadeProdutoItem(int numeroPedido, int produtoId)
    {
        String query = "SELECT "+PEDIDO_ITEM_QUANTIDADE+" FROM " + PEDIDO_ITEM_TABELA + " WHERE " + PEDIDO_ITEM_NUMERO_PEDIDO + " = ? AND " + PEDIDO_ITEM_PRODUTO_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PEDIDO_ITEM_QUANTIDADE, qtd);

        int linhasAfetadas = db.update(
                PEDIDO_ITEM_TABELA,
                values,
                PEDIDO_ITEM_NUMERO_PEDIDO + " = ? , " + PEDIDO_ITEM_PRODUTO_ID + " = ? ",  new String[] { String.valueOf(numeroPedido), String.valueOf(produtoId) }
        );

        db.close();

        return linhasAfetadas;
    }

    private boolean temPedidoItem(int numeroPedido, int produtoId)
    {
        String query = "SELECT "+PEDIDO_ITEM_QUANTIDADE+" FROM " + PEDIDO_ITEM_TABELA + " WHERE " + PEDIDO_ITEM_NUMERO_PEDIDO + " = ? AND " + PEDIDO_ITEM_PRODUTO_ID + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
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
        String query = "SELECT "+ PEDIDO_NUMERO+" FROM " + PEDIDO_TABELA + " WHERE mesaId = ? ORDER BY numero desc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(mesaId) });

        int numero = 0;
        if (cursor.moveToFirst())
        {
            numero = cursor.getInt(0);
        }
        return numero;
    }

    public int pagarPedido(Pedido pedido){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PEDIDO_GORJETA, pedido.getGorjeta());
        values.put(PEDIDO_PAGO, 1);

        int linhasAfetadas = db.update(
                PEDIDO_TABELA,
                values,
                PEDIDO_NUMERO + " = ? ",  new String[] { String.valueOf(pedido.getNumero()) }
        );

        db.close();
        return linhasAfetadas;
    }


    private Pedido cursorToPedido(Cursor cursor)
    {
        Mesa mesa = new Mesa();
        mesa.setMesaId(Integer.parseInt(cursor.getString(2)));
        mesa.setNumero(Integer.parseInt(cursor.getString(3)));

        Pedido pedido = new Pedido(
                Integer.parseInt(cursor.getString(0)),
                Boolean.parseBoolean(cursor.getString(1)),
                mesa
        );

        return pedido;
    }

    public List<Pedido> obterTodosPedidosSemPagamentoEfetuado()
    {
        List<Pedido> pedidos = new ArrayList<>();

        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                "WHERE Pedidos.pago = 0 ORDER BY  Pedidos.numero";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            do {
                Pedido pedido = cursorToPedido(cursor);
                pedidos.add(pedido);
            } while (cursor.moveToNext());
        }

        return pedidos;
    }

    public Pedido obterPedido(int numeroPedido)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                "INNER JOIN Mesas  ON Pedidos.mesaId = Mesas.mesaId " +
                "WHERE Pedidos.numero = ? ";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(numeroPedido) });

        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Pedido pedido = cursorToPedido(cursor);

            List<PedidoItem> itens = obterItensPedido(numeroPedido);
            pedido.setItens(itens);
            return pedido;
        }
    }

    private List<PedidoItem> obterItensPedido(int numeroPedido)
    {
        ArrayList<PedidoItem> itens = new ArrayList<>();

        String query = "SELECT " + PEDIDO_ITEM_TABELA + "." + PEDIDO_ITEM_PEDIDO_ITEM_ID + ","+
                PEDIDO_ITEM_TABELA + "." + PEDIDO_ITEM_QUANTIDADE  + ","+
                PEDIDO_ITEM_TABELA + "." + PEDIDO_ITEM_PRODUTO_ID  + ","+
                PRODUTO_TABELA + "." + PRODUTO_NOME + "," +
                PRODUTO_TABELA + "." + PRODUTO_FOTO + "," +
                PRODUTO_TABELA + "." + PRODUTO_PRECO +
                " FROM " + PEDIDO_ITEM_TABELA +
                " INNER JOIN " + PRODUTO_TABELA + " ON " + PRODUTO_TABELA + "." + PRODUTO_ID + " = " + PEDIDO_ITEM_TABELA + "." + PEDIDO_ITEM_PRODUTO_ID +" "+
                " WHERE "+PEDIDO_ITEM_TABELA+"." + PEDIDO_ITEM_NUMERO_PEDIDO + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(numeroPedido)});

        if (cursor.moveToFirst())
        {
            do {
                PedidoItem pedidoItem = cursorToPedidoItem(cursor);
                itens.add(pedidoItem);
            } while (cursor.moveToNext());
        }
        return itens;
    }

    private PedidoItem cursorToPedidoItem(Cursor cursor)
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
}
