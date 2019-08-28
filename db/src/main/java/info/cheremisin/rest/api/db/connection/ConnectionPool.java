package info.cheremisin.rest.api.db.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class ConnectionPool {
    private final static Logger logger = Logger.getLogger(ConnectionPool.class);
    private static ResourceBundle bundle = ResourceBundle.getBundle("connection");

    private static ConnectionPool cp;

    private Sql2o sql2o;

    private ConnectionPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(bundle.getString("driver"));
        hikariConfig.setJdbcUrl(bundle.getString("url"));
        hikariConfig.setUsername(bundle.getString("user"));
        hikariConfig.setPassword(bundle.getString("password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(bundle.getString("poolSize")));
        DataSource dataSource = new HikariDataSource(hikariConfig);
        sql2o = new Sql2o(dataSource);
        setDefaultColumnMapping(sql2o);
        logger.info("Initialized connection pool");
    }

    private void setDefaultColumnMapping(Sql2o sql2o) {
        Map<String, String> colMaps = new HashMap<>();
        colMaps.put("user_id", "userId");
        colMaps.put("first_name", "firstName");
        colMaps.put("last_name", "lastName");
        colMaps.put("account_donor", "accountDonor");
        colMaps.put("account_acceptor", "accountAcceptor");

        sql2o.setDefaultColumnMappings(colMaps);
    }

    private static ConnectionPool getConnectionPool() {
        if (cp == null) {
            synchronized (ConnectionPool.class) {
                if(cp == null) {
                    cp = new ConnectionPool();
                }
            }
        }
        return cp;
    }

    public static Connection getConnection() {
        return getConnectionPool().sql2o.open();
    }

    public static Sql2o getSql2o() {
        return getConnectionPool().sql2o;
    }

}
