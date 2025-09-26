package repository;

import domain.Enums.Role;
import domain.User;
import org.mindrot.jbcrypt.BCrypt;
import util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository{
    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try(PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return mapResultSetToUser(rs);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try(PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setString(1,email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return mapResultSetToUser(rs);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (email,password,role) VALUES (?,?,?)";
        try(PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setString(1,user.getEmail());
            stmt.setString(2,user.getMotDePasseHash());
            stmt.setString(3,user.getRole().name());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                user.setId(rs.getLong(1));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email = ?, password = ? , role = ? WHERE id = ?";
        try(PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setString(1,user.getEmail());
            stmt.setString(2,user.getMotDePasseHash());
            stmt.setString(2,user.getRole().name());
            stmt.setLong(4,user.getId());
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try(PreparedStatement stmt = JDBCUtil.getInstance().getConnection().prepareStatement(sql)){
            stmt.setInt(1,id);
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException{
        User user  = new User(
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
        );
        user.setId(rs.getLong("id"));
        return user;
    }

}
