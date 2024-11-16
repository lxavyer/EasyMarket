package ufmg.coltec.easymarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ufmg.coltec.easymarket.model.Gasto;
import ufmg.coltec.easymarket.model.Produto;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "MarketDB";
    private static final int DATABASE_VERSION = 4;

    // Tabela de Produtos
    private static final String TABLE_PRODUTO = "Produto";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_QUANTIDADE = "quantidade";
    private static final String COLUMN_SELECIONADO = "selecionado";

    // Tabela de Gastos
    private static final String TABLE_GASTO = "Gasto";
    private static final String COLUMN_VALOR = "valor";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_ITENS = "itens";
    private static final String COLUMN_NOME_GASTO = "nome_gasto";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando tabelas...");

        // Criar tabela Produto
        String createProdutoTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUTO + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT NOT NULL, " +
                COLUMN_QUANTIDADE + " INTEGER NOT NULL, " +
                COLUMN_SELECIONADO + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(createProdutoTable);

        // Criar tabela Gasto
        String createGastoTable = "CREATE TABLE IF NOT EXISTS " + TABLE_GASTO + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME_GASTO + " TEXT NOT NULL, " +
                COLUMN_VALOR + " REAL NOT NULL, " +
                COLUMN_DATA + " TEXT NOT NULL, " +
                COLUMN_ITENS + " TEXT NOT NULL);";
        db.execSQL(createGastoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASTO);
        onCreate(db);
    }

    // MÉTODOS PARA PRODUTOS

    public boolean insertProduto(Produto produto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, produto.getNome());
        values.put(COLUMN_QUANTIDADE, produto.getQuantidade());
        values.put(COLUMN_SELECIONADO, produto.isSelecionado() ? 1 : 0);
        long result = db.insert(TABLE_PRODUTO, null, values);
        return result != -1;
    }

    public List<Produto> getAllProdutos() {
        List<Produto> produtos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUTO, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME));
                int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTIDADE));
                boolean selecionado = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SELECIONADO)) == 1;
                produtos.add(new Produto(id, nome, quantidade, selecionado));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return produtos;
    }

    public Produto getProdutoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUTO, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        Produto produto = null;
        if (cursor != null && cursor.moveToFirst()) {
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME));
            int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTIDADE));
            boolean selecionado = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SELECIONADO)) == 1;
            produto = new Produto(id, nome, quantidade, selecionado);
        }
        if (cursor != null) cursor.close();
        return produto;
    }

    public boolean updateProduto(Produto produto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, produto.getNome());
        values.put(COLUMN_QUANTIDADE, produto.getQuantidade());
        values.put(COLUMN_SELECIONADO, produto.isSelecionado() ? 1 : 0);
        int rowsAffected = db.update(TABLE_PRODUTO, values, COLUMN_ID + " = ?", new String[]{String.valueOf(produto.getId())});
        return rowsAffected > 0;
    }

    public boolean deleteProduto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_PRODUTO, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // Método para deletar todos os produtos
    public boolean deleteAllProdutos() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("Produto", null, null);
        return rowsDeleted > 0;
    }


    // MÉTODOS PARA GASTOS

    // Método para inserir um novo gasto
    public boolean insertGasto(Gasto gasto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOME_GASTO, gasto.getNome());
        values.put(COLUMN_VALOR, gasto.getValor());
        values.put(COLUMN_DATA, gasto.getData());
        values.put(COLUMN_ITENS, gasto.getItens());

        try {
            long result = db.insert(TABLE_GASTO, null, values);
            if (result != -1) {
                Log.d("DatabaseHelper", "Gasto inserido com sucesso");
                return true;
            } else {
                Log.e("DatabaseHelper", "Erro ao inserir gasto");
                return false;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Exception ao inserir gasto", e);
            return false;
        }
    }

    // Método para obter todos os gastos
    public List<Gasto> getAllGastos() {
        List<Gasto> gastos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Gasto", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome_gasto"));
                double valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"));
                String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String itens = cursor.getString(cursor.getColumnIndexOrThrow("itens"));

                Gasto gasto = new Gasto(id, nome, valor, data, itens);
                gastos.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return gastos;
    }

    public Gasto getGastoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GASTO, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        Gasto gasto = null;
        if (cursor != null && cursor.moveToFirst()) {
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_GASTO));
            double valor = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VALOR));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA));
            String itens = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITENS));
            gasto = new Gasto(id, nome, valor, data, itens);
        }
        if (cursor != null) cursor.close();
        return gasto;
    }

    public boolean updateGasto(Gasto gasto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_GASTO, gasto.getNome());
        values.put(COLUMN_VALOR, gasto.getValor());
        values.put(COLUMN_DATA, gasto.getData());
        values.put(COLUMN_ITENS, gasto.getItens());
        int rowsAffected = db.update(TABLE_GASTO, values, COLUMN_ID + " = ?", new String[]{String.valueOf(gasto.getId())});
        return rowsAffected > 0;
    }

    public boolean deleteGasto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_GASTO, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }
}
