package br.ucs.androidlanches.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.iterfaces.IMesaDAO;
import br.ucs.androidlanches.data.Helpers.CursorHelper;
import br.ucs.androidlanches.data.Helpers.SqliteHelper;
import br.ucs.androidlanches.models.Mesa;

public class MesasDAO implements IMesaDAO
{
    private SQLiteDatabase db;
    private Context context;

    public MesasDAO(Context context)
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

    public void adicionarMesa(Mesa mesa)
    {
        ContentValues values = new ContentValues();

        values.put("numero", mesa.getNumero());
        conexao().insert("Mesas", null, values);
        db.close();
    }

    public Mesa obterMesa(int id)
    {
        Cursor cursor = conexao().query(
                "Mesas", new String[]{"mesaId","numero"}, " mesaId  = ?",  new String[] { String.valueOf(id) },
                null, // e. group by
                null, // f. having
                null, // g. order by
                null // h. limit
        );

        if (cursor == null) {
            db.close();
            return null;
        } else {
            cursor.moveToFirst();
            Mesa mesa = CursorHelper.cursorToMesa(cursor);
            db.close();
            return mesa;
        }
    }

    public ArrayList<Mesa> obterTodasMesasDesocupadas()
    {
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        String query = " SELECT * FROM Mesas WHERE mesaId "+
                       " NOT IN (SELECT mesaId From Pedidos WHERE PAGO = 0 GROUP BY mesaId) ORDER BY numero";

        Cursor cursor = conexao().rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            do {
                Mesa mesa = CursorHelper.cursorToMesa(cursor);
                mesas.add(mesa);
            } while (cursor.moveToNext());
        }
        db.close();
        return mesas;
    }

    public List<Mesa> obterTodasMesas()
    {
        List<Mesa> mesas = new ArrayList<>();
        Cursor cursor = conexao().rawQuery("SELECT * FROM Mesas", null);
        if (cursor.moveToFirst())
        {
            do {
                Mesa mesa = CursorHelper.cursorToMesa(cursor);
                mesas.add(mesa);
            } while (cursor.moveToNext());
        }
        db.close();
        return mesas;
    }

    public void deletarTodas()
    {
        conexao().delete("Mesas", null, null);
        db.close();
    }
}
