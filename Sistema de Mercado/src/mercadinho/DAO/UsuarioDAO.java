package mercadinho.DAO;

import mercadinho.database.ConexaoDB;
import model.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario buscarUsuario(String username) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        Usuario usuario = null;

        try (Connection conn = ConexaoDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("senha")
                );
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar usuário: " + e.getMessage(), e);
        }

        return usuario;
    }

    public void cadastrarUsuario(String username, String senha) throws Exception {
        String sql = "INSERT INTO usuarios (username, senha) VALUES (?, ?)";

        try (Connection conn = ConexaoDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, senha);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao cadastrar usuário: " + e.getMessage(), e);
        }
    }
}
