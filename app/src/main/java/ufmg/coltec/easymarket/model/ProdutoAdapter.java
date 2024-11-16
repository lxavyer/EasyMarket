package ufmg.coltec.easymarket.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ufmg.coltec.easymarket.DatabaseHelper;
import ufmg.coltec.easymarket.ListaComprasActivity;
import ufmg.coltec.easymarket.R;
import ufmg.coltec.easymarket.UpdateProdutoActivity;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private List<Produto> produtos;
    private DatabaseHelper dbHelper;
    private Context context;

    public ProdutoAdapter(List<Produto> produtos, DatabaseHelper dbHelper) {
        this.produtos = produtos;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        context = parent.getContext();
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = produtos.get(position);
        holder.textViewNome.setText(produto.getNome());
        holder.textViewQuantidade.setText(String.valueOf(produto.getQuantidade()));
        holder.checkBoxSelecionado.setChecked(produto.isSelecionado());

        // Marcar produto como selecionado/desmarcado
        holder.checkBoxSelecionado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            produto.setSelecionado(isChecked);
            dbHelper.updateProduto(produto);
        });

        // Botão de edição
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateProdutoActivity.class);
            intent.putExtra("produto_id", produto.getId());
            ((ListaComprasActivity) context).updateProdutoLauncher.launch(intent);
        });


        // Botão de deletar
        holder.btnDelete.setOnClickListener(v -> {
            if (dbHelper.deleteProduto(produto.getId())) {
                produtos.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Produto deletado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Erro ao deletar produto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNome, textViewQuantidade;
        CheckBox checkBoxSelecionado;
        ImageButton btnEdit, btnDelete;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewQuantidade = itemView.findViewById(R.id.textViewQuantidade);
            checkBoxSelecionado = itemView.findViewById(R.id.checkBoxSelecionado);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}