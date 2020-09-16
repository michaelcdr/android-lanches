package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import models.Mesa;
import models.Produto;

public class BDSQLiteHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
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

    // MESA ...
    private static final String TABELA_MESAS = "Mesas";
    private static final String MESAID = "produtoid";
    private static final String NUMERO_MESA = "produtoid";
    private static final String[] COLUNAS_MESA = {MESAID, NUMERO_MESA};
    public BDSQLiteHelper(Context context)
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PRODUTOS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABELA_MESAS);

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

    public ArrayList<Mesa> obterTodasMesas()
    {
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        String query = "SELECT * FROM " + TABELA_MESAS + " ORDER BY " + NUMERO_MESA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
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
                TABELA_PRODUTOS, COLUNAS_PRODUTO, " " + PRODUTOID +"  = ?",  new String[] { String.valueOf(id) },
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

    public ArrayList<Produto> obterTodosProdutos()
    {
        ArrayList<Produto> produtos = new ArrayList<Produto>();
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
}
