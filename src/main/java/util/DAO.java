package util;

import entity.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DAO {
    private static final Logger logger = LoggerFactory.getLogger(DAO.class);
    private static final String tableName = "Events";
    private static Connection connection;

    public DAO() throws SQLException {
        logger.info("Initializing DB");
        startDB();
    }

    private void startDB() throws SQLException{
        String jdbcURL = "jdbc:hsqldb:hsql:file:event";
        connection = DriverManager.getConnection(jdbcURL, "SA", "");
    }

    public void createTable() throws SQLException {
        logger.info("Creating table if doesn't exist");
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + tableName +" (id VARCHAR(50) NOT NULL, duration BIGINT NOT NULL, " +
                "type VARCHAR(50), host VARCHAR(50), alert BOOLEAN NOT NULL)";

        connection.createStatement().executeUpdate(sqlQuery);
    }

    public void insertEvent(LogEvent event) throws SQLException {
        String sqlQuery = "INSERT INTO " + tableName + " (id, duration, type, host, alert)  VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, event.getId());
        preparedStatement.setLong(2, event.getDuration());
        preparedStatement.setString(3, event.getType());
        preparedStatement.setString(4, event.getHost());
        preparedStatement.setBoolean(5, event.getAlert());

        preparedStatement.executeUpdate();
    }

    public void selectAll() throws SQLException {
        logger.info("Retrieving all rows from " + tableName + " table");

        String sqlQuery = "SELECT * FROM " + tableName;
        ResultSet results = connection.createStatement().executeQuery(sqlQuery);

        while (results.next()) {
            String id = results.getString(1);
            long duration = results.getLong(2);
            String type = results.getString(3);
            String host = results.getString(4);
            String alert = results.getString(5);

            String row = "id: " + id + ", duration: " + duration;
            if (type != null) { row += ", type: " + type; }
            if (host != null) { row += ", host: " + host; }
            System.out.println(row + ", alert: " + alert);
        }
    }

    public void clearTable() throws SQLException {
        logger.info("Clearing the table: " + tableName);

        String sqlQuery = "DELETE FROM " + tableName;
        connection.createStatement().executeUpdate(sqlQuery);
    }

    public void close() throws SQLException {
        logger.info("Closing connection");
        connection.close();
    }
}
