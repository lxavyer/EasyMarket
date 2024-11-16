package ufmg.coltec.easymarket;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ufmg.coltec.easymarket.model.Gasto;

public class UpdateGastoActivity extends AppCompatActivity {

    private EditText editTextNome, editTextValor, editTextData, editTextItens;
    private Button btnAtualizar;
    private DatabaseHelper dbHelper;
    private int gastoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gasto);

        dbHelper = new DatabaseHelper(this);

        editTextNome = findViewById(R.id.editTextNome);
        editTextValor = findViewById(R.id.editTextValor);
        editTextData = findViewById(R.id.editTextData);
        editTextItens = findViewById(R.id.editTextItens);
        btnAtualizar = findViewById(R.id.btnAtualizarGasto);

        gastoId = getIntent().getIntExtra("gasto_id", -1);
        Gasto gasto = dbHelper.getGastoById(gastoId);

        if (gasto != null) {
            editTextNome.setText(gasto.getNome());
            editTextValor.setText(String.valueOf(gasto.getValor()));
            editTextData.setText(gasto.getData());
            editTextItens.setText(gasto.getItens());
        }

        btnAtualizar.setOnClickListener(v -> {
            String nome = editTextNome.getText().toString();
            String valorStr = editTextValor.getText().toString();
            String data = editTextData.getText().toString();
            String itens = editTextItens.getText().toString();

            if (nome.isEmpty() || valorStr.isEmpty() || data.isEmpty() || itens.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double valor = Double.parseDouble(valorStr);
            gasto.setNome(nome);
            gasto.setValor(valor);
            gasto.setData(data);
            gasto.setItens(itens);

            if (dbHelper.updateGasto(gasto)) {
                Toast.makeText(this, "Gasto atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar gasto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
