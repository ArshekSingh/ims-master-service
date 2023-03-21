package com.sas.config;

import com.sas.constants.Constant;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DataSourceBuilder extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl implements Constant {

    private final Map<String, DataSource> map = new HashMap<>();
    boolean init = false;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void load() {
        map.put(DEFAULT_TENANT, dataSource);
        assembleDatasource();
    }

    private void assembleDatasource() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/main_db", "root", "P@ssw0rd");
            PreparedStatement stmt = con.prepareStatement("select * from data_source_detail");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String dbName = resultSet.getString("name");
                String dataSourceName = "Hikari:".concat(dbName);
                log.info("initializing data sources name {}", dataSourceName);
                String dbUser = resultSet.getString("username");
                String dbPassword = resultSet.getString("password");
                String dbUrl = resultSet.getString("url");
                HikariDataSource source = new HikariDataSource();
                source.setJdbcUrl(dbUrl);
                source.setUsername(dbUser);
                source.setPassword(dbPassword);
                source.setConnectionTimeout(resultSet.getInt("time_out"));
                source.setMaximumPoolSize(resultSet.getInt("max_pool_size"));
                source.setMinimumIdle(resultSet.getInt("min_idle"));
                source.setPoolName("Hikari:".concat(dbName));
                map.put(dbName, source);
            }
        } catch (Exception exception) {
            log.error("Exception occurred while adding datasource");
        }
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(DEFAULT_TENANT);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        if (!init) {
            init = true;
            assembleDatasource();
        }
        return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(DEFAULT_TENANT);
    }
}
