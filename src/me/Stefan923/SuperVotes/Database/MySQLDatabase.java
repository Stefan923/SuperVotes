package me.Stefan923.SuperVotes.Database;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class MySQLDatabase extends Database {

    private String tablename;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public MySQLDatabase(String host, Integer port, String dbname, String tablename, String username, String password) throws SQLException {
        this.tablename = tablename;
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
        connection = DriverManager.getConnection(url, username, password);
        initTable();
    }

    @Override
    public ResultSet get(String playerKey) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM %table WHERE `playerKey` = ?;".replace("%table", tablename));
            preparedStatement.setString(1, playerKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResultSet get(String playerKey, String key) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT `%key` FROM %table WHERE `playerKey` = ?;".replace("%table", tablename).replace("%key", key));
            preparedStatement.setString(1, playerKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String playerKey) {
        new Thread(() -> {
            try {
                PreparedStatement statement = getConnection().prepareStatement("DELETE FROM %table WHERE `playerKey` = ?".replace("%table", tablename));
                statement.setString(1, playerKey);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void put(String playerKey, String key, Integer value) {
        new Thread(() -> {
            try {
                if (value != null) {
                    PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO %table (`playerKey`, `%key`) VALUES (?,?) ON DUPLICATE KEY UPDATE `%key` = ?".replace("%table", tablename).replace("%key", key));
                    preparedStatement.setString(1, playerKey);
                    preparedStatement.setInt(2, value);
                    preparedStatement.setInt(3, value);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean has(String playerKey) {
        boolean result = false;
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT `playerKey` FROM %table WHERE `playerKey` = ?".replace("%table", tablename));
            statement.setString(1, playerKey);
            ResultSet rs = statement.executeQuery();
            result = rs.next();
            rs.close();
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void clear() {
        try {
            PreparedStatement statement = getConnection().prepareStatement("TRUNCATE TABLE %table".replace("%table", tablename));
            statement.executeQuery();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getKeys() {
        Set<String> tempset = new HashSet<>();
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT `playerKey` FROM %table".replace("%table", tablename));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tempset.add(rs.getString("id"));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempset;
    }

    private void initTable() throws SQLException {
        String tablequery = "CREATE TABLE IF NOT EXISTS %table (`playerKey` VARCHAR(36) PRIMARY KEY, `votes` INT(9));".replace("%table", tablename);
        PreparedStatement preparedStatement = getConnection().prepareStatement(tablequery);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private void connect() {
        try {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        if (this.connection == null || !this.connection.isValid(5)) {
            this.connect();
        }
        return this.connection;
    }

}
