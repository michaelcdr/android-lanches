package br.ucs.androidlanches.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.data.Helpers.CursorHelper;
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
            Produto produto = CursorHelper.cursorToProduto(cursor);
            return produto;
        }
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
                Bebida bebida = CursorHelper.cursorToBebida(cursor);
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
                Prato prato = CursorHelper.cursorToPrato(cursor);
                pratos.add(prato);
            } while (cursor.moveToNext());
        }
        return pratos;
    }






}
