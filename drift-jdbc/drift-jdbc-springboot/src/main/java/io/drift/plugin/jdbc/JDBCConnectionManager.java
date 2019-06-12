package io.drift.plugin.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.drift.jdbc.domain.system.JDBCConnectionDetails;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class JDBCConnectionManager {

    private Map<String, DataSource> dataSources = new HashMap<>();

    public DataSource getDataSource(JDBCConnectionDetails connectionDetails) {
        String key = connectionDetails.getJdbcUrl();
        DataSource dataSource = dataSources.get(key);
        if (dataSource == null) {

            HikariConfig config = new HikariConfig();
            HikariDataSource hikariDataSource;

            config.setJdbcUrl(connectionDetails.getJdbcUrl());
            config.setUsername(connectionDetails.getUserName());
            config.setPassword(connectionDetails.getPassword());
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            hikariDataSource = new HikariDataSource(config);


            dataSource = hikariDataSource;
            dataSources.put(key, dataSource);
        }
        return dataSource;

    }


}
