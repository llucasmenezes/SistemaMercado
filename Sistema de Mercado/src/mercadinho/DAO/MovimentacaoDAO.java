package mercadinho.DAO;

import model.entities.Produto;
import model.entities.Movimentacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static mercadinho.database.ConexaoDB.getConnection;

public class MovimentacaoDAO {
    ProdutoDAO produtoDAO = new ProdutoDAO();


    // Implementação do registro de movimentações
    public void registrarMovimentacao(Movimentacao movimentacao) throws Exception {
        String pgs = "INSERT INTO movimentacoes (produto_id, tipo_movimentacao, quantidade, data_movimentacao) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(pgs);

            // Aqui você deve buscar o ID do produto baseado no EAN
            int produtoId = buscarIdPorEan(movimentacao.getProduto().getEan());

            stmt.setInt(1, produtoId);
            stmt.setString(2, movimentacao.getTipo());
            stmt.setInt(3, movimentacao.getQuantidade());
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(movimentacao.getDataMovimentacao()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao registrar movimentação: " + e.getMessage(), e);
        }
    }

    // Método auxiliar para buscar o ID do produto com base no EAN
    private int buscarIdPorEan(String ean) throws SQLException {
        String pgs = "SELECT id FROM produtos WHERE ean = ?"; // Verifique se 'ean' está correto
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(pgs)) {
            stmt.setString(1, ean);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // Retorna o ID do produto
            } else {
                throw new SQLException("Produto não encontrado com o EAN: " + ean);
            }
        }
    }

    public void registrarMovimentacaoBalancete(Movimentacao movimentacao) throws Exception {
        String pgs = "INSERT INTO balancete (ean, tipo_movimentacao, quantidade, preco_custo, preco_venda, data_movimentacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(pgs);

            int produtoId = buscarIdPorEan(movimentacao.getProduto().getEan());

            stmt.setInt(1, produtoId);
            stmt.setString(2, movimentacao.getTipo());
            stmt.setInt(3, movimentacao.getQuantidade());
            stmt.setDouble(4, movimentacao.getPrecoCusto());
            stmt.setDouble(5, movimentacao.getPrecoVenda());
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(movimentacao.getDataMovimentacao()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao registrar movimentação no balancete: " + e.getMessage(), e);
        }
    }

    public List<Movimentacao> carregarMovimentacoes() throws Exception {
        String sql = "SELECT * FROM balancete";
        List<Movimentacao> movimentacoes = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Aqui agora estamos usando o EAN diretamente da tabela balancete
                Produto produto = produtoDAO.buscarPorEan(rs.getString("ean")); // Buscar produto pelo EAN
                Movimentacao movimentacao = new Movimentacao(
                        produto,
                        rs.getString("tipo_movimentacao"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_custo"),
                        rs.getDouble("preco_venda")
                );
                movimentacoes.add(movimentacao);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao carregar movimentações: " + e.getMessage(), e);
        }

        return movimentacoes;
    }


    public List<Movimentacao> recuperarBalancete() throws SQLException {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        String query = "SELECT * FROM balancete";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Movimentacao movimentacao = new Movimentacao();
                movimentacao.setTipo(resultSet.getString("tipo_movimentacao"));
                movimentacao.setQuantidade(resultSet.getInt("quantidade"));
                movimentacao.setPrecoCusto(resultSet.getDouble("preco_custo"));
                movimentacao.setPrecoVenda(resultSet.getDouble("preco_venda"));
                movimentacao.setDataMovimentacao(resultSet.getTimestamp("data_movimentacao").toLocalDateTime());

                movimentacoes.add(movimentacao);
            }
        }

        return movimentacoes;
    }

}


