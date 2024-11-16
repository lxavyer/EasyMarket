package ufmg.coltec.easymarket;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ufmg.coltec.easymarket.model.Gasto;
import ufmg.coltec.easymarket.model.Produto;
import ufmg.coltec.easymarket.model.ProdutoAdapter;

public class ListaComprasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoAdapter produtosAdapter;
    private List<Produto> produtos;
    private DatabaseHelper dbHelper;

    private final ActivityResultLauncher<Intent> addProdutoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    produtos.clear();
                    produtos.addAll(dbHelper.getAllProdutos());
                    produtosAdapter.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewProdutos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        produtos = dbHelper.getAllProdutos();
        if (produtos == null) {
            produtos = new ArrayList<>();
        }
        produtosAdapter = new ProdutoAdapter(produtos, dbHelper);
        recyclerView.setAdapter(produtosAdapter);

        // Botão para salvar lista
        Button btnSalvarLista = findViewById(R.id.btnSalvarLista);
        btnSalvarLista.setOnClickListener(view -> salvarListaDeCompras());

        // FloatingActionButton para adicionar novos produtos
        FloatingActionButton fabAddProduto = findViewById(R.id.fabAddProduto);
        fabAddProduto.setOnClickListener(view -> {
            Intent intent = new Intent(ListaComprasActivity.this, AddProdutoActivity.class);
            addProdutoLauncher.launch(intent);
        });
    }

    private void salvarListaDeCompras() {
        // Converter a lista de produtos em uma string
        StringBuilder itensComprados = new StringBuilder();
        for (Produto produto : produtos) {
            itensComprados.append(produto.getNome()).append(" (").append(produto.getQuantidade()).append("x), ");
        }

        // Remover a última vírgula e espaço
        if (itensComprados.length() > 0) {
            itensComprados.setLength(itensComprados.length() - 2);
        }

        // Exibir um diálogo para o usuário inserir o nome e valor do gasto
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salvar Lista de Compras");

        // Layout para o diálogo com dois campos de texto
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_salvar_gasto, null);
        EditText inputNome = dialogView.findViewById(R.id.inputNomeGasto);
        EditText inputValor = dialogView.findViewById(R.id.inputValorGasto);
        builder.setView(dialogView);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String nomeGasto = inputNome.getText().toString();
            String valorStr = inputValor.getText().toString();

            if (!nomeGasto.isEmpty() && !valorStr.isEmpty()) {
                double valor = Double.parseDouble(valorStr);
                String dataAtual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                // Criar um novo objeto Gasto com nome, valor, data e itens
                Gasto novoGasto = new Gasto(nomeGasto, valor, dataAtual, itensComprados.toString());

                // Inserir o novo gasto no banco de dados
                if (dbHelper.insertGasto(novoGasto)) {
                    Toast.makeText(this, "Lista salva com sucesso!", Toast.LENGTH_SHORT).show();

                    // Zerar a lista de compras no banco de dados
                    dbHelper.deleteAllProdutos();
                    produtos.clear();
                    produtosAdapter.notifyDataSetChanged();

                    Intent intent = new Intent(ListaComprasActivity.this, HistoricoGastosActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Erro ao salvar lista", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public final ActivityResultLauncher<Intent> updateProdutoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Atualizar a lista de produtos após editar um produto
                    produtos.clear();
                    produtos.addAll(dbHelper.getAllProdutos());
                    produtosAdapter.notifyDataSetChanged();
                }
            });

}
