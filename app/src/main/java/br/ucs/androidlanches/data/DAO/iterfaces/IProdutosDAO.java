package br.ucs.androidlanches.data.DAO.iterfaces;

import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.models.Produto;

public interface IProdutosDAO
{
    void adicionarBebida(Bebida produto);
    void adicionarPrato(Prato produto);
    Produto obterProduto(int id);
    ArrayList<Bebida> obterTodasBebidas();
    int atualizarProduto(Produto produto);
    int deletarProduto(Produto produto);
    List<Prato> obterTodosPratos();

    void deletarTodos();
}
