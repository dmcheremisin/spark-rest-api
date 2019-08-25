package info.cheremisin.rest.api.web;

import info.cheremisin.rest.api.db.connection.ConnectionPool;
import info.cheremisin.rest.api.db.connection.DbInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static info.cheremisin.rest.api.web.routs.UserRouts.importUserRouts;
import static spark.Spark.*;


public class RestApiApp {

    private static final Logger log = LoggerFactory.getLogger(RestApiApp.class);

    public static void main(String[] args) {
        DbInitializer stubInitializer = new DbInitializer();
        stubInitializer.initializeSchemaAndData();

        port(8080);
        importUserRouts();

        after((request, response) -> response.type("application/json; charset=UTF-8"));

        notFound((req, res) -> "Not found customer message");
        exception(Exception.class, (e, req, res) -> {
            log.error(e.getMessage());
        });

        ConnectionPool.showDb();
    }


}

