package br.ucs.androidlanches.data.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SqliteHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 21;
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

    // PEDIDO ...
    private static final String PEDIDO_TABELA = "Pedidos";
    private static final String PEDIDO_NUMERO = "numero";
    private static final String PEDIDO_PAGO = "pago";
    private static final String PEDIDO_MESAID = "mesaId";
    private static final String PEDIDO_GORJETA = "gorjeta";
    private static final String PEDIDO_MESA_NUMERO = "numero";

    // MESA ...
    private static final String MESA_TABELA = "Mesas";
    private static final String MESA_MESAID = "mesaId";
    private static final String MESA_NUMERO = "numero";

    //TABELA PEDIDO ITENS...
    private static final String PEDIDO_ITEM_TABELA = "PedidosItens";
    private static final String PEDIDO_ITEM_PEDIDO_ITEM_ID = "pedidoItemId";
    private static final String PEDIDO_ITEM_NUMERO_PEDIDO = "numero";
    private static final String PEDIDO_ITEM_QUANTIDADE = "quantidade";
    private static final String PEDIDO_ITEM_PRODUTO_ID = "produtoId";
    private static final String TAG = "BANCO ";

    public SqliteHelper(@Nullable Context context)
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
            "hashIntegracao TEXT, " +
            PEDIDO_MESAID + " INTEGER )";

        String sqlCreatePedidosItens =  "CREATE TABLE " + PEDIDO_ITEM_TABELA + " ("+
            PEDIDO_ITEM_PEDIDO_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PEDIDO_ITEM_NUMERO_PEDIDO  + " INTEGER NOT NULL, " +
            PEDIDO_ITEM_QUANTIDADE + " INTEGER NOT NULL, " +
            PEDIDO_ITEM_PRODUTO_ID + " INTEGER NOT NULL, " +
            "hashIntegracao TEXT, " +
            "FOREIGN KEY ("+ PEDIDO_ITEM_NUMERO_PEDIDO +") REFERENCES "+PEDIDO_TABELA+"("+PEDIDO_NUMERO+"), " +
            "FOREIGN KEY ("+ PEDIDO_ITEM_PRODUTO_ID +") REFERENCES "+PRODUTO_TABELA+"("+PRODUTO_ID+")" +
        ")";

        try
        {
            db.execSQL(sqlCreateTableMesa);
            db.execSQL(sqlCreateTableProduto);
            db.execSQL(sqlCreatePedidos);
            db.execSQL(sqlCreatePedidosItens);
        }
        catch (Exception e)
        {
            Log.i(TAG,"Erro ao criar o banco de dados SQLITE. " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("LOG_ANDROID_LANCHES_I","Entrou no sqlite helper no metodo onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS "+ PRODUTO_TABELA);
        db.execSQL("DROP TABLE IF EXISTS "+ MESA_TABELA);
        db.execSQL("DROP TABLE IF EXISTS "+ PEDIDO_ITEM_TABELA);
        db.execSQL("DROP TABLE IF EXISTS "+ PEDIDO_TABELA);
        this.onCreate(db);
    }
}