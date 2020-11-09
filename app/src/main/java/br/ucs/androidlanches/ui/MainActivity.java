package br.ucs.androidlanches.ui;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.models.*;
import br.ucs.androidlanches.recycleview.adapter.listeners.*;
import br.ucs.androidlanches.recycleview.adapter.PedidosAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.rest.services.IMesaApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
{
    private List<Pedido> pedidos = new ArrayList<>();
    private PedidosDAO _pedidosDao;
    private MesasDAO _mesasDao;
    private ProdutosDAO _produtosDao;
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
    }

    private void configurarRecicleViewPedidos()
    {
        recyclerViewPedidos = findViewById(R.id.recycleViewPedidos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPedidos.setLayoutManager(layoutManager);
    }

    private void configurarAdapter(List<Pedido> pedidos, RecyclerView recyclerView)
    {
        PedidosAdapter adapter = new PedidosAdapter(this, pedidos);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickVerPedidoListener(new IOnItemClickBtnVerPedidoListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                Intent irParaDetalhesPedido = new Intent(MainActivity.this, DetalhesDoPedidoActivity.class);
                irParaDetalhesPedido.putExtra("numeroPedido",pedido.getNumero());
                startActivityForResult(irParaDetalhesPedido, 1);
            }
        });

        adapter.setOnItemClickBtnPagarListener(new IOnItemClickBtnPagarPedidoListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                Intent irParaResumoPedido = new Intent(MainActivity.this, ResumoPedidoActivity.class);
                irParaResumoPedido.putExtra("numeroPedido",pedido.getNumero());
                startActivityForResult(irParaResumoPedido, 1);
            }
        });

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        obterTodosPedidosSemPagamentoEfetuado();
    }

    private void gerarEventoCadastroPedido()
    {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EscolherMesaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        boolean retorno = false;
        switch (item.getItemId())
        {
            case R.id.action_atualizar_base:
            {
                atualizarBaseLocal();
                break;
            }

            case R.id.action_limpar_base:
            {
                limparBaseLocal();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void limparBaseLocal()
    {
        Log.i("LOG_ANDROID_LANCHES","Limpando base local.");
        try {
            _pedidosDao.deletarTodos();
            _produtosDao.deletarTodos();
            _mesasDao.deletarTodas();

            Log.i("LOG_ANDROID_LANCHES","Dados locais removidos com sucesso.");
            Toast.makeText(this,"Dados locais removidos com sucesso.", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro limpando a base local: " + e.getMessage());
            Toast.makeText(this,"Não foi possível limpar a base local.", Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarBaseLocal() {
        Log.i("LOG_ANDROID_LANCHES","Iniciando atualização base local.");

        gerarCallMesas();
        gerarCallBebidas();
        gerarCallPratos();
    }

    private void gerarCallMesas() {
        Log.i("LOG_ANDROID_LANCHES","Entrou na call de mesa");
        Call<List<Mesa>> mesasCall = RetrofitApiClient.getMesaService().obterDesocupadas();
        mesasCall.enqueue(new Callback<List<Mesa>>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful()){
                    atualizarMesasBancoLocal(response);
                    Toast.makeText(MainActivity.this,"Dados atualizados com sucesso.", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro durante a atualização dos dados" + response.message());
                    Toast.makeText(MainActivity.this,"Ocorreu um erro durante a atualização dos dados.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","Erro ocorrido " + t.getMessage());
                Toast.makeText(MainActivity.this,"Não foi possivel atualizar a base local.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gerarCallBebidas() {
        Log.i("LOG_ANDROID_LANCHES","Entrou na call de bebidas");
        Call<List<Bebida>> bebidasCall = RetrofitApiClient.getProdutoService().obterBebidas();
        bebidasCall.enqueue(new Callback<List<Bebida>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Bebida>> call, Response<List<Bebida>> response) {
                if (response.isSuccessful()){
                    atualizarBebidasBancoLocal(response);
                    Toast.makeText(MainActivity.this,"Dados atualizados com sucesso.", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro durante a atualização dos dados" + response.message());
                    Toast.makeText(MainActivity.this,"Ocorreu um erro durante a atualização dos dados.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bebida>> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","Erro ocorrido " + t.getMessage());
                Toast.makeText(MainActivity.this,"Não foi possivel atualizar a base local.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gerarCallPratos() {
        Log.i("LOG_ANDROID_LANCHES","Entrou na call de pratos");
        Call<List<Prato>> pratosCall = RetrofitApiClient.getProdutoService().obterPratos();
        pratosCall.enqueue(new Callback<List<Prato>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Prato>> call, Response<List<Prato>> response) {
                if (response.isSuccessful()){
                    atualizarPratosBancoLocal(response);
                    Toast.makeText(MainActivity.this,"Dados atualizados com sucesso.", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro durante a atualização dos dados" + response.message());
                    Toast.makeText(MainActivity.this,"Ocorreu um erro durante a atualização dos dados.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Prato>> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","Erro ocorrido " + t.getMessage());
                Toast.makeText(MainActivity.this,"Não foi possivel atualizar a base local.", Toast.LENGTH_LONG).show();
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
    private void atualizarBebidasBancoLocal(Response<List<Bebida>> response){
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