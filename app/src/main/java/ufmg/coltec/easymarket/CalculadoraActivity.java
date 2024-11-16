package ufmg.coltec.easymarket;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CalculadoraActivity extends AppCompatActivity {

    private EditText editTextPeso1, editTextPreco1, editTextPeso2, editTextPreco2;
    private Button btnCalcular;
    private TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        // Inicializando elementos da interface
        editTextPeso1 = findViewById(R.id.editTextPeso1);
        editTextPreco1 = findViewById(R.id.editTextPreco1);
        editTextPeso2 = findViewById(R.id.editTextPeso2);
        editTextPreco2 = findViewById(R.id.editTextPreco2);
        btnCalcular = findViewById(R.id.btnCalcular);
        resultado = findViewById(R.id.resultado);

        // Configurar ação para o botão Calcular
        btnCalcular.setOnClickListener(v -> calcular());
    }

    private void calcular() {
        try {
            // Obter os valores inseridos pelo usuário
            double peso1 = Double.parseDouble(editTextPeso1.getText().toString());
            double preco1 = Double.parseDouble(editTextPreco1.getText().toString());
            double peso2 = Double.parseDouble(editTextPeso2.getText().toString());
            double preco2 = Double.parseDouble(editTextPreco2.getText().toString());

            // Calcular o custo-benefício
            double resultado1 = peso1 / preco1;
            double resultado2 = peso2 / preco2;

            String mensagem;
            if (resultado1 > resultado2) {
                mensagem = "Produto 1 está compensando mais";
            } else if (resultado2 > resultado1) {
                mensagem = "Produto 2 está compensando mais";
            } else {
                mensagem = "Os dois têm o mesmo custo-benefício";
            }

            // Exibir o resultado
            resultado.setText(mensagem);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show();
        }
    }
}