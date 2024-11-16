package ufmg.coltec.easymarket.model;

public class Produto {

    private int id;
    private String nome;
    private int quantidade;
    private boolean selecionado;

    // Construtor com ID
    public Produto(int id, String nome, int quantidade, boolean selecionado) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.selecionado = selecionado;
    }

    // Construtor sem ID (para inserção)
    public Produto(String nome, int quantidade, boolean selecionado) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.selecionado = selecionado;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
}
