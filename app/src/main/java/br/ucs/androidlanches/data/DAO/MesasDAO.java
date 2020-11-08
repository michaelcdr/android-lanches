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

    public MesasDAO(Context context)
    {
        SqliteHelper helper = new SqliteHelper(context);
        db = helper.getWritableDatabase();
    }

    public void adicionarMesa(Mesa mesa)
    {
        ContentValues values = new ContentValues();

        values.put("numero", mesa.getNumero());
        db.insert("Mesas", null, values);
        db.close();
    }

    public Mesa obterMesa(int id)
    {
        Cursor cursor = db.query(
                "Mesas", new String[]{"mesaId","numero"}, " mesaId  = ?",  new String[] { String.valueOf(id) },
                null, // e. group by
                null, // f. having
                null, // g. order by
                null // h. limit
        );

        if (cursor == null)
            return null;
        else
        {
            cursor.moveToFirst();
            Mesa mesa = CursorHelper.cursorToMesa(cursor);
            return mesa;
        }
    }

    public ArrayList<Mesa> obterTodasMesasDesocupadas()
    {
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        String query = " SELECT * FROM Mesas WHERE mesaId "+
                       " NOT IN (SELECT mesaId From Pedidos WHERE PAGO = 0 GROUP BY mesaId) ORDER BY numero";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            do {
                Mesa mesa = CursorHelper.cursorToMesa(cursor);
                mesas.add(mesa);
            } while (cursor.moveToNext());
        }
        return mesas;
    }

    public List<Mesa> obterTodasMesas()
    {
        List<Mesa> mesas = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Mesas", null);
        if (cursor.moveToFirst())
        {
            do {
                Mesa mesa = CursorHelper.cursorToMesa(cursor);
                mesas.add(mesa);
            } while (cursor.moveToNext());
        }
        return mesas;
    }

    @Override
    public void deletarTodas() {
        db.delete("Mesas", null, null);
        db.close();
    }
}
