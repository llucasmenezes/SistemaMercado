package mercadinho.DAO;

import model.entities.Movimentacao;
import mercadinho.database.ConexaoDB;

import java.sql.*;
import java.util.List;

public class VendaDAO {

    private Connection connection;

    public VendaDAO() {

    }


        private MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();  // Instancia o MovimentacaoDAO

        public void registrarVenda(double totalVenda, List<Movimentacao> movimentacoes) throws SQLException {
            String sql = "INSERT INTO vendas (total_venda) VALUES (?)";  // Exemplo de registro simples de venda

            try (Connection conn = getConnection()) {
                conn.setAutoCommit(false);  // Iniciar transação
                try {
                    // Insere a venda
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    stmt.setDouble(1, totalVenda);
                    stmt.executeUpdate();

                    // Pega o ID gerado para a venda
                    ResultSet rs = stmt.getGeneratedKeys();
                    int vendaId = 0;
                    if (rs.next()) {
                        vendaId = rs.getInt(1);
                    }

                    // Registra as movimentações de venda
                    for (Movimentacao movimentacao : movimentacoes) {
                        movimentacaoDAO.registrarMovimentacao(movimentacao);  // Chama o método do MovimentacaoDAO
                        System.out.println("-------------------------------------------------------------------");
                        System.out.println("Movimentação para o produto: " + movimentacao.getProduto().getNome() + " registrada.");
                    }

                   // conn.commit();  // Confirma transação
                   // System.out.println("Venda registrada com sucesso! ID da Venda: " + vendaId);

                } catch (Exception e) {
                    conn.rollback();  // Rollback se ocorrer erro
                    throw new SQLException("Erro ao registrar venda: " + e.getMessage(), e);
                }
            }
        }

        // Método para obter a conexão (Exemplo)
        private Connection getConnection() throws SQLException {
            // Seu código para obter a conexão com o banco de dados
            return ConexaoDB.getConnection();
        }
}



