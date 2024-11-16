package ufmg.coltec.easymarket;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import ufmg.coltec.easymarket.model.Produto;

public class UpdateProdutoActivity extends AppCompatActivity {

    private EditText editTextNome, editTextQuantidade;
    private Button btnAtualizar;
    private DatabaseHelper dbHelper;
    private int produtoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_produto);

        dbHelper = new DatabaseHelper(this);

        editTextNome = findViewById(R.id.editTextNome);
        editTextQuantidade = findViewById(R.id.editTextQuantidade);
        btnAtualizar = findViewById(R.id.btnAtualizarProduto);

        // Obter o ID do produto passado pela intent
        produtoId = getIntent().getIntExtra("produto_id", -1);
        Produto produto = dbHelper.getProdutoById(produtoId);
        if (produto != null) {
            editTextNome.setText(produto.getNome());
            editTextQuantidade.setText(String.valueOf(produto.getQuantidade()));
        }

        btnAtualizar.setOnClickListener(v -> {
            String nome = editTextNome.getText().toString();
            String quantidadeStr = editTextQuantidade.getText().toString();

            if (nome.isEmpty() || quantidadeStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantidade = Integer.parseInt(quantidadeStr);
            produto.setNome(nome);
            produto.setQuantidade(quantidade);

            if (dbHelper.updateProduto(produto)) {
                Toast.makeText(this, "Produto atualizado!", Toast.LENGTH_SHORT).show();
                // Retornar o resultado indicando que o produto foi atualizado
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}