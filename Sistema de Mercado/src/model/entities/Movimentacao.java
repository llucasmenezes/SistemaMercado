package model.entities;




import java.time.LocalDateTime;

public class Movimentacao {
    private Produto produto;
    private String tipo; // "Entrada" ou "Saída"
    private int quantidade;
    private double precoCusto;
    private double precoVenda;
    private LocalDateTime dataMovimentacao;

    public Movimentacao(Produto produto, String tipo, int quantidade, double precoCusto, double precoVenda) {
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.dataMovimentacao = LocalDateTime.now(); // Inicializa com a data e hora atual
    }

    public Movimentacao() {

    }

    // Getters
    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public double calcularLucro() {
        // Lucro é calculado apenas em vendas (saídas)
        if (tipo.equals("Saida")) {
            return (precoVenda - precoCusto) * quantidade;
        }
        return 0.0; // Não há lucro em entradas
    }

    public double calcularPrejuizo() {
        // Prejuízo é calculado apenas em entradas
        if (tipo.equals("Entrada")) {
            return (precoCusto - precoVenda) * quantidade; // Aqui o cálculo pode ser ajustado se necessário
        }
        return 0.0; // Não há prejuízo em saídas
    }


    // Getters
    public Produto getProduto() {
        return produto;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}



