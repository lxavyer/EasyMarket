package ufmg.coltec.easymarket.model;

public class Gasto {
    private int id;
    private String nome;
    private double valor;
    private String data;
    private String itens;

    public Gasto(int id, String nome, double valor, String data, String itens) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.data = data;
        this.itens = itens;
    }

    public Gasto(String nome, double valor, String data, String itens) {
        this.nome = nome;
        this.valor = valor;
        this.data = data;
        this.itens = itens;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getValor() { return valor; }
    public String getData() { return data; }
    public String getItens() { return itens; }
    public void setNome(String nome) { this.nome = nome; }

    public void setValor(double valor) {
        this.valor = valor;
    }
    public void setItens(String itens) {
        this.itens = itens;
    }

    public void setData(String data) {
        this.data = data;
    }
}

