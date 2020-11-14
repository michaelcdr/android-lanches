package br.ucs.androidlanches.services;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import br.ucs.androidlanches.data.DAO.MesasDAO;
import br.ucs.androidlanches.data.DAO.PedidosDAO;
import br.ucs.androidlanches.data.DAO.ProdutosDAO;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GerenciadorDadosLocais
{
    private PedidosDAO _pedidosDao;
    private MesasDAO _mesasDao;
    private ProdutosDAO _produtosDao;
    private Context _context;

    public GerenciadorDadosLocais(Context context)
    {
        _context = context;
        _pedidosDao =  new PedidosDAO(context);
        _mesasDao = new MesasDAO(context);
        _produtosDao = new ProdutosDAO(context);
    }

    public void limpar()
    {
        Log.i("LOG_ANDROID_LANCHES","Limpando base local.");
        try {
            _pedidosDao.deletarTodos();
            _produtosDao.deletarTodos();
            _mesasDao.deletarTodas();

            Log.i("LOG_ANDROID_LANCHES","Dados locais removidos com sucesso.");
            Toast.makeText(_context,"Dados locais removidos com sucesso.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro limpando a base local: " + e.getMessage());
            Toast.makeText(_context,"Não foi possível limpar a base local.", Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar()
    {
        Log.i("LOG_ANDROID_LANCHES","Iniciando atualização base local.");

        gerarCallMesas();
        gerarCallBebidas();
        gerarCallPratos();
    }

    private void gerarCallMesas()
    {
        Log.i("LOG_ANDROID_LANCHES","Entrou na call de mesa");
        Call<List<Mesa>> mesasCall = RetrofitApiClient.getMesaService().obterDesocupadas();
        mesasCall.enqueue(new Callback<List<Mesa>>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful()){
                    atualizarMesasBancoLocal(response);
                    Toast.makeText(_context,"Dados atualizados com sucesso.", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro durante a atualização dos dados" + response.message());
                    Toast.makeText(_context,"Ocorreu um erro durante a atualização dos dados.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","Erro ocorrido " + t.getMessage());
                Toast.makeText(_context,"Não foi possivel atualizar a base local.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gerarCallBebidas()
    {
        Log.i("LOG_ANDROID_LANCHES","Entrou na call de bebidas");
        Map<String,String> filtros = new HashMap<>();
        Call<List<Bebida>> bebidasCall = RetrofitApiClient.getProdutoService().obterBebidas(filtros);
        bebidasCall.enqueue(new Callback<List<Bebida>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Bebida>> call, Response<List<Bebida>> response) {
                if (response.isSuccessful()){
                    atualizarBebidasBancoLocal(response);
                    Toast.makeText(_context,"Dados atualizados com sucesso.", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro durante a atualização dos dados" + response.message());
                    Toast.makeText(_context,"Ocorreu um erro durante a atualização dos dados.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bebida>> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","Erro ocorrido " + t.getMessage());
                Toast.makeText(_context,"Não foi possivel atualizar a base local.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gerarCallPratos()
    {
        Log.i("LOG_ANDROID_LANCHES","Entrou na call de pratos");
        Map<String,String> filtros = new HashMap<>();
        Call<List<Prato>> pratosCall = RetrofitApiClient.getProdutoService().obterPratos(filtros);
        pratosCall.enqueue(new Callback<List<Prato>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Prato>> call, Response<List<Prato>> response) {
                if (response.isSuccessful()){
                    atualizarPratosBancoLocal(response);
                    Toast.makeText(_context,"Dados atualizados com sucesso.", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro durante a atualização dos dados" + response.message());
                    Toast.makeText(_context,"Ocorreu um erro durante a atualização dos dados.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Prato>> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","Erro ocorrido " + t.getMessage());
                Toast.makeText(_context,"Não foi possivel atualizar a base local.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Bebida> obterDistincaoDaListaDeBebidas(List<Bebida> listaComparacao, List<Bebida> listaAlvo)
    {
        List<Bebida> bebidas = new ArrayList<>();
        // indentificando quais bebidas nao estao no banco local
        if (listaComparacao.size() > 0){
            listaAlvo.forEach(bebida -> {
                Log.i("LOG_ANDROID_LANCHES","Bebida: " +bebida.getNome());
                if (listaComparacao.stream()
                        .filter(bebidaLocal -> bebida.getNome() == bebidaLocal.getNome() &&
                                bebida.getEmbalagem() == bebidaLocal.getEmbalagem() &&
                                bebida.getDescricao() == bebidaLocal.getDescricao())
                        .findAny().orElse(null) == null)
                {
                    Log.i("LOG_ANDROID_LANCHES","Não encontrou a bebida: " + bebida.getNome());
                    bebidas.add(bebida);
                }
            });
        } else
            bebidas.addAll(listaAlvo);

        return bebidas;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void atualizarPratosBancoLocal(Response<List<Prato>> response){
        List<Prato> pratosLocais = _produtosDao.obterTodosPratos();
        List<Prato> pratosNaApi = response.body();

        // identificando quais bebidas nao estao na api...
        List<Prato> pratosNovos = obterDistincaoDaListaDePratos(pratosLocais, pratosNaApi);
        List<Prato> pratosExcluidos = obterDistincaoDaListaDePratos(pratosNaApi, pratosLocais);

        if (pratosNovos.size() > 0){
            pratosNovos.forEach(bebida -> {
                _produtosDao.adicionarPrato(
                        new Prato(bebida.getNome(), bebida.getDescricao(), bebida.getPreco(), bebida.getServeQuantasPessoas(), bebida.getFoto())
                );
            });
        }

        // removendo bebidas que nao estao na api
        if (pratosExcluidos.size() > 0){
            pratosExcluidos.forEach(prato -> {
                _produtosDao.deletarProduto(prato);
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Prato> obterDistincaoDaListaDePratos(List<Prato> listaComparacao, List<Prato> listaAlvo) {
        List<Prato> pratos = new ArrayList<>();
        // indentificando quais bebidas nao estao no banco local
        if (listaComparacao.size() > 0){
            listaAlvo.forEach(prato -> {
                Log.i("LOG_ANDROID_LANCHES","Bebida: " +prato.getNome());
                if (listaComparacao.stream()
                        .filter(pratoLocal -> prato.getNome() == pratoLocal.getNome() &&
                                prato.getServeQuantasPessoas() == pratoLocal.getServeQuantasPessoas() &&
                                prato.getDescricao() == pratoLocal.getDescricao())
                        .findAny().orElse(null) == null)
                {
                    Log.i("LOG_ANDROID_LANCHES","Não encontrou o prato: " + prato.getNome());
                    pratos.add(prato);
                }
            });
        } else
            pratos.addAll(listaAlvo);

        return pratos;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void atualizarBebidasBancoLocal(Response<List<Bebida>> response)
    {
        List<Bebida> bebidasLocais = _produtosDao.obterTodasBebidas();
        List<Bebida> bebidasNaApi = response.body();

        // identificando quais bebidas nao estao na api...
        List<Bebida> bebidasNovas = obterDistincaoDaListaDeBebidas(bebidasLocais, bebidasNaApi);
        List<Bebida> bebidasExcluidas = obterDistincaoDaListaDeBebidas(bebidasNaApi, bebidasLocais);

        // adicionando bebidas novas...
        if (bebidasNovas.size() > 0){
            bebidasNovas.forEach(bebida -> {
                _produtosDao.adicionarBebida(
                        new Bebida(bebida.getNome(), bebida.getDescricao(), bebida.getPreco(), bebida.getEmbalagem(), bebida.getFoto())
                );
            });
        }

        // removendo bebidas que nao estao na api
        if (bebidasExcluidas.size() > 0){
            bebidasExcluidas.forEach(bebida -> {
                _produtosDao.deletarProduto(bebida);
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void atualizarMesasBancoLocal(Response<List<Mesa>> response) {
        List<Mesa> mesasLocais = _mesasDao.obterTodasMesas();
        List<Mesa> mesasNaApi = response.body();

        //identificando mesas que nao estao na base local...
        List<Mesa> mesasNovas = new ArrayList<>() ;
        if (mesasLocais.size() > 0){
            mesasNaApi.forEach(mesa -> {
                Log.i("LOG_ANDROID_LANCHES","Mesa: " +mesa.getNumero());
                if (mesasLocais.stream()
                        .filter(mesaLocal -> mesa.getNumero() == mesaLocal.getNumero())
                        .findAny().orElse(null) == null)
                {
                    Log.i("LOG_ANDROID_LANCHES","Não encontrou a mesa: " + mesa.getNumero());
                    mesasNovas.add(mesa);
                }
            });
        } else {
            mesasNovas.addAll(mesasNaApi);
        }

        //adicionando mesas no banco local
        if (mesasNovas.size() > 0){
            mesasNovas.forEach(mesa -> {
                _mesasDao.adicionarMesa(new Mesa(mesa.getNumero()));
            });
        }
    }
}
