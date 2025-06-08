package fiap.tds.repositories;

import fiap.tds.models.Users;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsersRepository implements CrudRepository<Users, Long> {
    @Inject
    DataSource dataSource;

    private static final String SQL_INSERT = """
            INSERT INTO USERS (USERNAME, PASSWORD)
            VALUES (?, ?)
            """;
    private static final String SQL_UPDATE = "UPDATE USERS SET USERNAME = ?, PASSWORD = ? WHERE ID = ?";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM USERS WHERE ID = ?";

    private static final String SQL_DELETE_BY_ID = "DELETE FROM USERS WHERE ID = ?";

    private static final String SQL_FIND_BY_USERNAME = "SELECT * FROM USERS WHERE USERNAME = ?";

    private Users mapRowToUsers(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setId(rs.getLong("ID"));
        user.setUsername(rs.getString("USERNAME"));
        user.setPassword(rs.getString("PASSWORD"));
        return user;
    }


    @Override
    public Users save(Users entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }


    private Users insert(Users entity) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_INSERT, new String[]{"ID"})) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.executeUpdate();

            int affectedRows = ps.getUpdateCount();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar Users, nenhuma linha afetada.");
            }

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                } else{
                    throw new SQLException("Falha ao criar Usuario, nao foi possivel obter o ID gerado.");
                }
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir Users:", e);
        }
    }

    private Users update(Users entity) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setLong(3, entity.getId());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Nenhuma linha atualizada para Users ID: " + entity.getId() + ". Verifique se o ID existe.");
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Users", e);
        }
    }

    @Override
    public Optional<Users> findById(Long id) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsers(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Users por ID:", e);
        }
    }

    public Optional<Users> findByUsername(String username) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsers(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Users por Username:", e);
        }
    }

    @Override
    public List<Users> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Nenhuma linha deletada para Users ID: \" + id + \". Pode não existir.\"");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar Users por ID:", e);
        }
    }

    @Override
    public long count() {
        return 0;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
