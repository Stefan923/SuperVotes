package me.stefan923.supervotes.database;

import java.sql.ResultSet;
import java.util.Set;

public abstract class Database {

    public abstract void put(String playerKey, String key, Integer value);

    public abstract boolean has(String key);

    public abstract Set<String> getKeys();

    public abstract ResultSet get(String key);

    public abstract ResultSet get(String playerKey, String key);

    public abstract void delete(String playerKey);

    public abstract void clear();

}
