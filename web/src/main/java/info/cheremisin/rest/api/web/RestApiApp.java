package info.cheremisin.rest.api.web;

import info.cheremisin.rest.api.db.connection.ConnectionPool;
import info.cheremisin.rest.api.db.connection.DbInitializer;
import info.cheremisin.rest.api.db.exceptions.UserNotFoundException;
import org.eclipse.jetty.http.HttpStatus;
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

        notFound((req, res) -> res.body() != null ? res.body() : "Something went wrong");

        exception(UserNotFoundException.class, (e, req, res) -> {
            log.error(e.getMessage());
            res.body(e.getMessage());
            res.status(HttpStatus.NOT_FOUND_404);
        });
        exception(Exception.class, (e, req, res) -> {
            log.error(e.getMessage());
        });


        ConnectionPool.showDb();
    }


}

