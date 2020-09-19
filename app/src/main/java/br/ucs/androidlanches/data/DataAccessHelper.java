package br.ucs.androidlanches.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.Produto;

public class DataAccessHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION =6;
    private static final String DATABASE_NAME = "AndroidLanchesDB";

    // PRODUTO ...
    private static final String TABELA_PRODUTOS = "Produtos";
    private static final String PRODUTOID = "produtoid";
    private static final String NOME = "nome";
    private static final String DESCRICAO = "descricao";
    private static final String PRECO = "preco";
    private static final String FOTO = "foto";

    private static final String[] COLUNAS_PRODUTO = {PRODUTOID, NOME, DESCRICAO, PRECO,FOTO};

    // PEDIDO ...
    private static final String TABELA_PEDIDOS = "Pedidos";
    private static final String NUMERO_PEDIDO = "numero";
    private static final String PAGO_PEDIDO = "pago";
    private static final String MESAID_PEDIDO = "mesaId";
    private static final String[] COLUNAS_PEDIDO = {NUMERO_PEDIDO, PAGO_PEDIDO, MESAID_PEDIDO};

    // MESA ...
    private static final String TABELA_MESAS = "Mesas";
    private static final String MESAID = "mesaId";
    private static final String NUMERO_MESA = "numero";
    private static final String[] COLUNAS_MESA = {MESAID, NUMERO_MESA};

    public DataAccessHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+TABELA_MESAS+" (mesaId INTEGER PRIMARY KEY AUTOINCREMENT, numero INTEGER) ");

        String sqlCreateTable = "CREATE TABLE "+ TABELA_PRODUTOS + " ("+
                PRODUTOID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                NOME + " TEXT,"+
                DESCRICAO + " TEXT,"+
                FOTO + " TEXT,"+
                PRECO + " DOUBLE)";

        db.execSQL(sqlCreateTable);

        db.execSQL(
            "CREATE TABLE " + TABELA_PEDIDOS + " ("+
            NUMERO_PEDIDO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PAGO_PEDIDO + " INTEGER, " +
            MESAID_PEDIDO + " INTEGER )"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_PRODUTOS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_MESAS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_PEDIDOS);
        this.onCreate(db);
    }

    public void adicionarMesa(Mesa mesa)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NUMERO_MESA, mesa.getNumero());
        db.insert(TABELA_MESAS, null, values);
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
                TABELA_MESAS, COLUNAS_MESA, " " + MESAID +"  = ?",  new String[] { String.valueOf(id) },
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

        String query = "SELECT * FROM " + TABELA_MESAS + " WHERE  "+MESAID +
                " NOT IN (SELECT " + MESAID + " From " + TABELA_PEDIDOS + " GROUP BY "+MESAID+") ORDER BY " + NUMERO_MESA;

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

    public void adicionarProduto(Produto produto)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOME, produto.getNome());
        values.put(DESCRICAO, produto.getDescricao());
        values.put(PRECO, produto.getPreco());
        values.put(FOTO, produto.getFoto());
        db.insert(TABELA_PRODUTOS, null, values);
        db.close();
    }



    public Produto obterProduto(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABELA_PRODUTOS, COLUNAS_PRODUTO, " " + PRODUTOID + " = ?",  new String[] { String.valueOf(id) },
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

    public ArrayList<Produto> obterTodosProdutos()
    {
        ArrayList<Produto> produtos = new ArrayList<>();
        String query = "SELECT * FROM " + TABELA_PRODUTOS + " ORDER BY " + NOME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Produto produto = cursorToProduto(cursor);
                produtos.add(produto);
            } while (cursor.moveToNext());
        }
        return produtos;
    }

    public int atualizarProduto(Produto produto)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOME, produto.getNome());
        values.put(DESCRICAO, produto.getDescricao());
        values.put(FOTO, produto.getFoto());
        values.put(PRECO, new Double(produto.getPreco()));

        int linhasAfetadas = db.update(
                TABELA_PRODUTOS,
                values,
                PRODUTOID + " = ?",  new String[] { String.valueOf(produto.getProdutoId()) }
        );

        db.close();

        return linhasAfetadas;
    }

    public int deletarProduto(Produto produto)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int linhasAfetadas = db.delete(
                TABELA_PRODUTOS,
                PRODUTOID + " = ?", new String[] { String.valueOf(produto.getProdutoId()) }
        );
        db.close();
        return linhasAfetadas;
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

    public List<Pedido> obterTodosPedidosSemPagamentoEfetuado()
    {
        List<Pedido> pedidos = new ArrayList<>();
        /*
        pedidos.add(new Pedido(1,false, new Mesa(01)));
        pedidos.add(new Pedido(2,false, new Mesa(02)));
        pedidos.add(new Pedido(3,false, new Mesa(03)));
        pedidos.add(new Pedido(4,false, new Mesa(04)));
        pedidos.add(new Pedido(5,false, new Mesa(05)));*/

        String query = "SELECT Pedidos.numero, Pedidos.pago, Mesas.mesaId, Mesas.numero FROM Pedidos  "+
                       "INNER JOIN Mesas  ON Mesas.mesaId = Mesas.mesaId " +
                       "WHERE Pedidos.pago = 1 ORDER BY  Pedidos.numero";

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

    private void Seed()
    {
        List<Mesa> mesas = obterTodasMesas();

        if (mesas.size() == 0)
        {
            for (int i = 1; i <= 10; i++)
            {
                adicionarMesa(new Mesa(i));
            }
        }
    }
}
