package br.ucs.androidlanches.data.DAO.iterfaces;

import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.models.Mesa;

public interface IMesaDAO
{
    Mesa obterMesa(int id);
    void adicionarMesa(Mesa mesa);
    ArrayList<Mesa> obterTodasMesasDesocupadas();
    List<Mesa> obterTodasMesas();

    void deletarTodas();
}
