package br.ucs.androidlanches.data.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SqliteHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 27;
    private static final String DATABASE_NAME = "AndroidLanchesDB";
    private static final String TAG = "LOG_ANDROID_LANCHES ";

    public SqliteHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sqlCreateTableMesa    = "CREATE TABLE Mesas (mesaId INTEGER PRIMARY KEY AUTOINCREMENT, numero INTEGER) ";

        String sqlCreateTableProduto = "CREATE TABLE Produtos (" +
                " produtoid INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nome TEXT NOT NULL, " +
                " descricao TEXT NOT NULL,"+
                " foto TEXT,"+
                " preco DOUBLE, " +
                " tipo TEXT, " +
                " serveQuantasPessoas INTEGER," +
                " embalagem TEXT)";

        String sqlCreatePedidos = "CREATE TABLE Pedidos ("+
            " pedidoId INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " numero INTEGER NOT NULL , "+
            " pago INTEGER, " +
            " gorjeta INTEGER, " +
            " pedidoIdApi INTEGER, " +
            " mesaId INTEGER )";

        String sqlCreatePedidosItens = "CREATE TABLE PedidosItens ("+
            " pedidoItemId INTEGER PRIMARY KEY AUTOINCREMENT," +
            " pedidoId INTEGER NOT NULL, " +
            " quantidade INTEGER NOT NULL, " +
            " produtoid INTEGER NOT NULL, " +
            "FOREIGN KEY (pedidoId) REFERENCES Pedidos(pedidoId), " +
            "FOREIGN KEY (produtoId) REFERENCES Produtos(produtoId)" +
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
        db.execSQL("DROP TABLE IF EXISTS Produtos");
        db.execSQL("DROP TABLE IF EXISTS Mesas");
        db.execSQL("DROP TABLE IF EXISTS PedidosItens");
        db.execSQL("DROP TABLE IF EXISTS Pedidos");
        this.onCreate(db);
    }
}