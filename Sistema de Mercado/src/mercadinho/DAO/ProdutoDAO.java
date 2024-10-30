package mercadinho.DAO;

import model.entities.Produto;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static mercadinho.database.ConexaoDB.getConnection;


public class ProdutoDAO {

    String url = "jdbc:mysql://localhost:3306/mercadinho_mml";
    String user = "root";
    String password = "";

    // Método para buscar produto por EAN
    public Produto buscarPorEan(String ean) throws Exception {
        String sql = "SELECT * FROM produtos WHERE ean = ?";
        Produto produto = null;

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ean);
            System.out.println("Executando consulta para o EAN: " + ean); // Log para verificar a consulta
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                produto = new Produto(
                        rs.getString("ean"),
                        rs.getString("nome"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_custo"),
                        rs.getDouble("preco_venda")
                );
                System.out.println("-------------------------------------------------------------------");
                System.out.println("Produto encontrado: " + produto.getNome() + ", Quantidade: " + produto.getQuantidade());
            } else {
                System.out.println("-------------------------------------------------------------------");

                System.out.println("Nenhum produto encontrado com o EAN: " + ean);
            }
        } catch (SQLException e) {
            System.out.println("-------------------------------------------------------------------");

            throw new Exception("Erro ao se conectar com o banco de dados" + e);
        }

        return produto;
    }


    // Método para cadastrar um novo produto
    public void cadastrarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (ean, nome, quantidade, preco_custo, preco_venda) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getEan()); // EAN
            stmt.setString(2, produto.getNome()); // Nome
            stmt.setInt(3, produto.getQuantidade()); // Quantidade
            stmt.setDouble(4, produto.getPrecoCusto()); // Preço de custo
            stmt.setDouble(5, produto.getPrecoVenda()); // Preço de venda

            // Debugging: imprime os valores que estão sendo salvos
            System.out.println("Cadastrando produto:");
            System.out.println("EAN: " + produto.getEan());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Quantidade: " + produto.getQuantidade());
            System.out.println("Preço de Custo: " + produto.getPrecoCusto());
            System.out.println("Preço de Venda: " + produto.getPrecoVenda());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("-------------------------------------------------------------------");

            throw new SQLException("Erro ao cadastrar produto: " + e.getMessage());
        }
    }


    public void adicionarProduto(Produto produto) throws Exception {
        String sql = "INSERT INTO produtos (nome, ean, preco_custo, preco_venda, quantidade) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            // Preparando a instrução SQL
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getEan());
            stmt.setDouble(3, produto.getPrecoCusto());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setInt(5, produto.getQuantidade());

            // Executa a query de inserção
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Produto '" + produto.getNome() + "' adicionado com sucesso!");
            } else {
                System.out.println("-------------------------------------------------------------------");
                throw new Exception("Falha ao adicionar o produto '" + produto.getNome() + "'. Nenhuma linha foi afetada.");
            }

        } catch (SQLException e) {
            // Detalha o erro caso a inserção falhe
            System.out.println("-------------------------------------------------------------------");
            throw new Exception("Erro ao adicionar produto '" + produto.getNome() + "': " + e.getMessage(), e);
        }
    }

    public void atualizarProduto(Produto produto) throws Exception {
        String sql = "UPDATE produtos SET quantidade = ?, preco_custo = ?, preco_venda = ? WHERE ean = ?";

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, produto.getQuantidade());
            stmt.setDouble(2, produto.getPrecoCusto());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setString(4, produto.getEan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("-------------------------------------------------------------------");
            throw new Exception("Erro ao atualizar o produto com EAN: " + produto.getEan(), e);
        }
    }

    public void atualizarProduto2(Produto produto) throws Exception {
        String sql = "UPDATE produtos SET quantidade = ? WHERE ean = ?";

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, produto.getQuantidade()); // Nova quantidade
            stmt.setString(2, produto.getEan()); // EAN do produto

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("-------------------------------------------------------------------");
            throw new Exception("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    public List<Produto> buscarTodosProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String query = "SELECT * FROM produtos";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getInt("id"));
                produto.setEan(resultSet.getString("ean"));
                produto.setNome(resultSet.getString("nome"));
                produto.setQuantidade(resultSet.getInt("quantidade"));
                produto.setPrecoCusto(resultSet.getDouble("preco_custo"));
                produto.setPrecoVenda(resultSet.getDouble("preco_venda"));
                produtos.add(produto);
            }
        }

        return produtos;
    }

    public Produto buscarPorNome(String nome) throws Exception {
        String sql = "SELECT * FROM produtos WHERE nome = ?";

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Supondo que o construtor de Produto aceite esses parâmetros
                return new Produto(rs.getInt("id"), rs.getString("nome"), rs.getString("ean"),
                        rs.getDouble("preco_custo"), rs.getDouble("preco_venda"), rs.getInt("quantidade"));
            } else {
                return null; // Produto não encontrado
            }

        } catch (SQLException e) {
            System.out.println("-------------------------------------------------------------------");
            throw new Exception("Erro ao buscar produto pelo nome: " + nome, e);
        }
    }


}



