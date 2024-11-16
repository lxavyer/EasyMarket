package ufmg.coltec.easymarket;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ufmg.coltec.easymarket.model.Gasto;

public class AddGastoActivity extends AppCompatActivity {

    private EditText editTextNome, editTextValor, editTextItens;
    private Button btnSalvar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gasto);

        dbHelper = new DatabaseHelper(this);

        // Inicializar os campos de entrada
        editTextNome = findViewById(R.id.editTextNomeGasto);
        editTextValor = findViewById(R.id.editTextValor);
        editTextItens = findViewById(R.id.editTextItens);
        btnSalvar = findViewById(R.id.btnSalvarGasto);

        btnSalvar.setOnClickListener(v -> {
            String nome = editTextNome.getText().toString();
            String valorStr = editTextValor.getText().toString();
            String itens = editTextItens.getText().toString();

            // Verificar se os campos estão preenchidos
            if (nome.isEmpty() || valorStr.isEmpty() || itens.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double valor;
            try {
                valor = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            String data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            // Criar um novo objeto Gasto com o nome, valor, data e itens
            Gasto novoGasto = new Gasto(nome, valor, data, itens);
            Log.d("AddGastoActivity", "Tentando salvar gasto: " + nome + ", " + valor + ", " + data);

            if (dbHelper.insertGasto(novoGasto)) {
                Toast.makeText(this, "Gasto adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Log.e("AddGastoActivity", "Erro ao adicionar gasto");
                Toast.makeText(this, "Erro ao adicionar gasto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
