package ufmg.coltec.easymarket;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ufmg.coltec.easymarket.model.Gasto;
import ufmg.coltec.easymarket.model.GastoAdapter;

public class HistoricoGastosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GastoAdapter gastoAdapter;
    private List<Gasto> gastos;
    private DatabaseHelper dbHelper;

    private final ActivityResultLauncher<Intent> addGastoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Atualizar lista após adicionar novo gasto
                    atualizarListaGastos();
                }
            });

    private final ActivityResultLauncher<Intent> updateGastoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Atualizar lista após editar gasto
                    atualizarListaGastos();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_gastos);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewGastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gastos = dbHelper.getAllGastos();
        if (gastos == null) {
            gastos = new ArrayList<>();
        }
        gastoAdapter = new GastoAdapter(gastos, dbHelper, updateGastoLauncher);
        recyclerView.setAdapter(gastoAdapter);

        FloatingActionButton fabAddGasto = findViewById(R.id.fabAddGasto);
        fabAddGasto.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddGastoActivity.class);
            addGastoLauncher.launch(intent);
        });
    }

    private void atualizarListaGastos() {
        gastos.clear();
        gastos.addAll(dbHelper.getAllGastos());
        gastoAdapter.notifyDataSetChanged();
    }
}

