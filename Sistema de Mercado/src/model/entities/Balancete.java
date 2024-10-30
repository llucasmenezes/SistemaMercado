package model.entities;

import model.entities.Movimentacao;

import java.util.ArrayList;
import java.util.List;

public class Balancete {

    private List<Movimentacao> movimentacoes;

    public Balancete() {
        this.movimentacoes = new ArrayList<>();
    }

    public void registrarMovimentacao(Movimentacao movimentacao) {
        movimentacoes.add(movimentacao);
    }

    public double calcularLucroTotal() {
        double lucroTotal = 0.0;
        for (Movimentacao movimentacao : movimentacoes) {
            // Apenas somar lucros de saídas
            if (movimentacao.getTipo().equals("Saída")) {
                lucroTotal += movimentacao.calcularLucro();
            }
        }
        return lucroTotal;
    }

    public double calcularPrejuizoTotal() {
        double prejuizoTotal = 0.0;
        for (Movimentacao movimentacao : movimentacoes) {
            // Apenas somar prejuízos de entradas
            if (movimentacao.getTipo().equals("Entrada")) {
                prejuizoTotal += movimentacao.calcularPrejuizo();
            }
        }
        return prejuizoTotal;
    }

    public double calcularResultadoFinal() {
        return calcularLucroTotal() - calcularPrejuizoTotal();
    }

    public void exibirBalancete() {
        System.out.println(":: Balancete ::");
        System.out.printf("Lucro Total: R$ %.2f\n", calcularLucroTotal());
        System.out.printf("Prejuízo Total: R$ %.2f\n", calcularPrejuizoTotal());
        System.out.printf("Resultado Final: R$ %.2f\n", calcularResultadoFinal());
    }
}
