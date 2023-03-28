package com.sts.ims.config;

import com.sts.ims.constant.Constant;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
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

    @Autowired
    private Environment environment;


    @PostConstruct
    public void load() throws SQLException {
        map.put(DEFAULT_TENANT, dataSource);
        assembleDatasource();
    }

    private void assembleDatasource() throws SQLException {
        Connection con = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        try {
            String url = environment.getProperty("spring.datasource.url");
            String username = environment.getProperty("spring.datasource.username");
            String password = environment.getProperty("spring.datasource.password");
            String driverClass = environment.getProperty("spring.datasource.driver-class-name");
            Class.forName(driverClass);
            con = DriverManager.getConnection(url, username, password);
            stmt = con.prepareStatement("select * from data_source_detail");
            resultSet = stmt.executeQuery();
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
        } finally {
            if (con != null)
                con.close();
            if (stmt != null)
                stmt.close();
            if (resultSet != null)
                resultSet.close();
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
            try {
                assembleDatasource();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(DEFAULT_TENANT);
    }
}
