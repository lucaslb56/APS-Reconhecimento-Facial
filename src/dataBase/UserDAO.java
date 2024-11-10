package dataBase;

import authentication.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Metodo para inserir usuário
    public void insertUser(String nome, String email, int nivelPermissao, String bioFacial) {
        String sql = "INSERT INTO usuarios(nome, email, nivelPermissao, bioFacial) VALUES(?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setInt(3, nivelPermissao);
            pstmt.setString(4, bioFacial);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    // Metodo para pegar usuário pelo email
    public User getUsuarioByEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setNome(rs.getString("nome"));
                user.setEmail(rs.getString("email"));
                user.setNivelPermissao(rs.getInt("nivelPermissao"));
                user.setBioFacial(rs.getString("bioFacial"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com banco de dados!");
        }
        return user;
    }

    // Metodo para atualzar um usuário
    public String updateUser(int id, String nome, String email, int nivelPermissao, String bioFacial) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, nivelPermissao = ?, bioFacial = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setInt(3, nivelPermissao);
            pstmt.setString(4, bioFacial);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            return "Usuário atualizado com sucesso!";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Erro ao atualizar usuário! "+e.getMessage();
        }
    }

    // Metodo para excluir um usuário
    public String deleteUser(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return "Usuário excluído com sucesso!";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Erro ao excluir usuário! "+e.getMessage();
        }
    }

    // metodo para listar usuários
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM usuarios";
        List<User> listUsers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User usuario = new User();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setNivelPermissao(rs.getInt("nivelPermissao"));
                usuario.setBioFacial(rs.getString("bioFacial"));
                listUsers.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listUsers;
    }
}
