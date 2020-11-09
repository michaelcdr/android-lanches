package br.ucs.androidlanches.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.iterfaces.IProdutosDAO;
import br.ucs.androidlanches.data.Helpers.CursorHelper;
import br.ucs.androidlanches.data.Helpers.SqliteHelper;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.models.Produto;

public class ProdutosDAO implements IProdutosDAO
{
    private SQLiteDatabase db;
    private Context context;

    public ProdutosDAO(Context context)
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

    public void adicionarBebida(Bebida produto)
    {
        ContentValues values = new ContentValues();

        values.put("nome", produto.getNome());
        values.put("descricao", produto.getDescricao());
        values.put("preco", produto.getPreco());
        values.put("foto", produto.getFoto());
        values.put("tipo", "bebida");
        values.put("embalagem", produto.getEmbalagem());

        conexao().insert("Produtos", null, values);
        db.close();
    }

    public void adicionarPrato(Prato produto)
    {
        ContentValues values = new ContentValues();

        values.put("nome", produto.getNome());
        values.put("descricao", produto.getDescricao());
        values.put("preco", produto.getPreco());
        values.put("foto", produto.getFoto());
        values.put("tipo", "prato");
        values.put("serveQuantasPessoas", produto.getServeQuantasPessoas());

        conexao().insert("Produtos", null, values);
        db.close();
    }

    public Produto obterProduto(int id)
    {
        String[] colunas = new String[] {"produtoId", "nome", "descricao", "preco", "foto", "embalagem", "serveQuantasPessoas", "tipo"};
        Cursor cursor = conexao().query(
            "Produtos", colunas, " produtoId = ?",  new String[] { String.valueOf(id) },
            null,
            null,
            null,
            null
        );

        if (cursor == null) {
            db.close();
            return null;
        }
        else
        {
            cursor.moveToFirst();
            Produto produto = CursorHelper.cursorToProduto(cursor);
            db.close();
            return produto;
        }
    }

    public ArrayList<Bebida> obterTodasBebidas()
    {
        ArrayList<Bebida> bebidas = new ArrayList<>();
        String query = "SELECT produtoId,nome,descricao,preco,foto,embalagem,serveQuantasPessoas,tipo FROM Produtos WHERE tipo = 'bebida' ORDER BY nome";
        Cursor cursor = conexao().rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                Bebida bebida = CursorHelper.cursorToBebida(cursor);
                bebidas.add(bebida);
            } while (cursor.moveToNext());
        }
        db.close();
        return bebidas;
    }

    public int atualizarProduto(Produto produto)
    {
        ContentValues values = new ContentValues();
        values.put("nome", produto.getNome());
        values.put("descricao", produto.getDescricao());
        values.put("foto", produto.getFoto());
        values.put("preco", new Double(produto.getPreco()));

        int linhasAfetadas = conexao().update("produtos", values,  "produtoId = ?",  new String[] { String.valueOf(produto.getProdutoId()) } );
        db.close();

        return linhasAfetadas;
    }

    public int deletarProduto(Produto produto)
    {
        int linhasAfetadas = conexao().delete("produtos", " produtoId = ?", new String[] { String.valueOf(produto.getProdutoId()) } );
        db.close();
        return linhasAfetadas;
    }

    public List<Prato> obterTodosPratos()
    {
        ArrayList<Prato> pratos = new ArrayList<>();
        String query = "SELECT produtoId, nome, descricao, preco, foto, embalagem, serveQuantasPessoas, tipo  FROM produtos WHERE tipo = 'prato' ORDER BY nome";
        Cursor cursor = conexao().rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                Prato prato = CursorHelper.cursorToPrato(cursor);
                pratos.add(prato);
            } while (cursor.moveToNext());
        }
        db.close();
        return pratos;
    }

    public void deletarTodos()
    {
        conexao().delete("Produtos", null, null);
        db.close();
    }
}