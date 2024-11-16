package ufmg.coltec.easymarket;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializando o Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Botões para navegar para as diferentes atividades
        Button listaDeComprasButton = findViewById(R.id.btn_lista_de_compras);
        Button calculadoraButton = findViewById(R.id.btn_calculadora);
        Button panfletosButton = findViewById(R.id.btn_panfletos);
        Button marketsProximosButton = findViewById(R.id.btn_markets_proximos);
        Button historicoDeGastosButton = findViewById(R.id.btn_historico_de_gastos);

        // Configurando os listeners para os botões
        listaDeComprasButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "btn_lista_de_compras");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Lista de Compras");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
            Intent intent = new Intent(MainActivity.this, ListaComprasActivity.class);
            startActivity(intent);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        });

        calculadoraButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalculadoraActivity.class);
            startActivity(intent);
        });

        panfletosButton.setOnClickListener(v -> {
            // Intent para a atividade de Panfletos (em breve)
        });

        marketsProximosButton.setOnClickListener(v -> {
            // Intent para a atividade de Markets Próximos (em breve)
        });

        // Atualização: Acessar o Histórico de Gastos
        historicoDeGastosButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoricoGastosActivity.class);
            startActivity(intent);
        });
    }
}
