package ufmg.coltec.easymarket.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ufmg.coltec.easymarket.DatabaseHelper;
import ufmg.coltec.easymarket.HistoricoGastosActivity;
import ufmg.coltec.easymarket.R;
import ufmg.coltec.easymarket.UpdateGastoActivity;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private List<Gasto> gastos;
    private DatabaseHelper dbHelper;
    private Context context;
    private ActivityResultLauncher<Intent> updateGastoLauncher;

    public GastoAdapter(List<Gasto> gastos, DatabaseHelper dbHelper, ActivityResultLauncher<Intent> updateGastoLauncher) {
        this.gastos = gastos;
        this.dbHelper = dbHelper;
        this.updateGastoLauncher = updateGastoLauncher;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gasto, parent, false);
        context = parent.getContext();
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = gastos.get(position);

        // Exibindo o nome, valor, data e itens
        holder.textViewNome.setText(gasto.getNome());
        holder.textViewValor.setText(String.format("R$ %.2f", gasto.getValor()));
        holder.textViewData.setText(gasto.getData());
        holder.textViewItens.setText(gasto.getItens());

        // Botão para editar o gasto
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateGastoActivity.class);
            intent.putExtra("gasto_id", gasto.getId());
            updateGastoLauncher.launch(intent);
        });

        // Botão para deletar o gasto
        holder.btnDelete.setOnClickListener(v -> {
            if (dbHelper.deleteGasto(gasto.getId())) {
                gastos.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Gasto deletado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Erro ao deletar gasto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public static class GastoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNome, textViewValor, textViewData, textViewItens;
        ImageButton btnEdit, btnDelete;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewValor = itemView.findViewById(R.id.textViewValor);
            textViewData = itemView.findViewById(R.id.textViewData);
            textViewItens = itemView.findViewById(R.id.textViewItens);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
