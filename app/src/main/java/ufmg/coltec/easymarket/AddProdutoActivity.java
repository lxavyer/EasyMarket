package ufmg.coltec.easymarket;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ufmg.coltec.easymarket.model.Produto;

public class AddProdutoActivity extends AppCompatActivity {

    private EditText editTextNomeProduto, editTextQuantidadeProduto;
    private Button btnSalvarProduto;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        dbHelper = new DatabaseHelper(this);

        editTextNomeProduto = findViewById(R.id.editTextNome);
        editTextQuantidadeProduto = findViewById(R.id.editTextQuantidade);
        btnSalvarProduto = findViewById(R.id.btnSalvarProduto);

        btnSalvarProduto.setOnClickListener(v -> {
            String nomeProduto = editTextNomeProduto.getText().toString();
            String quantidadeStr = editTextQuantidadeProduto.getText().toString();

            if (nomeProduto.isEmpty() || quantidadeStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantidade = Integer.parseInt(quantidadeStr);
            Produto novoProduto = new Produto(nomeProduto, quantidade, false);

            if (dbHelper.insertProduto(novoProduto)) {
                Toast.makeText(this, "Produto adicionado!", Toast.LENGTH_SHORT).show();

                // Retornar resultado para ListaComprasActivity
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Erro ao adicionar produto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
