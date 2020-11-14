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
<<<<<<< HEAD:app/src/main/java/br/ucs/androidlanches/services/GerenciadorDadosLocais.java
    private Context _context;
=======
    private RecyclerView recyclerViewPedidos;
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pedidos");

        _pedidosDao =  new PedidosDAO(this);
        _mesasDao = new MesasDAO(this);
        _produtosDao = new ProdutosDAO(this);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_lista_pedidos);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                obterTodosPedidosSemPagamentoEfetuado();
            }
        });

        obterTodosPedidosSemPagamentoEfetuado();
        configurarRecicleViewPedidos();
        gerarEventoCadastroPedido();
    }

    private void obterTodosPedidosSemPagamentoEfetuado()
    {
<<<<<<< HEAD
        List<Mesa> mesas = db.obterTodasMesas();

        if (mesas.size() == 0)
        {
            for (int i = 1; i <= 100; i++)
            {
                db.adicionarMesa(new Mesa(i));
            }

            //adicionando bebidas
            db.adicionarBebida(new Bebida("Coca cola", "Zero", 8.0, "2 litros", "cocacola_zero_2l"));
            db.adicionarBebida(new Bebida("Coca cola", "Zero", 5.0, "600 ml", "cocacola_zero_600ml"));
            db.adicionarBebida(new Bebida("Coca cola", "Zero Lata", 3.5, "350 ml", "cocacola_zero_350ml"));

            db.adicionarBebida(new Bebida("Coca cola", "Normal", 8.0, "2 litros", "cocacola_2l"));
            db.adicionarBebida(new Bebida("Coca cola", "Normal", 5.0, "600 ml", "cocacola_600ml"));
            db.adicionarBebida(new Bebida("Coca cola", "Normal Lata", 3.5, "350 ml", "cocacola_350ml"));

            //db.adicionarBebida(new Bebida("Pepsi", "Coca cola é melhor mas quebra um galho.",5.0,"600 ml"));
            //db.adicionarBebida(new Bebida("Cachaça 51", "cachaça da boa!",3.50,"1 lt"));

            //adicionando pratos
            db.adicionarPrato(new Prato("Xis salada", "Hamburguer, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 20.0, 1, "xis_salada"));
            db.adicionarPrato(new Prato("Xis Calabresa", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 20.0, 1, "xis_calabresa"));
            db.adicionarPrato(new Prato("Pizza Calabresa", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza Palmito", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza Bacon", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza 4 Queijos", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza 5 Queijos", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza 8 Queijos", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
        }
=======
        Call<List<Pedido>> callPedidos = RetrofitApiClient.getPedidoService().obterTodosSemPagamentoEfetuado();
        callPedidos.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful()){
                    Log.i("LOG_ANDROID_LANCHES", "Requisição com sucesso em obter pedidos não pagos: ");
                    pedidos = response.body();
                    configurarAdapter(pedidos, recyclerViewPedidos);
                    swipe.setRefreshing(false);
                } else {
                    Log.e("LOG_ANDROID_LANCHES", "Algo deu errado ao tentar obter os pedidos em abertos na API: " + response.message());
                    Toast.makeText(MainActivity.this,"Algo deu errado ao tentar obter os pedidos em abertos no servidor.",Toast.LENGTH_LONG).show();
                    configurarAdapter(pedidos, recyclerViewPedidos);
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","Falhou na requisição de pedidos, erro ocorrido: " + t.getMessage());
                pedidos = _pedidosDao.obterTodosPedidosSemPagamentoEfetuado();
                Log.i("LOG_ANDROID_LANCHES","Pedidos obtidos no banco local, total de " + pedidos.size() + " pedidos.");
                configurarAdapter(pedidos, recyclerViewPedidos);
                swipe.setRefreshing(false);
            }
        });
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
    }
>>>>>>> 3ab8db2d48275d29391054538063e2a9c19555f8:app/src/main/java/br/ucs/androidlanches/ui/MainActivity.java

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
