package model.entities;


public class Produto {
    private int id;
    private String nome;
    private String ean;
    private double precoCusto;
    private double precoVenda;
    private int quantidade;

    // Construtor que aceita todos os atributos
    public Produto(int id, String nome, String ean, double precoCusto, double precoVenda, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.ean = ean;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.quantidade = quantidade;
    }

    // Construtor que não tem o id
    public Produto(String ean, String nome, int quantidade, double precoCusto, double precoVenda) {
        this.ean = ean;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
    }

    public Produto() {

    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEan() { return ean; }
    public double getPrecoCusto() { return precoCusto; }
    public double getPrecoVenda() { return precoVenda; }
    public int getQuantidade() { return quantidade; }

    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }
}

