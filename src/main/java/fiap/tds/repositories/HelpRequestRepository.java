package fiap.tds.repositories;

import fiap.tds.models.HelpRequest;
import fiap.tds.models.Status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class HelpRequestRepository implements CrudRepository<HelpRequest, Long> {
    @Inject
    DataSource dataSource;

    private static final String SQL_INSERT = """
    INSERT INTO HELP_REQUEST 
    (CEP, LATITUDE, LONGITUDE, REQUEST_TIMESTAMP, STATUS, NOTES, CONTACT_INFO, ENDERECO_APROXIMADO, UPDATE_BY, USER_ID)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?)
""";

    private static final String SQL_UPDATE = "UPDATE HELP_REQUEST SET NOTES = ?, CONTACT_INFO = ?, REQUEST_TIMESTAMP = ?, STATUS = ?, LATITUDE = ?, LONGITUDE = ?, ENDERECO_APROXIMADO = ?, UPDATE_BY = ?, USER_ID = ? WHERE ID = ?";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM HELP_REQUEST WHERE ID = ?";

    private static final String SQL_FIND_ALL = "SELECT * FROM HELP_REQUEST";

    private static final String SQL_DELETE_BY_ID = "DELETE FROM HELP_REQUEST WHERE ID = ?";

    private static final String SQL_COUNT_ALL = "SELECT COUNT(*) FROM HELP_REQUEST";

    private HelpRequest mapRowToHelpRequest(ResultSet rs) throws SQLException {
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setId(rs.getLong("ID"));
        helpRequest.setCep(rs.getString("CEP"));
        helpRequest.setNotes(rs.getString("NOTES"));
        //Esse updateBy tem que pegar o id do usuario que reportou a ajuda.
        helpRequest.setUpdateBy(rs.getLong(("UPDATE_BY")));
        helpRequest.setUserId(rs.getLong(("USER_ID")));
        helpRequest.setContactInfo(rs.getString("CONTACT_INFO"));
        helpRequest.setRequestTimestamp(rs.getTimestamp("REQUEST_TIMESTAMP").toLocalDateTime());
        helpRequest.setStatus(Status.valueOf(rs.getString("STATUS")));
        helpRequest.setLatitude(rs.getDouble("LATITUDE"));
        helpRequest.setLongitude(rs.getDouble("LONGITUDE"));
        helpRequest.setEnderecoAproximado(rs.getString("ENDERECO_APROXIMADO"));
        return helpRequest;
    }

    @Override
    public HelpRequest save(HelpRequest entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    private HelpRequest insert(HelpRequest entity) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_INSERT, new String[]{"ID"})) {
            ps.setString(1, entity.getCep());
            ps.setDouble(2, entity.getLatitude());
            ps.setDouble(3, entity.getLongitude());
            ps.setTimestamp(4, entity.getRequestTimestamp() != null ? Timestamp.valueOf(entity.getRequestTimestamp()) : null);
            ps.setString(5, entity.getStatus() != null ? entity.getStatus().name() : null);
            ps.setString(6, entity.getNotes());
            ps.setString(7, entity.getContactInfo());
            ps.setString(8, entity.getEnderecoAproximado());
            // Adicionado hoje, !!!TESTAR!!!
            ps.setLong(9, entity.getUpdateBy() != null ? entity.getUpdateBy() : 1L);
            ps.setLong(10, entity.getUserId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar HelpRequest, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Falha ao criar HelpRequest, não foi possível obter o ID gerado.");
                }
            }
            return entity;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir HelpRequest: " + e.getMessage(), e);
        }
    }

    private HelpRequest update(HelpRequest entity) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, entity.getNotes());
            ps.setString(2, entity.getContactInfo());
            ps.setTimestamp(3, entity.getRequestTimestamp() != null ? Timestamp.valueOf(entity.getRequestTimestamp()) : null);
            ps.setString(4, entity.getStatus() != null ? entity.getStatus().name() : null);
            ps.setDouble(5, entity.getLatitude());
            ps.setDouble(6, entity.getLongitude());
            ps.setString(7, entity.getEnderecoAproximado());
            ps.setLong(8, entity.getUpdateBy() != null ? entity.getUpdateBy() : 1L);
            ps.setLong(9, entity.getUserId());
            ps.setLong(10, entity.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Nenhuma linha atualizada para HelpRequest ID: " + entity.getId() + ". Verifique se o ID existe.");
            }
            return entity;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar HelpRequest: " + e.getMessage(), e);
        }
    }


    @Override
    public Optional<HelpRequest> findById(Long id) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToHelpRequest(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar HelpRequest por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<HelpRequest> findAll() {
        List<HelpRequest> requests = new ArrayList<>();
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                requests.add(mapRowToHelpRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os HelpRequests: " + e.getMessage(), e);
        }
        return requests;
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE_BY_ID)) {

            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {

                System.out.println("Nenhuma linha deletada para HelpRequest ID: " + id + ". Pode não existir.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar HelpRequest por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public long count() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_COUNT_ALL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao contar HelpRequests: " + e.getMessage(), e);
        }
        return 0;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
