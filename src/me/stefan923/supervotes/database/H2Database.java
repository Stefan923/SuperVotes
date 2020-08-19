package me.stefan923.supervotes.database;

import me.stefan923.supervotes.SuperVotes;
import me.stefan923.supervotes.utils.MessageUtils;

import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class H2Database extends Database implements MessageUtils {

    private final String tablename;
    private Connection connection;

    public H2Database(String tablename) throws SQLException, ClassNotFoundException {
        SuperVotes instance = SuperVotes.getInstance();
        this.tablename = tablename;

        Class.forName("org.h2.Driver");

        String url = "jdbc:h2:" + instance.getDataFolder().getAbsolutePath() + File.separator + "database;mode=MySQL";
        connection = DriverManager.getConnection(url);
        if (connection == null)
            return;
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS %table (`id` INT NOT NULL AUTO_INCREMENT, `playerKey` VARCHAR(36) PRIMARY KEY, `votes` INT(9));".replace("%table", tablename));
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    @Override
    public ResultSet get(String playerKey) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM %table WHERE `playerKey` = ?;".replace("%table", tablename));
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `%key` FROM %table WHERE `playerKey` = ?;".replace("%table", tablename).replace("%key", key));
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
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM %table WHERE `playerKey` = ?".replace("%table", tablename));
                preparedStatement.setString(1, playerKey);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void put(String playerKey, String key, Integer value) {
        new Thread(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO %table (`playerKey`, `%key`) VALUES (?,?) ON DUPLICATE KEY UPDATE `%key` = ?".replace("%table", tablename).replace("%key", key));
                preparedStatement.setString(1, playerKey);
                preparedStatement.setInt(2, value);
                preparedStatement.setInt(3, value);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean has(String key) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `playerKey` FROM %table WHERE `playerKey` = ?;".replace("%table", tablename));
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            result = resultSet.next();
            resultSet.close();
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void clear() {
        new Thread(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE * FROM %table;".replace("%table", tablename));
                preparedStatement.executeQuery();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Set<String> getKeys() {
        Set<String> tempSet = new HashSet<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `playerKey` FROM %table;".replace("%table", tablename));
            ResultSet result = preparedStatement.executeQuery();
            while (result.next())
                tempSet.add(result.getString("playerKey"));
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempSet;
    }


}
