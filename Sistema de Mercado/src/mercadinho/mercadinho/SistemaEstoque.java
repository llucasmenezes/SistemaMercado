package mercadinho.mercadinho;

import model.entities.*;
import mercadinho.DAO.MovimentacaoDAO;
import mercadinho.DAO.ProdutoDAO;
import mercadinho.DAO.UsuarioDAO;
import mercadinho.DAO.VendaDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SistemaEstoque {
    Produto produto;

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Balancete balancete = new Balancete();
    private VendaDAO vendaDAO;

    public static void main(String[] args) {
        SistemaEstoque sistema = new SistemaEstoque();
        sistema.menuInicial();
    }

    public void menuInicial() {
        Scanner scanner = new Scanner(System.in);

        while (true) {


            try{
            System.out.println(":: Bem-vindo ao Sistema de Estoque :: MML ::");
            System.out.println("1. Login");
            System.out.println("2. Cadastrar Novo Usuário");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");



            int opcao = scanner.nextInt();
            scanner.nextLine();  // Limpar o buffer


                switch (opcao) {
                    case 1 -> login();
                    case 2 -> cadastrarUsuario();
                    case 3 -> {
                        System.out.println("Saindo...");
                        return;
                    }
                    default -> System.out.println("Opçao inválida. Tente novamente.");
                }
            } catch(InputMismatchException e){
                System.out.println("Opcçao invalida. tente novamente");
                scanner.nextLine();
            }
        }
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Usuário: ");
        String username = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {
            Usuario usuario = usuarioDAO.buscarUsuario(username);

            if (usuario != null && usuario.getSenha().equals(senha)) {
                System.out.println("Login bem-sucedido! Acessando o sistema MML...");
                menuPrincipal();
            } else {
                System.out.println("Usuário ou senha incorretos.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void cadastrarUsuario() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome de usuário: ");
        String username = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        try {
            // Verificar se o usuário já existe
            Usuario usuarioExistente = usuarioDAO.buscarUsuario(username);
            if (usuarioExistente != null) {
                System.out.println("Usuário já existe. Escolha um nome diferente.");
                return;
            }

            // Cadastrar o novo usuário
            usuarioDAO.cadastrarUsuario(username, senha);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public void menuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

            while(continuar) {
                try {
                    System.out.println("\nMenu Principal");
                    System.out.println("1. Adicionar Item");
                    System.out.println("2. Remover Item");
                    System.out.println("3. Verificar quantidade do item");
                    System.out.println("4. Cadastrar Produto");
                    System.out.println("5. Registrar Venda");
                    System.out.println("6. Ver Balancete");
                    System.out.println("7. verificar estoque geral");
                    System.out.println("8. Sair");

                    int opcao = scanner.nextInt();
                    scanner.nextLine();  // Limpa o buffer

                    switch (opcao) {
                        case 1 -> adicionarItem(scanner);
                        case 2 -> removerItem(scanner);
                        case 3 -> verificarEstoque(scanner);
                        case 4 -> cadastrarProduto(scanner);
                        case 5 -> registrarVenda(scanner);
                        case 6 -> verBalancete();
                        case 7 -> verificarProdutosGerais();
                        case 8 -> {
                            System.out.println("Saindo...");
                            return;
                        }
                        default -> System.out.println("Opção inválida! Por favor, escolha uma das opções do menu.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Opcao invalida! Por favor, esocolha uma das opcoes do menu.");
                    scanner.nextLine();
                }
            }
    }

    // Método para registrar vendas
    public void registrarVenda(Scanner scanner) {
        try {
            System.out.print("Quantos produtos deseja vender? ");
            int numProdutos = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            double totalVenda = 0.0; // Total da venda
            List<Movimentacao> movimentacoes = new ArrayList<>(); // Lista para armazenar as movimentações de saída

            for (int i = 0; i < numProdutos; i++) {
                System.out.print("Digite o EAN do produto " + (i + 1) + ": ");
                String eanProduto = scanner.nextLine();

                // Log de verificação
                System.out.println("Buscando produto com EAN: " + eanProduto);
                Produto produto = produtoDAO.buscarPorEan(eanProduto); // Busca por EAN

                if (produto == null) {
                    System.out.println("Erro: Produto não encontrado com o EAN: " + eanProduto);
                    continue;  // Continua para o próximo produto
                }

                System.out.print("Digite a quantidade vendida para o produto " + produto.getNome() + ": ");
                int quantidade = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer

                // Verifica se o estoque é suficiente
                if (quantidade > produto.getQuantidade()) {
                    System.out.println("Erro: Estoque insuficiente para vender " + quantidade + " itens do produto " + produto.getNome());
                    continue;  // Continua para o próximo produto
                }

                try {
                    // Atualiza o estoque e log de depuração
                    System.out.println("Atualizando estoque para o produto: " + produto.getNome() + ", Nova quantidade: " + (produto.getQuantidade() - quantidade));
                    produto.setQuantidade(produto.getQuantidade() - quantidade);
                    produtoDAO.atualizarProduto(produto);
                } catch (SQLException e) {
                    System.out.println("Erro de banco de dados ao atualizar produto: " + e.getMessage());
                    continue; // Continua para o próximo produto
                }

                // Calcula o total da venda
                totalVenda += produto.getPrecoVenda() * quantidade;

                // Cria e armazena a movimentação de saída para cada produto
                Movimentacao movimentacao = new Movimentacao(produto, "Saída", quantidade, produto.getPrecoCusto(), produto.getPrecoVenda());
                movimentacoes.add(movimentacao); // Adiciona a movimentação à lista

                try {
                    movimentacaoDAO.registrarMovimentacao(movimentacao);
                    System.out.println("Movimentação registrada com sucesso.");
                } catch (SQLException e) {
                    System.out.println("Erro de banco de dados ao registrar movimentação: " + e.getMessage());
                    continue;
                }
            }

            if (!movimentacoes.isEmpty()) { // Só registra a venda se houver movimentações válidas
                VendaDAO vendaDAO = new VendaDAO();
                vendaDAO.registrarVenda(totalVenda, movimentacoes); // Método que grava a venda e as movimentações no banco de dados
                System.out.println("Venda registrada com sucesso! Total: R$ " + totalVenda);
            } else {
                System.out.println("Nenhuma venda foi registrada.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Erro: Entrada inválida! Certifique-se de inserir números válidos para quantidade.");
            scanner.nextLine(); // Limpa o buffer para prevenir loop infinito de erro

        } catch (SQLException e) {
            System.out.println("Erro de banco de dados: " + e.getMessage());

        } catch (NullPointerException e) {
            System.out.println("Erro: Tentativa de acessar um objeto nulo. Verifique se o produto existe no banco de dados.");

        } catch (Exception e) {
            System.out.println("Erro inesperado ao registrar venda: " + e.getMessage());
            e.printStackTrace();
        }
    }









    public void adicionarItem(Scanner scanner) {
        System.out.print("Digite o EAN do produto: ");
        String ean = scanner.nextLine();

        try {
            // Tenta buscar o produto pelo EAN
            Produto produto = produtoDAO.buscarPorEan(ean);

            // Verifica se o produto foi encontrado
            if (produto == null) {
                System.out.println("Erro ao adicionar estoque: Produto não encontrado com o EAN: " + ean);
                return;
            }

            // Solicita a quantidade a ser adicionada
            System.out.print("Digite a quantidade a ser adicionada: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();  // Limpa o buffer

            // Atualiza a quantidade no objeto Produto
            int novaQuantidade = produto.getQuantidade() + quantidade;
            produto.setQuantidade(novaQuantidade);

            // Atualiza o produto no banco de dados
            produtoDAO.atualizarProduto(produto);

            // Cria e registra a movimentação de entrada
            Movimentacao movimentacao = new Movimentacao(produto, "Entrada", quantidade, produto.getPrecoCusto(), produto.getPrecoVenda());
            movimentacaoDAO.registrarMovimentacao(movimentacao);
            balancete.registrarMovimentacao(movimentacao);

            System.out.println("Estoque adicionado com sucesso! Nova quantidade: " + novaQuantidade);
        } catch (SQLException e) {
            System.out.println("Erro de banco de dados ao adicionar estoque: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado ao adicionar estoque: " + e.getMessage());
            e.printStackTrace();  // Log completo para ajudar na depuração
        }
    }


    public void removerItem(Scanner scanner) {
        System.out.print("Digite o EAN do produto: ");
        String ean = scanner.nextLine();

        try {
            Produto produto = produtoDAO.buscarPorEan(ean);

            if (produto == null) {
                System.out.println("Erro ao remover estoque: Produto não encontrado com o EAN: " + ean);
                return;
            }

            System.out.print("Digite a quantidade a ser removida: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();  // Limpa o buffer

            if (quantidade > produto.getQuantidade()) {
                System.out.println("Erro: Estoque insuficiente para remover " + quantidade + " itens.");
                return;
            }

            produto.setQuantidade(produto.getQuantidade() - quantidade);

            Movimentacao movimentacao = new Movimentacao(produto, "Saída", quantidade, produto.getPrecoCusto(), produto.getPrecoVenda());
            movimentacaoDAO.registrarMovimentacao(movimentacao);
            balancete.registrarMovimentacao(movimentacao);

            System.out.println("Estoque removido com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao remover estoque: " + e.getMessage());
        }
    }

    public void verificarEstoque(Scanner scanner) {
        System.out.print("Digite o EAN do produto: ");
        String ean = scanner.nextLine();

        try {
            Produto produto = produtoDAO.buscarPorEan(ean);

            if (produto == null) {
                System.out.println("Erro ao verificar estoque: Produto não encontrado com o EAN: " + ean);
                return;
            }

            System.out.println("Produto: " + produto.getNome());
            System.out.println("Quantidade em estoque: " + produto.getQuantidade());
            System.out.println("Preço de custo: " + produto.getPrecoCusto());
            System.out.println("Preço de venda: " + produto.getPrecoVenda());
        } catch (Exception e) {
            System.out.println("Erro ao verificar estoque: " + e.getMessage());
        }
    }

    public void cadastrarProduto(Scanner scanner) {
        try {
            while (true) {
                System.out.print("Digite o EAN do produto: ");
                String ean = scanner.nextLine();

                // Verifica se já existe um produto com o mesmo EAN
                Produto produtoExistente = produtoDAO.buscarPorEan(ean);
                if (produtoExistente != null) {
                    System.out.println("Erro: Produto com o EAN " + ean + " já está cadastrado.");
                    System.out.print("Deseja tentar novamente? (S/N): ");
                    String opcao = scanner.nextLine().toUpperCase();

                    if (opcao.equals("N")) {
                        System.out.println("Voltando ao menu...");
                        return; // Sai do método, voltando ao menu principal
                    } else {
                        continue; // Tenta cadastrar novamente
                    }
                }

                System.out.print("Digite a descrição do produto: ");
                String descricao = scanner.nextLine();

                System.out.print("Digite a quantidade: ");
                int quantidade = scanner.nextInt();
                scanner.nextLine();  // Limpa o buffer

                System.out.print("Digite o preço de custo: ");
                double precoCusto = scanner.nextDouble();
                scanner.nextLine();  // Limpa o buffer

                System.out.print("Digite o preço de venda: ");
                double precoVenda = scanner.nextDouble();
                scanner.nextLine();  // Limpa o buffer

                // Cria o novo produto
                Produto produto = new Produto(ean, descricao, quantidade, precoCusto, precoVenda);

                // Tenta cadastrar o produto no banco de dados
                produtoDAO.cadastrarProduto(produto);
                System.out.println("Produto cadastrado com sucesso!");
                break; // Sai do loop se o cadastro for bem-sucedido
            }
        } catch (InputMismatchException e) {
            System.out.println("Erro: Entrada inválida! Verifique os valores inseridos.");
            scanner.nextLine(); // Limpa o buffer
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }


    // Método para visualizar o balancete
    public void verBalancete() {
        double lucroTotal = balancete.calcularLucroTotal();
        double prejuizoTotal = balancete.calcularPrejuizoTotal();
        double resultadoFinal = balancete.calcularResultadoFinal();

        System.out.println("\n:: Balancete ::");
        System.out.printf("Lucro Total: R$ %.2f%n", lucroTotal);
        System.out.printf("Prejuízo Total: R$ %.2f%n", prejuizoTotal);
        System.out.printf("Resultado Final: R$ %.2f%n", resultadoFinal);
    }

    public void verificarProdutosGerais() {
        try {
            // Busca todos os produtos cadastrados no banco de dados
            List<Produto> produtos = produtoDAO.buscarTodosProdutos();

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto encontrado no estoque.");
                return;
            }

            System.out.println("Produtos em Estoque:");
            System.out.println("------------------------------------------------------------");
            System.out.printf("%-20s %-20s %s%n", "EAN", "Nome", "Quantidade");

            for (Produto produto : produtos) {
                System.out.printf("%-20s %-20s %d%n", produto.getEan(), produto.getNome(), produto.getQuantidade());
            }
            System.out.println("------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Erro ao verificar produtos: " + e.getMessage());
        }
    }
}
